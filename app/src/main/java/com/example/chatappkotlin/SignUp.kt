package com.example.chatappkotlin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import com.example.chatappkotlin.databinding.ActivitySignUpBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class SignUp : AppCompatActivity() {
    private lateinit var binding: ActivitySignUpBinding
    private lateinit var auth: FirebaseAuth
    var databaseReference: DatabaseReference?=null
    var database:FirebaseDatabase?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        database  = FirebaseDatabase.getInstance()
        databaseReference =database?.reference!!.child("Profile")

        binding.btnSignUp.setOnClickListener {
            val email = binding.etEmail.text.toString()
            val name = binding.etName.text.toString()
            val password = binding.etpassword.text.toString()

            if (TextUtils.isEmpty(name)){
                binding.etName.error = "Please enter your name"
                return@setOnClickListener
            }else if (TextUtils.isEmpty(password)){
                binding.etpassword.error = "Please enter your password"
                return@setOnClickListener
            }else if (TextUtils.isEmpty(email)){
                binding.etEmail.error = "Please enter your email"
                return@setOnClickListener
            }
            auth.createUserWithEmailAndPassword(binding.etEmail.text.toString(),binding.etpassword.text.toString())
                .addOnCompleteListener(this){ task ->
                    if (task.isSuccessful){
                        val currentUser = auth.currentUser
                        val currentUserDb = currentUser?.let { it1 -> databaseReference?.child(it1.uid) }
                        currentUserDb?.child("name")?.setValue(binding.etName.text.toString())
                        Toast.makeText(this@SignUp,"Sign up succesfully",Toast.LENGTH_SHORT).show()
                    }else{
                        Toast.makeText(this@SignUp,"Sign up not succesfully",Toast.LENGTH_SHORT).show()
                    }

                }
        }
        binding.btnLogin.setOnClickListener {
            intent = Intent(applicationContext,Login::class.java)
            startActivity(intent)
            finish()
        }
    }
}