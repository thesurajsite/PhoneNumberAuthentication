package com.example.phonenumberauthentication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import com.google.firebase.auth.FirebaseAuth
import org.w3c.dom.Text

class MainActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        auth=FirebaseAuth.getInstance()

        val greeting=findViewById<TextView>(R.id.greeting)
        val logout_btn=findViewById<Button>(R.id.logout_btn)

        if(auth.currentUser==null){
            val intent=Intent(this, loginPage::class.java)
            startActivity(intent)
            finish()
        }

        greeting.setText("Hii👋 "+auth.currentUser?.phoneNumber.toString())

        logout_btn.setOnClickListener {
            auth.signOut()
            startActivity(Intent(this, loginPage::class.java))
        }







    }
}