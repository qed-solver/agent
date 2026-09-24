from __future__ import annotations

import re
from dataclasses import dataclass
from pathlib import Path

HEADER_RE = re.compile(r"^#\s*(Name|Backend|Source)\s*:\s*(.+)$", re.IGNORECASE)


@dataclass
class RuleSpec:
    name: str
    backend: str
    source_path: str
    hint: str

    @property
    def description(self) -> str:
        parts = [f"Source: {self.source_path}" if self.source_path else "(no source path given)"]
        if self.hint:
            parts.append(self.hint)
        return "\n\n".join(parts)


def parse_spec_file(path: Path, name_override: str | None = None, backend_override: str | None = None) -> RuleSpec:
    text = path.read_text()
    lines = text.splitlines()
    header = {}
    body_start = 0
    for i, line in enumerate(lines):
        m = HEADER_RE.match(line.strip())
        if m:
            header[m.group(1).lower()] = m.group(2).strip()
            body_start = i + 1
        elif line.strip() == "":
            body_start = i + 1
            continue
        else:
            break
    hint = "\n".join(lines[body_start:]).strip()
    name = name_override or header.get("name") or path.stem
    backend = backend_override or header.get("backend") or "unspecified backend"
    source_path = header.get("source", "")
    return RuleSpec(name=name, backend=backend, source_path=source_path, hint=hint)
