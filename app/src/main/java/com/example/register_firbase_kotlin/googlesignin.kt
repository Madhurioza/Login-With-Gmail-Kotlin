package com.example.register_firbase_kotlin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.SignInButton
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task

class googlesignin : AppCompatActivity() {

    lateinit var mGoogleSignInClient: GoogleSignInClient
    lateinit var gso: GoogleSignInOptions
    val RC_SIGN_IN: Int=1
    lateinit var signOut: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_googlesignin)
        val signin = findViewById<View>(R.id.signinbtn) as  SignInButton
        gso= GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        mGoogleSignInClient=GoogleSignIn.getClient(this,gso)
        signOut = findViewById<View>(R.id.signout) as Button
        signOut.visibility = View.INVISIBLE

        signin.setOnClickListener{
            View: View? -> signInGoogle()

        }
    }

    private fun signInGoogle()
    {
        val SignInIntent : Intent = mGoogleSignInClient.signInIntent
        startActivityForResult(SignInIntent , RC_SIGN_IN)

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode==RC_SIGN_IN)
        {
            val task: Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(data)
            handleResult(task)

        }
    }

    private fun handleResult(completedTask : Task<GoogleSignInAccount>)
    {
        try{
            val account : GoogleSignInAccount = completedTask.getResult(ApiException::class.java)!!
            updateUI(account)

        }catch (e:ApiException)
        {
            Toast.makeText(this,e.toString(),Toast.LENGTH_SHORT).show()
        }

    }

    private fun updateUI(account : GoogleSignInAccount)
    {
        val distxt = findViewById<View>(R.id.distxt) as TextView
        distxt.text= account.displayName
        signOut.visibility = View.VISIBLE
        signOut.setOnClickListener{
            View: View? ->mGoogleSignInClient.signOut().addOnCompleteListener{
                task: Task<Void> -> distxt.text=""
           signOut.visibility =android.view.View.INVISIBLE
        }
        }
    }
}