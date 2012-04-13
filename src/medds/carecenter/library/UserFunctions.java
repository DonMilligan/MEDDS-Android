package medds.carecenter.library;
 
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import android.content.Context;
import android.util.Log;


public class UserFunctions {
 
    private JSONParser jsonParser;
 
    // Testing in localhost using wamp or xampp
    // use http://10.0.2.2/ to connect to your localhost ie http://localhost/
    private static String loginURL = "http://142.25.97.127/phone/index.php";
    private static String registerURL = "http://142.25.97.127/phone/index.php";
    private static String testURL = "http://142.25.97.127/phone/base.php";

    private static String login_tag = "login";
    private static String register_tag = "register";
    private static String push_tag = "push";
    // constructor
    public UserFunctions(){
        jsonParser = new JSONParser();
    }
 
    /**
     * function make Login Request
     * @param email
     * @param password
     * */
    public JSONObject loginUser(String email, String password){
        // Building Parameters
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("tag", login_tag));
        params.add(new BasicNameValuePair("email", email));
        params.add(new BasicNameValuePair("password", password));
        JSONObject json = jsonParser.getJSONFromUrl(loginURL, params);
        // return json
//         Log.e("JSON", json.toString());
        return json;
    }
 
    /**
     * function make Login Request
     * @param name
     * @param email
     * @param password
     * */
    public JSONObject registerUser(String name, String email, String password){
        // Building Parameters
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("tag", register_tag));
        params.add(new BasicNameValuePair("name", name));
        params.add(new BasicNameValuePair("email", email));
        params.add(new BasicNameValuePair("password", password));
 
        // getting JSON Object
        JSONObject json = jsonParser.getJSONFromUrl(registerURL, params);
        // return json
        return json;
    }
 
    /**
     * Function get Login status
     * */
    public boolean isUserLoggedIn(Context context){
        LoginDatabaseHandler db = new LoginDatabaseHandler(context);
        int count = db.getRowCount();
        if(count > 0){
            // user logged in
            return true;
        }
        return false;
    }
 
    /**
     * Function to logout user
     * Reset Database
     * */
    public boolean logoutUser(Context context){
    	LoginDatabaseHandler db = new LoginDatabaseHandler(context);
        db.resetTables();
        return true;
    }
 
    public JSONObject pushData(String notes, String photo, String email)
    {
    	Log.d("contacting server: ", "top of push..");
    	// Building Parameters
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("tag", push_tag));
        params.add(new BasicNameValuePair("notes", notes));
        params.add(new BasicNameValuePair("photo", photo));
        params.add(new BasicNameValuePair("email", email));
        JSONObject json = jsonParser.getJSONFromUrl(loginURL, params);
        
//        String logMessage = "Failed to parse JSON String";
//        if (json != null) {
//        	logMessage = json.toString();
//        }
        // return json
        String logMessage = json.toString();
        Log.e("JSON", logMessage);
        return json;
    }
    
    public JSONObject pushData2(String photo)
    {
    	Log.d("contacting server: ", "top of push..");
    	// Building Parameters
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("image", photo));
        
        
        JSONObject json = jsonParser.getJSONFromUrl(testURL, params);
        
        String logMessage = json.toString();
        Log.e("JSON", logMessage);
        return json;
    }
    
}