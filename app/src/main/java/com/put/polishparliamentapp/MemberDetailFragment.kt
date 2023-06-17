package com.put.polishparliamentapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.navigation.fragment.navArgs
import coil.load
import com.put.polishparliamentapp.model.DbHandler


class MemberDetailFragment : Fragment() {

    private val args: MemberDetailFragmentArgs by navArgs()
    private lateinit var id: String
    private lateinit var term: String
    private lateinit var db: DbHandler

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        id = args.id
        term = args.term
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_member_detail, container, false)

        db = DbHandler(requireContext(), DbHandler.DATABASE_NAME, null, 1)
        val member = db.selectMember(id, term)

        view.findViewById<TextView>(R.id.name).text = "${member.firstName} ${member.lastName}"
        view.findViewById<TextView>(R.id.birthDate).text = String.format(getString(R.string.birthDate), member.birthDate)
        view.findViewById<TextView>(R.id.club).text = String.format(getString(R.string.club), member.club)
        view.findViewById<TextView>(R.id.profession).text = String.format(getString(R.string.profession), member.profession)
        view.findViewById<TextView>(R.id.email).text = String.format(getString(R.string.email), member.email)
        view.findViewById<TextView>(R.id.districtName).text = String.format(getString(R.string.districtName), member.districtName)
        view.findViewById<ImageView>(R.id.photo).load(member.photo.replace("photo-mini", "photo"))

        return view
    }

    companion object {

        @JvmStatic
        fun newInstance() = MemberDetailFragment()
    }
}