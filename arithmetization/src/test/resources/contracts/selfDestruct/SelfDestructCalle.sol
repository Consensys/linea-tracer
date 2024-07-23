// SPDX-License-Identifier: MIT
pragma solidity ^0.8.0;

contract SelfDestructCallee {
    bool public selfDestructInvoked = false;
    
    function invokeSelfDestruct() external {
        selfDestructInvoked = true;
        emit Bye(selfDestructInvoked);
        
        selfdestruct(payable(msg.sender));
        
        // assembly {
        // Execute the STOP opcode
        // stop()
        // }
    }

    event Bye(bool selfDestructInvoked);
}