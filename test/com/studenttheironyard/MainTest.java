package com.studenttheironyard;

import org.junit.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;

import static org.junit.Assert.*;

/**
 * Created by hoseasandstrom on 6/16/16.
 */
public class MainTest {
    public Connection startConnection() throws SQLException {
        Connection conn = DriverManager.getConnection("jdbc:h2:mem:test");
        Main.createTables(conn);
        return conn;
    }
    @Test
    public void testSelectAllComments() throws SQLException {
        Connection conn = startConnection();
        Comment testComment = new Comment(1, "Alice", "Hello");
        Main.insertComment(conn, testComment);
        Comment testComment2 = new Comment(2, "Bob", "Hi");
        Main.insertComment(conn, testComment2);
        Comment testComment3 = new Comment(3, "Charlie", "Bye");
        Main.insertComment(conn, testComment3);
        ArrayList<Comment> testList = Main.selectAllComments(conn);
        conn.close();
        assertTrue(testList.size() == 3);
    }

    @Test
    public void testUpdateComment() throws SQLException {
        Connection conn = startConnection();
        Comment testComment = new Comment(1, "Alice", "Hello");
        Main.insertComment(conn, testComment);
        Comment testUpdate = new Comment(1, "Alice", "Bye");
        Main.updateComment(conn, testUpdate, 1);
        ArrayList<Comment> testList = Main.selectAllComments(conn);
        Comment comment = testList.get(0);
        String testString = comment.text;

        conn.close();
        assertTrue(testList.size() == 1);
        assertTrue(testString.length() == 3);
    }

    @Test
    public void testUpVote() throws SQLException {
        Connection conn = startConnection();
        Meme meme = new Meme(1, "a", 5, 2);
        //insert meme to run
        Main.updateUpVote(conn, meme);
        ArrayList<Meme> testMemeList = Main.selectMemes(conn);
        Meme testMeme = testMemeList.get(0);
        conn.close();

        assertTrue(testMeme.upVote == 6);
    }



}