package com.example.tiktacticssignup_login.screens.signUp;

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.tiktacticssignup_login.R
import com.example.tiktacticssignup_login.screens.MainActivity
import com.example.tiktacticssignup_login.screens.login.Login
import com.google.android.material.textfield.TextInputEditText
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class SignUp : AppCompatActivity() {

    private lateinit var editTextFirstName: TextInputEditText
    private lateinit var editTextLastName: TextInputEditText
    private lateinit var editTextEmail: TextInputEditText
    private lateinit var editTextPassword: TextInputEditText
    private lateinit var buttonSignUp: Button
    private lateinit var progressBar: ProgressBar

    private val viewModel: SignUpViewModel by viewModels<SignUpViewModel>()




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        editTextFirstName = findViewById(R.id.firstname)
        editTextLastName = findViewById(R.id.lastname)
        editTextEmail = findViewById(R.id.email)
        editTextPassword = findViewById(R.id.password)
        buttonSignUp = findViewById(R.id.btn_signup)
        progressBar = findViewById(R.id.pb_loading)

        observeStateChanges()

        observeOneTimeEvents()



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
            viewModel.signUpUser(firstname, lastname, email, password)
        }
    }


    private fun observeOneTimeEvents() {
        lifecycleScope.launch {
            viewModel.oneTimeEvents.collectLatest { event ->
                when (event) {
                    OneTimeEvents.Navigate -> {
                        // User has to login so as to acquire the token
                        val intent = Intent(this@SignUp, Login::class.java)
                        startActivity(intent)
                        finish()
                    }

                    is OneTimeEvents.ShowToast -> {
                        Toast.makeText(this@SignUp, event.message, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }


    private fun observeStateChanges() {
        lifecycleScope.launch {
            viewModel.isLoading.collectLatest {
                if (it) {
                    buttonSignUp.visibility = View.GONE
                    progressBar.visibility = View.VISIBLE
                } else {
                    buttonSignUp.visibility = View.VISIBLE
                    progressBar.visibility = View.GONE
                }
            }
        }
    }
}


