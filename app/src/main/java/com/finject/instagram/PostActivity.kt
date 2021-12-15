package com.finject.instagram

import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
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
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PostActivity : AppCompatActivity() {
    var post: Post? =null
    var post_id : String? = null
    var post_foto: String? = null
    var post_caption: String? = null
    var user_name: String? = null
    var user_avatar: String? = null
    var post_likes: String? = null

    val comment_list = ArrayList<ItemComment>()
    lateinit var CommentAdapter : CommentAdapter

    var name : TextView? = null
    var logo : ImageView? = null
    var photo : ImageView? = null
    var likes : TextView? = null
    var description : TextView? = null

    var isApiSuccess = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post)

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
    }

    fun initPost(){
        post_id = intent.getStringExtra("post_id").toString()
        post_foto = intent.getStringExtra("post_foto").toString()
        post_caption = intent.getStringExtra("post_caption").toString()
        post_likes = intent.getStringExtra("likes").toString()
        user_name = intent.getStringExtra("user_name").toString()
        user_avatar = intent.getStringExtra("user_avatar").toString()

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
    }
}