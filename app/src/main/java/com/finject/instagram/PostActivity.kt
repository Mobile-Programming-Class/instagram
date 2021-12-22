package com.finject.instagram

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.finject.instagram.adapter.CommentAdapter
import com.finject.instagram.adapter.PostAdapter
import com.finject.instagram.data.*
import com.finject.instagram.service.DataServices
import kotlinx.android.synthetic.main.fragment_home.*
import org.tensorflow.lite.examples.textclassification.TextClassificationClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PostActivity : AppCompatActivity() {
    var post: PostGet? =null
    var post_id : String? = null
    var post_foto: String? = null
    var post_caption: String? = null
    var user_name: String? = null
    var user_avatar: String? = null
    var post_likes: String? = null
    var access_token: String ? =null

    lateinit var client: TextClassificationClient

    val comment_list = ArrayList<ItemComment>()
    lateinit var CommentAdapter : CommentAdapter

    var name : TextView? = null
    var logo : ImageView? = null
    var photo : ImageView? = null
    var likes : TextView? = null
    var description : TextView? = null
    var profile_pic : ImageView?=null
    var comment_edt : EditText?=null
    var sendBtn : ImageView?=null
    var heart : ImageView ?= null

    var isApiSuccess = false

    @SuppressLint("WrongThread")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post)

        client = TextClassificationClient(this)
        client.load()

        val commentViewList = findViewById<RecyclerView>(R.id.recyclerViewComment)
        commentViewList.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        CommentAdapter = CommentAdapter(comment_list)
        commentViewList.adapter = CommentAdapter

        initItem();

        initPost()
        supportActionBar?.hide()
    }

    fun initItem() {
        name = findViewById<TextView>(R.id.brand_name)
        logo = findViewById<ImageView>(R.id.logo)
        photo = findViewById<ImageView>(R.id.post_img)
        likes = findViewById<TextView>(R.id.likes_txt)
        description = findViewById<TextView>(R.id.description_txt)

        profile_pic = findViewById( R.id.profile_pic )
        comment_edt = findViewById( R.id.comment_edt )
        sendBtn = findViewById( R.id.sendBtn )
        heart = findViewById( R.id.heart )

        sendBtn?.setOnClickListener { handleComment() }
        heart?.setOnClickListener { onClickLikeIt() }
    }

    fun predictIt(comment: String): Float? {

        val results = client.classify(comment)
        Toast.makeText(this, "Success predict " + results[0].confidence.toString(),
            Toast.LENGTH_LONG).show()

        var res : Float ? = null
        for(result in results){
            Toast.makeText(this, result.toString(),
                Toast.LENGTH_LONG).show()
            if ( result.title.equals("Positive") )
                res = result.confidence
        }

        return res
    }

    fun handleComment() {

        if ( access_token.equals("") || access_token == null ) {
            Toast.makeText(this, "Please login before posting", Toast.LENGTH_LONG).show()
            return
        }

        Toast.makeText(this,
            "Bearer " + access_token!!.toString().subSequence(0, 10).toString() + " \n" +
            predictIt( comment_edt?.text.toString() ).toString() + " \n" +
            comment_edt?.text.toString() + " \n" +
            post_id?.toString()
            , Toast.LENGTH_LONG).show()

        val networkServices = DataServices.create()
        val callComment = networkServices.createComment(
            "Bearer " + access_token!!.toString(),
            predictIt( comment_edt?.text.toString() ).toString(),
            comment_edt?.text.toString(),
            post_id?.toInt() )

        callComment.enqueue(object: Callback<ResponseComment> {
            override fun onFailure(call: Call<ResponseComment>, t: Throwable) {
                println("On Failure")
                println(t.message)
                Toast.makeText(getApplicationContext(), "Failed Getting Response" + t.message,
                    Toast.LENGTH_LONG).show()
            }
            override fun onResponse(call: Call<ResponseComment>, response: Response<ResponseComment>) {
                Toast.makeText(getApplicationContext(), "Success Getting Response",
                    Toast.LENGTH_LONG).show()
                if (response.body() != null) {
                    val data: ResponseComment = response.body()!!
                    Toast.makeText(getApplicationContext(), "Response body not null",
                        Toast.LENGTH_LONG).show()
                    if (data.data != null) {
                        refreshComment()
                        Toast.makeText(getApplicationContext(), data.data?.isi ,
                            Toast.LENGTH_LONG).show()
                    } else {
                        Toast.makeText(getApplicationContext(), "Response NOT null. Post IS null",
                            Toast.LENGTH_LONG).show()
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Response Body Null on get fun",
                        Toast.LENGTH_LONG).show()
                }
            }
        })
    }

    fun initPost(){
        post_id = intent.getStringExtra("post_id").toString()
        post_foto = intent.getStringExtra("post_foto").toString()
        post_caption = intent.getStringExtra("post_caption").toString()
        post_likes = intent.getStringExtra("likes").toString()
        user_name = intent.getStringExtra("user_name").toString()
        user_avatar = intent.getStringExtra("user_avatar").toString()
        access_token = intent.getStringExtra("access_token").toString()



        displayPost()

        // CALL api post
        val networkServices = DataServices.create()

        val callPost = networkServices.getPostById(post_id!!)
        callPost.enqueue(object: Callback<ResponsePostingGetById> {
            override fun onFailure(call: Call<ResponsePostingGetById>, t: Throwable) {
                println("On Failure")
                println(t.message)
                Toast.makeText(getApplicationContext(), "Failed Getting Response" + t.message,
                    Toast.LENGTH_LONG).show()
            }
            override fun onResponse(call: Call<ResponsePostingGetById>, response: Response<ResponsePostingGetById>) {
                Toast.makeText(getApplicationContext(), "Success Getting Response",
                    Toast.LENGTH_LONG).show()
                if (response.body() != null) {
                    val data: ResponsePostingGetById = response.body()!!
                    Toast.makeText(getApplicationContext(), "Response body not null",
                        Toast.LENGTH_LONG).show()
                    if (data.data != null) {
                        post = data.data
                        post?.foto = data.message + post?.foto
                        isApiSuccess = true
                    } else {
                        Toast.makeText(getApplicationContext(), "Response NOT null. Post IS null",
                            Toast.LENGTH_LONG).show()
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Response Body Null on get fun",
                        Toast.LENGTH_LONG).show()
                }
            }
        })

        refreshComment()
    }

    fun refreshComment() {
        val networkServices = DataServices.create()
        // CALL api comment
        val callComment = networkServices.getCommentByPostId(post_id!!)
        callComment.enqueue(object: Callback<Comment> {
            override fun onFailure(call: Call<Comment>, t: Throwable) {
                println("On Failure")
                println(t.message)
                Toast.makeText(getApplicationContext(), "Failed Getting Response" + t.message,
                    Toast.LENGTH_LONG).show()
            }
            override fun onResponse(call: Call<Comment>, response: Response<Comment>) {
                Toast.makeText(getApplicationContext(), "Success Getting Response",
                    Toast.LENGTH_LONG).show()
                if (response.body() != null) {
                    val data: Comment = response.body()!!
                    Toast.makeText(getApplicationContext(), "Response body not null",
                        Toast.LENGTH_LONG).show()
                    if (data.data != null) {
                        if (data.data!!.isNotEmpty()) {
                            comment_list.clear()
                            for (comment in data.data!!) {
                                comment_list.add(ItemComment(
                                    comment?.id,
                                    comment?.isi,
                                    comment?.id_post,
                                    comment?.id_user,
                                    comment?.created_at,
                                    comment?.updated_at,
                                    comment?.sentiment,
                                    comment?.user
                                ))
                            }
                            if (comment_list.count() != 0) {
                                println("Dataset isnt null")
                                CommentAdapter.notifyDataSetChanged()
                            }
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "Response NOT null. Post IS null",
                            Toast.LENGTH_LONG).show()
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Response Body Null on get fun comment",
                        Toast.LENGTH_LONG).show()
                }
            }
        })
    }

    fun displayPost() {
        name!!.text = user_name
        likes!!.text =  post_likes // postList[p1].likes +" "+"likes"
        description?.text = post_caption

        Glide.with(this)
            .load(user_avatar)
            .into(this.logo!!)

        Glide.with(this)
            .load(post_foto)
            .into(this.photo!!)

        Glide.with(this)
            .load(user_avatar)
            .into(this.profile_pic!!)
    }

    fun onClickLikeIt () {
        val networkServices = DataServices.create()
        val call = networkServices.liking( "Bearer " + access_token, post_id?.toString() )

        call.enqueue(object: Callback<General> {
            override fun onFailure(call: Call<General>, t: Throwable) {
                println("On Failure")
                println(t.message)
                Toast.makeText(getApplicationContext(), "Failed Getting Response" + t.message,
                    Toast.LENGTH_LONG).show()
            }
            override fun onResponse(call: Call<General>, response: Response<General>) {
                Toast.makeText(getApplicationContext(), "Success Getting Response",
                    Toast.LENGTH_LONG).show()
                if (response.body() != null) {
                    val data: General = response.body()!!
                    Toast.makeText(getApplicationContext(), "Response body not null",
                        Toast.LENGTH_LONG).show()
                    if (data.data!!.isNotEmpty()) {
                        Toast.makeText(getApplicationContext(), data.data.toString(),
                            Toast.LENGTH_LONG).show()

                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Response Body Null on get fun",
                        Toast.LENGTH_LONG).show()
                }
            }
        })
    }
}