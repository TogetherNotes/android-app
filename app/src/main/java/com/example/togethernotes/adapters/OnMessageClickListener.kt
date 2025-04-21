package com.example.togethernotes.adapters

import Message

interface OnMessageClickListener {
    fun onAcceptButtonClick(message: Message, position: Int)
    fun onDiscardButtonClick(message: Message, position: Int)
}