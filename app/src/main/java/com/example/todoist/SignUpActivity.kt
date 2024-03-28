package com.example.todoist

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.todoist.PrivacyActivity.PrivacyPolicyActivity
import com.example.todoist.databinding.ActivitySignUpBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class SignUpActivity : AppCompatActivity() {

    private lateinit var binding : ActivitySignUpBinding
    private lateinit var database : DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.btnNext.setOnClickListener {
            if(binding.editTextUsername.text.isNullOrEmpty()||binding.editTextEmail.text.isNullOrEmpty()
                ||binding.editTextPassword.text.isNullOrEmpty()) {
                showToast("fill field")
            }else {
                if (binding.maleRB.isChecked||binding.femaleRB.isChecked) {
                    if(binding.privacyCB.isChecked){
                        val gender : String
                        if (binding.femaleRB.isChecked) {
                            gender = "female"
                        }else
                            gender = "male"
                        writeDataToDatabase(gender)
                    }else {
                        val gender : String
                        if (binding.femaleRB.isChecked) {
                            gender = "female"
                        }else
                            gender = "male"
                        writeDataToDatabase(gender)
                    }
                }else {
                    showToast("please check the radio button")
                }
            }
        }
        binding.privacyButton.setOnClickListener {
            val intent = Intent(applicationContext , PrivacyPolicyActivity::class.java)
            startActivity(intent)
        }
        binding.backButton.setOnClickListener {
            finish()
        }
    }

    private fun writeDataToDatabase(gender: String) {
        database = FirebaseDatabase.getInstance().getReference("users")

        val data = DataClassForDatabase(binding.editTextEmail.text.toString() , binding.editTextUsername.text.toString() , binding.editTextPassword.text.toString() , gender)

        database.child(binding.editTextUsername.text.toString()).setValue(data).addOnSuccessListener {
            showToast("Successfully SignUp")
        }.addOnFailureListener {
            showToast("failed")
        }
    }

    private fun showToast(s: String) {
        Toast.makeText(applicationContext , s , Toast.LENGTH_SHORT ).show()
    }
}