package gnutella.handlers

import gnutella.messages.Message

abstract class MessageHandler(private val message: Message) : Runnable