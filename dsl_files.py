from __future__ import annotations

from pathlib import Path

DSL_FILES = [
    "RelRN.java",
    "RexRN.java",
    "RRule.java",
    "RelType.java",
    "JSONSerializer.java",
]


def load_dsl_files(repo_dir: Path) -> str:
    base = repo_dir / "src" / "main" / "java" / "org" / "qed"
    parts = [
        "# RuleScript DSL source (ground truth — current as of this run)",
        "",
        "These are the complete, current contents of the files that define the "
        "DSL. Trust these over any paraphrase, including the reference document "
        "above.",
    ]
    for name in DSL_FILES:
        path = base / name
        if not path.exists():
            continue
        parts.append(f"\n## `src/main/java/org/qed/{name}`\n")
        parts.append(f"```java\n{path.read_text().rstrip()}\n```")
    return "\n".join(parts)
