package de.pfeufferweb.android.whereru;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;

public class ListenActivity extends Activity {

	private EditText responseText;
	private CheckBox activateBox;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_listen);
		responseText = (EditText) findViewById(R.id.responseText);
		responseText.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				SharedPreferences prefs = getSharedPreferences("Settings",
						Context.MODE_PRIVATE);
				String newSmsText = responseText.getText().toString();
				Log.d("ListenActivity", newSmsText);
				Editor editor = prefs.edit();
				editor.putString(Settings.TRIGGER_TEXT, newSmsText);
				editor.commit();
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			@Override
			public void afterTextChanged(Editable arg0) {
			}
		});
		activateBox = (CheckBox) findViewById(R.id.activateCheckBox);
		activateBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				SharedPreferences prefs = getSharedPreferences(
						Settings.SETTINGS, Context.MODE_PRIVATE);
				String newSmsText = responseText.getText().toString();
				Log.d("ListenActivity", newSmsText);
				Editor editor = prefs.edit();
				editor.putBoolean(Settings.ACTIVE, isChecked);
				editor.commit();
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_listen, menu);
		return true;
	}
}
