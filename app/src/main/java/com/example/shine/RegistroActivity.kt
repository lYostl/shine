package com.example.shine

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class RegistroActivity : AppCompatActivity() {

    lateinit var correoInput: EditText
    lateinit var usuarioInput: EditText
    lateinit var passwordInput: EditText
    lateinit var btnRegistrar: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registro)

        // Inicializa los elementos de la interfaz
        correoInput = findViewById(R.id.et_correo_registro)
        usuarioInput = findViewById(R.id.et_usuario_registro)
        passwordInput = findViewById(R.id.et_password_registro)
        btnRegistrar = findViewById(R.id.btn_registrar)


        btnRegistrar.setOnClickListener {
            // Captura los datos ingresados
            val correo = correoInput.text.toString()
            val usuario = usuarioInput.text.toString()
            val password = passwordInput.text.toString()


            if (correo.isNotEmpty() && usuario.isNotEmpty() && password.isNotEmpty()) {
                Toast.makeText(this, "Usuario registrado: $usuario", Toast.LENGTH_SHORT).show()
                finish()
            } else {
                Toast.makeText(this, "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
