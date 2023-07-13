package com.example.e_waste.ui.home.lostfound


import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.e_waste.R
import com.example.e_waste.databinding.RvFindPropertyBinding
import com.example.e_waste.model.FoundProperty

class FindAdapter: RecyclerView.Adapter<FindAdapter.FindViewHolder>() {

    inner class FindViewHolder(val binding: RvFindPropertyBinding): RecyclerView.ViewHolder(binding.root)

    private val diffUtilCallBack = object : DiffUtil.ItemCallback<FoundProperty>(){
        override fun areItemsTheSame(oldItem: FoundProperty, newItem: FoundProperty): Boolean {
            return oldItem.imageUri == newItem.imageUri
        }

        override fun areContentsTheSame(oldItem: FoundProperty, newItem: FoundProperty): Boolean {
            return oldItem == newItem
        }
    }

    val differCallBack = AsyncListDiffer(this, diffUtilCallBack)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FindViewHolder {
        return FindViewHolder(RvFindPropertyBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: FindViewHolder, position: Int) {
        val foundProperty = differCallBack.currentList[position]

        holder.binding.apply {
            txtPropertyName.text = foundProperty.name
            txtPlaceFound.text = "Found Location: ${foundProperty.place}"
            txtPostedBy.text = "Found By: ${foundProperty.postedBy}"
            txtContact.text = "Contact: ${foundProperty.contact}"

            Glide.with(holder.itemView.context)
                .load(foundProperty.imageUri)
                .placeholder(R.drawable.img_e_auction)
                .into(holder.binding.imgFoundProperty)
        }

    }

    override fun getItemCount(): Int {
        return differCallBack.currentList.size
    }

}