package com.example

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.tugasroomdatabase.R
import com.example.tugasroomdatabase.database.DataPemilih

class DataPemilihAdapter(
    private val dataList: List<DataPemilih>,
    private val onEditClicked: (DataPemilih) -> Unit,
    private val onDeleteClicked: (DataPemilih) -> Unit,
    private val onDetailClicked: (DataPemilih) -> Unit
) : RecyclerView.Adapter<DataPemilihAdapter.DataPemilihViewHolder>() {

    inner class DataPemilihViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val nomor: TextView = view.findViewById(R.id.nomor)
        val nama: TextView = view.findViewById(R.id.nama)
        val deleteButton: ImageButton = view.findViewById(R.id.delete_button)
        val editButton: ImageButton = view.findViewById(R.id.edit_button)
        val detailButton: ImageButton = view.findViewById(R.id.see_detail_button)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataPemilihViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_card_data_pemilih, parent, false)
        return DataPemilihViewHolder(view)
    }

    override fun onBindViewHolder(holder: DataPemilihViewHolder, position: Int) {
        val data = dataList[position]
        holder.nomor.text = (position + 1).toString()
        holder.nama.text = if (data.nama_pemilih.length > 15) {
            "${data.nama_pemilih.take(15)}..."
        } else {
            data.nama_pemilih
        }
        holder.deleteButton.setOnClickListener { onDeleteClicked(data) }
        holder.editButton.setOnClickListener { onEditClicked(data) }
        holder.detailButton.setOnClickListener { onDetailClicked(data) }
    }

    override fun getItemCount(): Int = dataList.size
}
