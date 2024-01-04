package com.example.project

import android.app.AlertDialog
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
import com.google.firebase.firestore.FirebaseFirestore

class FoundItemAdapter(private val context: Context, private val items: List<Found>) :
    RecyclerView.Adapter<FoundItemAdapter.ViewHolder>() {

    private val db = FirebaseFirestore.getInstance()

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        //represent various views based on their IDs in the item layout.
        val textViewUserName: TextView = itemView.findViewById(R.id.textViewUserName)
        val imageViewFoundItem: ImageView = itemView.findViewById(R.id.imageViewFoundItem)
        val textViewFoundItemName: TextView = itemView.findViewById(R.id.textViewFoundItemName)
        val textItemDescription: TextView = itemView.findViewById(R.id.textViewItemDescription)
        val textItemLocation : TextView = itemView.findViewById(R.id.textViewItemLocation)
        val buttonClaim: Button = itemView.findViewById(R.id.buttonClaim)
    }

    //creation of views
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_found_item, parent, false)
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
        holder.textItemLocation.text = foundItem.location

        holder.buttonClaim.setOnClickListener {
            // Show a confirmation dialog before claiming
            showClaimConfirmationDialog(foundItem.id)
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    private fun showClaimConfirmationDialog(documentId: String) {
        val alertDialogBuilder = AlertDialog.Builder(context)
        alertDialogBuilder.setTitle("Mark Item as Claimed")
        alertDialogBuilder.setMessage("Are you sure you want to mark this item as claimed?")

        alertDialogBuilder.setPositiveButton("OK") { dialog, which ->
            // User clicked OK, proceed with the claim
            updateIsClaimedInDatabase(documentId)
        }

        alertDialogBuilder.setNegativeButton("Cancel") { dialog, which ->
            // User clicked Cancel, do nothing
        }

        val alertDialog = alertDialogBuilder.create()
        alertDialog.show()
    }

    private fun updateIsClaimedInDatabase(documentId: String) {
        val foundRef = db.collection("Found").document(documentId)

        // Add a snapshot listener to listen for real-time updates
        foundRef.addSnapshotListener { snapshot, error ->
            if (error != null) {
                // Handle error
                return@addSnapshotListener
            }

            // Check if the document exists and the isClaimed field is true
            if (snapshot != null && snapshot.exists() && snapshot.getBoolean("isclaimed") == true) {
                // The isClaimed field is true, no need to update UI again
                return@addSnapshotListener
            }

            // Update the isClaimed field to true
            foundRef.update("isclaimed", true)
                .addOnSuccessListener {
                    // Update successful
                    // Note: The real-time listener in fetchFoundItemsFromFirestore will automatically refresh the UI
                }
                .addOnFailureListener { e ->
                    // Handle failure
                    // You may log the error or display a message to the user
                }
        }
    }
}
