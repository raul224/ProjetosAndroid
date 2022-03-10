package com.example.apptp1seg

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import androidx.documentfile.provider.DocumentFile
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.io.File

class listarArquivos : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_listar_arquivos)

        var rwListaArquivos = findViewById<RecyclerView>(R.id.rwListaArquivos)

        var lista = mutableListOf<String>()
        var externo = getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS)!!.listFiles()

        externo.forEach {
            lista.add(it.name)
        }

        var adapter = ArquivoAdapter(lista)
        rwListaArquivos.adapter = adapter
        rwListaArquivos.layoutManager = LinearLayoutManager(this)

    }
}