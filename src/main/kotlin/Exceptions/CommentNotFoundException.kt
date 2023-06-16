package Exceptions

class CommentNotFoundException (message:String = "Comment(s) not found") : RuntimeException(message) {
}