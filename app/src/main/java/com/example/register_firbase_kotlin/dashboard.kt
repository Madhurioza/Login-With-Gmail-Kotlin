package com.example.register_firbase_kotlin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.ktx.Firebase
import java.util.logging.Logger.global

class dashboard : AppCompatActivity() {
    private  lateinit var auth : FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        val btnpassword = findViewById<Button>(R.id.btn_change_password);
        auth = FirebaseAuth.getInstance()

        btnpassword.setOnClickListener {
            changepasword()
        }
    }

    private fun changepasword()
    {
        val edcurrent = findViewById<EditText>(R.id.ed_current_password);
        val ednew = findViewById<EditText>(R.id.ed_new_password);
        val edconfirm = findViewById<EditText>(R.id.ed_confirm_password);
        if(edcurrent.text.isNotEmpty() && ednew.text.isNotEmpty() && edconfirm.text.isNotEmpty())
        {
            if(ednew.text.toString().equals(edconfirm.text.toString())){
                val user =auth.currentUser
                if(user!=null && user.email!=null)
                {
                    val credential = EmailAuthProvider
                            .getCredential(user.email!!, edcurrent.text.toString())

// Prompt the user to re-provide their sign-in credentials
                    user.reauthenticate(credential)
                            .addOnCompleteListener {
                                if(it.isSuccessful){
                                    Toast.makeText(this,"re-authentication success",Toast.LENGTH_SHORT).show()

                                    user!!.updatePassword(ednew.text.toString())
                                            .addOnCompleteListener { task ->
                                                if (task.isSuccessful) {
                                                    Toast.makeText(this,"password change successfully",Toast.LENGTH_SHORT).show()
                                                    auth.signOut()
                                                    startActivity(Intent(this , Login::class.java))
                                                    finish()

                                                }
                                            }
                                }else
                                {
                                    Toast.makeText(this,"re-authentication failed",Toast.LENGTH_SHORT).show()
                                }
                            }

                }else
                {
                    startActivity(Intent(this , Login::class.java))
                    finish()
                }

            }else
            {
                Toast.makeText(this,"password mismatch",Toast.LENGTH_SHORT).show()
            }
        }else
        {
            Toast.makeText(this,"please fill all fileds",Toast.LENGTH_SHORT).show()
        }
    }
}