package actor

import akka.actor.typed.javadsl.AbstractBehavior
import akka.actor.typed.javadsl.ActorContext
import akka.actor.typed.javadsl.Receive

import actor.message.ChildMessage
import actor.message.MasterMessage

class ChildActor(
    context: ActorContext<ChildMessage>,
) : AbstractBehavior<ChildMessage>(context) {
    override fun createReceive(): Receive<ChildMessage> =
        newReceiveBuilder()
            .onMessage(ChildMessage.Request::class.java) {
                it.respondTo.tell(
                    MasterMessage.Response(
                        name = "${it.engine}",
                        response = it.engine.search(it.query),
                    )
                )
                this
            }.build()
}
