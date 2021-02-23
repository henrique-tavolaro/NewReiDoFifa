package com.henriquetavolaro.newreidofifa.ui.activities.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.henriquetavolaro.newreidofifa.R
import com.henriquetavolaro.newreidofifa.ui.firebase.FirestoreClass
import com.henriquetavolaro.newreidofifa.ui.models.Games

class GamesAdapter(
    options: FirestoreRecyclerOptions<Games>,
    var clickListener: OnItemClickListener
) : FirestoreRecyclerAdapter<Games, GamesAdapter.GameViewHolder>(options) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GameViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.adapter_result, parent, false)
        return GameViewHolder(view)
    }

    override fun onBindViewHolder(holder: GameViewHolder, position: Int, games: Games) {
        holder.initialize(games, clickListener)
    }

    inner class GameViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun initialize (games: Games, action: OnItemClickListener){
            val result1 : TextView = itemView.findViewById(R.id.tv_adapter_result_1)
            val result2 : TextView = itemView.findViewById(R.id.tv_adapter_result_2)
            val date : TextView = itemView.findViewById(R.id.tv_adapter_date)


            result1.text = games.resultP1
            result2.text = games.resultP2
            date.text = games.date
            itemView.setOnClickListener {
                action.onItemClicked(games, adapterPosition)
            }
        }
    }

    interface OnItemClickListener {
        fun onItemClicked(games: Games, position: Int)
    }
}