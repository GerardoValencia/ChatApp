package com.example.chatapp

import android.R
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.chatapp.fragments.Chats
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.util.UUID


class ListOfChatsActivity : AppCompatActivity() {
    private var user = ""

    private var db = Firebase.firestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_of_chats)

        intent.getStringExtra("user")?.let { user = it }

        val bundle = Bundle()
        val myMessage = "Stackoverflow is cool!"
        bundle.putString("message", myMessage)
        val fragInfo = Chats()
        fragInfo.setArguments(bundle)
        transaction.replace(R.id.fragment_chats, fragInfo)
        transaction.commit()


        if (user.isNotEmpty()) {
            initViews()
        }
    }

    private fun initViews() {
        newChatButton.setOnClickListener { newChat() }

        listChatsRecyclerView.layoutManager = LinearLayoutManager(this)
        listChatsRecyclerView.adapter =
            ChatAdapter { chat ->
                chatSelected(chat)
            }

        val userRef = db.collection("users").document(user)

        userRef.collection("chats")
            .get()
            .addOnSuccessListener { chats ->
                val listChats = chats.toObjects(Chat::class.java)

                (listChatsRecyclerView.adapter as ChatAdapter).setData(listChats)
            }

        userRef.collection("chats")
            .addSnapshotListener { chats, error ->
                if(error != null){
                    chats?.let {
                        val listChats = it.toObjects(Chat::class.java)

                        (listChatsRecyclerView.adapter as ChatAdapter).setData(listChats)
                    }
                }
            }
    }

    private fun chatSelected(chat: Chat) {
        val intent = Intent(this, ChatActivity::class.java)
        intent.putExtra("chatid", chat.id)
        intent.putExtra("user", user)
        startActivity(intent)
    }

    private fun newChat() {
        val chatId = UUID.randomUUID().toString()
        val otherUser = newChatText.text.toString()
        val users = listOf(user, otherUser)

        val chat = Chat(
            id = chatId,
            name = "Chat con $otherUser",
            users = users
        )

        db.collection("chats").document(chatId).set(chat)
        db.collection("chats").document(user).collection("chats").document(chatId).set(chat)
        db.collection("chats").document(otherUser).collection("chats").document(chatId).set(chat)


        val intent = Intent(this, ChatActivity::class.java)
        intent.putExtra("chatid", chatid)
        intent.putExtra("user", user)
        startActivity(intent)
    }
}