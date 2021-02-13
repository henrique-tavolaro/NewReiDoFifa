package com.henriquetavolaro.newreidofifa.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import com.henriquetavolaro.newreidofifa.R
import com.henriquetavolaro.newreidofifa.SignInActivity
import com.henriquetavolaro.newreidofifa.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login)

        binding.buttonSignUp.setOnClickListener {
            startActivity(Intent(this, SignUpActivity::class.java))
        }

        binding.buttonLogin.setOnClickListener {
            startActivity(Intent(this, SignInActivity::class.java))
        }
    }
}

