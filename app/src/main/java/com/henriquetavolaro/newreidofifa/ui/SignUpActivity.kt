package com.henriquetavolaro.newreidofifa.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.henriquetavolaro.newreidofifa.R
import com.henriquetavolaro.newreidofifa.databinding.ActivitySignUpBinding

class SignUpActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignUpBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_sign_up)

        binding.appCompatButton.setOnClickListener {
            registerUser()
        }

    }
    fun userRegisteredSuccess(){
        Toast.makeText(this, "Success", Toast.LENGTH_SHORT).show()
        FirebaseAuth.getInstance().signOut()
        finish()
    }

    fun registerUser() {
        val email: String = binding.etEmailSignUp.text.toString().trim { it <= ' ' }
        val name: String = binding.etNameSignUp.text.toString().trim { it <= ' ' }
        val password: String = binding.etPasswordSignUp.text.toString().trim { it <= ' ' }

        if (validateForm(name, email, password)) {
            FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val firebaseUser: FirebaseUser = task.result!!.user!!
                        val registeredEmail = firebaseUser.email!!
                        val user = User(firebaseUser.uid, name, registeredEmail)
                        FirestoreClass().registerUser(this, user)
                    } else {
                        Toast.makeText(this, task.exception!!.message, Toast.LENGTH_SHORT).show()

                    }
                }
        }
    }

    private fun validateForm(email: String, name: String, password: String): Boolean {
        when {

            TextUtils.isEmpty(email) -> {
                Toast.makeText(this, "Please insert E-mail", Toast.LENGTH_SHORT).show()
                return false
            }
            TextUtils.isEmpty(name) -> {
                Toast.makeText(this, "Please insert Name", Toast.LENGTH_SHORT).show()
                return false
            }
            TextUtils.isEmpty(password) -> {
                Toast.makeText(this, "Please insert Password", Toast.LENGTH_SHORT).show()
                return false
            }
            else -> return true
        }
    }

    fun getCurrentUserID(): String {
        return FirebaseAuth.getInstance().currentUser!!.uid
    }
}