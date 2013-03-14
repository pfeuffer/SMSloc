package de.pfeufferweb.android.whereru;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.NumberPicker.OnValueChangeListener;
import android.widget.ToggleButton;

public class ListenActivity extends Activity {

	private EditText responseText;
	private ToggleButton toggleButton;
	private Button helpButton;
	private NumberPicker seconds;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_listen);
		responseText = (EditText) findViewById(R.id.responseText);
		responseText.setText(Settings.getRequestText(this));
		responseText.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				String newRequestText = responseText.getText().toString();
				Settings.setRequestText(ListenActivity.this, newRequestText);
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			@Override
			public void afterTextChanged(Editable arg0) {
			}
		});
		toggleButton = (ToggleButton) findViewById(R.id.activateToggleButton);
		toggleButton.setChecked(Settings.getActive(this));
		toggleButton.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				Settings.setActive(ListenActivity.this, isChecked);
			}
		});

		helpButton = (Button) findViewById(R.id.helpButton);
		helpButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent i = new Intent(Intent.ACTION_VIEW);
				i.setData(Uri.parse(getString(R.string.helpUrl)));
				startActivity(i);
			}
		});

		seconds = (NumberPicker) findViewById(R.id.numberPicker1);
		seconds.setValue(Settings.getSeconds(this));
		seconds.setMinValue(15);
		seconds.setMaxValue(600);
		seconds.setOnValueChangedListener(new OnValueChangeListener() {
			@Override
			public void onValueChange(NumberPicker picker, int oldVal,
					int newVal) {
				int newSeconds = seconds.getValue();
				Settings.setSeconds(ListenActivity.this, newSeconds);
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
