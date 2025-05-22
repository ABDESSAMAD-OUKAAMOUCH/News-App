package com.example.appnews

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.appnews.dataBase.NewsDatabase
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class Login : AppCompatActivity() {
    lateinit var mGoogleSignInClient: GoogleSignInClient
    lateinit var signInBtn: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login)
        signInBtn=findViewById(R.id.signinwithGoogle)
        val gso= GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .build()
        mGoogleSignInClient= GoogleSignIn.getClient(this,gso)

        signInBtn.setOnClickListener {
            signIn()
        }
    }

    fun signIn(){
        val signInIntent: Intent =mGoogleSignInClient.signInIntent
        launcher.launch(signInIntent)
    }
    val launcher=registerForActivityResult(ActivityResultContracts.StartActivityForResult()){ result->
        if(result.resultCode==RESULT_OK){
            val task= GoogleSignIn.getSignedInAccountFromIntent(result.data)
            handleSignInResult(task)
        }
    }

    fun handleSignInResult(task: Task<GoogleSignInAccount>){
        try{
            val account=task.getResult(ApiException::class.java)
            updateUI(account)
        }catch (e: ApiException){
            Toast.makeText(this, "check your internet", Toast.LENGTH_SHORT).show();
        }
    }

    fun updateUI(account: GoogleSignInAccount?) {
        if (account != null) {
            val user = com.example.appnews.Models.User(
                email = account.email ?: "",
                displayName = account.displayName,
                photoUrl = account.photoUrl?.toString()
            )

            val db = NewsDatabase.getDatabase(applicationContext)

            CoroutineScope(Dispatchers.IO).launch {
                db.userDao().insertUser(user)
            }

            startActivity(Intent(this, HomeActivity::class.java))
            finish()
        }
    }


    override fun onStart() {
        super.onStart()
        val account = GoogleSignIn.getLastSignedInAccount(this)
        if(account!=null){
            updateUI(account)
        }
    }
}