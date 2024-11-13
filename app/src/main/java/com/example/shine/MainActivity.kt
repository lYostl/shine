package com.example.shine

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {

    private lateinit var usuarioInput: EditText
    private lateinit var passwordInput: EditText
    private lateinit var loginBtn: Button
    private lateinit var registroBtn: Button
    private lateinit var dbHelper: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        // Inicializar vistas
        usuarioInput = findViewById(R.id.et_usuario)
        passwordInput = findViewById(R.id.et_password)
        loginBtn = findViewById(R.id.btn_login)
        registroBtn = findViewById(R.id.btn_registro)

        // Inicializar DatabaseHelper
        dbHelper = DatabaseHelper(this)

        loginBtn.setOnClickListener {
            val usuario = usuarioInput.text.toString()
            val password = passwordInput.text.toString()

            if (usuario.isNotEmpty() && password.isNotEmpty()) {
                // Verifica si el usuario y contraseña existen en la base de datos
                val isValidUser = dbHelper.checkUser(usuario, password)
                if (isValidUser) {
                    // Si es un usuario válido, muestra mensaje de bienvenida e inicia MenuActivity
                    Toast.makeText(this, "Bienvenido, $usuario", Toast.LENGTH_LONG).show()
                    val intent = Intent(this, MenuActivity::class.java)
                    intent.putExtra("USERNAME", usuario)
                    startActivity(intent)
                } else {
                    Toast.makeText(this, "Usuario o contraseña incorrectos", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Por favor, ingresa tu usuario y contraseña", Toast.LENGTH_SHORT).show()
            }
        }

        // Configurar botón de registro para abrir RegistroActivity
        registroBtn.setOnClickListener {
            val intent = Intent(this, RegistroActivity::class.java)
            startActivity(intent)
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}
