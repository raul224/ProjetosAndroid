package com.example.apptp1seg

import android.content.Intent
import android.content.pm.PackageManager
import android.icu.text.SimpleDateFormat
import android.icu.util.ULocale
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.widget.Button
import android.widget.Toast
import java.io.File
import java.io.FileOutputStream
import java.util.*

class MainActivity : AppCompatActivity(), LocationListener {
    val COARSE_REQUEST = 12345
    val FINE_REQUEST = 67890

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val btnLocation = this.findViewById<Button>(R.id.btnSalvarLocalizacao)
        btnLocation.setOnClickListener {
            var location : Location? = this.obterLocalizacaoPelaRede()

            if(location != null){
                this.escreveLocalizacao(location)
            } else {
                Toast.makeText(this, "Não foi possivel obter a localização", Toast.LENGTH_LONG).show()
            }
        }

        val btnVerArquivos = this.findViewById<Button>(R.id.btnVerArquivos)
        btnVerArquivos.setOnClickListener{
            var intent = Intent(this, listarArquivos::class.java)
            startActivity(intent)
        }


    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == COARSE_REQUEST && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            this.obterLocalizacaoPelaRede()
        } else if (requestCode == FINE_REQUEST && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            this.obterlocalizacaoPeloGps()
        }


    }

    private fun escreveLocalizacao(location: Location) {
        var count = 1

        val file = File(
            this.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS),
            "arquivoLocalizacao" + count + ".crd"
        )
        val fos = FileOutputStream(file)

        val date = SimpleDateFormat("dd/M/yyyy hh:mm:ss", )
        val currentDate = date.format(Date())

        var texto =
            "latitude: " + location.latitude.toString() + "  Longitude: " + location.longitude.toString() + " --- " + currentDate + "\n"


        fos.write(texto.toByteArray())
        fos.close()

        Toast.makeText(this, "Arquivo salvo", Toast.LENGTH_LONG).show()
        count++
    }

    private fun obterLocalizacaoPelaRede(): Location? {
        var location: Location? = null
        var locationManager = this.getSystemService(LOCATION_SERVICE) as LocationManager

        val isProviderEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)

        if (isProviderEnabled) {

            if (checkSelfPermission(android.Manifest.permission.ACCESS_COARSE_LOCATION)
                == PackageManager.PERMISSION_GRANTED
            ) {

                locationManager.requestLocationUpdates(
                    LocationManager.NETWORK_PROVIDER,
                    2000L,
                    0f,
                    this
                )

                location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
                return location
            } else {
                this.requestPermissions(
                    arrayOf(android.Manifest.permission.ACCESS_COARSE_LOCATION),
                    COARSE_REQUEST
                )
                return location
            }
        }
        return location
    }

    private fun obterlocalizacaoPeloGps(): Location? {
        var location: Location? = null
        val locationManager = this.getSystemService(LOCATION_SERVICE) as LocationManager
        val isGpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
        if (isGpsEnabled) {

            if (checkSelfPermission(android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED
            ) {
                locationManager.requestLocationUpdates(
                    LocationManager.NETWORK_PROVIDER,
                    2000L, 0f, this
                )

                location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)

                return location
            }
            return location
        } else {
            requestPermissions(
                arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                FINE_REQUEST
            )
            return location
        }
    }

    override fun onLocationChanged(location: Location) {

    }
}