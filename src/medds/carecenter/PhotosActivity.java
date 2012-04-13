package medds.carecenter;

import java.io.ByteArrayOutputStream;

import medds.carecenter.library.PatientDatabaseHandler;
import medds.carecenter.library.Preview;
import medds.carecenter.library.TableData;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.hardware.Camera;
import android.hardware.Camera.PictureCallback;
import android.hardware.Camera.ShutterCallback;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;

public class PhotosActivity extends Activity
{
	private static final String TAG = "PhotosActivity";
	Camera camera;
	Preview preview;
	Button buttonClick, refresh, release, start;
	FrameLayout frame;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.photos_layout);
		preview = new Preview(this);
		frame = (FrameLayout) findViewById(R.id.preview);
		//start camera button
		start = (Button) findViewById(R.id.btn_s);
		start.setOnClickListener( new OnClickListener() {
			public void onClick(View v){	
					//hide start camera button
					start.setVisibility(8);
					//start camera preview
					frame.addView(preview);
					//show capture button
					buttonClick = (Button) findViewById(R.id.btn_capture);
					buttonClick.setVisibility(View.VISIBLE);
					buttonClick.setOnClickListener( new OnClickListener() {
						public void onClick(View v) {
							//take photo
							preview.camera.takePicture(shutterCallback, rawCallback, jpegCallback);
							//hide captre button
							buttonClick.setVisibility(8);
							release = (Button) findViewById(R.id.btn_cam_release);
							frame.setVisibility(8);
							release.setVisibility(View.VISIBLE);
							release.setOnClickListener( new OnClickListener() {
								public void onClick(View v ){
									release.setVisibility(8);
									//destroy surfaceview preview from camera
									frame.destroyDrawingCache();
									frame.removeView(preview);
									//show toast for saved photo
									Context context = getApplicationContext();
							    	CharSequence text = "Your selected photo has been saved and is ready for upload! ";
							    	int duration = Toast.LENGTH_LONG;
							    	Toast toast = Toast.makeText(context, text, duration);
							    	toast.show();		
											//give option to restart with button
											refresh = (Button) findViewById(R.id.btn_refresh);
											refresh.setVisibility(View.VISIBLE);
											refresh.setOnClickListener( new OnClickListener() {
												public void onClick(View v) {
													// Relaunch Activity
						                            Intent retake = new Intent(getApplicationContext(), PhotosActivity.class);
						                            // Close all views before launching Dashboard
						                            retake.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
						                            startActivity(retake);
												}
											});
								}
							});
						}
					});
					Log.d(TAG, "PhotosActivity onCreate'd");
			}
		});
	}

	
	ShutterCallback shutterCallback = new ShutterCallback() {
		public void onShutter() {
			Log.d(TAG, "onShutter'd");
		}
	};

	
	/** Handles data for raw picture */
	PictureCallback rawCallback = new PictureCallback() {
		public void onPictureTaken(byte[] data, Camera camera) {
			Log.d(TAG, "onPictureTaken - raw");
		}
	};

	/** Handles data for jpeg picture */
	PictureCallback jpegCallback = new PictureCallback() {
		public void onPictureTaken(byte[] data, Camera camera) {
			try {
				//resize image via bitmap function then return to byte array
				Bitmap bitmap = getResizedBitmap(data, 100, 100);
				byte[] resized = getByteArray(bitmap);
				//object to hold data
				TableData td = new TableData();
				PatientDatabaseHandler db = new PatientDatabaseHandler(getApplicationContext());
						int rows = db.getNumRows();
						if (rows>2){ 
							db.deleteAllRecords();
							Log.d("Sqlite", "deleted table data because there were "+rows+" rows.");
						}
						else if(rows<1){ Log.d("Sqlite", "records in db are: "+rows);}
						else if (rows==2){  Log.d("Sqlite", "records in db are: "+rows);}
						else if (rows==1){  Log.d("Sqlite", "records in db are: "+rows);}
						if (rows<1) { td.setID(1); td.setNotes("defalut notes");
										db.addRecord(td);
										Log.d("Sqlite", "created new row"); }
				//give object photo data
				td.setPhoto(resized);
				td.setID(1);
				//send to db
				db.updatePhoto(td);
				
				Log.d(TAG, "PictureTaken - wrote bytes to database: " + resized.length);
			} finally {
			}
			Log.d(TAG, "onPictureTaken - jpeg");
		}
	};

	//resizes image 
	public Bitmap getResizedBitmap(byte[] data, int newHeight, int newWidth) {
		Log.d(TAG, "passed to bitmap factory");
		Bitmap bm = BitmapFactory.decodeByteArray(data , 0, data.length);			
		int width = bm.getWidth();
		int height = bm.getHeight();
		float scaleWidth = ((float) newWidth) / width;
		float scaleHeight = ((float) newHeight) / height;
		// create a matrix for the manipulation
		Matrix matrix = new Matrix();
		// resize the bit map
		matrix.postScale(scaleWidth, scaleHeight);
		// recreate the new Bitmap
		Bitmap resizedBitmap = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, false);
		return resizedBitmap;
	
	}
	
	//returns image to byte array
	public byte[] getByteArray(Bitmap bitmap) {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		bitmap.compress(CompressFormat.PNG, 0, bos);
		return bos.toByteArray();
	}


}
