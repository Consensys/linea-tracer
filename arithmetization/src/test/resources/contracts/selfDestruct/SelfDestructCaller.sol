// SPDX-License-Identifier: MIT
pragma solidity ^0.8.0;

contract SelfDestructCaller {
    
    function staticCallToBWhenWarm(address bAddress) external {
        // Get the balance of contract B to warm it
        uint256 balance = address(bAddress).balance;
        emit BalanceOfB(balance);

        uint256 size;
        assembly {
            size := extcodesize(bAddress)
        }
        emit ExtCodeSize(size);

        // Perform the STATICCALL
        (bool success, ) = bAddress.call(abi.encodeWithSignature("invokeSelfDestruct()"));
        emit CallSuccess(success);

        assembly {
            size := extcodesize(bAddress)
        }
        emit ExtCodeSize(size);
    }

    event BalanceOfB(uint256 balance);

    event CallSuccess(bool success);

    event ExtCodeSize(uint256 size);
}
