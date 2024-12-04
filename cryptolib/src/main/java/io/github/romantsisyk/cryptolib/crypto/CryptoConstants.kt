package io.github.romantsisyk.cryptolib.crypto

object CryptoConstants {
    // KeyStore
    const val ANDROID_KEYSTORE = "AndroidKeyStore"
    const val DEFAULT_KEY_ALIAS = "MySecureKeyAlias"
    const val QR_CODE_KEY_ALIAS = "CryptoKitQRCodeKey"

    // Algorithms
    const val AES_TRANSFORMATION = "AES/GCM/NoPadding"
    const val RSA_TRANSFORMATION = "RSA/ECB/OAEPWithSHA-256AndMGF1Padding"
    const val SIGNATURE_ALGORITHM_RSA = "SHA256withRSA/PSS"
    const val SIGNATURE_ALGORITHM_ECDSA = "SHA256withECDSA"
    const val ALGORITHM_AES = "AES"
    const val ALGORITHM_RSA = "RSA"
    const val ALGORITHM_EC = "EC"

    // Key Sizes
    const val AES_KEY_SIZE = 256
    const val RSA_KEY_SIZE = 2048
    const val EC_KEY_SIZE = 256

    // Errors
    const val ERROR_UNSUPPORTED_KEY = "Unsupported key algorithm"
    const val ERROR_ENCRYPTION = "Encryption"
    const val ERROR_DECRYPTION = "Decryption"
    const val ERROR_CIPHER_INSTANCE = "Failed to get Cipher instance"

    // Other Constants
    const val GCM_IV_SIZE = 12
    const val GCM_TAG_SIZE = 128
    const val ROTATION_INTERVAL_DAYS = 90

    // Logging
    const val TAG_KEY_ROTATION = "KeyRotationManager"
    const val LOG_ROTATION_SUCCESS = "Key '%s' rotated successfully."
    const val LOG_ROTATION_FAILED = "Key rotation failed for '%s': %s"
}
