from __future__ import annotations

import fcntl
import shutil
import time
from contextlib import contextmanager
from pathlib import Path

WORKSPACE_COPY_IGNORE = shutil.ignore_patterns("target", ".git")


def make_workspace(main_repo: Path, workspaces_dir: Path, rule_name: str) -> Path:
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
