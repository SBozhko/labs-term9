package by.bsuir.labs.mongoblog.dao;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.QueryBuilder;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class BlogPostDAO {
    DBCollection postsCollection;

    public BlogPostDAO(final DB blogDatabase) {
        postsCollection = blogDatabase.getCollection("posts");
    }

    public DBObject findByPermalink(String permalink) {

        DBObject post = null;

        QueryBuilder query = QueryBuilder.start("permalink").is(permalink);
        post = postsCollection.findOne(query.get());

        return post;
    }

    public List<DBObject> findByDateDescending(int limit) {

        List<DBObject> posts = new ArrayList<DBObject>();
        DBCursor cursor = postsCollection.find().sort(new BasicDBObject("date", -1)).limit(limit);

        while (cursor.hasNext()) {
            posts.add(cursor.next());
        }
        return posts;
    }


    public String addPost(String title, String body, List tags, String username) {

        System.out.println("inserting blog entry " + title + " " + body);

        String permalink = title.replaceAll("\\s", "_"); // whitespace becomes _
        permalink = permalink.replaceAll("\\W", ""); // get rid of non alphanumeric
        permalink = permalink.toLowerCase();

        BasicDBObject post = new BasicDBObject();
        post.put("title", title);
        post.put("author", username);
        post.put("body", body);
        post.put("permalink", permalink);
        post.put("tags", tags);
        post.put("comments", new ArrayList<String>());
        post.put("date", new Date());

        postsCollection.insert(post);

        return permalink;
    }

    public void addPostComment(final String name, final String email, final String body,
                               final String permalink) {
        DBObject post = findByPermalink(permalink);
        BasicDBList comments = (BasicDBList) post.get("comments");
        BasicDBObject comment = new BasicDBObject();
        comment.put("author", name);
        if (email == null || email.equals("")) {
            System.out.println("EMAIL was null");
        } else {
            comment.put("email", email);
        }
        comment.put("body", body);
        comments.add(comment);
        postsCollection.update(new BasicDBObject("permalink", permalink), post, true, false);
    }
}
