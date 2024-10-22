object "YulContractOld" {
    code {
        // return the bytecode of the contract
        datacopy(0x00, dataoffset("runtime"), datasize("runtime"))
        return(0x00, datasize("runtime"))
    }

    object "runtime" {
        code {
            switch selector()
            case 0xa770741d // Write()
            {
               write()
            }

            case 0x97deb47b // Read()
            {
                // store the ID to memory at 0x00
                mstore(0x00, sload(0x00))

                return (0x00,0x20)
            }

            case 0x0dd2602c // writeToStorage()
            {
                // Load the first argument (x) from calldata
                let x := calldataload(0x04)

                // Load the second argument (y) from calldata
                let y := calldataload(0x24)

                // call the writeToStorage function
                writeToStorage(x, y)

                // log the call
                logValuesWrite(x, y)
            }

            case 0xd40e607a // readFromStorage()
            {
                // Load the first argument (x) from calldata
                let x := calldataload(0x04)

                // call the readFromStorage function
                let y := readFromStorage(x)

                // log the call
                logValuesRead(x, y)
            }

            case 0x3f5a0bdd // selfDestruct
            {
                // Load the first argument (recipient) from calldata
                let recipient := calldataload(0x04)

                 // log the call before self destructing
                logSelfDestruct(recipient)

                // call the self-destruct function
                selfDestruct(recipient)
            }

            default {
                // if the function signature sent does not match any
                // of the contract functions, revert
                revert(0, 0)
            }

            function write() {
                // take no inputs, push the 1234...EF 32 byte integer onto the stack
                // add 0 as the slot key to the stack
                // store the key/value
                verbatim_0i_0o(hex"7f0123456789ABCDEF0123456789ABCDEF0123456789ABCDEF0123456789ABCDEF600055")
            }

            // Function to log two uint256 values for the write storage operation, along with the contract address
           function logValuesWrite(x, y) {
                // Define an event signature to generate logs for storage operations
                let eventSignature := "Write(address,uint256,uint256)" // The signature is "Write(address,uint256,uint256)", signature length is 30 characters (thus 30 bytes)

                let memStart := mload(0x40) // get the free memory pointer
                mstore(memStart, eventSignature)

                let eventSignatureHash := keccak256(memStart, 30) // expected 33d8dc4a860afa0606947f2b214f16e21e7eac41e3eb6642e859d9626d002ef6

                // in the case of a delegate call, this will be the original that called the .Yul snippet.
                // For a regular call, it will be the address of the .Yul contract.
                let contractAddress := address()

                // call the inbuilt logging function
                log4(0x20, 0x60, eventSignatureHash, contractAddress, x, y)
           }
           // Function to log two uint256 values for the read storage operation, along with the contract address
           function logValuesRead(x, y) {
                // Define an event signature to generate logs for storage operations
                let eventSignatureHex := 0x5265616428616464726573732c75696e743235362c75696e7432353629202020
                // Above is the hex for "Read(address,uint256,uint256)", exact length is 29 characters, padded with 3 spaces (20 in hex)
                // if we do not pad at the end, the hex is stored in the wrong manner. The spaces will be disregarded when we hash.

                let memStart := mload(0x40) // get the free memory pointer
                mstore(memStart, eventSignatureHex)

                let eventSignatureHash := keccak256(memStart, 29) // 29 bytes is the string length, expected output c2db4694c1ec690e784f771a7fe3533681e081da4baa4aa1ad7dd5c33da95925

                // in the case of a delegate call, this will be the original that called the .Yul snippet.
                // For a regular call, it will be the address of the .Yul contract.
                let contractAddress := address()

                // call the inbuilt logging function
                log4(0x20, 0x60, eventSignatureHash, contractAddress, x, y)
           }

            // Function to log the self destruction of the contract
           function logSelfDestruct(recipient) {
                // Define an event signature to generate logs for storage operations
                let eventSignature := "ContractDestroyed(address)"

                let memStart := mload(0x40) // get the free memory pointer
                mstore(memStart, eventSignature)

                let eventSignatureHash := keccak256(memStart, 26) // 26 bytes is the string length, expected output 3ab1d7915d663a46c292b8f01ac13567c748cff5213cb3652695882b5f9b2e0f

                // call the inbuilt logging function
                log2(0x20, 0x60, eventSignatureHash, recipient)
           }

            // 0x0dd2602cce131b717885742cf4e9a79978d80b588950101dc8619106202b5fd5
            // function signature: first 4 bytes 0x0dd2602c
            // example of call: [["Addr",0x0dd2602c00000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000001,0,0,0]]
            // writeToStorage(uint256,uint256) is the unhashed function signature
            function writeToStorage(x, y) {
                // Use verbatim to include raw bytecode for storing y at storage key x
                // 55 corresponds to SSTORE
                verbatim_2i_0o(hex"55", x, y)
            }

            // d40e607a2e462b02b78a86a129a210a71054c4774d1e5b71932b4e61bbd12df6
            // function signature: first 4 bytes 0xd40e607a
            // example of call: [["Addr",0xd40e607a0000000000000000000000000000000000000000000000000000000000000001,0,0,0]]
            // readFromStorage(uint256) is the unhashed function signature
            function readFromStorage(x) -> y{
                // Use verbatim to include raw bytecode for reading the value stored at x
                // 54 corresponds to SLOAD
                y := verbatim_1i_1o(hex"54", x)
            }

            /**
            * @notice Selfdestructs and sends remaining ETH to a payable address.
            * @dev Keep in mind you need to compile and target London EVM version - this doesn't work for repeat addresses on Cancun etc.
            * @param _fundAddress The deploying contract's address.
            * example of call: [["Addr",0x3f5a0bdd0000000000000000000000005b38da6a701c568545dcfcb03fcb875f56beddc4,0,0,0]]
            * (replace the argument 5b38da6a701c568545dcfcb03fcb875f56beddc4 with the intended recipient, as needed)
            **/
            function selfDestruct(recipient) {
                // There is a weird bug with selfdestruct(recipient), so we replace that with verbatim
                // Use verbatim to include the selfdestruct opcode (0xff)
                verbatim_1i_0o(hex"ff", recipient)
            }

            // Return the function selector: the first 4 bytes of the call data
            function selector() -> s {
                s := div(calldataload(0), 0x100000000000000000000000000000000000000000000000000000000)
            }

            // Implementation of the require statement from Solidity
            function require(condition) {
                if iszero(condition) { revert(0, 0) }
            }

            // Check if the calldata has the correct number of params
            function checkParamLength(len) {
                require(eq(calldatasize(), add(4, mul(32, len))))
            }

            // Transfer ether to the caller address
            function transfer(amount) {
                if iszero(call(gas(), caller(), amount, 0, 0, 0, 0)) {
                    revert(0,0)
                }
            }
        }
    }
}