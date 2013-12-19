package by.bsuir.labs.mongoblog.controller;

import by.bsuir.labs.mongoblog.dao.BlogPostDAO;
import by.bsuir.labs.mongoblog.dao.SessionDAO;
import by.bsuir.labs.mongoblog.dao.UserDAO;
import com.mongodb.DB;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import freemarker.template.Configuration;
import freemarker.template.SimpleHash;
import freemarker.template.TemplateException;
import org.apache.commons.lang3.StringEscapeUtils;
import spark.Request;
import spark.Response;

import javax.servlet.http.Cookie;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static spark.Spark.get;
import static spark.Spark.post;
import static spark.Spark.setPort;

public class BlogController {
    private final Configuration cfg;
    private final BlogPostDAO blogPostDAO;
    private final UserDAO userDAO;
    private final SessionDAO sessionDAO;

    public BlogController(String mongoURIString) throws IOException {
        final MongoClient mongoClient = new MongoClient(new MongoClientURI(mongoURIString));
        final DB blogDatabase = mongoClient.getDB("blog");

        blogPostDAO = new BlogPostDAO(blogDatabase);
        userDAO = new UserDAO(blogDatabase);
        sessionDAO = new SessionDAO(blogDatabase);

        cfg = createFreemarkerConfiguration();
        setPort(8084);
        initializeRoutes();
    }


    private void initializeRoutes() throws IOException {
        get(new FreemarkerBasedRoute("/", "blog_template.ftl", cfg) {
            @Override
            public void doHandle(Request request, Response response, Writer writer) throws IOException, TemplateException {
                String username = sessionDAO.findUserNameBySessionId(getSessionCookie(request));

                List<DBObject> posts = blogPostDAO.findByDateDescending(10);
                SimpleHash root = new SimpleHash();

                root.put("myposts", posts);
                if (username != null) {
                    root.put("username", username);
                }

                template.process(root, writer);
            }
        });

        get(new FreemarkerBasedRoute("/post/:permalink", "entry_template.ftl", cfg) {
            @Override
            protected void doHandle(Request request, Response response, Writer writer) throws IOException, TemplateException {
                String permalink = request.params(":permalink");

                System.out.println("/post: get " + permalink);

                DBObject post = blogPostDAO.findByPermalink(permalink);
                if (post == null) {
                    response.redirect("/post_not_found");
                } else {
                    SimpleHash newComment = new SimpleHash();
                    newComment.put("author", "");
                    newComment.put("email", "");
                    newComment.put("body", "");

                    SimpleHash root = new SimpleHash();

                    root.put("post", post);
                    root.put("comment", newComment);

                    template.process(root, writer);
                }
            }
        });

        post(new FreemarkerBasedRoute("/signup", "signup.ftl", cfg) {
            @Override
            protected void doHandle(Request request, Response response, Writer writer) throws IOException, TemplateException {
                String email = request.queryParams("email");
                String username = request.queryParams("username");
                String password = request.queryParams("password");
                String verify = request.queryParams("verify");

                HashMap<String, String> root = new HashMap<String, String>();
                root.put("username", StringEscapeUtils.escapeHtml4(username));
                root.put("email", StringEscapeUtils.escapeHtml4(email));

                if (validateSignup(username, password, verify, email, root)) {
                    System.out.println("Signup: Creating user with: " + username + " " + password);
                    if (!userDAO.addUser(username, password, email)) {
                        // duplicate user
                        root.put("username_error", "Username already in use, Please choose another");
                        template.process(root, writer);
                    } else {
                        // good user, start a session
                        String sessionID = sessionDAO.startSession(username);
                        System.out.println("Session ID is" + sessionID);

                        response.raw().addCookie(new Cookie("session", sessionID));
                        response.redirect("/welcome");
                    }
                } else {
                    // bad signup
                    System.out.println("User Registration did not validate");
                    template.process(root, writer);
                }
            }
        });

        get(new FreemarkerBasedRoute("/signup", "signup.ftl", cfg) {
            @Override
            protected void doHandle(Request request, Response response, Writer writer)
                    throws IOException, TemplateException {

                SimpleHash root = new SimpleHash();

                root.put("username", "");
                root.put("password", "");
                root.put("email", "");
                root.put("password_error", "");
                root.put("username_error", "");
                root.put("email_error", "");
                root.put("verify_error", "");

                template.process(root, writer);
            }
        });

        get(new FreemarkerBasedRoute("/newpost", "newpost_template.ftl", cfg) {
            @Override
            protected void doHandle(Request request, Response response, Writer writer) throws IOException, TemplateException {
                String username = sessionDAO.findUserNameBySessionId(getSessionCookie(request));

                if (username == null) {
                    response.redirect("/login");
                } else {
                    SimpleHash root = new SimpleHash();
                    root.put("username", username);

                    template.process(root, writer);
                }
            }
        });

        post(new FreemarkerBasedRoute("/newpost", "newpost_template.ftl", cfg) {
            @Override
            protected void doHandle(Request request, Response response, Writer writer)
                    throws IOException, TemplateException {

                String title = StringEscapeUtils.escapeHtml4(request.queryParams("subject"));
                String post = StringEscapeUtils.escapeHtml4(request.queryParams("body"));
                String tags = StringEscapeUtils.escapeHtml4(request.queryParams("tags"));

                String username = sessionDAO.findUserNameBySessionId(getSessionCookie(request));

                if (username == null) {
                    response.redirect("/login");
                } else if (title.equals("") || post.equals("")) {
                    HashMap<String, String> root = new HashMap<String, String>();
                    root.put("errors", "post must contain a title and blog entry.");
                    root.put("subject", title);
                    root.put("username", username);
                    root.put("tags", tags);
                    root.put("body", post);
                    template.process(root, writer);
                } else {
                    ArrayList<String> tagsArray = extractTags(tags);

                    post = post.replaceAll("\\r?\\n", "<p>");

                    String permalink = blogPostDAO.addPost(title, post, tagsArray, username);
                    response.redirect("/post/" + permalink);
                }
            }
        });

        get(new FreemarkerBasedRoute("/welcome", "welcome.ftl", cfg) {
            @Override
            protected void doHandle(Request request, Response response, Writer writer) throws IOException, TemplateException {

                String cookie = getSessionCookie(request);
                String username = sessionDAO.findUserNameBySessionId(cookie);

                if (username == null) {
                    System.out.println("welcome() can't identify the user, redirecting to signup");
                    response.redirect("/signup");

                } else {
                    SimpleHash root = new SimpleHash();

                    root.put("username", username);

                    template.process(root, writer);
                }
            }
        });

        post(new FreemarkerBasedRoute("/newcomment", "entry_template.ftl", cfg) {
            @Override
            protected void doHandle(Request request, Response response, Writer writer)
                    throws IOException, TemplateException {
                String name = StringEscapeUtils.escapeHtml4(request.queryParams("commentName"));
                String email = StringEscapeUtils.escapeHtml4(request.queryParams("commentEmail"));
                String body = StringEscapeUtils.escapeHtml4(request.queryParams("commentBody"));
                String permalink = request.queryParams("permalink");

                DBObject post = blogPostDAO.findByPermalink(permalink);
                if (post == null) {
                    response.redirect("/post_not_found");
                } else if (name.equals("") || body.equals("")) {
                    SimpleHash root = new SimpleHash();
                    SimpleHash comment = new SimpleHash();

                    comment.put("name", name);
                    comment.put("email", email);
                    comment.put("body", body);
                    root.put("comment", comment);
                    root.put("post", post);
                    root.put("errors", "Post must contain your name and an actual comment");

                    template.process(root, writer);
                } else {
                    blogPostDAO.addPostComment(name, email, body, permalink);
                    response.redirect("/post/" + permalink);
                }
            }
        });

        get(new FreemarkerBasedRoute("/login", "login.ftl", cfg) {
            @Override
            protected void doHandle(Request request, Response response, Writer writer) throws IOException, TemplateException {
                SimpleHash root = new SimpleHash();

                root.put("username", "");
                root.put("login_error", "");

                template.process(root, writer);
            }
        });

        post(new FreemarkerBasedRoute("/login", "login.ftl", cfg) {
            @Override
            protected void doHandle(Request request, Response response, Writer writer) throws IOException, TemplateException {

                String username = request.queryParams("username");
                String password = request.queryParams("password");

                System.out.println("Login: User submitted: " + username + "  " + password);

                DBObject user = userDAO.validateLogin(username, password);

                if (user != null) {

                    String sessionID = sessionDAO.startSession(user.get("_id").toString());

                    if (sessionID == null) {
                        response.redirect("/internal_error");
                    } else {
                        response.raw().addCookie(new Cookie("session", sessionID));

                        response.redirect("/welcome");
                    }
                } else {
                    SimpleHash root = new SimpleHash();
                    root.put("username", StringEscapeUtils.escapeHtml4(username));
                    root.put("password", "");
                    root.put("login_error", "Invalid Login");
                    template.process(root, writer);
                }
            }
        });


        get(new FreemarkerBasedRoute("/post_not_found", "post_not_found.ftl", cfg) {
            @Override
            protected void doHandle(Request request, Response response, Writer writer) throws IOException, TemplateException {
                SimpleHash root = new SimpleHash();
                template.process(root, writer);
            }
        });

        get(new FreemarkerBasedRoute("/logout", "signup.ftl", cfg) {
            @Override
            protected void doHandle(Request request, Response response, Writer writer) throws IOException, TemplateException {

                String sessionID = getSessionCookie(request);

                if (sessionID == null) {
                    response.redirect("/login");
                } else {
                    sessionDAO.endSession(sessionID);

                    Cookie c = getSessionCookieActual(request);
                    c.setMaxAge(0);
                    response.raw().addCookie(c);
                    response.redirect("/login");
                }
            }
        });


        get(new FreemarkerBasedRoute("/internal_error", "error_template.ftl", cfg) {
            @Override
            protected void doHandle(Request request, Response response, Writer writer) throws IOException, TemplateException {
                SimpleHash root = new SimpleHash();

                root.put("error", "System has encountered an error.");
                template.process(root, writer);
            }
        });
    }

    private String getSessionCookie(final Request request) {
        if (request.raw().getCookies() == null) {
            return null;
        }
        for (Cookie cookie : request.raw().getCookies()) {
            if (cookie.getName().equals("session")) {
                return cookie.getValue();
            }
        }
        return null;
    }

    private Cookie getSessionCookieActual(final Request request) {
        if (request.raw().getCookies() == null) {
            return null;
        }
        for (Cookie cookie : request.raw().getCookies()) {
            if (cookie.getName().equals("session")) {
                return cookie;
            }
        }
        return null;
    }

    private ArrayList<String> extractTags(String tags) {
        tags = tags.replaceAll("\\s", "");
        String tagArray[] = tags.split(",");

        ArrayList<String> cleaned = new ArrayList<String>();
        for (String tag : tagArray) {
            if (!tag.equals("") && !cleaned.contains(tag)) {
                cleaned.add(tag);
            }
        }

        return cleaned;
    }

    public boolean validateSignup(String username, String password, String verify, String email,
                                  HashMap<String, String> errors) {
        String USER_RE = "^[a-zA-Z0-9_-]{3,20}$";
        String PASS_RE = "^.{3,20}$";
        String EMAIL_RE = "^[\\S]+@[\\S]+\\.[\\S]+$";

        errors.put("username_error", "");
        errors.put("password_error", "");
        errors.put("verify_error", "");
        errors.put("email_error", "");

        if (!username.matches(USER_RE)) {
            errors.put("username_error", "invalid username. try just letters and numbers");
            return false;
        }

        if (!password.matches(PASS_RE)) {
            errors.put("password_error", "invalid password.");
            return false;
        }

        if (!password.equals(verify)) {
            errors.put("verify_error", "password must match");
            return false;
        }

        if (!email.equals("")) {
            if (!email.matches(EMAIL_RE)) {
                errors.put("email_error", "Invalid Email Address");
                return false;
            }
        }

        return true;
    }

    private Configuration createFreemarkerConfiguration() {
        Configuration retVal = new Configuration();
        retVal.setClassForTemplateLoading(BlogController.class, "/freemarker");
        return retVal;
    }
}
