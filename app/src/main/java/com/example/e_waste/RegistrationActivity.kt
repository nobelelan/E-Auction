package com.example.e_waste

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.e_waste.databinding.ActivityRegistrationBinding
import com.example.e_waste.model.User
import com.example.e_waste.utils.verifyDataFromUser
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class RegistrationActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegistrationBinding

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegistrationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = Firebase.auth

        binding.btnSignUp.setOnClickListener {
            registerUser()
        }


    }

    private fun registerUser() {
        val username = binding.edtUsername.text.toString()
        val email = binding.edtEmail.text.toString()
        val contact = binding.edtContact.text.toString()
        val password = binding.edtPassword.text.toString()
        val password2 = binding.edtPassword2.text.toString()

        if (verifyDataFromUser(username, email, contact, password, password2)){
            val user = User(username, email, contact, password)
            auth.createUserWithEmailAndPassword(user.email, user.password)
                .addOnSuccessListener {
                    Toast.makeText(this, "${user.username} successfully registered!", Toast.LENGTH_SHORT).show()
                    auth.currentUser?.let {
                        val registeredUser = User(user.username, user.email, user.contact)
                        val registeredDocumentReference = Firebase.firestore.collection("registeredUsers").document(it.uid)
                        registeredDocumentReference.set(registeredUser).addOnSuccessListener {
                            startActivity(Intent(this, MainActivity::class.java))
                        }
                    }
                }
                .addOnFailureListener{
                    Toast.makeText(this, "Registration failed!", Toast.LENGTH_SHORT).show()
                }

        }else{
            Toast.makeText(this, "Fill out all the fields properly!", Toast.LENGTH_SHORT).show()
        }
    }
}