package com.parkour.occular;

import java.io.IOException;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ImageFormat;
import android.graphics.Paint;
import android.graphics.Rect;
import android.hardware.Camera;
import android.hardware.Camera.PreviewCallback;
import android.util.Log;
import android.view.Display;
import android.view.SurfaceHolder;
import android.view.SurfaceView;


/**
 * This class handles the camera. In particular, the method setPreviewCallback
 * is used to receive camera images. The camera images are not processed in this
 * class but delivered to the GLLayer. This class itself does not display the
 * camera images.
 * 
 * @author Niels
 * 
 */
public class CamLayer extends SurfaceView implements SurfaceHolder.Callback,
		PreviewCallback {
	Camera mCamera;
	boolean isPreviewRunning = false;
	Camera.PreviewCallback callback;
	List<Camera.Size> sizeologist;
	int width, height;
	Canvas cOver;
	SurfaceHolder sHolder;
	Paint pt;
	protected Display mDisplay;

	Bitmap bmp;
	BitmapFactory.Options BitFacOpt;
	static {
		System.loadLibrary("ndkfoo");
	}
	private native void nativeSobel (byte[] frame,
			int width,
			int height,
			float[] crimona);

	private native void nativeSobel (byte[] frame,
			int width,
			int height,
			int[] crimona);

	

	CamLayer(Context context, Camera.PreviewCallback callback) {
		super(context);
		this.callback = callback;


		// Install a SurfaceHolder.Callback so we get notified when the
		// underlying surface is created and destroyed.
		sHolder = getHolder();
		sHolder.addCallback(this);
		//sHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		// don't actually need this right now because the camera preview is hidden
		
		pt  = new Paint ();
		pt.setStyle(Paint.Style.FILL);
		pt.setStrokeWidth(2);
		pt.setARGB(0xAA, 0xFF, 0x22, 0x33);
		//BitFacOpt = new BitmapFactory.Options();

	}

	// mPreview.setLayoutParams(new LayoutParams(100,100))

	public void surfaceCreated(SurfaceHolder holder) {
		synchronized (this) {
			mCamera = Camera.open();

			Camera.Parameters p = mCamera.getParameters();
			sizeologist = p.getSupportedPreviewSizes();
			//for (int i = 0; i < sizeologist.size(); i++) {
				//Log.d("Params:", "PreviewSize: "+ sizeologist.get(i).width +" x " + sizeologist.get(i).height );
			//}
			Log.d("Params:", "preview rate:" + p.getSupportedPreviewFrameRates());
			Log.d("Params:", "formatpreview" + p.getSupportedPreviewFormats());
			Log.d("Params:", "preview size:" + p.getPreviewSize().width + " x " + p.getPreviewSize().height);
			width = 240;
			height = 160;
			p.setPreviewSize(width, height);
			
			p.setPreviewFrameRate(15);
			//p.setPreviewFormat(ImageFormat.RGB_565);

			mCamera.setParameters(p);
			mCamera.setDisplayOrientation(90);

			/*try {
				mCamera.setPreviewDisplay(holder);
			} catch (IOException e) {
				Log.e("Camera", "mCamera.setPreviewDisplay(holder);");
			}*/

			int i = 0;
			//int[] formatlist = new int[];
			//List<Integer> formatlist = p.getSupportedPreviewFormats(); 
			mCamera.startPreview();
			mCamera.setPreviewCallback(this);
			sHolder = holder;
			//Log.d("Params:", "PreviewSize: " + p.getPreviewSize().height +" x " + p.getPreviewSize().width);
			//Log.d("Params:", "SceneMode:   " + p.getSceneMode());
			/*do {
			Log.d("Params:", "PreviewFormat" + formatlist.get(i));
			i++;
			} while (i < formatlist.size());
			Log.d("Params:", "Preview size: "+ formatlist.size());
			*/
		}
	}

	public void surfaceDestroyed(SurfaceHolder holder) {
		// Surface will be destroyed when we return, so stop the preview.
		// Because the CameraDevice object is not a shared resource, it's very
		// important to release it when the activity is paused.
		synchronized (this) {
			try {
				if (mCamera != null) {
					mCamera.setPreviewCallback(null);
					mCamera.stopPreview();
					isPreviewRunning = false;
					mCamera.release();
				}
			} catch (Exception e) {
				Log.e("Camerafail", e.getMessage());
			}
		}
	}

	public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {
		//Log.d("surfaceChanged", "change occurred");
	}

	public void onPreviewFrame(byte[] arg0, Camera arg1) {
		//int crimonacounter = 0;
		//float[] crimona = new float[76800];
		int[] crimona = new int[width*height*2];
		
		//cOver = sHolder.lockCanvas(new Rect(0, 0, height*2, width*2));
		cOver = sHolder.lockCanvas();
		nativeSobel(arg0, width, height, crimona);

		Rect src = new Rect(0, 0, width, height); 
		Rect dst = new Rect(0, 0, cOver.getWidth()*2, cOver.getHeight()*2); 
		
		Bitmap bmp = Bitmap.createBitmap(crimona, width, height, Bitmap.Config.RGB_565);
		//Bitmap bmp = Bitmap.createScaledBitmap(crimona, height, width, Bitmap.Config.ARGB_8888);
		cOver.drawBitmap(bmp, src, dst, pt);
		//cOver.rotate(90);

		//cOver.drawPoints(crimona, pt);
		//bmp = BitmapFactory.decodeByteArray(arg0, 0, 57600);
		//Bitmap bimp = Bitmap.createBitmap(BitmapFactory.decodeByteArray(arg0, 0, arg0.length), 0, 0, 240, 160, Bitmap.Config.RGB_565);
		
		//cOver.drawBitmap(bimp, 0, 0, pt);
		//bmp = null;

		sHolder.unlockCanvasAndPost(cOver);
		//crimona = null;

	}
}
//nativeSobel(arg0, width, height, crimona);
//if (callback != null)
//callback.onPreviewFrame(arg0, arg1);
/*for (int y = 0; y < 160; y++) {
for (int x = 0; x < 240; x++) {
	
	if (arg0[(y*240 + x) ] < 28) {
		crimona[crimonacounter] = (160 - y)*2;
		crimonacounter++;
		crimona[crimonacounter] = x*2;
		crimonacounter++;

	}
	
	
}
}*/
