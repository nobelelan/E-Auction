package com.example.e_waste.ui.profile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.e_waste.R
import com.example.e_waste.databinding.FragmentProfileBinding
import com.example.e_waste.model.User
import com.example.e_waste.utils.ExtensionFunctions.enable
import com.example.e_waste.utils.ExtensionFunctions.hide
import com.example.e_waste.utils.ExtensionFunctions.showToast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase


class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentProfileBinding.bind(view)

        auth = Firebase.auth

        val registeredDocumentReference = Firebase.firestore.collection("registeredUsers").document(auth.currentUser?.uid!!)
        registeredDocumentReference.get().addOnSuccessListener { userDoc->
            val user = userDoc.toObject<User>()

            binding.apply {
                txtUsername.text = user?.username
                txtContact.text = user?.contact
                txtEmail.text = user?.email
            }

        }

        binding.txtSignOut.setOnClickListener {
            auth.signOut()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}