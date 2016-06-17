package com.studenttheironyard;

import jodd.json.JsonSerializer;
import org.h2.command.ddl.CreateTable;
import org.h2.tools.Server;
import spark.Session;
import spark.Spark;

import java.sql.*;
import java.util.ArrayList;

import static com.sun.tools.doclint.Entity.reg;

public class Main {

    public static void createTables(Connection conn) throws SQLException {
        Statement stmt = conn.createStatement();
        stmt.execute("CREATE TABLE IF NOT EXISTS users( userid IDENTITY, name VARCHAR, email VARCHAR)");
        stmt.execute("CREATE TABLE IF NOT EXISTS comments( commentid IDENTITY, replyID INT, author VARCHAR, text VARCHAR");
        stmt.execute("CREATE TABLE IF NOT EXISTS memes(memeid IDENTITY, memename VARCHAR, upvote INT, downvote INT");
    }

    public static void insertUser(Connection conn, String name, String email) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement("INSERT INTO users VALUES(NULL, ?, ?)");
        stmt.setString(1, name);
        stmt.setString(2, email);
        stmt.execute();
    }

    public static ArrayList<User> selectUsers(Connection conn) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement("SELECT * FROM users");
        ResultSet results = stmt.executeQuery();
        ArrayList<User> userArrayList = new ArrayList<>();
        while (results.next()) {
            int userId = results.getInt("userId");
            String name = results.getString("name");
            String email = results.getString("email");
            User user = new User(userId, name, email);
            userArrayList.add(user);
        }
        return userArrayList;
    }

    public static void insertComment(Connection conn, String text, int userId) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement("INSERT INTO comments VALUES (NULL, ?, ?)");
        stmt.setString(1, text);
        stmt.setInt(2, userId);
        stmt.execute();
    }

    public static Comment selectComment(Connection conn, int commentId) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement("SELECT * FROM comments INNER JOIN users ON comments.user_id = users.id WHERE users.id = ?");
        stmt.setInt(1, commentId);
        ResultSet results = stmt.executeQuery();
        if (results.next()) {
            String text = results.getString("comments.text");
            String author = results.getString("users.name");
            return new Comment(commentId, author, text);
        }
        return null;
    }


    public static void deleteComment(Connection conn, int commentId) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement("DELETE FROM messages WHERE commentid = ?");
        stmt.setInt(1, commentId);
        stmt.execute();
    }

    public static void updateComment(Connection conn, Comment comment) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement("UPDATE comments SET comment text = ? WHERE userid = ?");
        stmt.setString(1,comment.text);
        stmt.setInt(2, comment.commentId);
        stmt.execute();
    }

    public static void insertMeme(Connection conn, int upVote, int downVote) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement("INSERT INTO memes VALUES(NULL, NULL, ?, ?");
        stmt.setInt(1, upVote);
        stmt.setInt(2, downVote);
        stmt.execute();
    }



    public static void main(String[] args) throws SQLException {
        Server.createWebServer().start();
        Connection conn = DriverManager.getConnection("jdbc:h2:./main");
        createTables(conn);

        Spark.init();

        Spark.get(
                "/user",
                (request, response) -> {
                    ArrayList<User> regs = selectUsers(conn);
                    JsonSerializer s = new JsonSerializer();
                    return s.serialize(regs);
                }
        );

    }
}
