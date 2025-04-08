package com.example.togethernotes.tools

import android.util.Base64
import javax.crypto.Cipher
import javax.crypto.spec.SecretKeySpec
import java.security.MessageDigest

object CryptoUtil {

    private const val password = "TogetherNotesPasswordToEncryptAndDecrypt"

    private fun getSecretKey(): SecretKeySpec {
        val key = password.toByteArray(Charsets.UTF_8)
        val sha = MessageDigest.getInstance("SHA-256")
        val keyDigest = sha.digest(key).copyOf(16) // use first 16 bytes (128-bit AES)
        return SecretKeySpec(keyDigest, "AES")
    }

    fun encrypt(input: String): String {
        val cipher = Cipher.getInstance("AES/ECB/PKCS5Padding")
        cipher.init(Cipher.ENCRYPT_MODE, getSecretKey())
        val encrypted = cipher.doFinal(input.toByteArray(Charsets.UTF_8))
        return Base64.encodeToString(encrypted, Base64.DEFAULT).trim()
    }

    fun decrypt(encryptedBase64: String): String {
        val cipher = Cipher.getInstance("AES/ECB/PKCS5Padding")
        cipher.init(Cipher.DECRYPT_MODE, getSecretKey())
        val decryptedBytes = cipher.doFinal(Base64.decode(encryptedBase64, Base64.DEFAULT))
        return String(decryptedBytes, Charsets.UTF_8)
    }
}