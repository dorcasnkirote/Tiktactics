package com.example.tiktacticssignup_login.screens;

import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.tiktacticssignup_login.R
import com.google.android.material.textfield.TextInputEditText

class SignUp : AppCompatActivity() {

    private lateinit var editTextFirstName: TextInputEditText
    private lateinit var editTextLastName: TextInputEditText
    private lateinit var editTextEmail: TextInputEditText
    private lateinit var editTextPassword: TextInputEditText
    private lateinit var buttonSignUp: Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_sign_up)
        editTextFirstName = findViewById(R.id.firstname)
        editTextLastName = findViewById(R.id.lastname)
        editTextEmail = findViewById(R.id.email)
        editTextPassword = findViewById(R.id.password)
        buttonSignUp = findViewById(R.id.btn_signup)

        buttonSignUp.setOnClickListener {
            val firstname = editTextFirstName.text.toString()
            val lastname = editTextLastName.text.toString()
            val email = editTextEmail.text.toString()
            val password = editTextPassword.text.toString()

            if (TextUtils.isEmpty(firstname)) {
                Toast.makeText(this, "First name Required", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (TextUtils.isEmpty(lastname)) {
                Toast.makeText(this, "Last name Required", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (TextUtils.isEmpty(email)) {
                Toast.makeText(this, "Email Required", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (TextUtils.isEmpty(password)) {
                Toast.makeText(this, "Password Required", Toast.LENGTH_SHORT).show()
            }
        }
    }
}


