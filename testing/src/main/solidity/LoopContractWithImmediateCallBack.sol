// SPDX-License-Identifier: MIT
pragma solidity 0.8.26;


import {Create2Special} from "./Create2Special.sol";

interface ICreate2Special {
    function create2WithCodeC() external;
}

/**
 * @notice LoopContractWithImmediateCallBack
 */
contract LoopContractWithImmediateCallBack {

    constructor() {
        ICreate2Special(0xd8b934580fcE35a11B58C6D73aDeE468a2833fa8).create2WithCodeC();
    }

    function callBackB(address addB) public {
        ICreate2Special(addB).create2WithCodeC();
    }


}
