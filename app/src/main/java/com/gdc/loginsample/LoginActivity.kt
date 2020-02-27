package com.gdc.loginsample

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.gdc.loginsample.model.Admin
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var ref: DatabaseReference
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        ref = FirebaseDatabase.getInstance().reference
        auth = FirebaseAuth.getInstance()

        if (FirebaseAuth.getInstance().currentUser != null) {
            val intent = Intent(this, HomeActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        } else {
            Log.d("_FirebaseAuth", "Has been logout...")
        }

        btn_masuk.setOnClickListener(this)
        btn_daftar.setOnClickListener(this)
    }

    override fun onClick(p0: View?) {
        if (p0?.id == R.id.btn_masuk) {
            signIn()
        } else if (p0?.id == R.id.btn_daftar) {
            signUp()
        }
    }

    private fun signIn() {
        Log.d("_FirebaseAuth", "SignIn...")

        val email = et_email.text.toString()
        val pass = et_pass.text.toString()

        auth.signInWithEmailAndPassword(email, pass)
            .addOnCompleteListener(this) {
                Log.d("_Login","" + it.isSuccessful)

                if (it.isSuccessful) {
                    onAuthSuccess(it.result?.user!!)
                } else {
                    Toast.makeText(this, "Sign In Failed",
                        Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun signUp() {
        Log.d("_FirebaseAuth", "SignUp...")

        val email = et_email.text.toString()
        val pass = et_pass.text.toString()

        auth.createUserWithEmailAndPassword(email, pass)
            .addOnCompleteListener(this) {
                Log.d("_Register","" + it.isSuccessful)

                if (it.isSuccessful) {
                    onAuthSuccess(it.result?.user!!)
                } else {
                    Toast.makeText(this, "Sign Up Failed",
                        Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun onAuthSuccess(user: FirebaseUser) {
        val username = usernameFromEmail(user.email!!)

        writeNewAdmin(user.uid, username, user.email!!)
        startActivity(Intent(this, HomeActivity::class.java))
        finish()
    }

    private fun usernameFromEmail(email: String): String {
        return if (email.contains("@")) {
            email.split("@")[0]
        } else {
            email
        }
    }

    private fun writeNewAdmin(userId: String, name: String, email: String) {
        val admin = Admin(name, email)

        ref.child("admins").child(userId).setValue(admin)
    }
}
