package com.example.appnews

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.MenuItem
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.bumptech.glide.Glide
import com.example.appnews.dataBase.NewsDatabase
import com.example.appnews.fragments.AboutUsFragment
import com.example.appnews.fragments.MainFragment
import com.example.appnews.fragments.ProfileFragment
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.material.navigation.NavigationView
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File

class HomeActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    lateinit var drawerLayout: DrawerLayout
    lateinit var mGoogleSignInClient: GoogleSignInClient
    lateinit var navigationView: NavigationView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_home)
        val toolbar = findViewById<Toolbar>(R.id.toolBar)
        navigationView = findViewById(R.id.navigationView)
        // Initialize Google Sign-In client
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .build()
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso)
        drawerLayout = findViewById(R.id.drawerLayout)
        setSupportActionBar(toolbar)


        val actionToggle = ActionBarDrawerToggle(
            this,
            drawerLayout,
            toolbar,
            R.string.open,
            R.string.close
        )
        drawerLayout.addDrawerListener(actionToggle)
        actionToggle.syncState()
        navigationView.setNavigationItemSelectedListener(this)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, MainFragment())
                .commit()
            navigationView.setCheckedItem(R.id.item1)
        }


    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.item1 -> {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, MainFragment())
                    .commit()
            }

            R.id.item4 -> {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, ProfileFragment())
                    .commit()
            }

            R.id.item2 -> {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, AboutUsFragment())
                    .commit()
            }

            R.id.item3 -> {
                mGoogleSignInClient.signOut().addOnCompleteListener {
                    startActivity(Intent(this, Login::class.java))
                    finish()
                }
            }
        }
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    override fun onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    fun updateNavigationHeader() {
        val headerView = navigationView.getHeaderView(0)
        val emailTextView = headerView.findViewById<TextView>(R.id.emailUser)
        val imageView = headerView.findViewById<CircleImageView>(R.id.imageProfile)

        CoroutineScope(Dispatchers.IO).launch {
            val db = NewsDatabase.getDatabase(applicationContext)
            val account = GoogleSignIn.getLastSignedInAccount(applicationContext)
            val email = account?.email ?: return@launch
            val user = db.userDao().getUser(email)

            user?.let {
                val photoUrl = it.photoUrl
                withContext(Dispatchers.Main) {
                    if (!photoUrl.isNullOrEmpty()) {
                        val isUrl = photoUrl.startsWith("http://") || photoUrl.startsWith("https://")
                        val imageSource = if (isUrl) photoUrl else File(photoUrl)

                        Glide.with(this@HomeActivity)
                            .load(imageSource)
                            .placeholder(R.drawable.profilepicture)
                            .error(R.drawable.profilepicture)
                            .into(imageView)
                    } else {
                        imageView.setImageResource(R.drawable.profilepicture)
                    }

                    emailTextView.text = it.displayName
                }
            }
        }
    }


    override fun onResume() {
        super.onResume()
        updateNavigationHeader()
    }

}