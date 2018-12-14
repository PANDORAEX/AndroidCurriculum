package com.ines.myproject2

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.text.format.DateFormat
import android.view.View
import android.widget.ArrayAdapter
import io.realm.Realm
import io.realm.kotlin.createObject
import io.realm.kotlin.where
import kotlinx.android.synthetic.main.activity_project_edit.*
import org.jetbrains.anko.alert
import org.jetbrains.anko.yesButton
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*


class ProjectEditActivity : AppCompatActivity() {
    private lateinit var realm: Realm

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_project_edit)
        realm = Realm.getDefaultInstance()
        createInit()
        setPullDown()

        val projectId = intent?.getLongExtra("project_id", -1L)
        if (projectId != -1L) {
            val project = realm.where<Project>()
                    .equalTo("PROJECT_ID", projectId).findFirst()
            startdateEdit.setText(DateFormat.format("yyyy/MM/dd", project!!.START_DATE))
            protypeEdit.setSelection(project.PROTYPE_CODE.toInt())
            projectnoEdit.setText(project.PROJECT_NO)
            projectnameEdit.setText(project.PROJECT_NAME)
            languageEdit.setSelection(project.LANGUAGE_CODE.toInt())
            summaryEdit.setText(project.SUMMARY)
            statusEdit.setSelection(project.STATUS_CODE.toInt())
            customerEdit.setText(project.CUSTOMER)
            chargeEdit.setText(project.CHARGE)
            reviewerEdit.setText(project.REVIEWER)
            if (project.RELEASE_DATE == null){
                releasedateEdit.setText("")
            }
            else {
                releasedateEdit.setText(DateFormat.format("yyyy/MM/dd", project.RELEASE_DATE))
            }
            remarksEdit.setText(project.REMARKS)
            startdateEdit.isEnabled = false
            projectnoEdit.isEnabled = false
            projectnameEdit.isEnabled = false
            protypeEdit.isEnabled = false
            delete.visibility = View.VISIBLE
        } else{
            delete.visibility = View.INVISIBLE
        }

        save.setOnClickListener {
            val messages: MutableList<String> = mutableListOf("")
            var message: String = ""
            if (startdateEdit.text.toString() == "") {
                messages.add("発生日を入力してください。")
            } else if (!startdateEdit.text.toString().matches("[0-9]{4}/[0-9]{2}/[0-9]{2}".toRegex())) {
                messages.add("発生日は yyyy/MM/dd の形式で入力してください。")
            } else if(!isDate(startdateEdit.text.toString())) {
                messages.add("発生日はカレンダーに存在しない日付です。")
            }
            if (projectnoEdit.text.toString() == "") {
                messages.add("案件番号を入力してください。")
            }
            if (projectnameEdit.text.toString() == "") {
                messages.add("案件名を入力してください。")
            }
            if (protypeEdit.selectedItemId.toInt() == 0) {
                messages.add("工程区分を選択してください。")
            }
            if (languageEdit.selectedItemId.toInt() == 0) {
                messages.add("開発言語を選択してください。")
            }
            if (summaryEdit.text.toString() == "") {
                messages.add("概要を入力してください。")
            }
            if (statusEdit.selectedItemId.toInt() == 0) {
                messages.add("状態を選択してください。")
            }
            if (releasedateEdit.text.toString() !=""){
                if (!releasedateEdit.text.toString().matches("[0-9]{4}/[0-9]{2}/[0-9]{2}".toRegex())) {
                    messages.add("リリース日は yyyy/MM/dd の形式で入力してください。")
                } else if(!isDate(releasedateEdit.text.toString())){
                    messages.add("リリース日はカレンダーに存在しない日付です。")
                }
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
                when (projectId) {
                    -1L -> {
                        realm.executeTransaction {
                            val maxprojectId = realm.where<Project>().max("PROJECT_ID")
                            val nextId = (maxprojectId?.toLong() ?: 0L) + 1
                            val project = realm.createObject<Project>(nextId)
                            startdateEdit.text.toString().toDate("yyyy/MM/dd")?.let {
                                project.START_DATE = it
                            }
                            project.PROJECT_NO = projectnoEdit.text.toString()
                            project.PROJECT_NAME = projectnameEdit.text.toString()
                            project.PROTYPE_CODE = protypeEdit.selectedItemId.toString()
                            project.LANGUAGE_CODE = languageEdit.selectedItemId.toString()
                            project.SUMMARY = summaryEdit.text.toString()
                            project.STATUS_CODE = statusEdit.selectedItemId.toString()
                            project.CUSTOMER = customerEdit.text.toString()
                            project.CHARGE = chargeEdit.text.toString()
                            project.REVIEWER = reviewerEdit.text.toString()
                            if (releasedateEdit.text.toString() !="") {
                                releasedateEdit.text.toString().toDate("yyyy/MM/dd")?.let {
                                    project.RELEASE_DATE = it
                                }
                            }
                            else{
                                project.RELEASE_DATE = null
                            }
                            project.REMARKS = remarksEdit.text.toString()
                        }
                        alert("追加しました") {
                            yesButton { finish() }
                        }.show()
                    }
                    else -> {
                        realm.executeTransaction {
                            val project = realm.where<Project>()
                                    .equalTo("PROJECT_ID", projectId).findFirst()
                            startdateEdit.text.toString().toDate("yyyy/MM/dd")?.let {
                                project?.START_DATE = it
                            }
                            project?.LANGUAGE_CODE = languageEdit.selectedItemId.toString()
                            project?.SUMMARY = summaryEdit.text.toString()
                            project?.STATUS_CODE = statusEdit.selectedItemId.toString()
                            project?.CUSTOMER = customerEdit.text.toString()
                            project?.CHARGE = chargeEdit.text.toString()
                            project?.REVIEWER = reviewerEdit.text.toString()
                            if (releasedateEdit.text.toString() !="") {
                                releasedateEdit.text.toString().toDate("yyyy/MM/dd")?.let {
                                    project?.RELEASE_DATE = it
                                }
                            }
                            else{
                                project?.RELEASE_DATE = null
                            }
                            project?.REMARKS = remarksEdit.text.toString()
                        }
                        alert("修正しました") {
                            yesButton { finish() }
                        }.show()
                    }
                }
            }
        }

        delete.setOnClickListener {
            realm.executeTransaction {
                realm.where<Project>().equalTo("PROJECT_ID", projectId)
                        ?.findFirst()
                        ?.deleteFromRealm()
            }
            alert("削除しました") {
                yesButton { finish() }
            }.show()
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        realm.close()
    }

    fun String.toDate(pattern: String = "yyyy/MM/dd HH:mm"): Date?{
        val sdFormat = try {
            SimpleDateFormat(pattern)
        }
        catch (e: IllegalArgumentException){
            null
        }
        val date = sdFormat?.let {
            try {
                it.parse(this)
            }
            catch (e: ParseException){
                null
            }
        }
        return date
    }

    private fun isDate(date: String): Boolean{
        try {
            val df = java.text.SimpleDateFormat("yyyy/MM/dd")
            df.isLenient = false // ←これで厳密にチェックしてくれるようになる
            val s2 = df.format(df.parse(date)) // ←df.parseでParseExceptionがThrowされる
            return true
        } catch (p: ParseException) {
            null
        }
        return false
    }

    private fun createInit() {
        if(realm.where<Projecttype>()
                        .equalTo("PROTYPE_CODE","1").findFirst() == null){
            realm.executeTransaction {
                val projecttype = realm.createObject<Projecttype>("1")
                projecttype.PROTYPE_NAME = "要件定義"
            }
        }
        if(realm.where<Projecttype>()
                        .equalTo("PROTYPE_CODE","2").findFirst() == null){
            realm.executeTransaction {
                val projecttype = realm.createObject<Projecttype>("2")
                projecttype.PROTYPE_NAME = "見積り"
            }
        }
        if(realm.where<Projecttype>()
                        .equalTo("PROTYPE_CODE","3").findFirst() == null){
            realm.executeTransaction {
                val projecttype = realm.createObject<Projecttype>("3")
                projecttype.PROTYPE_NAME = "基本設計"
            }
        }
        if(realm.where<Projecttype>()
                        .equalTo("PROTYPE_CODE","4").findFirst() == null){
            realm.executeTransaction {
                val projecttype = realm.createObject<Projecttype>("4")
                projecttype.PROTYPE_NAME = "詳細設計"
            }
        }
        if(realm.where<Projecttype>()
                        .equalTo("PROTYPE_CODE","5").findFirst() == null){
            realm.executeTransaction {
                val projecttype = realm.createObject<Projecttype>("5")
                projecttype.PROTYPE_NAME = "実装"
            }
        }
        if(realm.where<Projecttype>()
                        .equalTo("PROTYPE_CODE","6").findFirst() == null){
            realm.executeTransaction {
                val projecttype = realm.createObject<Projecttype>("6")
                projecttype.PROTYPE_NAME = "単体テスト"
            }
        }
        if(realm.where<Projecttype>()
                        .equalTo("PROTYPE_CODE","7").findFirst() == null){
            realm.executeTransaction {
                val projecttype = realm.createObject<Projecttype>("7")
                projecttype.PROTYPE_NAME = "結合テスト"
            }
        }
        if(realm.where<Projecttype>()
                        .equalTo("PROTYPE_CODE","8").findFirst() == null){
            realm.executeTransaction {
                val projecttype = realm.createObject<Projecttype>("8")
                projecttype.PROTYPE_NAME = "システムテスト"
            }
        }
        if(realm.where<Projecttype>()
                        .equalTo("PROTYPE_CODE","9").findFirst() == null){
            realm.executeTransaction {
                val projecttype = realm.createObject<Projecttype>("9")
                projecttype.PROTYPE_NAME = "最終テスト"
            }
        }
        if(realm.where<Language>()
                        .equalTo("LANGUAGE_CODE","1").findFirst() == null){
            realm.executeTransaction {
                val language = realm.createObject<Language>("1")
                language.LANGUAGE_NAME = "Java"
            }
        }
        if(realm.where<Language>()
                        .equalTo("LANGUAGE_CODE","2").findFirst() == null){
            realm.executeTransaction {
                val language = realm.createObject<Language>("2")
                language.LANGUAGE_NAME = "Python"
            }
        }
        if(realm.where<Language>()
                        .equalTo("LANGUAGE_CODE","3").findFirst() == null){
            realm.executeTransaction {
                val language = realm.createObject<Language>("3")
                language.LANGUAGE_NAME = "C++"
            }
        }
        if(realm.where<Language>()
                        .equalTo("LANGUAGE_CODE","4").findFirst() == null){
            realm.executeTransaction {
                val language = realm.createObject<Language>("4")
                language.LANGUAGE_NAME = "未定"
            }
        }
        if(realm.where<Status>()
                        .equalTo("STATUS_CODE","1").findFirst() == null){
            realm.executeTransaction {
                val status = realm.createObject<Status>("1")
                status.STATUS_NAME = "未着手"
            }
        }
        if(realm.where<Status>()
                        .equalTo("STATUS_CODE","2").findFirst() == null){
            realm.executeTransaction {
                val status = realm.createObject<Status>("2")
                status.STATUS_NAME = "作業中"
            }
        }
        if(realm.where<Status>()
                        .equalTo("STATUS_CODE","3").findFirst() == null){
            realm.executeTransaction {
                val status = realm.createObject<Status>("3")
                status.STATUS_NAME = "レビュー中"
            }
        }
        if(realm.where<Status>()
                        .equalTo("STATUS_CODE","4").findFirst() == null){
            realm.executeTransaction {
                val status = realm.createObject<Status>("4")
                status.STATUS_NAME = "指摘修正中"
            }
        }
        if(realm.where<Status>()
                        .equalTo("STATUS_CODE","5").findFirst() == null){
            realm.executeTransaction {
                val status = realm.createObject<Status>("5")
                status.STATUS_NAME = "再レビュー中"
            }
        }
        if(realm.where<Status>()
                        .equalTo("STATUS_CODE","6").findFirst() == null){
            realm.executeTransaction {
                val status = realm.createObject<Status>("6")
                status.STATUS_NAME = "完了"
            }
        }
    }

    private fun setPullDown(){
        val protypes = realm.where<Projecttype>().findAll()
        val protypelist : MutableList<String> = mutableListOf("")
        for (protype in protypes){
            protypelist.add(protype.PROTYPE_NAME)
        }
        val adapter1 = ArrayAdapter(applicationContext,android.R.layout.simple_spinner_item,protypelist)
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        protypeEdit.adapter = adapter1

        val statuses = realm.where<Status>().findAll()
        val statuslist : MutableList<String> = mutableListOf("")
        for (status in statuses){
            statuslist.add(status.STATUS_NAME)
        }
        val adapter2 = ArrayAdapter(applicationContext,android.R.layout.simple_spinner_item,statuslist)
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        statusEdit.adapter = adapter2

        val languages = realm.where<Language>().findAll()
        val languagelist : MutableList<String> = mutableListOf("")
        for (language in languages){
            languagelist.add(language.LANGUAGE_NAME)
        }
        val adapter3 = ArrayAdapter(applicationContext,android.R.layout.simple_spinner_item,languagelist)
        adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        languageEdit.adapter = adapter3
    }
}
