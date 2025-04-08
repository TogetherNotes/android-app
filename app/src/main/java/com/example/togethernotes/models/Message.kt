import java.util.*

data class Message(
    val id: Int,
    val senderId: Int,
    val content: String,
    val sendAt: Date,
    var isRead: Boolean,
    val chatId: Int
)