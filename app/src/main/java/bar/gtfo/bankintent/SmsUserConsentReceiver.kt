package bar.gtfo.bankintent

import android.app.Activity
import android.content.*
import android.os.Build
import android.util.Log
import androidx.activity.result.contract.ActivityResultContract
import androidx.annotation.RequiresApi
import com.google.android.gms.auth.api.phone.SmsRetriever
import com.google.android.gms.common.api.CommonStatusCodes
import com.google.android.gms.common.api.Status

class SmsUserConsentReceiver(private val activity: Activity): BroadcastReceiver() {

    val receiver = this

    fun init() {
        val task = SmsRetriever.getClient(activity).startSmsUserConsent(null)
        val intentFilter = IntentFilter(SmsRetriever.SMS_RETRIEVED_ACTION)
        activity.registerReceiver(receiver, intentFilter, SmsRetriever.SEND_PERMISSION, null)
    }

    private val SMS_CONSENT_REQUEST = 2  // Set to an unused reqbtuest code

    override fun onReceive(context: Context, intent: Intent) {
        if (SmsRetriever.SMS_RETRIEVED_ACTION == intent.action) {
            val extras = intent.extras
            val smsRetrieverStatus = extras?.get(SmsRetriever.EXTRA_STATUS) as Status

            when (smsRetrieverStatus.statusCode) {
                CommonStatusCodes.SUCCESS -> {
                    // Get consent intent
                    val consentIntent = extras.getParcelable<Intent>(SmsRetriever.EXTRA_CONSENT_INTENT)
                    try {
                        // Start activity to show consent dialog to user, activity must be started in
                        // 5 minutes, otherwise you'll receive another TIMEOUT intent
                        activity.startActivityForResult(consentIntent, SMS_CONSENT_REQUEST)
                    } catch (e: ActivityNotFoundException) {
                        Log.e(this.toString(), "Activity not found?")
                    }
                }
                CommonStatusCodes.TIMEOUT -> {
                    // Time out occurred, handle the error.
                }
            }
        }
    }

    class SmsConsentContract() : ActivityResultContract<Intent, String?>() {

        override fun createIntent(context: Context, input: Intent) = input

        override fun parseResult(resultCode: Int, intent: Intent?) =
            intent?.getStringExtra(SmsRetriever.EXTRA_SMS_MESSAGE)
    }
}