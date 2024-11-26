# Definitions

Trivial CREATE's are those creates that are
- unexceptional
- don't abort
- don't raise the failure condition **F**
- but deploy with empty initialization code

# Parameters

In terms of the parameters we can play with we find:
- CREATE vs CREATE2
- zero value vs nonzero value
- WILL_REVERT vs WONT_REVERT
- root level or higher level
- zero size, huge offset shenanigans

We _could_ also consider collisions via CREATE2 or through multi transactions and SELFDESTRUCT's.
This will be the focus of other tests. We should also measure the interaction that these trivial deployments have with EXTCODEHASH.
Furthermore, we should see how these deployments squash any return data (if present.) And then there is obviously the question of deployment numbers, deployment statuses.

- EXTCODEHASH before and after
- squashing of return data and RETURNDATASIZE
- warmth post deployment (EXTCODEXXX + BALANCE + CALL's + SELFDESTRUCT heir + ...)
