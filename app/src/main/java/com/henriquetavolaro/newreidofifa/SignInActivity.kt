package com.henriquetavolaro.newreidofifa

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.google.firebase.auth.FirebaseAuth
import com.henriquetavolaro.newreidofifa.databinding.ActivitySignInBinding
import com.henriquetavolaro.newreidofifa.ui.FirestoreClass
import com.henriquetavolaro.newreidofifa.ui.User

class SignInActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var binding: ActivitySignInBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_sign_in)

        auth = FirebaseAuth.getInstance()

        binding.btnSignIn.setOnClickListener {
            signInUser()
        }
    }


    fun signInSuccess(user: User) {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }


    private fun signInUser() {
        val email: String = binding.etEmailSignIn.text.toString().trim { it <= ' ' }
        val password: String = binding.etPasswordSignIn.text.toString().trim { it <= ' ' }

        if (validateForm(email, password)) {
            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        FirestoreClass().signInUser(this)

                    } else {
                        Toast.makeText(this, "failed", Toast.LENGTH_SHORT).show()
                    }
                }
        }
    }

    private fun validateForm(email: String, password: String): Boolean {
        when {

            TextUtils.isEmpty(email) -> {
                Toast.makeText(this, "Please insert E-mail", Toast.LENGTH_SHORT).show()
                return false
            }
            TextUtils.isEmpty(password) -> {
                Toast.makeText(this, "Please insert Password", Toast.LENGTH_SHORT).show()
                return false
            }
            else -> return true
        }
    }

}