package io.github.romantsisyk.cryptolib.exceptions

/**
 * Exception thrown during encryption/decryption failures.
 */
class CryptoOperationException(operation: String, cause: Throwable?) :
    CryptoLibException("Encryption/Decryption operation failed: $operation.", cause)