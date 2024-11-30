import android.content.Context
import androidx.biometric.BiometricPrompt
import androidx.fragment.app.FragmentActivity
import io.github.romantsisyk.cryptolib.crypto.aes.AESEncryption
import io.github.romantsisyk.cryptolib.crypto.keymanagement.KeyHelper
import javax.crypto.Cipher

class BiometricHelper(private val context: Context) {

    /**
     * Authenticates the user using biometrics and optionally decrypts the provided encrypted data.
     * @param activity The activity where the biometric prompt will be displayed.
     * @param encryptedData Data to be decrypted upon successful authentication.
     * @param title The title displayed on the biometric prompt.
     * @param description The description displayed on the biometric prompt.
     * @param onSuccess Callback to handle the decrypted data.
     * @param onError Callback to handle errors during authentication or decryption.
     * @param onAuthenticationError Callback to handle authentication-specific errors.
     */
    fun authenticate(
        activity: FragmentActivity,
        title: String,
        description: String,
        encryptedData: ByteArray,
        onSuccess: (ByteArray) -> Unit,
        onError: (Exception) -> Unit,
        onAuthenticationError: (Int, CharSequence) -> Unit
    ) {
        val promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle(title)
            .setDescription(description)
            .setSubtitle("Log in using your biometrics")
            .setDescription("Place your fingerprint on the sensor to authenticate")
            .setNegativeButtonText("Cancel")
            .build()

        val biometricPrompt = BiometricPrompt(
            activity,
            activity.mainExecutor,
            object : BiometricPrompt.AuthenticationCallback() {
                override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                    super.onAuthenticationSucceeded(result)
                    try {
                        // Decrypt the data using the authenticated cipher
                        val decryptedData = result.cryptoObject?.cipher?.let {
                            AESEncryption.decrypt(
                                encryptedData.toString(Charsets.UTF_8),
                                KeyHelper.getKey()
                            )
                        }
                        if (decryptedData != null) {
                            onSuccess(decryptedData)
                        } else {
                            onError(Exception("Decryption returned null"))
                        }
                    } catch (e: Exception) {
                        onError(e)
                    }
                }

                override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                    super.onAuthenticationError(errorCode, errString)
                    onAuthenticationError(errorCode, errString)
                }

                override fun onAuthenticationFailed() {
                    super.onAuthenticationFailed()
                    onError(Exception("Authentication failed"))
                }
            }
        )

        val cipher = getCipher()
        val cryptoObject = BiometricPrompt.CryptoObject(cipher)

        biometricPrompt.authenticate(promptInfo, cryptoObject)
    }

    /**
     * Initializes a Cipher object for decryption.
     * @return A Cipher initialized with a secret key.
     * @throws IllegalStateException if initialization fails.
     */
    private fun getCipher(): Cipher {
        return try {
            val secretKey = KeyHelper.getKey() // Retrieve the secure key from KeyHelper
            val cipher = Cipher.getInstance("AES/GCM/NoPadding")
            cipher.init(Cipher.DECRYPT_MODE, secretKey)
            cipher
        } catch (e: Exception) {
            throw IllegalStateException("Failed to initialize Cipher", e)
        }
    }

    /**
     * Decrypts the provided data using the Cipher.
     * @param cipher The Cipher used for decryption.
     * @param encryptedData The encrypted data to decrypt.
     * @return The decrypted data as a ByteArray.
     */
    private fun decryptData(cipher: Cipher, encryptedData: ByteArray): ByteArray {
        return cipher.doFinal(encryptedData)
    }
}
