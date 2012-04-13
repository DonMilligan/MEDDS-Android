package medds.carecenter;

import medds.carecenter.library.Base64;
import medds.carecenter.library.LoginDatabaseHandler;
import medds.carecenter.library.PatientDatabaseHandler;
import medds.carecenter.library.TableData;
import medds.carecenter.library.UserFunctions;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class SavesActivity extends Activity {
	public static SavesActivity group;
	public static int loops;
	UserFunctions userFunctions;
	Button btnLogout;
	Button btnSave;
	TextView errorMsg;
	TextView successMsg;
	EditText inputEmail;
	// JSON Response node names
	private static String KEY_SUCCESS = "success";
	
    Button btnLogin;
    Button btnLinkToRegister;
    EditText inputPassword;
    TextView loginErrorMsg;
 
    // JSON Response node names
//    private static String KEY_ERROR = "error";
//    private static String KEY_ERROR_MSG = "error_msg";

    private static String KEY_NAME = "name";
    private static String KEY_EMAIL = "email";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// Check login status in database
		userFunctions = new UserFunctions();
		//if statement to choose what view to display
		if (userFunctions.isUserLoggedIn(getApplicationContext())) {
			// user already logged in show databoard
			showDashboard();
		} else {
			// user is not logged in show login screen
			showLogin();
		}
	}
	/**
	 * Dashboard Screen for the application
	 **/
	void showDashboard() {
		setContentView(R.layout.dashboard);
		// Importing all assets like buttons, text fields
		inputEmail = (EditText) findViewById(R.id.patientEmail);
		errorMsg = (TextView) findViewById(R.id.login_error);
		successMsg = (TextView) findViewById(R.id.login_error);
		btnSave = (Button) findViewById(R.id.btnSave);
		btnLogout = (Button) findViewById(R.id.btnLogout);
		btnSave.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				// continue
				Log.d("BTN: ", " Save CLICKED..");
				String email = inputEmail.getText().toString();
				PatientDatabaseHandler pdb = new PatientDatabaseHandler(
						getApplicationContext());
				int rows = pdb.getNumRows();
				if (rows < 1) {
					Log.d("SQLtest", rows + " rows in db!");
				} else if (rows > 1) {
					Log.d("SQLtest", rows + " rows in db!");
				} else if (rows == 1) {
					Log.d("SQLtest", rows + " rows in db!");
				} else if (rows == 2) {
					Log.d("SQLtest", rows + " rows in db!");
				}

				TableData td = pdb.getRecord(1);
				//encode byte array photo and turn into base64 string
				byte[] jpg = td.getPhoto();
				String photo = Base64.encodeBytes(jpg);;
				String notes = td.getNotes();
				Log.d("coming from DB", notes);
				Log.d("coming from form", email);
				//error checking not fully implemented
				if (email == null) { errorMsg.setText("Please enter patient email first.");}
				// push notes to server
				JSONObject json = userFunctions.pushData(notes, photo, email);
				try { // check for success response
					if (json.getString(KEY_SUCCESS) != null) {
						Log.e("Don","key successs is: " + json.getString(KEY_SUCCESS));
						Log.e("NP", "Does errorMsg cotain anything? >>: "+ errorMsg);
						// show no message at this point
						errorMsg.setText("");
						String res = json.getString(KEY_SUCCESS);
						if (Integer.parseInt(res) == 1) {
							// data successfully pushed
							// notify user and wait for log out
							Log.d("successMsg: ", "data successfuly uploaded");
							successMsg.setText("Data Successfuly uploaded: Please Log Out");
						} else {
							// Error in login
							errorMsg.setText("Error Pushing Data: Please try again.");
						}
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		});
		//logout button
		btnLogout.setOnClickListener(new View.OnClickListener() {
			public void onClick(View arg0) {
				userFunctions.logoutUser(getApplicationContext());
				Intent login = new Intent(getApplicationContext(),
						SavesActivity.class);
				//renew view to login
				login.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(login);
			}
		});
	}

	
	
	void showLogin()
	{
		setContentView(R.layout.login);
        // Importing all assets like buttons, text fields
        inputEmail = (EditText) findViewById(R.id.loginEmail);
        inputPassword = (EditText) findViewById(R.id.loginPassword);
        btnLogin = (Button) findViewById(R.id.btnLogin);
        btnLinkToRegister = (Button) findViewById(R.id.btnLinkToRegisterScreen);
        loginErrorMsg = (TextView) findViewById(R.id.login_error);
        // Login button Click Event
        btnLogin.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
            	Log.d("Button: ", "Login clicked..");
            	//get email input
                String email = inputEmail.getText().toString();
                String password = inputPassword.getText().toString();
                UserFunctions userFunction = new UserFunctions();
                //send login info to server
                JSONObject json = userFunction.loginUser(email, password);
                // check for login response
                try {
                	Log.d("test: ", "try..");
                    if (json.getString(KEY_SUCCESS) != null) {
                    	Log.d("json: ", "keysuccess..");
                        loginErrorMsg.setText("");
                        String res = json.getString(KEY_SUCCESS);
                        if(Integer.parseInt(res) == 1){
                            // user successfully logged in
                            // Store user details in SQLite Database
                            LoginDatabaseHandler db = new LoginDatabaseHandler(getApplicationContext());
                            JSONObject json_user = json.getJSONObject("user");
 
                            // Clear all previous data in database
                            userFunction.logoutUser(getApplicationContext());
                            db.addUser(json_user.getString(KEY_NAME), json_user.getString(KEY_EMAIL));                        
 
                            // Launch Dashboard Screen
                            Intent dashboard = new Intent(getApplicationContext(), SavesActivity.class);
 
                            // Close all views before launching Dashboard
                            dashboard.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(dashboard);
                        }else{
                            // Error in login
                            loginErrorMsg.setText("Incorrect username/password");
                            Log.d("login: ", "errormsg..");
                        }
                    }
                } catch (JSONException e) {
                	Log.d("prinstack: ", "called..");
                	e.printStackTrace();
                }
            }
        });
	}

	void logout()
	{
		
	}
	// //Open medds.save package for login and push to MySQL
	// @Override
	// public void onCreate(Bundle savedInstanceState)
	// {
	// super.onCreate(savedInstanceState);
	// setContentView(R.layout.saves_layout);
	//
	// PackageManager pm = getPackageManager();
	// Intent intent = pm.getLaunchIntentForPackage("medds.save");
	// startActivity(intent);
	// // finish();
	// }

}
