package armoury.ready.network;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import armoury.common.XinText;
import armoury.network.GeoLocation;
import xinlake.armoury.ready.R;

public class GeoLocationRunner implements View.OnClickListener {
    private final Activity activity;
    private final GeoLocation geoLocation = new GeoLocation();

    public GeoLocationRunner(Activity activity) {
        this.activity = activity;
    }

    @Override
    public void onClick(View view) {
        final View layout = View.inflate(activity, R.layout.dialog_geo_location, null);
        final EditText editAddress = layout.findViewById(R.id.geo_address);
        final TextView textResult = layout.findViewById(R.id.geo_result);

        AlertDialog alertDialog = new AlertDialog.Builder(activity)
            .setView(layout)
            .setTitle("Geo location")
            .setNeutralButton("Generate", null)
            .setPositiveButton("Find", null)
            .show();

        Button buttonNeutral = alertDialog.getButton(DialogInterface.BUTTON_NEUTRAL);
        buttonNeutral.setOnClickListener(button -> {
            String ip = XinText.generateIp();
            editAddress.setText(ip);
        });

        alertDialog.getButton(DialogInterface.BUTTON_POSITIVE).setOnClickListener(button -> {
            button.setEnabled(false);
            buttonNeutral.setEnabled(false);
            editAddress.setEnabled(false);

            final String address = editAddress.getText().toString();
            new Thread(() -> {
                final String location = geoLocation.fromWebsite(address);
                activity.runOnUiThread(() -> {
                    if (location != null) {
                        textResult.setText(location);
                    }

                    button.setEnabled(true);
                    buttonNeutral.setEnabled(true);
                    editAddress.setEnabled(true);
                });
            }, "geo-location").start();
        });
    }
}
