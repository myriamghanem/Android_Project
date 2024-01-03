package com.example.project

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.project.databinding.FragmentLostBinding
import com.example.project.model.Post
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage

class LostFragment : Fragment() {

    private var _binding: FragmentLostBinding? = null
    private val binding get() = _binding!!

    private lateinit var pickImageLauncher: ActivityResultLauncher<Intent>
    private lateinit var selectedImageUri: Uri // Store the selected image URI

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLostBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        pickImageLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                selectedImageUri = result.data?.data!!
                binding.imageView.setImageURI(selectedImageUri)
            }
        }

        binding.buttonPickImage.setOnClickListener {
            openGalleryForImage()
        }

        binding.buttonFirst.setOnClickListener {
            val itemName = binding.editTextItemName.text.toString().trim()
            val description = binding.editTextDescription.text.toString().trim()
            val location = binding.editTextLocation.text.toString().trim()

            if (itemName.isNotEmpty() && description.isNotEmpty() && ::selectedImageUri.isInitialized) {
                uploadImageToStorageAndAddPost(itemName, description, location)
            } else {
                Toast.makeText(requireContext(), "Fill in all fields", Toast.LENGTH_SHORT).show()
            }
        }

        // Add back button click listener
        binding.buttonBack.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun openGalleryForImage() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        pickImageLauncher.launch(intent)
    }

    private fun uploadImageToStorageAndAddPost(itemName: String, description: String, location: String) {
        val storageRef = FirebaseStorage.getInstance().reference.child("images/${selectedImageUri.lastPathSegment}")
        storageRef.putFile(selectedImageUri).addOnSuccessListener {
            storageRef.downloadUrl.addOnSuccessListener { uri ->
                addDataToFirestore(itemName, description, location, uri.toString())
            }
        }.addOnFailureListener {
            // Handle any errors
        }
    }

    private fun addDataToFirestore(itemName: String, description: String, location: String, imageUrl: String) {
        val db = FirebaseFirestore.getInstance()
        val post = Post(
            itemName = itemName,
            description = description,
            imageUrl = imageUrl,
            location = location
        )

        // Generate a unique ID for the post
        val postId = db.collection("Post").document().id
        post.id = postId // Set the generated ID in the post object

        db.collection("Post").document(postId).set(post) // Use the generated ID when adding the post
            .addOnSuccessListener {
                Toast.makeText(requireContext(), "Post added successfully", Toast.LENGTH_SHORT).show()
                findNavController().navigate(R.id.action_FirstFragment_to_SecondFragment)
            }
            .addOnFailureListener { e ->
                Toast.makeText(requireContext(), "Error adding post: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
