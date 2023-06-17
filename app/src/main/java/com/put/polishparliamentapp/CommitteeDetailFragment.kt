package com.put.polishparliamentapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import com.put.polishparliamentapp.model.DbHandler


class CommitteeDetailFragment : Fragment() {

    private lateinit var id: String
    private lateinit var term: String
    private val args: CommitteeDetailFragmentArgs by navArgs()
    private lateinit var  db: DbHandler
    private val list: MutableList<String> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        id = args.id
        term = args.term
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view =  inflater.inflate(R.layout.fragment_committee_detail, container, false)

        db = DbHandler(requireContext(), DbHandler.DATABASE_NAME, null, 1)

        val api = ApiHandler(requireContext())
        val url = "https://api.sejm.gov.pl/sejm/term$term/committees/$id"
        api.makeJsonObjectRequest(url) { response ->
            val array = response.getJSONArray("members")
            for (i in 0 until array.length()) {
                val member = array.getJSONObject(i)
                val id = member.getString("id")
                list.add(id)
            }
        }

        val committee = db.selectCommittee(id, term)
        view.findViewById<TextView>(R.id.name).text = committee.name
        view.findViewById<TextView>(R.id.phone).text =  String.format(getString(R.string.phone), committee.phone)
        view.findViewById<TextView>(R.id.scope).text = committee.scope
        view.findViewById<Button>(R.id.button).setOnClickListener {
            val action = CommitteeDetailFragmentDirections.actionCommitteeDetailFragmentToMemberFragment(term, null, list.toTypedArray())
            it.findNavController().navigate(action)
        }

        return view
    }

    companion object {

        @JvmStatic
        fun newInstance() = CommitteeDetailFragment()
    }
}