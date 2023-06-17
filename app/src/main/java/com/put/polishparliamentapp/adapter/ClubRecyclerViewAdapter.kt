package com.put.polishparliamentapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.navigation.Navigation.findNavController
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.put.polishparliamentapp.ClubFragment
import com.put.polishparliamentapp.ClubFragmentDirections
import com.put.polishparliamentapp.databinding.FragmentClubBinding
import com.put.polishparliamentapp.model.Club


class ClubRecyclerViewAdapter(
    private val values: MutableList<Club>
) : RecyclerView.Adapter<ClubRecyclerViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        return ViewHolder(
            FragmentClubBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = values[position]
        holder.idView.text = item.id
        holder.logoView.load(item.image)
        holder.membersCount.text = item.membersCount.toString()
        holder.itemView.setOnClickListener{
            val action = ClubFragmentDirections.actionClubFragmentToClubDetailFragment(item.id, item.term)
            it.findNavController().navigate(action)
        }
    }

    override fun getItemCount(): Int = values.size

    inner class ViewHolder(binding: FragmentClubBinding) : RecyclerView.ViewHolder(binding.root) {
        val logoView: ImageView = binding.logo
        val idView: TextView = binding.id
        val membersCount: TextView = binding.membersCount
        override fun toString(): String {
            return super.toString() + " '" + idView.text + "'"
        }
    }

}