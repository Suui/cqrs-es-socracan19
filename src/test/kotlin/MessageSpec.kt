import io.kotlintest.matchers.collections.shouldBeEmpty
import io.kotlintest.matchers.types.shouldBeTypeOf
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

interface DomainEvent

class MessagePublished : DomainEvent

class MessageDeleted : DomainEvent

val eventBuffer = EventBuffer()


class MessageSpec {

    @BeforeEach
    fun `clean event queue`() {
        eventBuffer.flush()
    }

    @Test
    fun `raises a MessagePublished after tweeting a message`() {
        PublishMessage().execute()

        eventBuffer.queue.first().shouldBeTypeOf<MessagePublished>()
    }

    @Test
    fun `raises a MessageDeleted event after deleting a message`() {
        DeleteMessage().execute(Message(listOf(
            MessagePublished()
        )))

        eventBuffer.queue.last().shouldBeTypeOf<MessageDeleted>()
    }

    @Test
    fun `does not raise a MessageDeleted event after deleting an already deleted message`() {
        DeleteMessage().execute(Message(listOf(
            MessagePublished(),
            MessageDeleted()
        )))

        eventBuffer.queue.shouldBeEmpty()
    }
}
