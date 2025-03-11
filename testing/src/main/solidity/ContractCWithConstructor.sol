// SPDX-License-Identifier: MIT
pragma solidity 0.8.26;


import {CustomCreate2} from "./CustomCreate2.sol";

interface ICustomCreate2 {
    function create2WithInitCodeC() external;
}

/**
 * @notice ContractCWithConstructor
 */
contract ContractCWithConstructor {

    constructor() payable {
        uint256 value = msg.value;
        // deployment is done by CustomCreate2
        address from = msg.sender;
        if (value == 1) {
            storageMap[value]=from;
        } else if (value == 2) {
            ICustomCreate2(from).create2WithInitCodeC();
        } else if (value == 3) {
            selfDestructOnDemand();
        } else if (value == 4) {
            revertOnDemand();
        }
    }

    mapping(uint => address) public storageMap;

    function storeInMap(uint key, address add) public {
        storageMap[key] = add;
    }

    function callBackCustomCreate2(address addCustomCreate2) public {
        ICustomCreate2(addCustomCreate2).create2WithInitCodeC();
    }

    function revertOnDemand() public {
        revert();
    }

    function selfDestructOnDemand() public {
        address payable thisAddr = payable(address(this));
        selfdestruct(thisAddr);
    }

    // Future usage

    /* function getDeployedCode() view public returns (bytes memory) {
        return address(this).code;
    } */

}
