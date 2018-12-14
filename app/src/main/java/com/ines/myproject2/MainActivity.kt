package com.ines.myproject2

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import io.realm.Realm
import io.realm.kotlin.where

import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import org.jetbrains.anko.startActivity

class MainActivity : AppCompatActivity() {

    private lateinit var realm: Realm

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        realm = Realm.getDefaultInstance()
        val projects = realm.where<Project>().findAll()
        listView.adapter = ProjectAdapter(projects)
        fab.setOnClickListener { view ->
            startActivity<ProjectEditActivity>()
        }

        listView.setOnItemClickListener { parent, view, position, id ->
            val project = parent.getItemAtPosition(position) as Project
            startActivity<ProjectEditActivity>(
                    "project_id" to project.PROJECT_ID)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        realm.close()
    }
}
