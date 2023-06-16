package Exceptions

class DeletedNoteException(message:String = "Note with this id has been deleted") : RuntimeException(message) {
}