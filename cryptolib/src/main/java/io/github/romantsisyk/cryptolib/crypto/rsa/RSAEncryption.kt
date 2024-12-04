package io.github.romantsisyk.cryptolib.crypto.rsa

import java.util.Base64
import java.security.KeyPair
import java.security.KeyPairGenerator
import javax.crypto.Cipher
import java.security.PrivateKey
import java.security.PublicKey

object RSAEncryption {

    private const val TRANSFORMATION = "RSA/ECB/OAEPWithSHA-256AndMGF1Padding"
    private const val KEY_SIZE = 2048

    /**
     * Encrypts plaintext using RSA OAEP.
     */
    fun encrypt(plaintext: ByteArray, publicKey: PublicKey): String {
        val cipher = Cipher.getInstance(TRANSFORMATION)
        cipher.init(Cipher.ENCRYPT_MODE, publicKey)
        val ciphertext = cipher.doFinal(plaintext)
        return Base64.getEncoder().encodeToString(ciphertext)
    }

    /**
     * Decrypts ciphertext using RSA OAEP.
     */
    fun decrypt(encryptedData: String, privateKey: PrivateKey): ByteArray {
        val cipher = Cipher.getInstance(TRANSFORMATION)
        cipher.init(Cipher.DECRYPT_MODE, privateKey)
        val encryptedBytes = Base64.getDecoder().decode(encryptedData)
        return cipher.doFinal(encryptedBytes)
    }

    /**
     * Generates an RSA key pair (public and private keys).
     */
    fun generateKeyPair(): KeyPair {
        val keyPairGenerator = KeyPairGenerator.getInstance("RSA")
        keyPairGenerator.initialize(KEY_SIZE)
        return keyPairGenerator.generateKeyPair()
    }
}
