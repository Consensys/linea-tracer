# Definitions

Nontrivial CREATE's are those CREATE's that are
- unexceptional
- don't abort
- don't raise the failure condition **F**
- but deploy with nonempty initialization code

# Parameters

In terms of the parameters we can play with we find a lot, so many that breaking them up into orthogonal subsets is mandatory

The first distinction is wrt its **initiation**
- [ ] deployment transaction
- [ ] CREATE-induced deployment
- [ ] CREATE2-induced deployment

And we must explore the option where said deployment gets reverted later or not
- [ ] WILL_REVERT
- [ ] WONT_REVERT

Then **during deployment** we can play around with
- [ ] modifying its own storage during deployment phase
- [ ] (using CREATE2) calling its own creator to attempt a second CREATE2 deployment within the deployment at the current deployment address

In terms of how we **terminate deployment** we must explore those terminations that **revoke it altogether**
- [ ] exception terminates deployment
  - [ ] standard exception
  - [ ] maxCodeSizeException
  - [ ] invalidCodePrefixException
- [ ] REVERT terminates deployment, revoking it altogether

In terms of how we **terminate deployment** we must explore those terminations that **lead to (temporary) deployment**
- [ ] STOP terminates deployment (leading to empty deployment)
- [ ] SELFDESTRUCT terminates deployment (leading to empty deployment)
- [ ] RETURN terminates deployment
  - [ ] empty deployment
  - [ ] nonempty deployment

Once we are in the realm of successful deployments we must explore deployed byte code that
- [ ] allows for modifying its own storage
- [ ] allows for SELFDESTRUCT through its own code
- [ ] ability to DELEGATECALL into other accounts