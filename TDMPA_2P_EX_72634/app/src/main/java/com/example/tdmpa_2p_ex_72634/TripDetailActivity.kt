package com.example.tdmpa_2p_ex_72634

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView

class TripDetailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_trip_detail)

        val txtDetalleViaje = findViewById<TextView>(R.id.txtDetalleViaje)
        val btnVolver = findViewById<Button>(R.id.btnVolver)

        val Nombre = intent.extras?.getString("Nombre")
        val Destino = intent.extras?.getString("Destino")
        val FechaSalida = intent.extras?.getString("FechaSalida")
        val FechaRegreso = intent.extras?.getString("FechaRegreso")
        val DiasDuracion = intent.extras?.getString("DiasDuracion")
        val Actividades = intent.extras?.getString("Actividades")

        txtDetalleViaje.text = "${Nombre}\nDestino: ${Destino}\n\nFecha de Salida: ${FechaSalida}\n\nFecha de Regreso: ${FechaRegreso}\n\nDias de Duracion: ${DiasDuracion}\n\nActividades:\n${Actividades}"


        btnVolver.setOnClickListener {
            finish()
        }
    }
}