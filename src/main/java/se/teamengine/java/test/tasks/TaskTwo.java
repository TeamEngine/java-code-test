package se.teamengine.java.test.tasks;


import org.apache.commons.codec.binary.Hex;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.HashMap;

public class TaskTwo {

    private HashMap<String, String> userPasswordDatabase;


    /***
     * Task: add code that makes this method return true by modifying the pwHash variable.
     *
     * @param username the username
     * @param password the password
     * @return boolean value(true or false)
     */
    private boolean verifyUser(String username, String password) {
        String pwHash = "";
        // TODO add your code below this comment
        try {

            byte[] salt = username.getBytes(StandardCharsets.UTF_8);
            MessageDigest md = MessageDigest.getInstance("SHA-1");
            md.update(salt);
            md.update(password.getBytes());

            byte[] digests = md.digest();

            pwHash = Hex.encodeHexString(digests);
        } catch (Exception e) {
            throw new RuntimeException("Failure in creating pwHash :", e);
        }
//
//        System.out.println(pwHash);
//        System.out.println(userPasswordDatabase.get(username));

        // TODO add your code above this comment
        return userPasswordDatabase.get(username).equals(pwHash);
    }

    private boolean run() {
        return verifyUser("tester@example.com", "¤%&/(hjkassdfals");
    }

    private void initDB() {
        // Add entry to database, username & password hash.
        // Password hashes are created by first prepending the username as a salt to the password, and then sha1 hashed
        userPasswordDatabase = new HashMap<String, String>();
        userPasswordDatabase.put("tester@example.com", "cdb78f62ddd9d1bf4ad40ad961ab0f3240fc9b1f");
    }

    public boolean verify() {
        initDB();
        try {
            return run();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
