package Exceptions

class NoteNotFoundException (message:String = "Note(s) not found") : RuntimeException(message) {
}