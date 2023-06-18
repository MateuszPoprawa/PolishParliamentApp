package com.put.polishparliamentapp.adapter

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.navigation.findNavController
import com.put.polishparliamentapp.VideoFragmentDirections

import com.put.polishparliamentapp.databinding.FragmentVideoBinding
import com.put.polishparliamentapp.model.Video


class MyVideoRecyclerViewAdapter(
    private val values: List<Video>
) : RecyclerView.Adapter<MyVideoRecyclerViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        return ViewHolder(
            FragmentVideoBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = values[position]
        holder.titleView.text = item.title
        holder.itemView.setOnClickListener {
            val action = VideoFragmentDirections.actionVideoFragmentToVideoPageFragment(item.title, item.link, item.room)
            it.findNavController().navigate(action)
        }
    }

    override fun getItemCount(): Int = values.size

    inner class ViewHolder(binding: FragmentVideoBinding) : RecyclerView.ViewHolder(binding.root) {
        val titleView: TextView = binding.title

        override fun toString(): String {
            return super.toString() + " '" + titleView.text + "'"
        }
    }

}