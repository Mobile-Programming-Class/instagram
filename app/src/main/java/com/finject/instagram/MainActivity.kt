package com.finject.instagram

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.finject.instagram.data.Count
import com.finject.instagram.data.Follower
import com.finject.instagram.data.Post
import com.finject.instagram.data.User
import com.finject.instagram.fragment.*

//php artisan serve --host=192.168.0.103 --port=80

class MainActivity : AppCompatActivity() {
    var refreshListener: Refresh? = null
    var homeFragment : Fragment ? = null
    var searchFragment : Fragment ? = null
    var galleryFragment : Fragment ? = null
    var favouriteFragment : Fragment ? = null
    var profileFragment : Fragment ? = null
    var loginFragment : Fragment ? = null

    var access_token : String ? = null
    var user : User ? = null
    var follower: Count? = null

    override fun onCreate(savedInstanceState: Bundle?) {

        homeFragment = HomeFragment(this)
        searchFragment = SearchFragment()
        galleryFragment = GalleryFragment(this)
        favouriteFragment = FavouriteFragment()
        profileFragment = ProfileFragment(this)
        loginFragment = LoginFragment(this)

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

        getSupportFragmentManager().beginTransaction().replace(R.id.frame, homeFragment!!).commit()

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
                getSupportFragmentManager().beginTransaction().replace(R.id.frame, homeFragment!!).commit()
            }
            R.id.search_icon -> {
                getSupportFragmentManager().beginTransaction().replace(R.id.frame, searchFragment!!).commit()
            }
            R.id.add_icon -> {
                getSupportFragmentManager().beginTransaction().replace(R.id.frame, galleryFragment!!).commit()
            }
            R.id.heart_icon -> {
                getSupportFragmentManager().beginTransaction().replace(R.id.frame, favouriteFragment!!).commit()
            }
            R.id.profile_icon -> {
                if (access_token == null || access_token.equals("")) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.frame, loginFragment!!).commit()
                } else {
                    getSupportFragmentManager().beginTransaction().replace(R.id.frame, profileFragment!!).commit()
                }
            }
        }
    }

}