package com.put.polishparliamentapp

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
import com.put.polishparliamentapp.adapter.MyMemberRecyclerViewAdapter
import com.put.polishparliamentapp.model.DbHandler
import com.put.polishparliamentapp.model.Member

class MemberFragment : Fragment() {

    private var columnCount = 1
    private var club: String? = null
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
                adapter = if (club != null) {
                    MyMemberRecyclerViewAdapter(db.selectMembers(club!!, term))
                } else {
                    val list: MutableList<Member> = mutableListOf()
                    args.members?.forEach {
                        list.add(db.selectMember(it, term))
                    }
                    MyMemberRecyclerViewAdapter(list)
                }
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
        fun newInstance() = MemberFragment()

    }
}