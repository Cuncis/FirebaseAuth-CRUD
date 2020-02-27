package com.gdc.loginsample

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.PopupMenu
import androidx.recyclerview.widget.LinearLayoutManager
import com.gdc.loginsample.adapter.BarangAdapter
import com.gdc.loginsample.model.Barang
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_home.*

class HomeActivity : AppCompatActivity(), BarangAdapter.ClickListener {

    private lateinit var database: DatabaseReference

    private lateinit var barangList: ArrayList<Barang>
    private lateinit var barangAdapter: BarangAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        database = FirebaseDatabase.getInstance().reference
        barangList = ArrayList()
        barangAdapter = BarangAdapter(this)

        Log.d("_Firebase", "UserId: " + FirebaseAuth.getInstance().currentUser?.uid)
        initRecyclerView()

        showData()

        btn_add.setOnClickListener {
            val intent = Intent(this, AddEditActivity::class.java)
            startActivity(intent)
        }

        btn_logout.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
            FirebaseAuth.getInstance().signOut()
        }

    }

    private fun showData() {
        database.child(FirebaseAuth.getInstance().currentUser?.uid.toString())
                .addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(p0: DataSnapshot) {
                        barangList.clear()

                        for (data in p0.children) {
                            val barang = data.getValue(Barang::class.java)
                            barang?.key = data.key

                            barangList.add(barang!!)
                        }
                        barangAdapter.setBarangList(barangList)
                    }
                    override fun onCancelled(p0: DatabaseError) {
                        Log.d("_Firebase", "Error: ${p0.message}")
                    }
                })
    }

    private fun initRecyclerView() {
        rv_barang.apply {
            layoutManager = LinearLayoutManager(this@HomeActivity)
            setHasFixedSize(true)
            adapter = barangAdapter
        }
        barangAdapter.setClickListener(this)
    }

    override fun onClick(position: Int) {
        Toast.makeText(this, "" + barangList[position].nama, Toast.LENGTH_SHORT).show()
    }

    override fun onClickMore(position: Int, button: ImageButton, barang: Barang) {
        val popup = PopupMenu(this, button)
        popup.inflate(R.menu.menu_more_item)
        popup.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.menu_edit -> {
                    Toast.makeText(this, "Edit", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, AddEditActivity::class.java)
                    intent.putExtra("BARANG_EXTRA", barang)
                    startActivityForResult(intent, 1)
                    true
                }
                R.id.menu_delete -> {
                    dialogDelete(barang)
                    true
                }
                else -> false
            }
        }
        popup.show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1) {
            if (resultCode == Activity.RESULT_OK) {
                val msg = data?.getStringExtra("BARANG_EXTRA")
                Toast.makeText(this, "" + msg, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun dialogDelete(barang: Barang) {
        val builder = AlertDialog.Builder(this)
        builder.setCancelable(true)
        builder.setTitle("Confirm")
        builder.setMessage("Are you sure want to delete this item?")
        builder.setPositiveButton("Yes") { dialog, _ ->
            database.child(FirebaseAuth.getInstance().currentUser?.uid.toString())
                .child(barang.key!!)
                .removeValue()
                .addOnSuccessListener {
                    Toast.makeText(this, "Item successfully deleted...", Toast.LENGTH_SHORT).show()
                }
            dialog.dismiss()
        }
        builder.setNegativeButton("NO") { dialog, _ ->
            dialog.dismiss()
        }

        builder.show()

    }
}


















