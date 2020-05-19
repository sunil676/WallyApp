package com.sunil.wallyapp.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.TaskExecutors
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.auth.PhoneAuthProvider.ForceResendingToken
import com.google.firebase.auth.PhoneAuthProvider.OnVerificationStateChangedCallbacks
import com.sunil.wallyapp.MainActivity
import com.sunil.wallyapp.R
import kotlinx.android.synthetic.main.activity_verify_phone.*
import java.util.concurrent.TimeUnit


class PhoneVerifyActivity : AppCompatActivity() {

    private var mAuth: FirebaseAuth? = null
    private var verificationId: String? = null
    private var phoneNumber: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_verify_phone)

        mAuth = FirebaseAuth.getInstance();
        phoneNumber = intent.getStringExtra("phoneNumber")
        sendVerificationCode(phoneNumber!!);

        buttonSignIn.setOnClickListener(View.OnClickListener {
            val code: String = editTextCode.getText().toString().trim()

            if (code.isEmpty() || code.length < 6) {
                editTextCode.setError("Enter code...")
                editTextCode.requestFocus()
            } else {
                verifyCode(code)
            }
        })

    }

    private fun sendVerificationCode(number: String) {
        progressBar.visibility = View.VISIBLE
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
            number, 60, TimeUnit.SECONDS,
            TaskExecutors.MAIN_THREAD,
            mCallBack
        )
        progressBar.setVisibility(View.GONE)
    }

    private val mCallBack: OnVerificationStateChangedCallbacks =
        object : OnVerificationStateChangedCallbacks() {
            override fun onCodeSent(s: String, forceResendingToken: ForceResendingToken) {
                super.onCodeSent(s, forceResendingToken)
                verificationId = s
            }

            override fun onVerificationCompleted(phoneAuthCredential: PhoneAuthCredential) {
                val code = phoneAuthCredential.smsCode
                if (code != null) {
                    editTextCode.setText(code)
                    verifyCode(code)
                }
            }

            override fun onVerificationFailed(e: FirebaseException) {
                Toast.makeText(this@PhoneVerifyActivity, e.message, Toast.LENGTH_LONG).show()
                progressBar.visibility = View.GONE
            }
        }

    private fun verifyCode(code: String) {
        val credential = PhoneAuthProvider.getCredential(verificationId!!, code)
        signInWithCredential(credential)
    }

    private fun signInWithCredential(credential: PhoneAuthCredential) {
        mAuth!!.signInWithCredential(credential).addOnCompleteListener(OnCompleteListener {
            if (it.isSuccessful()) {
                saveUserInfo(phoneNumber!!)
                val intent =
                    Intent(this@PhoneVerifyActivity, MainActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
            } else {
                Toast.makeText(
                    this@PhoneVerifyActivity, it.exception?.message, Toast.LENGTH_LONG
                ).show()
                progressBar.visibility = View.GONE
            }
        })
    }

    private fun saveUserInfo(phoneNumber : String){
        // save phone number
        val prefs = applicationContext.getSharedPreferences(
            "USER_PREF", Context.MODE_PRIVATE)
        val editor = prefs.edit()
        editor.putString("phoneNumber", phoneNumber)
        editor.apply()
    }

}