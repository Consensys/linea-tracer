// SPDX-License-Identifier: MIT
pragma solidity 0.8.26;

import {TestingBase} from "./TestingBase.sol";
import {ContractC} from "./ContractC.sol";

interface IContractC {
    function callBackCustomCreate2(address addCustomCreate2) external;
}

/**
 * @notice CustomCreate2
 */
contract CustomCreate2 is TestingBase {

    address addContractC;
    bytes initCodeC;
    bytes32 salt;

    // Events
    event CallMyselfFail();
    event StaticCallMyselfFail();
    event CallContractCFail();
    event StaticCallContractCFail();
    event CallCreate2WithInitCodeC_withValue();
    event CallCreate2WithInitCodeC_noValue();

    function storeInitCodeC(bytes memory code) public {
        initCodeC = code;
    }

    function storeSalt(bytes32 saltEx) public {
        salt = saltEx;
    }

    /////////////////////////
    // Custom CREATE2 methods
    /////////////////////////

    function create2WithInitCodeC_withValueAndRevert() public payable {
        address addC = deployWithCreate2_withValueAndRevert(salt, initCodeC, msg.value);
        addContractC = addC;
        emit CallCreate2WithInitCodeC_withValue();
    }

    function create2WithInitCodeC_noValueNoRevert() public payable {
        address addC = deployWithCreate2_withValueNoRevert(salt, initCodeC, 0);
        emit CallCreate2WithInitCodeC_noValue();
        if (addC != address(0)) {
            addContractC = addC;
        }
    }

    // SCENARIO 1
    function create2FourTimes_withRevertTrigger(bool triggerRevert) public payable {
        uint256 max = type(uint256).max;
        // Attempt 1 with max value, fails
        deployWithCreate2_withValueNoRevert(salt, initCodeC, max);
        // Attempt 2 with no value, deploys the contract
        address addC = deployWithCreate2_withValueNoRevert(salt, initCodeC, 0);
        addContractC = addC;
        // We test that the contract code is not empty
        addContractC.call(
            abi.encodeWithSignature("storeInMap(uint256,address)", msg.value, addC)
        );
        // Attempt 3 with max value, fails
        deployWithCreate2_withValueNoRevert(salt, initCodeC, max);
        // Attempt 4 with no value, collision with attempt 2, fails
        deployWithCreate2_withValueNoRevert(salt, initCodeC, 0);
        if (triggerRevert) {
            revertOnDemand();
        }
    }

    // SCENARIO 3 when called with msg.value == 2
    function create2CallC_withRevertTrigger(bool revertTrigger) public payable {
        address addC = deployWithCreate2_withValueNoRevert(salt, initCodeC, msg.value);
        addContractC = addC;
        addC.call(
            abi.encodeWithSignature("storeInMap(uint256,address)", msg.value, addC)
        );
        if (revertTrigger) {
            revertOnDemand();
        }
    }

    // SCENARIO 4
    function create2WithCallCtoCallback_noValue() public payable {
        address addC = deployWithCreate2_withValueNoRevert(salt, initCodeC, 0);
        addContractC = addC;
        callContractC(
            abi.encodeWithSignature("callBackCustomCreate2(address)", address(this)),
            false
        );
    }

    // SCENARIO 5
    function callCToModifyStorageAndSelfdestruct() public payable {
        callContractC(
            abi.encodeWithSignature("storeInMap(uint256,address)", 1, "0x0000000000000000000000000000000000001234"),
            false
        );
        callContractC(
            abi.encodeWithSignature("selfDestructOnDemand()"),
            false
        );
    }

    /////////////////////
    // Behavior on demand
    /////////////////////

    function revertOnDemand() public {
        revert();
    }

    function selfDestructOnDemand() public {
        address payable thisAddr = payable(address(this));
        selfdestruct(thisAddr);
    }

    function callMyself(bytes memory executePayload, bool staticCall) public {
        bool success;
        if (staticCall) {
            success = doStaticCall(address(this), executePayload, 1000000, 0);
            if (!success) {
                emit StaticCallMyselfFail();
            }
        } else {
            success = doCall(address(this), executePayload, 1000000, 0);
            if (!success) {
                emit CallMyselfFail();
            }
        }
    }

    // Call Contract C
    function callContractC(bytes memory executePayload, bool staticCall) public {
        bool success;
        if (staticCall) {
            success = doStaticCall(addContractC, executePayload, 2000000, 0);
            if (!success) {
                emit StaticCallContractCFail();
            }
        } else {
            success = doCall(addContractC, executePayload, 2000000, 0);
            if (!success) {
                emit CallContractCFail();
            }
        }
    }

    // Combine 5 scenarii in one transaction
    function advancedCreateScenariiOneTx(bytes memory code, bytes32 saltEx) public payable{
        storeInitCodeC(code);
        storeSalt(saltEx);
        callMyself(
            abi.encodeWithSignature("create2FourTimes_withRevertTrigger(bool)", true),
            false
        );
        callMyself(
            abi.encodeWithSignature("create2WithInitCodeC_withValueAndRevert()"),
            true
        );
        callMyself(
            abi.encodeWithSignature("create2CallC_withRevertTrigger(bool)", true),
            false
        );
        callMyself(
            abi.encodeWithSignature("create2WithCallCtoCallback_noValue()"),
            false
        );
        callMyself(
            abi.encodeWithSignature("callCToModifyStorageAndSelfdestruct()"),
            false
        );
    }

}
