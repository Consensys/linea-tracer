// SPDX-License-Identifier: MIT
pragma solidity 0.8.26;


import {CustomCreate2} from "./CustomCreate2.sol";

interface ICustomCreate2 {
    function create2WithImmediateCallBack() external;
    function create2WithInitCodeC() external;
}

/**
 * @notice ContractCWithConstructor
 */
contract ContractCWithConstructor {

    constructor(uint selector, address addToCallOrStore) {
        if (selector == 1) {
            storageMap[selector]=addToCallOrStore;
        } else if (selector == 2) {
            ICustomCreate2(addToCallOrStore).create2WithImmediateCallBack();
        } else if (selector == 3) {
            selfDestructOnDemand();
        } else if (selector == 4) {
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
