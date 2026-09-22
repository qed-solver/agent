# RuleScript rule-porting agent

This repo is **an agent**, not a paper archive: it ports query-optimizer
rewrite rules from an external SQL backend (Apache Calcite, CockroachDB,
Apache DataFusion, ...) into [RuleScript](docs/rulescript.pdf) — a
Java-embedded DSL for expressing logical query-plan rewrites — and uses the
[QED prover](docs/qed.pdf) to formally check that each ported rule is
semantics-preserving (bag-semantics equivalence, proved for *all*
instantiations of its uninterpreted symbols, not just tested on samples).

## Layout

```
port_rule.py              entry point — run this
pipeline.py                compile / JSON-serialize / run qed-prover
llm_client.py               tiny stdlib-only HTTP client (Anthropic or OpenAI-style APIs)
prompts.py                  porter agent's prompts
verifier_prompts.py         verifier agent's prompts
progress.py                 PROGRESS.md / progress.json writer
spec.py                     rule-spec (input) file parser
rulescript_reference.md     the RuleScript DSL reference given to both agents
support/JsonGenerator.java  tiny helper compiled once, used to dump a rule's QED JSON

rule_specs/     <- put the rules you want ported here (one file each)
rules/          <- OUTPUT: every ported rule lands here for human inspection,
                   whatever the outcome — <Name>/<Name>.java, .json,
                   .result.json, and a REPORT.md summary
PROGRESS.md     <- OUTPUT: one-page summary of every rule's status
progress.json   <- OUTPUT: same, machine-readable

docs/           reference papers (RuleScript + QED)
vendor/         external checkouts the agent drives (not part of this repo — see Setup)
```

## What the agent actually does

Two LLM roles per rule, so a single hallucinated "yes it's provable" or "no,
unsupported" can't slip through unchecked:

1. **Porter** — given a description of the source rule, writes a candidate
   `RRule` Java record (see `rulescript_reference.md` for the exact DSL). The
   harness then mechanically: writes the file into
   `vendor/rulescript-repo/src/main/java/org/qed/RRuleInstances/`, compiles
   the Maven project, serializes the rule to QED's JSON format, and runs the
   real `qed-prover` binary on it. Compile errors and "not provable" results
   are fed back to the porter verbatim, up to `--max-attempts` retries.
2. **Verifier** — a *fresh* conversation (no memory of the porter's attempts)
   independently reviews the porter's final artifact:
   - If QED reported the rule provable, the verifier checks the encoding is
     actually *faithful* to the source rule (not trivial, not narrowed down
     to a special case, right operators/join-kinds/set-vs-bag flags, no
     silently-dropped preconditions like a needed primary key).
   - If the porter concluded the rule is unsupported (or exhausted its
     retries without a proof), the verifier independently checks that this
     is a *genuine* limitation of RuleScript/QED (see Limitations below) —
     not the porter simply failing to find a correct encoding.

   If the verifier disagrees, the porter gets another round (with the
   verifier's critique folded into its conversation) — up to
   `--max-verification-rounds` (default 2). Only a verifier-confirmed
   outcome is ever recorded as PROVED or SKIPPED; if rounds run out without
   agreement, the rule is recorded FAILED for a human to look at, with the
   full trail of reasoning attached in `rules/<Name>/REPORT.md`.

Every rule ends up in exactly one of three states, always with reasoning
attached (never a bare "no" or "yes"):

| Status | Meaning |
|---|---|
| **PROVED** | QED proved `before() == after()` for all instantiations, and the verifier confirmed the encoding is a faithful, general rendering of the source rule. |
| **SKIPPED** | The verifier agrees this rule is genuinely outside what RuleScript's core language + QED can express (e.g. it depends on row order, `Sort`/`Limit`/`Offset`/`Window`/`Sample`, an aggregate identity QED can't know, or the bag-variant of intersect/minus). This is expected and fine — see Limitations. |
| **FAILED** | Neither of the above within the round budget — needs a human to look at `rules/<Name>/REPORT.md` (it has the full source rule, the last candidate encoding, and every compiler/prover/verifier message along the way). |

**Out of scope on purpose:** the agent's job stops at "express the rule in
RuleScript and get QED's verdict." It does not generate the rewrite rule's
implementation for any concrete backend (Calcite `RelRule`, CockroachDB
Optgen, etc.) — `vendor/rulescript-repo`'s code generators for that exist
but are not invoked by this agent at all.

## Setup

Requires: Java 25 (`temurin@25` via brew works), Rust nightly, Python 3.10+,
and `z3` + `cvc5` installed (`brew install z3`; cvc5 via
[their install instructions](https://github.com/cvc5/cvc5) or `pip install
cvc5` won't give you the CLI — build from source or grab a release binary).

```sh
mkdir -p vendor && cd vendor

# 1. The RuleScript DSL + Maven project (this clone already has every
#    previously-ported rule *deleted* — see "Starting point" below — but
#    keeps all the surrounding architecture: RelRN/RexRN, RRule, JSON
#    serialization, the Calcite/CockroachDB code generators, build scripts).
git clone --branch dsl https://github.com/qed-solver/parser.git rulescript-repo
cd rulescript-repo
# Apply this project's starting-point patch: deletes every previously-ported
# rule but keeps all the architecture (see "Starting point" below).
git apply ../../docs/baseline-setup.patch
./mvnw -q compile   # sanity check
cd ../..

# 2. The QED prover itself — never modified by this agent.
cd ../
git clone https://github.com/qed-solver/prover.git qed-prover
cd qed-prover
# z3-sys needs to find the z3 headers/libs from Homebrew on macOS:
Z3_SYS_Z3_HEADER=$(brew --prefix z3)/include/z3.h \
CPATH=$(brew --prefix z3)/include \
LIBRARY_PATH=$(brew --prefix z3)/lib \
cargo +nightly build --release
```

You should end up with `vendor/rulescript-repo/mvnw` and
`vendor/qed-prover/target/release/qed-prover` both present — `port_rule.py`
checks for exactly these paths by default (override with `--repo` /
`--qed-prover`).

### Starting point: why rules are pre-deleted from `vendor/rulescript-repo`

The upstream `dsl` branch's HEAD already has 33 rules ported (that's the
paper's own evaluation). For this agent to have something to *do*, we start
from that same commit but with:

- every file under `src/main/java/org/qed/RRuleInstances/` and
  `.../UnprovableRRuleInstances/` deleted (this includes the README's own
  `FilterMerge` walkthrough example — it's documentation now, not a checked
  in rule),
- all `Generated/` backend output and per-rule `Tests/` deleted,
- the handful of hardcoded per-rule special cases inside
  `CalciteGenerator.java` / `CockroachGenerator.java` / `MySQLGenerator.java`
  / `ProxySQLGenerator.java` that referenced those deleted rules' custom
  inner classes removed (these were backend-codegen glue this agent never
  calls anyway — see "Out of scope" above),
- `pom.xml`'s Java release bumped from 23 to 25 to match locally available
  JDKs (Temurin ships 21/25, not 23).

Everything else — `RelRN`/`RexRN`/`RRule`/`RuleBuilder`, the JSON
serializer, the Calcite/CockroachDB/MySQL/ProxySQL generators' generic
dispatch, all Maven/CI scripts — is untouched.

## Running it

```sh
export ANTHROPIC_API_KEY=sk-ant-...        # or OPENAI_API_KEY with --provider openai

# one rule
python3 port_rule.py --spec rule_specs/my_rule.md

# a whole batch
python3 port_rule.py --spec-dir rule_specs

# use a different (e.g. stronger) model as the verifier than the porter
python3 port_rule.py --spec-dir rule_specs \
    --model claude-sonnet-5 --verifier-model claude-opus-5

# point at a self-hosted / third-party OpenAI-compatible endpoint instead
python3 port_rule.py --spec-dir rule_specs \
    --provider openai --endpoint https://your-host/v1/chat/completions --api-key ...

# smoke-test the compile/JSON/prove/publish plumbing with no LLM at all,
# by handing the porter's first "reply" a file directly (still goes through
# the real compiler and the real qed-prover; add --no-verifier to also skip
# the review step for a pure infra check)
python3 port_rule.py --spec rule_specs/example_filter_merge.md \
    --offline-code some_handwritten_rule.java --no-verifier
```

### Writing a rule-spec file

Anything under `rule_specs/*.md` or `*.txt`. A tiny optional header, then
freeform content (paste the original rule's source code, a before/after SQL
example, or just describe it in prose — the porter is a capable reader):

```markdown
# Name: FilterMerge
# Backend: Apache Calcite
# Source: core/src/main/java/org/apache/calcite/rel/rules/FilterMergeRule.java

<the rule's source / description goes here>
```

See `rule_specs/example_filter_merge.md` for a filled-in example.

## Limitations the agent is told about up front

Ported straight from `rulescript_reference.md` (which is also literally the
system prompt both agents read) — QED is a real SMT-backed decision
procedure, not a heuristic, but it does not model:

- **Row order / list semantics**: `Sort`, `Order By`, `Limit`, `Offset`,
  `Fetch`, `Sample`, `Window` functions. If a rule's correctness genuinely
  depends on one of these, it is out of scope and should be SKIPPED, not
  forced into a bag-semantics encoding that lies about what was checked.
- **Aggregate algebraic identities**: aggregates are uninterpreted black
  boxes to QED; it can prove a lot of aggregate-pushdown/regrouping rules
  (because that reduces to bag equality of the aggregate's *input*), but not
  rules that need to know what `SUM`/`COUNT`/etc. specifically compute.
- **Bag-variant `INTERSECT`/`MINUS`** (only the set/duplicate-eliminating
  forms are modeled; `UNION ALL` is fully supported).
- **Implicit type casts** and backend-specific opaque operators whose
  correctness depends on their internal semantics (e.g. Calcite's `SEARCH`).

When a rule hits one of these, the expected, correct outcome is **SKIPPED
with clear reasoning** attached (both the porter's and the independent
verifier's) — that's success for this agent, not failure. Read
`rules/<Name>/REPORT.md` for the full reasoning trail on any SKIPPED or
FAILED rule.
