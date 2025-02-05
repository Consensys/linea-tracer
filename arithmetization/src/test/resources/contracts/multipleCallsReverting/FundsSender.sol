// SPDX-License-Identifier: MIT
pragma solidity ^0.8.0;

contract FundsSender {
    enum CallCase {
        BASE,
        SEND_ALL,
        INVOKE_TIP_THE_SENDER
    }

    receive() external payable {}

    function transferFunds(
        address payable _contractFR1,
        address payable _contractFR2,
        bool _mustRevert,
        CallCase _callCase
    ) external {
        require(
            address(this).balance == 21 ether,
            "FundsSender balance must be at least 21 ether"
        );

        bool success;

        (success, ) = _contractFR1.call{value: 1 ether}("");
        require(success, "call 1 failed");

        (success, ) = address(this).call{value: 2 ether}("");
        require(success, "call 2 failed");

        (success, ) = _contractFR1.call{value: 3 ether}("");
        require(success, "call 3 failed");

        (success, ) = _contractFR2.call{value: 4 ether}("");
        require(success, "call 4 failed");

        (success, ) = address(this).call{value: 5 ether}("");
        require(success, "call 5 failed");

        if (_callCase == CallCase.BASE) {
            (success, ) = _contractFR1.call{value: 6 ether}("");
            require(success, "call 6 failed");
            /*
            FS  ends up with 2 + 5     =  7 ether
            FR1 ends up with 1 + 3 + 6 = 10 ether
            FR2 ends uo with              4 ether
            */
        }
        if (_callCase == CallCase.SEND_ALL) {
            sendAll(_contractFR1);
            /*
            FS  ends up with 2 + 5      =  0 ether
            FR1 ends up with 1 + 3 + 13 = 17 ether
            FR2 ends uo with               4 ether
            */
        } else if (_callCase == CallCase.INVOKE_TIP_THE_SENDER) {
            invokeTipTheSender(_contractFR1, 6 ether);
            /*
            FS  ends up with 2 + 5 + 3 = 10 ether
            FR1 ends up with 1 + 3 + 3 = 7  ether
            FR2 ends uo with             4  ether
            */
        }

        require(!_mustRevert, "revert transferFunds");
    }

    function sendAll(address payable _contractFR1) internal {
        (bool success, ) = _contractFR1.call{value: address(this).balance}("");
        require(success, "call 6 with send all failed");
    }

    function invokeTipTheSender(address payable _contractFR1, uint256 _amount)
    internal
    {
        (bool success, ) = _contractFR1.call{value: _amount}(
            abi.encodeWithSignature("tipTheSender()")
        );
        require(success, "call 5 with invoke tip the sender failed");
    }
}