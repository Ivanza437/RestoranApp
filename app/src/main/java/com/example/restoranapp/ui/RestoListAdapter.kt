package com.example.restoranapp.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView.OnItemClickListener
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.restoranapp.R
import com.example.restoranapp.model.Resto

class RestoListAdapter(
    private val onItemClickListener: (Resto) -> Unit
): ListAdapter<Resto, RestoListAdapter.RestoViewHolder>(WORDS_COMPARATOR) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RestoViewHolder {
        return RestoViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: RestoViewHolder, position: Int) {
        val resto = getItem(position)
        holder.bind(resto)
        holder.itemView.setOnClickListener {
            onItemClickListener(resto)
        }

    }
    class RestoViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        private val nameTextView : TextView = itemView.findViewById(R.id.nameTextView)
        private val kindTextView : TextView = itemView.findViewById(R.id.kindTextView)
        private val priceTextView : TextView = itemView.findViewById(R.id.priceTextView)

        fun bind(resto: Resto?) {
            nameTextView.text = resto?.name
            kindTextView.text = resto?.kind
            priceTextView.text = resto?.price

        }

        companion object {
            fun create(parent: ViewGroup): RestoListAdapter.RestoViewHolder {
                val view: View = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_resto, parent, false)
                return RestoViewHolder(view)
            }
        }
    }

    companion object{
        private val WORDS_COMPARATOR = object :DiffUtil.ItemCallback<Resto>(){
            override fun areItemsTheSame(oldItem: Resto, newItem: Resto): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: Resto, newItem: Resto): Boolean {
                return oldItem.id == newItem.id

            }
        }

    }
}