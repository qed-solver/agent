from __future__ import annotations

import re
from dataclasses import dataclass
from pathlib import Path

HEADER_RE = re.compile(r"^#\s*(Name|Backend|Source)\s*:\s*(.+)$", re.IGNORECASE)
SOURCE_RANGE_RE = re.compile(r"^(?P<path>.+?):(?P<start>\d+)-(?P<end>\d+)$")


@dataclass
class RuleSpec:
    name: str
    backend: str
    source_path: str
    hint: str
    source_line_start: int | None = None
    source_line_end: int | None = None

    @property
    def description(self) -> str:
        if not self.source_path:
            parts = ["(no source path given)"]
        elif self.source_line_start is not None:
            parts = [f"Source: {self.source_path}, lines {self.source_line_start}-{self.source_line_end}"]
        else:
            parts = [f"Source: {self.source_path}"]
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
    raw_source = header.get("source", "")
    source_line_start = source_line_end = None
    m = SOURCE_RANGE_RE.match(raw_source)
    if m:
        source_path = m.group("path")
        source_line_start = int(m.group("start"))
        source_line_end = int(m.group("end"))
    else:
        source_path = raw_source
    return RuleSpec(
        name=name, backend=backend, source_path=source_path, hint=hint,
        source_line_start=source_line_start, source_line_end=source_line_end,
    )
