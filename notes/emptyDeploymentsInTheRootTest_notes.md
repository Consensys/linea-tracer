deploymentTransactionLeadsToEmptyDeploymentTest
===============================================

hub.tx-finalization---success-setting-sender-account-row

deploymentTransactionLeadsToNonemptyDeploymentTest
==================================================

hub-into-gas
hub.tx-finalization---failure-setting-recipient-account-row
hub.tx-finalization---failure-setting-sender-account-row

deploymentTransactionEmptyReverts
=================================

hub.tx-finalization---failure-setting-recipient-account-row
hub.tx-finalization---failure-setting-sender-account-row

deploymentTransactionNonemptyReverts
====================================

hub-into-gas
hub.tx-finalization---failure-setting-recipient-account-row
hub.tx-finalization---failure-setting-sender-account-row

createDeploysEmptyByteCode
==========================

hub-into-romlex
hub.account---the-ROMLEX-lookup-requires-nonzero-code-size
hub.create-instruction---createe-balance-operation
hub.create-instruction---createe-code-operation
hub.create-instruction---createe-nonce-operation
hub.create-instruction---creator-balance-update
hub.create-instruction---setting-GAS_NEXT
hub.tx-finalization---success-setting-sender-account-row

createDeploysNonemptyByteCode
=============================

hub-into-gas
hub.account-instruction---foreign-address-opcode---doing-account-row
hub.account-instruction---foreign-address-opcode---setting-peeking-flags
hub.account-instruction---foreign-address-opcode---undoing-account-row
hub.create-instruction---createe-balance-operation
hub.create-instruction---createe-code-operation
hub.create-instruction---createe-nonce-operation
hub.create-instruction---creator-balance-update
hub.create-instruction---final-context-row-for-deployment-failures-that-will-revert
hub.create-instruction---setting-GAS_NEXT
hub.create-instruction---setting-the-CREATE-scenario
hub.create-instruction---setting-the-module-flags-of-the-miscellaneous-row
hub.create-instruction---setting-the-next-context-number
hub.create-instruction---undoing-createe-account-operations
hub.create-instruction---undoing-createe-account-operations-failure-will-revert-case
hub.create-instruction---undoing-createe-account-operations-for-deployment-failures
hub.create-instruction---undoing-creator-account-operations
hub.create-instruction---undoing-creator-account-operations-failure-will-revert-case
hub.create-instruction---undoing-creator-account-operations-for-deployment-failures
hub.generalities---context-number-generalities
hub.tx-finalization---failure-setting-recipient-account-row
hub.tx-finalization---failure-setting-sender-account-row

createRevertsWithNonemptyReturnData
===================================

hub-into-gas
hub.account-instruction---foreign-address-opcode---doing-account-row
hub.account-instruction---foreign-address-opcode---setting-peeking-flags
hub.account-instruction---foreign-address-opcode---undoing-account-row
hub.create-instruction---createe-balance-operation
hub.create-instruction---createe-code-operation
hub.create-instruction---createe-nonce-operation
hub.create-instruction---creator-balance-update
hub.create-instruction---final-context-row-for-deployment-failures-that-will-revert
hub.create-instruction---setting-GAS_NEXT
hub.create-instruction---setting-the-CREATE-scenario
hub.create-instruction---setting-the-module-flags-of-the-miscellaneous-row
hub.create-instruction---setting-the-next-context-number
hub.create-instruction---undoing-createe-account-operations
hub.create-instruction---undoing-createe-account-operations-failure-will-revert-case
hub.create-instruction---undoing-createe-account-operations-for-deployment-failures
hub.create-instruction---undoing-creator-account-operations
hub.create-instruction---undoing-creator-account-operations-failure-will-revert-case
hub.create-instruction---undoing-creator-account-operations-for-deployment-failures
hub.generalities---context-number-generalities
hub.tx-finalization---failure-setting-recipient-account-row
hub.tx-finalization---failure-setting-sender-account-row


hub.account---the-ROMLEX-lookup-requires-nonzero-code-size
hub.create-instruction---setting-the-CREATE-scenario---not-rebuffed-nonempty-init-code
hub.create-instruction---creator-balance-update
hub.account-instruction---foreign-address-opcode---doing-account-row
hub.generalities---context-number-generalities
hub-into-romlex
hub-into-gas
hub.account-instruction---foreign-address-opcode---undoing-account-row
hub.create-instruction---setting-GAS_NEXT
hub.create-instruction---undoing-createe-account-operations-for-deployment-failures
hub.tx-finalization---failure-setting-sender-account-row
hub.create-instruction---createe-balance-operation
hub.create-instruction---undoing-creator-account-operations-for-deployment-failures
hub.create-instruction---createe-nonce-operation
hub.create-instruction---createe-code-operation
hub.create-instruction---final-context-row-for-deployment-failures-that-will-revert
hub.tx-finalization---failure-setting-recipient-account-row
hub.account-instruction---foreign-address-opcode---setting-peeking-flags

createRevertsWithEmptyReturnData
================================

hub.create-instruction---createe-balance-operation
hub.create-instruction---createe-code-operation
hub.create-instruction---createe-nonce-operation
hub.create-instruction---creator-balance-update
hub.create-instruction---final-context-row-for-deployment-failures-that-wont-revert
hub.create-instruction---setting-GAS_NEXT
hub.create-instruction---setting-the-CREATE-scenario
hub.create-instruction---undoing-createe-account-operations-for-deployment-failures
hub.create-instruction---undoing-creator-account-operations-for-deployment-failures
hub.tx-finalization---success-setting-sender-account-row

TO INVESTIGATE NEXT
===================

hub.create-instruction---setting-the-CREATE-scenario
hub.generalities---context-number-generalities

RESOLVED
========

hub.return-instruction---setting-NSR
hub.create-instruction---createe-account-row-first-appearance
hub.only-one-active-scenario
hub.create-instruction---setting-the-MMU-instruction
hub.return-instruction---setting-peeking-flags
hub.return-instruction---first-account-row-for-empty-deployments
hub.return-instruction---setting-the-callers-new-return-data-empty-deployments

hub.create-instruction---final-context-row-for-deployment-successes-that-wont-revert
hub.create-instruction---setting-NSR
hub.create-instruction---forward-scenario-setting
hub.create-instruction---creator-account-first-encouter
hub.create-instruction---createe-deployment-number-operation
hub.create-instruction---createe-deployment-status-operation
hub.create-instruction---setting-the-peeking-flags
hub.create-instruction---final-context-row-when-raising-failure-condition-and-reverting
