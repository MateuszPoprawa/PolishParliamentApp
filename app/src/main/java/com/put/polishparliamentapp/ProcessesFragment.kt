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
import com.put.polishparliamentapp.adapter.MyProcessesRecyclerViewAdapter
import com.put.polishparliamentapp.model.DbHandler
import com.put.polishparliamentapp.model.Processes

class ProcessesFragment : Fragment() {

    private var columnCount = 1
    private val args: ProcessesFragmentArgs by navArgs()
    private lateinit var term: String
    private lateinit var  db: DbHandler
    private lateinit var list: MutableList<Processes>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        term = args.term
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_processes_list, container, false)

        db = DbHandler(requireContext(), DbHandler.DATABASE_NAME, null, 1)

        val api = ApiHandler(requireContext())
        val url = "https://api.sejm.gov.pl/sejm/term$term/processes"
        api.makeJsonArrayRequest(url) { response ->
            for (i in response.length() - 1 downTo response.length() - 100) {
                val values = ContentValues()
                val processes = response.getJSONObject(i)
                val id = processes.getString("number")
                val title = processes.getString("title")
                val description = processes.optString("description") ?: ""
                val documentDate = processes.getString("documentDate")
                with(values) {
                    put("id", id)
                    put("title", title)
                    put("description", description)
                    put("documentDate", documentDate)
                    put("term", term)
                }

                if (db.insert(DbHandler.processes_table, values)) {
                    list.add(Processes(id, title, description, documentDate, term))
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
                list = db.selectProcesses(term)
                adapter = MyProcessesRecyclerViewAdapter(list)
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
        fun newInstance() = ProcessesFragment()
    }
}