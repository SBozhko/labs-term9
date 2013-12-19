package by.bsuir.labs.mongoblog.dao;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import sun.misc.BASE64Encoder;

import java.security.SecureRandom;

public class SessionDAO {
    private final DBCollection sessionsCollection;

    public SessionDAO(final DB blogDatabase) {
        sessionsCollection = blogDatabase.getCollection("sessions");
    }

    public String findUserNameBySessionId(String sessionId) {
        DBObject session = getSession(sessionId);

        if (session == null) {
            return null;
        } else {
            return session.get("username").toString();
        }
    }

    public String startSession(String username) {

        // get 32 byte random number. that's a lot of bits.
        SecureRandom generator = new SecureRandom();
        byte randomBytes[] = new byte[32];
        generator.nextBytes(randomBytes);
        BASE64Encoder encoder = new BASE64Encoder();
        String sessionID = encoder.encode(randomBytes);
        BasicDBObject session = new BasicDBObject("username", username);
        session.append("_id", sessionID);
        sessionsCollection.insert(session);

        return session.getString("_id");
    }

    public void endSession(String sessionID) {
        sessionsCollection.remove(new BasicDBObject("_id", sessionID));
    }

    public DBObject getSession(String sessionID) {
        return sessionsCollection.findOne(new BasicDBObject("_id", sessionID));
    }
}
