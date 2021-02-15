package xinlake.armoury.ready;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import java.util.List;

import armoury.common.Logger;
import armoury.mobile.PermissionActivity;
import armoury.network.WifiInterface;
import armoury.vision.CameraXActivity;

public class ActivityMain extends PermissionActivity {
    private static final String Tag = ActivityMain.class.getSimpleName();
    public static final int ACTION_CAMERAX = 100;

    // permission
    private final View.OnClickListener clickPermission = view -> {
        acquirePermissions(new String[]{Manifest.permission.CAMERA}, new Listener() {
            @Override
            public void onPermissionGranted() {
                Log.i(Tag, "onPermissionGranted");
            }

            @Override
            public void onPermissionDenied(String[] permissionsDenied) {
                Log.i(Tag, "onPermissionDenied");
            }
        });
    };

    // wifi ip address
    private final View.OnClickListener clickWifiAddress = view -> {
        List<WifiInterface> wifiInterfaces = WifiInterface.getIpAddress();
        StringBuilder stringBuilder = new StringBuilder();
        if (wifiInterfaces.size() > 0) {
            for (WifiInterface wifiInterface : wifiInterfaces) {
                stringBuilder.append(wifiInterface.name).append("\r\n");

                for (String ip4 : wifiInterface.ip4List) {
                    stringBuilder.append(ip4).append("\r\n");
                }

                for (String ip6 : wifiInterface.ip6List) {
                    stringBuilder.append(ip6).append("\r\n");
                }
            }
        } else {
            stringBuilder.append("Wifi is not connected");
        }

        new AlertDialog.Builder(ActivityMain.this)
            .setTitle("Wifi address")
            .setMessage(stringBuilder)
            .setPositiveButton("OK", (dialog, which) -> {
                dialog.dismiss();
            })
            .show();
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == ACTION_CAMERAX) {
            if (resultCode == RESULT_OK && data != null) {
                String code = data.getStringExtra(CameraXActivity.KEY_RESULT);

                new AlertDialog.Builder(ActivityMain.this)
                    .setTitle("Code")
                    .setMessage(code)
                    .setPositiveButton("OK", (dialog, which) -> {
                        dialog.dismiss();
                    })
                    .show();
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        armoury.Core.init(getApplicationContext(), getFilesDir() + "/ready.log");
        Logger.write(Tag, "hello");

        setContentView(R.layout.activity_main);
        findViewById(R.id.main_acquire_permission).setOnClickListener(clickPermission);

        findViewById(R.id.main_get_wifi_address).setOnClickListener(clickWifiAddress);
        findViewById(R.id.main_get_geo_location).setOnClickListener(new GeoLocationRunner(this));

        findViewById(R.id.main_scanner).setOnClickListener(new CameraXRunner(this));
    }
}
