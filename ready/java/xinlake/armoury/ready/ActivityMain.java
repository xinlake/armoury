package xinlake.armoury.ready;

import android.Manifest;
import android.os.Bundle;
import android.util.Log;

import armoury.mobile.PermissionActivity;

public class ActivityMain extends PermissionActivity {
    private static final String Tag = ActivityMain.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
    }
}
