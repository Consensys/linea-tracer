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

# Specific scenarios that we need to test

- [ ] deployment transaction
  - [ ] COINBASE is the deployment address [yes / no]
  - [ ] deployment address already has funds [yes / no]
  - [ ] deployment success [yes / no]
  - [ ] 0th transaction CALL's future deployment address with value [yes / no]
  - [ ] 2nd transaction CALL's failed deployment address with value [yes / no]
  - [ ] 2nd transaction EXTCODEHASH's failed deployment address [yes / no]
  - [ ] 2nd transaction SELFDESTRUCT's and targets failed deployment address with value [yes / no]
- [ ] same thing with CREATE-induced deployment (though this should be working fine as is)

The rationale for the last points: the recent change that we made to TX_INIT / TX_FINL solved an issue whereby
after a failed deployment transaction the deployment address would still have a nonzero nonce (≥ 1) and thus would
EXIST in the state σ after transaction end. This would have down stream effects such as
- EXTCODEHASH would not behave the same
- CALL with value may cost 25k less gas
- sending funds via SELFDESTRUCT would cost 25k less gas