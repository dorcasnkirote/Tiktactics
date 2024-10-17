package com.example.tiktacticssignup_login.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.tiktacticssignup_login.data.local.entities.SpamEmailEntity
import com.kamilimu.tiktaktics.data.local.entities.EmailEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface SpamEmailsDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveSpamEmails(emails: List<SpamEmailEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveSpamEmail(email: SpamEmailEntity)

    @Query("SELECT * FROM spam_emails")
    fun getAllSpamEmails(): Flow<List<SpamEmailEntity>>

    @Query("SELECT COUNT(*) FROM spam_emails")
    suspend fun getSpamEmailCount(): Int

    @Query("SELECT EXISTS(SELECT 1 FROM spam_emails WHERE id = :emailId)")
    suspend fun doesSpamEmailExist(emailId: String): Boolean
}