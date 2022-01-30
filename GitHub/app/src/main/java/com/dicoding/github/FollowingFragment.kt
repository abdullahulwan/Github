package com.dicoding.github

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class FollowingFragment(username: String) : Fragment() {
    private val data = ArrayList<UserList>()

    private lateinit var progressBar: ProgressBar
    private lateinit var rvFollowing: RecyclerView
    private var username: String = username

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_following, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        rvFollowing = view.findViewById(R.id.rv_following)
        progressBar = view.findViewById(R.id.progress_bar_following)

        showLoading(true)
        rvFollowing.setHasFixedSize(true)
        val layoutManager = LinearLayoutManager(activity)
        rvFollowing.layoutManager = layoutManager
        val itemDecoration = DividerItemDecoration(activity, layoutManager.orientation)
        rvFollowing.addItemDecoration(itemDecoration)
        RequestData.getFollowingList(username, ::addUsernameList)
    }

    private fun addUsernameList(list: ArrayList<FollowResponseItem>): Unit{
        data.clear()
        list.forEach {
            val userFollow = UserList(it.login, it.avatarUrl)
            data.add(userFollow)
        }
        showRecyclerList()
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun showRecyclerList(){
        val listUserAdapter = ListUserAdapter(data)
        listUserAdapter.setOnItemClickCallback(object : ListUserAdapter.OnItemCilickCallback {
            override fun onItemClikced(data: String?) {
                showSelectedUser(data)
            }
        })
        showLoading(false)
        rvFollowing.adapter = listUserAdapter
    }

    private fun showLoading(isLoading: Boolean){
        if (isLoading){
            progressBar.visibility = View.VISIBLE
        }else{
            progressBar.visibility = View.GONE
        }
    }

    private fun showSelectedUser(user: String?){
        val moveToDetailUser = Intent(activity, DetailUser::class.java)
        moveToDetailUser.putExtra(DetailUser.EXTRA_USER, user)
        startActivity(moveToDetailUser)
    }

    companion object{
        var TAG = FollowingFragment::class.java.simpleName
    }
}