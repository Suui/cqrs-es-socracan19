data class Message(private val events: List<DomainEvent>) {

    private var deleted: Boolean = false

    init {
        events.forEach {
            when {
                it.javaClass == MessageDeleted::class.java -> deleted = true
            }
        }
    }

    fun delete() {
        if (deleted) return

        deleted = true
        eventBuffer.publish(MessageDeleted())
    }

    fun publish() {
        eventBuffer.publish(MessagePublished())
    }
}