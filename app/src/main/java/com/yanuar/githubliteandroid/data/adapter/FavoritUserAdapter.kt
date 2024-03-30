package com.yanuar.githubliteandroid.data.adapter

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
import com.yanuar.githubliteandroid.data.model.UserFav

class FavoritUserAdapter(private var users: List<UserFav>, private val onItemClick: (String) -> Unit) : RecyclerView.Adapter<FavoritUserAdapter.ViewHolder>() {
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(userFav: UserFav){
            itemView.findViewById<MaterialCardView>(R.id.id_card_layout).setOnClickListener {
                onItemClick(userFav.username)
            }
            val tvUsername = itemView.findViewById<TextView>(R.id.tvUserName)
            tvUsername.text = userFav.username
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
                .load(userFav.avatarUrl)
                .placeholder(shimmerDrawable)
                .into(imageView)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_user, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(users[position])
    }

    override fun getItemCount(): Int = users.size

    fun updateUsers(newUsers: List<UserFav>) {
        this.users = newUsers
        notifyDataSetChanged()
    }
}
