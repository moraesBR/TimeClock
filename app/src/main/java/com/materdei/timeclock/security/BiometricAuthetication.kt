package com.materdei.timeclock.security

import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import android.widget.Toast
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.materdei.timeclock.utils.Constants.Companion.KEY_BIOMETRIC
import com.materdei.timeclock.viewmodels.DoItViewModel
import java.nio.charset.Charset
import java.security.KeyStore
import java.util.*
import java.util.concurrent.Executor
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey

class BiometricAuthetication(fragment: Fragment) {

    private var executor: Executor
    private var biometricPrompt: BiometricPrompt
    private lateinit var promptInfo: BiometricPrompt.PromptInfo
    private val doIt = ViewModelProvider(fragment).get(DoItViewModel::class.java)

    private val bioAutheticationCallback = object : BiometricPrompt.AuthenticationCallback() {
        override fun onAuthenticationError(errorCode: Int,
                                           errString: CharSequence) {
            super.onAuthenticationError(errorCode, errString)
            Toast.makeText(fragment.requireContext(),
                "Authentication error: $errString", Toast.LENGTH_SHORT)
                .show()
        }

        override fun onAuthenticationSucceeded(
            result: BiometricPrompt.AuthenticationResult) {
            super.onAuthenticationSucceeded(result)
            val encryptedInfo: ByteArray = result.cryptoObject?.cipher?.doFinal(
                KEY_BIOMETRIC.toByteArray(Charset.defaultCharset()))!!
            Toast.makeText(fragment.requireContext(),
                "Authentication succeeded!" +
                        Arrays.toString(encryptedInfo), Toast.LENGTH_SHORT)
                .show()
            doIt.engage()
        }

        override fun onAuthenticationFailed() {
            super.onAuthenticationFailed()
            Toast.makeText(fragment.requireContext(), "Authentication failed",
                Toast.LENGTH_SHORT)
                .show()
        }
    }

    init {
        executor = ContextCompat.getMainExecutor(fragment.requireContext())
        biometricPrompt = BiometricPrompt(fragment,executor,bioAutheticationCallback)
    }

    private fun generateSecretKey(keyGenParameterSpec: KeyGenParameterSpec) {
        val keyGenerator = KeyGenerator.getInstance(
            KeyProperties.KEY_ALGORITHM_AES, "AndroidKeyStore")
        keyGenerator.init(keyGenParameterSpec)
        keyGenerator.generateKey()
    }

    private fun getSecretKey(): SecretKey {
        val keyStore = KeyStore.getInstance("AndroidKeyStore")

        // Before the keystore can be accessed, it must be loaded.
        keyStore.load(null)
        return keyStore.getKey(KEY_BIOMETRIC, null) as SecretKey
    }

    private fun getCipher(): Cipher {
        return Cipher.getInstance(KeyProperties.KEY_ALGORITHM_AES + "/"
                + KeyProperties.BLOCK_MODE_CBC + "/"
                + KeyProperties.ENCRYPTION_PADDING_PKCS7)
    }

    fun start(title:String, subTitle: String, negativeButton: String){
        generateSecretKey(KeyGenParameterSpec.Builder(
            KEY_BIOMETRIC,
            KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT)
            .setBlockModes(KeyProperties.BLOCK_MODE_CBC)
            .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_PKCS7)
            .setUserAuthenticationRequired(true)
            .setInvalidatedByBiometricEnrollment(true)
            .build())

        val cipher = getCipher()
        val secretKey = getSecretKey()
        cipher.init(Cipher.ENCRYPT_MODE, secretKey)

        promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle(title)
            .setSubtitle(subTitle)
            .setNegativeButtonText(negativeButton)
            .build()

        biometricPrompt.authenticate(promptInfo,BiometricPrompt.CryptoObject(cipher))
    }
}