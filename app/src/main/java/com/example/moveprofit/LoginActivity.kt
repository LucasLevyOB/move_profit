package com.example.moveprofit

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.moveprofit.databinding.ActivityLoginBinding
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private val auth = FirebaseAuth.getInstance()

    private fun showSnackbar(view: View, text: String) {
        val snackbar = Snackbar.make(view, text, Snackbar.LENGTH_SHORT)
        snackbar.show()
    }


    private fun login(view: View, email: String, password: String) {
        binding.btnLogin.isEnabled = false
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener { response ->
            if (response.isSuccessful) {
                val intent = Intent(this, MainActivity::class.java);
                startActivity(intent);
            }
            binding.btnLogin.isEnabled = true
        }.addOnFailureListener { response ->
            showSnackbar(view, "Desculpe, ocorreu um erro ao tentar fazer o login.")
            binding.btnLogin.isEnabled = true
        }
    }

    private fun onLogin(view: View) {
        val email = binding.edtEmail.text.toString()
        val password = binding.edtPassword.text.toString()

        if (email.isEmpty() || password.isEmpty()) {
            showSnackbar(view, "Por favor, preencha todos os campos!")
        } else {
            login(view, email, password)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnLogin.setOnClickListener {
            onLogin(it)
        }

        binding.tvGotoRegister.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java);
            startActivity(intent);
        }
    }
}