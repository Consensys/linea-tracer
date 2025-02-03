// SPDX-License-Identifier: MIT
pragma solidity ^0.8.0;

contract A {
    receive() external payable {}

    function tipTheSender() external payable {
        require(msg.value > 0, "A expects funds from the sender");
        uint256 refundAmount = msg.value / 2;
        (bool success, ) = msg.sender.call{value: refundAmount}("");
        require(success, "A could not tip the sender");
    }
}