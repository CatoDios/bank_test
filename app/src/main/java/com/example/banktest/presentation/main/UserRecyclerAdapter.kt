package com.example.banktest.presentation.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.banktest.databinding.UserCardBinding
import com.example.banktest.utils.ListItemClickListener
import com.example.domain.User
import kotlin.properties.Delegates

class UserRecyclerAdapter(private val itemClickListener: ListItemClickListener<User>) : RecyclerView.Adapter<UserRecyclerAdapter.UserHolder>()  {

    var itemList: List<User> by Delegates.observable(emptyList()) { _, _, _ ->
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): UserHolder {
        val itemBinding = UserCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return UserHolder(itemBinding)
    }

    override fun getItemCount() = itemList.size


    override fun onBindViewHolder(holder: UserHolder, position: Int) {
        val itemUser = itemList[position]
        holder.bindUser(itemUser)
        holder.itemView.setOnClickListener { itemClickListener(itemUser) }
    }

    class UserHolder(private val itemBinding: UserCardBinding) : RecyclerView.ViewHolder(itemBinding.root){
        private var user: User? = null

        fun bindUser(user: User) {
            this.user = user
            itemBinding.itemDate.text = user.name
            itemBinding.itemDescription.text = "${user.address.street} ${user.address.city}"
        }
    }

}
