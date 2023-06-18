package com.put.polishparliamentapp

import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.VideoView
import androidx.navigation.fragment.navArgs

class VideoPageFragment : Fragment() {

    private val args: VideoPageFragmentArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view =  inflater.inflate(R.layout.fragment_video_page, container, false)

        view.findViewById<TextView>(R.id.title).text = args.title
        view.findViewById<TextView>(R.id.room).text = args.room
        val uri = Uri.parse(args.link)
        view.findViewById<VideoView>(R.id.videoView).setVideoURI(uri)

        return view
    }

    companion object {

        @JvmStatic
        fun newInstance() = VideoPageFragment()
    }
}