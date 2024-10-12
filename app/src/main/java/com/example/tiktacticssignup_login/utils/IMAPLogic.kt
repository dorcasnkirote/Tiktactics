package com.kamilimu.tiktaktics.utils

import android.util.Log
import com.example.tiktacticssignup_login.data.local.EmailsDao
import com.kamilimu.tiktaktics.data.local.entities.EmailEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import java.util.Properties
import javax.mail.Folder
import javax.mail.Message
import javax.mail.Session
import javax.mail.Store
import javax.mail.internet.MimeMessage


const val TAG = "IMAPLogic"

class IMAPLogic(
    private val onNewEmails: (List<MailContent>) -> Unit,
    private val emailDao: EmailsDao
) {
    private val stringMailHost = "imap.gmail.com"
    private var session: Session? = null
    private var store: Store? = null
    private var properties: Properties? = null
    private val stringUserName = "sammuigai880"
    private val stringPassword = "deuvqlvetrooondz"


    init {
        properties = System.getProperties()
        properties?.setProperty("mail.store.protocol", "imaps")
    }


    suspend fun connectImap() {
        withContext(Dispatchers.IO) {
            try {
                session = Session.getDefaultInstance(properties, null)
                store = session?.getStore("imaps") as Store
                store!!.connect(stringMailHost, stringUserName, stringPassword)
                Log.d(TAG, "connectImap: Success")
            } catch (e: Exception) {
                Log.d(TAG, "connectImap: Failure ${e.message}")
                e.printStackTrace()
            }
        }
    }

    fun readEmails(): Flow<List<MailContent>> = flow {
        while (true) {
            try {
                if (store?.isConnected != true) {
                    connectImap()
                    delay(5000) // Wait for connection to establish
                    continue
                }
                val recentEmails = getRecentEmails()
                if (recentEmails.isNotEmpty()) {
                    onNewEmails(recentEmails)
                }
                emit(recentEmails)
            } catch (e: Exception) {
                Log.e(TAG, "Error reading emails: ${e.message}")
                e.printStackTrace()
            }
            delay(10000) // Check for new emails every 10 seconds
        }
    }.flowOn(Dispatchers.IO)

    private suspend fun getRecentEmails(): List<MailContent> {
        return withContext(Dispatchers.IO) {
            val folder = store?.getFolder("Inbox")
            folder?.open(Folder.READ_ONLY)
            val messages: Array<Message> = folder?.messages ?: emptyArray()
            Log.d(TAG, "Total mails: ${messages.size}")

            // Perform the check before saving the emails
            val isFirstTimeInstalling = emailDao.getEmailCount() == 0
            Log.d(TAG, "isFirstTimeInstalling: $isFirstTimeInstalling")

            val recentEmails = messages.takeLast(20).mapNotNull { message ->
                val mailContent = processEmail(message as MimeMessage)
                if (!emailDao.doesEmailExist(mailContent.messageId)) {
                    mailContent
                } else {
                    null
                }
            }

            val recentEmailsEntities = recentEmails.map { it.toMailEntity() }
            emailDao.saveEmails(recentEmailsEntities)

            folder?.close(false)
            // If there are no new emails,it means it's the first time user is installing the application
            if (isFirstTimeInstalling) emptyList() else recentEmails
        }
    }

    private fun processEmail(message: MimeMessage): MailContent {
        val messageBody = message.content.toString()
        val mailContent = MailContent(
            from = message.from[0].toString(),
            subject = message.subject,
            sentDate = message.sentDate.toString(),
            content = messageBody,
            messageId = message.messageID
        )
        return mailContent
    }

}

data class MailContent(
    val from: String,
    val subject: String,
    val sentDate: String,
    val content: String,
    val messageId: String
){
    fun toMailEntity():EmailEntity{
        return EmailEntity(
            from = from,
            subject = subject,
            sentDate = sentDate,
            content = content,
            messageId = messageId
        )
    }
}


