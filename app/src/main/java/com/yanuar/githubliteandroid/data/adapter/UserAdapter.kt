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
import com.yanuar.githubliteandroid.data.model.ItemsItem
import com.yanuar.githubliteandroid.ui.fragment.SearchUserFragment
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone

class UserAdapter(private var users: List<ItemsItem>, private val onItemClick: (String) -> Unit) : RecyclerView.Adapter<UserAdapter.ViewHolder>() {
    interface OnImageLoadErrorListener {
        fun onImageLoadError(position: Int)
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun formatDateString(originalDateString: String): String {
            val parser = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
            parser.timeZone = TimeZone.getTimeZone("UTC") // Ensure the parser is set to UTC
            val date = parser.parse(originalDateString)
            val formatter = SimpleDateFormat("d MMMM yyyy", Locale("id", "ID")) // Indonesian locale for "Maret" etc.
            return formatter.format(date)
        }

        fun bind(item: ItemsItem, isSelected: Boolean){


            val textView = itemView.findViewById<TextView>(R.id.tvUserName)
            textView.setOnClickListener{
                onItemClick(item.login.toString())
            }
            val textViewDescription = itemView.findViewById<TextView>(R.id.tvUserUrl)
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
//            textView.text = item.title
//            textViewDescription.text = truncateText(item.shortDescription,88)
//            textViewDatePublish.text = item.date

            itemView.setOnClickListener {
//                val intent = Intent(itemView.context, DetailActivity::class.java).apply {
//                    putExtra("EXTRA_NEWS", item)
//                }
//                itemView.context.startActivity(intent)

                if (selectedPosition != adapterPosition) {
                    notifyItemChanged(selectedPosition)
                    selectedPosition = adapterPosition
                    notifyItemChanged(selectedPosition)
                }
            }
        }
    }
    fun truncateText(text: String, maxLength: Int): String {
        return if (text.length > maxLength) {
            text.substring(0, maxLength - 3) + "..."
        } else {
            text
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
        notifyDataSetChanged() // Notify any registered observers that the data set has changed.
    }
}
