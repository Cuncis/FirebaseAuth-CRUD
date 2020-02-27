package com.gdc.loginsample.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.recyclerview.widget.RecyclerView
import com.gdc.loginsample.R
import com.gdc.loginsample.model.Barang
import kotlinx.android.synthetic.main.item_barang.view.*

class BarangAdapter(private val context: Context): RecyclerView.Adapter<BarangAdapter.BarangHolder>() {

    private var barangList: List<Barang> = ArrayList()
    private lateinit var clickListener: ClickListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BarangHolder {
        return BarangHolder(LayoutInflater.from(context).inflate(R.layout.item_barang, parent, false))
    }

    override fun getItemCount(): Int = barangList.size

    override fun onBindViewHolder(holder: BarangHolder, position: Int) = holder.bind(barangList[position])

    fun setBarangList(barangList: List<Barang>) {
        this.barangList = barangList
        notifyDataSetChanged()
    }

    fun setClickListener(clickListener: ClickListener) {
        this.clickListener = clickListener
    }

    inner class BarangHolder(private val view: View): RecyclerView.ViewHolder(view), View.OnClickListener {

        fun bind(barang: Barang) {
            view.tv_namabarang.text = barang.nama
            view.tv_merkBarang.text = barang.merk
            view.tv_hargabarang.text = barang.harga

            view.btn_more.setOnClickListener(this)
            itemView.setOnClickListener(this)
        }

        override fun onClick(p0: View?) {
            if (p0?.id == R.id.btn_more) {
                clickListener.onClickMore(adapterPosition, view.btn_more, barangList[adapterPosition])
            } else {
                clickListener.onClick(adapterPosition)
            }
        }
    }

    interface ClickListener {
        fun onClick(position: Int)
        fun onClickMore(position: Int, button: ImageButton, barang: Barang)
    }
}