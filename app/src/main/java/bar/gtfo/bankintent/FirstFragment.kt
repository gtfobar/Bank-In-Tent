package bar.gtfo.bankintent

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import bar.gtfo.bankintent.databinding.FragmentFirstBinding
import java.io.File
import java.security.SecureRandom
import java.util.stream.Collectors

private const val WEBVIEW_ACTIVITY = "bar.gtfo.bankintent.InsecureWebViewActivity"
private const val PACKAGE_NAME = "bar.gtfo.bankintent"
private const val WEBVIEW_URL = "https://bank.gtfo.bar"

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class FirstFragment : Fragment() {

    private var _binding: FragmentFirstBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentFirstBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val smsConsentReceiver = activity?.let { SmsUserConsentReceiver(it) }
        val goodOldSmsReceiver = activity?.let { GoodOldSmsReceiver(it as PhoneActivity) }

        binding.buttonSubmitPhone.setOnClickListener {
            sendSmsVerificationCode()
            smsConsentReceiver?.init()
            findNavController().navigate(R.id.action_FirstFragment_to_SecondFragment)
        }

        binding.buttonIAmDinosaur.setOnClickListener {
            sendSmsVerificationCode()
            goodOldSmsReceiver?.init()
            findNavController().navigate(R.id.action_FirstFragment_to_SecondFragment)
        }

        binding.buttonSavePhone.setOnClickListener {
            savePhone(binding.editTextPhone.text.toString())
        }

        binding.buttonLaunchWebview.setOnClickListener {
            launchWebView(WEBVIEW_URL)
        }
    }

    private fun savePhone(phoneNum: String) {

        activity?.openFileOutput("phone.txt", Context.MODE_PRIVATE).use {
            it?.write(phoneNum.toByteArray())
        }

        Toast.makeText(
            activity,
            "The phone number is saved to files/phone.txt",
            Toast.LENGTH_LONG
        ).show()
    }

    private fun sendSmsVerificationCode() {
        Log.i(context?.packageName,"sendSmsVerificationCode")
    }

    private fun launchWebView(url: String) {
        Intent().apply {
            setClassName(PACKAGE_NAME, WEBVIEW_ACTIVITY)
            data = Uri.parse(url)
        }.also {
            startActivity(it)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}