package com.example.e_waste.ui.home.lostfound

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView.OnQueryTextListener
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.e_waste.R
import com.example.e_waste.databinding.FragmentFindBinding
import com.example.e_waste.model.FoundProperty
import com.example.e_waste.utils.ExtensionFunctions.showToast
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObjects
import com.google.firebase.ktx.Firebase


class FindFragment : Fragment() {

    private var _binding: FragmentFindBinding? = null
    private val binding get() = _binding!!

    private val findAdapter by lazy { FindAdapter() }

    private var foundItemCollectionRef = Firebase.firestore.collection("foundItems")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_find, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentFindBinding.bind(view)


        setUpFindRecyclerView()

        foundItemCollectionRef.addSnapshotListener { value, error ->
            error?.let {
                requireActivity().showToast("Something went wrong while retrieving data!")
            }
            value?.let {querySnapshot ->
                if(querySnapshot.documents.isNotEmpty()){
                    val foundProperties = querySnapshot.toObjects<FoundProperty>()
                    findAdapter.differCallBack.submitList(foundProperties)
                }
            }
        }

        binding.searchView.setOnQueryTextListener(object : OnQueryTextListener{
            override fun onQueryTextSubmit(propertyName: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(propertyName: String?): Boolean {
                if (propertyName != null) {
                    foundItemCollectionRef
                        .whereGreaterThanOrEqualTo("name", propertyName)
                        .whereLessThan("name", propertyName + "\uf8ff")
                        .get().addOnSuccessListener {
                            if (it.documents.isNotEmpty()){
                                val foundProperties = it.toObjects<FoundProperty>()
                                findAdapter.differCallBack.submitList(foundProperties)
                            }
                        }
                }
                return true
            }
        })


    }

    private fun setUpFindRecyclerView() = binding.rvFind.apply {
        adapter = findAdapter
        layoutManager = LinearLayoutManager(requireContext())
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}