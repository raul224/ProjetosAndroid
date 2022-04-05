package com.example.bloconotasapp.Criptografia

import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import android.util.Base64
import java.security.KeyStore
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.IvParameterSpec

class Criptografador {
    fun cipher(texto: String) : String{
        var chave = getSecretKey()
        if (chave != null) {
            Cipher.getInstance("AES/CBC/PKCS7Padding").run {

                init(Cipher.ENCRYPT_MODE, chave)
                var valorCripto = doFinal(texto.toByteArray())
                var ivCripto = ByteArray(16)
                iv.copyInto(ivCripto, 0, 0, 16)
                return Base64.encodeToString(ivCripto + valorCripto, Base64.URL_SAFE)
            }
        } else return ""
    }

    fun decipher(cifra: String) : String{
        var chave = getSecretKey()
        if (chave != null) {

            Cipher.getInstance("AES/CBC/PKCS7Padding").run {

                val criptoAsBytes = Base64.decode(cifra, Base64.URL_SAFE)
                var ivCripto = ByteArray(16)
                var valorCripto = ByteArray(criptoAsBytes.size - 16)
                criptoAsBytes.copyInto(ivCripto, 0, 0, 16)
                criptoAsBytes.copyInto(valorCripto, 0, 16, criptoAsBytes.size)
                val ivParams = IvParameterSpec(ivCripto)
                init(Cipher.DECRYPT_MODE, chave, ivParams)
                return String(doFinal(valorCripto))
            }
        } else return ""
    }

    private fun getSecretKey() : SecretKey? {
        var chave: SecretKey? = null
        val ks: KeyStore = KeyStore.getInstance("AndroidKeyStore").apply { load(null) }
        if (ks.containsAlias("chaveCripto")) {

            val entrada = ks.getEntry("chaveCripto", null) as? KeyStore.SecretKeyEntry
            chave = entrada?.secretKey
        } else {

            val builder = KeyGenParameterSpec.Builder(
                "chaveCripto",
                KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT
            )
            val keySpec = builder.setKeySize(256)
                .setBlockModes(KeyProperties.BLOCK_MODE_CBC)
                .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_PKCS7)
                .build()
            val kg = KeyGenerator.getInstance("AES", "AndroidKeyStore")
            kg.init(keySpec)
            chave = kg.generateKey()
        }
        return chave
    }
}