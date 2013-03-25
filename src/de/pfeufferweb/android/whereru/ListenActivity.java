package de.pfeufferweb.android.whereru;

import java.util.Date;
import java.util.List;
import java.util.Locale;

import android.app.ListActivity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MenuItem.OnMenuItemClickListener;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.ToggleButton;
import android.widget.TwoLineListItem;
import de.pfeufferweb.android.whereru.repository.LocationRequest;
import de.pfeufferweb.android.whereru.repository.RequestRepository;
import de.pfeufferweb.android.whereru.repository.Status;

public class ListenActivity extends ListActivity {

	static final String BROADCAST_NEW_REQUEST = "newRequest";

	private RequestRepository datasource;

	private TextView triggerText;
	private TextView triggerOnText;
	private TextView triggerOffText;
	private TextView historyText;
	private TextView noHistoryText;
	private ToggleButton toggleButton;

	private final BroadcastReceiver newRequestReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			fillRequests();
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_listen);
		triggerText = (TextView) findViewById(R.id.textViewTrigger);

		triggerOnText = (TextView) findViewById(R.id.textViewTriggerOn);
		triggerOffText = (TextView) findViewById(R.id.textViewTriggerOff);

		historyText = (TextView) findViewById(R.id.textViewHistory);
		noHistoryText = (TextView) findViewById(R.id.textViewNoHistory);

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

		this.getListView().setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				LocationRequest request = (LocationRequest) getListAdapter()
						.getItem(position);
				String s = request.toString(ListenActivity.this);
				Log.d("ListenActivity", "clicked " + s);
				if (request.getLocation() != null) {
					String uri = String.format(Locale.US, "geo:%f,%f?q=%f,%f",
							request.getLocation().getLatitude(), request
									.getLocation().getLongitude(), request
									.getLocation().getLatitude(), request
									.getLocation().getLongitude());
					startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(uri)));
				}
			}
		});
		registerForContextMenu(getListView());

		LocalBroadcastManager.getInstance(this).registerReceiver(
				newRequestReceiver,
				new IntentFilter(ListenActivity.BROADCAST_NEW_REQUEST));

		fillRequests();
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		if (v == getListView() && Settings.getActive(this)) {

			menu.add(getString(R.string.sendAgain)).setOnMenuItemClickListener(
					new OnMenuItemClickListener() {
						@Override
						public boolean onMenuItemClick(MenuItem item) {
							AdapterContextMenuInfo info = (AdapterContextMenuInfo) item
									.getMenuInfo();
							int index = info.position;
							LocationRequest request = (LocationRequest) getListAdapter()
									.getItem(index);
							Intent startService = new Intent(
									ListenActivity.this, SendService.class);
							startService.putExtra("receiver",
									request.getRequester());
							startService.putExtra("notificationId", -1);
							startService.putExtra("seconds",
									Settings.getSeconds(ListenActivity.this));
							ListenActivity.this.startService(startService);
							return true;
						}
					});
		}
	}

	@Override
	protected void onStart() {
		datasource.open();
		boolean active = Settings.getActive(this);
		toggleButton.setChecked(active);
		triggerText.setText(Settings.getRequestText(this));
		setTriggerActivated(active);
		fillRequests();
		Log.d("ListenActivity", "" + Settings.getSeconds(this));
		super.onStart();
	}

	private void fillRequests() {
		final List<LocationRequest> requests = datasource.getAllRequests();
		if (requests.isEmpty()) {
			noHistoryText.setVisibility(View.VISIBLE);
			historyText.setVisibility(View.INVISIBLE);
		} else {
			noHistoryText.setVisibility(View.INVISIBLE);
			historyText.setVisibility(View.VISIBLE);
		}
		ArrayAdapter<LocationRequest> adapter = new ArrayAdapter<LocationRequest>(
				this, android.R.layout.simple_list_item_2, requests) {
			@SuppressWarnings("deprecation")
			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				TwoLineListItem row;
				if (convertView == null) {
					LayoutInflater inflater = (LayoutInflater) getApplicationContext()
							.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
					row = (TwoLineListItem) inflater.inflate(
							android.R.layout.simple_list_item_2, null);
				} else {
					row = (TwoLineListItem) convertView;
				}
				LocationRequest request = requests.get(position);
				row.getText1().setTextColor(
						getResources().getColor(android.R.color.black));
				row.getText2().setTextColor(
						getResources().getColor(android.R.color.black));
				String success = stringForStatus(request.getStatus());
				String requester = request.getRequester();
				String dateOfRequest = DateFormat.getDateFormat(
						ListenActivity.this)
						.format(new Date(request.getTime()));
				String timeOfRequest = DateFormat.getTimeFormat(
						ListenActivity.this)
						.format(new Date(request.getTime()));
				row.getText1().setText(
						String.format(
								getString(R.string.locationListEntryHeader),
								dateOfRequest, timeOfRequest));
				row.getText2().setText(
						String.format(
								getString(R.string.locationListEntryFooter),
								requester, success));
				return row;
			}

			private String stringForStatus(Status status) {
				switch (status) {
				case NO_LOCATION:
					return getString(R.string.noLocation);
				case SUCCESS:
					return getString(R.string.locationFound);
				case RUNNING:
					return getString(R.string.running);
				default:
					throw new IllegalArgumentException("unknown status: "
							+ status);
				}
			}
		};
		setListAdapter(adapter);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_listen, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.preferences:
			startActivity(new Intent(this, MainPreferenceActivity.class));
			break;
		case R.id.clearRequests:
			clearRequests();
			fillRequests();
			break;
		default:
			break;
		}
		return false;
	}

	private void clearRequests() {
		this.datasource.deleteAllRequests();
	}

	@Override
	protected void onPause() {
		datasource.close();
		super.onPause();
	}

	@Override
	protected void onDestroy() {
		LocalBroadcastManager.getInstance(this).unregisterReceiver(
				newRequestReceiver);
		super.onDestroy();
	}

	private void setTriggerActivated(boolean activated) {
		int visibility = activated ? View.VISIBLE : View.INVISIBLE;
		triggerText.setVisibility(visibility);
		triggerOnText.setVisibility(visibility);
		triggerOffText
				.setVisibility(!activated ? View.VISIBLE : View.INVISIBLE);
	}
}
