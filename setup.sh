#!/usr/bin/env bash
set -euo pipefail

ROOT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
VENDOR_DIR="$ROOT_DIR/vendor"

echo "== Checking prerequisites =="
for cmd in java cargo git python3; do
    command -v "$cmd" >/dev/null 2>&1 || { echo "Missing required command: $cmd"; exit 1; }
done
if ! rustup toolchain list 2>/dev/null | grep -q nightly && ! cargo +nightly --version >/dev/null 2>&1; then
    echo "Missing required Rust nightly toolchain (rustup toolchain install nightly)"
    exit 1
fi
if ! command -v brew >/dev/null 2>&1; then
    echo "Homebrew not found — this script assumes macOS + Homebrew for z3/cvc5 paths."
    echo "On another platform, install z3 and cvc5 yourself, then run the two vendor"
    echo "clone/build steps below by hand with the right include/lib paths."
    exit 1
fi
for pkg in z3 cvc5; do
    brew list "$pkg" >/dev/null 2>&1 || { echo "Installing $pkg via Homebrew..."; brew install "$pkg"; }
done

mkdir -p "$VENDOR_DIR"

echo "== Setting up RuleScript DSL + Maven project =="
if [ -d "$VENDOR_DIR/rulescript-repo" ]; then
    echo "vendor/rulescript-repo already exists, skipping clone."
else
    git clone --branch dsl https://github.com/qed-solver/parser.git "$VENDOR_DIR/rulescript-repo"
    (cd "$VENDOR_DIR/rulescript-repo" && git apply "$ROOT_DIR/docs/baseline-setup.patch")
fi
(cd "$VENDOR_DIR/rulescript-repo" && ./mvnw -q compile)

echo "== Setting up QED prover =="
if [ -d "$VENDOR_DIR/qed-prover" ]; then
    echo "vendor/qed-prover already exists, skipping clone."
else
    git clone https://github.com/qed-solver/prover.git "$VENDOR_DIR/qed-prover"
fi
(
    cd "$VENDOR_DIR/qed-prover"
    export Z3_SYS_Z3_HEADER="$(brew --prefix z3)/include/z3.h"
    export CPATH="$(brew --prefix z3)/include"
    export LIBRARY_PATH="$(brew --prefix z3)/lib"
    cargo +nightly build --release
)

echo
echo "== Done =="
echo "vendor/rulescript-repo/mvnw: $([ -f "$VENDOR_DIR/rulescript-repo/mvnw" ] && echo OK || echo MISSING)"
echo "vendor/qed-prover/target/release/qed-prover: $([ -f "$VENDOR_DIR/qed-prover/target/release/qed-prover" ] && echo OK || echo MISSING)"
