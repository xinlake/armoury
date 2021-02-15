package xinlake.armoury.ready;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;

import armoury.common.Logger;
import armoury.mobile.PermissionActivity;
import armoury.mobile.picker.ImagePickActivity;
import armoury.vision.CameraXActivity;

public class ActivityMain extends PermissionActivity {
    private static final String Tag = ActivityMain.class.getSimpleName();
    public static final int ACTION_CAMERAX = 100;
    public static final int ACTION_PICK_IMAGE = 200;

    // permission
    private final View.OnClickListener clickPermission = view -> {
        acquirePermissions(new String[]{Manifest.permission.CAMERA}, new Listener() {
            @Override
            public void onPermissionGranted() {
                Toast.makeText(ActivityMain.this,
                    "onPermissionGranted",
                    Toast.LENGTH_LONG).show();
            }

            @Override
            public void onPermissionDenied(String[] permissionsDenied) {
                Toast.makeText(ActivityMain.this,
                    "onPermissionDenied",
                    Toast.LENGTH_LONG).show();
            }
        });
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
        } else if (requestCode == ACTION_PICK_IMAGE) {
            if (resultCode == RESULT_OK && data != null) {
                ArrayList<Uri> uriList = data.getParcelableArrayListExtra(ImagePickActivity.KEY_RESULT);

                StringBuilder stringBuilder = new StringBuilder();
                if (uriList != null && uriList.size() > 0) {
                    for (Uri uri : uriList) {
                        stringBuilder.append(uri.toString()).append("\r\n");
                    }
                } else {
                    stringBuilder.append("Invalid selection");
                }

                new AlertDialog.Builder(this)
                    .setTitle("Images")
                    .setMessage(stringBuilder)
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
        findViewById(R.id.main_pick_image).setOnClickListener(new PickImageRunner(this));

        findViewById(R.id.main_get_wifi_address).setOnClickListener(new WifiRunner(this));
        findViewById(R.id.main_get_geo_location).setOnClickListener(new GeoLocationRunner(this));

        findViewById(R.id.main_scanner).setOnClickListener(new CameraXRunner(this));
    }
}
