package com.put.polishparliamentapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.put.polishparliamentapp.CommitteesFragmentDirections
import com.put.polishparliamentapp.databinding.FragmentCommitteesBinding
import com.put.polishparliamentapp.model.Committee


class MyCommitteesRecyclerViewAdapter(
    private val values: MutableList<Committee>
) : RecyclerView.Adapter<MyCommitteesRecyclerViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        return ViewHolder(
            FragmentCommitteesBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = values[position]
        holder.nameView.text = item.name
        holder.itemView.setOnClickListener {
            val action = CommitteesFragmentDirections.actionCommitteesFragmentToCommitteeDetailFragment(item.id, item.term)
            it.findNavController().navigate(action)
        }
    }

    override fun getItemCount(): Int = values.size

    inner class ViewHolder(binding: FragmentCommitteesBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val nameView: TextView = binding.name

        override fun toString(): String {
            return super.toString() + " '" + nameView.text + "'"
        }
    }

}