package com.example.appnews.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.appnews.Models.ModelClass
import com.example.appnews.R
import com.example.appnews.WebViewActivity

class NewsAdapter(
    val context: Context,var modelClassArraylist: ArrayList<ModelClass>
) : RecyclerView.Adapter<NewsAdapter.ViewHolder>() {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val mainheading: TextView = view.findViewById(R.id.mainheading)
        val content: TextView = view.findViewById(R.id.content)
        val time: TextView = view.findViewById(R.id.time)
        val author: TextView = view.findViewById(R.id.author)
        val image: ImageView = view.findViewById(R.id.imageView)
        val cardView:CardView=view.findViewById(R.id.cardView)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.layout_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.cardView.setOnClickListener {
            val intent=Intent(context, WebViewActivity::class.java)
            intent.putExtra("url", modelClassArraylist[position].url)
            context.startActivity(intent)
        }
        holder.time.text="published At ${modelClassArraylist[position].publishedAt}"
        holder.author.text=modelClassArraylist[position].author
        holder.mainheading.text=modelClassArraylist[position].title
        holder.content.text=modelClassArraylist[position].description
        Glide.with(context).load(modelClassArraylist[position].urlToImage).into(holder.image)
    }
    override fun getItemCount(): Int = modelClassArraylist.size
}