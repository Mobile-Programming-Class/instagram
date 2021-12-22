package com.finject.instagram

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.view.WindowManager
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.finject.instagram.data.Count
import com.finject.instagram.data.User
import com.finject.instagram.fragment.*
import com.finject.instagram.interfaces.HandleCameraIntent
import com.finject.instagram.interfaces.HandleGalleryIntent
import com.finject.instagram.interfaces.LaunchIntent
import com.finject.instagram.interfaces.Refresh
import com.finject.instagram.service.SessionManager
import java.io.IOException

//php artisan serve --host=192.168.0.103 --port=80

class MainActivity : AppCompatActivity() {

    private val PICK_IMAGE_REQUEST = 71
    private val CAMERA_REQUEST = 1888
    var photo : Bitmap ? = null
    var imgBase64 : String ? = null
    var filePath: Uri? = null

    var refreshListener: Refresh? = null
    var handleCameraIntent: HandleCameraIntent? = null
    var handleGalleryIntent: HandleGalleryIntent?= null
    var launchIntent : LaunchIntent ?= null

    var homeFragment : Fragment ? = null
    var searchFragment : Fragment ? = null
    var galleryFragment : Fragment ? = null
    var favouriteFragment : Fragment ? = null
    var profileFragment : Fragment ? = null
    var loginFragment : Fragment ? = null

    var access_token : String ? = null
    var user : User ? = null
    var follower: Count? = null

    var sessionManager : SessionManager ? = null;

    override fun onCreate(savedInstanceState: Bundle?) {

        homeFragment = HomeFragment(this)
        searchFragment = SearchFragment(this)
        galleryFragment = GalleryFragment(this)
        favouriteFragment = FavouriteFragment(this)
        profileFragment = ProfileFragment(this)
        loginFragment = LoginFragment(this)

        sessionManager = SessionManager(this)

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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // handle intent camera
        if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {
            photo = data?.extras?.get("data") as Bitmap

            getSupportFragmentManager().beginTransaction().replace(R.id.frame, galleryFragment!!).commit()

            handleCameraIntent?.handleCameraIntent( photo )
        }

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK) {
            if(data == null || data.data == null){
                return
            }

            filePath = data.data
            try {
                getSupportFragmentManager().beginTransaction().replace(R.id.frame, galleryFragment!!).commit()
                handleGalleryIntent?.handleGalleryIntent( filePath )
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

}