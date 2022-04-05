package com.example.bloconotasapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.bloconotasapp.Ui.HomeActivity
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_item.*

class ItemActivity : AppCompatActivity() {
    val db = Firebase.firestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_item)

        val bundle = intent.extras
                if(bundle!!.get("item") == null) {
                    Toast.makeText(this, "Erro ao receber id", Toast.LENGTH_LONG).show()
                    startActivity(Intent(this, HomeActivity::class.java))
                }

        val itemId = bundle.get("item").toString()

        btnVoltarEdit.setOnClickListener {
            voltarLista()
        }

        btnEditarEdit.setOnClickListener {
            EditarItem(itemId)
        }

        btnExcluirEdit.setOnClickListener {
            ExcluirItem(itemId)
        }

        db.collection("listaNotas")
            .document("${itemId}").get().addOnSuccessListener { nota ->
                if (nota != null){
                    viewTituloEdit.setText(nota.get("titulo").toString())
                    viewTextoEdit.setText(nota.get("texto").toString())
                    viewDataEdit.text = nota.get("data").toString()
                    viewlocalizacaoEdit.text = nota.get("local").toString()
                }
            }.addOnFailureListener {
                Toast.makeText(this, "Erro ao carregar nota", Toast.LENGTH_LONG).show()
            }
    }

    fun voltarLista(){
        startActivity(Intent(this, HomeActivity::class.java))
    }

    fun EditarItem(item : String) {
        var novoTitulo = viewTituloEdit.text.toString()
        var novoTexto = viewTextoEdit.text.toString()

        //ver como fazer update
        db.collection("listaNotas").document(item).update(mapOf(
            "titulo" to novoTitulo,
            "texto" to novoTexto
        )).addOnSuccessListener {
            Toast.makeText(this, "${novoTitulo} alterado", Toast.LENGTH_LONG).show()
            voltarLista()
        }.addOnFailureListener {
            Toast.makeText(this, "Não foi possível modificar o produto, tente novamente", Toast.LENGTH_SHORT).show()
        }
    }

    fun ExcluirItem(item : String) {
        db.collection("listaNotas").document(item).delete()
            .addOnSuccessListener {
                Toast.makeText(this, "Nota excluido!", Toast.LENGTH_LONG).show()
                voltarLista()
            } .addOnFailureListener {
                Toast.makeText(this, "Não foi possível excluir o nota, tente novamente", Toast.LENGTH_SHORT).show()
            }
    }

}