# Definitions

Failure Condition **F** CREATE's are those CREATE's that
- are unexceptional
- don't abort
- but raise Failure Condition **F**

Recall that this condition is triggered when the deployment address has one or more of the following properties
- nonzero nonce
- nonempty code

**Note.** Nonzero balance isn't an issue.

There are two main ways to produce the Failure Condition **F**.
- simple: address collisions are trivial to produce via CREATE2; here we have two ways to deploy:
  - [ ] empty deployed code
  - [ ] nonempty deployed code
- harder: address collisions via CREATE

This is classical by now:
- contract A does a CREATE2 deploying contract B
  - contract B should be able to do a SELFDESTRUCT / CREATE
- contract B does a CREATE deploying contract C while it has nonce n (e.g. n = 1)
- contract B selfdestructs
- contract A re-deploys contract B using CREATE2 again
- contract B attempts a CREATE at the same nonce n as previously, effectively attempting to re-deploy at contract C's address

I guess the simplest way to achieve this would be to have B have the following code

```asm
// contract B deployed code
PUSH1 0
PUSH1 0
PUSH1 0
PUSH1 0
PUSH1 0
CALLDATALOAD  // expecting to find an address in call data
GAS
DELEGATECALL
```

This byte code measures 13 bytes. The associated init code would be
```asm
PUSH13 ...
PUSH1 0
MSTORE
PUSH1 13
PUSH1 0
RETURN
```

which is 22 bytes. It should be deployed in a CREATE2 with 1 Wei of value.

We then need two contracts:
- one that simply selfdestructs
- another one that CREATEs.

E.g.

```asm
// basic CREATE-or contract (deploys 0x)
PUSH1 0
PUSH1 0
PUSH1 0
CREATE
```
or
```asm
// alternative basic CREATE-or contract (deploys 0x00 .. 00)
PUSH1 32
PUSH1 0
PUSH1 0
CREATE
```

```asm
// basic SELFDESTRUCT-or
ORIGIN
SELFDESTRUCT
```

The simplest scenario would be
- transaction 1:
  - to contract A
  - deploy contract B
  - get back to A
  - CALL contract B with call data the address of the basic CREATE-or
  - CALL contract B with call data the address of the basic SELFDESTRUCT-or
- transaction 2:
  - to contract A
  - deploy contract B (which is clean)
  - CALL contract B with call data the address of the basic CREATE-or
    - raise failure condition F
