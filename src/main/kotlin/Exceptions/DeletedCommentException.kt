package Exceptions

class DeletedCommentException (message:String = "Comment with this id has been deleted") : RuntimeException(message) {
}