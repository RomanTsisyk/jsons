import io.github.romantsisyk.cryptolib.crypto.CryptoConstants.ALGORITHM_EC
import io.github.romantsisyk.cryptolib.crypto.CryptoConstants.ALGORITHM_RSA
import io.github.romantsisyk.cryptolib.crypto.CryptoConstants.EC_KEY_SIZE
import io.github.romantsisyk.cryptolib.crypto.CryptoConstants.ERROR_UNSUPPORTED_KEY
import io.github.romantsisyk.cryptolib.crypto.CryptoConstants.RSA_KEY_SIZE
import io.github.romantsisyk.cryptolib.crypto.CryptoConstants.SIGNATURE_ALGORITHM_ECDSA
import io.github.romantsisyk.cryptolib.crypto.CryptoConstants.SIGNATURE_ALGORITHM_RSA
import java.util.Base64
import java.security.KeyPair
import java.security.KeyPairGenerator
import java.security.PrivateKey
import java.security.PublicKey
import java.security.Signature
import org.bouncycastle.jce.provider.BouncyCastleProvider
import java.security.Security

object DigitalSignature {

    init {
        Security.addProvider(BouncyCastleProvider())
    }

    /**
     * Signs data using RSA or EC private key.
     */
    fun sign(data: ByteArray, privateKey: PrivateKey): String {
        val algorithm = when (privateKey.algorithm) {
            ALGORITHM_RSA -> SIGNATURE_ALGORITHM_RSA
            ALGORITHM_EC  -> SIGNATURE_ALGORITHM_ECDSA
            else -> throw UnsupportedOperationException(ERROR_UNSUPPORTED_KEY)

        }

        val signature = Signature.getInstance(algorithm, "BC") // Use Bouncy Castle
        signature.initSign(privateKey)
        signature.update(data)
        val signedData = signature.sign()
        return Base64.getEncoder().encodeToString(signedData)
    }

    /**
     * Verifies a signature using RSA or EC public key.
     */
    fun verify(data: ByteArray, signatureStr: String, publicKey: PublicKey): Boolean {
        val algorithm = when (publicKey.algorithm) {
            ALGORITHM_RSA -> SIGNATURE_ALGORITHM_RSA
            ALGORITHM_EC  -> SIGNATURE_ALGORITHM_ECDSA
            else -> throw UnsupportedOperationException(ERROR_UNSUPPORTED_KEY)
        }

        val signature = Signature.getInstance(algorithm, "BC") // Use Bouncy Castle
        signature.initVerify(publicKey)
        signature.update(data)
        val signatureBytes = Base64.getDecoder().decode(signatureStr)
        return signature.verify(signatureBytes)
    }

    /**
     * Generates a key pair for RSA or ECDSA signing.
     * Defaults to RSA.
     */
    fun generateKeyPair(algorithm: String = ALGORITHM_RSA): KeyPair {
        val keyPairGenerator = KeyPairGenerator.getInstance(algorithm)
        val keySize = when (algorithm) {
            ALGORITHM_RSA -> RSA_KEY_SIZE
            ALGORITHM_EC  -> EC_KEY_SIZE
            else -> throw UnsupportedOperationException(ERROR_UNSUPPORTED_KEY)
        }
        keyPairGenerator.initialize(keySize)
        return keyPairGenerator.generateKeyPair()
    }
}