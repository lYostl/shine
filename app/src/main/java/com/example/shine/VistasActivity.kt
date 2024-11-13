package com.example.shine

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class VistasActivity : AppCompatActivity() {

    private lateinit var tvCitas: TextView
    private lateinit var dbHelper: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_vistas)

        // Inicializar vistas
        tvCitas = findViewById(R.id.tv_citas)

        // Inicializar DatabaseHelper
        dbHelper = DatabaseHelper(this)

        // Mostrar citas almacenadas
        mostrarCitasAgendadas()
    }

    private fun mostrarCitasAgendadas() {
        val citas = dbHelper.getAppointments()
        val citasTexto = if (citas.isNotEmpty()) {
            citas.joinToString(separator = "\n\n")
        } else {
            "No hay citas agendadas."
        }
        tvCitas.text = citasTexto
    }
}
