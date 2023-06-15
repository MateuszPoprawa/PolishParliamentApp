package com.put.polishparliamentapp

import android.content.ContentValues
import android.content.Context
import android.util.Log
import androidx.activity.compose.setContent
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import org.json.JSONObject

class ApiHandler(private val context: Context) {
    private var mRequestQueue: RequestQueue? = null
    private var jsonObjectRequest : JsonObjectRequest? = null

    public fun makeRequest(url: String, callback: (JSONObject) -> Unit) {
        // RequestQueue initialized
        mRequestQueue = Volley.newRequestQueue(context)

        // String Request initialized
        jsonObjectRequest = JsonObjectRequest(
            Request.Method.GET, url, null,
            { response ->
                    callback(response)
            }
        ) { error -> Log.i(ContentValues.TAG, "Error :$error") }
        mRequestQueue!!.add(jsonObjectRequest)
    }
}