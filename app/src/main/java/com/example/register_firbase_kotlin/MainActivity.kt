package com.example.register_firbase_kotlin

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase


class MainActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        auth = FirebaseAuth.getInstance()

        val btn = findViewById<Button>(R.id.btnreg);
        val loginbtn = findViewById<Button>(R.id.btnlogin);

        loginbtn.setOnClickListener{
            val intent =Intent(this, Login:: class.java)
            startActivity(intent);
        }

       btn.setOnClickListener{

            signup();
       }
    }

   private fun signup()
    {
        val email = findViewById<EditText>(R.id.editemail);
        val password = findViewById<EditText>(R.id.editpassword);
        if(email.text.toString().isEmpty())
        {
            email.error= "enter email";
            email.requestFocus();
            return
        }
       /* if(!Patterns.EMAIL_ADDRESS.matcher(password.text.toString()).matches())
        {
            password.error="enter valid ";
            password.requestFocus();
            return
        }*/
        if(password.text.toString().isEmpty())
        {
            password.error="enter password";
            password.requestFocus();
            return
        }

        auth.createUserWithEmailAndPassword(email.text.toString(), password.text.toString())
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val user = Firebase.auth.currentUser

                    user!!.sendEmailVerification()
                            .addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    startActivity(Intent(this,Login::class.java))
                                    finish()
                                }
                            }





                } else {

                    Toast.makeText(baseContext, "Authentication failed.",
                        Toast.LENGTH_SHORT).show()

                }
            }
    }

    public override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser
       // if(currentUser != null){
            reload(currentUser);


    }
    fun reload(currentUser : FirebaseUser?){

    }

    }


