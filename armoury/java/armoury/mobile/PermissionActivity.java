package armoury.mobile;

import android.app.Activity;
import android.content.pm.PackageManager;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.HashMap;


/**
 * @author XinLake
 * @version 2020-09
 */
public abstract class PermissionActivity extends Activity {
    public interface Listener {
        void onPermissionGranted();
        void onPermissionDenied(String[] permissionsDenied);
    }

    private int CODE_REQUEST_PERMISSIONS = 100;
    private final HashMap<Integer, Listener> mListeners = new HashMap<>();

    /**
     * Check and requestPermissions. The results will be returned from the listener call
     * @param listener The callback when all permission requests are completed
     */
    protected void acquirePermissions(@NonNull String[] permissionsRequested, @NonNull Listener listener) {
        ArrayList<String> permissionsNotGranted = new ArrayList<>();
        for (String permission : permissionsRequested) {
            if (checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) {
                permissionsNotGranted.add(permission);
            }
        }

        if (permissionsNotGranted.size() > 0) {
            ++CODE_REQUEST_PERMISSIONS;
            mListeners.put(CODE_REQUEST_PERMISSIONS, listener);

            String[] request = permissionsNotGranted.toArray(new String[0]);
            requestPermissions(request, CODE_REQUEST_PERMISSIONS);
        } else {
            listener.onPermissionGranted();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (mListeners.containsKey(requestCode)) {
            ArrayList<String> permissionsDenied = new ArrayList<>();
            for (int i = 0; i < grantResults.length; ++i) {
                if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                    permissionsDenied.add(permissions[i]);
                }
            }

            Listener listener = mListeners.remove(requestCode);
            if (permissionsDenied.size() > 0) {
                //permission denied by user
                assert listener != null;
                listener.onPermissionDenied(permissionsDenied.toArray(new String[0]));
            } else {
                //permission granted
                assert listener != null;
                listener.onPermissionGranted();
            }
        }
    }
}
