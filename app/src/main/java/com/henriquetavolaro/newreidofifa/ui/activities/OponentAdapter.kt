package com.henriquetavolaro.newreidofifa.ui.activities

import android.content.Context
import android.provider.ContactsContract
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Adapter
import android.widget.ImageView
import android.widget.ListAdapter
import android.widget.TextView
import androidx.compose.ui.viewinterop.viewModel
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.henriquetavolaro.newreidofifa.R
import com.henriquetavolaro.newreidofifa.ui.home.HomeFragment
import com.henriquetavolaro.newreidofifa.ui.models.User

class OponentAdapter(
    options: FirestoreRecyclerOptions<User>,
    private val context: Context,
    var clickListener: OnItemClickListener
) : FirestoreRecyclerAdapter<User, OponentAdapter.UserViewHolder>(options) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.oponent_adapter, parent, false)
        return UserViewHolder(view)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int, user: User) {
//        val tvName: TextView = holder.itemView.findViewById(R.id.tv_recycler_oponent)
//        val ivImageView: ImageView = holder.itemView.findViewById(R.id.iv_recyclerview_oponent)
//
//        Glide
//            .with(context)
//            .load(user.image)
//            .centerCrop()
//            .placeholder(R.drawable.ic_user_place_holder)
//            .into(ivImageView)
//
//        tvName.text = user.name
        holder.initialize(user, clickListener)

    }

    inner class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun initialize (user: User, action: OnItemClickListener){
            val tvName: TextView = itemView.findViewById(R.id.tv_recycler_oponent)
            val ivImageView: ImageView = itemView.findViewById(R.id.iv_recyclerview_oponent)

            Glide
                .with(context)
                .load(user.image)
                .centerCrop()
                .placeholder(R.drawable.ic_user_place_holder)
                .into(ivImageView)

            tvName.text = user.name

            itemView.setOnClickListener {
                action.onItemClicked(user, adapterPosition)
            }
        }

    }

    interface OnItemClickListener {
        fun onItemClicked(user: User, position: Int)
    }
}

