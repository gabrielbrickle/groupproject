package com.studenttheironyard;

import jodd.json.JsonParser;
import jodd.json.JsonSerializer;
import org.h2.tools.Server;
import spark.Session;
import spark.Spark;

import java.sql.*;
import java.util.ArrayList;

public class Main {

    public static void createTables(Connection conn) throws SQLException {
        Statement stmt = conn.createStatement();
        stmt.execute("CREATE TABLE IF NOT EXISTS users( userid IDENTITY, name VARCHAR, email VARCHAR)");
        stmt.execute("CREATE TABLE IF NOT EXISTS comments( commentid IDENTITY, author VARCHAR, text VARCHAR)");
        stmt.execute("CREATE TABLE IF NOT EXISTS memes(memeid IDENTITY, memename VARCHAR, upvote INT, downvote INT)");
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

    public static void insertComment(Connection conn, Comment comment) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement("INSERT INTO comments VALUES (NULL, ?, ?)");
        stmt.setString(1, comment.author);
        stmt.setString(2, comment.text);
        stmt.execute();

    }

    public static ArrayList<Comment> selectAllComments(Connection conn) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement("SELECT * FROM comments");
        ArrayList<Comment> comments = new ArrayList<>();
        ResultSet results = stmt.executeQuery();
        while (results.next()) {
            int commentid = results.getInt("commentid");
            String author = results.getString("author");
            String text = results.getString("text");
            Comment comment = new Comment(commentid, author, text);
            comments.add(comment);
        }

        stmt.execute();
        return comments;
    }

    public static Comment selectComment(Connection conn, int commentId) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement("SELECT * FROM comments WHERE commentid = ?");
        stmt.setInt(1, commentId);
        ResultSet results = stmt.executeQuery();
        if (results.next()) {
            String text = results.getString("text");
            String author = results.getString("author");
            return new Comment(commentId, author, text);
        }
        return null;
    }


    public static void deleteComment(Connection conn, int commentId) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement("DELETE FROM comments WHERE commentid = ?");
        stmt.setInt(1, commentId);
        stmt.execute();
    }

    public static void updateComment(Connection conn, Comment comment, Integer commentId) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement("UPDATE comments SET author = ? WHERE commentid = ?");
        PreparedStatement stmt1 = conn.prepareStatement("UPDATE comments SET text = ? WHERE commentid = ?");
        stmt.setString(1, comment.author);
        stmt1.setString(1, comment.text);
        stmt.setInt(2, commentId);
        stmt1.setInt(2, commentId);
        stmt.execute();
        stmt1.execute();
    }

    public static void updateUpVote(Connection conn, Meme meme) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement("UPDATE memes SET upvote = ? WHERE memeid= ?");
        stmt.setInt(1, meme.upVote);
        stmt.setInt(2, meme.id);
        stmt.execute();
    }

    public static void updateDownVote(Connection conn, Meme meme) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement("UPDATE memes SET downvote = ? WHERE memeid= ?");
        stmt.setInt(1, meme.downVote);
        stmt.setInt(2, meme.id);
        stmt.execute();
    }

    public static ArrayList<Meme> selectMemes(Connection conn) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement("SELECT * FROM memes");
        ArrayList<Meme> memesArrayList = new ArrayList<>();
        stmt.execute();
        return memesArrayList;
    }

    public static void main(String[] args) throws SQLException {
        Server.createWebServer().start();
        Connection conn = DriverManager.getConnection("jdbc:h2:./main");
        createTables(conn);

        Spark.externalStaticFileLocation("public");
        Spark.init();

        Spark.get(
                "/comment",
                (request, response) -> {
                    ArrayList<Comment> commentList = selectAllComments(conn);
                    JsonSerializer s = new JsonSerializer();
                    return s.serialize(commentList);
                }
        );

        Spark.post(
                "/comment",
                (request, response) -> {
                    //String body = request.body();
                    //JsonParser p = new JsonParser();
                    //Comment comment = p.parse(body, Comment.class);
                    String author = request.queryParams("author");
                    String text = request.queryParams("text");
                    Comment comment = new Comment(null, author, text);
                    insertComment(conn, comment);
                    return "";
                }
        );
        Spark.put(
                "/updateComment",
                (request, response) -> {
                    String body = request.body();
                    JsonParser p = new JsonParser();
                    Comment comment = p.parse(body, Comment.class);
                    updateComment(conn, comment, comment.commentId);
                    return "";
                }
        );

        Spark.put(
                "/updateUpvote",
                (request, response) -> {
                    String body = request.body();
                    JsonParser p = new JsonParser();
                    Meme meme = p.parse(body, Meme.class);
                    int vote = meme.upVote++;
                    int id = meme.memeId;
                    updateUpVote(conn, vote, id);
                    return "";
                }
        );

        Spark.put(
                "/updateDownvote",
                (request, response) -> {
                    String body = request.body();
                    JsonParser p = new JsonParser();
                    Meme meme = p.parse(body, Meme.class);
                    int vote = meme.downVote++;
                    int id = meme.memeId;
                    updateDownVote(conn, vote, id);
                    return "";
                }
        );


        Spark.delete(
                "/deleteComment",
                (request, response) -> {
                    int id = Integer.valueOf(request.queryParams("author"));
                    Session session = request.session();
                    String username = session.attribute("author");
                    Comment comment = selectComment(conn, id);
                    if(!comment.author.equals(username)){
                        throw new Exception("you can't delete this");
                    }

                    deleteComment(conn, id);
                    response.redirect("/meme");
                    return "";
                }
        );



        Spark.put(
                "/comment/:id",
                (request, response) -> {
                    String body = request.body();
                    JsonParser p = new JsonParser();
                    Comment comment = p.parse(body, Comment.class);
                    updateComment(conn, comment, comment.id);
                    return "";
                }
        );

        Spark.post(
                "/meme",
                (request, response) -> {
                    String body = request.body();
                    JsonParser p = new JsonParser();
                    Meme meme = p.parse(body, Meme.class);
                    updateUpVote(conn, meme);
                    return "";
                }
        );

        Spark.get(
                "/meme",
                (request, response) -> {
                    String body = request.body();
                    JsonParser p = new JsonParser();
                    Meme meme = p.parse(body, Meme.class);
                    updateDownVote(conn, meme);
                    return "";
                }
        );


        Spark.delete(
                "/comment/:id",
                (request, response) -> {
                    int id = Integer.valueOf(request.params(":id"));
//                    Session session = request.session();
//                    String username = session.attribute("author");
//                    Comment comment = selectComment(conn, id);
//                    if(!comment.author.equals(username)){
//                        throw new Exception("you can't delete this");
//                    }

                    deleteComment(conn, id);
                    response.redirect("/meme");
                    return "";
                }
        );


    }
}
