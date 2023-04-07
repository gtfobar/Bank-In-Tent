package bar.gtfo.bankintent

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.webkit.WebView
import android.widget.Toast
import java.io.File
import java.nio.charset.StandardCharsets

class InsecureWebViewActivity : AppCompatActivity() {

    private lateinit var webView: WebView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_insecure_webview)
        webView = findViewById(R.id.webview_insecure)

        val phoneNum = File(filesDir, "phone.txt").readBytes().let{
            String(it, StandardCharsets.UTF_8)
        }

        intent.data?.toString()?.let {
            webView.loadUrl("${it}?phone=${phoneNum}")
        }
    }
}