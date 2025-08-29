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
    event CalledCreate2WithInitCodeC();
    event CalledCreate2WithInitCodeCNoValue();

    function storeInitCodeC(bytes memory code) public {
        initCodeC = code;
    }

    function storeSalt(bytes32 saltEx) public {
        salt = saltEx;
    }

    // Custom CREATE2 methods

    function create2WithInitCodeC() public payable {
        address addC = deployWithCreate2(salt, initCodeC);
        addContractC = addC;
        emit CalledCreate2WithInitCodeC();
    }

    function create2WithInitCodeCNoValue() public payable {
        address addC = deployWithCreate2_withValueNoRevert(salt, initCodeC, 0);
        addContractC = addC;
        emit CalledCreate2WithInitCodeCNoValue();
    }

    function create2WithCallBackAfterCreate2() public payable {
        address addC = deployWithCreate2(salt, initCodeC);
        IContractC(addC).callBackCustomCreate2(address(this));
    }

    function create2WithCallCtoCallbackNoValue() public payable {
        address addC = deployWithCreate2_withValueNoRevert(salt, initCodeC, 0);
        addContractC = addC;
        callContractC(
            abi.encodeWithSignature("callBackCustomCreate2(address)", address(this)),
            false
        );
    }

    function create2CallCAndRevert() public payable {
        address addC = deployWithCreate2(salt, initCodeC);
        addContractC = addC;
        addC.call(
            abi.encodeWithSignature("storeInMap(uint256,address)", msg.value, addC)
        );
        revertOnDemand();
    }

    function create2FourTimes() public payable {
        uint256 max = type(uint256).max;
        deployWithCreate2_withValueNoRevert(salt, initCodeC, max);
        deployWithCreate2_withValueNoRevert(salt, initCodeC, 0);
        deployWithCreate2_withValueNoRevert(salt, initCodeC, max);
        deployWithCreate2_withValueNoRevert(salt, initCodeC, 0);
    }

    function create2FourTimesWithRevert() public payable {
        uint256 max = type(uint256).max;
        deployWithCreate2_withValueNoRevert(salt, initCodeC, max);
        deployWithCreate2_withValueNoRevert(salt, initCodeC, 0);
        deployWithCreate2_withValueNoRevert(salt, initCodeC, max);
        deployWithCreate2_withValueNoRevert(salt, initCodeC, 0);
        revert();
    }

    // Behavior on demand

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

    function advancedCreateScenariiOneTx(bytes memory code, bytes32 saltEx) public payable{
        storeInitCodeC(code);
        storeSalt(saltEx);
        callMyself(
            abi.encodeWithSignature("create2FourTimesWithRevert()"),
            false
        );
        callMyself(
            abi.encodeWithSignature("create2WithInitCodeCNoValue()"),
            true
        );
        callMyself(
            abi.encodeWithSignature("create2CallCAndRevert()"),
            false
        );
        callMyself(
            abi.encodeWithSignature("create2WithCallCtoCallbackNoValue()"),
            false
        );
        callContractC(
            abi.encodeWithSignature("storeInMap(uint256,address)", 1, "0x0000000000000000000000000000000000001234"),
            false
        );
        callContractC(
            abi.encodeWithSignature("selfDestructOnDemand()"),
            false
        );
    }

}
