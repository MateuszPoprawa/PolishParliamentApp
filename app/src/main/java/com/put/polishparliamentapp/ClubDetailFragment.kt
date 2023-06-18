package com.put.polishparliamentapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import coil.load
import com.put.polishparliamentapp.model.DbHandler

class ClubDetailFragment : Fragment() {

    private lateinit var id: String
    private lateinit var term: String
    private val args: ClubDetailFragmentArgs by navArgs()
    private lateinit var  db: DbHandler

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        id = args.id
        term = args.term
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_club_detail, container, false)

        db = DbHandler(requireContext(), DbHandler.DATABASE_NAME, null, 1)
        val club = db.selectClub(id, term)

        view.findViewById<ImageView>(R.id.logo).load(club.image)
        view.findViewById<TextView>(R.id.name).text = club.name
        view.findViewById<TextView>(R.id.membersCount).text = String.format(getString(R.string.members_count), club.membersCount.toString())
        view.findViewById<Button>(R.id.button).setOnClickListener {
            val action = ClubDetailFragmentDirections.actionClubDetailFragmentToMemberFragment(term, club.id, null)
            view.findNavController().navigate(action)
        }
        return view
    }

    override fun onDestroy() {
        super.onDestroy()
        if (::db.isInitialized) db.close()
    }

    companion object {

        @JvmStatic
        fun newInstance() = ClubDetailFragment()
    }
}