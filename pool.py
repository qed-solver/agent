from __future__ import annotations

import fcntl
import json
import time
from contextlib import contextmanager
from pathlib import Path

MAX_ATTEMPTS = 2


@contextmanager
def _locked(lock_path: Path, timeout: float = 60.0):
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
                raise TimeoutError(f"could not acquire pool lock ({lock_path}) within {timeout}s")
            time.sleep(0.2)
    try:
        yield
    finally:
        fcntl.flock(fh, fcntl.LOCK_UN)
        fh.close()


class Pool:
    def __init__(self, state_path: Path):
        self.state_path = state_path
        self.lock_path = state_path.with_suffix(".lock")

    def init(self, spec_names: list[str]) -> None:
        with _locked(self.lock_path):
            if self.state_path.exists():
                return
            state = {name: {"status": "pending", "attempts": 0} for name in spec_names}
            self.state_path.write_text(json.dumps(state, indent=2))

    def claim_next(self, worker_id: str) -> str | None:
        with _locked(self.lock_path):
            state = json.loads(self.state_path.read_text())
            for name, entry in state.items():
                if entry["status"] == "pending":
                    entry["status"] = "in_progress"
                    entry["worker"] = worker_id
                    self.state_path.write_text(json.dumps(state, indent=2))
                    return name
            return None

    def mark_outcome(self, name: str, outcome: str) -> str:
        with _locked(self.lock_path):
            state = json.loads(self.state_path.read_text())
            entry = state[name]
            if outcome in ("PROVED", "SKIPPED"):
                entry["status"] = outcome
                result = outcome
            else:
                entry["attempts"] += 1
                if entry["attempts"] < MAX_ATTEMPTS:
                    entry["status"] = "pending"
                    entry.pop("worker", None)
                    result = "requeued"
                else:
                    entry["status"] = "FAILED"
                    result = "dropped"
            state[name] = entry
            self.state_path.write_text(json.dumps(state, indent=2))
            return result

    def has_pending(self) -> bool:
        with _locked(self.lock_path):
            state = json.loads(self.state_path.read_text())
            return any(e["status"] in ("pending", "in_progress") for e in state.values())

    def summary(self) -> dict:
        with _locked(self.lock_path):
            return json.loads(self.state_path.read_text())
