package com.example.tiktacticssignup_login.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.tiktacticssignup_login.data.local.entities.SpamEmailEntity
import com.kamilimu.tiktaktics.data.local.entities.EmailEntity

@Database(
    entities = [EmailEntity::class, SpamEmailEntity::class],
    version = 2,
    exportSchema = false
)
abstract class EmailDatabase : RoomDatabase() {
    abstract fun emailsDao(): EmailsDao
    abstract fun spamEmailsDao(): SpamEmailsDao

    companion object {
        private var database: EmailDatabase? = null

        fun getInstance(context: Context): EmailDatabase {
            synchronized(this) {
                var instance = database
                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        EmailDatabase::class.java,
                        "email_database"
                    )
                        .fallbackToDestructiveMigration()
                        .build()
                }
                return instance
            }
        }
    }
}


