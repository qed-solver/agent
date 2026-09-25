from __future__ import annotations

import json
import os
import re
import urllib.error
import urllib.request
from dataclasses import dataclass, field
from typing import Literal

_THINK_BLOCK_RE = re.compile(r"<think>.*?</think>", re.DOTALL | re.IGNORECASE)


def _strip_thinking(text: str) -> str:
    return _THINK_BLOCK_RE.sub("", text).strip()


@dataclass
class ToolCall:
    id: str
    name: str
    arguments: dict


@dataclass
class AgentTurn:
    content: str
    tool_calls: list[ToolCall]


def format_assistant_message(provider: str, turn: AgentTurn) -> dict:
    if provider == "openai":
        msg = {"role": "assistant", "content": turn.content or None}
        if turn.tool_calls:
            msg["tool_calls"] = [
                {"id": tc.id, "type": "function",
                 "function": {"name": tc.name, "arguments": json.dumps(tc.arguments)}}
                for tc in turn.tool_calls
            ]
        return msg
    content = []
    if turn.content:
        content.append({"type": "text", "text": turn.content})
    for tc in turn.tool_calls:
        content.append({"type": "tool_use", "id": tc.id, "name": tc.name, "input": tc.arguments})
    return {"role": "assistant", "content": content}


def format_tool_results_message(provider: str, results: list[tuple[ToolCall, dict]]) -> dict:
    if provider == "openai":
        return {
            "role": "__multi_tool__",
            "messages": [
                {"role": "tool", "tool_call_id": tc.id, "content": json.dumps(result)}
                for tc, result in results
            ],
        }
    return {
        "role": "user",
        "content": [
            {"type": "tool_result", "tool_use_id": tc.id, "content": json.dumps(result)}
            for tc, result in results
        ],
    }

Provider = Literal["anthropic", "openai"]

DEFAULT_ENDPOINTS = {
    "anthropic": "https://api.anthropic.com/v1/messages",
    "openai": "https://api.openai.com/v1/chat/completions",
}

DEFAULT_MODELS = {
    "anthropic": "claude-sonnet-5",
    "openai": "gpt-4o",
}

ENV_KEYS = {
    "anthropic": ["RULESCRIPT_AGENT_API_KEY", "ANTHROPIC_API_KEY"],
    "openai": ["RULESCRIPT_AGENT_API_KEY", "OPENAI_API_KEY"],
}


class LLMError(RuntimeError):
    pass


@dataclass
class LLMClient:
    provider: Provider = "anthropic"
    model: str | None = None
    api_key: str | None = None
    endpoint: str | None = None
    max_tokens: int = 8192
    timeout: float = 600.0
    extra_headers: dict = field(default_factory=dict)

    def __post_init__(self):
        if self.model is None:
            self.model = DEFAULT_MODELS[self.provider]
        if self.endpoint is None:
            self.endpoint = DEFAULT_ENDPOINTS[self.provider]
        if self.api_key is None:
            for env_name in ENV_KEYS[self.provider]:
                if os.environ.get(env_name):
                    self.api_key = os.environ[env_name]
                    break
        if not self.api_key:
            if self.endpoint != DEFAULT_ENDPOINTS[self.provider]:
                self.api_key = "EMPTY"
            else:
                raise LLMError(
                    f"No API key found for provider={self.provider!r}. Pass --api-key "
                    f"or set one of {ENV_KEYS[self.provider]}."
                )

    def complete(self, system: str, messages: list[dict]) -> str:
        original_max_tokens = self.max_tokens
        try:
            while True:
                try:
                    if self.provider == "anthropic":
                        return self._complete_anthropic(system, messages)
                    elif self.provider == "openai":
                        return self._complete_openai(system, messages)
                    raise LLMError(f"Unknown provider {self.provider!r}")
                except LLMError as e:
                    if "maximum context length" in str(e).lower() and self.max_tokens > 1024:
                        self.max_tokens = max(1024, self.max_tokens // 2)
                        print(f"   [llm] context length exceeded, retrying with max_tokens={self.max_tokens}")
                        continue
                    raise
        finally:
            self.max_tokens = original_max_tokens


    def step(self, system: str, messages: list[dict], tools: list[dict]) -> AgentTurn:
        original_max_tokens = self.max_tokens
        try:
            while True:
                try:
                    if self.provider == "anthropic":
                        return self._step_anthropic(system, messages, tools)
                    elif self.provider == "openai":
                        return self._step_openai(system, messages, tools)
                    raise LLMError(f"Unknown provider {self.provider!r}")
                except LLMError as e:
                    if "maximum context length" in str(e).lower() and self.max_tokens > 1024:
                        self.max_tokens = max(1024, self.max_tokens // 2)
                        print(f"   [llm] context length exceeded, retrying with max_tokens={self.max_tokens}")
                        continue
                    raise
        finally:
            self.max_tokens = original_max_tokens

    def assistant_message(self, turn: AgentTurn) -> dict:
        return format_assistant_message(self.provider, turn)

    def tool_results_message(self, results: list[tuple[ToolCall, dict]]) -> dict:
        return format_tool_results_message(self.provider, results)

    def _step_anthropic(self, system: str, messages: list[dict], tools: list[dict]) -> AgentTurn:
        headers = {
            "content-type": "application/json",
            "x-api-key": self.api_key,
            "anthropic-version": "2023-06-01",
            **self.extra_headers,
        }
        anthropic_tools = [
            {"name": t["name"], "description": t["description"], "input_schema": t["parameters"]}
            for t in tools
        ]
        body = {
            "model": self.model, "max_tokens": self.max_tokens, "system": system,
            "messages": messages, "tools": anthropic_tools,
        }
        resp = self._post(headers, body)
        if "content" not in resp:
            raise LLMError(f"Unexpected Anthropic response: {resp}")
        text = "".join(b.get("text", "") for b in resp["content"] if b.get("type") == "text")
        tool_calls = [
            ToolCall(id=b["id"], name=b["name"], arguments=b.get("input", {}))
            for b in resp["content"] if b.get("type") == "tool_use"
        ]
        return AgentTurn(content=text, tool_calls=tool_calls)

    def _step_openai(self, system: str, messages: list[dict], tools: list[dict]) -> AgentTurn:
        headers = {
            "content-type": "application/json",
            "Authorization": f"Bearer {self.api_key}",
            **self.extra_headers,
        }
        openai_tools = [{"type": "function", "function": t} for t in tools]
        body = {
            "model": self.model, "max_tokens": self.max_tokens,
            "messages": [{"role": "system", "content": system}, *messages],
            "tools": openai_tools,
        }
        resp = self._post(headers, body)
        try:
            choice = resp["choices"][0]
            message = choice["message"]
        except (KeyError, IndexError) as e:
            raise LLMError(f"Unexpected OpenAI-style response: {resp}") from e
        raw_calls = message.get("tool_calls") or []
        tool_calls = []
        for tc in raw_calls:
            try:
                args = json.loads(tc["function"]["arguments"])
            except (KeyError, json.JSONDecodeError):
                args = {}
            tool_calls.append(ToolCall(id=tc["id"], name=tc["function"]["name"], arguments=args))
        truncated = choice.get("finish_reason") == "length"
        raw_reasoning = message.get("reasoning_content") or message.get("reasoning") or ""
        content = _strip_thinking(message.get("content") or "")
        if not content and not tool_calls:
            content = _strip_thinking(raw_reasoning)
        if truncated and not tool_calls:
            content += "\n\n[NOTE: the above reasoning was truncated at the token limit before finishing — continue from here instead of starting over.]"
        return AgentTurn(content=content, tool_calls=tool_calls)

    def _post(self, headers: dict, body: dict) -> dict:
        data = json.dumps(body).encode("utf-8")
        req = urllib.request.Request(self.endpoint, data=data, headers=headers, method="POST")
        try:
            with urllib.request.urlopen(req, timeout=self.timeout) as resp:
                return json.loads(resp.read().decode("utf-8"))
        except urllib.error.HTTPError as e:
            detail = e.read().decode("utf-8", errors="replace")
            raise LLMError(f"HTTP {e.code} from {self.endpoint}: {detail}") from e
        except urllib.error.URLError as e:
            raise LLMError(f"Failed to reach {self.endpoint}: {e}") from e
        except TimeoutError as e:
            raise LLMError(f"Request to {self.endpoint} timed out after {self.timeout}s: {e}") from e

    def _complete_anthropic(self, system: str, messages: list[dict]) -> str:
        headers = {
            "content-type": "application/json",
            "x-api-key": self.api_key,
            "anthropic-version": "2023-06-01",
            **self.extra_headers,
        }
        body = {
            "model": self.model,
            "max_tokens": self.max_tokens,
            "system": system,
            "messages": messages,
        }
        resp = self._post(headers, body)
        if "content" not in resp:
            raise LLMError(f"Unexpected Anthropic response: {resp}")
        return "".join(
            block.get("text", "") for block in resp["content"] if block.get("type") == "text"
        )

    def _complete_openai(self, system: str, messages: list[dict]) -> str:
        headers = {
            "content-type": "application/json",
            "Authorization": f"Bearer {self.api_key}",
            **self.extra_headers,
        }
        body = {
            "model": self.model,
            "max_tokens": self.max_tokens,
            "messages": [{"role": "system", "content": system}, *messages],
        }
        resp = self._post(headers, body)
        try:
            choice = resp["choices"][0]
            message = choice["message"]
        except (KeyError, IndexError) as e:
            raise LLMError(f"Unexpected OpenAI-style response: {resp}") from e
        truncated = choice.get("finish_reason") == "length"
        content = _strip_thinking(message.get("content") or "")
        if not content and not truncated:
            content = _strip_thinking(message.get("reasoning_content") or message.get("reasoning") or "")
        if truncated:
            content += (
                "\n\n[NOTE: response was truncated at the token limit before finishing — "
                "if this cut off mid-code-block, that's why it couldn't be parsed.]"
            )
        return content
