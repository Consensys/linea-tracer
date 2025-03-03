// SPDX-License-Identifier: MIT
pragma solidity 0.8.26;

import {TestingBase} from "./TestingBase.sol";
import {LoopContract} from "./LoopContract.sol";

interface ILoopContract {
    function callBackB(address addB) external;
}

/**
 * @notice Create2Special
 */
contract Create2Special is TestingBase {

    // address foreignCodeAddressKey;
    bytes codeC;
    bytes codeCWithImmediateCallBack;
    bytes32 salt;

    /*
    function store(address addr) public {
        foreignCodeAddressKey = addr;
    }   */

    function storeCodeC(bytes memory code) public {
        codeC = code;
    }

    function storeCodeCWithImmediateCallBack(bytes memory codeWith) public {
        codeCWithImmediateCallBack = codeWith;
    }

    function storeSalt(bytes32 saltEx) public {
        salt = saltEx;
    }

    function create2WithCodeC() public {
        deployWithCreate2(salt, codeC);
    }

    function create2WithCallBackAfterCreate2() public {
        address addC = deployWithCreate2(salt, codeC);
        ILoopContract(addC).callBackB(address(this));
    }

    function create2WithImmediateCallBack() public {
        deployWithCreate2(salt, codeCWithImmediateCallBack);
    }

    function create2WithStaticCall(uint256 gas) view public {
        bytes memory executePayload = abi.encodeWithSignature("create2WithCodeC()");
        doStaticCall(address(this), executePayload, gas, 1);
    }

}
