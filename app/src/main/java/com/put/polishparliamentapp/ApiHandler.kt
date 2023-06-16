package com.put.polishparliamentapp

import android.content.ContentValues
import android.content.Context
import android.util.Log
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import org.json.JSONArray
import org.json.JSONObject

class ApiHandler(private val context: Context) {
    private var mRequestQueue: RequestQueue? = null
    private var jsonObjectRequest : JsonObjectRequest? = null
    private var jsonArrayRequest: JsonArrayRequest? = null

    public fun makeJsonObjectRequest(url: String, callback: (JSONObject) -> Unit) {

        mRequestQueue = Volley.newRequestQueue(context)

        jsonObjectRequest = JsonObjectRequest (
            Request.Method.GET, url, null,
            { response ->
                    callback(response)
            }
        ) { error -> Log.i(ContentValues.TAG, "Error :$error") }
        mRequestQueue!!.add(jsonObjectRequest)
    }

    public fun makeJsonArrayRequest(url: String, callback: (JSONArray) -> Unit) {

        mRequestQueue = Volley.newRequestQueue(context)

        jsonArrayRequest = JsonArrayRequest (
            Request.Method.GET, url, null,
            { response ->
                callback(response)
            }
        ) { error -> Log.i(ContentValues.TAG, "Error :$error") }
        mRequestQueue!!.add(jsonArrayRequest)
    }
}