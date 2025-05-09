package com.uv.services

import android.os.Bundle
import android.widget.ListView
import android.widget.SimpleAdapter
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.android.volley.Request
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import org.json.JSONObject

class MainActivity : AppCompatActivity() {

    private lateinit var listView: ListView
    private val userList = ArrayList<Map<String, String>>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        listView = findViewById(R.id.listViewUsers)
        fetchUsers()
    }

    private fun fetchUsers() {
        val url = "https://jsonplaceholder.typicode.com/users"
        val queue = Volley.newRequestQueue(this)

        val req = JsonArrayRequest(
            Request.Method.GET, url, null,
            { response ->
                for (i in 0 until response.length()) {
                    val user: JSONObject = response.getJSONObject(i)
                    val name = user.getString("name")
                    val email = user.getString("email")
                    val city = user.getJSONObject("address").getString("city")

                    val map = HashMap<String, String>()
                    map["title"] = "$name - $city"
                    map["subtitle"] = email

                    userList.add(map)
                }

                val adapter = SimpleAdapter(
                    this,
                    userList,
                    android.R.layout.simple_list_item_2,
                    arrayOf("title", "subtitle"),
                    intArrayOf(android.R.id.text1, android.R.id.text2)
                )

                listView.adapter = adapter
            },
            { error ->
                error.printStackTrace()
                Toast.makeText(this, "Error at fetch users", Toast.LENGTH_SHORT).show()
            }
        )

        queue.add(req)
    }
}
