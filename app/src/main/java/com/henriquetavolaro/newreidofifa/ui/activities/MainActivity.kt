package com.henriquetavolaro.newreidofifa.ui.activities

import android.app.Dialog
import android.os.Bundle
import android.view.Menu
import android.view.View
import android.view.WindowManager
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.navigation.NavigationView
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.drawerlayout.widget.DrawerLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.henriquetavolaro.newreidofifa.R
import com.henriquetavolaro.newreidofifa.ui.Constants
import com.henriquetavolaro.newreidofifa.ui.NavigationUpdaterListener
import com.henriquetavolaro.newreidofifa.ui.firebase.FirestoreClass
import com.henriquetavolaro.newreidofifa.ui.models.User

class MainActivity : AppCompatActivity(){

    private lateinit var appBarConfiguration: AppBarConfiguration

//    private lateinit var dialogBox: Dialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        FirestoreClass().loadUserData(this)

        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

//        val query = FirestoreClass().getAllUsers()
//
//        val options: FirestoreRecyclerOptions<User> = FirestoreRecyclerOptions.Builder<User>()
//            .setQuery(query, User::class.java)
//            .setLifecycleOwner(this)
//            .build()
//
//        val fab: FloatingActionButton = findViewById(R.id.fab)
//        fab.setOnClickListener(View.OnClickListener {
//            val adapter = OponentAdapter(options)
//            val oponentAdapter = it.findViewById<RecyclerView>(R.id.rv_players_list)
//            oponentAdapter.adapter = adapter
//            this.dialogBox = Dialog(this)
//            this.dialogBox.setContentView(R.layout.dialog_oponent)
//            oponentAdapter.layoutManager = LinearLayoutManager(this)
//            dialogBox.show()
//        })
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        val navView: NavigationView = findViewById(R.id.nav_view)
        val navController = findNavController(R.id.nav_host_fragment)
//        navController.addOnDestinationChangedListener{_, destination, _ ->
//            if(destination.id == R.id.nav_home) {
//                Toast.makeText(this, "zeca", Toast.LENGTH_SHORT).show()
////                FirestoreClass().loadUserData(this@MainActivity)
//
////                FirestoreClass().loadUserData(this)
//            } else {
//                Toast.makeText(this, "zi", Toast.LENGTH_SHORT).show()
//            }
//
//        }
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_home, R.id.nav_my_profile, R.id.nav_sign_out
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

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
        return FirebaseAuth.getInstance().currentUser.toString()!!
    }

}