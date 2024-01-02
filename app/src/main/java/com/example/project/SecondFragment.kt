package com.example.project

import android.content.ContentValues
import android.graphics.Typeface
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.project.databinding.FragmentSecondBinding
import com.example.project.model.Found
import com.example.project.model.Post
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.firestore.FirebaseFirestore

class SecondFragment : Fragment() {

    private var _binding: FragmentSecondBinding? = null
    private val binding get() = _binding!!

    private lateinit var recyclerView: RecyclerView
    private lateinit var lostButton: Button
    private lateinit var foundButton: Button
    private lateinit var addButton: FloatingActionButton


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSecondBinding.inflate(inflater, container, false)
        recyclerView = binding.recyclerView
        lostButton = binding.buttonLost
        foundButton = binding.buttonFind
        addButton = binding.addButton
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        makeBold(lostButton)

        lostButton.setOnClickListener {
            fetchLostItemsFromFirestore()
            makeBold(lostButton)
            makeNormal(foundButton)
            navigateToAddFragment(R.id.action_SecondFragment_to_LostFragment)
        }

        foundButton.setOnClickListener {
            fetchFoundItemsFromFirestore()
            makeBold(foundButton)
            makeNormal(lostButton)
            navigateToAddFragment(R.id.action_SecondFragment_to_FoundFragment)
        }

        recyclerView.layoutManager = LinearLayoutManager(context)

        fetchLostItemsFromFirestore()
    }

    private fun makeBold(button: Button) {
        button.setTypeface(null, Typeface.BOLD)
    }

    private fun makeNormal(button: Button) {
        button.setTypeface(null, Typeface.NORMAL)
    }

    private fun fetchFoundItemsFromFirestore() {
        val db = FirebaseFirestore.getInstance()

        db.collection("Found")
            .get()
            .addOnSuccessListener { result ->
                val foundItemsList = mutableListOf<Found>()

                for (document in result) {
                    val foundItem = document.toObject(Found::class.java)
                    foundItemsList.add(foundItem)
                }

                val foundItemAdapter = FoundItemAdapter(requireContext(), foundItemsList)
                recyclerView.adapter = foundItemAdapter
            }
            .addOnFailureListener { exception ->
                // Handle failures
                Log.e(ContentValues.TAG, "Error getting found items.", exception)
            }
    }

    private fun fetchLostItemsFromFirestore() {
        val db = FirebaseFirestore.getInstance()

        db.collection("Post")
            .get()
            .addOnSuccessListener { result ->
                val lostItemsList = mutableListOf<Post>()

                for (document in result) {
                    val lostItem = document.toObject(Post::class.java)
                    lostItemsList.add(lostItem)
                }

                val lostItemAdapter = LostItemAdapter(requireContext(), lostItemsList)
                recyclerView.adapter = lostItemAdapter

                Log.d(ContentValues.TAG, "Lost items retrieved successfully. Item count: ${lostItemsList.size}")

            }
            .addOnFailureListener { exception ->
                // Handle failures
                Log.e(ContentValues.TAG, "Error getting lost items.", exception)
            }
    }

    private fun navigateToAddFragment(actionId: Int) {
        addButton.setOnClickListener {
            findNavController().navigate(actionId)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
