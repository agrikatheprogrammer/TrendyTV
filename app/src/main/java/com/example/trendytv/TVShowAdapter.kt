package com.example.trendytv

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners

private const val TAG = "TVShowAdapter"
const val TV_EXTRA = "TV_EXTRA"
class TVShowAdapter(private val context : Context, private val tvshows: List<TVShow>) :
    RecyclerView.Adapter<TVShowAdapter.ViewHolder>() {

    // Expensive operation: create a view
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        Log.i(TAG, "onCreateViewHolder")
        val view = LayoutInflater.from(context).inflate(R.layout.item_tvshow, parent, false)
        return ViewHolder(view)
    }

    // Cheap: simply bind data to an existing viewholder
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Log.i(TAG, "onBindViewHolder position $position")
        val tvshow = tvshows[position]
        holder.bind(tvshow)
    }

    override fun getItemCount() = tvshows.size

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener {
        private val tvPoster = itemView.findViewById<ImageView>(R.id.tvPoster)
        private val tvTitle = itemView.findViewById<TextView>(R.id.tvTitle)
        private val tvOverview = itemView.findViewById<TextView>(R.id.tvOverview)

        init {
            itemView.setOnClickListener(this)
        }

        fun bind(tvshow: TVShow) {
            tvTitle.text = tvshow.title
            tvOverview.text = tvshow.overview
            Glide.with(context)
                .load(tvshow.posterImageURL)
                .centerCrop()
                .transform(RoundedCorners(30))
                .placeholder(R.drawable.hourglass)
                .error(R.drawable.error)
                .into(tvPoster)
        }

        override fun onClick(p0: View?) {
            //1. get notified of particular movie which was clicked on
            val tvshow = tvshows[adapterPosition]
            Toast.makeText(context, tvshow.title, Toast.LENGTH_SHORT).show()
            //2. Use the intent system to navigate to the new activity
            val intent = Intent(context, DetailActivity::class.java)
            intent.putExtra(TV_EXTRA, tvshow)
            context.startActivity(intent)
        }
    }
}
