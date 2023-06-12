package com.example.e_waste.ui.fav

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.e_waste.R
import com.example.e_waste.databinding.FragmentFavBinding
import com.example.e_waste.model.Property
import com.example.e_waste.utils.ExtensionFunctions.hide
import com.example.e_waste.utils.ExtensionFunctions.show
import com.example.e_waste.utils.ExtensionFunctions.showToast
import com.example.e_waste.utils.Resource
import com.example.e_waste.viewmodel.FirebaseViewModel


class FavFragment : Fragment() {

    private var _binding: FragmentFavBinding? = null
    private val binding get() = _binding!!

    private lateinit var firebaseViewModel: FirebaseViewModel

    private val wishlistAdapter by lazy { FavAdapter() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_fav, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentFavBinding.bind(view)

        firebaseViewModel = ViewModelProvider(requireActivity())[FirebaseViewModel::class.java]

        setUpFavAdapter()
        retrieveAndSetFav()

        wishlistAdapter.setOnClickListener(object : FavAdapter.OnItemClickListener{
            override fun onPropertyClick(property: Property) {
                val action = FavFragmentDirections.actionFavFragmentToDetailsFragment(property)
                findNavController().navigate(action)
            }

            override fun onFavIconClick(property: Property) {
                firebaseViewModel.deleteFav(property)

                firebaseViewModel.deleteFav.observe(viewLifecycleOwner, Observer { resource->
                    when(resource){
                        is Resource.Loading ->{
                            binding.pbFav.show()
                        }
                        is Resource.Success ->{
                            requireActivity().showToast(getString(R.string.removed_from_wishlist))
                            binding.pbFav.hide()
                        }
                        is Resource.Error ->{
                            requireActivity().showToast(resource.message.toString())
                            binding.pbFav.hide()
                        }
                    }
                })
            }
        })
    }

    private fun retrieveAndSetFav() {
        firebaseViewModel.getFav.observe(viewLifecycleOwner, Observer { resource->
            when(resource){
                is Resource.Loading ->{
                    binding.pbFav.show()
                }
                is Resource.Success ->{
                    if (resource.data?.isEmpty() == true){
                        binding.txtNoItem.show()
                        binding.rvFav.hide()
                    }else{
                        binding.txtNoItem.hide()
                        binding.rvFav.show()
                        wishlistAdapter.differCallBack.submitList(resource.data)
                    }
                    binding.pbFav.hide()
                }
                is Resource.Error ->{
                    requireActivity().showToast(resource.message.toString())
                    binding.pbFav.hide()
                }
            }
        })
    }

    private fun setUpFavAdapter() = binding.rvFav.apply {
        adapter = wishlistAdapter
        layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}