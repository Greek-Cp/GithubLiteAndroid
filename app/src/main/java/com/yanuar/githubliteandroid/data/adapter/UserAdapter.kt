package com.yanuar.githubliteandroid.data.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.facebook.shimmer.Shimmer
import com.facebook.shimmer.ShimmerDrawable
import com.google.android.material.card.MaterialCardView
import com.yanuar.githubliteandroid.R
import com.yanuar.githubliteandroid.data.model.ItemsItem
class UserAdapter(private var users: List<ItemsItem>, private val onItemClick: (String) -> Unit) : RecyclerView.Adapter<UserAdapter.ViewHolder>() {
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(item: ItemsItem, isSelected: Boolean){
            itemView.findViewById<MaterialCardView>(R.id.id_card_layout)
                .setOnClickListener{
                onItemClick(item.login.toString())
            }
            val tv_username = itemView.findViewById<TextView>(R.id.tvUserName)
            tv_username.setText(item.login)
            val imageView = itemView.findViewById<ImageView>(R.id.imgUserAvatar)
            val shimmer = Shimmer.AlphaHighlightBuilder()
                .setDuration(1800)
                .setBaseAlpha(0.7f)
                .setHighlightAlpha(0.6f)
                .setDirection(Shimmer.Direction.RIGHT_TO_LEFT)
                .setAutoStart(true)
                .build()
            val shimmerDrawable = ShimmerDrawable().apply {
                setShimmer(shimmer)
            }
            Glide.with(itemView.context)
                .load(item.avatarUrl)
                .placeholder(shimmerDrawable)
                .into(imageView)
            itemView.setOnClickListener {
                if (selectedPosition != adapterPosition) {
                    notifyItemChanged(selectedPosition)
                    selectedPosition = adapterPosition
                    notifyItemChanged(selectedPosition)
                }
            }
        }
    }
    var selectedPosition = RecyclerView.NO_POSITION
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_user, parent, false)
        return ViewHolder(view)
    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val category = users[position]
        val isSelected = position == selectedPosition
        holder.bind(category, isSelected)
    }
    override fun getItemCount(): Int = users.size
    fun updateUsers(newUsers: List<ItemsItem>) {
        Log.d("update", "Update Users")
        this.users = newUsers
        notifyDataSetChanged()
    }
}
