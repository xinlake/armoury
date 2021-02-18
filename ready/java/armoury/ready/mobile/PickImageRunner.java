package armoury.ready.mobile;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.View;
import android.widget.EditText;

import armoury.mobile.picker.ImagePickActivity;
import armoury.ready.ActivityMain;
import xinlake.armoury.ready.R;

public class PickImageRunner implements View.OnClickListener {
    private final Activity activity;

    public PickImageRunner(Activity activity) {
        this.activity = activity;
    }

    @Override
    public void onClick(View view) {
        final View layout = View.inflate(activity, R.layout.dialog_pick_image, null);
        final EditText editMaxPick = layout.findViewById(R.id.pick_image_count);

        DialogInterface.OnClickListener clickRun = (dialog, which) -> {
            int maxPick = 0;
            try {
                maxPick = Integer.parseInt(editMaxPick.getText().toString());
            } catch (Exception ignored) {
            }

            final Intent intent = new Intent(activity, ImagePickActivity.class);
            if (maxPick > 0) {
                intent.putExtra(ImagePickActivity.KEY_MAX_SELECT, maxPick);
            }

            activity.startActivityForResult(intent, ActivityMain.ACTION_PICK_IMAGE);
        };

        new AlertDialog.Builder(activity)
            .setView(layout)
            .setTitle("Pick image")
            .setPositiveButton("Run", clickRun)
            .show();
    }
}
