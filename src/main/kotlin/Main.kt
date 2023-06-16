import Exceptions.*

fun main() {
}

object NoteService {
    private var noteIds = 0
    private var commentIds = 0
    private val notesList = mutableListOf<PareIdValue<Int, Note>>()
    private val commentsList = mutableListOf<PareIdValue<Int, Comment>>()
    private val deletedNotes = mutableListOf<PareIdValue<Int, Note>>()
    private val deletedComments = mutableListOf<PareIdValue<Int, Comment>>()


    fun add(note: Note): Int {
        if (note.privacy < 0 || note.privacy > 3) throw WrongNotePrivacyException()
        if (note.commentPrivacy < 0 || note.commentPrivacy > 3) throw WrongCommentPrivacyException()
        noteIds++
        notesList.add(PareIdValue(noteIds, note))
        return noteIds
    }

    fun createComment(comment: Comment): Int {
        for (note in notesList) {
            if (note.id == comment.noteId) {
                commentIds++
                commentsList.add(PareIdValue(commentIds, comment))
                return commentIds
            }

        }
        throw NoteNotFoundException()
    }

    fun edit(newNote: PareIdValue<Int, Note>): Boolean {
        if (newNote.value!!.privacy < 0 || newNote.value!!.privacy > 3) throw WrongNotePrivacyException()
        if (newNote.value!!.commentPrivacy < 0 || newNote.value!!.commentPrivacy > 3) throw WrongCommentPrivacyException()
        if (deleteNoteCheck(newNote.id!!)) {
            for ((index, note) in notesList.withIndex()) {
                if (note.id == newNote.id) {
                    notesList[index] = newNote
                    return true
                }
            }
        }
        throw NoteNotFoundException()
    }

    fun editComment(commentId: Int, message: String): Boolean {
        if (deleteCommentCheck(commentId)) {
            for ((index, comment) in commentsList.withIndex()) {
                if (comment.id == commentId) {
                    commentsList[index].value?.message = message
                    return true
                }
            }
        }
        throw CommentNotFoundException()
    }

    fun getById(noteId: Int): PareIdValue<Int, Note> {
        if (deleteNoteCheck(noteId)) {
            for (note in notesList) {
                if (note.id == noteId) {
                    return note
                }
            }
        }
        throw NoteNotFoundException()
    }

    fun getComments(noteId: Int): MutableList<PareIdValue<Int, Comment>> {
        if (deleteNoteCheck(noteId)) {
            val noteComments = mutableListOf<PareIdValue<Int, Comment>>()
            for (note in notesList) {
                if (note.id == noteId) {
                    for (comment in commentsList) {
                        if (comment.value?.noteId == noteId) {
                            noteComments.add(comment)
                        }
                    }
                    if (noteComments.isEmpty()) throw CommentNotFoundException()
                    return noteComments
                }
            }
        }
        throw NoteNotFoundException()
    }

    fun get(noteIds: String): MutableList<PareIdValue<Int, Note>> {
        val noteIdsStringList = noteIds.split(",")
        val noteIdsIntList = mutableListOf<Int>()
        val noteListGet = mutableListOf<PareIdValue<Int, Note>>()
        for (string in noteIdsStringList) noteIdsIntList.add(string.toInt())
        for (id in noteIdsIntList) {
            if (deleteNoteCheck(id)) {
                for (note in notesList) {
                    if (note.id == id) noteListGet.add(note)
                }
            }
            if (noteListGet.isEmpty()) throw NoteNotFoundException()
        }
        return noteListGet
    }

    fun delete(noteId: Int): Boolean {
        if (deleteNoteCheck(noteId)) {
            for ((indexN, note) in notesList.withIndex()) {
                if (note.id == noteId) {
                    for ((indexC, comment) in commentsList.withIndex()) {
                        if (comment.value?.noteId == noteId) {
                            deletedComments.add(comment)
                            commentsList[indexC] = PareIdValue(id = null, value = null)

                        }
                    }
                    notesList[indexN] = PareIdValue(id = null, value = null)
                    deletedNotes.add(note)
                    return true
                }
            }
        }
        throw NoteNotFoundException()
    }

    fun commentDelete(commentId: Int): Boolean {
        for ((index, comment) in commentsList.withIndex()) {
            if (comment.id == commentId) {
                commentsList[index] = PareIdValue(id = null, value = null)
                deletedComments.add(comment)
                return true
            }
        }
        throw CommentNotFoundException()
    }

    fun restoreComment(commentId: Int): Boolean {
        for ((index, comment) in deletedComments.withIndex()) {
            if (comment.id == commentId) {
                commentsList.add(comment)
                deletedComments[index] = PareIdValue(id = null, value = null)
                return true
            }
        }
        throw CommentNotFoundException()
    }

    fun deleteNoteCheck(id: Int): Boolean {
        for (deletedNote in deletedNotes) {
            if (deletedNote.id == id) {
                throw DeletedNoteException()
            }
        }
        return true
    }

    fun deleteCommentCheck(id: Int): Boolean {
        for (deletedComment in deletedComments) {
            if (deletedComment.id == id)
                throw DeletedCommentException()
        }
        return true
    }

    fun clear() {
        noteIds = 0
        commentIds = 0
        notesList.clear()
        commentsList.clear()
        deletedNotes.clear()
        deletedComments.clear()
    }
}