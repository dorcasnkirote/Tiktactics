package com.example.tiktacticssignup_login.screens.emailList

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tiktacticssignup_login.R
import com.example.tiktacticssignup_login.data.local.EmailDatabase
import com.example.tiktacticssignup_login.screens.emailList.adapter.EmailAdapter
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class EmailList : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_email_list)
        val emailDb = EmailDatabase.getInstance(this)
        val emailDao = emailDb.spamEmailsDao()

        val rvEmails = findViewById<RecyclerView>(R.id.email_recycler_view)
        rvEmails.layoutManager = LinearLayoutManager(this)
        lifecycleScope.launch {
            emailDao.getAllSpamEmails().collectLatest { emails ->
                val rvAdapter = EmailAdapter(emails)
                rvEmails.adapter = rvAdapter
            }
        }
    }
}