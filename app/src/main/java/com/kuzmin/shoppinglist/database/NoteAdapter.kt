package com.kuzmin.shoppinglist.database

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.kuzmin.shoppinglist.R
import com.kuzmin.shoppinglist.databinding.NoteListItemBinding
import com.kuzmin.shoppinglist.entities.NoteItem
import com.kuzmin.shoppinglist.utils.HtmlManager

class NoteAdapter(private val listener : Listener)
    : ListAdapter<NoteItem, NoteAdapter.ItemHolder>(ItemCompare()) {

    interface Listener{
        fun deleteItem(id: Int)
        fun onClickItem(note: NoteItem)
    }
    // Заполняем носитель (RecyclerView) данными из класса NoteItem
    class ItemHolder(view : View) : RecyclerView.ViewHolder(view){
        private val binding = NoteListItemBinding.bind(view)

        fun setData(note : NoteItem, listener: Listener) = with(binding){
            textViewTitle.text = note.title
            textViewDescription.text = HtmlManager.getFromHTML(note.content).trim()
            textViewTime.text = note.time
            itemView.setOnClickListener {
                listener.onClickItem(note)
            }
            imageButtonDelete.setOnClickListener {
                listener.deleteItem(note.id!!)
            }
        }

        companion object{
            fun create(parent : ViewGroup): ItemHolder{
                return ItemHolder(LayoutInflater.from(parent.context)
                    .inflate(R.layout.note_list_item, parent, false))
            }
        }
    }
    // Класс, обновляющий список заметок (?)
    class ItemCompare : DiffUtil.ItemCallback<NoteItem>(){
        override fun areItemsTheSame(oldItem: NoteItem, newItem: NoteItem): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: NoteItem, newItem: NoteItem): Boolean {
            return oldItem == newItem
        }
    }




    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {
        return ItemHolder.create(parent)
    }

    override fun onBindViewHolder(holder: ItemHolder, position: Int) {
        holder.setData(getItem(position), listener)
    }







}