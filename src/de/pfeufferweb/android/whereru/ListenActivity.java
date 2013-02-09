package de.pfeufferweb.android.whereru;

import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class ListenActivity extends Activity {

	private LocationManager locationManager;
	private Button retrieveLocationButton;
	private EditText responseText;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_listen);
		retrieveLocationButton = (Button) findViewById(R.id.retrieve_location_button);
		responseText = (EditText) findViewById(R.id.responseText);
		locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

		retrieveLocationButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				showCurrentLocation();
			}
		});
	}

	private void showCurrentLocation() {
		Toast.makeText(ListenActivity.this, "Getting Location",
				Toast.LENGTH_LONG);
		Location location = locationManager
				.getLastKnownLocation(LocationManager.GPS_PROVIDER);

		if (location != null) {
			String message = String.format(
					"Current Location \n Longitude: %1$s \n Latitude: %2$s",
					location.getLongitude(), location.getLatitude());
			Toast.makeText(ListenActivity.this, message, Toast.LENGTH_LONG)
					.show();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_listen, menu);
		return true;
	}
}
