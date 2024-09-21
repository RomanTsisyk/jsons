package io.github.romantsisyk.cryptolib.exceptions

/**
 * Base class for all CryptoLib exceptions.
 */
open class CryptoLibException(message: String, cause: Throwable? = null) : Exception(message, cause)

/**
 * Exception thrown when a key is not found in the Keystore.
 */
class KeyNotFoundException(alias: String) :
    CryptoLibException("Key with alias '$alias' not found in the Keystore.")

/**
 * Exception thrown when key generation fails.
 */
class KeyGenerationException(alias: String, cause: Throwable?) :
    CryptoLibException("Failed to generate key with alias '$alias'.", cause)

/**
 * Exception thrown during encryption/decryption failures.
 */
class CryptoOperationException(operation: String, cause: Throwable?) :
    CryptoLibException("Encryption/Decryption operation failed: $operation.", cause)

/**
 * Exception thrown during authentication failures.
 */
class AuthenticationException(message: String) :
    CryptoLibException("Authentication failed: $message")
