package com.finject.instagram

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.ImageView
import androidx.core.content.ContextCompat
import com.finject.instagram.data.Post
import com.finject.instagram.fragment.*
//php artisan serve --host=192.168.0.103 --port=80
class MainActivity : AppCompatActivity() {
    var refreshListener: Refresh? = null
    var homeFragment = HomeFragment(this)
    var searchFragment = SearchFragment()
    var galleryFragment = GalleryFragment()
    var favouriteFragment = FavouriteFragment()
    var profileFragment = ProfileFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val window = window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.statusBarColor = ContextCompat.getColor(this, R.color.grey)
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR

        val homeBtn = findViewById<ImageView>(R.id.home_icon)
        val searchBtn = findViewById<ImageView>(R.id.search_icon)
        val galleryBtn = findViewById<ImageView>(R.id.add_icon)
        val favouriteBtn = findViewById<ImageView>(R.id.heart_icon)
        val profileBtn = findViewById<ImageView>(R.id.profile_icon)
        val insta = findViewById<ImageView>(R.id.insta_img)

        getSupportFragmentManager().beginTransaction().replace(R.id.frame, homeFragment).commit()

        homeBtn.setOnClickListener(clickListener)
        searchBtn.setOnClickListener(clickListener)
        galleryBtn.setOnClickListener(clickListener)
        favouriteBtn.setOnClickListener(clickListener)
        profileBtn.setOnClickListener(clickListener)
        insta.setOnClickListener{ refreshListener?.refresh() }

        supportActionBar?.hide()
    }

    private val clickListener : View.OnClickListener = View.OnClickListener { view ->
        when (view.id) {
            R.id.home_icon -> {
                getSupportFragmentManager().beginTransaction().replace(R.id.frame, homeFragment).commit()
            }
            R.id.search_icon -> {
                getSupportFragmentManager().beginTransaction().replace(R.id.frame, searchFragment).commit()
            }
            R.id.add_icon -> {
                getSupportFragmentManager().beginTransaction().replace(R.id.frame, galleryFragment).commit()
            }
            R.id.heart_icon -> {
                getSupportFragmentManager().beginTransaction().replace(R.id.frame, favouriteFragment).commit()
            }
            R.id.profile_icon -> {
                getSupportFragmentManager().beginTransaction().replace(R.id.frame, profileFragment).commit()
            }
        }
    }

}