package com.example.project

import android.content.Intent
import android.os.Bundle //pass data between components
import androidx.appcompat.app.AppCompatActivity //base class for activities in Android
import com.example.project.databinding.ActivityEntryBinding

class EntryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEntryBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEntryBinding.inflate(layoutInflater)
        //interacting with views in your layout files
        setContentView(binding.root)
        //View Binding makes it easier for your code to talk to, or "bind,"
        // with the things you designed in the XML file.
        binding.loginButton.setOnClickListener {
            startActivity(Intent(this@EntryActivity, LoginFragment::class.java))
        }

        binding.signupButton.setOnClickListener {
            startActivity(Intent(this@EntryActivity, SignupFragment::class.java))
        }
    }
}
