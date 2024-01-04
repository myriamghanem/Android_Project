package com.example.project

import LostItemAdapter
import android.content.ContentValues
import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.project.databinding.FragmentSecondBinding
import com.example.project.model.Found
import com.example.project.model.Post
import com.google.firebase.firestore.FirebaseFirestore

class SecondFragment : Fragment() {

    private var _binding: FragmentSecondBinding? = null
    private val binding get() = _binding!!

    private lateinit var recyclerView: RecyclerView
    private lateinit var lostButton: Button
    private lateinit var foundButton: Button
    private lateinit var addButton: Button
    private lateinit var logoutButton: ImageButton

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


        val logoutButton: ImageButton = view.findViewById(R.id.logoutButton)

        // Set a click listener for the logoutButton
        logoutButton.setOnClickListener {
            // Handle the click event here
            logout()
        }

        // Initially hide the FloatingActionButton

        makeBold(lostButton)

        lostButton.setOnClickListener {
            fetchLostItemsFromFirestore()
            makeBold(lostButton)
            makeNormal(foundButton)
            navigateToAddFragment(R.id.action_SecondFragment_to_LostFragment)

            // Show the FloatingActionButton when Lost is clicked
        }

        foundButton.setOnClickListener {
            fetchFoundItemsFromFirestore()
            makeBold(foundButton)
            makeNormal(lostButton)
            navigateToAddFragment(R.id.action_SecondFragment_to_FoundFragment)

            // Show the FloatingActionButton when Found is clicked
        }

        recyclerView.layoutManager = LinearLayoutManager(context)

        // Fetch lost items initially
        fetchLostItemsFromFirestore()
    }
    private fun logout() {
        // Perform any necessary logout actions here
        // For example, you might want to clear user authentication state

        // Navigate to the EntryActivity
        val entryActivityIntent = Intent(requireContext(), EntryActivity::class.java)
        startActivity(entryActivityIntent)

        // Optionally, you can finish the current activity to prevent going back to it
        requireActivity().finish()
    }
    private fun makeBold(button: Button) {
        button.setTypeface(null, Typeface.BOLD)
    }

    private fun makeNormal(button: Button) {
        button.setTypeface(null, Typeface.NORMAL)
    }

    private fun fetchFoundItemsFromFirestore() {
        val db = FirebaseFirestore.getInstance()

        val query = db.collection("Found")
            .whereEqualTo("isclaimed", false)
        // Add a snapshot listener to listen for real-time updates
        query.addSnapshotListener { value, error ->
            if (error != null) {
                Log.e(ContentValues.TAG, "Error getting found items.", error)
                return@addSnapshotListener
            }

            val foundItemsList = mutableListOf<Found>()

            for (document in value!!) {
                val foundItem = document.toObject(Found::class.java)
                foundItemsList.add(foundItem)
            }

            val foundItemAdapter = FoundItemAdapter(requireContext(), foundItemsList)
            recyclerView.adapter = foundItemAdapter
        }
    }

    private fun fetchLostItemsFromFirestore() {
        val db = FirebaseFirestore.getInstance()

        val query = db.collection("Post")
            .whereEqualTo("isfound", false)

        // Add a snapshot listener to listen for real-time updates
        query.addSnapshotListener { value, error ->
            if (error != null) {
                Log.e(ContentValues.TAG, "Error getting lost items.", error)
                return@addSnapshotListener
            }

            val lostItemsList = mutableListOf<Post>()

            for (document in value!!) {
                val lostItem = document.toObject(Post::class.java)
                lostItemsList.add(lostItem)
            }

            val lostItemAdapter = LostItemAdapter(requireContext(), lostItemsList)
            recyclerView.adapter = lostItemAdapter

            Log.d(ContentValues.TAG, "Lost items retrieved successfully. Item count: ${lostItemsList.size}")
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
