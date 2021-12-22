package com.finject.instagram.fragment

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.media.Image
import android.os.Bundle
import android.provider.MediaStore
import android.util.Base64.DEFAULT
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.util.Base64.encodeToString
import android.widget.*
import android.net.Uri
import com.finject.instagram.MainActivity

import com.finject.instagram.R
import com.finject.instagram.data.General
import com.finject.instagram.interfaces.HandleCameraIntent
import com.finject.instagram.interfaces.HandleGalleryIntent
import com.finject.instagram.interfaces.LaunchIntent
import com.finject.instagram.service.DataServices
import kotlinx.android.synthetic.main.item_search.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.ByteArrayOutputStream

import org.tensorflow.lite.TensorFlowLite
import org.tensorflow.lite.examples.textclassification.TextClassificationClient
import java.io.IOException

class GalleryFragment (var thisContext: MainActivity ? = null) : Fragment(), HandleCameraIntent, HandleGalleryIntent, LaunchIntent {

    private var filePath: Uri? = null

    var btnGallery : Button? = null
    var btnCamera : Button ? = null
    var logo : ImageView? = null
    var brand_name : TextView? = null
    var post_img : ImageView ? = null
    var likes_txt : TextView? = null
    var description_txt : TextView? = null
    var add_caption : EditText? = null
    var btnSendPost : Button ? = null
    var testPredict : Button ? = null
    var btnCheckPhoto : Button ? = null

    var photo : Bitmap ? = null

    var imgBase64 : String ? = null
    var predictedCaption : Float ? = null

    private val PICK_IMAGE_REQUEST = 71
    private val CAMERA_REQUEST = 1888

    lateinit var client: TextClassificationClient

    init {
        if (thisContext == null) {
            thisContext = activity as MainActivity?
        }
    }

    @SuppressLint("WrongThread")
    override fun onCreateView(inflater: LayoutInflater, parent: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_gallery, parent, false)

        if (thisContext == null) {
            thisContext = activity as MainActivity?
        }
        client = TextClassificationClient(thisContext)
        client.load()

        thisContext?.launchIntent = this

        initItem(view)
        return view
    }

    /*fun newInstance(bundle : Bundle) : ReceiveFragment{
        val fragment = ReceiveFragment()
        fragment.arguments=bundle
        return fragment
    }*/

    fun initItem(view: View) {
        btnGallery = view.findViewById(R.id.btnGallery)
        btnCamera = view.findViewById(R.id.btnCamera)
        logo = view.findViewById(R.id.logo)
        brand_name = view.findViewById(R.id.brand_name)
        post_img = view.findViewById(R.id.post_img)
        likes_txt = view.findViewById(R.id.likes_txt)
        description_txt = view.findViewById(R.id.description_txt)
        add_caption = view.findViewById(R.id.add_caption)
        btnSendPost = view.findViewById(R.id.btnSendPost)
        testPredict = view.findViewById(R.id.testPredict)
        btnCheckPhoto = view.findViewById( R.id.btnCheckPhoto )

        btnGallery?.setOnClickListener {
            if ( thisContext?.launchIntent != null)
                thisContext?.launchIntent?.launchIntentGallery()
            else launchCamera()
        }
        btnCamera?.setOnClickListener {
            if ( thisContext?.launchIntent != null)
                thisContext?.launchIntent?.launchIntentCamera()
            else launchGallery()
        }
        testPredict?.setOnClickListener {
            predictIt(add_caption?.text.toString())
        }

        btnSendPost?.setOnClickListener { handlePost() }
        btnCheckPhoto?.setOnClickListener { checkCurrentPhoto() }
        post_img?.setOnClickListener { restorePhoto() }
    }
    fun restorePhoto() {
        post_img?.visibility = View.VISIBLE
        post_img?.setImageBitmap(photo)

        // TODO: NAME FOR FILE
        var caption = add_caption?.text.toString()
    }

    fun checkCurrentPhoto () {
        Toast.makeText(thisContext, "current photo \n" + imgBase64?.subSequence(0, 10).toString(),
            Toast.LENGTH_LONG).show()
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
            handleCameraIntent( photo )
        }

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK) {
            if(data == null || data.data == null){
                return
            }

            filePath = data.data
            try {
                handleGalleryIntent( filePath )
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    override fun handleCameraIntent(photo: Bitmap?) {
        post_img?.setImageBitmap(photo)
        // TODO: upload baos to instapp
        val bitmap = (post_img?.drawable as BitmapDrawable).bitmap
        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val data = baos.toByteArray()
        val encodedImage: String = encodeToString(data, DEFAULT)
        imgBase64 = encodedImage

        handlePost()
        Toast.makeText(thisContext, "Success handle " + encodedImage.subSequence(0, 5),
            Toast.LENGTH_LONG).show()
    }

    override fun handleGalleryIntent(filePath: Uri?) {
        val bitmap = MediaStore.Images.Media.getBitmap(thisContext?.contentResolver, filePath)

        post_img?.setImageBitmap(bitmap)

        // TODO: upload baos to instapp
        val bitmap1 = (post_img?.drawable as BitmapDrawable).bitmap
        val baos = ByteArrayOutputStream()
        bitmap1.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val data = baos.toByteArray()
        val encodedImage: String = encodeToString(data, DEFAULT)
        imgBase64 = encodedImage

        handlePost()
    }

    fun handlePost ( /*caption: String, imageEncoded: String */) {
        var imgEncodedToPass = "";
        val networkServices = DataServices.create()
        if (imgBase64?.subSequence(0, 4).toString().equals("/9j/")) imgEncodedToPass = "data:image/jpeg;base64," + imgBase64
        if (imgBase64?.subSequence(0, 4).toString().equals("iVBO")) imgEncodedToPass = "data:image/png;base64," + imgBase64
        val results = client.classify( add_caption?.text.toString() )

        if (thisContext?.access_token == null) {
            Toast.makeText(thisContext, "Please login before posting", Toast.LENGTH_LONG).show()
            return
        }


        val call = networkServices.posting( "Bearer " + thisContext?.access_token!!, add_caption?.text.toString(), imgEncodedToPass, predictIt( add_caption?.text.toString() ) )
        // Create JSON using JSONObject
//        val data = SendPhoto(caption, imgBase64)
        call.enqueue(object: Callback<General> {
            override fun onFailure(call: Call<General>, t: Throwable) {
                println("On Failure")
                println(t.message)
                Toast.makeText(thisContext, "Failed Post" + t.message,
                    Toast.LENGTH_LONG).show()
            }
            override fun onResponse(call: Call<General>, response: Response<General>) {
                Toast.makeText(thisContext, "Success Getting Response " + imgBase64?.subSequence(0, 50),
                    Toast.LENGTH_LONG).show()
                if (response.body() != null) {
                    val data: General = response.body()!!
                    Toast.makeText(thisContext, "Response body not null: " + data.message,
                        Toast.LENGTH_LONG).show()
                    if (data.data!!.isNotEmpty()) {
                        Toast.makeText(thisContext, "Foto telah diposting",
                            Toast.LENGTH_LONG).show()
                    }
                } else {
                    Toast.makeText(thisContext, "Response Body Null on Post fun",
                        Toast.LENGTH_LONG).show()
                }
            }
        })
    }

    fun predictIt(caption: String): Float? {

        val results = client.classify(caption)
        Toast.makeText(thisContext, "Success predict " + results[0].confidence.toString(),
            Toast.LENGTH_LONG).show()

        var res : Float ? = null
        for(result in results){
            Toast.makeText(thisContext, result.toString(),
                Toast.LENGTH_LONG).show()
            if ( result.title.equals("Positive") )
                res = result.confidence
        }

        return res
    }

    override fun launchIntentCamera() {
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(cameraIntent, CAMERA_REQUEST)
    }

    override fun launchIntentGallery() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST)
    }
}