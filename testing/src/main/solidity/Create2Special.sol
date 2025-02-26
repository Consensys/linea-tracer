// SPDX-License-Identifier: MIT
pragma solidity 0.8.26;

import {TestingBase} from "./TestingBase.sol";

/**
 * @notice Create2Special
 */
contract Create2Special is TestingBase {

    // address foreignCodeAddressKey;
    bytes code;

    /*
    function store(address addr) public {
        foreignCodeAddressKey = addr;
    }   */

    function storeCode(bytes memory co) public {
        code = co;
    }

    function create2special(bytes32 salt) public {
        bytes memory c = code;
        deployWithCreate2(salt, c);
    }

}
