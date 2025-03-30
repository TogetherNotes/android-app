import java.util.*

class Message(
    val id: Int,
    val senderId: Int,
    val content: String,
    val sendAt: Date,
    val isRead: Boolean,
    val chatId: Int
)