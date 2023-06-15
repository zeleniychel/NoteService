import Exceptions.CommentNotFoundException
import Exceptions.NoteNotFoundException
import Exceptions.WrongCommentPrivacyException
import Exceptions.WrongNotePrivacyException

fun main() {
    val note = Note("Homework","test text",0,0)
    val note2 = Note("Homework2","test text2",1,1)
    val note3 = Note("Homework3","test text3",2,2)
    val note4 = Note("Homework4","test text4",3,3)
    println(NoteService.add(note))
    println(NoteService.add(note2))
    println(NoteService.add(note3))
    println(NoteService.add(note4))
    println(NoteService.noteList[0])
    println(NoteService.noteList[1])
    println(NoteService.noteList[2])
    println(NoteService.noteList[3])
    println(NoteService.createComment(Comment(1,"lalala")))
    NoteService.edit(PareIdValue(1, Note("Hello", "kotlin",2,2)))
    println(NoteService.noteList)
    println(NoteService.commentList)
    NoteService.editComment(1,"ololo")
    println(NoteService.commentList)

}

object NoteService {
    private var noteId = 0
    private var commentId = 0
    val noteList = mutableListOf<PareIdValue<Int,Note>>()
    val commentList = mutableListOf<PareIdValue<Int,Comment>>()


    fun add (note: Note): Int {
        if (note.privacy < 0 || note.privacy > 3) throw WrongNotePrivacyException("Wrong privacy value")
        if (note.commentPrivacy < 0 || note.commentPrivacy > 3) throw WrongCommentPrivacyException("Wrong comment privacy value")
        noteId++
        noteList.add(PareIdValue(noteId, note))
        return noteList[noteId-1].id
    }

    fun createComment (comment: Comment): Int {
        for (id in noteList.withIndex()){
            if (noteList[id.index].id == comment.noteId) {
                commentId++
                commentList.add(PareIdValue(commentId, comment))
                return commentList[commentId-1].id
            }

        }
        throw NoteNotFoundException ("Note not found")
    }

    fun edit (pareIdValue: PareIdValue<Int,Note>): Boolean{
        for (note in noteList.withIndex()){
            if (noteList[note.index].id == pareIdValue.id) {
                noteList[pareIdValue.id-1] = pareIdValue
                return true
            }
        }
        throw NoteNotFoundException ("Note not found")
    }

    fun editComment (commentId: Int, message: String): Boolean {
        for (comment in commentList.withIndex()){
            if (commentList[comment.index].id == commentId) {
                commentList[commentId-1].value.message = message
                return true
            }
        }
        throw CommentNotFoundException ("Comment not found")
    }
    
}