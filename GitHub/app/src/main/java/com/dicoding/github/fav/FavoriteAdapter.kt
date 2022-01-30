package com.dicoding.github.fav

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dicoding.github.DetailUser
import com.dicoding.github.R
import com.dicoding.github.databinding.ItemRowBinding
import com.dicoding.github.fav.db.UserFav
import com.dicoding.github.fav.helper.UserDiffCallback

class FavoriteAdapter : RecyclerView.Adapter<FavoriteAdapter.UserViewHolder>() {

    private val listUser = ArrayList<UserFav>()

    fun setListUsers(listNotes: List<UserFav>) {
        val diffCallback = UserDiffCallback(this.listUser, listNotes)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        this.listUser.clear()
        this.listUser.addAll(listNotes)
        diffResult.dispatchUpdatesTo(this)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val binding = ItemRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return UserViewHolder(binding)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        holder.bind(listUser[position])
    }

    override fun getItemCount(): Int {
        return listUser.size
    }

    inner class UserViewHolder(private val binding: ItemRowBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(user: UserFav) {
            with(binding) {
                Glide.with(itemView.context)
                    .load(user.avUrl)
                    .placeholder(R.drawable.logo)
                    .error(Glide.with(itemView.context).load(R.drawable.logo))
                    .circleCrop()
                    .into(imgItemPhoto)
                tvItemName.text = user.username
                itemView.setOnClickListener {
                    val moveToDetailUser = Intent(it.context, DetailUser::class.java)
                    moveToDetailUser.putExtra(DetailUser.EXTRA_USER, user.username)
                    it.context?.startActivity(moveToDetailUser)
                }
            }
        }
    }

}