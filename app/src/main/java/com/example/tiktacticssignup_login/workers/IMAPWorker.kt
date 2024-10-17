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
import com.example.tiktacticssignup_login.data.datastore.PreferenceManager
import com.example.tiktacticssignup_login.data.local.EmailDatabase
import com.example.tiktacticssignup_login.data.remote.TiktacticsApiService
import com.example.tiktacticssignup_login.data.remote.dtos.request.EmailsDto
import com.example.tiktacticssignup_login.data.remote.dtos.request.RefreshTokenRequestDto
import com.kamilimu.tiktaktics.utils.IMAPLogic
import com.example.tiktacticssignup_login.utils.NotificationUtil
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.concurrent.TimeUnit
import kotlin.math.log

private const val TAG = "IMAPWorker"

class IMAPWorker(appContext: Context, workerParams: WorkerParameters) :
    CoroutineWorker(appContext, workerParams) {

    private val notificationUtil = NotificationUtil(applicationContext)

    private val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())


    private var job: Job? = null

    private val preferencesManager = PreferenceManager(applicationContext)


    override suspend fun doWork(): Result = withContext(Dispatchers.IO) {
        try {
            val email = preferencesManager.getUserEmail().first() ?: ""
            val userEmailPassword = preferencesManager.getUserEmailAppPassword().first() ?: ""


            Log.d(TAG, "doWork User email: $email")
            Log.d(TAG, "doWork Email Password: $userEmailPassword")

            val spamEmailsDao = EmailDatabase.getInstance(applicationContext).spamEmailsDao()

            val imapLogic = IMAPLogic(
                onNewEmails = { recentEmails ->
                    val emails = recentEmails.map { mailContent ->
                        mailContent.toEmailDto()
                    }
                    val emailsDto = EmailsDto(emails)
                    scope.launch {

                        val refreshToken = preferencesManager.getRefreshToken().first() ?: ""
                        val refreshTokenResponseDto = TiktacticsApiService.getInstance()
                            .refreshToken(RefreshTokenRequestDto(refresh = refreshToken))
                        preferencesManager.saveAuthToken(refreshTokenResponseDto.access)
                        preferencesManager.saveRefreshToken(refreshTokenResponseDto.refresh)

                        delay(1600)
                        val apiService = TiktacticsApiService.getInstance(
                            preferencesManager.getAuthToken().first()
                        )
                        val spamEmailsDto = apiService.startDetection(emailsDto)
                        spamEmailsDao.saveSpamEmails(spamEmailsDto.spamEmailDtos.map { it.toSpamEmailEntity() })
                        if (spamEmailsDto.spamEmailDtos.isNotEmpty()) {
                            for (spamEmailDto in spamEmailsDto.spamEmailDtos) {
                                notificationUtil.showNotification(
                                    title = "New Spam Email Detected from ${spamEmailDto.sender}",
                                    message = spamEmailDto.subject,
                                    notificationId = spamEmailDto.id
                                )
                            }
                        }
                    }
                },
                emailDao = EmailDatabase.getInstance(applicationContext).emailsDao(),
                userEmail = email,
                userEmailAppPassword = userEmailPassword
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
                ExistingPeriodicWorkPolicy.CANCEL_AND_REENQUEUE,
                emailWorkRequest
            )
        }
    }


}