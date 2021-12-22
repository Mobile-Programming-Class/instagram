package com.finject.instagram

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import com.finject.instagram.R
import com.finject.instagram.data.General
import com.finject.instagram.data.ResponseRegister
import com.finject.instagram.service.DataServices
import com.finject.instagram.service.SessionManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.ByteArrayOutputStream
import java.io.IOException

class RegisterActivity: AppCompatActivity()  {

    private val PICK_IMAGE_REQUEST = 71
    private val CAMERA_REQUEST = 1888
    var photo : Bitmap ? = null
    var imgBase64 : String ? = null
    var filePath: Uri? = null
    private lateinit var sessionManager: SessionManager

    var scrollView : ScrollView ?= null
    var etEmailInput : EditText ?= null
    var etPasswordInput : EditText ?= null
    var etNameInput : EditText ?= null
    var etBioInput : EditText ?= null
    var etMobileInput : EditText ?= null
    var etCityInput : EditText ?= null
    var btnSend : Button ?= null
    var btnGallery : Button ?= null
    var btnCamera : Button ?= null

    var ivAvatarProfileInput : ImageView ?= null

    var tv_name : TextView ?= null
    var tvEmailProfile : TextView ?= null
    var tvPhoneNumber : TextView ?= null
    var tvBio : TextView ?= null
    var card : CardView ?= null
    var tvPreview : TextView ?= null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        sessionManager = SessionManager( this )
        initItem()
    }

    fun initItem() {
        scrollView = findViewById( R.id.linearLayout )
        etEmailInput = findViewById( R.id.etEmailInput )
        etPasswordInput = findViewById( R.id.etPasswordInput )
        etNameInput = findViewById( R.id.etNameInput )
        etBioInput = findViewById( R.id.etBioInput )
        etMobileInput = findViewById( R.id.etMobileInput )
        etCityInput = findViewById( R.id.etCityInput )
        btnSend = findViewById( R.id.btnSend )
        btnGallery = findViewById( R.id.btnGallery )
        btnCamera = findViewById( R.id.btnCamera )

        ivAvatarProfileInput = findViewById( R.id.ivAvatarProfileInput )
        tv_name = findViewById( R.id.tv_name )
        tvEmailProfile = findViewById( R.id.tvEmailProfile )
        tvPhoneNumber = findViewById( R.id.tvPhoneNumber )
        tvBio = findViewById( R.id.tvBio )
        card = findViewById( R.id.card )
        tvPreview = findViewById( R.id.tvPreview )

        scrollView?.setOnClickListener{
            refreshView()
        }
        tvPreview?.setOnClickListener {
            refreshView()
        }
        card?.setOnClickListener{
            refreshView()
        }
        btnSend?.setOnClickListener {
            registerAccount()
        }

        btnCamera?.setOnClickListener { launchCamera() }
        btnGallery?.setOnClickListener { launchGallery() }
    }

    fun refreshView () {
        tv_name?.text =  etNameInput?.text.toString()
        tvEmailProfile?.text =  etEmailInput?.text.toString()
        tvPhoneNumber?.text = etMobileInput?.text.toString()
        tvBio?.text = etBioInput?.text.toString()
    }

    private fun launchGallery() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST)
    }

    private fun launchCamera() {
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(cameraIntent, CAMERA_REQUEST)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // handle intent camera
        if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {
            photo = data?.extras?.get("data") as Bitmap
            handleCameraIntent()
        }

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK) {
            if(data == null || data.data == null){
                return
            }

            filePath = data.data
            try {
                handleGalleryIntent()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    fun handleCameraIntent() {
        ivAvatarProfileInput?.setImageBitmap(photo)
        encodePhoto()
    }

    fun handleGalleryIntent() {
        val bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, filePath)
        ivAvatarProfileInput?.setImageBitmap(bitmap)
        encodePhoto()
    }

    fun encodePhoto() {
        val bitmap = (ivAvatarProfileInput?.drawable as BitmapDrawable).bitmap
        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val data = baos.toByteArray()
        val encodedImage: String = Base64.encodeToString(data, Base64.DEFAULT)
        imgBase64 = encodedImage
    }

    fun registerAccount() {
        if ( etEmailInput?.text.toString().equals("") ||
            etPasswordInput?.text.toString().equals("") ||
            etNameInput?.text.toString().equals("")
        ) {
            Toast.makeText(getApplicationContext(), "please fill the required fields",
                Toast.LENGTH_LONG).show()
            return
        }
        var imgEncodedToPass = "";
        if (imgBase64?.subSequence(0, 4).toString().equals("/9j/")) imgEncodedToPass = "data:image/jpeg;base64," + imgBase64
        if (imgBase64?.subSequence(0, 4).toString().equals("iVBO")) imgEncodedToPass = "data:image/png;base64," + imgBase64

        val networkServices = DataServices.create()
        val call = networkServices.register(
            etEmailInput?.text.toString(),
            etPasswordInput?.text.toString(),
            etNameInput?.text.toString(),
            etBioInput?.text.toString(),
            etMobileInput?.text.toString(),
            etCityInput?.text.toString(),
            imgEncodedToPass
        )

        call.enqueue(object: Callback<ResponseRegister> {
            override fun onFailure(call: Call<ResponseRegister>, t: Throwable) {
                println("On Failure")
                println(t.message)
                Toast.makeText(getApplicationContext(), "Failed Getting Response" + t.message,
                    Toast.LENGTH_LONG).show()
            }
            override fun onResponse(call: Call<ResponseRegister>, response: Response<ResponseRegister>) {
                Toast.makeText(getApplicationContext(), "Success Getting Response " + imgBase64?.subSequence(0, 5),
                    Toast.LENGTH_LONG).show()
                if (response.body() != null) {
                    val data: ResponseRegister = response.body()!!
                    Toast.makeText(getApplicationContext(), "Response body not null: \nSUCCESS" + data.user?.name,
                        Toast.LENGTH_LONG).show()
                } else {
                    Toast.makeText(getApplicationContext(), "Response Body Null",
                        Toast.LENGTH_LONG).show()
                }
            }
        })
    }
}