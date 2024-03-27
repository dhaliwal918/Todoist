package com.example.todoist

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.todoist.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {

    private lateinit var binding : ActivityLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val username = binding.editTextUsername.text.toString()
        val password = binding.editTextPassword.text.toString()

        binding.btnNext.setOnClickListener {
            UsernameNotExistDialog()
        }

    }

    private fun UsernameNotExistDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setIcon(R.drawable.baseline_browser_not_supported_24)
        builder.setTitle("Username not found")
        builder.setMessage("Are you want to add?")
        builder.setPositiveButton("Yes" , DialogInterface.OnClickListener { dialog, which ->
            val intent = Intent(this , SignUpActivity::class.java)
            startActivity(intent)
        }).setNegativeButton("No" , DialogInterface.OnClickListener { dialog, which ->
            dialog.dismiss()
        })
        val alertDialog = builder.create()
        alertDialog.show()
    }

}