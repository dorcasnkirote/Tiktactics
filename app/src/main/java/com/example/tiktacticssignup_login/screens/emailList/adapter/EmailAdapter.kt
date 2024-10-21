package com.example.tiktacticssignup_login.screens.emailList.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.tiktacticssignup_login.R
import com.example.tiktacticssignup_login.data.local.entities.SpamEmailEntity

class EmailAdapter(
    private val spamEmails: List<SpamEmailEntity>
) : RecyclerView.Adapter<EmailAdapter.EmailViewHolder>() {

    inner class EmailViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val emailTitle: TextView = view.findViewById(R.id.email_title)
        val emailDate: TextView = view.findViewById(R.id.email_date)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EmailViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.single_email, parent, false)
        return EmailViewHolder(view)
    }

    override fun getItemCount(): Int {
        return spamEmails.size
    }

    override fun onBindViewHolder(holder: EmailViewHolder, position: Int) {
        val spamEmail = spamEmails[position]
        holder.emailTitle.text = spamEmail.subject
        holder.emailDate.text = spamEmail.detectedOn
    }
}