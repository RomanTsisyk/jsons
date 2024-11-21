package io.github.romantsisyk.cryptolib.exceptions

/**
 * Exception thrown during authentication failures.
 */
class AuthenticationException(message: String) :
    CryptoLibException("Authentication failed: $message")
