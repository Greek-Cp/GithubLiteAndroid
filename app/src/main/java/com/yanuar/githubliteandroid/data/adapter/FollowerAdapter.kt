package com.yanuar.githubliteandroid.data.adapter


import android.graphics.drawable.Drawable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.facebook.shimmer.Shimmer
import com.facebook.shimmer.ShimmerDrawable
import com.yanuar.githubliteandroid.R
import com.yanuar.githubliteandroid.data.model.GithubSearchResponse
import com.yanuar.githubliteandroid.data.model.GithubUserFollowerItem
import com.yanuar.githubliteandroid.data.model.ItemsItem
import com.yanuar.githubliteandroid.ui.fragment.SearchUserFragment
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone

class FollowerAdapter(private var users: List<GithubUserFollowerItem>, private val onItemClick: (String) -> Unit) : RecyclerView.Adapter<FollowerAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(item: GithubUserFollowerItem, isSelected: Boolean){


            val textView = itemView.findViewById<TextView>(R.id.tvUserName)
            textView.setOnClickListener{
                onItemClick(item.login.toString())
            }
            val imageView = itemView.findViewById<ImageView>(R.id.imgUserAvatar)
            textView.setText(item.login)
            val shimmer = Shimmer.AlphaHighlightBuilder()// The attributes for a ShimmerDrawable is set by this builder
                .setDuration(1800) // how long the shimmering animation takes to do one full sweep
                .setBaseAlpha(0.7f) //the alpha of the underlying children
                .setHighlightAlpha(0.6f) // the shimmer alpha amount
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
    fun updateUsers(newUsers: List<GithubUserFollowerItem>) {
        Log.d("update", "Update Users")
        this.users = newUsers
        notifyDataSetChanged()
    }
}
