package com.example.phonenumberauthentication

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.method.PasswordTransformationMethod
import android.view.View
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.common.io.Resources
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import java.util.concurrent.TimeUnit
import java.util.logging.Handler

class loginPage : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var verificationId: String
    private lateinit var mobileNumber: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_page)

        auth=FirebaseAuth.getInstance()
        var sendOtpFlag=0;

        val mobile_et=findViewById<EditText>(R.id.mobile_et)
        val otp_et=findViewById<EditText>(R.id.otp_et)
        val checkbox=findViewById<CheckBox>(R.id.checkbox)
        val sendOtp_btn=findViewById<Button>(R.id.sendOtp_btn)
        val verify_btn=findViewById<Button>(R.id.verify_btn)
        val loginProgressBar=findViewById<ProgressBar>(R.id.loginProgressbar)
        val otpStatus=findViewById<TextView>(R.id.otpStatus)

        sendOtp_btn.setOnClickListener {
            mobileNumber=mobile_et.text.toString()

            if(mobileNumber.isEmpty()){
                otpStatus.setTextColor(Color.parseColor("#FF0000"))
                otpStatus.setText("Please enter Mobile Number")
                otpStatus.visibility=View.VISIBLE

                android.os.Handler().postDelayed({
                    otpStatus.visibility=View.INVISIBLE
                }, 3000)

//                Toast.makeText(this, "Please Enter Mobile Number", Toast.LENGTH_SHORT).show()
            }
            else{
                mobileNumber="+91"+mobileNumber

                otpStatus.setTextColor(Color.parseColor("#0ac34f"))
                otpStatus.setText("Sending OTP...")
                otpStatus.visibility=View.VISIBLE

                val options=PhoneAuthOptions.newBuilder(auth)
                    .setPhoneNumber(mobileNumber)
                    .setTimeout(120, TimeUnit.SECONDS)
                    .setActivity(this)
                    .setCallbacks(object : PhoneAuthProvider.OnVerificationStateChangedCallbacks(){
                        override fun onVerificationCompleted(p0: PhoneAuthCredential) {


                        }

                        override fun onVerificationFailed(p0: FirebaseException) {
                            Toast.makeText(this@loginPage, "Please Try Again", Toast.LENGTH_SHORT).show()
                            otpStatus.visibility=View.INVISIBLE
//                            Log.d("error123", p0.toString())

                        }

                        override fun onCodeSent(p0: String, p1: PhoneAuthProvider.ForceResendingToken) {
                            verificationId=p0
//                            Toast.makeText(this@loginPage, "OTP Sent", Toast.LENGTH_SHORT).show()

                            otpStatus.setTextColor(Color.parseColor("#0ac34f"))
                            otpStatus.setText("OTP Sent...")
                            otpStatus.visibility=View.VISIBLE

                            sendOtp_btn.visibility=View.GONE
                            verify_btn.visibility=View.VISIBLE

                            android.os.Handler().postDelayed({
                                otpStatus.visibility=View.INVISIBLE
                            }, 3000)

                        }

                    }).build()

                PhoneAuthProvider.verifyPhoneNumber(options)

            }



        }

        verify_btn.setOnClickListener {
            val otp=otp_et.text.toString()
            if(!otp.isEmpty()){
                loginProgressBar.visibility=View.VISIBLE
                if(mobileNumber.isEmpty()){
                    Toast.makeText(this, "Please Enter Mobile Number", Toast.LENGTH_SHORT).show()
                    loginProgressBar.visibility=View.INVISIBLE
                }
                else{
                    val credential=PhoneAuthProvider.getCredential(verificationId, otp_et.text.toString())
                    auth.signInWithCredential(credential)
                        .addOnCompleteListener{
                            if(it.isSuccessful){
                                loginProgressBar.visibility=View.INVISIBLE
                                startActivity(Intent(this, MainActivity::class.java))
                                finish()
                            }
                            else{
                                loginProgressBar.visibility=View.INVISIBLE
//                                Toast.makeText(this, "Verification Failed", Toast.LENGTH_SHORT).show()
                                otpStatus.setTextColor(Color.parseColor("#FF0000"))
                                otpStatus.setText("Verification Failed")
                                otpStatus.visibility=View.VISIBLE

                                android.os.Handler().postDelayed({
                                    otpStatus.visibility=View.INVISIBLE
                                }, 3000)
                            }
                        }

                }
                
            }
            else{
                otpStatus.setTextColor(Color.parseColor("#FF0000"))
                otpStatus.setText("Please enter OTP")
                otpStatus.visibility=View.VISIBLE

                android.os.Handler().postDelayed({
                    otpStatus.visibility=View.INVISIBLE
                }, 3000)

//                Toast.makeText(this, "Please Enter OTP", Toast.LENGTH_SHORT).show()
            }
            
        }

        checkbox.setOnCheckedChangeListener { buttonView, isChecked ->
            if(isChecked)
                otp_et.transformationMethod=null
            else
                otp_et.transformationMethod=PasswordTransformationMethod.getInstance()
        }




    }
}