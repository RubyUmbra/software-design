package actor.message

import akka.actor.typed.ActorRef

import searchengine.SearchEngine

sealed class ChildMessage {
    data class Request(
        val engine: SearchEngine,
        val query: String,
        val respondTo: ActorRef<MasterMessage>,
    ) : ChildMessage()
}
