package com.put.polishparliamentapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DividerItemDecoration
import com.put.polishparliamentapp.adapter.MyVideoRecyclerViewAdapter
import com.put.polishparliamentapp.model.Video

private const val term = "9"

class VideoFragment : Fragment() {

    private var columnCount = 1
    private val list: MutableList<Video> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_video_list, container, false)

        list.clear()

        val api = ApiHandler(requireContext())
        val url = "https://api.sejm.gov.pl/sejm/term$term/videos/today"
        api.makeJsonArrayRequest(url) { response ->
            for (i in 0 until response.length()) {
                val video = response.getJSONObject(i)
                val title = video.getString("title")
                val link = video.getString("videoLink")
                val room = video.getString("room")
                list.add(Video(title, link, room))
                with(view as RecyclerView) {
                    adapter?.notifyItemInserted(list.lastIndex)
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
                adapter = MyVideoRecyclerViewAdapter(list)
            }
        }
        return view
    }

    companion object {

        @JvmStatic
        fun newInstance() = VideoFragment()
    }
}