package com.example.bloconotasapp.Adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.bloconotasapp.Models.Nota
import com.example.bloconotasapp.databinding.NotaItemListBinding

class NotasAdapter(private val onItemClicked: (Nota) -> Unit) : ListAdapter<Nota, NotasAdapter.ViewHolder>(DiffCallBack){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            NotaItemListBinding.inflate(LayoutInflater.from(parent.context))
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int){
        val current = getItem(position)
        holder.itemView.setOnClickListener {
            onItemClicked(current)
        }
        holder.bind(current)
    }

    class ViewHolder(private val binding: NotaItemListBinding) : RecyclerView.ViewHolder(binding.root){

        fun bind(item: Nota){
            binding.viewTitulo.text = item.Titulo
            binding.viewTexto.text = item.Texto
            binding.viewData.text = item.Data
        }
    }

    companion object {
        private val DiffCallBack = object : DiffUtil.ItemCallback<Nota>() {
            override fun areItemsTheSame(oldItem: Nota, newItem: Nota): Boolean {
                return oldItem === newItem
            }

            override fun areContentsTheSame(oldItem: Nota, newItem: Nota): Boolean {
                return oldItem.Titulo == newItem.Titulo
            }
        }
    }
}