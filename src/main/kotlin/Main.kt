import Exceptions.*

fun main() {
    try {
        println(NoteService.add(Note("Homework","test text",0,0)))
        println(NoteService.add(Note("Homework2","test text2",1,1)))
        println(NoteService.add(Note("Homework3","test text3",2,2)))
        println(NoteService.add(Note("Homework4","test text4",3,3)))
        println(NoteService.createComment(Comment(1,"comment-1")))
        println(NoteService.createComment(Comment(1,"comment-2")))
        println(NoteService.createComment(Comment(2,"comment-3")))
        println(NoteService.createComment(Comment(3,"comment-4")))
        NoteService.edit(PareIdValue(1, Note("Hello", "kotlin",2,2)))
        println(NoteService.getById(1))
        println(NoteService.get("1,3"))
        NoteService.editComment(1,"ololo")
        println(NoteService.getComments(1))
        println(NoteService.delete(1))
        println(NoteService.commentDelete(3))
        println(NoteService.get("1"))
    } catch (e: NumberFormatException) {
        println("Invalid delimiter, please list the ID by \",\"")
    } catch (e: NoteNotFoundException) {
        println("Note(s) not found")
    }

}

object NoteService {
    private var noteId = 0
    private var commentId = 0
    private var deletedCommentId = 0
    private val noteList = mutableListOf<PareIdValue<Int,Note>>()
    private val commentList = mutableListOf<PareIdValue<Int,Comment>>()
    private val deletedNotes = mutableListOf<PareIdValue<Int,Note>>()
    private val deletedComments = mutableListOf<PareIdValue<Int,Comment>>()


    fun add (note: Note): Int? {
        if (note.privacy < 0 || note.privacy > 3) throw WrongNotePrivacyException("Wrong privacy value")
        if (note.commentPrivacy < 0 || note.commentPrivacy > 3) throw WrongCommentPrivacyException("Wrong comment privacy value")
        noteId++
        noteList.add(PareIdValue(noteId, note))
        return noteList[noteId-1].id
    }

    fun createComment (comment: Comment): Int? {
        for (note in noteList){
            if (note.id == comment.noteId) {
                commentId++
                commentList.add(PareIdValue(commentId, comment))
                return commentList[commentId-1].id
            }

        }
        throw NoteNotFoundException ()
    }

    fun edit (newNote: PareIdValue<Int,Note>): Boolean{
        for (note in noteList){
            if (note.id == newNote.id) {
                noteList[newNote.id!! -1] = newNote
                return true
            }
        }
        throw NoteNotFoundException ()
    }

    fun editComment (commentId: Int, message: String): Boolean {
        for (comment in commentList){
            if (comment.id == commentId) {
                commentList[commentId-1].value!!.message = message
                return true
            }
        }
        throw CommentNotFoundException ()
    }

    fun getById(noteId:Int): PareIdValue<Int,Note> {
        for (note in noteList) {
            if (note.id == noteId) {
                return note
            }
        }
        throw NoteNotFoundException ()
    }

    fun getComments (noteId: Int): MutableList<PareIdValue<Int,Comment>> {
        val noteComments = mutableListOf<PareIdValue<Int,Comment>>()
        for (note in noteList) {
            if (note.id == noteId) {
                for (comment in commentList){
                    if (comment.value!!.noteId == noteId) {
                        noteComments.add(comment)
                    }
                }
                if (noteComments.isEmpty()) throw CommentNotFoundException ()
                return noteComments
            }
        }
        throw NoteNotFoundException ()
    }

    fun get(noteIds: String): MutableList<PareIdValue<Int,Note>> {
        val noteIdsStringList = noteIds.split(",")
        val noteIdsIntList = mutableListOf<Int>()
        val noteListGet = mutableListOf<PareIdValue<Int,Note>>()
        for (string in noteIdsStringList) noteIdsIntList.add(string.toInt())
        for (id in noteIdsIntList) {
            for (note in noteList) {
                if (note.id == id) noteListGet.add(note)
            }

        }
        if (noteListGet.isEmpty()) throw NoteNotFoundException ()
        return noteListGet
    }

    fun delete (noteId: Int): Boolean {
        for (note in noteList) {
            if (note.id == noteId) {
                for (comment in commentList) {
                    if (comment.value!!.noteId == noteId) {
                        deletedCommentId++
                        commentList[comment.id!! -1] = PareIdValue(id = null, value = null)
                        deletedComments.add(comment)
                    }
                }
                noteList[note.id!! -1] = PareIdValue(id = null,value = null)
                deletedNotes.add(note)
                return true
            }
        }
        throw NoteNotFoundException()
    }

    fun commentDelete (commentId: Int): Boolean {
        for (comment in commentList) {
            if (comment.id == commentId) {
                commentList[comment.id!! -1] = PareIdValue(id = null, value = null)
                deletedComments.add(comment)
                return true
            }
        }
        throw CommentNotFoundException()
    }

    fun restoreComment (commentId: Int): Boolean {
        for (comment in deletedComments) {
            if (comment.id == commentId) {
                deletedComments[comment.id!! -1] = PareIdValue(id = null, value = null)
                commentList[commentId-1] = comment
                return true
            }
        }
        throw CommentNotFoundException()
    }
}