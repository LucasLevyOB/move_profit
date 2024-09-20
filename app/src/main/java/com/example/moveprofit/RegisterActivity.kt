package com.example.moveprofit

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.example.moveprofit.databinding.ActivityRegisterBinding
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    private val auth = FirebaseAuth.getInstance()

    private fun showSnackbar(view: View, text: String) {
        val snackbar = Snackbar.make(view, text, Snackbar.LENGTH_SHORT)
        snackbar.show()
    }

    private fun switchView() {
        val intent = Intent(this, LoginActivity::class.java);
        startActivity(intent);
    }

    private fun registerUser(view: View, email: String, password: String) {
        binding.btnRegister.isEnabled = false
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { response ->
            if (response.isSuccessful) {
                val snackbar = Snackbar.make(view, "Usuário cadastrado com sucesso!", Snackbar.LENGTH_SHORT)
                snackbar.show()
                switchView()
            }
            binding.btnRegister.isEnabled = true
        }.addOnFailureListener { response ->
            Log.e("tag-register-response-error", response.message!!)
            val snackbar = Snackbar.make(view, "Desculpe, ocorreu um erro ao registar o usuário.", Snackbar.LENGTH_SHORT)
            snackbar.show()
            binding.btnRegister.isEnabled = true
        }
    }

    private fun onRegisterUser(view: View) {
        val email = binding.edtEmail.text.toString()
        val password = binding.edtPassword.text.toString()
        val confirmPassword = binding.edtConfirmPassword.text.toString()


        Log.e("tag-register", "clico")
        if (email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            showSnackbar(view, "Por favor, preencha todos os campos!")
        } else if (password != confirmPassword) {
            showSnackbar(view, "O campo de confirmação de senha não bate com o campo de senha!")
        } else if (password.length < 6) {
            showSnackbar(view, "A senha deve conter pelo menos 6 caracteres")
        } else {
            registerUser(view, email, password)
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnRegister.setOnClickListener {
            onRegisterUser(it)
        }

        binding.tvGotoLogin.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java);
            startActivity(intent);
        }
    }
}