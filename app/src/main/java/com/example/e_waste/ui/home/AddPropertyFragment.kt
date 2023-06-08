package com.example.e_waste.ui.home

import android.os.Bundle
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
import com.example.e_waste.utils.Constants
import com.example.e_waste.utils.Constants.APARTMENT
import com.example.e_waste.utils.Constants.BIKE
import com.example.e_waste.utils.Constants.CAR
import com.example.e_waste.utils.Constants.LAND
import com.example.e_waste.utils.Constants.RESTAURANT
import com.example.e_waste.utils.Constants.VARIETIES
import com.example.e_waste.utils.ExtensionFunctions.hide
import com.example.e_waste.utils.ExtensionFunctions.show
import com.example.e_waste.utils.ExtensionFunctions.showToast
import com.example.e_waste.utils.Resource
import com.example.e_waste.viewmodel.FirebaseViewModel


class AddPropertyFragment : Fragment() {

    private var _binding: FragmentAddPropertyBinding? = null
    private val binding get() = _binding!!

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

        firebaseViewModel = ViewModelProvider(requireActivity())[FirebaseViewModel::class.java]

        setUpSpinner()
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
                    when(selectedProductType){
                        VARIETIES -> {
                            binding.btnSubmitProperty.setOnClickListener {
                                addVarieties()
                            }
                        }
                        APARTMENT -> {
                            binding.btnSubmitProperty.setOnClickListener {
                                addAllProperties(APARTMENT)
                            }
                        }
                        LAND -> {
                            binding.btnSubmitProperty.setOnClickListener {
                                addAllProperties(LAND)
                            }
                        }
                        CAR -> {
                            binding.btnSubmitProperty.setOnClickListener {
                                addAllProperties(CAR)
                            }
                        }
                        BIKE -> {
                            binding.btnSubmitProperty.setOnClickListener {
                                addAllProperties(BIKE)
                            }
                        }
                        RESTAURANT -> {
                            binding.btnSubmitProperty.setOnClickListener {
                                addAllProperties(RESTAURANT)
                            }
                        }
                    }
                }

                override fun onNothingSelected(p0: AdapterView<*>?) {}
            }
        }
    }

    private fun addVarieties() {
        firebaseViewModel.addVarieties(
            hashMapOf(
                "name" to binding.edtPropertyName.text.toString(),
                "type" to VARIETIES,
                "url" to binding.edtPropertyUrl.text.toString(),
                "price" to binding.edtAskingPrice.text.toString(),
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

    private fun addAllProperties(type: String) {
        firebaseViewModel.addProperty(
            hashMapOf(
                "name" to binding.edtPropertyName.text.toString(),
                "type" to type,
                "url" to binding.edtPropertyUrl.text.toString(),
                "price" to binding.edtAskingPrice.text.toString(),
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}