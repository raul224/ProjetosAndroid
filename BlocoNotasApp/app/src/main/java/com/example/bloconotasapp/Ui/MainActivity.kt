package com.example.bloconotasapp.Ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.bloconotasapp.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        auth = Firebase.auth

        btnCadastrar.setOnClickListener {
            if(!inputEmail.text.toString().isNullOrEmpty() || !inputSenha.text.toString().isNullOrEmpty()){
                registrar(inputEmail.text.toString(), inputSenha.text.toString())
            }else {
                Toast.makeText(this, "Entre com e-mail e senha", Toast.LENGTH_LONG).show()
            }
        }

        btnLogin.setOnClickListener {
            login(inputEmail.text.toString(), inputSenha.text.toString())
        }
    }

    fun login(email:String, senha:String){
        auth = FirebaseAuth.getInstance();
        auth.signInWithEmailAndPassword(email, senha).
        addOnSuccessListener {
            Toast.makeText(this, "Logado", Toast.LENGTH_LONG).show()
            startActivity(Intent(this, HomeActivity::class.java))
        }.addOnFailureListener{
            Toast.makeText(this, it.message, Toast.LENGTH_LONG).show()
        }
    }

    override fun onStart() {
        super.onStart()
        val currentUser = auth.currentUser
        if(currentUser != null){
            startActivity(Intent(this, HomeActivity::class.java))
        }

    }

    fun registrar(email:String, senha:String) {
        auth = FirebaseAuth.getInstance()
        auth.createUserWithEmailAndPassword(email, senha).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Toast.makeText(this, "Conta criada com sucesso", Toast.LENGTH_LONG).show()
                startActivity(Intent(this, HomeActivity::class.java))
            } else {
                Toast.makeText(this, "Não foi possivel criar a conta", Toast.LENGTH_LONG).show()
            }
        }
    }
}