package com.example.project

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.project.databinding.FragmentSignupBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import android.util.Log

class SignupFragment : AppCompatActivity() {

    private lateinit var binding: FragmentSignupBinding
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = FragmentSignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()

        binding.textView.setOnClickListener {
            // Use Intent to start the LoginFragment activity
            val intent = Intent(this, LoginFragment::class.java)
            startActivity(intent)
        }

        binding.button.setOnClickListener {
            val email = binding.emailEt.text.toString()
            val pass = binding.passET.text.toString()
            val confirmPass = binding.confirmPassEt.text.toString()
            val username = binding.usernameEt.text.toString()

            if (email.isNotEmpty() && pass.isNotEmpty() && confirmPass.isNotEmpty() && username.isNotEmpty()) {
                if (pass == confirmPass) {
                    firebaseAuth.createUserWithEmailAndPassword(email, pass)
                        .addOnCompleteListener {
                            if (it.isSuccessful) {
                                // Save user information including username to Firebase
                                saveUserInfo(email, username)

                                // Use Intent to start the LoginFragment activity upon successful signup
                                val intent = Intent(this, LoginFragment::class.java)
                                startActivity(intent)
                                finish()
                            } else {
                                Toast.makeText(this, it.exception.toString(), Toast.LENGTH_SHORT).show()
                            }
                        }
                } else {
                    Toast.makeText(this, "Password is not matching", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Empty Fields Are not Allowed !!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun saveUserInfo(email: String, username: String) {
        // Save user information to Firebase (you can modify this based on your data structure)
        val db = FirebaseFirestore.getInstance()
        val user = hashMapOf(
            "email" to email,
            "username" to username
        )

        db.collection("users")
            .add(user)
            .addOnSuccessListener {
                Log.d(TAG, "User information saved successfully")
            }
            .addOnFailureListener { e ->
                Log.w(TAG, "Error saving user information", e)
            }
    }

    companion object {
        private const val TAG = "SignupFragment"
    }
}
