# Name: FoldFunction
# Backend: CockroachDB
# Source: pkg/sql/opt/norm/rules/fold_constants.opt

FoldFunction is similar to FoldBinary, but it involves a function with
constant inputs. As with FoldBinary, FoldFunction applies as long as the
evaluation would not cause an error. Additionally, only certain functions
are safe to fold as part of normalization. Other functions rely on context
that may change between runs of a prepared query.

Extracted from `fold_constants.opt` (which defines multiple rules — implement specifically `FoldFunction`, not the other rules in that file):

```
# FoldFunction is similar to FoldBinary, but it involves a function with
# constant inputs. As with FoldBinary, FoldFunction applies as long as the
# evaluation would not cause an error. Additionally, only certain functions
# are safe to fold as part of normalization. Other functions rely on context
# that may change between runs of a prepared query.
[FoldFunction, Normalize]
(Function
    $args:* & (IsListOfConstants $args)
    $private:* &
        (Let ($result $ok):(FoldFunction $args $private) $ok)
)
=>
$result
```
