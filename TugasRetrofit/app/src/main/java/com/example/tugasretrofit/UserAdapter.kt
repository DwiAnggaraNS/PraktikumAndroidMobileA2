package com.example.tugasretrofit

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.tugasretrofit.model.Data

class UserAdapter(
    private var userList: List<Data>, // Daftar data user
    private val onClickUser: (Data) -> Unit // Fungsi lambda untuk handle klik item
) : RecyclerView.Adapter<UserAdapter.UserViewHolder>() {

    inner class UserViewHolder(itemView: View, private val onClickUser: (Data) -> Unit) : RecyclerView.ViewHolder(itemView) {
        private val tvName: TextView = itemView.findViewById(R.id.tv_name)
        private val tvEmail: TextView = itemView.findViewById(R.id.tv_email)
        private val imgProfile: ImageView = itemView.findViewById(R.id.img_profile)

        fun bind(user: Data) {
            tvName.text = "${user.firstName} ${user.lastName}"
            tvEmail.text = user.email

            // Load image using Glide
            Glide.with(itemView.context)
                .load(user.avatar)
                .into(imgProfile)

            // Set click listener
            itemView.setOnClickListener {
                onClickUser(user)
            }
        }
    }

    // Tambahkan fungsi untuk update data
    fun updateData(newList: List<Data>) {
        userList = newList
        notifyDataSetChanged()
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_user, parent, false)
        return UserViewHolder(view, onClickUser)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        holder.bind(userList[position])
    }

    override fun getItemCount(): Int = userList.size
}
