// SPDX-License-Identifier: MIT
pragma solidity ^0.8.0;

import "./FundsSender.sol";

contract FundsSenderRoot {

    function invokeFS(
        address payable _contractFS,
        address payable _contractFR1,
        address payable _contractFR2,
        bool _mustRevert,
        FundsSender.Case _case
    ) external {
      FundsSender(_contractFS).transferFunds(_contractFR1, _contractFR2, _mustRevert, _case);
    }
}
