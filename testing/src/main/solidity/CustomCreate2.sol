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

    function create2WithInitCodeC() public {
        deployWithCreate2(salt, initCodeC);
    }

    function create2WithCallBackAfterCreate2() public {
        address addC = deployWithCreate2(salt, initCodeC);
        IContractC(addC).callBackCustomCreate2(address(this));
    }

    function create2WithImmediateCallBack() public {
        deployWithCreate2(salt, initCodeCWithImmediateCallBack);
    }

    function create2WithStaticCall(uint256 gas) view public {
        bytes memory executePayload = abi.encodeWithSignature("create2WithInitCodeC()");
        doStaticCall(address(this), executePayload, gas, 1);
    }

    function revertOnDemand() pure public {
        revert();
    }

    function selfDestructOnDemand() public {
        address payable thisAddr = payable(address(this));
        selfdestruct(thisAddr);
    }


}
