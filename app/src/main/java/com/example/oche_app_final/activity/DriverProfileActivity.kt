package com.example.oche_app_final.activity

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.oche_app_final.R
import com.example.oche_app_final.databinding.ActivityDriverProfileBinding
import com.example.oche_app_final.model.ChatDriver
import com.example.oche_app_final.model.Constances
import com.example.oche_app_final.model.CustomerReviews
import com.example.oche_app_final.model.CustomerReviewsAdapter
import com.yandex.mapkit.MapKitFactory
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.map.CameraPosition
import com.yandex.mapkit.mapview.MapView

class DriverProfileActivity : AppCompatActivity() {

    private lateinit var mapView: MapView
    private var customerReviews: List<CustomerReviews> = emptyList()
    private lateinit var customerReviewsAdapter: CustomerReviewsAdapter
    lateinit var binding: ActivityDriverProfileBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (!Constances.isMapInitialized) {
            MapKitFactory.setApiKey("e6e7b570-be74-4502-9a79-da3c217e14a3")
            MapKitFactory.initialize(this)
            Constances.isMapInitialized = true
        }
        binding = ActivityDriverProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)


        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Rider profile"

        val chatDriver = intent.getSerializableExtra("chatDriver") as ChatDriver
        val profileImage = chatDriver.profileImage
        val name = chatDriver.name
        val registrationNumber = chatDriver.registrationNumber
        val stars = chatDriver.stars
        val carModel = chatDriver.carModel
        val gender = chatDriver.gender

        val star1: ImageView = findViewById(R.id.driverStar1)
        val star2: ImageView = findViewById(R.id.driverStar2)
        val star3: ImageView = findViewById(R.id.driverStar3)
        val star4: ImageView = findViewById(R.id.driverStar4)
        val star5: ImageView = findViewById(R.id.driverStar5)

        val layoutManager = LinearLayoutManager(this)
        binding.reviewsRecyclerView.layoutManager = layoutManager

        customerReviews = listOf(
            CustomerReviews(R.drawable.profile_avatar_customer_1, "Mrs Bimbo", "Right on time ", 4),
            CustomerReviews(
                R.drawable.profile_avatar_customer_2,
                "Gentle Jack",
                "Calls with update on location",
                5
            ),
            CustomerReviews(R.drawable.profile_avatar_3, "Raph Ron", "Thanks for your service", 5),
            CustomerReviews(
                R.drawable.profile_avatar_4,
                "Joy Ezekiel",
                "Thanks for your service",
                0
            ),
            CustomerReviews(
                R.drawable.profile_avatar_5,
                "Joy Ezekiel",
                "Thanks for your service",
                1
            )
        )
        customerReviewsAdapter = CustomerReviewsAdapter(customerReviews)
        binding.reviewsRecyclerView.adapter = customerReviewsAdapter

        val starViews = arrayOf(star1, star2, star3, star4, star5)
        for (i in starViews.indices) {
            if (i < stars) {
                starViews[i].setImageResource(R.drawable.ic_star_filled)
            } else {
                starViews[i].setImageResource(R.drawable.ic_star)
            }
        }

        binding.profileImageView.setImageResource(profileImage)
        binding.nameTextView.text = name
        binding.carModelTextView.text = carModel
        binding.registrationNumberTextView.text = registrationNumber
        binding.genderTextView.text = gender

        binding.buttonSendMessage.setOnClickListener {
            val intent = Intent(this, ChatsMessagesActivity::class.java)
            intent.putExtra("chatName", name)
            intent.putExtra("chatImage", profileImage.toString())
            startActivity(intent)
        }

        binding.buttonCallRider.setOnClickListener {
            val intent = Intent(this, CallRiderActivity::class.java)
            intent.putExtra("chatName", name)
            intent.putExtra("chatImage", profileImage.toString())
            startActivity(intent)
        }

        mapView = findViewById(R.id.mapview)
    }

    override fun onStart() {
        super.onStart()
        MapKitFactory.getInstance().onStart()
        mapView.onStart()

        val cameraPosition = CameraPosition(
            Point(56.838011, 60.597465),
            9f, 0f, 0f
        )
        mapView.map.move(
            cameraPosition
        )
    }

    override fun onStop() {
        mapView.onStop()
        MapKitFactory.getInstance().onStop()
        super.onStop()
    }

    override fun onDestroy() {
        mapView.onStop()
        MapKitFactory.getInstance().onStop()
        super.onDestroy()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

}