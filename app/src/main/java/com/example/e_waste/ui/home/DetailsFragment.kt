package com.example.e_waste.ui.home

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.e_waste.R
import com.example.e_waste.databinding.FragmentDetailsBinding
import com.example.e_waste.viewmodel.FirebaseViewModel


class DetailsFragment : Fragment() {

    private var _binding: FragmentDetailsBinding? = null
    private val binding get() = _binding!!

    private val args by navArgs<DetailsFragmentArgs>()

    private lateinit var firebaseViewModel: FirebaseViewModel

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

        val property = args.currentProperty

        binding.apply {
            txtPropertyPrice.text = "${property.price}/= Taka"
            txtPropertyName.text = property.name
            txtPropertyDetails.text = property.description
            Glide.with(requireContext()).load(property.url).into(imgProperty)

//            btnAddToCart.setOnClickListener {
//                addToCart(property)
//            }
        }
    }

//    private fun addToCart(property: Property) {
//        firebaseViewModel.addCart(
//            hashMapOf(
//                "name" to property.name.toString(),
//                "url" to property.url.toString(),
//                "price" to property.price.toString(),
//                "description" to property.description.toString(),
//                "rating" to property.rating.toString()
//            )
//        )
//
//        firebaseViewModel.addCart.observe(viewLifecycleOwner, Observer { resource->
//            when(resource){
//                is Resource.Loading ->{
//                    binding.pbDetails.show()
//                }
//                is Resource.Success->{
//                    requireActivity().showToast(resource.data.toString())
//                    binding.pbDetails.hide()
//                }
//                is Resource.Error ->{
//                    requireActivity().showToast(resource.message.toString())
//                }
//            }
//        })
//    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}