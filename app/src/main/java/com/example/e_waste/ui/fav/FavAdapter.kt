package com.example.e_waste.ui.fav

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.e_waste.R
import com.example.e_waste.databinding.RvHomeAllBinding
import com.example.e_waste.model.Property

class FavAdapter: RecyclerView.Adapter<FavAdapter.FavViewHolder>() {

    inner class FavViewHolder(val binding: RvHomeAllBinding): RecyclerView.ViewHolder(binding.root)

    private val diffUtilCallBack = object : DiffUtil.ItemCallback<Property>(){
        override fun areItemsTheSame(oldItem: Property, newItem: Property): Boolean {
            return oldItem.url == newItem.url
        }

        override fun areContentsTheSame(oldItem: Property, newItem: Property): Boolean {
            return oldItem == newItem
        }
    }

    val differCallBack = AsyncListDiffer(this, diffUtilCallBack)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavViewHolder {
        return FavViewHolder(RvHomeAllBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: FavViewHolder, position: Int) {
        val wishlist = differCallBack.currentList[position]

        holder.binding.apply {
            txtPropertyName.text = wishlist.name
            txtCurrentBid.text = "Current Bid: ${wishlist.price}/= Taka"

            Glide.with(holder.itemView.context)
                .load(wishlist.url)
                .placeholder(R.drawable.img_e_auction)
                .into(imgProperty)

            imgAddWishlist.setOnClickListener {
                onItemClickListener?.onFavIconClick(wishlist)
            }
            root.setOnClickListener {
                onItemClickListener?.onPropertyClick(wishlist)
            }
        }

    }

    override fun getItemCount(): Int {
        return differCallBack.currentList.size
    }

    private var onItemClickListener: OnItemClickListener? = null

    interface OnItemClickListener{
        fun onPropertyClick(property: Property)
        fun onFavIconClick(property: Property)
    }

    fun setOnClickListener(listener: OnItemClickListener){
        onItemClickListener = listener
    }
}