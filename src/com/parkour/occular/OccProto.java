package com.parkour.occular;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.view.Display;
import android.view.Window;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;


/**
 * Application's main Activity. Does nothing special apart from putting the app
 * in fullscreen mode and creating a GLLayer object as well as a Preview object.
 * 
 * @author Niels
 * 
 */
public class OccProto extends Activity {
	private CamLayer mPreview;
	private GLLayerProto glView;
	//private HoponPop Target;
	protected SensorManager mSensei;
	protected PowerManager mPower;
	protected LocationManager mLocato;
	private WakeLock mWakeLock;
	protected Display mDisplay;

	// static int counter=0;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mSensei = (SensorManager) getSystemService(SENSOR_SERVICE);
		mPower = (PowerManager) getSystemService(POWER_SERVICE);
		mWakeLock = mPower.newWakeLock(PowerManager.SCREEN_BRIGHT_WAKE_LOCK,
				getClass().getName());
		mLocato = (LocationManager) getSystemService(LOCATION_SERVICE);
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		// counter++;
		// if (counter==2) {
		// MediaPlayer mp=MediaPlayer.create(this, R.raw.track);
		// mp.start();
		// }
	}

	public void onStart() {
		super.onStart();
		//this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		final Window win = getWindow();
		win.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		mDisplay = win.getWindowManager().getDefaultDisplay();
		// Hide the window title.

		Sensor mMagSensor = mSensei
				.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
		Sensor mAccSensor = mSensei.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		// glView = new GLLayer_Proto(this, mSensei, mDisplay);
		
		glView = new GLLayerProto(this, mSensei, mAccSensor, mMagSensor,
				mLocato, mDisplay);

		mPreview = new CamLayer(this, glView);
		//Target = new HoponPop(this, mPreview);

		// add the camera first
		// removing glView causes it to only show the camera.
		setContentView(mPreview);
		addContentView(glView, new LayoutParams(LayoutParams.FILL_PARENT,
				LayoutParams.FILL_PARENT));
		//addContentView(Target, new LayoutParams(LayoutParams.FILL_PARENT, 
				//LayoutParams.FILL_PARENT));
		// addContentView(new HoponPop(this), new
		// LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
		// mSensei = null;
		// this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);

	}

	/** Called when the activity is first created. */

	public void onResume() {
		super.onResume();
		glView.onResume();
		mWakeLock.acquire();
		//Target.invalidate();
	}

	protected void onPause() {
		super.onPause();
		glView.onPause();
		// mWakeLock.release();
		// if (counter>=2) {
		// System.exit(0);
		// }
	}
	
	protected void onStop() {
		super.onStop();
		//mPreview.mCamera.release();
	}

	protected void onDestroy() {
		super.onDestroy();
	}

}