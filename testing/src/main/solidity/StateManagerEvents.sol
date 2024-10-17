// SPDX-License-Identifier: MIT
pragma solidity 0.8.19;

/**
 * @notice Shared contract containing framework events.
 */
abstract contract StateManagerEvents {
    /// @dev The event for writing storage;
    /// @param contractAddress is the contract addresss, x is the storage key and y is the storage value.
    /// Event signature: 33d8dc4a860afa0606947f2b214f16e21e7eac41e3eb6642e859d9626d002ef6
    event Write(address contractAddress, uint256 x, uint256 y);

    /// @dev The event for reading storage;
    /// @param contractAddress is the contract addresss, x is the storage key and y is the storage value.
    /// the event will be generated after y is read as the value stored at x.
    /// Event signature: c2db4694c1ec690e784f771a7fe3533681e081da4baa4aa1ad7dd5c33da95925
    event Read(address contractAddress, uint256 x, uint256 y);

    event EventReadFromStorage(address contractAddress, uint256 x);
}
