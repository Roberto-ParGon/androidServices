package com.uv.services

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.os.Bundle
import android.view.MotionEvent
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.android.volley.Request
import com.android.volley.toolbox.ImageRequest
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import org.json.JSONException

class MainActivity : AppCompatActivity() {

    private lateinit var textView: TextView
    private lateinit var imageViewCat: ImageView
    private val factUrl = "https://meowfacts.herokuapp.com/"
    private val imageUrl = "https://cataas.com/cat"

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        val rootView = findViewById<androidx.constraintlayout.widget.ConstraintLayout>(R.id.main)

        ViewCompat.setOnApplyWindowInsetsListener(rootView) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        textView = findViewById(R.id.text)
        imageViewCat = findViewById(R.id.imageViewCat)

        fetchCatData()

        rootView.setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_DOWN) {
                fetchCatData()
            }
            true
        }
    }

    private fun fetchCatData() {
        val requestQueue = Volley.newRequestQueue(this)

        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.GET,
            factUrl,
            null,
            { response ->
                try {
                    val dataArray = response.getJSONArray("data")
                    val fact = dataArray.getString(0)
                    textView.text = fact
                } catch (e: JSONException) {
                    e.printStackTrace()
                    textView.text = getString(R.string.error_processing_response)
                }
            },
            { error ->
                error.printStackTrace()
                textView.text = getString(R.string.request_error)
            }
        )

        val imageRequest = ImageRequest(
            imageUrl,
            { bitmap: Bitmap ->
                imageViewCat.setImageBitmap(bitmap)
            },
            0,
            0,
            ImageView.ScaleType.CENTER_CROP,
            Bitmap.Config.ARGB_8888,
            { _ ->
                Toast.makeText(this, "Error al cargar la imagen.", Toast.LENGTH_SHORT).show()
            }
        )

        requestQueue.add(jsonObjectRequest)
        requestQueue.add(imageRequest)
    }
}
