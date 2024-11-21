package io.github.romantsisyk.cryptolib.crypto.rsa

import android.util.Base64
import javax.crypto.Cipher
import java.security.PrivateKey
import java.security.PublicKey

object RSAEncryption {

    private const val TRANSFORMATION = "RSA/ECB/OAEPWithSHA-256AndMGF1Padding"

    /**
     * Encrypts plaintext using RSA OAEP.
     */
    fun encrypt(plaintext: ByteArray, publicKey: PublicKey): String {
        val cipher = Cipher.getInstance(TRANSFORMATION)
        cipher.init(Cipher.ENCRYPT_MODE, publicKey)
        val ciphertext = cipher.doFinal(plaintext)
        return Base64.encodeToString(ciphertext, Base64.NO_WRAP)
    }

    /**
     * Decrypts ciphertext using RSA OAEP.
     */
    fun decrypt(encryptedData: String, privateKey: PrivateKey): ByteArray {
        val cipher = Cipher.getInstance(TRANSFORMATION)
        cipher.init(Cipher.DECRYPT_MODE, privateKey)
        val encryptedBytes = Base64.decode(encryptedData, Base64.NO_WRAP)
        return cipher.doFinal(encryptedBytes)
    }
}
