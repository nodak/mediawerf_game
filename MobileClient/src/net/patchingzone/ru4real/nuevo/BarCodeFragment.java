package net.patchingzone.ru4real.nuevo;

import java.util.ArrayList;

import net.patchingzone.ru4real.R;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

public class BarCodeFragment extends Fragment {

	private static final String TAG = "Text";

	TextView key;

	Button bBarCode;

	Object[] values = new Object[2];

	private ArrayAdapter<String> mAdapter;
	private ArrayList<String> mStrings = new ArrayList<String>();

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		// ac.setContentView(R.layout.barcode);

		LinearLayout linearLayout = (LinearLayout) getActivity().findViewById(R.id.llbarcode);
		linearLayout.getBackground().mutate().setColorFilter(Color.BLUE, PorterDuff.Mode.MULTIPLY);

		bBarCode = (Button) getActivity().findViewById(R.id.bBarCode);

		bBarCode.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				String PACKAGE_NAME = "com.google.zxing.client.android";
				int versionCode = 0;
				String versionName = "unknown";
				try {
					PackageInfo info = getActivity().getPackageManager().getPackageInfo(PACKAGE_NAME, 0);
					versionCode = info.versionCode;
					versionName = info.versionName;

					Intent intent = new Intent("com.google.zxing.client.android.SCAN");
					intent.setPackage("com.google.zxing.client.android"); // This
					// is explicit - leave off to let other apps match
					// intent.putExtra("SCAN_MODE", "QR_CODE_MODE");
					startActivityForResult(intent, 0);

				} catch (PackageManager.NameNotFoundException e) {
					showDialog("No bar code scanner found", "Please, install the Zxing barcode scanner from the market");
				}

				// text.setText("");
			}
		});

	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent intent) {
		if (requestCode == 0) {
			if (resultCode == getActivity().RESULT_OK) {
				String contents = intent.getStringExtra("SCAN_RESULT");
				String format = intent.getStringExtra("SCAN_RESULT_FORMAT");
				// showDialog("ok", "Format: " + format + "\nContents: " +
				// contents);

				values[0] = format;
				values[1] = contents;
				//TODO send values 
				//osc.send(OSCMessageType.OSC_BARCODE, values);

			} else if (resultCode == getActivity().RESULT_CANCELED) {
				// showDialog("Sorry :(", "pq si");
			}
		}
	}

	private void showDialog(String title, String message) {
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setTitle(title);
		builder.setMessage(message);
		builder.setPositiveButton("To the market!", new DialogInterface.OnClickListener() {

			public void onClick(DialogInterface dialog, int which) {

				final Intent marketIntent = new Intent(android.content.Intent.ACTION_VIEW, Uri
						.parse("http://market.android.com/details?id=com.google.zxing.client.android"));
				startActivity(marketIntent);

				return;

			}
		});

		builder.setNegativeButton("Cancel", null);
		builder.show();

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		return inflater.inflate(R.layout.barcode, null);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();

	}

}
