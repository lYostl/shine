package com.example.shine

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import java.util.*

class AgendaActivity : AppCompatActivity() {

    private lateinit var tvFechaSeleccionada: TextView
    private lateinit var tvHoraSeleccionada: TextView
    private lateinit var btnFecha: Button
    private lateinit var btnHora: Button
    private lateinit var btnGuardar: Button
    private lateinit var spinnerServicios: Spinner

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_agendamiento)

        // Inicializar vistas
        tvFechaSeleccionada = findViewById(R.id.tv_fecha_seleccionada)
        tvHoraSeleccionada = findViewById(R.id.tv_hora_seleccionada)
        btnFecha = findViewById(R.id.btn_fecha)
        btnHora = findViewById(R.id.btn_hora)
        btnGuardar = findViewById(R.id.btn_guardar)
        spinnerServicios = findViewById(R.id.spinner_servicios)

        // Establecer listeners para botones
        btnFecha.setOnClickListener { mostrarDatePicker() }
        btnHora.setOnClickListener { mostrarTimePicker() }
        btnGuardar.setOnClickListener { confirmarAgendamiento() }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.activity_agendamiento)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun mostrarDatePicker() {
        val calendar = Calendar.getInstance()
        val año = calendar.get(Calendar.YEAR)
        val mes = calendar.get(Calendar.MONTH)
        val dia = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(this, { _, year, month, dayOfMonth ->
            val fechaSeleccionada = "$dayOfMonth/${month + 1}/$year"
            tvFechaSeleccionada.text = "Fecha seleccionada: $fechaSeleccionada"
        }, año, mes, dia)

        // Establecer la fecha mínima como la fecha actual
        datePickerDialog.datePicker.minDate = System.currentTimeMillis()
        datePickerDialog.show()
    }

    private fun mostrarTimePicker() {
        val calendar = Calendar.getInstance()
        val hora = calendar.get(Calendar.HOUR_OF_DAY)
        val minuto = calendar.get(Calendar.MINUTE)

        val timePickerDialog = TimePickerDialog(this, { _, hourOfDay, minute ->
            val horaSeleccionada = String.format("%02d:%02d", hourOfDay, minute)
            tvHoraSeleccionada.text = "Hora seleccionada: $horaSeleccionada"
        }, hora, minuto, true)

        timePickerDialog.show()
    }

    private fun confirmarAgendamiento() {
        val servicioSeleccionado = spinnerServicios.selectedItem.toString()
        val fechaSeleccionada = tvFechaSeleccionada.text.toString()
        val horaSeleccionada = tvHoraSeleccionada.text.toString()

        val mensajeConfirmacion = "Servicio: $servicioSeleccionado\n$fechaSeleccionada\n$horaSeleccionada"

        // Muestra un mensaje de confirmación (puedes cambiarlo a un Toast o AlertDialog)
        Toast.makeText(this, mensajeConfirmacion, Toast.LENGTH_LONG).show()
    }
}

