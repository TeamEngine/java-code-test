package se.teamengine.java.test.tasks;

import com.google.gson.Gson;
import se.teamengine.java.test.models.TaskOneRequest;
import se.teamengine.java.test.models.UserSession;
import se.teamengine.java.test.util.HMacUtil;

import java.util.Date;

public class TaskOne {

    private static final long TWO_HOURS_MS = 1000 * 60 * 60 * 2;
    private static final long ONE_HOURS_MS = 1000 * 60 * 60;
    private final Gson gson = new Gson();

    /***
     * Task: add code that makes this method return true by modifying the jsonString variable.
     *
     * @return boolean value (true or false)
     */
    private boolean run() {
        String jsonString = "";
        // TODO keep your code changes below this comment
        long sessionExpiry = new Date().getTime() + ONE_HOURS_MS;
        String userName = "tester@example.com";
        String identifier = "s1337";

        UserSession session = new UserSession("tester@example.com", "s1337", sessionExpiry);

        jsonString = "{ " +
                " userSession : {" +
                "  userName : " + userName + "," +
                "  userIdentifier :" + identifier + "," +
                "  sessionExpiery : " + sessionExpiry +
                "  }," +
                " hmac : " + HMacUtil.generateHMAC(session.toString()) +
                "}";

        // TODO keep your code changes above this comment
        return receiver(jsonString);
    }


    private boolean receiver(String jsonString) {
        if (jsonString == null || jsonString.isEmpty()) {
            return false;
        }
        TaskOneRequest request = gson.fromJson(jsonString, TaskOneRequest.class);
        HMacUtil.assertHMacValid(request.getUserSession().toString(), request.getHmac());

        return isSessionValid(request.getUserSession());
    }

    private boolean isSessionValid(UserSession session) {
        long currentTime = new Date().getTime();
        return session != null && session.getUserIdentifier().equals("s1337") && session.getUserName().equals("tester@example.com") &&
                session.getSessionExpiery() > currentTime && session.getSessionExpiery() < currentTime + TWO_HOURS_MS;
    }

    public boolean verify() {
        try {
            return run();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}