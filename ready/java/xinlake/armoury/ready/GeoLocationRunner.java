package xinlake.armoury.ready;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import armoury.network.GeoLocation;

public class GeoLocationRunner implements View.OnClickListener {
    private final Activity activity;
    private final GeoLocation geoLocation = new GeoLocation();

    public GeoLocationRunner(Activity activity) {
        this.activity = activity;
    }

    @Override
    public void onClick(View view) {
        final View layout = View.inflate(activity, R.layout.dialog_geo_location, null);
        final EditText editText = layout.findViewById(R.id.geo_address);
        final TextView textView = layout.findViewById(R.id.geo_location);

        AlertDialog alertDialog = new AlertDialog.Builder(activity)
            .setView(layout)
            .setTitle("Geo location")
            .setNeutralButton("Get", null)
            .setNegativeButton("Close", (dialog, which) -> dialog.dismiss())
            .create();

        alertDialog.show();

        alertDialog.getButton(DialogInterface.BUTTON_NEUTRAL).setOnClickListener(button -> {
            button.setEnabled(false);
            editText.setEnabled(false);

            final String address = editText.getText().toString();
            new Thread(() -> {
                final String location = geoLocation.fromWebsite(address);
                activity.runOnUiThread(() -> {
                    if (location != null) {
                        textView.setText(location);
                    }

                    button.setEnabled(true);
                    editText.setEnabled(true);
                });
            }, "geo-location").start();
        });
    }
}
