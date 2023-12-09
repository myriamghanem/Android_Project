package com.example.project

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.project.databinding.FragmentSecondBinding
import com.example.project.model.Post
import com.google.firebase.firestore.FirebaseFirestore

class SecondFragment : Fragment() {

    private var _binding: FragmentSecondBinding? = null
    private val binding get() = _binding!!

    private lateinit var recyclerView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSecondBinding.inflate(inflater, container, false)
        recyclerView = binding.recyclerView  // Initialize recyclerView here
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Your existing code for top bar, buttons, and separator

        // Initialize RecyclerView
        recyclerView.layoutManager = LinearLayoutManager(context)

        // Fetch lost items from Firestore
        fetchLostItemsFromFirestore()

        // Your existing code for bottom section
    }

    private fun fetchLostItemsFromFirestore() {
        val db = FirebaseFirestore.getInstance()

        db.collection("Post")
            .get()
            .addOnSuccessListener { result ->
                val lostItemsList = mutableListOf<Post>()

                for (document in result) {
                    val post = document.toObject(Post::class.java)
                    lostItemsList.add(post)
                }

                val lostItemAdapter = LostItemAdapter(requireContext(), lostItemsList)
                recyclerView.adapter = lostItemAdapter

                Log.d(TAG, "Data retrieved successfully. Item count: ${lostItemsList.size}")
            }
            .addOnFailureListener { exception ->
                // Handle failures
                Log.e(TAG, "Error getting documents.", exception)
            }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
