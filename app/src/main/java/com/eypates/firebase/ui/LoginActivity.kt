package com.eypates.firebase.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.eypates.firebase.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class LoginActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    private lateinit var layoutBnd: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        layoutBnd = ActivityMainBinding.inflate(layoutInflater).apply {
            lifecycleOwner = this@LoginActivity
        }
        setContentView(layoutBnd.root)

        auth = Firebase.auth

        val currentUser = auth.currentUser
        if (currentUser != null) {
            startActivity(Intent(this, DashboardActivity::class.java))
            finish()
            return
        }

        layoutBnd.mainBtnLogin.setOnClickListener {

            val email = layoutBnd.mainTxtEmail.text.toString()
            val password = layoutBnd.mainTxtPassword.text.toString()
            if (email.isEmpty() || email.isBlank() || password.isEmpty() || password.isBlank()) {
                // özel mesaj sınıf
                Toast.makeText(this, "Boş geçilmez", Toast.LENGTH_LONG).show()
                return@setOnClickListener

            }

            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(this, "Hoşgeldiniz! ${auth.currentUser!!.email}", Toast.LENGTH_LONG).show()
                        startActivity(Intent(this, DashboardActivity::class.java))
                        finish()
                    }
                }
                .addOnFailureListener { exception -> Toast.makeText(this, exception.localizedMessage, Toast.LENGTH_LONG).show() }
        }

        layoutBnd.mainBtnRegister.setOnClickListener {

            val email = layoutBnd.mainTxtEmail.text.toString()
            val password = layoutBnd.mainTxtPassword.text.toString()
            if (email.isEmpty() || email.isBlank() || password.isEmpty() || password.isBlank()) {
                // özel mesaj sınıf
                Toast.makeText(this, "Boş geçilmez", Toast.LENGTH_LONG).show()
                return@setOnClickListener

            }
            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task -> if (task.isSuccessful) Toast.makeText(this, "Kayıt Başarılı", Toast.LENGTH_LONG).show() }
                .addOnFailureListener { exception -> Toast.makeText(this, exception.localizedMessage, Toast.LENGTH_LONG).show() }

        }
    }
}