package com.example.shine

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class RegistroActivity : AppCompatActivity() {

    private lateinit var correoInput: EditText
    private lateinit var usuarioInput: EditText
    private lateinit var passwordInput: EditText
    private lateinit var btnRegistrar: Button
    private lateinit var dbHelper: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registro)

        // Inicializa los elementos de la interfaz
        correoInput = findViewById(R.id.et_correo_registro)
        usuarioInput = findViewById(R.id.et_usuario_registro)
        passwordInput = findViewById(R.id.et_password_registro)
        btnRegistrar = findViewById(R.id.btn_registrar)

        // Inicializa el DatabaseHelper
        dbHelper = DatabaseHelper(this)

        btnRegistrar.setOnClickListener {
            // Captura los datos ingresados
            val correo = correoInput.text.toString()
            val usuario = usuarioInput.text.toString()
            val password = passwordInput.text.toString()

            if (correo.isNotEmpty() && usuario.isNotEmpty() && password.isNotEmpty()) {
                // Llama al método addUser para agregar el usuario a la base de datos
                val isUserAdded = dbHelper.addUser(correo, usuario, password)
                if (isUserAdded) {
                    Toast.makeText(this, "Usuario registrado: $usuario", Toast.LENGTH_SHORT).show()
                    finish() // Finaliza la actividad después del registro exitoso
                } else {
                    Toast.makeText(this, "Error al registrar el usuario", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show()
            }
        }
    }
}

