import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.project.R
import com.example.project.model.Found
import com.example.project.model.Post
import com.example.project.utils.loadImageFromURL
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore

class LostItemAdapter(private val context: Context, private val items: List<Post>) :
    RecyclerView.Adapter<LostItemAdapter.ViewHolder>() {

    private val db = FirebaseFirestore.getInstance()

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textViewUserName: TextView = itemView.findViewById(R.id.textViewUserName)
        val imageViewLostItem: ImageView = itemView.findViewById(R.id.imageViewLostItem)
        val textViewLostItemName: TextView = itemView.findViewById(R.id.textViewLostItemName)
        val textItemDescription: TextView = itemView.findViewById(R.id.textViewItemDescription)
        val buttonFound: Button = itemView.findViewById(R.id.buttonFound)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_lost_item, parent, false)
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
            updateIsFoundInDatabase(post.id)
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    private fun updateIsFoundInDatabase(documentId: String) {
        val postRef = db.collection("Post").document(documentId)

        // Update the Isfound field to true
        postRef.update("isfound", true)
            .addOnSuccessListener {
                // Update successful
                // Add the item to the Found table when isfound is true
                    addToFoundTable(postRef)
            }
            .addOnFailureListener { e ->
                // Handle failure
                // You may log the error or display a message to the user
            }
    }

    private fun addToFoundTable(postRef: DocumentReference) {
        // Get the post data from the Firestore document
        postRef.get()
            .addOnSuccessListener { documentSnapshot ->
                if (documentSnapshot.exists()) {
                    // Convert the document snapshot to a Post object
                    val post = documentSnapshot.toObject(Post::class.java)

                    // Create a Found object using the data from the Post object
                    val foundItem = Found(
                        id = post?.id ?: "",
                        itemName = post?.itemName ?: "",
                        description = post?.description ?: "",
                        imageUrl = post?.imageUrl ?: "",
                        location = "", // Set location based on your requirements
                        userId = post?.userId ?: 0,
                        claimed = false
                    )

                    // Add the found item to the "Found" table
                    db.collection("Found").add(foundItem)
                        .addOnSuccessListener {
                            // Item added to Found table successfully
                            // You may handle success as needed
                        }
                        .addOnFailureListener { e ->
                            // Handle failure
                            // You may log the error or display a message to the user
                        }
                }
            }
            .addOnFailureListener { e ->
                // Handle failure
                // You may log the error or display a message to the user
            }
    }

}