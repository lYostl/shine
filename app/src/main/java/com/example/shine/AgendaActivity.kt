package com.example.shine

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.widget.ArrayAdapter
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

    private lateinit var etUsuarioConfirmacion: EditText
    private lateinit var tvFechaSeleccionada: TextView
    private lateinit var tvHoraSeleccionada: TextView
    private lateinit var tvAgendado: TextView
    private lateinit var btnFecha: Button
    private lateinit var btnHora: Button
    private lateinit var btnGuardar: Button
    private lateinit var spinnerServicios: Spinner
    private lateinit var dbHelper: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_agendamiento)

        // Inicializar vistas
        etUsuarioConfirmacion = findViewById(R.id.et_usuario_confirmacion)
        tvFechaSeleccionada = findViewById(R.id.tv_fecha_seleccionada)
        tvHoraSeleccionada = findViewById(R.id.tv_hora)
        tvAgendado = findViewById(R.id.tv_Agendado)
        btnFecha = findViewById(R.id.btn_fecha)
        btnHora = findViewById(R.id.btn_hora)
        btnGuardar = findViewById(R.id.btn_guardar)
        spinnerServicios = findViewById(R.id.spinner_servicios)

        // Inicializar DatabaseHelper
        dbHelper = DatabaseHelper(this)

        // Establecer adaptador para el Spinner de servicios
        val servicios = listOf(
            "Lavado exterior básico",
            "Lavado completo",
            "Lavado de motor",
            "Descontaminación de pintura",
            "Pulido de pintura",
            "Encerado",
            "Limpieza de tapicería",
            "Tratamiento de cuero",
            "Protección antirobo",
            "Desinfección interior",
            "Protección cerámica",
            "Limpieza de llantas y neumáticos"
        )
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, servicios)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerServicios.adapter = adapter

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
            actualizarAgendado()
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
            actualizarAgendado()
        }, hora, minuto, true)

        timePickerDialog.show()
    }

    private fun actualizarAgendado() {
        val fecha = tvFechaSeleccionada.text.toString().replace("Fecha seleccionada: ", "")
        val hora = tvHoraSeleccionada.text.toString().replace("Hora seleccionada: ", "")
        tvAgendado.text = "Agendamiento para: $fecha $hora"
    }

    private fun confirmarAgendamiento() {
        val usuarioConfirmacion = etUsuarioConfirmacion.text.toString()
        val servicioSeleccionado = spinnerServicios.selectedItem.toString()
        val fechaSeleccionada = tvFechaSeleccionada.text.toString()
        val horaSeleccionada = tvHoraSeleccionada.text.toString()

        if (usuarioConfirmacion.isNotEmpty() && servicioSeleccionado.isNotEmpty() && fechaSeleccionada.isNotEmpty() && horaSeleccionada.isNotEmpty()) {
            val userId = dbHelper.getUserId(usuarioConfirmacion)

            if (userId != -1) {
                val isAppointmentAdded = dbHelper.addAppointment(userId, servicioSeleccionado, fechaSeleccionada, horaSeleccionada)
                if (isAppointmentAdded) {
                    Toast.makeText(this, "Gracias por agendar tu servicio", Toast.LENGTH_LONG).show()
                    finish() // Finaliza la actividad después de confirmar la cita
                } else {
                    Toast.makeText(this, "Error al agendar el servicio", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Error al obtener el usuario", Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(this, "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show()
        }
    }
}
