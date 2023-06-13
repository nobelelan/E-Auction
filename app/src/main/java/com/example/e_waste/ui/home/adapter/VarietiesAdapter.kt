package com.example.e_commerce.ui.fragments.Property.adapter


import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnLongClickListener
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.e_waste.R
import com.example.e_waste.databinding.RvHomeVarietyBinding
import com.example.e_waste.model.Property

class VarietiesAdapter: RecyclerView.Adapter<VarietiesAdapter.VarietiesViewHolder>() {

    inner class VarietiesViewHolder(val binding: RvHomeVarietyBinding): RecyclerView.ViewHolder(binding.root)

    private val diffUtilCallBack = object : DiffUtil.ItemCallback<Property>(){
        override fun areItemsTheSame(oldItem: Property, newItem: Property): Boolean {
            return oldItem.url == newItem.url
        }

        override fun areContentsTheSame(oldItem: Property, newItem: Property): Boolean {
            return oldItem == newItem
        }
    }

    val differCallBack = AsyncListDiffer(this, diffUtilCallBack)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VarietiesViewHolder {
        return VarietiesViewHolder(RvHomeVarietyBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: VarietiesViewHolder, position: Int) {
        val property = differCallBack.currentList[position]

        holder.binding.apply {
            txtPropertyName.text = property.name
            txtCurrentBid.text = "Current Bid: ${property.currentBid}/= Taka"

            Glide.with(holder.itemView.context)
                .load(property.url)
                .placeholder(R.drawable.img_e_auction)
                .into(imgProperty)

            imgAddWishlist.setOnClickListener {
                onItemClickListener?.onFavIconClick(property)
            }
            root.setOnClickListener {
                onItemClickListener?.onPropertyClick(property)
            }
            root.setOnLongClickListener(object : OnLongClickListener{
                override fun onLongClick(p0: View?): Boolean {
                    onItemClickListener?.onLongClick(property)
                    return true
                }
            })
        }

    }

    override fun getItemCount(): Int {
        return differCallBack.currentList.size
    }

    private var onItemClickListener: OnItemClickListener? = null

    interface OnItemClickListener{
        fun onPropertyClick(property: Property)
        fun onFavIconClick(property: Property)
        fun onLongClick(property: Property)
    }

    fun setOnClickListener(listener: OnItemClickListener){
        onItemClickListener = listener
    }
}