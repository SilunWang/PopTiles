package allen.server;

import com.mongodb.*;

import java.net.UnknownHostException;

/**
 * Created by Allen on 14/12/26.
 */
public class MongoDriver {

    MongoClient mongoClient;
    DB db;
    DBCollection users;
    DBCollection scores;

    public MongoDriver() throws UnknownHostException {
        mongoClient  = new MongoClient();
        // database name: popTiles
        db = mongoClient.getDB("popTiles");
        // collection: users
        users = db.getCollection("users");
        // collection: scores
        scores = db.getCollection("scores");
    }

    public boolean authenticate(UserManager userManager) {
        BasicDBObject query = new BasicDBObject("_id", userManager.getUsername());
        DBCursor cursor = users.find(query);
        boolean ret = false;
        if (cursor.hasNext()){
            String pass = (String)cursor.next().get("password");
            if (pass.equals(userManager.getPassword()))
                ret = true;
        }
        return ret;
    }

    public boolean saveUser(UserManager userManager) {
        long n = users.count();
        DBObject obj = new BasicDBObject();
        obj.put("_id", userManager.getUsername());
        obj.put("password", userManager.getPassword());
        try {
            users.insert(obj);
        } catch (Exception e) {
            e.printStackTrace();
        }
        boolean ret;
        ret = users.getCount() > n;
        return ret;
    }

    public boolean saveScore(UserManager userManager) {
        long n = scores.count();
        DBObject obj = new BasicDBObject();
        obj.put("username", userManager.getUsername());
        obj.put("timestamp", userManager.getTimestamp());
        obj.put("score", userManager.getHighestScore());
        boolean ret = false;
        try {
            ret = scores.insert(obj).getN() > n;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ret;
    }
}
