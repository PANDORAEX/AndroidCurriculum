package com.ines.myproject2

import android.text.format.DateFormat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import io.realm.OrderedRealmCollection
import io.realm.Realm
import io.realm.RealmBaseAdapter

class ProjectAdapter(data: OrderedRealmCollection<Project>?) : RealmBaseAdapter<Project>(data) {

    private lateinit var realm: Realm

    inner class ViewHolder(cell: View){
        val startdate = cell.findViewById<TextView>(android.R.id.text1)
        val projectname = cell.findViewById<TextView>(android.R.id.text2)
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view: View
        val viewHolder: ViewHolder
        realm = Realm.getDefaultInstance()

        when(convertView){
            null -> {
                val inflater = LayoutInflater.from(parent?.context)
                view = inflater.inflate(android.R.layout.simple_list_item_2,parent,false)
                viewHolder = ViewHolder(view)
                view.tag = viewHolder
            }
            else -> {
                view = convertView
                viewHolder = view.tag as ViewHolder
            }
        }

        adapterData?.run {
            val project = get(position)
            viewHolder.startdate.text = DateFormat.format("yyyy/MM/dd", project.START_DATE)
            viewHolder.projectname.text = project.PROJECT_NAME
        }
        return view
    }
}