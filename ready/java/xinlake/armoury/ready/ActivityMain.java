package xinlake.armoury.ready;

import android.Manifest;
import android.os.Bundle;
import android.util.Log;

import java.util.List;

import armoury.mobile.PermissionActivity;
import armoury.network.WifiInterface;

public class ActivityMain extends PermissionActivity {
    private static final String Tag = ActivityMain.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // permission
        findViewById(R.id.button_acquire_permission).setOnClickListener(view -> {
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
        });

        // wifi ip address
        findViewById(R.id.button_get_wifi_address).setOnClickListener(view -> {
            List<WifiInterface> wifiInterfaces = WifiInterface.getIpAddress();
        });
    }
}
