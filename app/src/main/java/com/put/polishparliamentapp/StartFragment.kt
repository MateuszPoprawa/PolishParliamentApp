package com.put.polishparliamentapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController

const val DEFAULT_TERM = "9"

class StartFragment() : Fragment(), AdapterView.OnItemSelectedListener {

    private var spinner: Spinner? = null
    private var termList = arrayOf ("9", "8", "7")
    private var term = DEFAULT_TERM

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view =  inflater.inflate(R.layout.fragment_start, container, false)

        val arrayAdapter = ArrayAdapter(requireActivity(), android.R.layout.simple_spinner_item, termList)
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        spinner = view.findViewById(R.id.spinner)
        spinner!!.onItemSelectedListener  = this
        spinner!!.adapter = arrayAdapter

        view.findViewById<Button>(R.id.buttonClubs).setOnClickListener {
            val action = StartFragmentDirections.actionStartFragmentToClubFragment(term)
            view.findNavController().navigate(action)
        }

        view.findViewById<Button>(R.id.buttonCommittees).setOnClickListener {
            val action = StartFragmentDirections.actionStartFragmentToCommitteesFragment(term)
            view.findNavController().navigate(action)
        }

        view.findViewById<Button>(R.id.buttonProcesses).setOnClickListener {

        }

        return view
    }

    companion object {

        @JvmStatic
        fun newInstance() = StartFragment()

    }

    override fun onItemSelected(adapterView: AdapterView<*>?, view: View?, position: Int, id: Long) {
        term = termList[position]
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {
        term = DEFAULT_TERM
    }
}