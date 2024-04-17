package com.example.tdmpa_2p_ex_72634

import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import java.text.SimpleDateFormat
import java.util.Date
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {
    val listaViajes: MutableList<Viaje> = mutableListOf()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val spnFiltros = findViewById<Spinner>(R.id.spnFiltros)
        val filtros: Array<String> = resources.getStringArray(R.array.Filtro)

        val adapter: ArrayAdapter<String> = ArrayAdapter<String>(
            this,
            android.R.layout.simple_dropdown_item_1line,
            filtros
        )
        spnFiltros.setAdapter(adapter)

        val txtNombre = findViewById<EditText>(R.id.txtNombre)
        val txtDestino = findViewById<EditText>(R.id.txtDestino)
        val txtFechaSalida = findViewById<EditText>(R.id.txtFechaSalida)
        val txtFechaRegreso = findViewById<EditText>(R.id.txtFechaRegreso)
        val txtDiasDuracion = findViewById<TextView>(R.id.txtDiasDuracion)

        val chpSenderismo = findViewById<Chip>(R.id.chpSenderismo)
        val chpBuceo = findViewById<Chip>(R.id.chpBuceo)
        val chpSnowboard = findViewById<Chip>(R.id.chpSnowboard)

        val btnGuardar = findViewById<Button>(R.id.btnGuardar)

        txtFechaRegreso.setOnKeyListener{_, keyCode, event->
            if(event.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                val dias = calcularDias(txtFechaSalida.text.toString(), txtFechaRegreso.text.toString())
                txtDiasDuracion.text = dias.toString()
            }
            false
        }

        btnGuardar.setOnClickListener {
            val listaActividades: MutableList<String> = mutableListOf()
            if (chpSenderismo.isChecked){
                listaActividades.add("${chpSenderismo.text}\n")
            }
            if (chpBuceo.isChecked){
                listaActividades.add("${chpBuceo.text}\n")
            }
            if (chpSnowboard.isChecked){
                listaActividades.add("${chpSnowboard.text}\n")
            }
            listaViajes.add(Viaje(
                txtNombre.text.toString(),
                txtDestino.text.toString(),
                txtFechaSalida.text.toString(),
                txtFechaRegreso.text.toString(),
                txtDiasDuracion.text.toString().toInt(),
                listaActividades
            ))
            agregarChipViaje(txtNombre.text.toString())
            txtNombre.text = null
            txtDestino.text = null
            txtFechaSalida.text = null
            txtFechaRegreso.text = null
            txtDiasDuracion.text = null
            if (chpSenderismo.isChecked){
                chpSenderismo.isChecked = false
            }
            if (chpBuceo.isChecked){
                chpBuceo.isChecked = false
            }
            if (chpSnowboard.isChecked){
                chpSnowboard.isChecked = false
            }
        }

        spnFiltros.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener{
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id:Long) {
                val opcion = filtros[position].toString()
                if (opcion == "Todos"){
                    filtroTodos()
                }
                if (opcion == "Proximos"){
                    filtroPoximos()
                }
                if (opcion == "Pasados"){
                    filtroPasados()
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                Toast.makeText(this@MainActivity, "No se selecciono carrera", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun agregarChipViaje(viaje:String){
        val chip = Chip(this@MainActivity)
        chip.text = viaje
        chip.isClickable = true
        chip.isCheckable = false
        chip.isCloseIconEnabled = true
        chip.closeIconTint = ColorStateList.valueOf(Color.RED)
        val chgViajes = findViewById<ChipGroup>(R.id.chgViajes)
        chgViajes.addView(chip as View)
        chip.setOnClickListener {
            val intent = Intent(this, TripDetailActivity::class.java)
            for (viaje in listaViajes) {
                if (viaje.Nombre == chip.text) {
                    intent.putExtra("Nombre", viaje.Nombre)
                    intent.putExtra("Destino", viaje.Destino)
                    intent.putExtra("FechaSalida", viaje.FechaSalida)
                    intent.putExtra("FechaRegreso", viaje.FechaRegreso)
                    intent.putExtra("DiasDuracion", viaje.DiasDuracion.toString())
                    intent.putExtra("Actividades", viaje.Actividades.joinToString(""))
                    startActivity(intent)
                }
            }
        }
        chip.setOnCloseIconClickListener{
            for (viaje in listaViajes){
                if (viaje.Nombre == chip.text){
                    listaViajes.remove(viaje)
                }
            }
            chgViajes.removeView(chip as View)
        }
    }

    private fun calcularDias(fechaInicio:String, fechaFinal:String): Int {
        val dateFormat = SimpleDateFormat("dd/MM/yyyy") // Especifica el formato de fecha de tu cadena

        val fechaDeSalida = dateFormat.parse(fechaInicio)
        val fechaDeRegreso = dateFormat.parse(fechaFinal)

        val diferenciaTiempo = fechaDeRegreso.time - fechaDeSalida.time
        val dias = TimeUnit.MILLISECONDS.toDays(diferenciaTiempo)

        return dias.toInt()
    }

    private fun filtroPasados(){
        val chgViajes = findViewById<ChipGroup>(R.id.chgViajes)
        chgViajes.removeAllViews()
        val dateFormat = SimpleDateFormat("dd/MM/yyyy")
        for (viaje in listaViajes){
            val fecha = dateFormat.parse(viaje.FechaRegreso)
            if (fecha.before(Date())){
                agregarChipViaje(viaje.Nombre)
            }
        }
    }

    private fun filtroPoximos(){
        val chgViajes = findViewById<ChipGroup>(R.id.chgViajes)
        chgViajes.removeAllViews()
        val dateFormat = SimpleDateFormat("dd/MM/yyyy")
        for (viaje in listaViajes){
            val fecha = dateFormat.parse(viaje.FechaSalida)
            if (fecha.after(Date())){
                agregarChipViaje(viaje.Nombre)
            }
        }
    }

    private fun filtroTodos(){
        val chgViajes = findViewById<ChipGroup>(R.id.chgViajes)
        chgViajes.removeAllViews()
        for (viaje in listaViajes){
            agregarChipViaje(viaje.Nombre)
        }
    }
}