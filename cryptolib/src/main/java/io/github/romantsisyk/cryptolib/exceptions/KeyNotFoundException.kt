package io.github.romantsisyk.cryptolib.exceptions

/**
 * Exception thrown when a key is not found in the Keystore.
 */
class KeyNotFoundException(alias: String) :
    CryptoLibException("Key with alias '$alias' not found in the Keystore.")