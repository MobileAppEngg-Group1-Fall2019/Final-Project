package com.group_01.finalproject.ui.notifications

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import com.group_01.finalproject.R
import com.group_01.finalproject.db.DBContract
import com.group_01.finalproject.db.DbBitmapUtil
import com.group_01.finalproject.db.ImageModel


class PhotoAdapter(context: Context, private val resource: Int, private val itemList: ArrayList<ImageModel>?) : ArrayAdapter<PhotoAdapter.ItemHolder>(context, resource) {

    override fun getCount(): Int {
        return if (this.itemList != null) this.itemList.size else 0
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var convertView = convertView

        val holder: ItemHolder
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(resource, null)
            holder = ItemHolder()
            holder.name = convertView!!.findViewById(R.id.photo_grid_element_textView)
            holder.photo = convertView.findViewById(R.id.photo_grid_element_photo)
            convertView.tag = holder
        } else {
            holder = convertView.tag as ItemHolder
        }

        holder.name!!.text = this.itemList!![position].lastModified.toString()
        holder.photo!!.setImageBitmap(DbBitmapUtil.getImage(this.itemList!![position].data))

        return convertView
    }

    class ItemHolder {
        var name: TextView? = null
        var photo: ImageView? = null
    }

}