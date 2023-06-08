package com.example.e_waste.ui.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.denzcoskun.imageslider.constants.AnimationTypes
import com.denzcoskun.imageslider.constants.ScaleTypes
import com.denzcoskun.imageslider.interfaces.ItemClickListener
import com.denzcoskun.imageslider.models.SlideModel
import com.example.e_waste.R
import com.example.e_waste.databinding.FragmentHomeBinding


class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

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

        setUpImageSlider()

        binding.imageSlider.setItemClickListener(object: ItemClickListener{
            override fun doubleClick(position: Int) {
                TODO("Not yet implemented")
            }

            override fun onItemSelected(position: Int) {
                TODO("Not yet implemented")
            }
        })
    }

    private fun setUpImageSlider() {
        val imageList = ArrayList<SlideModel>()
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}