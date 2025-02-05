// SPDX-License-Identifier: MIT
pragma solidity ^0.8.0;

contract FundsReceiver1 {
    receive() external payable {}

    function tipTheSender() external payable {
        uint256 refundAmount = msg.value / 2;
        (bool success, ) = msg.sender.call{value: refundAmount}("");
        require(success, "FR1 could not tip the sender");
    }
}
