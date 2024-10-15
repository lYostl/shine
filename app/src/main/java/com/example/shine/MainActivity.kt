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

    lateinit var usuarioInput: EditText
    lateinit var passwordInput: EditText
    lateinit var loginBtn: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)


        usuarioInput = findViewById(R.id.et_usuario)
        passwordInput = findViewById(R.id.et_password)
        loginBtn = findViewById(R.id.btn_login)


        loginBtn.setOnClickListener {

            val usuario = usuarioInput.text.toString()

            // Muestra un mensaje de bienvenida
            Toast.makeText(this, "Bienvenido, $usuario", Toast.LENGTH_LONG).show()

            // Inicia MenuActivity
            val intent = Intent(this, MenuActivity::class.java)
            intent.putExtra("USERNAME", usuario) // Cambia EMAIL a USERNAME
            startActivity(intent)
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}


