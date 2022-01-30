package com.dicoding.github

import android.graphics.Color
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class ListUserAdapter(private val listUser : ArrayList<UserList>) : RecyclerView.Adapter<ListUserAdapter.ListViewHolder>(){

    private lateinit var onItemClickCallback: OnItemCilickCallback


    fun  setOnItemClickCallback(onItemCallback : OnItemCilickCallback){
        this.onItemClickCallback = onItemCallback
    }
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ListViewHolder {
        val view: View = LayoutInflater.from(viewGroup.context).inflate(R.layout.item_row, viewGroup, false)
        return ListViewHolder(view)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val (name, photo) = listUser[position]
        Glide.with(holder.itemView.context)
            .load(photo)
            .placeholder(R.drawable.logo)
            .error(Glide.with(holder.itemView.context).load(R.drawable.logo))
            .circleCrop()
            .into(holder.imgPhto)
        holder.tvname.text = name
        holder.itemView.setOnClickListener { onItemClickCallback.onItemClikced(listUser[holder.adapterPosition].name)}
    }

    override fun getItemCount(): Int = listUser.size

    class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var imgPhto: ImageView = itemView.findViewById(R.id.img_item_photo)
        var tvname: TextView = itemView.findViewById(R.id.tv_item_name)
    }

    interface OnItemCilickCallback{
        fun  onItemClikced(data: String?)
    }
}