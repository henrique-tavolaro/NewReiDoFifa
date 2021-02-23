package com.henriquetavolaro.newreidofifa.ui.activities

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.WindowManager
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import androidx.navigation.ui.*
import com.bumptech.glide.Glide
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.henriquetavolaro.newreidofifa.R
import com.henriquetavolaro.newreidofifa.ui.firebase.FirestoreClass
import com.henriquetavolaro.newreidofifa.ui.models.User

class MainActivity : AppCompatActivity(){

    private lateinit var appBarConfiguration: AppBarConfiguration


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        FirestoreClass().loadUserData(this)

        val userId = getCurrentUserID()


        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )


        val fab: FloatingActionButton = findViewById(R.id.fab)
        fab.setOnClickListener {
            FirestoreClass().signOut(this)
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        val navView: NavigationView = findViewById(R.id.nav_view)
        val navController = findNavController(R.id.nav_host_fragment)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_home, R.id.nav_my_profile, R.id.nav_sign_out
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
//        navView.setNavigationItemSelectedListener(

//        navView.menu.findItem(R.id.nav_sign_out).setOnMenuItemClickListener { menuItem ->
//            val intent = Intent(this@MainActivity, LoginActivity::class.java)
//
//            menuItem.
////            val intent = Intent(this@MainActivity, LoginActivity::class.java)
////            Toast.makeText(this, "logged out", Toast.LENGTH_LONG).show()
//        }



        val signOutBtn : FloatingActionButton = findViewById(R.id.fab)
        signOutBtn.setOnClickListener {
            FirestoreClass().signOut(this)
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }



    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    fun updateNavigationUserDetails(user: User) {

        val ivDrawerImage: ImageView = findViewById(R.id.iv_drawer!!)

        Glide
            .with(this)
            .load(user.image)
            .centerCrop()
            .placeholder(R.drawable.ic_user_place_holder)
            .into(ivDrawerImage)

        val userName: TextView = findViewById(R.id.tv_drawer)
        userName.text = user.name
        val userEmail: TextView = findViewById(R.id.tv_email_drawer)
        userEmail.text = user.email

    }


    fun getCurrentUserID(): String {
        return FirebaseAuth.getInstance().currentUser.toString()
    }



}