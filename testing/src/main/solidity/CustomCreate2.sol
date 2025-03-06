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
    bytes initCodeCWithImmediateCallBack;
    bytes32 salt;

    function storeInitCodeC(bytes memory code) public {
        initCodeC = code;
    }

    function storeInitCodeCWithImmediateCallBack(bytes memory codeWith) public {
        initCodeCWithImmediateCallBack = codeWith;
    }

    function storeSalt(bytes32 saltEx) public {
        salt = saltEx;
    }

    // Custom CREATE2 methods

    function create2WithInitCodeC() public {
        address addC = deployWithCreate2(salt, initCodeC);
        addContractC = addC;
    }

    function create2WithCallBackAfterCreate2() public {
        address addC = deployWithCreate2(salt, initCodeC);
        IContractC(addC).callBackCustomCreate2(address(this));
    }

    function create2WithImmediateCallBack() public {
        deployWithCreate2(salt, initCodeCWithImmediateCallBack);
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
        if (staticCall) {
            doStaticCall(address(this), executePayload, 5000000, 0);
        } else {
            doCall(address(this), executePayload, 5000000, 0);
        }
    }


    // Call Contract C
    function callContractC (bytes memory executePayload, bool staticCall) public {
        if (staticCall) {
            doStaticCall(addContractC, executePayload, 5000000, 0);
        } else {
            doCall(addContractC, executePayload, 5000000, 0);
        }
    }

}
