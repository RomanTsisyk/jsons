package io.github.romantsisyk.cryptolib.crypto.rsa

import io.github.romantsisyk.cryptolib.crypto.CryptoConstants
import io.github.romantsisyk.cryptolib.crypto.CryptoConstants.ALGORITHM_RSA
import java.util.Base64
import java.security.KeyPair
import java.security.KeyPairGenerator
import javax.crypto.Cipher
import java.security.PrivateKey
import java.security.PublicKey

object RSAEncryption {

    /**
     * Encrypts plaintext using RSA OAEP.
     */
    fun encrypt(plaintext: ByteArray, publicKey: PublicKey): String {
        val cipher = Cipher.getInstance(CryptoConstants.RSA_TRANSFORMATION)
        cipher.init(Cipher.ENCRYPT_MODE, publicKey)
        val ciphertext = cipher.doFinal(plaintext)
        return Base64.getEncoder().encodeToString(ciphertext)
    }

    /**
     * Decrypts ciphertext using RSA OAEP.
     */
    fun decrypt(encryptedData: String, privateKey: PrivateKey): ByteArray {
        val cipher = Cipher.getInstance(CryptoConstants.RSA_TRANSFORMATION)
        cipher.init(Cipher.DECRYPT_MODE, privateKey)
        val encryptedBytes = Base64.getDecoder().decode(encryptedData)
        return cipher.doFinal(encryptedBytes)
    }

    /**
     * Generates an RSA key pair (public and private keys).
     */
    fun generateKeyPair(): KeyPair {
        val keyPairGenerator = KeyPairGenerator.getInstance(ALGORITHM_RSA)
        keyPairGenerator.initialize(CryptoConstants.RSA_KEY_SIZE)
        return keyPairGenerator.generateKeyPair()
    }
}
