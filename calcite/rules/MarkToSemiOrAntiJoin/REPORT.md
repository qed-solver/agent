# MarkToSemiOrAntiJoin

**Status:** SKIPPED
**Source backend:** Apache Calcite
**Porter attempts used:** 60  **Verification rounds used:** 2

## Source rule (as given to the porter)

```
Source: core/src/main/java/org/apache/calcite/rel/rules/MarkToSemiOrAntiJoinRule.java
```

## Independent verifier review

**Verdict:** AGREE

The blocker is the mark join itself: LEFT_MARK is a Calcite-specific operator whose defining feature — an appended boolean marker column encoding "this left row has a match under the join condition" — is bespoke internal semantics QED's bag-semiring model has no encoding for, and it cannot be reconstructed from the core language (a LEFT join + not-null filter has the wrong bag multiplicities and no null test is expressible; a group-by/exists reconstruction collapses the left bag and would require aggregate algebra QED does not know; an uninterpreted marker symbol would make the semi/anti equivalence an unprovable entailment about an independent symbol), and the anti branch additionally rests on the "join condition is not strong" null-behavior notion QED cannot model. (The porter's recorded "reason" was actually an LLM context-overflow error before it finished checking the LEFT_MARK kind's round-trip through the prover, not a reasoned limitation analysis — but the UNSUPPORTED conclusion is correct for the fundamental reason above.)
