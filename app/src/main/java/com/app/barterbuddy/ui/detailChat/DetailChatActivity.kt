package com.app.barterbuddy.ui.detailChat

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.barterbuddy.R
import com.app.barterbuddy.ViewModelsFactory
import com.app.barterbuddy.adapter.ChatAdapter
import com.app.barterbuddy.databinding.ActivityDetailChatBinding
import com.app.barterbuddy.di.models.AddChat
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.auth
import kotlinx.coroutines.launch

class DetailChatActivity : AppCompatActivity() {

    private lateinit var adapter: ChatAdapter
    private lateinit var auth: FirebaseUser
    private lateinit var binding: ActivityDetailChatBinding
    private val detailChatViewModel by viewModels<DetailChatViewModel> {
        ViewModelsFactory.getInstance()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailChatBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        auth = Firebase.auth.currentUser!!
        val userId = intent.getStringExtra("userId")

        adapter = ChatAdapter(auth.uid)
        binding.rvChat.layoutManager = LinearLayoutManager(this@DetailChatActivity)
        binding.rvChat.adapter = adapter

        lifecycleScope.launch {
            fetchingChat(auth.uid, userId!!)
        }

        binding.back.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        binding.btnSend.setOnClickListener {
            val dataMsg = binding.edMsg.text.toString()

            when {
                dataMsg.isEmpty() -> messageToast("Ketikkan Sesuatu...")

                else -> {
                    val data = AddChat(
                        senderId = auth.uid,
                        receiverId = userId!!,
                        message = dataMsg
                    )

                    lifecycleScope.launch {
                        detailChatViewModel.postChat(data).observe(this@DetailChatActivity){
                            if (it.success){
                                messageToast("Pesan Berhasil Di kirim")
                                binding.edMsg.text.clear()
                                lifecycleScope.launch {
                                    fetchingChat(auth.uid, userId)
                                }
                            } else {
                                messageToast("Pesan Gagal Di kirim")
                            }
                        }
                    }
                }
            }
        }
    }

    private suspend fun fetchingChat(uid: String, userId: String) {
        detailChatViewModel.getListChat(uid, userId).observeForever{
            adapter.submitList(it.messages)
        }
    }

    private fun messageToast(s: String) {
        Toast.makeText(this@DetailChatActivity, s, Toast.LENGTH_SHORT).show()
    }
}