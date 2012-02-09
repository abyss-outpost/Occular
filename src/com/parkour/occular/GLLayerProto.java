package com.parkour.occular;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.hardware.Camera;
import android.hardware.Camera.PreviewCallback;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.LocationManager;
import android.opengl.GLSurfaceView;
import android.opengl.GLSurfaceView.Renderer;
import android.opengl.GLU;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Display;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;


public class GLLayerProto extends GLSurfaceView implements PreviewCallback,
		Callback, Renderer {

	private FloatBuffer mFrameSobel = FloatBuffer.allocate(76800);

	/*static {
		System.loadLibrary("ndkfoo");
	}
	private native void nativeSobel (byte[] frame,
			int width,
			int height,
			FloatBuffer out);*/

	final static int OBJ_AT_REST = 0;
	final static int OBJ_IN_MOTION = 1;
	final static int OBJ_IN_XPOS = 100;
	final static int OBJ_IN_XNEG = -100;
	final static int OBJ_IN_YPOS = 1000;
	final static int OBJ_IN_YNEG = -1000;
	final static int OBJ_IN_ZPOS = 10000;
	final static int OBJ_IN_ZNEG = -10000;

	int crimonacounter = 0;

	float[] crimona = new float[76800];
	byte[] glCameraFrame = new byte[256 * 256]; // size of a texture must be a
												// power of 2
	SensorManager mSensor;
	LocationManager mLocatus;
	SensorEventListener myAccListener, myMagListener;
	SensorEventListener myMultiListener;
	Sensor MultiSensor;

	SurfaceHolder sHolder;
	public Display mDisplay;
	Sensor AccSensor;
	Sensor MagSensor;
	Shape mShape;
	Canvas cOver;
	Paint paint;
	int mWidth, mHeight;
	int magCounter = 0, accCounter = 0, onDrawFrameCounter = 0;
	int tempSpine = 0, currSpine = 0, CurrentStatus = 0;
	long previewStart = 0, previewEnd = 0;
	long nanoTimeStart = 0, nanoTimeEnd = 0;
	long MagTimeReference = 0, AccTimeReference = 0;
	long MagTTime = 0, AccTTime = 0;
	float MagbTime = 0, AccbTime = 0;

	long MagNTime = 0, AccNTime = 0;
	long MagOTime = 0, AccOTime = 0;
	// long MagOTime1= 0, AccOTime1= 0;
	// long MagOTime2= 0, AccOTime2= 0;
	// long MagOTime3= 0, AccOTime3= 0;
	// long MagOTime4= 0, AccOTime4= 0;

	float MagSecx = 0, AccSecx = 0;
	float MagSecy = 0, AccSecy = 0;
	float MagSecz = 0, AccSecz = 0;

	float MagSecx1 = 0, AccSecx1 = 0;
	float MagSecx2 = 0, AccSecx2 = 0;
	float MagSecx3 = 0, AccSecx3 = 0;
	float MagSecx4 = 0, AccSecx4 = 0;
	float MagSecy1 = 0, AccSecy1 = 0;
	float MagSecy2 = 0, AccSecy2 = 0;
	float MagSecy3 = 0, AccSecy3 = 0;
	float MagSecy4 = 0, AccSecy4 = 0;

	float OffsetX = 0;
	float OffsetY = 0, OffsetZ = 0;
	float angle = 0, accAngle = 0, Inclingle = 0, Inclinger = 0;
	float xangle = 0, yangle = 0, zangle = 0;

	final static float RAD2DEGS = (float) (180 / Math.PI);

	boolean Accfirsttime = false, Magfirsttime = false;
	boolean AccBeginSamp = false, MagBeginSamp = false;
	boolean SpineChanged = false, Orientofirsttime = true;
	boolean Indicat1 = false, Indicat2 = false, Indicat3 = false;

	float[] AccTValues = { 0, 0, 0 };
	float[] AccOValues = { 0, 0, 0 };
	// float[] AccOValues1= { 0, 0, 0 };
	// float[] AccOValues2= { 0, 0, 0 };
	// float[] AccOValues3= { 0, 0, 0 };
	// float[] AccOValues4= { 0, 0, 0 };

	float[] AccNValues = { 0, 0, 0 };
	float[] MagTValues = { 0, 0, 0 };
	float[] MagOValues = { 0, 0, 0 };
	// float[] MagOValues1= { 0, 0, 0 };
	// float[] MagOValues2= { 0, 0, 0 };
	// float[] MagOValues3= { 0, 0, 0 };
	// float[] MagOValues4= { 0, 0, 0 };

	float[] MagNValues = { 0, 0, 0 };
	float[] MagReference = { 0, 0, 0 };
	float[] AccReference = { 0, 0, 0 };
	float[] CurrentMagP = { 0, 0, 0 };
	float[] CurrentAccP = { 0, 0, 0 };
	float[] OldAccP = { 0, 0, 0 };
	float[] TempAccP = { 0, 0, 0 };
	float[] Oriento = { 0, 0, 0 };
	float[] Offseto = { 0, 0, 0 };
	float[] Ostarto = { 0, 0, 0 };

	float[] Temptato = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
	float[] Rotato = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
	float[] Inclino = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };

	public GLLayerProto(Context context, SensorManager mySensor,
			Sensor myAccSensor, Sensor myMagSensor, LocationManager myLocator,
			Display myDisplay) {
		super(context);
		this.setEGLConfigChooser(8, 8, 8, 8, 16, 0);
		this.setRenderer(this);
		sHolder = this.getHolder();
		sHolder.addCallback(this);
		sHolder.setFormat(PixelFormat.TRANSLUCENT);
		mSensor = mySensor;
		mDisplay = myDisplay;
		AccSensor = myAccSensor;
		MagSensor = myMagSensor;
		mLocatus = myLocator;
		// AccSensor = mSensor.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		// MagSensor = mSensor.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);

		mWidth = mDisplay.getWidth();
		mHeight = mDisplay.getHeight();
		createMultiListener();
		mShape = new Shape();
		cOver = new Canvas();
		
		paint = new Paint();
		paint.setColor(Color.WHITE); 
		paint.setAlpha(0xFF); 

		
	}
	
	public void onDrawFrame(GL10 gl) {
		reShuffle();
		nanoTimeStart = System.nanoTime();
		// if ((nanoTimeStart - nanoTimeEnd) < 16670000) return;
		// Log.d("DrawTime: ", (nanoTimeStart - nanoTimeEnd)
		// + "\nDrawFrame:" + onDrawFrameCounter);
		onDrawFrameCounter++;
		// play with these
		// if (onDrawFrameCounter < 60) {
		// angle+=0.005f;
		// }
		// else angle = 0;
		// angle += Offseto[0];
		// angle += 0.5f;
		// float x = Offseto[1];
		// float x = 1;
		// float y = Offseto[2];
		// float y = 1;
		// float z = 0;
		// if (onDrawFrameCounter % 10 == 0) {
		// y++;
		// z++;
		// }
		// xangle = Offseto[0];
		// yangle = Offseto[1];
		// zangle = Offseto[2];
		// make the following conditional as to if/where to place the object
		gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
		gl.glLoadIdentity(); // Reset The Current Modelview Matrix
		// gl.glTranslatef(0.0f +OffsetX, -1.2f +OffsetY, -9.0f + OffsetZ);//
		// this is affecting the size of the square, thus also affecting what
		// part of is visible
		// gl.glTranslatef(0.0f, -1.2f, -9.0f);
		// gl.glMatrixMode(GL10.GL_PROJECTION);
		// gl.glMultMatrixf(Rotato, 0);
		// gl.glRotatef(xangle, 1, 0, 0);
		// gl.glTranslatef(0.0f, -1.2f, -9.0f);
		// gl.glLoadIdentity();
		// gl.glRotatef(yangle, 0, 1, 0);
		// gl.glLoadIdentity();
		// gl.glRotatef(zangle, 0, 0, 1);
		// gl.glLoadIdentity();
		// gl.glRotatef(0, 0, 0, 0);
		// gl.glRotatef(accAngle, Offseto[0], Offseto[1], Offseto[2]);

		// gl.glMatrixMode(GL10.GL_MODELVIEW); // Select The Modelview Matrix

		// gl.glLoadIdentity();
		// gl.glRotatef(angle, x, y, z);
		// gl.glLoadMatrixf(Rotato, 0);

		gl.glMultMatrixf(Rotato, 0);
		mShape.draw(gl, Offseto[0], Offseto[1], Offseto[2]); // Draw the square
		// gl.glRotatef(xangle, 1, 0, 0);
		

		nanoTimeEnd = System.nanoTime();
		// angle = 0;
	}

	public void onSurfaceChanged(GL10 gl, int width, int height) {
		if (height == 0) {
			height = 1;
		}
		gl.glViewport(0, 0, width, height); // set these to different positions
											// and they'll be offset
		gl.glMatrixMode(GL10.GL_PROJECTION);
		gl.glLoadIdentity(); // documentation says its the same as calling
								// glLoadMatrix()
								// but i have yet to see it
		// gl.glLoadMatrixf(mShape.vertexBuffer);

		GLU.gluPerspective(gl, 45.0f, (float) width / (float) height, 0.1f,
				100.0f);

		gl.glMatrixMode(GL10.GL_MODELVIEW); // Select The Modelview Matrix
		// gl.glLoadMatrixf(Rotato, 0);
		gl.glLoadIdentity(); // Reset The Modelview Matrix

	}

	public void onSurfaceCreated(GL10 gl, EGLConfig config) {
		// gl.glViewport(0, 0, width, height);
		// gl.glMatrixMode(GL10.GL_PROJECTION);
		// gl.glLoadIdentity();

		// gl.glMatrixMode(GL10.GL_MODELVIEW);
		// gl.glEnable(GL10.GL_CULL_FACE);
		// gl.glShadeModel(GL10.GL_SMOOTH); // Enable Smooth Shading
		// gl.glClearColor(0.0f, 0.0f, 0.0f, 0.5f); // Black Background
		// gl.glClearDepthf(1.0f);

		// gl.glEnable(GL10.GL_DEPTH_TEST);

		// gl.glDepthFunc(GL10.GL_ALWAYS);

		// gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT, GL10.GL_FASTEST);
		gl.glShadeModel(GL10.GL_SMOOTH); // Enable Smooth Shading
		gl.glClearColor(0.0f, 0.0f, 0.0f, 0.0f); // Black Background
		gl.glClearDepthf(1.0f); // Depth Buffer Setup
		gl.glEnable(GL10.GL_DEPTH_TEST); // Enables Depth Testing
		gl.glDepthFunc(GL10.GL_ALWAYS); // The Type Of Depth Testing To Do

		// Really Nice Perspective Calculations
		gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT, GL10.GL_FASTEST);

		SensorManager.remapCoordinateSystem(Temptato, SensorManager.AXIS_X,
				SensorManager.AXIS_Z, Rotato);

		Accfirsttime = true;
		Magfirsttime = true;
	}

	public void onPreviewFrame(byte[] yuvs, Camera camera) {
		previewStart = System.nanoTime();
		boolean reg = false;
		int bwCounter = 0;
		int yuvsCounter = 0;
		crimonacounter = 0;
		//cOver = sHolder.lockCanvas();
		for (int y = 0; y < 160; y++) {
			for (int x = 0; x < 240; x++) {
				
				if (yuvs[(y*240 + x) ] < 28) {
					crimona[crimonacounter] = (160 - y)*2;
					crimonacounter++;
					crimona[crimonacounter] = x*2;
					crimonacounter++;
					reg = true;
				}
				
				
			}
		}
		//nativeSobel(yuvs, 240, 160, mFrameSobel);
		//crimona = mFrameSobel.array().clone();
		//cOver.drawPoints(crimona, paint);
		//sHolder.unlockCanvasAndPost(cOver);

		previewEnd = System.nanoTime();
		//if (reg) Log.d("Previews: ", 1e09 / (previewEnd - previewStart) + "Preview FPS");
		Log.d("Previews: ", ": " + yuvs.length);
	}

	public void onResume() {
		super.onResume();
		mSensor.registerListener(myAccListener, AccSensor,
				SensorManager.SENSOR_DELAY_GAME);
		mSensor.registerListener(myMagListener, MagSensor,
				SensorManager.SENSOR_DELAY_GAME);

	}

	public void onPause() {
		super.onPause();
		mSensor.unregisterListener(myAccListener);
		mSensor.unregisterListener(myMagListener);
	}

	private void createMultiListener() {
		// Log.d("Create:", "ass.");
		myMagListener = new SensorEventListener() {

			public void onAccuracyChanged(Sensor sensor, int accuracy) {

			}

			public void onSensorChanged(SensorEvent event) {
				synchronized (this) {
					if ((event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD)) {

						MagTValues = event.values.clone();
						MagTTime = System.nanoTime();
						MagOValues = MagNValues.clone();
						MagOTime = MagNTime;
						MagNValues = MagTValues.clone();
						MagNTime = MagTTime;

						magCounter++;
					}
				}
			}
		};
		myAccListener = new SensorEventListener() {

			public void onAccuracyChanged(Sensor sensor, int accuracy) {

			}

			public void onSensorChanged(SensorEvent event) {
				synchronized (this) {
					if ((event.sensor.getType() == Sensor.TYPE_ACCELEROMETER)) {

						AccTValues = event.values.clone();
						AccTTime = System.nanoTime();
						AccOValues = AccNValues.clone();
						AccOTime = AccNTime;
						AccNValues = AccTValues.clone();
						AccNTime = AccTTime;

						accCounter++;
						/*
						 * if (accCounter%10 == 0) {
						 * 
						 * Log.d("Sens", "CPU Time: " + System.nanoTime() +
						 * " onSensorChanged: " + event.sensor.getType() +
						 * " Orientation: " + mDisplay.getOrientation() +
						 * ",\n x: " + (AccTValues[0]) + ", y: " +
						 * (AccTValues[1]) + ", z: " + (AccTValues[2]) +
						 * "\n Sensor :" + event.timestamp + "\nACompass:" +
						 * Oriento[0]*RAD2DEGS + "\nACompass:" +
						 * Oriento[1]*RAD2DEGS + "\nACompass:" +
						 * Oriento[2]*RAD2DEGS + "\nAInclino:" + Inclingle ); }
						 */
					}
				}
			}
		};
	}
	
	private void reShuffle() {
		SensorManager
				.getRotationMatrix(Rotato, Inclino, AccNValues, MagNValues);
		// SensorManager.getRotationMatrix(Temptato, Inclino, AccNValues,
		// MagNValues);

		// SensorManager.remapCoordinateSystem(Temptato, SensorManager.AXIS_X,
		// SensorManager.AXIS_Z, Rotato);
		SensorManager.getOrientation(Rotato, Oriento);
		Inclingle = SensorManager.getInclination(Inclino);

		if (Inclinger != Inclingle) {
			Inclinger -= Inclingle;
		} else
			Inclinger = 0;

		/*
		 * if (Orientofirsttime) { Offseto[0] = Oriento[0]*RAD2DEGS; Offseto[1]
		 * = Oriento[1]*RAD2DEGS; Offseto[2] = Oriento[2]*RAD2DEGS;
		 * Orientofirsttime = false; } else { if (Oriento[0] != Ostarto[0]) {
		 * Offseto[0] -= Oriento[0]*RAD2DEGS/360; Indicat1 = true; } else
		 * Offseto[0] = 0; if (Oriento[1] != Ostarto[1]) { Offseto[1] -=
		 * Oriento[1]*RAD2DEGS/360; Indicat2 = true; } else Offseto[1] = 0; if
		 * (Oriento[2] != Ostarto[2]) { Offseto[2] -= Oriento[2]*RAD2DEGS/360;
		 * Indicat3 = true; } else Offseto[2] = 0; }
		 */
		// if (Accfirsttime && Magfirsttime) {
		// SensorManager.remapCoordinateSystem(Rotato, SensorManager.AXIS_X,
		// SensorManager.AXIS_Y, Temptato);
		// Rotato = Temptato.clone();
		// }

		if (Offseto[0] != Ostarto[0])
			Offseto[0] = Oriento[0] * RAD2DEGS - Ostarto[0];
		else
			Offseto[0] = 0;
		if (Offseto[1] != Ostarto[1])
			Offseto[1] = Oriento[1] * RAD2DEGS - Ostarto[1];
		else
			Offseto[1] = 0;
		if (Offseto[2] != Ostarto[2])
			Offseto[2] = Oriento[2] * RAD2DEGS - Ostarto[2];
		else
			Offseto[2] = 0;

		// else Offseto[0] = 0;
		// Offseto[1] = Oriento[1]*RAD2DEGS - Ostarto[1];
		// Offseto[2] = Oriento[2]*RAD2DEGS - Ostarto[2];

		int j = 0;
		OldAccP = CurrentAccP.clone();
		CurrentAccP[0] = (float) (AccNValues[0] - 9.8 * (Math.sin(Oriento[0])));
		CurrentAccP[1] = (float) (AccNValues[1] - 9.8 * (Math.sin(Oriento[1])));
		CurrentAccP[2] = (float) (AccNValues[2]
				- (9.8 * (Math.sin(Oriento[1]))) + Math.sin(Oriento[0]));
		TempAccP[0] = (CurrentAccP[0] - OldAccP[0]);
		TempAccP[1] = (CurrentAccP[1] - OldAccP[1]);
		TempAccP[2] = (CurrentAccP[2] - OldAccP[2]);
		AccbTime = (float) ((AccNTime - AccOTime) / 1e8);

		if (Math.abs(TempAccP[0]) > 0.05f) {
			OffsetX = AccbTime * TempAccP[0];
		}
		if (Math.abs(TempAccP[1]) > 0.05f) {
			OffsetY = AccbTime * TempAccP[1];
		}
		if (Math.abs(TempAccP[2]) > 0.05f) {
			OffsetZ = AccbTime * TempAccP[2];
		}

		/*if (magCounter % 10 == 0) {
			Log.d("Sens", "CPU Time: "
					+ System.nanoTime()
					// + " GeoSensrChanged: " + event.sensor.getType()
					+ "\nAccelerometer: "
					+ ", x: "
					+ (AccNValues[0])
					+ ", y: "
					+ (AccNValues[1])
					+ ", z: "
					+ (AccNValues[2])
					+ "\nSurplusX:"
					+ CurrentAccP[0]
					+ ",SurplusY: "
					+ CurrentAccP[1]
					+ ",SurplusZ: "
					+ CurrentAccP[2]
					// + "\nGeoSensor"
					// + " Orientation: " + mDisplay.getOrientation()
					// + ", x: " + (MagNValues[0])
					// + ", y: " + (MagNValues[1])
					// + ", z: " + (MagNValues[2])
					// + "\n GeoSens:" + event.timestamp
					+ "\nGCompass:" + Oriento[0] * RAD2DEGS + "\nGCompass:"
					+ Oriento[1] * RAD2DEGS + "\nGCompass:"
					+ Oriento[2] * RAD2DEGS + "\nRotato0: " + Rotato[0]
					+ ", Rotato1: " + Rotato[1] + ", Rotato2: " + Rotato[2]
					+ ", Rotato3: " + Rotato[3] + "\nRotato4: " + Rotato[4]
					+ ", Rotato5: " + Rotato[5] + ", Rotato6: " + Rotato[6]
					+ ", Rotato7: " + Rotato[7] + "\nRotato8: " + Rotato[8]
					+ ", Rotato9: " + Rotato[9] + ", Rotato10: " + Rotato[10]
					+ ", Rotato11: " + Rotato[11] + "\nRotato12: " + Rotato[12]
					+ ", Rotato13: " + Rotato[13] + ", Rotato14: " + Rotato[14]
					+ ", Rotato15: " + Rotato[15] + "\n\nOffset0 : "
					+ Offseto[0] + ", Offset1 : " + Offseto[1] + ", Offset2 : "
					+ Offseto[1]

			// + "\nGInclino:" + Inclingle
			);
		}*/
		Ostarto = Offseto.clone();

	}

	private int findSpine() {
		if (Math.abs(AccTValues[0]) > Math.abs(AccTValues[1]))
			if (Math.abs(AccTValues[0]) > Math.abs(AccTValues[2]))
				if (AccTValues[0] > 0)
					return 1;
				else
					return -1;
			else if (Math.abs(AccTValues[2]) > Math.abs(AccTValues[1]))
				if (AccTValues[2] > 0)
					return 3;
				else
					return -3;
			else if (Math.abs(AccTValues[1]) > Math.abs(AccTValues[2]))
				if (AccTValues[1] > 0)
					return 2;
				else
					return -2;
			else if (AccTValues[2] > 0)
				return 3;
			else
				return -3;
		return 0;
	}

	private int checklowest() {
		int returnvalue;

		if (Math.abs(AccTValues[0]) > 0.3f)
			if (Math.abs(AccTValues[1]) > 0.3f)
				if (Math.abs(AccTValues[2]) > 0.3f)
					return 0;
				else
					return 1;
			else if (Math.abs(AccTValues[2]) > 0.3f)
				return 2;
			else
				return 3;
		else if (Math.abs(AccTValues[1]) < 0.3f)
			if (Math.abs(AccTValues[2]) > 0.3f)
				return 4;
			else
				return 5;
		else if (Math.abs(AccTValues[2]) > 0.3f)
			return 6;
		else
			return 7;
	}

	/*
	 * if (AccTValues[0] > 3) if (AccTValues[1] > 3) if (AccTValues[2] > 3)
	 * return 0; else if (AccTValues[2] < -3) return 1; else return 2; else if
	 * (AccTValues[1] < 3) if (AccTValues[1] < -3) if (AccTValues[2] > 3) return
	 * 3; else if (AccTValues[2] < -3) return 4; else return 5;
	 * 
	 * 
	 * return 0;
	 */

	/*
	 * int i = 0;
	 * 
	 * do { // tempValues[i] = //-event.values[i]/SensorManager.GRAVITY_EARTH;
	 * AccTValues[i] = -event.values[i]/9.8f; AccOValues[i] = AccNValues[i];
	 * AccNValues[i] = AccTValues[i]; i++; } while (i < 3);
	 */

	/*
	 * int i = 0; do { MagTValues[i] = -event.values[i]; MagOValues[i] =
	 * MagNValues[i]; MagNValues[i] = MagTValues[i]; i++; } while (i < 3);
	 */
	// MagOValues1= MagOValues2.clone();
	// MagOTime1 = MagOTime2;
	// MagOValues2= MagOValues3.clone();
	// MagOTime2 = MagOTime3;
	// MagOValues3= MagOValues4.clone();
	// MagOTime3 = MagOTime4;
	// MagOValues4 = MagNValues.clone();
	// MagOTime4 = MagNTime;
	// MagSecx1 = (MagOValues1[0] )/MagOTime1;
	// MagSecx = (MagNValues[0]-MagOValues[0])/((MagNTime - MagOTime)/100000);
	// MagSecy = (MagNValues[1]-MagOValues[1])/((MagNTime - MagOTime)/100000);
	// MagSecz = (MagNValues[2]-MagOValues[2])/((MagNTime - MagOTime)/100000);
	// AccOValues1= AccOValues2.clone();
	// AccOTime1 = AccOTime2;
	// AccOValues2= AccOValues3.clone();
	// AccOTime2 = AccOTime3;
	// AccOValues3= AccOValues4.clone();
	// AccOTime3 = AccOTime4;
	// AccOValues4= AccNValues.clone();
	// AccOTime4 = AccNTime;
	// long startTree = System.nanoTime();
	// long endTree = System.nanoTime();
	// long startSpine = System.nanoTime();
	// long endSpine = System.nanoTime();
	// Log.d("Returned LowType", Integer.toString(checklow)
	// + "\nTime to iterate tree :" + (endTree - startTree)
	// + "\nTime to iterate spine:" + (endSpine - startSpine));
	// OffsetX += (AccTValues[0]-AccOValues[0]);
	// OffsetY += (AccTValues[1]-AccOValues[1]);
	// OffsetZ += (AccTValues[2]-AccOValues[2]);

	/*
	 * switch (0) { case 0: break; case 1: OffsetX +=
	 * (AccTValues[0]-AccOValues[0]); OffsetY += (AccTValues[1]-AccOValues[1]);
	 * OffsetZ += (AccTValues[2]-AccOValues[2]); case 2: OffsetX +=
	 * (AccTValues[0]-AccOValues[0]); OffsetY += (AccTValues[1]-AccOValues[1]);
	 * OffsetZ += (AccTValues[2]-AccOValues[2]); case 3: OffsetX +=
	 * (AccTValues[0]-AccOValues[0]); OffsetY += (AccTValues[1]-AccOValues[1]);
	 * OffsetZ += (AccTValues[2]-AccOValues[2]); case -1: OffsetX +=
	 * (AccTValues[0]-AccOValues[0]); OffsetY += (AccTValues[1]-AccOValues[1]);
	 * OffsetZ += (AccTValues[2]-AccOValues[2]); case -2: OffsetX +=
	 * (AccTValues[0]-AccOValues[0]); OffsetY += (AccTValues[1]-AccOValues[1]);
	 * OffsetZ += (AccTValues[2]-AccOValues[2]); case -3: OffsetX +=
	 * (AccTValues[0]-AccOValues[0]); OffsetY += (AccTValues[1]-AccOValues[1]);
	 * OffsetZ += (AccTValues[2]-AccOValues[2]); }
	 * 
	 * switch (0) { case 0: break; //accAngle = case 1: case 2: case 4: OffsetY
	 * += (AccTValues[0]-AccOValues[0]); OffsetX +=
	 * -(AccTValues[1]-AccOValues[1]); //OffsetY += AccTValues[0]/10; //OffsetX
	 * += -AccTValues[1]/10; if (OffsetX == 0) OffsetY = 0.1f; //accAngle =
	 * (float) Math.tan
	 * ((AccTValues[1]-AccOValues[1]/AccTValues[0]-AccOValues[0])); accAngle =
	 * (float) Math.tan(OffsetY/OffsetX); Log.d("AccAngle",
	 * Float.toString(accAngle));
	 * 
	 * break; case 3: case 5: case 6: case 7: };
	 */
	/*
	 * int checklow = checklowest(); int briefSpine = findSpine(); if
	 * (accCounter > 1) { if (tempSpine == briefSpine) SpineChanged = false;
	 * else { SpineChanged = true; tempSpine = briefSpine; } } else { tempSpine
	 * = briefSpine; };
	 */

}
