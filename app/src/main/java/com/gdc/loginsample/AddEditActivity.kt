package com.gdc.loginsample

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import com.gdc.loginsample.model.Barang
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_add_edit.*

class AddEditActivity : AppCompatActivity() {

    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_edit)

        database = FirebaseDatabase.getInstance().reference

        if (intent.hasExtra("BARANG_EXTRA")) {
            val barang: Barang? = intent.getParcelableExtra("BARANG_EXTRA")

            et_namabarang.setText(barang?.nama)
            et_merkbarang.setText(barang?.merk)
            et_hargabarang.setText(barang?.harga)

            btn_submit.setOnClickListener {
                barang?.nama = et_namabarang.text.toString()
                barang?.merk = et_merkbarang.text.toString()
                barang?.harga = et_hargabarang.text.toString()

                updateBarang(barang)
            }
        } else {
            btn_submit.setOnClickListener {
                if (et_namabarang.text.isNotEmpty() && et_merkbarang.text.isNotEmpty() && et_hargabarang.text.isNotEmpty()) {
                    submitBarang(Barang(
                        et_namabarang.text.toString(),
                        et_merkbarang.text.toString(),
                        et_hargabarang.text.toString(),
                        FirebaseAuth.getInstance().currentUser?.uid.toString()
                    ))
                } else {
                    Toast.makeText(this, "Data barang tidak boleh kosong", Toast.LENGTH_SHORT).show()

                    val imm: InputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.hideSoftInputFromWindow(et_namabarang.windowToken, 0)
                }
            }
        }

    }

    private fun updateBarang(barang: Barang?) {
        database.child(FirebaseAuth.getInstance().currentUser?.uid.toString())
            .child(barang?.key!!)
            .setValue(barang)
            .addOnSuccessListener {
                val intent = Intent()
                intent.putExtra("BARANG_EXTRA", "Data Berhasil Diubah")
                setResult(Activity.RESULT_OK, intent)
                finish()
            }
            .addOnFailureListener {
                Toast.makeText(this, "Data Gagal Di Ubah", Toast.LENGTH_SHORT).show()
            }
    }

    private fun submitBarang(barang: Barang) {
        database.child(FirebaseAuth.getInstance().currentUser?.uid.toString())
            .push()
            .setValue(barang)
            .addOnCompleteListener {
                et_namabarang.setText("")
                et_merkbarang.setText("")
                et_hargabarang.setText("")
                Toast.makeText(this, "Data Berhasil Ditambahkan", Toast.LENGTH_SHORT).show()
            }
        finish()
    }
}
