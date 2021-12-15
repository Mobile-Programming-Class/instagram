package com.finject.instagram.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.finject.instagram.MainActivity
import com.finject.instagram.R
import com.finject.instagram.data.ResponseLogin
import com.finject.instagram.data.ResponsePostingGetById
import com.finject.instagram.service.DataServices
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginFragment (private var thisContext: MainActivity) : Fragment() {

    lateinit var etEmail : EditText
    lateinit var etPassword : EditText
    lateinit var btnLogin : Button

    override fun onCreateView(inflater: LayoutInflater, parent: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_login, parent, false)

        initItem(view)

        return view
    }

    fun initItem(view : View) {
        etEmail = view.findViewById<EditText>(R.id.etEmail)
        etPassword = view.findViewById<EditText>(R.id.etPassword)
        btnLogin = view.findViewById<Button>(R.id.btnLogin)

        btnLogin.setOnClickListener { login() }
    }

    fun login() {
        val networkServices = DataServices.create()

        val callLogin = networkServices.login(etEmail.text.toString(), etPassword.text.toString())
        callLogin.enqueue(object: Callback<ResponseLogin> {
            override fun onFailure(call: Call<ResponseLogin>, t: Throwable) {
                println("On Failure")
                println(t.message)
                Toast.makeText(thisContext, "Failed Getting Response" + t.message,
                    Toast.LENGTH_LONG).show()
            }
            override fun onResponse(call: Call<ResponseLogin>, response: Response<ResponseLogin>) {
                Toast.makeText(thisContext, "Success Getting Response",
                    Toast.LENGTH_LONG).show()
                if (response.body() != null) {
                    val data: ResponseLogin = response.body()!!
                    Toast.makeText(thisContext, "Response body not null",
                        Toast.LENGTH_LONG).show()
                    if ( !data.access_token.equals("") ) {
                        thisContext.access_token = data.access_token
                        thisContext.user = data.user
                        thisContext.user?.avatar = data.asset_link + data.user?.avatar

                        thisContext.getSupportFragmentManager().beginTransaction().replace(R.id.frame, thisContext.profileFragment!!).commit()
                    } else {
                        Toast.makeText(thisContext, "Response NOT null. access_token IS null",
                            Toast.LENGTH_LONG).show()
                    }
                } else {
                    Toast.makeText(thisContext, "Response Body Null on login",
                        Toast.LENGTH_LONG).show()
                }
            }
        })
    }
}