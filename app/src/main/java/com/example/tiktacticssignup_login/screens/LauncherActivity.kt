package com.example.tiktacticssignup_login.screens;



import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.tiktacticssignup_login.R

class LauncherActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_launcher)

        Handler().postDelayed({
                val intent = Intent(this, Login::class.java)
        startActivity(intent)

        overridePendingTransition(R.anim.fade_in, R.anim.fade_out)

        finish()
        }, 3000)
    }
}

