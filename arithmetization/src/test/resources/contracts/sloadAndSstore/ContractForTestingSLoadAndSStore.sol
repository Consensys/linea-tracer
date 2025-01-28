// SPDX-License-Identifier: MIT
pragma solidity ^0.8.0;

contract ContractForTestingSLoadAndSStore {
    uint256 public counter;

    // Threshold for the counter
    uint256 public constant THRESHOLD = 4;

    // Address of another instance of this contract
    address public otherContract;

    /**
     * @notice Constructor to set the address of the other contract instance.
     * @param _otherContract Address of another instance of this contract.
     */
    constructor(address _otherContract) {
        // 0x0000000000000000000000000000000000000000 for the first instantiation
        otherContract = _otherContract;
    }

    /**
     * @notice Increment the counter and call the same function on another instance of this contract.
     * @param _counter The current counter value.
     */
    function incrementAndCall(uint256 _counter) external {
         // Increment the counter
        counter = _counter + 1;

        // Ensure the counter does not exceed the threshold
        require(counter < THRESHOLD, "Counter value exceeds threshold");


        // Call the same function on the other instance of the contract
        if (otherContract != address(0)) {
            ContractForTestingSLoadAndSStore(otherContract).incrementAndCall(counter);
        }
    }
}