package medds.carecenter;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
//import android.util.Log;
import android.widget.TabHost;
//import android.widget.TabHost.TabSpec;
/**
 * A tab has a tab indicator, content, and a tag that is used to keep track of it. This builder helps choose among these options.
 */
//import android.widget.TabHost.TabSpec;


public class TabbsActivity extends TabActivity{

	public TabHost tabHost;

	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.main);

            // Get the tabHost
	    this.tabHost = getTabHost();

	    TabHost.TabSpec spec;  // Resusable TabSpec for each tab
	    Intent intent;  // Reusable Intent for each tab

	    // Create an Intent to launch the first Activity for the tab (to be reused)
	    intent = new Intent().setClass(this, PhotosActivity.class);

	    // Initialize a TabSpec for the first tab and add it to the TabHost
	    spec = tabHost.newTabSpec("Photos").setIndicator("Photos",
	    		getResources().getDrawable(R.drawable.ic_photos_tab)) // Replace null with R.drawable.your_icon to set tab icon
	    				.setContent(intent);
	    tabHost.addTab(spec);

            // Create an Intent to launch an Activity for the tab (to be reused)
	    intent = new Intent().setClass(this, NotesActivity.class);

	    // Initialize a TabSpec for the second tab and add it to the TabHost
	    spec = tabHost.newTabSpec("Notes").setIndicator("Notes",
	    		getResources().getDrawable(R.drawable.ic_notes_tab)) // Replace null with R.drawable.your_icon to set tab icon
	    				.setContent(intent);
	    tabHost.addTab(spec);
	    
	    // Create an Intent to launch the first Activity for the tab (to be reused)
	    intent = new Intent().setClass(this, SavesActivity.class);

	    // Initialize a TabSpec for the third tab and add it to the TabHost
	    spec = tabHost.newTabSpec("Save").setIndicator("Save",
	    		getResources().getDrawable(R.drawable.ic_saves_tab)) // Replace null with R.drawable.your_icon to set tab icon
	    				.setContent(intent);
	    tabHost.addTab(spec);
	    
	    
	    tabHost.setCurrentTab(0);
	    
	}
}