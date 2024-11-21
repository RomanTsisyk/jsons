package io.github.romantsisyk.cryptolib.exceptions

/**
 * Exception thrown when key generation fails.
 */
class KeyGenerationException(alias: String, cause: Throwable?) :
    CryptoLibException("Failed to generate key with alias '$alias'.", cause)