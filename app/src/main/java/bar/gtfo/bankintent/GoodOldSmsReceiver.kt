package bar.gtfo.bankintent

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.telephony.SmsMessage
import android.util.Log
import androidx.core.app.ActivityCompat


class GoodOldSmsReceiver(private val activity: PhoneActivity) : BroadcastReceiver() {

    private val ACTION = "android.provider.Telephony.SMS_RECEIVED"
    private val PERMISSION = "android.permission.RECEIVE_SMS"
    private val MY_PERMISSIONS_REQUEST_SMS_RECEIVE = 10

    fun init() {
        val intentFilter = IntentFilter(ACTION)
        activity.registerReceiver(this, /* SmsRetriever.SEND_PERMISSION,  */ intentFilter)
        ActivityCompat.requestPermissions(activity,
            arrayOf(PERMISSION),
            MY_PERMISSIONS_REQUEST_SMS_RECEIVE);
        Log.i("GoodOldSmsReceiver", "GoodOldSmsReceiver is initialized")
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        Log.i("GoodOldSmsReceiver", "GoodOldSmsReceiver receives $ACTION")
        if (intent != null && intent.action != null && ACTION.equals(intent.action, ignoreCase=true)) {
            val pduArray = intent.extras!!["pdus"] as Array<Any>?
            val messages: Array<SmsMessage?> = arrayOfNulls<SmsMessage>(pduArray!!.size)
            for (i in pduArray!!.indices) {
                messages[i] = SmsMessage.createFromPdu(pduArray[i] as ByteArray)
            }
            // SMS Sender, example: 123456789
            //val sms_from: String = messages[0].getDisplayOriginatingAddress()

            //Lets check if SMS sender is 123456789
            //if (sms_from.equals(SMS_SENDER, ignoreCase = true)) {
            val bodyText = StringBuilder()

            // If SMS has several parts, lets combine it :)
            for (i in messages.indices) {
                bodyText.append(messages[i]!!.messageBody)
            }
            //SMS Body
            val body = bodyText.toString()
            // Lets get SMS Code
            val code = body.replace("\\D".toRegex(), "")

            activity.populateCode(code)

            //}
        }
    }
}