// SPDX-License-Identifier: MIT
pragma solidity 0.8.26;


import {Create2Special} from "./Create2Special.sol";

interface ICreate2Special {
    function create2special(bytes32 salt) external;
}

/**
 * @notice LoopContract
 */
contract LoopContract {

    address create2SpecialAddr;

/*
   constructor(address create2SpecialAddress, bytes32 salt) {
        create2Special = Create2Special(create2SpecialAddress);
        create2Special.create2special(salt);
    }
     */

    function storeCode(address addr) public {
        create2SpecialAddr = addr;
    }

    function callBackB(bytes32 salt) public {
        ICreate2Special(create2SpecialAddr).create2special(salt);
    }


}
