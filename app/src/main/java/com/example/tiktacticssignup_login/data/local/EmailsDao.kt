package com.example.tiktacticssignup_login.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.kamilimu.tiktaktics.data.local.entities.EmailEntity
import kotlinx.coroutines.flow.Flow


@Dao
interface EmailsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveEmails(emails: List<EmailEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveEmail(email: EmailEntity)

    @Query("SELECT * FROM emails")
    fun getAllEmails(): Flow<List<EmailEntity>>

    @Query("SELECT COUNT(*) FROM emails")
    suspend fun getEmailCount(): Int

    @Query("SELECT EXISTS(SELECT 1 FROM emails WHERE messageId = :emailId)")
    suspend fun doesEmailExist(emailId: String): Boolean

}