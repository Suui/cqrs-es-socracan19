class EventBuffer {

    val queue: MutableList<DomainEvent> = mutableListOf()

    fun publish(event: DomainEvent) {
        queue.add(event)
    }

    fun flush() {
        queue.removeAll { true }
    }

}