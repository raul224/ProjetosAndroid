package com.example.bloconotasapp

import android.Manifest
import java.io.File
import android.content.Context.LOCATION_SERVICE
import android.content.pm.PackageManager
import android.location.*
import android.os.Bundle
import android.os.Environment
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.PermissionChecker
import androidx.core.content.PermissionChecker.checkSelfPermission
import androidx.navigation.findNavController
import com.example.bloconotasapp.Criptografia.Criptografador
import com.example.bloconotasapp.Models.Nota
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.fragment_adicionar.*
import java.io.*
import java.time.LocalDateTime

class AdicionarFragment : Fragment(), LocationListener {
    private lateinit var auth: FirebaseAuth
    val COARSE_REQUEST = 54321
    var Endereco : String? = null
    val WRITE_REQUEST = 999
    var cripto = Criptografador()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        getLocalizacaoNet()
        auth = Firebase.auth

        return inflater.inflate(R.layout.fragment_adicionar, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        btnVoltarAddToList.setOnClickListener {
            view.findNavController().navigate(R.id.action_adicionarFragment_to_listarFragment)
        }

        btnCadNota.setOnClickListener {
            if(Endereco == null){
                getLocalizacaoNet()
            } else {
                Toast.makeText(context, "Falha ao pegar a localizacao", Toast.LENGTH_LONG).show()
            }

            if(verififcaDados()){
                gravarArquivo()
                view.findNavController().navigate(R.id.action_adicionarFragment_to_listarFragment)
            }
        }
    }



    fun verififcaDados() : Boolean{
        if(
            inputTituloAdd.text.toString().isNullOrEmpty() ||
            inputTextoAdd.text.toString().isNullOrEmpty()
        ){
            Toast.makeText(context, "Dados invalidos!", Toast.LENGTH_LONG).show()
            return false
        } else {
            var novaNota = Nota(
                Id = null,
                Titulo = inputTituloAdd.text.toString(),
                Texto = inputTextoAdd.text.toString(),
                Data = LocalDateTime.now().toString(),
                Local = Endereco
            )

            addNota(novaNota)
            return true
        }
    }

    fun addNota(nota: Nota){
        val nomeColection = "listaNotas"
        var db = Firebase.firestore
        db.collection(nomeColection).add(nota).addOnSuccessListener {
            Toast.makeText(context, "Nota salvo!", Toast.LENGTH_LONG).show()
        }.addOnFailureListener {
            Toast.makeText(context, "Falha ao salvar nota, tente novamente", Toast.LENGTH_LONG).show()
        }

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode == COARSE_REQUEST && grantResults[0] == PackageManager.PERMISSION_GRANTED){
            this.getLocalizacaoNet()
        }
    }

    override fun onLocationChanged(location: Location) {

    }

    fun getLocalizacaoNet() {
        val locationManager = activity?.getSystemService(LOCATION_SERVICE) as LocationManager
        val isNetEnable = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)

        if(isNetEnable){
            if(checkSelfPermission(requireContext() ,Manifest.permission.ACCESS_COARSE_LOCATION)
                == PermissionChecker.PERMISSION_GRANTED  ){
                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,
                2000L,
                0f,
                this)
                var location: Location? = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
                if(location != null){
                    Endereco = "Latitude: ${location.latitude.toString()}, Longitude: ${location.longitude}"
                }
            }else {
                requestPermissions(arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION), COARSE_REQUEST)
                Log.i("Localizacao","Perguntou de localizacao")
            }
        }
    }

    fun gravarArquivo() {
        val string = inputTituloAdd.text.toString() + "\n" + inputTextoAdd.text.toString() + "\n" + LocalDateTime.now().toString() + "\n" + Endereco
        val dataEncripto = cripto.cipher(string)
        val arquivo = inputTituloAdd.text.toString()

        if(Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED) {
            val directory = context?.getExternalFilesDir("notas")
            val file = File(directory, arquivo)

            if (!directory!!.exists()) {
                directory.mkdir()
            }

            BufferedWriter(FileWriter(file)).use {
                it.write(dataEncripto)
            }
        }

    }
}