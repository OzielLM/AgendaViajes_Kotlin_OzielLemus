package com.example.tdmpa_2p_ex_72634

class Viaje(
    nombre: String,
    destino: String,
    fechaSalida: String,
    fechaRegreso: String,
    diasDuracion: Int,
    actividades: MutableList<String>
) {
    val Nombre:String = nombre
    val Destino:String = destino
    val FechaSalida:String = fechaSalida
    val FechaRegreso:String = fechaRegreso
    val DiasDuracion:Int = diasDuracion
    val Actividades: MutableList<String> = actividades

}