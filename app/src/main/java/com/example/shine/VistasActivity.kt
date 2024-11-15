package com.example.shine

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity

class VistasActivity : AppCompatActivity() {

    private lateinit var dbHelper: DatabaseHelper
    private lateinit var layoutCitas: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_vistas)

        // Inicializar vistas
        layoutCitas = findViewById(R.id.layout_citas)

        // Inicializar DatabaseHelper
        dbHelper = DatabaseHelper(this)

        // Mostrar citas almacenadas
        mostrarCitasAgendadas()
    }

    private fun mostrarCitasAgendadas() {
        layoutCitas.removeAllViews() // Limpiar las vistas anteriores

        val citas = dbHelper.getAppointments() // Obtener todas las citas
        if (citas.isNotEmpty()) {
            for (cita in citas) {
                // Crear programáticamente la vista para cada cita
                val citaView = LinearLayout(this).apply {
                    orientation = LinearLayout.VERTICAL
                    setPadding(16, 16, 16, 16)
                    layoutParams = LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                    ).apply {
                        setMargins(0, 0, 0, 16)
                    }
                    setBackgroundResource(R.drawable.card_background) // Fondo personalizado para cada cita
                }

                // Crear TextView para el nombre del servicio
                val tvServicio = TextView(this).apply {
                    text = "Servicio: ${cita.split("\n")[0].replace("Servicio: ", "")}"
                    textSize = 20f
                    setTextColor(resources.getColor(R.color.white, null))
                    setPadding(0, 0, 0, 8)
                }

                // Crear TextView para la fecha y hora
                val tvFechaHora = TextView(this).apply {
                    text = cita.split("\n")[1] + "\n" + cita.split("\n")[2]
                    textSize = 16f
                    setTextColor(resources.getColor(R.color.grey, null))
                    setPadding(0, 0, 0, 8)
                }

                // Crear botón para cancelar la cita
                val btnCancelarCita = Button(this).apply {
                    text = "CANCELAR"
                    textSize = 16f
                    setTextColor(resources.getColor(R.color.white, null))
                    setBackgroundColor(resources.getColor(R.color.red, null))
                    setOnClickListener {
                        eliminarCita(cita.split("\n")[0].replace("Servicio: ", "").hashCode())
                    }
                }

                // Añadir los elementos a la vista de la cita
                citaView.addView(tvServicio)
                citaView.addView(tvFechaHora)
                citaView.addView(btnCancelarCita)

                // Añadir la vista de la cita al layout principal
                layoutCitas.addView(citaView)
            }
        } else {
            val noCitasView = TextView(this).apply {
                text = "No hay citas agendadas."
                textSize = 16f
                setTextColor(resources.getColor(R.color.white, null))
            }
            layoutCitas.addView(noCitasView)
        }
    }

    private fun eliminarCita(citaId: Int) {
        val dialog = AlertDialog.Builder(this)
            .setTitle("Cancelar Cita")
            .setMessage("¿Estás seguro de que deseas cancelar esta cita?")
            .setPositiveButton("Sí") { _, _ ->
                val isDeleted = dbHelper.deleteAppointmentById(citaId)
                if (isDeleted) {
                    Toast.makeText(this, "Cita eliminada con éxito", Toast.LENGTH_SHORT).show()
                    mostrarCitasAgendadas() // Refresca la lista de citas
                } else {
                    Toast.makeText(this, "Error al eliminar la cita", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("No", null)
            .create()
        dialog.show()
    }
}

