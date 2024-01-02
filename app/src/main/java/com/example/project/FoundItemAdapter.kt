package com.example.project

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.project.model.Found
import com.example.project.utils.loadImageFromURL

class FoundItemAdapter(private val context: Context, private val items: List<Found>) :
    RecyclerView.Adapter<FoundItemAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textViewUserName: TextView = itemView.findViewById(R.id.textViewUserName)
        val imageViewFoundItem: ImageView = itemView.findViewById(R.id.imageViewFoundItem)
        val textViewFoundItemName: TextView = itemView.findViewById(R.id.textViewFoundItemName)
        val textItemDescription: TextView = itemView.findViewById(R.id.textViewItemDescription)
        val buttonClaim: Button = itemView.findViewById(R.id.buttonClaim)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_found_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val foundItem = items[position]

        // Bind data to your view components here
        // Adjust the code based on your actual layout and design
        holder.textViewUserName.text = "User" // You may replace it with the actual user information
        holder.imageViewFoundItem.loadImageFromURL(foundItem.imageUrl) // Assuming you're using a library like Glide or Picasso to load images
        holder.textViewFoundItemName.text = foundItem.itemName
        holder.textItemDescription.text = foundItem.description

        holder.buttonClaim.setOnClickListener {
            // Handle button click if needed
            // You may want to update the found item status here
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }
}
