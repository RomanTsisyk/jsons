package io.github.romantsisyk.cryptolib.crypto.digital

import android.util.Base64
import java.security.PrivateKey
import java.security.PublicKey
import java.security.Signature

object DigitalSignature {

    private const val SIGNATURE_ALGORITHM_RSA = "SHA256withRSA/PSS"
    private const val SIGNATURE_ALGORITHM_ECDSA = "SHA256withECDSA"

    /**
     * Signs data using RSA or EC private key.
     */
    fun sign(data: ByteArray, privateKey: PrivateKey): String {
        val algorithm = when (privateKey.algorithm) {
            "RSA" -> SIGNATURE_ALGORITHM_RSA
            "EC" -> SIGNATURE_ALGORITHM_ECDSA
            else -> throw UnsupportedOperationException("Unsupported key algorithm")
        }

        val signature = Signature.getInstance(algorithm)
        signature.initSign(privateKey)
        signature.update(data)
        val signedData = signature.sign()
        return Base64.encodeToString(signedData, Base64.NO_WRAP)
    }

    /**
     * Verifies a signature using RSA or EC public key.
     */
    fun verify(data: ByteArray, signatureStr: String, publicKey: PublicKey): Boolean {
        val algorithm = when (publicKey.algorithm) {
            "RSA" -> SIGNATURE_ALGORITHM_RSA
            "EC" -> SIGNATURE_ALGORITHM_ECDSA
            else -> throw UnsupportedOperationException("Unsupported key algorithm")
        }

        val signature = Signature.getInstance(algorithm)
        signature.initVerify(publicKey)
        signature.update(data)
        val signatureBytes = Base64.decode(signatureStr, Base64.NO_WRAP)
        return signature.verify(signatureBytes)
    }
}
