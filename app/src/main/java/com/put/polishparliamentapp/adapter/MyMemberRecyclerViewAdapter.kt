package com.put.polishparliamentapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.put.polishparliamentapp.MemberFragmentDirections
import com.put.polishparliamentapp.databinding.FragmentMemberBinding
import com.put.polishparliamentapp.model.Member

class MyMemberRecyclerViewAdapter(
    private val values: List<Member>
) : RecyclerView.Adapter<MyMemberRecyclerViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        return ViewHolder(
            FragmentMemberBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = values[position]
        holder.photoView.load(item.photo)
        holder.nameView.text = "${item.firstName} ${item.lastName}"
        holder.itemView.setOnClickListener {
            val action = MemberFragmentDirections.actionMemberFragmentToMemberDetailFragment(item.term, item.id)
            it.findNavController().navigate(action)
        }
    }

    override fun getItemCount(): Int = values.size

    inner class ViewHolder(binding: FragmentMemberBinding) : RecyclerView.ViewHolder(binding.root) {
        val photoView: ImageView = binding.photo
        val nameView: TextView = binding.name

        override fun toString(): String {
            return super.toString() + " '" + nameView.text + "'"
        }
    }

}