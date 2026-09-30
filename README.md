# RuleScript rule-porting agent

Ports query-optimizer rewrite rules from an external SQL backend (Apache
Calcite, CockroachDB, Apache DataFusion, ...) into
[RuleScript](https://github.com/qed-solver/rulescript) — a Java-embedded DSL for
logical query-plan rewrites — and uses the
[QED prover](https://github.com/qed-solver/prover) to formally check each
ported rule is semantics-preserving, for *all* instantiations, not just
tested examples.

## Layout

```
port_rule.py              entry point — run this
pipeline.py                compile / JSON-serialize / run qed-prover
dsl_audit.py                 regression-checks a shared-DSL edit against every proved rule
pool.py                     work-queue for running many rules concurrently
prompts.py / verifier_prompts.py   the two agents' prompts
progress.py                 PROGRESS.md / progress.json writer
spec.py                     rule-spec (input) file parser
rulescript_reference.md     DSL reference given to both agents

<backend>/               <- one folder per source SQL engine (calcite/, cockroach/,
                             datafusion/ exist already; adding one more backend
                             means creating a folder laid out the same way)
  rule_specs/             <- input: the rules you want ported, one file each
  rules/                  <- output: <Name>/<Name>.java, .json, .result.json, REPORT.md
  PROGRESS.md, progress.json   <- output: status of every rule, human + machine readable

docs/      reference papers (RuleScript + QED)
vendor/    the RuleScript/QED toolchain, plus a read-only checkout of each
           backend's source (see Setup)
```

## What it does

Two LLM roles per rule, so one hallucinated verdict can't slip through:

1. **Porter** writes a candidate `RRule` Java record, then the harness
   mechanically compiles it, serializes it to QED's JSON format, and runs
   the real prover. Errors and "not provable" results get fed back to the
   porter to retry.
2. **Verifier** — a fresh, independent conversation — reviews the porter's
   final answer: if QED said provable, is the encoding actually *faithful*
   (not trivial, not silently narrowed, no dropped preconditions)? If the
   porter claims unsupported, is that a *genuine* limitation (see
   Limitations) and not just giving up early? Disagreement sends it back to
   the porter for another round.

Every rule ends up **PROVED**, **SKIPPED** (genuinely out of scope), or
**FAILED**.

## Setup

Requires Java 25, Rust nightly, Python 3.10+ (macOS + Homebrew; installs
`z3`/`cvc5` for you).

```sh
./setup.sh
```

Clones and builds the two vendored projects under `vendor/`: the RuleScript
DSL/Maven project (patched via `docs/baseline-setup.patch` to reset it to
just its architecture — DSL, JSON serializer, build scripts — with every
previously-ported rule removed, so this agent has rules left to port), and
the QED prover itself (never modified by this agent). Safe to re-run — it
skips any clone that already exists. Leaves you with
`vendor/rulescript-repo/mvnw` and `vendor/qed-prover/target/release/qed-prover`
(`port_rule.py` looks for exactly these paths; override with `--repo` /
`--qed-prover` if you set things up differently).

## Running it

```sh
export ANTHROPIC_API_KEY=sk-ant-...        # or OPENAI_API_KEY with --provider openai

# one rule
python3 port_rule.py --spec calcite/rule_specs/my_rule.md

# a whole directory of specs
python3 port_rule.py --spec-dir calcite/rule_specs

# same, N workers claiming rules concurrently from a shared queue
python3 port_rule.py --spec-dir calcite/rule_specs --pool --workers 4

# a different backend: point spec-dir/progress/rules-out-dir at its folder,
# and backend-root/backend-name at its vendored source checkout
python3 port_rule.py --spec-dir cockroach/rule_specs \
    --progress-md cockroach/PROGRESS.md --progress-json cockroach/progress.json \
    --rules-out-dir cockroach/rules \
    --backend-root vendor/cockroach-src --backend-name cockroach
```

### Writing a rule-spec file

A `.md`/`.txt` file under `<backend>/rule_specs/`: a small header, then
optional freeform content.

```markdown
# Name: FilterMerge
# Backend: Apache Calcite
# Source: core/src/main/java/org/apache/calcite/rel/rules/FilterMergeRule.java

<optional: description, example — but the porter reads the real source
itself before writing anything, so prefer leaving this blank>
```

`Source:` may end in `:<start>-<end>` to point at an exact line range
instead of a whole file.

## Porting rules for a new backend

1. Vendor a read-only checkout of the backend's source under `vendor/`.
2. Create `<backend>/rule_specs/`, `<backend>/rules/`, `<backend>/progress.json` (`[]`).
3. Write one spec per rule. If a source file bundles several independent
   rewrites in one function, split it into one spec per rewrite with a
   line-range `Source:` for each.
4. Run a small trial batch first and read the `REPORT.md` files before
   committing to a full run.

## Limitations

QED does not model:

- Row order / list semantics — `Sort`, `Limit`, `Offset`, `Sample`, `Window`.
- Aggregate algebraic identities (aggregates are uninterpreted).
- Bag-variant `INTERSECT`/`MINUS` (set forms only).
- Implicit type casts and opaque backend-specific operators.

These should produce SKIPPED with clear reasoning.
