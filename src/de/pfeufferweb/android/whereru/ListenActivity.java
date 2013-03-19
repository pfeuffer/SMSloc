package de.pfeufferweb.android.whereru;

import java.util.ArrayList;
import java.util.List;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.ToggleButton;

public class ListenActivity extends ListActivity {
	private RequestRepository datasource;

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

		datasource = new RequestRepository(this);
		datasource.open();

		List<String> values = render(datasource.getAllRequests());

		// Use the SimpleCursorAdapter to show the
		// elements in a ListView
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, values);
		setListAdapter(adapter);
	}

	private List<String> render(List<Request> requests) {
		List<String> result = new ArrayList<String>(requests.size());
		for (Request request : requests) {
			result.add(request.toString(this));
		}
		return result;
	}

	@Override
	protected void onStart() {
		datasource.open();
		boolean active = Settings.getActive(this);
		toggleButton.setChecked(active);
		triggerText.setText(Settings.getRequestText(this));
		setTriggerActivated(active);
		List<String> values = render(datasource.getAllRequests());
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, values);
		setListAdapter(adapter);
		Log.d("ListenActivity", "" + Settings.getSeconds(this));
		super.onStart();
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

	@Override
	protected void onPause() {
		datasource.close();
		super.onPause();
	}

	private void setTriggerActivated(boolean activated) {
		int visibility = activated ? View.VISIBLE : View.INVISIBLE;
		triggerText.setVisibility(visibility);
		triggerOnText.setVisibility(visibility);
		triggerOffText
				.setVisibility(!activated ? View.VISIBLE : View.INVISIBLE);
	}
}
