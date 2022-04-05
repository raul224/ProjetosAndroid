package com.example.bloconotasapp

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.bloconotasapp.Adapter.NotasAdapter
import com.example.bloconotasapp.Models.Nota
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.MobileAds
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.fragment_listar.*

class ListarFragment : Fragment() {
    lateinit var adapter : NotasAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_listar, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        MobileAds.initialize(this.context) {}

        val adRequest = AdRequest.Builder().build()
        adView.loadAd(adRequest)

        btnAddNota.setOnClickListener {
            view.findNavController().navigate(R.id.action_listarFragment_to_adicionarFragment)
        }

        setupRecyclerView()
    }

    fun setupRecyclerView(){
        val nomeCollection = "listaNotas"
        val db = Firebase.firestore

        var listaDb : ArrayList<Nota> = ArrayList()

        db.collection(nomeCollection).addSnapshotListener { notas, e ->
            if(e != null){
                return@addSnapshotListener
            }

            if(notas != null){
                for(nota in notas.documentChanges){
                    when (nota.type){
                        DocumentChange.Type.ADDED -> {
                            var listItem = Nota(
                                Id = nota.document.id,
                                Titulo = nota.document.data.get("titulo").toString(),
                                Texto = nota.document.data.get("texto").toString(),
                                Data = nota.document.data.get("data").toString(),
                            )
                            listaDb.add(listItem)
                        }
                        DocumentChange.Type.MODIFIED -> {
                            var not = listaDb.get(nota.oldIndex)
                            not.Titulo = nota.document.data.get("titulo").toString()
                            not.Texto = nota.document.data.get("texto").toString()
                            not.Data = nota.document.data.get("data").toString()
                        }
                        DocumentChange.Type.REMOVED -> {
                            listaDb.removeAt(nota.oldIndex)
                        }

                    }
                }
            }

            adapter = NotasAdapter{
                var intent = Intent(context, ItemActivity::class.java)
                intent.putExtra("item", it.Id)
                startActivity(intent);
            }

            adapter.submitList(listaDb)

            recyclerView.layoutManager = LinearLayoutManager(context)
            recyclerView.adapter = adapter
        }
    }
}