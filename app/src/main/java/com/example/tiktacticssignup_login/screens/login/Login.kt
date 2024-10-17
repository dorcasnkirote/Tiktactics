package com.example.tiktacticssignup_login.screens.login;

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.tiktacticssignup_login.R
import com.example.tiktacticssignup_login.data.datastore.PreferenceManager
import com.example.tiktacticssignup_login.screens.MainActivity
import com.example.tiktacticssignup_login.screens.signUp.SignUp
import com.example.tiktacticssignup_login.workers.IMAPWorker
import com.google.android.material.textfield.TextInputEditText
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class Login : AppCompatActivity() {
    private lateinit var editTextEmail: TextInputEditText
    private lateinit var editTextPassword: TextInputEditText
    private lateinit var buttonLogin: Button
    private lateinit var textView: TextView
    private lateinit var progressBar: ProgressBar


    private val viewModel: LoginViewModel by viewModels<LoginViewModel>()

    private val preferenceManager = PreferenceManager(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        editTextEmail = findViewById(R.id.email)
        editTextPassword = findViewById(R.id.password)
        buttonLogin = findViewById(R.id.btn_login)
        textView = findViewById(R.id.sign_up_text)
        progressBar = findViewById(R.id.progressBar)

        observeStateChanges()
        observeOneTimeEvents()

        textView.setOnClickListener {
            val intent = Intent(applicationContext, SignUp::class.java)
            startActivity(intent)
        }

        buttonLogin.setOnClickListener {
            val email = editTextEmail.text.toString()
            val password = editTextPassword.text.toString()
            if (TextUtils.isEmpty(email)) {
                Toast.makeText(this, "Enter Email", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (TextUtils.isEmpty(password)) {
                Toast.makeText(this, "Enter Password", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            viewModel.signInUser(email, password)
        }
    }

    private fun observeOneTimeEvents() {
        lifecycleScope.launch {
            viewModel.oneTimeEvents.collectLatest { event ->
                when (event) {
                    is OneTimeEvents.Navigate -> {
                        val email = editTextEmail.text.toString()
                        val refreshToken = event.loginResponseDto.refresh
                        preferenceManager.saveAuthToken(event.loginResponseDto.access)
                        preferenceManager.saveUserEmail(email)
                        preferenceManager.saveRefreshToken(refreshToken)

                        val intent = Intent(this@Login, MainActivity::class.java)
                        startActivity(intent)
                        finish()
                    }

                    is OneTimeEvents.ShowToast -> {
                        Toast.makeText(this@Login, event.message, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }


    private fun observeStateChanges() {
        lifecycleScope.launch {
            viewModel.isLoading.collectLatest {
                if (it) {
                    buttonLogin.visibility = View.GONE
                    progressBar.visibility = View.VISIBLE
                } else {
                    buttonLogin.visibility = View.VISIBLE
                    progressBar.visibility = View.GONE
                }
            }
        }
    }



}
