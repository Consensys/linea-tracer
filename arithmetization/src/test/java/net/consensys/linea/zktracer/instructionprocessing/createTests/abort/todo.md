# Definitions

Aborted CREATE's are those CREATE's that are
- unexceptional
- attempt to provide more balance than is avaiable or happen at CSD = 1024

# Parameters

- [ ] fail because of BALANCE
- [ ] fail because of CALL_STACK_DEPTH

We can then
- [ ] do an EXTCODEHASH on the deployment address to test
  - warmth (shouldn't be warm)
  - existence (should exist or not depending on balance alone unless collision)
- [ ] two deployment tests
  - [ ] first deployment aborts with CREATE(2), a second deployment at this place doesn't abort
  - [ ] first deployment succeeds with CREATE2, a second attempt aborts before FailureCondition **F** can be raised.