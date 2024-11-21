package io.github.romantsisyk.cryptolib.exceptions

/**
 * Base class for all CryptoLib exceptions.
 */
open class CryptoLibException(message: String, cause: Throwable? = null) : Exception(message, cause)