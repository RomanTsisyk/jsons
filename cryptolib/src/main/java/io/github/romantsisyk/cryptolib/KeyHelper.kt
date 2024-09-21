package io.github.romantsisyk.cryptolib

import android.security.keystore.*
import io.github.romantsisyk.cryptolib.exceptions.CryptoLibException
import io.github.romantsisyk.cryptolib.exceptions.KeyGenerationException
import io.github.romantsisyk.cryptolib.exceptions.KeyNotFoundException
import java.security.*
import java.security.spec.ECGenParameterSpec
import java.util.Calendar
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.SecretKeyFactory

object KeyHelper {

    private const val ANDROID_KEYSTORE = "AndroidKeyStore"

    /**
     * Generates an AES symmetric key and stores it in the Keystore.
     * Throws KeyGenerationException on failure.
     */
    fun generateAESKey(
        alias: String,
        validityDays: Int = 365,
        requireUserAuthentication: Boolean = false
    ) {
        try {
            val keyGenerator = KeyGenerator.getInstance(
                KeyProperties.KEY_ALGORITHM_AES,
                ANDROID_KEYSTORE
            )

            val calendar = Calendar.getInstance()
            val startDate = calendar.time
            calendar.add(Calendar.DAY_OF_YEAR, validityDays)
            val endDate = calendar.time

            val builder = KeyGenParameterSpec.Builder(
                alias,
                KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT
            )
                .setBlockModes(KeyProperties.BLOCK_MODE_GCM)
                .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
                .setKeySize(256)
                .setKeyValidityStart(startDate)
                .setKeyValidityEnd(endDate)

            if (requireUserAuthentication) {
                builder.setUserAuthenticationRequired(true)
                    .setUserAuthenticationValidityDurationSeconds(-1) // Require authentication for every use
            }

            val keyGenParameterSpec = builder.build()

            keyGenerator.init(keyGenParameterSpec)
            keyGenerator.generateKey()
        } catch (e: Exception) {
            throw KeyGenerationException(alias, e)
        }
    }

    /**
     * Generates an RSA key pair and stores it in the Keystore.
     */
    fun generateRSAKeyPair(alias: String) {
        val keyPairGenerator = KeyPairGenerator.getInstance(
            KeyProperties.KEY_ALGORITHM_RSA,
            ANDROID_KEYSTORE
        )

        val keyGenParameterSpec = KeyGenParameterSpec.Builder(
            alias,
            KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT or
                    KeyProperties.PURPOSE_SIGN or KeyProperties.PURPOSE_VERIFY
        )
            .setDigests(
                KeyProperties.DIGEST_SHA256,
                KeyProperties.DIGEST_SHA512
            )
            .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_RSA_OAEP)
            .setSignaturePaddings(KeyProperties.SIGNATURE_PADDING_RSA_PSS)
            .setKeySize(2048)
            .build()

        keyPairGenerator.initialize(keyGenParameterSpec)
        keyPairGenerator.generateKeyPair()
    }

    /**
     * Generates an EC key pair and stores it in the Keystore.
     */
    fun generateECKeyPair(alias: String) {
        val keyPairGenerator = KeyPairGenerator.getInstance(
            KeyProperties.KEY_ALGORITHM_EC,
            ANDROID_KEYSTORE
        )

        val keyGenParameterSpec = KeyGenParameterSpec.Builder(
            alias,
            KeyProperties.PURPOSE_SIGN or KeyProperties.PURPOSE_VERIFY
        )
            .setDigests(
                KeyProperties.DIGEST_SHA256,
                KeyProperties.DIGEST_SHA512
            )
            .setAlgorithmParameterSpec(ECGenParameterSpec("secp256r1"))
            .build()

        keyPairGenerator.initialize(keyGenParameterSpec)
        keyPairGenerator.generateKeyPair()
    }

    /**
     * Retrieves a SecretKey from the Keystore.
     * Throws KeyNotFoundException if the key does not exist.
     */
    fun getAESKey(alias: String): SecretKey {
        val keyStore = KeyStore.getInstance(ANDROID_KEYSTORE).apply { load(null) }
        val key = keyStore.getKey(alias, null) as? SecretKey
            ?: throw KeyNotFoundException(alias)
        return key
    }

    /**
     * Retrieves a PrivateKey from the Keystore.
     */
    fun getPrivateKey(alias: String): PrivateKey? {
        val keyStore = KeyStore.getInstance(ANDROID_KEYSTORE)
        keyStore.load(null)
        val entry = keyStore.getEntry(alias, null) as? KeyStore.PrivateKeyEntry
        return entry?.privateKey
    }

    /**
     * Retrieves a PublicKey from the Keystore.
     */
    fun getPublicKey(alias: String): PublicKey? {
        val keyStore = KeyStore.getInstance(ANDROID_KEYSTORE)
        keyStore.load(null)
        val entry = keyStore.getEntry(alias, null) as? KeyStore.PrivateKeyEntry
        return entry?.certificate?.publicKey
    }

    /**
     * Lists all aliases (keys) stored in the Keystore.
     */
    fun listKeys(): List<String> {
        val keyStore = KeyStore.getInstance(ANDROID_KEYSTORE).apply { load(null) }
        val aliases = keyStore.aliases()
        val keyList = mutableListOf<String>()
        while (aliases.hasMoreElements()) {
            keyList.add(aliases.nextElement())
        }
        return keyList
    }

    /**
     * Deletes a key from the Keystore by its alias.
     * Throws KeyNotFoundException if the key does not exist.
     */
    fun deleteKey(alias: String) {
        val keyStore = KeyStore.getInstance(ANDROID_KEYSTORE).apply { load(null) }
        if (keyStore.containsAlias(alias)) {
            keyStore.deleteEntry(alias)
        } else {
            throw KeyNotFoundException(alias)
        }
    }

    /**
     * Retrieves KeyInfo for a given key alias.
     * Throws KeyNotFoundException if the key does not exist.
     */
    fun getKeyInfo(alias: String): KeyInfo {
        val keyStore = KeyStore.getInstance(ANDROID_KEYSTORE).apply { load(null) }
        val entry = keyStore.getEntry(alias, null) as? KeyStore.SecretKeyEntry
            ?: throw KeyNotFoundException(alias)
        val key = entry.secretKey
        val keyFactory = SecretKeyFactory.getInstance(key.algorithm, ANDROID_KEYSTORE)
        return keyFactory.getKeySpec(key, KeyInfo::class.java) as? KeyInfo
            ?: throw CryptoLibException("Unable to retrieve KeyInfo for alias '$alias'.")
    }
}
