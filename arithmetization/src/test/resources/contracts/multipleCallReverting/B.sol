// SPDX-License-Identifier: MIT
pragma solidity ^0.8.0;

contract B {
    receive() external payable {}

    function transferFunds(address payable _contractA, address payable _contractC, bool _mustRevert) external {
        require(address(this).balance >= 21 ether, "B balance must be at least 21 ether");
          
        (bool a1, ) = _contractA.call{value: 1 ether}("");
        require(a1, "a1 failed");

        (bool b2, ) = address(this).call{value: 2 ether}("");
        require(b2, "b2 failed");
            
        (bool a3, ) = _contractA.call{value: 3 ether}("");
        require(a3, "a3 failed");

        (bool c4, ) = _contractC.call{value: 4 ether}("");
        require(c4, "c4 failed");

       (bool b5, ) = address(this).call{value: 5 ether}("");
       require(b5, "b5 failed");

       (bool a6, ) = _contractA.call{value: 6 ether}("");
       require(a6, "a6 failed"); 

       require(!_mustRevert, "revert transferFunds");

       /*
       A receives     1 + 3 + 6 = 10 ether
       B remains with 2 + 5     =  7 ether
       C receives                  4 ether
       */
    }


    function sendAll(address payable _contractA) external {
        require(address(this).balance > 0 ether, "B balance must be positive");
        
        (bool aAll, ) = _contractA.call{value: address(this).balance}("");
        require(aAll, "aAll failed");
    }

    function invokeTipTheSender(address payable _contractA) external {
        (bool aTipTheSender, ) = _contractA.call{value: address(this).balance}(abi.encodeWithSignature("tipTheSender()"));
        require(aTipTheSender, "aTipTheSender failed ");
    }
}