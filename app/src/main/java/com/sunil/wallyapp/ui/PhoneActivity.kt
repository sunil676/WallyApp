package com.sunil.wallyapp.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.sunil.wallyapp.MainActivity
import com.sunil.wallyapp.R
import kotlinx.android.synthetic.main.activity_phone.*

class PhoneActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_phone)

        // check the user is verified or not
        val prefs = applicationContext.getSharedPreferences(
            "USER_PREF", Context.MODE_PRIVATE
        )
        val phoneNumber = prefs.getString("phoneNumber", "")
        if (phoneNumber != null && phoneNumber.isNotEmpty()) {
            startMainActivity()
        }

        button_submit.setOnClickListener(View.OnClickListener {
            if (editText.text.toString().trim().isEmpty() || editText.text?.trim()?.length!! < 10) {
                editText.setError(resources.getString(R.string.error_phone));
                editText.requestFocus();
            } else {
                startVerifyActivity()
            }
        })
    }

    private fun startMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
    }

    private fun startVerifyActivity() {
        // simulate for india country
        var phone = "+91"+ editText.text.toString().trim()
        val intent = Intent(this, PhoneVerifyActivity::class.java)
        intent.putExtra("phoneNumber", phone)
        startActivity(intent)
    }
}