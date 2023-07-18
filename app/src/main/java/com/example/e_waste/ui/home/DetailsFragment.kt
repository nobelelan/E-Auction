package com.example.e_waste.ui.home

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.e_waste.R
import com.example.e_waste.databinding.FragmentDetailsBinding
import com.example.e_waste.model.Property
import com.example.e_waste.model.User
import com.example.e_waste.utils.ExtensionFunctions.hide
import com.example.e_waste.utils.ExtensionFunctions.show
import com.example.e_waste.utils.ExtensionFunctions.showToast
import com.example.e_waste.viewmodel.FirebaseViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase


class DetailsFragment : Fragment() {

    private var _binding: FragmentDetailsBinding? = null
    private val binding get() = _binding!!

    private val args by navArgs<DetailsFragmentArgs>()

    private lateinit var firebaseViewModel: FirebaseViewModel

    private lateinit var auth: FirebaseAuth

    private val allPropertiesCollectionRef = Firebase.firestore.collection("allProperties")
    private val varietiesCollectionRef = Firebase.firestore.collection("varieties")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_details, container, false)
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentDetailsBinding.bind(view)

        firebaseViewModel = ViewModelProvider(requireActivity())[FirebaseViewModel::class.java]
        auth = Firebase.auth

        val property = args.currentProperty

        binding.apply {
            txtStartingBid.text = "Starting Bid:\n${property.price}/="
            txtCurrentBid.text = "Current Bid: \n${property.currentBid}/="
            txtTopBidder.text = "Top Bidder: ${property.topBidder}"
            txtTotalBids.text = "Total Bids: ${property.totalBids}"
            txtTopBidderContact.text = "Top Bidder Contact: ${property.topBidderContact}"
            txtPropertyName.text = property.name
            txtPropertyDetails.text = property.description
            Glide.with(requireContext()).load(property.url).into(imgProperty)

            btnPlaceBid.setOnClickListener {
                binding.pbDetails.show()
                val registeredDocumentReference = Firebase.firestore.collection("registeredUsers").document(auth.currentUser?.uid!!)
                val bidAmount = binding.edtBidAmount.text.toString().toInt()
                if (bidAmount > 100){
                    val newBid = bidAmount + property.currentBid?.toInt()!!
                    // handling all properties
                    allPropertiesCollectionRef
                        .whereEqualTo("url", property.url)
                        .whereEqualTo("name", property.name)
                        .whereEqualTo("description", property.description)
                        .get().addOnSuccessListener {
                            if (it.documents.isNotEmpty()){
                                it.forEach{document->
                                    val totalBids = document.toObject<Property>().totalBids.toString().toInt()
                                    allPropertiesCollectionRef.document(document.id)
                                        .update(
                                            hashMapOf(
                                                "currentBid" to newBid.toString(),
                                                "totalBids" to "${totalBids + 1}"
                                                ) as Map<String, Any>
                                        ).addOnSuccessListener {
                                            binding.pbDetails.hide()
                                            requireActivity().showToast("Successfully placed bid")
                                            registeredDocumentReference.get().addOnSuccessListener { userDoc->
                                                val user = userDoc.toObject<User>()
                                                allPropertiesCollectionRef.document(document.id)
                                                    .update(
                                                        hashMapOf(
                                                            "topBidder" to user?.username.toString(),
                                                            "topBidderContact" to user?.contact.toString()
                                                        ) as Map<String, Any>
                                                    )

                                            }
                                        }.addOnFailureListener {
                                            binding.pbDetails.hide()
                                        }
                                }
                            }
                        }.addOnFailureListener {
                            binding.pbDetails.hide()
                        }

                    // Handling varieties of properties
                    varietiesCollectionRef
                        .whereEqualTo("url", property.url)
                        .whereEqualTo("name", property.name)
                        .whereEqualTo("description", property.description)
                        .get().addOnSuccessListener {
                            if (it.documents.isNotEmpty()){
                                it.forEach{document->
                                    varietiesCollectionRef.document(document.id)
                                        .update(
                                            hashMapOf(
                                                "currentBid" to newBid.toString(),
                                            ) as Map<String, Any>
                                        ).addOnSuccessListener {
                                            binding.pbDetails.hide()
                                            requireActivity().showToast("Successfully placed bid")
                                            registeredDocumentReference.get().addOnSuccessListener { userDoc->
                                                val user = userDoc.toObject<User>()
                                                varietiesCollectionRef.document(document.id)
                                                    .update(
                                                        hashMapOf(
                                                            "topBidder" to user?.username.toString(),
                                                            "topBidderContact" to user?.contact.toString()
                                                        ) as Map<String, Any>
                                                    )

                                            }
                                        }.addOnFailureListener {
                                            binding.pbDetails.hide()
                                        }
                                }
                            }
                        }.addOnFailureListener {
                            binding.pbDetails.hide()
                        }

                }else{
                    requireActivity().showToast("Bid amount must be more than 100/=")
                    binding.pbDetails.hide()
                }
            }

        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}