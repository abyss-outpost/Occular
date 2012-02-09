package com.parkour.occular;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;


public class Entry extends Activity {

	Button button0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.entry);
		button0 = (Button) findViewById(R.id.button0);
		button0.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent I = new Intent(Entry.this, OccProto.class);
				I.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(I);
				finish();
			}

		});

	}

	protected void onDestroy() {
		super.onDestroy();
		finish();
	}
}
