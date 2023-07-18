package com.example.e_waste.ui.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.denzcoskun.imageslider.constants.AnimationTypes
import com.denzcoskun.imageslider.constants.ScaleTypes
import com.denzcoskun.imageslider.interfaces.ItemClickListener
import com.denzcoskun.imageslider.models.SlideModel
import com.example.e_commerce.ui.fragments.Property.adapter.VarietiesAdapter
import com.example.e_waste.R
import com.example.e_waste.databinding.FragmentHomeBinding
import com.example.e_waste.model.Property
import com.example.e_waste.ui.home.adapter.PropertyAdapter
import com.example.e_waste.utils.Constants.APARTMENT
import com.example.e_waste.utils.Constants.BIKE
import com.example.e_waste.utils.Constants.CAR
import com.example.e_waste.utils.Constants.LAND
import com.example.e_waste.utils.Constants.RESTAURANT
import com.example.e_waste.utils.ExtensionFunctions.hide
import com.example.e_waste.utils.ExtensionFunctions.show
import com.example.e_waste.utils.ExtensionFunctions.showToast
import com.example.e_waste.utils.Resource
import com.example.e_waste.viewmodel.FirebaseViewModel


class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var firebaseViewModel: FirebaseViewModel

    private val propertyAdapter by lazy { PropertyAdapter() }
    private val varietiesAdapter by lazy { VarietiesAdapter() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentHomeBinding.bind(view)

        firebaseViewModel = ViewModelProvider(requireActivity())[FirebaseViewModel::class.java]

        setUpImageSlider()

        setUpPropertyRecyclerView()
        setUpVarietiesRecyclerView()

        retrieveAndSetProperties()
        retrieveAndSetVarieties()

        binding.fabAddProperty.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_addPropertyFragment)
        }

        binding.imageSlider.setItemClickListener(object: ItemClickListener{
            override fun doubleClick(position: Int) {
                TODO("Not yet implemented")
            }

            override fun onItemSelected(position: Int) {
                when(position){
                    1, 2 -> {
                        // Lost Something
                        findNavController().navigate(R.id.action_homeFragment_to_findFragment)
                    }
                    3, 4 -> {
                        // Found Something
                        findNavController().navigate(R.id.action_homeFragment_to_postFragment)
                    }
                }
            }
        })

        propertyAdapter.setOnClickListener(object : PropertyAdapter.OnItemClickListener{
            override fun onPropertyClick(property: Property) {
                navigateToDetails(property)
            }

            override fun onFavIconClick(property: Property) {
                addPropertyToFav(property)
            }

            override fun onLongClick(property: Property) {
                // Implement this only if you separate property for each user
            }
        })

        varietiesAdapter.setOnClickListener(object : VarietiesAdapter.OnItemClickListener{
            override fun onPropertyClick(property: Property) {
                navigateToDetails(property)
            }

            override fun onFavIconClick(property: Property) {
                addPropertyToFav(property)
            }

            override fun onLongClick(property: Property) {
                // Implement this only if you separate property for each user
            }
        })
    }

    private fun navigateToDetails(property: Property) {
        val action = HomeFragmentDirections.actionHomeFragmentToDetailsFragment2(property)
        findNavController().navigate(action)
    }

    private fun addPropertyToFav(property: Property){
        firebaseViewModel.addFav(
            hashMapOf(
                "name" to property.name.toString(),
                "url" to property.url.toString(),
                "price" to property.price.toString(),
                "currentBid" to property.currentBid.toString(),
                "topBidder" to property.topBidder.toString(),
                "totalBids" to property.totalBids.toString(),
                "description" to property.description.toString()
            )
        )
        firebaseViewModel.addFav.observe(viewLifecycleOwner, Observer { resource->
            when(resource){
                is Resource.Loading ->{
                    binding.pbHome.show()
                }
                is Resource.Success ->{
                    binding.pbHome.hide()
                    requireActivity().showToast(getString(R.string.added_to_fav))
                }
                is Resource.Error ->{
                    binding.pbHome.hide()
                    requireActivity().showToast(resource.message.toString())
                }
            }
        })
    }

    private fun setUpVarietiesRecyclerView() {
        binding.rvHomeOtherProperties.apply {
            adapter = varietiesAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    private fun setUpPropertyRecyclerView() {
        binding.rvHomeAll.apply {
            adapter = propertyAdapter
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        }
    }

    private fun setUpImageSlider() {
        val imageList = ArrayList<SlideModel>()
        imageList.add(SlideModel(R.drawable.e_auction))
        imageList.add(SlideModel(R.drawable.lost_something))
        imageList.add(SlideModel(R.drawable.findt_it))
        imageList.add(SlideModel(R.drawable.found_something))
        imageList.add(SlideModel(R.drawable.post_it))

        binding.imageSlider.apply {
            setImageList(imageList, ScaleTypes.CENTER_CROP)
            setSlideAnimation(AnimationTypes.DEPTH_SLIDE)
            startSliding(2000) // with new period
//            startSliding()
//            stopSliding()
        }
    }

    private fun retrieveAndSetProperties(){
        binding.chipGroupAll.setOnCheckedChangeListener { group, checkedId ->
            when(checkedId){
                R.id.chip_apartment ->{
                    firebaseViewModel.getProperty(APARTMENT)
                }
                R.id.chip_land ->{
                    firebaseViewModel.getProperty(LAND)
                }
                R.id.chip_bike ->{
                    firebaseViewModel.getProperty(BIKE)
                }
                R.id.chip_car ->{
                    firebaseViewModel.getProperty(CAR)
                }
                R.id.chip_restaurant ->{
                    firebaseViewModel.getProperty(RESTAURANT)
                }
            }
        }
        getProperties()
    }

    private fun getProperties(){
        firebaseViewModel.getProperty.observe(viewLifecycleOwner, Observer { resource->
            when(resource){
                is Resource.Loading ->{
                    binding.pbHome.show()
                }
                is Resource.Success ->{
                    if (resource.data?.isEmpty() == true){
                        binding.txtHomeShoeNoItems.show()
                        binding.rvHomeAll.hide()
                    }else{
                        binding.rvHomeAll.show()
                        binding.txtHomeShoeNoItems.hide()
                    }
                    propertyAdapter.differCallBack.submitList(resource.data)
                    binding.pbHome.hide()
                }
                is Resource.Error ->{
                    binding.pbHome.hide()
                    requireActivity().showToast(resource.message.toString())
                }
            }
        })
    }

    private fun retrieveAndSetVarieties() {
        firebaseViewModel.getVarieties.observe(viewLifecycleOwner, Observer { resource->
            when(resource){
                is Resource.Loading ->{
                    binding.pbHome.show()
                }
                is Resource.Success ->{
                    if (resource.data?.isEmpty() == true){
                        binding.txtHomeVarietiesNoItems.show()
                        binding.rvHomeOtherProperties.hide()
                    }else{
                        binding.rvHomeOtherProperties.show()
                        binding.txtHomeVarietiesNoItems.hide()
                    }
                    binding.pbHome.hide()
                    varietiesAdapter.differCallBack.submitList(resource.data)
                }
                is Resource.Error ->{
                    binding.pbHome.hide()
                    requireActivity().showToast(resource.message.toString())
                }
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}