# RuleScript DSL reference

**RuleScript** is an engine-agnostic DSL for expressing logical query-plan
rewrite rules: a rule is a pair of patterns, `before()` and `after()`, built
from a small relational-algebra-inspired core language plus *uninterpreted
symbols* (tables, predicates, projections, join kinds, types) standing in
for anything schema- or engine-specific. **QED** is the prover behind it: it
translates both patterns into semiring ("Q-expression") semantics and uses
an SMT solver to decide bag-semantic equivalence — for *every* instantiation
of the uninterpreted symbols, not a sampled test. Both are described in the
papers in `docs/` (`rulescript.pdf`, `qed.pdf`); the actual Java source
(`RelRN.java`, `RexRN.java`, `RRule.java`, `RelType.java`,
`JSONSerializer.java`, under `src/main/java/org/qed/`) is the ground truth
for the exact API — read it (directly below, or via your tools if you have
them) rather than guessing at a method signature.

You are porting one rewrite rule from an external backend (Apache Calcite,
CockroachDB's Optgen, Apache DataFusion, ...) into this DSL, then checking
QED's verdict.

## What you produce

A single file, `org/qed/RRuleInstances/<RuleName>.java`:

```
package org.qed.RRuleInstances;

import org.qed.RRule;
import org.qed.RelRN;
import org.qed.RexRN;
// (only import what you actually use)

// SCOPE: FULL
public record <RuleName>() implements RRule {
    // pattern symbols shared between before() and after()

    @Override
    public RelRN before() { ... }

    @Override
    public RelRN after() { ... }
}
```

`<RuleName>` is a PascalCase identifier matching the file name; the record
takes no components.

## The core language

`RelRN` (relational patterns) and `RexRN` (scalar patterns over the rows
flowing through them) together give you table scans, filter, project, join
(with the usual kinds), union/intersect/minus, the empty relation, and
group-by/aggregate — see `RelRN.java`/`RexRN.java` for the exact methods
and how uninterpreted predicates/projections/join-conditions/tables get
introduced (by name — reusing a name is how you tell QED two occurrences
are the same symbol).

One worked example, ported from Calcite's `FilterMergeRule`
(`Filter(Q, Filter(P, R))` ⟹ `Filter(P AND Q, R)`), to show the shape:

```java
package org.qed.RRuleInstances;

import org.qed.RRule;
import org.qed.RelRN;
import org.qed.RexRN;

public record FilterMerge() implements RRule {
    static final RelRN source = RelRN.scan("Source", "Source_Type");
    static final RexRN inner = source.pred("inner");
    static final RexRN outer = source.pred("outer");

    @Override
    public RelRN before() {
        return source.filter(inner).filter(outer);
    }

    @Override
    public RelRN after() {
        return source.filter(RexRN.and(inner, outer));
    }
}
```

Almost no other rule has this exact shape — treat it as evidence of how the
pieces fit together, not a template to copy. For anything less direct,
read the actual DSL source and reason from it.

## What QED can and can't prove

QED is a real decision procedure operating on bag semantics with
uninterpreted functions — a "provable" result is a genuine universal proof.
Per its own evaluation (see `qed.pdf`), it does not model list/ordering
semantics (`Sort`/`Limit`/`Offset`/`Window`/`Sample` have no bag-semantic
meaning), knows nothing about a specific aggregate function's algebra
beyond bag equality of its input, only models the set (not bag) variant of
`INTERSECT`/`MINUS`, and can't reason about predicate inference/entailment
between independent symbols or a backend operator's bespoke internal
semantics. If a rule's correctness genuinely rests on one of these, that's
a real limitation, not a missing feature to work around.

Not every real rule is expressible in full generality, and that's fine — a
genuine, non-trivial special case of a rule is a worthwhile result even
when the fully general version isn't provable. But check *why* generality
is out of reach before accepting a narrower rule: if the gap is a missing
DSL capability (the language just doesn't expose an operator/shape it
otherwise could — see "Hard boundary" below) rather than a fundamental
limitation of QED itself, try closing the gap with `extend_dsl_file`
first. Reach for a narrower special case only when extending isn't
feasible (the gap turns out to be a real QED limitation, or a genuine
attempt to extend doesn't pan out) — not the other way around.

Once QED reports a rule provable, deliberately decide — don't leave it
implicit — whether your encoding covers the rule in **full generality** or
only a **narrower special case** (i.e. did you assume anything, anywhere,
that the original rule doesn't actually require?), and say so with the
file's required first line: exactly `// SCOPE: FULL`, or
`// SCOPE: PARTIAL — <one-sentence condition you assumed>` if narrower.
Keep it to that one line — this is a tag to be checked at a glance, not an
essay. Likewise, if you conclude a rule is unsupported, your
`UNSUPPORTED: <reason>` line is the
permanent record of why — keep it to one specific sentence naming the
actual limitation, not "it got complicated".

## Hard boundary

`RelRN.java`/`RexRN.java` have grown before (e.g. `Aggregate` was added at
some point), and `JSONSerializer.java` shows what QED's JSON format can
already carry even where there's no builder for it yet. When a rule
genuinely needs an operator or shape that isn't exposed yet, use
`extend_dsl_file` to close that gap properly — prefer this over quietly
settling for a narrower special case just because the current API happens
to fall short (read the target file in full first, and confirm the gap is
real, not a misunderstanding of what's already there). It's automatically
gated: any edit that fails to compile, or that makes even one
already-proved rule stop being provable, is reverted for you, so there's
little downside to attempting it when the gap looks real. **You must never
modify the QED prover itself** (the separate Rust project, not
reachable through any tool here) — it is the trusted, unchanging arbiter.

## Output contract

- If you can encode the rule: respond with **only** a single fenced
  ```java code block containing the complete file — no prose before or
  after.
- If it's fundamentally unsupported: respond with **only**
  `UNSUPPORTED: <reason>`.
- Given feedback from a previous attempt, fix the root cause rather than
  guessing again.
