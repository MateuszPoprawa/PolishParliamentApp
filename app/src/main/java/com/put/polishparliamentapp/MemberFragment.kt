package com.put.polishparliamentapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.DividerItemDecoration
import com.put.polishparliamentapp.adapter.MyMemberRecyclerViewAdapter
import com.put.polishparliamentapp.model.DbHandler
import com.put.polishparliamentapp.placeholder.PlaceholderContent

class MemberFragment : Fragment() {

    private var columnCount = 1
    private lateinit var club: String
    private lateinit var term: String
    private lateinit var  db: DbHandler
    private val args: MemberFragmentArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        club = args.club
        term = args.term
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_member_list, container, false)

        db = DbHandler(requireContext(), DbHandler.DATABASE_NAME, null, 1)

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
                adapter = MyMemberRecyclerViewAdapter(db.selectMembers(club, term))
            }
        }
        return view
    }

    companion object {

        @JvmStatic
        fun newInstance() = MemberFragment()

    }
}