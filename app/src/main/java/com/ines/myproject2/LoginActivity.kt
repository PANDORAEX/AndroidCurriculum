package com.ines.myproject2

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import io.realm.Realm
import io.realm.kotlin.createObject
import io.realm.kotlin.where
import kotlinx.android.synthetic.main.activity_login.*
import org.jetbrains.anko.alert
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.yesButton

class LoginActivity : AppCompatActivity() {

    private lateinit var realm: Realm

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        realm = Realm.getDefaultInstance()

        val createuser = realm.where<User>()
                .equalTo("USERID","testtest").findFirst()
        if (createuser == null) {
            realm.executeTransaction {
                val user = realm.createObject<User>("testtest")
                user.PASSWORD = "testtest"
            }
        }

        login.setOnClickListener{ view ->
            val username = usernameEdit.text.toString()
            val password = passwordEdit.text.toString()
            val messages: MutableList<String> = mutableListOf("")
            var message: String = ""
            if (username == ""){
                messages.add("ユーザ名を入力してください。")
            }
            else if (username.length < 8) {
                messages.add("ユーザ名は8桁で入力してください。")
            }
            else if (!username.matches("[0-9a-zA-Z]+".toRegex())) {
                messages.add("ユーザ名は半角英数字で入力してください。")
            }
            if (password == ""){
                messages.add("パスワードを入力してください。")
            }
            if (messages != mutableListOf<String>("")) {
                for (a in messages){
                    if (a !=""){
                        message = message + a + "\n"
                    }
                }
                message = message.substring(0,message.length-1)
                alert(message) { yesButton {null } }.show()
            } else {
                val user = realm.where<User>()
                        .equalTo("USERID", username).findFirst()
                if (user != null) {
                    if (password == user.PASSWORD) {
                        startActivity<MainActivity>()
                    } else {
                        alert("ユーザ名またはパスワードに誤りがあります。") { yesButton { null } }.show()
                    }
                } else {
                    alert("ユーザ名またはパスワードに誤りがあります。") { yesButton { null } }.show()
                }
            }
        }

        clear.setOnClickListener {
            usernameEdit.setText("")
            passwordEdit.setText("")
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        realm.close()
    }
}
