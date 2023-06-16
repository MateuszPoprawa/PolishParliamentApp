package com.put.polishparliamentapp

import android.content.ContentValues
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.put.polishparliamentapp.adapter.ClubRecyclerViewAdapter
import com.put.polishparliamentapp.model.Club
import com.put.polishparliamentapp.model.DbHandler


class ClubFragment : Fragment() {

    private var columnCount = 1
    private val args: ClubFragmentArgs by navArgs()
    private lateinit var term: String
    private lateinit var  db: DbHandler
    private lateinit var list: MutableList<Club>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        term = args.term
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_club_list, container, false)

        db = DbHandler(requireContext(), DbHandler.DATABASE_NAME, null, 1)

        val api = ApiHandler(requireContext())
        val url = "https://api.sejm.gov.pl/sejm/term$term/clubs"
        api.makeJsonArrayRequest(url) { response ->
            for (i in 0 until response.length()) {
                val values = ContentValues()
                val club = response.getJSONObject(i)
                val id = club.get("id").toString()
                val name = club.get("name").toString()
                val membersCount = Integer.parseInt(club.get("membersCount").toString())
                val image =  url + "/" + club.get("id").toString() + "/logo"
                values.put("id", id)
                values.put("name", name)
                values.put("membersCount", membersCount)
                values.put("image", image)
                values.put("term", term)
                if (db.insert(DbHandler.clubs_table, values)) {
                    list.add(Club(id, name, membersCount, image))
                    with(view as RecyclerView) {
                        adapter?.notifyItemInserted(list.lastIndex)
                    }
                }
            }
        }

        if (view is RecyclerView) {
            with(view) {
                layoutManager = when {
                    columnCount <= 1 -> LinearLayoutManager(context)
                    else -> GridLayoutManager(context, columnCount)
                }
                list = db.selectClubs(term)
                adapter = ClubRecyclerViewAdapter(list)
            }
        }
        return view
    }

    override fun onDestroy() {
        super.onDestroy()
        db.close()
    }

    companion object {

        @JvmStatic
        fun newInstance() = ClubFragment()
    }
}