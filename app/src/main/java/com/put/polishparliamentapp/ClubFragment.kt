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
import com.put.polishparliamentapp.adapter.ClubRecyclerViewAdapter
import com.put.polishparliamentapp.model.Club
import com.put.polishparliamentapp.model.DbHandler

const val MEMBERS_COUNT = 460
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
        val clubsUrl = "https://api.sejm.gov.pl/sejm/term$term/clubs"
        api.makeJsonArrayRequest(clubsUrl) { response ->
            for (i in 0 until response.length()) {
                val values = ContentValues()
                val club = response.getJSONObject(i)
                val id = club.getString("id")
                val name = club.getString("name")
                val membersCount = Integer.parseInt(club.getString("membersCount"))
                val image = "$clubsUrl/$id/logo"
                with(values) {
                    put("id", id)
                    put("name", name)
                    put("membersCount", membersCount)
                    put("image", image)
                    put("term", term)
                }
                if (db.insert(DbHandler.clubs_table, values)) {
                    list.add(Club(id, name, membersCount, image, term))
                    with(view as RecyclerView) {
                        adapter?.notifyItemInserted(list.lastIndex)
                    }
                }
            }
        }

        if (db.membersTotalCount(term) < MEMBERS_COUNT) {
            val mpUrl = "https://api.sejm.gov.pl/sejm/term$term/MP"
            api.makeJsonArrayRequest(mpUrl) { response ->
                for (i in 0 until response.length()) {
                    val values = ContentValues()
                    val member = response.getJSONObject(i)
                    val id = member.getString("id")
                    val firstName = member.getString("firstName")
                    val lastName = member.getString("lastName")
                    val birthDate = member.getString("birthDate")
                    val club  = member.optString("club") ?: "-"
                    val profession = member.getString("profession")
                    val email = member.optString("email") ?: "-"
                    val districtName = member.getString("districtName")
                    val photo = "$mpUrl/$id/photo-mini"
                    val term = term
                    with(values) {
                        put("id", id)
                        put("firstName", firstName)
                        put("lastName", lastName)
                        put("birthDate", birthDate)
                        put("club", club)
                        put("profession", profession)
                        put("email", email)
                        put("districtName", districtName)
                        put("photo", photo)
                        put("term", term)
                    }
                    db.insert(DbHandler.members_table, values)
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