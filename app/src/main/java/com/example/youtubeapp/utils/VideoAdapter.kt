package com.example.youtubeapp.utils

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.example.youtubeapp.R
import com.example.youtubeapp.databinding.VideoCardBinding
import com.example.youtubeapp.repository.VideoData


class GenericAdapter<T : Any>(
    private var itemList: List<T>,
    private val bindingInflater: (LayoutInflater, ViewGroup, Boolean) -> ViewDataBinding,
    private val onBind: (ViewDataBinding, T) -> Unit,
    private val onClickListener: ((T) -> Unit)? = null
) : RecyclerView.Adapter<GenericAdapter<T>.GenericViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GenericViewHolder {
        val binding = bindingInflater(LayoutInflater.from(parent.context), parent, false)
        return GenericViewHolder(binding)
    }

    override fun onBindViewHolder(holder: GenericViewHolder, position: Int) {
        onBind(holder.binding, itemList[position])

        onClickListener?.let { listener ->
            holder.binding.root.setOnClickListener {
                listener(itemList[position])
            }
        }
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateList(newList: List<T>) {
        itemList = newList
        notifyDataSetChanged()
    }

    inner class GenericViewHolder(val binding: ViewDataBinding) : RecyclerView.ViewHolder(binding.root)
}