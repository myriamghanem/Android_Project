package com.example.project

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.project.model.Post
import com.example.project.utils.loadImageFromURL

class LostItemAdapter(private val context: Context, private val items: List<Post>) :
    RecyclerView.Adapter<LostItemAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textViewUserName: TextView = itemView.findViewById(R.id.textViewUserName)
        val imageViewLostItem: ImageView = itemView.findViewById(R.id.imageViewLostItem)
        val textViewLostItemName: TextView = itemView.findViewById(R.id.textViewLostItemName)
        val textItemDescription: TextView = itemView.findViewById(R.id.textViewItemDescription)
        val buttonFound: Button = itemView.findViewById(R.id.buttonFound)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_lost_item, parent, false)
        return ViewHolder(view)
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val post = items[position]

        // Bind data to your view components here
        // Adjust the code based on your actual layout and design
        holder.textViewUserName.text = "User" // You may replace it with the actual user information
        holder.imageViewLostItem.loadImageFromURL(post.imageUrl) // Assuming you're using a library like Glide or Picasso to load images
        holder.textViewLostItemName.text = post.itemName
        holder.textItemDescription.text = post.description

        holder.buttonFound.setOnClickListener {
            // Handle button click if needed
            // You may want to update the post status here
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }
}
