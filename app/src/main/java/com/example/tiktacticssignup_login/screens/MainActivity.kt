package com.example.tiktacticssignup_login.screens

import android.content.Intent
import android.os.Bundle
import android.widget.Button

import androidx.activity.enableEdgeToEdge
import androidx.appcompat.widget.Toolbar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.Insets
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.example.tiktacticssignup_login.R
import com.example.tiktacticssignup_login.data.datastore.PreferenceManager
import com.example.tiktacticssignup_login.screens.emailList.EmailList
import com.example.tiktacticssignup_login.workers.IMAPWorker
import com.google.android.material.textfield.TextInputEditText
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class MainActivity : AppCompatActivity() {

    private val preferenceManager = PreferenceManager(this)
    private lateinit var emailAppPasswordTextField: TextInputEditText

    private lateinit var btnInitiateProtection: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        emailAppPasswordTextField = findViewById(R.id.appPassword)
        btnInitiateProtection = findViewById(R.id.btnInitiateProtection)


        btnInitiateProtection.setOnClickListener {
            val emailPassword = emailAppPasswordTextField.text.toString().removeSpaces()
            if(emailPassword.length != 16){
                emailAppPasswordTextField.error = "Invalid Email App Password"
                return@setOnClickListener
            }
            saveEmailAppPassword(emailPassword)
            val intent = Intent(this, EmailList::class.java)
            startActivity(intent)
        }
    }

    private fun saveEmailAppPassword(emailPassword: String) {
        lifecycleScope.launch {
            preferenceManager.saveUserEmailAppPassword(emailPassword)
            delay(1000)
            startBackgroundListeningToEmails()
        }
    }

    private fun startBackgroundListeningToEmails() {
        IMAPWorker.scheduleEmailCheck(applicationContext)
    }
}

private fun String.removeSpaces(): String {
    return this.replace(" ", "").trim()
}





