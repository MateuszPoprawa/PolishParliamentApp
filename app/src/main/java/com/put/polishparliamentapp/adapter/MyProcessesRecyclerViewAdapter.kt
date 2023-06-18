package com.put.polishparliamentapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.put.polishparliamentapp.databinding.FragmentProcessesBinding
import com.put.polishparliamentapp.model.Processes

class MyProcessesRecyclerViewAdapter(
    private val values: MutableList<Processes>
) : RecyclerView.Adapter<MyProcessesRecyclerViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        return ViewHolder(
            FragmentProcessesBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = values[position]
        holder.titleView.text = item.title
        holder.dateView.text = item.documentDate

    }

    override fun getItemCount(): Int = values.size

    inner class ViewHolder(binding: FragmentProcessesBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val titleView: TextView = binding.title
        val dateView: TextView = binding.date

        override fun toString(): String {
            return super.toString() + " '" + titleView.text + "'"
        }
    }

}