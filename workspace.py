"""Isolated per-rule workspaces for the shared RuleScript Maven project.

Running several rules concurrently against one shared checkout of
vendor/rulescript-repo has two real failure modes, both hit in practice:

  1. `mvnw compile` compiles the *whole* module. Any other rule's broken,
     mid-edit candidate file (completely normal mid-`try_rule` WIP state)
     makes every other concurrently-running rule's compile check fail too,
     even though its own code is fine.
  2. Any DSL edit (RelRN.java etc.) is visible to every other in-flight rule
     immediately. `finalize_dsl_changes` used to decide whether to keep/revert
     an edit by diffing against a snapshot taken when *that* rule started —
     which can't tell "I changed this" apart from "someone else changed this
     while I was running", so one rule concluding SKIPPED could silently
     revert a *different*, already-verified rule's extension.

The fix: each rule gets its own private copy of the Maven project to work in
completely undisturbed — nothing it does is visible to any other rule, or to
the shared main repo, until it has a real, QED-confirmed proof. At that
point `merge_rule_to_main` (in port_rule.py) replays exactly the DSL edits
it made — as snippet-level (old, new) pairs, the same primitive
`extend_dsl_file` already uses — onto the *current* main repo, re-gated by
the same compile + regression + audit checks, under a cross-process file
lock (`merge_lock`) so only one rule merges at a time.
"""
from __future__ import annotations

import fcntl
import shutil
import time
from contextlib import contextmanager
from pathlib import Path

# Nothing under these ever needs to travel into a workspace copy: `target/`
# is a build output (can be large, and is actively being written by whichever
# workspace last compiled), `.git/` isn't needed to run `mvnw compile`.
WORKSPACE_COPY_IGNORE = shutil.ignore_patterns("target", ".git")


def make_workspace(main_repo: Path, workspaces_dir: Path, rule_name: str) -> Path:
    """Fresh, private copy of `main_repo` for one rule to work in. Blows
    away any stale copy left over from a previous (e.g. killed) attempt at
    the same rule name."""
    dest = workspaces_dir / rule_name
    if dest.exists():
        shutil.rmtree(dest)
    dest.parent.mkdir(parents=True, exist_ok=True)
    shutil.copytree(main_repo, dest, ignore=WORKSPACE_COPY_IGNORE)
    return dest


def cleanup_workspace(workspace: Path) -> None:
    shutil.rmtree(workspace, ignore_errors=True)


@contextmanager
def merge_lock(lock_path: Path, timeout: float = 900.0):
    """Cross-process mutex (flock) around the critical section that touches
    the shared main repo, so two rules never merge into it at the same time.
    A plain in-process lock wouldn't do — each rule normally runs as its own
    OS process."""
    lock_path.parent.mkdir(parents=True, exist_ok=True)
    fh = open(lock_path, "w")
    start = time.time()
    while True:
        try:
            fcntl.flock(fh, fcntl.LOCK_EX | fcntl.LOCK_NB)
            break
        except BlockingIOError:
            if time.time() - start > timeout:
                fh.close()
                raise TimeoutError(f"could not acquire merge lock ({lock_path}) within {timeout}s")
            time.sleep(1.0)
    try:
        yield
    finally:
        fcntl.flock(fh, fcntl.LOCK_UN)
        fh.close()
