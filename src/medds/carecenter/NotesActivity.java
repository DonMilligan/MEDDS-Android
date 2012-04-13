package medds.carecenter;

import java.util.ArrayList;
import java.util.List;

import medds.carecenter.library.PatientDatabaseHandler;
import medds.carecenter.library.TableData;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Sample code that invokes the speech recognition intent API.
 */
public class NotesActivity extends Activity implements OnClickListener{

	private static final String TAG = "NotesActivity";
//	private int MY_DATA_CHECK_CODE = 0;
	private static final int VOICE_RECOGNITION_REQUEST_CODE = 1234;
    private ListView mList;
    Button speakButton, newNotes;
//    private TextToSpeech tts;

    /**
     * Called with the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);

        // Inflate our UI from its XML layout description.
        setContentView(R.layout.notes_layout);
        
        // Get display items for later interaction
        speakButton = (Button) findViewById(R.id.btn_speak);

        mList = (ListView) findViewById(R.id.list);

        // Check to see if a recognition activity is present
        PackageManager pm = getPackageManager();
        List<ResolveInfo> activities = pm.queryIntentActivities(
                new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH), 0);
        if (activities.size() != 0) {
            speakButton.setOnClickListener(this);
        } else {
            speakButton.setEnabled(false);
            speakButton.setText("Recognizer not present");
        }
    }

    /**
     * Handle the click on the start recognition button.
     */
    public void onClick(View v) 
    {
        if (v.getId() == R.id.btn_speak) {
            startVoiceRecognitionActivity();
        } else if (v.getId() == R.id.btn_new_notes){
        	// Relaunch Activity
            Intent retake = new Intent(getApplicationContext(), NotesActivity.class);
            // Close all views before launching Dashboard
            retake.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(retake);
        }
    }

    /**
     * Fire an intent to start the speech recognition activity.
     */
    private void startVoiceRecognitionActivity() 
    {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);

        // Specify the calling package to identify the application
        intent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, getClass().getPackage().getName());

        // Display an hint to the user about what they should say.
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Speak notes on patient now.");

        // Given an hint to the recognizer about what the user is going to say
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);

        // Specify how many results you want to receive. The results will be sorted
        // where the first result is the one with higher confidence.
        intent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 5);

        startActivityForResult(intent, VOICE_RECOGNITION_REQUEST_CODE);
    }

    /**
     * Handle the results from the recognition activity.
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) 
    {
    	//check to see if thre are results
    	if (requestCode == VOICE_RECOGNITION_REQUEST_CODE && resultCode == RESULT_OK) {
            // Fill the list view with the strings the recognizer thought it could have heard
            ArrayList<String> matches = data.getStringArrayListExtra(
                    RecognizerIntent.EXTRA_RESULTS);
            //display results in list addapter
            mList.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,
                    matches));
            //let user select which result
            mList.setItemsCanFocus(true);
            mList.setOnItemClickListener(new OnItemClickListener() {
            	   public void onItemClick(AdapterView<?> adapter, View view, int position, long arg) {
            	      Object listItem = mList.getItemAtPosition(position);
            	      //turn selection into string and pass it to the db function below
            	      String selectedItem = listItem.toString();
            	      if (selectedItem != null) {  
                      	Log.d("Insert: ", "sending string to database function..");
                      	sendToDB(selectedItem); 
                      }
            	   } 
            	});         
    	}
        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * sends notes to db and then a toast message.
     */
    private String sendToDB(String selectedItem) 
    {
    	//attempt to upload notes to db
    	try {
			TableData td = new TableData();
			PatientDatabaseHandler db = new PatientDatabaseHandler(getApplicationContext());
				int rows = db.getNumRows();
				Log.d("database content", "There are "+rows+" in the db");
			td.setNotes(selectedItem);
			td.setID(1);
			db.updateNotes(td);
			Log.d(TAG, "Speech updated in db :");
		} finally {
		}
    	//show toast for notes upload
    	Context context = getApplicationContext();
    	CharSequence text = "Your selected notes have been saved and are ready for upload! ";
    	int duration = Toast.LENGTH_LONG;
    	Toast toast = Toast.makeText(context, text, duration);
    	toast.show();
    	Log.d(TAG, "Notes toasted");
    	//hide listview, button, and directions
    	mList.setVisibility(8);
    	speakButton.setVisibility(8);
    	TextView txt = (TextView) findViewById(R.id.directions);
    	txt.setVisibility(8);
    	//show reload button
    	// Get display items for later interaction
        newNotes = (Button) findViewById(R.id.btn_new_notes);
        newNotes.setVisibility(View.VISIBLE);
        newNotes.setOnClickListener(this);
		
		return selectedItem;
	}

//    /*
//     * Check to see if TTS is working
//     */
//    public void onInit(int status) 
//    { 
//    	Log.i("TranslatorActivity", "Testing log and onInit for TTS"); 
//    	if (status == TextToSpeech.SUCCESS) { 
//    		Toast.makeText(NotesActivity.this, 
//    				"Text-To-Speech engine is initialized", Toast.LENGTH_LONG).show(); 
//    	} else if (status == TextToSpeech.ERROR) { 
//    		Toast.makeText(NotesActivity.this, 
//    				"Error occurred while initializing Text-To-Speech engine", 
//    				Toast.LENGTH_LONG).show(); 
//    	} 
//    }

}

