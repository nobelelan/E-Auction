package com.example.e_waste.ui.home.lostfound

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.e_waste.R
import com.example.e_waste.databinding.FragmentPostBinding
import com.example.e_waste.model.FoundProperty
import com.example.e_waste.model.User
import com.example.e_waste.utils.ExtensionFunctions.disable
import com.example.e_waste.utils.ExtensionFunctions.enable
import com.example.e_waste.utils.ExtensionFunctions.hide
import com.example.e_waste.utils.ExtensionFunctions.show
import com.example.e_waste.utils.ExtensionFunctions.showToast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage


class PostFragment : Fragment() {

    private var _binding: FragmentPostBinding? = null
    private val binding get() = _binding!!

    private lateinit var auth: FirebaseAuth

    private val PICK_IMAGE_REQUEST = 1
    private var selectedImageUri: Uri? = null
    private lateinit var storageReference: StorageReference

    private var foundItemCollectionRef = Firebase.firestore.collection("foundItems")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_post, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentPostBinding.bind(view)

        auth = Firebase.auth
        // Initialize Firebase Storage reference
        storageReference = FirebaseStorage.getInstance().reference

        binding.imgFoundProperty.setOnClickListener {
            openImagePicker()
        }

        binding.btnPost.setOnClickListener {
            binding.btnPost.disable()
            binding.pbPost.show()
            uploadImage()
        }
    }

    private fun openImagePicker() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, PICK_IMAGE_REQUEST)
    }

    private fun uploadImage() {
        if (selectedImageUri != null) {
            val imageName = selectedImageUri!!.lastPathSegment!! + auth.uid.toString()
            val imageRef = storageReference.child("images/$imageName")

            imageRef.putFile(selectedImageUri!!)
                .addOnSuccessListener {
                    requireActivity().showToast("Image uploaded successfully!")
                    imageRef.downloadUrl.addOnSuccessListener { uri ->
                        val registeredDocumentReference = Firebase.firestore.collection("registeredUsers").document(auth.currentUser?.uid!!)
                        registeredDocumentReference.get().addOnSuccessListener { userDoc->
                            val user = userDoc.toObject<User>()
                            val propertyName = binding.edtPropertyName.text.toString()
                            val foundPlace = binding.edtFoundPlace.text.toString()
                            if( propertyName.isNotEmpty() && foundPlace.isNotEmpty() && selectedImageUri != null){
                                foundItemCollectionRef.add(
                                    hashMapOf(
                                        "name" to propertyName,
                                        "imageUri" to uri.toString(),
                                        "place" to foundPlace,
                                        "postedBy" to user?.username,
                                        "contact" to user?.contact
                                    )
                                ).addOnSuccessListener {
                                    binding.pbPost.hide()
                                    binding.btnPost.enable()
                                    requireActivity().showToast("Successfully posted found property!")
                                }.addOnFailureListener {
                                    requireActivity().showToast("Failed, try again!")
                                    binding.pbPost.hide()
                                    binding.btnPost.enable()
                                }
                            }else{
                                requireActivity().showToast("Please fill out all the fields.")
                            }

                        }.addOnFailureListener {
                            binding.pbPost.hide()
                            binding.btnPost.enable()
                        }
                        // Store the image URI in your model or perform other actions

                    }
                }
                .addOnFailureListener { exception ->
                    // Handle any errors that occurred during image upload
                    binding.pbPost.hide()
                    binding.btnPost.enable()
                }
        } else {
            // No image selected, handle accordingly
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK) {
            selectedImageUri = data?.data
            binding.imgFoundProperty.setImageURI(selectedImageUri)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}