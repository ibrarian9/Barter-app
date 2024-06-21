package com.app.barterbuddy.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.app.barterbuddy.databinding.ListChatBinding
import com.app.barterbuddy.databinding.ListChatUserBinding
import com.app.barterbuddy.di.models.ChatMessage
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter

class ChatAdapter(private val currentUserId: String): ListAdapter<ChatMessage, RecyclerView.ViewHolder>(DIFF_CALLBACK) {

    override fun getItemViewType(position: Int): Int {
        return if (getItem(position).senderId == currentUserId) TYPE_ME else TYPE_USER
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == TYPE_ME) {
            val binding = ListChatBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            MeChatViewHolder(binding)
        } else {
            val binding = ListChatUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            UserChatViewHolder(binding)
        }
    }

    class UserChatViewHolder(val binding: ListChatUserBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(chatMessage: ChatMessage) {
            binding.userChat.text = chatMessage.message
            binding.time.text = formatTime(chatMessage.timestamp)
        }

        private fun formatTime(timestamp: String): String {
            val dateTime = OffsetDateTime.parse(timestamp)
            val timeFormatter = DateTimeFormatter.ofPattern("HH.mm")
            return dateTime.format(timeFormatter)
        }
    }

    class MeChatViewHolder(val binding: ListChatBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(chatMessage: ChatMessage) {
            binding.meChat.text = chatMessage.message
            binding.time.text = formatTime(chatMessage.timestamp)
        }

        private fun formatTime(timestamp: String): String {
            val dateTime = OffsetDateTime.parse(timestamp)
            val timeFormatter = DateTimeFormatter.ofPattern("HH.mm")
            return dateTime.format(timeFormatter)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val chatMessage = getItem(position)
        if (holder is MeChatViewHolder) {
            holder.bind(chatMessage)
        } else if (holder is UserChatViewHolder) {
            holder.bind(chatMessage)
        }
    }

    companion object {
        private const val TYPE_ME = 1
        private const val TYPE_USER = 2

        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ChatMessage>() {
            override fun areItemsTheSame(oldItem: ChatMessage, newItem: ChatMessage): Boolean {
                return oldItem.timestamp == newItem.timestamp
            }

            override fun areContentsTheSame(oldItem: ChatMessage, newItem: ChatMessage): Boolean {
                return oldItem == newItem
            }
        }
    }
}
