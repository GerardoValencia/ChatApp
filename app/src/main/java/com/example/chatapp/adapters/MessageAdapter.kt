package com.example.chatapp.adapters

import android.os.Message
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.auth.User

class MessageAdapter(private val user: String): RecyclerView.Adapter<MessageAdapter.MessageViewHolder>() {

    private var messages: List<Message> = emptyList()

    fun setData(list: List<Message>){
        messages = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MessageViewHolder {
        return MessageViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_message,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
        val message = messages[position]

        if(user == message.from){
            holder.itemView.myMessageLayout.visibility = View.VISIBLE
            holder.itemView.otherMessageLayout.visibility = View.GONE

            holder.itemView.otherMessageTextView.text = message.message
        } else {
            holder.itemView.myMessageLayout.visibility = View.GONE
            holder.itemView.otherMessageLayout.visibility = View.VISIBLE

            holder.itemView.otherMessageTextView.text = message.message
        }
    }

    override fun getItemCount(): Int {
        return messages.size
    }

    class MessageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}