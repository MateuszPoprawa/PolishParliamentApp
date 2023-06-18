package com.put.polishparliamentapp

import android.content.ContentValues
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.put.polishparliamentapp.adapter.MyCommitteesRecyclerViewAdapter
import com.put.polishparliamentapp.model.Committee
import com.put.polishparliamentapp.model.DbHandler


class CommitteesFragment : Fragment() {

    private var columnCount = 1
    private val args: CommitteesFragmentArgs by navArgs()
    private lateinit var term: String
    private lateinit var  db: DbHandler
    private lateinit var list: MutableList<Committee>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        term = args.term
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_item_committees, container, false)

        db = DbHandler(requireContext(), DbHandler.DATABASE_NAME, null, 1)

        val api = ApiHandler(requireContext())
        val url = "https://api.sejm.gov.pl/sejm/term$term/committees"
        api.makeJsonArrayRequest(url) { response ->
            for (i in 0 until response.length()) {
                val values = ContentValues()
                val committee = response.getJSONObject(i)
                val id = committee.getString("code")
                val name = committee.getString("name")
                val phone = committee.getString("phone")
                val scope = committee.optString("scope") ?: ""
                with(values) {
                    put("id", id)
                    put("name", name)
                    put("phone", phone)
                    put("scope", scope)
                    put("term", term)
                }
                if(db.insert(DbHandler.committees_table, values)) {
                    list.add(Committee(id, name, phone, scope, term))
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
                addItemDecoration(
                    DividerItemDecoration(
                        context, DividerItemDecoration.VERTICAL
                    )
                )
                list = db.selectCommittees(term)
                adapter = MyCommitteesRecyclerViewAdapter(list)
            }
        }
        return view
    }

    override fun onDestroy() {
        super.onDestroy()
        if (::db.isInitialized) db.close()
    }

    companion object {

        @JvmStatic
        fun newInstance() = CommitteesFragment()
    }
}