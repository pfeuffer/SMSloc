package de.pfeufferweb.android.whereru;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.ToggleButton;

public class ListenActivity extends Activity {

	private TextView triggerText;
	private TextView triggerOnText;
	private TextView triggerOffText;
	private ToggleButton toggleButton;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_listen);
		triggerText = (TextView) findViewById(R.id.textViewTrigger);

		triggerOnText = (TextView) findViewById(R.id.textViewTriggerOn);
		triggerOffText = (TextView) findViewById(R.id.textViewTriggerOff);

		toggleButton = (ToggleButton) findViewById(R.id.activateToggleButton);
		toggleButton.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				Settings.setActive(ListenActivity.this, isChecked);
				setTriggerActivated(isChecked);
			}
		});
	}

	@Override
	protected void onStart() {
		super.onStart();
		boolean active = Settings.getActive(this);
		toggleButton.setChecked(active);
		triggerText.setText(Settings.getRequestText(this));
		setTriggerActivated(active);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_listen, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.preferences:
			startActivity(new Intent(this, MainPreferenceActivity.class));
			break;
		default:
			break;
		}
		return false;
	}

	private void setTriggerActivated(boolean activated) {
		int visibility = activated ? View.VISIBLE : View.INVISIBLE;
		triggerText.setVisibility(visibility);
		triggerOnText.setVisibility(visibility);
		triggerOffText.setVisibility(!activated ? View.VISIBLE : View.INVISIBLE);
	}
}
