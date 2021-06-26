package com.example.register_firbase_kotlin

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class Login : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        auth= FirebaseAuth.getInstance()

        val google = findViewById<View>(R.id.google) as Button
        google.setOnClickListener{
            val intent =Intent(this, googlesignin:: class.java)
            startActivity(intent);
        }



        val btn2=findViewById<Button>(R.id.loginbtn);
        btn2.setOnClickListener {
            doLogin();
        }
        val forgotbtn = findViewById<Button>(R.id.btnresetpassword);
        
        forgotbtn.setOnClickListener {
                val builder =AlertDialog.Builder(this)
            builder.setTitle("Forgot Password")
            val view = layoutInflater.inflate(R.layout.layout_forgot_password,null)
            val username = view.findViewById<EditText>(R.id.et_username);
            builder.setView(view)
            builder.setPositiveButton("Reset", DialogInterface.OnClickListener { _, _ ->
                forgotpassword(username)  })
            builder.setNegativeButton("cancel", DialogInterface.OnClickListener { _, _ ->   })
            builder.show()



        }
    }

    fun forgotpassword( username : EditText)
    {
       /* if(username.text.toString().isEmpty())
        {
            return
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(username.text.toString()).matches())
        {

            return
        }*/

        Firebase.auth.sendPasswordResetEmail(username.text.toString())
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(baseContext, "email reset ",
                                Toast.LENGTH_SHORT).show()
                    }
                }
    }

    private  fun doLogin() {
        val email = findViewById<EditText>(R.id.editemail);
        val password = findViewById<EditText>(R.id.editpassword);
        if (email.text.toString().isEmpty()) {
            email.error = "enter email";
            email.requestFocus();
            return
        }
        /* if(!Patterns.EMAIL_ADDRESS.matcher(password.text.toString()).matches())
         {
             password.error="enter valid ";
             password.requestFocus();
             return
         }*/
        if (password.text.toString().isEmpty()) {
            password.error = "enter password";
            password.requestFocus();
            return
        }
        auth.signInWithEmailAndPassword(email.text.toString(), password.text.toString())
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {

                    val user = auth.currentUser
                    updateUI(user)
                } else {


                    updateUI(null)
                }
            }


    }

    private fun updateUI(cureentUser:FirebaseUser?) {
        if(cureentUser!=null)
        {
            if(cureentUser.isEmailVerified)
            {
                val intent =Intent(this, dashboard:: class.java)
                startActivity(intent);
            }
            else
            {
                Toast.makeText(baseContext, "verify email address",
                        Toast.LENGTH_SHORT).show()

            }

        }
        else
        {
            Toast.makeText(baseContext, "Login failed.",
                Toast.LENGTH_SHORT).show()
        }

    }
    }
