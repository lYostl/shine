package com.example.shine

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import android.widget.Button
import android.content.Intent

class MenuActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)

        val usuario = intent.getStringExtra("USERNAME")

        val tvBienvenido: TextView = findViewById(R.id.tv_bienvenido)
        tvBienvenido.text = "Bienvenido $usuario"

        // Referencia al botón de agendar hora
        val btnAgendarHora: Button = findViewById(R.id.btn_agendar)

        // Establecer un listener para el botón
        btnAgendarHora.setOnClickListener {
            // Iniciar AgendaActivity
            val intent = Intent(this, AgendaActivity::class.java)
            startActivity(intent)
        }

        // Referencia al botón de ver historial de reserva
        val btnVerHistorial: Button = findViewById(R.id.btn_historial)

        // Establecer un listener para el botón
        btnVerHistorial.setOnClickListener {
            // Iniciar VistasActivity
            val intent = Intent(this, VistasActivity::class.java)
            startActivity(intent)
        }
    }
}
