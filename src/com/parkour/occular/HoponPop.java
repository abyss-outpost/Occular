package com.parkour.occular;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.opengl.GLSurfaceView.Renderer;
import android.view.View;

public class HoponPop extends View {
	private Paint paint;
	private Paint paint1;
	private Canvas holdcanvas;
	int counter = 0;
	//float[] crimona = new float[57600];
	//private GLLayerProto gimmick;
	private CamLayer Camule;
	//public HoponPop(Context context, GLLayerProto mySweep) {
	public HoponPop(Context context, CamLayer Camula) {
		super(context);
		paint  = new Paint ();
		paint.setStyle(Paint.Style.STROKE);
		paint.setStrokeWidth(10);
		paint.setARGB(0xAA, 0xFF, 0x22, 0x33);
		paint1  = new Paint ();
		paint1.setStyle(Paint.Style.FILL);
		paint1.setStrokeWidth(2);
		paint1.setARGB(0x55, 0xFF, 0x22, 0x33);

		//gimmick = mySweep;
		//crimona = mySweep.crimona;
		Camule = Camula;
	}

	private void drawBackground(Canvas canvas) {
		int height = getHeight();
		int width = getWidth();
		counter++;
		//lower left corner
		canvas.drawLine(width/2-width/10, height/2-height/10, width/2, height/2-height/9, paint);
		canvas.drawLine(width/2-width/10, height/2-height/10, width/2-width/9, height/2, paint);
		//upper left corner
		canvas.drawLine(width/2-width/10, height/2+height/10, width/2, height/2-height/9, paint);
		canvas.drawLine(width/2-width/10, height/2+height/10, width/2-width/9, height/2, paint);
		//upper right corner		
		//canvas.drawLine(width/2+width/10, height/2+height/10, width/2, height/2-height/10, paint);
		//canvas.drawLine(width/2+width/10, height/2+height/10, width/2-width/10, height/2, paint);
		//lower right corner
		//canvas.drawLine(width/2+width/10, height/2-height/10, width/2, height/2+height/10, paint);
		//canvas.drawLine(width/2+width/10, height/2-height/10, width/2+width/10, height/2, paint);

		//canvas.drawLine(width/2, height/2-50, width/2, height/2-25, paint);
		//canvas.drawLine(width/2+50, height/2, width/2+25, height/2, paint);
		//canvas.drawLine(width/2-50, height/2, width/2-25, height/2, paint);
		counter = 0;
		//canvas.drawLine(width/2, 0, width/2, (height/2)-(height/5), paint);
		//canvas.drawLine(width/2, (height/2)+(height/5), width/2, height, paint);
		//canvas.drawLine(0, height/2, (width/2)-(width/5), height/2, paint);
		//canvas.drawLine((width/2)+(width/5), height/2,width,height/2,paint);
		//canvas.drawCircle(width/2,height/2,(height/2)-(height/40), paint);
		canvas.drawCircle(width/2,height/2, 12, paint);
		//canvas.drawPoints(crimona, paint);
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		drawBackground(canvas);
		//holdcanvas = canvas;
		//canvas.drawPoints(gimmick.crimona, paint1);
		//canvas.drawPoints(Camule.crimona, paint1);
		super.postInvalidate();
		
	}
	
	@Override
	public void onWindowFocusChanged(boolean icanhasfocus) {
		if (!icanhasfocus) {
			//drawBackground(holdcanvas);
			//postInvalidate();
			//Mane.myLayout.removeView(Mane.TriciaTanaka);
			//Mane.myLayout.addView(Mane.TriciaTanaka);
		}
		else {
			//postInvalidate();
			//drawBackground(holdcanvas);
			//Mane.myLayout.removeView(Mane.TriciaTanaka);
			//Mane.myLayout.addView(Mane.TriciaTanaka);
		}
	}
}
