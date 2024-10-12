package com.example.tiktacticssignup_login.workers

import android.content.Context
import android.util.Log
import androidx.work.BackoffPolicy
import androidx.work.Constraints
import androidx.work.CoroutineWorker
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import com.example.tiktacticssignup_login.data.local.EmailDatabase
import com.kamilimu.tiktaktics.utils.IMAPLogic
import com.example.tiktacticssignup_login.utils.NotificationUtil
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.concurrent.TimeUnit

private const val TAG = "IMAPWorker"

class IMAPWorker(appContext: Context, workerParams: WorkerParameters) :
    CoroutineWorker(appContext, workerParams) {

    private val notificationUtil = NotificationUtil(applicationContext)


    private var job: Job? = null


    override suspend fun doWork(): Result = withContext(Dispatchers.IO) {
        try {
            val imapLogic = IMAPLogic(
                onNewEmails = { recentEmails ->
                    for (recentEmail in recentEmails) {
                        notificationUtil.showNotification(
                            recentEmail.messageId.hashCode(),
                            recentEmail.subject,
                            "You've got an email from: ${recentEmail.from}"
                        )

                        Log.d(TAG, "Email from: ${recentEmail.from}")
                    }
                },
                emailDao = EmailDatabase.getInstance(applicationContext).emailsDao()
            )
            job?.cancel()
            job = launch {
                imapLogic.readEmails().collectLatest { recentEmails ->
                    Log.d(TAG, "MailSize: ${recentEmails.size}")
                }
            }
            Result.success()
        } catch (e: Exception) {
            if (e is CancellationException) {
                Result.retry()
            } else {
                Result.failure()
            }
        }
    }

    companion object {
        private const val TAG = "IMAPWorker"
        private const val WORK_NAME = "EmailCheckWork"

        fun scheduleEmailCheck(context: Context) {
            val constraints = Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build()

            val emailWorkRequest = PeriodicWorkRequestBuilder<IMAPWorker>(15, TimeUnit.MINUTES)
                .setConstraints(constraints)
                .setBackoffCriteria(BackoffPolicy.LINEAR, 10, TimeUnit.SECONDS)
                .build()

            WorkManager.getInstance(context).enqueueUniquePeriodicWork(
                WORK_NAME,
                ExistingPeriodicWorkPolicy.UPDATE,
                emailWorkRequest
            )
        }
    }


}