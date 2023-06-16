import Exceptions.*
import org.junit.Test

import org.junit.Assert.*
import org.junit.Before

class NoteServiceTest {

    @Before
    fun clearBeforeTest() {
        NoteService.clear()
        NoteService.add(Note("title","text",1,1))
        NoteService.createComment(Comment(1,"message"))
    }

    @Test
    fun addFunTest() {
        val note = Note("t","t", 2, 2)
        val add = NoteService.add(note)
        assertEquals(2,add)
    }

    @Test
    fun createCommentFunTest() {
        val comment = Comment(1,"message")
        val newComment = NoteService.createComment(comment)
        assertEquals(2,newComment)
    }

    @Test
    fun editFunTest() {
        val editNote = PareIdValue(1,Note("t","t", 2, 2))
        assertEquals(true, NoteService.edit(editNote))
    }


    @Test
    fun editCommentFunTest() {
        val id = 1
        val comment = "M"
        assertEquals(true,NoteService.editComment(id, comment))
    }


    @Test
    fun getByIdFunTest() {
        val noteId = 1
        assertEquals(PareIdValue(1,Note("title","text",1,1)), NoteService.getById(noteId))
    }

    @Test
    fun getCommentsFunTest() {
        val noteId = 1
        assertEquals(mutableListOf(PareIdValue(1,Comment(1,"message"))), NoteService.getComments(noteId))
    }

    @Test
    fun getFunTest() {
        val noteId = "1"
        assertEquals(mutableListOf(PareIdValue(1,Note("title","text",1,1))), NoteService.get(noteId))
    }

    @Test
    fun deleteFunTest() {
        val noteId = 1
        assertEquals(true, NoteService.delete(noteId))
    }

    @Test
    fun commentDeleteFunTest() {
        val commentId = 1
        assertEquals(true, NoteService.commentDelete(commentId))
    }

    @Test
    fun restoreCommentFunTest() {
        val commentId = 1
        NoteService.commentDelete(commentId)
        assertEquals(true, NoteService.restoreComment(commentId))
    }
    @Test
    fun deleteNoteCheckFunTest() {
        val id =1
        assertEquals(true, NoteService.deleteNoteCheck(id))
    }

    @Test
    fun deleteCommentCheckFunTest() {
        val id =1
        assertEquals(true, NoteService.deleteCommentCheck(id))
    }

    @Test(expected = WrongCommentPrivacyException::class)
    fun WrongCommentPrivacyAddFunException() {
        NoteService.add(Note("t","t",1,5))
    }

    @Test(expected = WrongCommentPrivacyException::class)
    fun WrongCommentPrivacyEditFunException() {
        NoteService.edit(PareIdValue(1,Note("t","t",1,5)))
    }
    @Test(expected = WrongNotePrivacyException::class)
    fun WrongNotePrivacyAddFunException() {
        NoteService.add(Note("t","t",5,1))

    }
    @Test(expected = WrongNotePrivacyException::class)
    fun WrongNotePrivacyEditFunException() {
        NoteService.edit(PareIdValue(1,Note("t","t",5,1)))
    }
    @Test(expected = DeletedNoteException::class)
    fun DeletedNoteDeleteNoteFunException() {
        NoteService.delete(1)
        NoteService.deleteNoteCheck(1)
    }
    @Test(expected = DeletedCommentException::class)
    fun DeletedCommentDeleteCommentFunException() {
        NoteService.delete(1)
        NoteService.deleteCommentCheck(1)
    }

    @Test(expected = NoteNotFoundException::class)
    fun NoteNotFoundAddFunException() {
        NoteService.createComment(Comment(3,"m"))
    }
    @Test(expected = NoteNotFoundException::class)
    fun NoteNotFoundEditFunException() {
        NoteService.edit(PareIdValue(4,Note("title","text", 1, 1)))
    }

    @Test(expected = NoteNotFoundException::class)
    fun NoteNotFoundGetByIdFunException() {
        NoteService.getById(3)
    }

    @Test(expected = NoteNotFoundException::class)
    fun NoteNotFoundGetCommentsFunException() {
        NoteService.getComments(3)
    }

    @Test(expected = NoteNotFoundException::class)
    fun NoteNotFoundGetFunException() {
        NoteService.get("4,5")
    }

    @Test(expected = NoteNotFoundException::class)
    fun NoteNotFoundDeleteFunException() {
        NoteService.delete(6)
    }

    @Test(expected = CommentNotFoundException::class)
    fun CommentNotFoundEditCommentFunException() {
        NoteService.editComment(4,"edit")
    }

    @Test(expected = CommentNotFoundException::class)
    fun CommentNotFoundGetCommentsFunException() {
        NoteService.add(Note("t","t",2,2))
        NoteService.getComments(2)
    }

    @Test(expected = CommentNotFoundException::class)
    fun CommentNotFoundRestoreCommentFunException() {
        NoteService.restoreComment(4)
    }

    @Test(expected = CommentNotFoundException::class)
    fun CommentNotFoundCommentDeleteFunException() {
        NoteService.commentDelete(6)
    }

}