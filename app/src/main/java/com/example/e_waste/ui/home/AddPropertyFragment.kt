package com.example.e_waste.ui.home

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.e_waste.R
import com.example.e_waste.databinding.FragmentAddPropertyBinding
import com.example.e_waste.model.User
import com.example.e_waste.utils.Constants
import com.example.e_waste.utils.Constants.APARTMENT
import com.example.e_waste.utils.Constants.BIKE
import com.example.e_waste.utils.Constants.CAR
import com.example.e_waste.utils.Constants.LAND
import com.example.e_waste.utils.Constants.RESTAURANT
import com.example.e_waste.utils.Constants.VARIETIES
import com.example.e_waste.utils.ExtensionFunctions.enable
import com.example.e_waste.utils.ExtensionFunctions.hide
import com.example.e_waste.utils.ExtensionFunctions.show
import com.example.e_waste.utils.ExtensionFunctions.showToast
import com.example.e_waste.utils.Resource
import com.example.e_waste.viewmodel.FirebaseViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference


class AddPropertyFragment : Fragment() {

    private var _binding: FragmentAddPropertyBinding? = null
    private val binding get() = _binding!!

    private lateinit var auth: FirebaseAuth

    private val PICK_IMAGE_REQUEST = 2
    private var selectedImageUri: Uri? = null
    private lateinit var storageReference: StorageReference

    private lateinit var firebaseViewModel: FirebaseViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_property, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentAddPropertyBinding.bind(view)

        auth = Firebase.auth
        // Initialize Firebase Storage reference
        storageReference = FirebaseStorage.getInstance().reference
        firebaseViewModel = ViewModelProvider(requireActivity())[FirebaseViewModel::class.java]

        setUpSpinner()

        binding.imgProduct.setOnClickListener {
            openImagePicker()
        }
    }

    private fun openImagePicker() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, PICK_IMAGE_REQUEST)
    }


    private fun setUpSpinner() {
        val properties = arrayListOf("Variety", "Apartment", "Land", "Car", "Bike", "Restaurant")

        val arrayAdapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_item,
            properties
        )
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerProductType.apply {
            adapter = arrayAdapter
            setSelection(0)
            onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>?, p1: View?, position: Int, p3: Long) {
                    val selectedProductType = parent?.getItemAtPosition(position).toString()

                    if (selectedImageUri != null) {
                        val imageName = selectedImageUri!!.lastPathSegment!! + auth.uid.toString()
                        val imageRef = storageReference.child("images/$imageName")

                        imageRef.putFile(selectedImageUri!!)
                            .addOnSuccessListener {
                                requireActivity().showToast("Image uploaded successfully!")
                                imageRef.downloadUrl.addOnSuccessListener { uri ->
                                    // Store the image URI in your model or perform other actions
                                    when(selectedProductType){
                                        VARIETIES -> {
                                            binding.btnSubmitProperty.setOnClickListener {
                                                addVarieties(uri.toString())
                                            }
                                        }
                                        APARTMENT -> {
                                            binding.btnSubmitProperty.setOnClickListener {
                                                addAllProperties(APARTMENT, uri.toString())
                                            }
                                        }
                                        LAND -> {
                                            binding.btnSubmitProperty.setOnClickListener {
                                                addAllProperties(LAND, uri.toString())
                                            }
                                        }
                                        CAR -> {
                                            binding.btnSubmitProperty.setOnClickListener {
                                                addAllProperties(CAR, uri.toString())
                                            }
                                        }
                                        BIKE -> {
                                            binding.btnSubmitProperty.setOnClickListener {
                                                addAllProperties(BIKE, uri.toString())
                                            }
                                        }
                                        RESTAURANT -> {
                                            binding.btnSubmitProperty.setOnClickListener {
                                                addAllProperties(RESTAURANT, uri.toString())
                                            }
                                        }
                                    }
                                }
                            }
                    } else {
                        // No image selected, handle accordingly
                    }

                }

                override fun onNothingSelected(p0: AdapterView<*>?) {}
            }
        }

    }

    private fun addVarieties(url: String) {

        firebaseViewModel.addVarieties(
            hashMapOf(
                "name" to binding.edtPropertyName.text.toString(),
                "type" to VARIETIES,
                "url" to url,
                "price" to binding.edtAskingPrice.text.toString(),
                "currentBid" to binding.edtAskingPrice.text.toString(),
                "description" to binding.edtPropertyDescription.text.toString()
            )
        )

        firebaseViewModel.addVarieties.observe(viewLifecycleOwner, Observer { resource->
            when(resource){
                is Resource.Success ->{
                    requireActivity().showToast("Submitted Successfully!")
                    binding.pbAddProperty.hide()
                }
                is Resource.Loading ->{
                    binding.pbAddProperty.show()
                }
                is Resource.Error ->{
                    requireActivity().showToast("Something went wrong!")
                    binding.pbAddProperty.hide()
                }
            }
        })
    }

    private fun addAllProperties(type: String, url: String) {
        firebaseViewModel.addProperty(
            hashMapOf(
                "name" to binding.edtPropertyName.text.toString(),
                "type" to type,
                "url" to url,
                "price" to binding.edtAskingPrice.text.toString(),
                "currentBid" to binding.edtAskingPrice.text.toString(),
                "description" to binding.edtPropertyDescription.text.toString()
            )
        )
        firebaseViewModel.addProperty.observe(viewLifecycleOwner, Observer { resource->
            when(resource){
                is Resource.Success ->{
                    requireActivity().showToast("Submitted Successfully!")
                    binding.pbAddProperty.hide()
                }
                is Resource.Loading ->{
                    binding.pbAddProperty.show()
                }
                is Resource.Error ->{
                    requireActivity().showToast("Something went wrong!")
                    binding.pbAddProperty.hide()
                }
            }
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK) {
            selectedImageUri = data?.data
            binding.imgProduct.setImageURI(selectedImageUri)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}