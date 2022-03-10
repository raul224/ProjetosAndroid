package com.example.apptp1seg

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ArquivoAdapter (val arquivos : MutableList<String>) : RecyclerView.Adapter<ArquivoAdapter.ArquivoViewHolder>() {

    class ArquivoViewHolder(view: View) : RecyclerView.ViewHolder(view){
        val arquivo = view.findViewById<TextView>(R.id.txtArquivo)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArquivoViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.view_arquivos,parent,false)
        val usuarioViewHolder = ArquivoViewHolder(v)
        return usuarioViewHolder    }

    override fun getItemCount(): Int = arquivos!!.size


    override fun onBindViewHolder(holder: ArquivoViewHolder, position: Int) {
        val file = arquivos!![position]
        holder.arquivo.text = file
    }

}