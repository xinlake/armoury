package xinlake.armoury.ready;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;

import com.google.android.material.switchmaterial.SwitchMaterial;

import armoury.vision.CameraXActivity;

public class CameraXRunner implements View.OnClickListener {
    private final Activity activity;

    public CameraXRunner(Activity activity) {
        this.activity = activity;
    }

    @Override
    public void onClick(View view) {
        final View layout = View.inflate(activity, R.layout.dialog_camerax, null);
        final EditText editPrefix = layout.findViewById(R.id.camerax_prefix);
        final SwitchMaterial switchFacing = layout.findViewById(R.id.camerax_facing);
        final RadioButton radioZxing = layout.findViewById(R.id.camerax_zxing);

        DialogInterface.OnClickListener clickRun = (dialog, which) -> {
            int facing = switchFacing.isChecked() ? CameraXActivity.FACING_FONT : CameraXActivity.FACING_BACK;
            String analyzer = radioZxing.isChecked() ? CameraXActivity.ANALYZER_ZXING : CameraXActivity.ANALYZER_MLKIT;

            Intent intent = new Intent(activity, CameraXActivity.class);
            intent.putExtra(CameraXActivity.KEY_FACING, facing);
            intent.putExtra(CameraXActivity.KEY_ANALYZER, analyzer);
            intent.putExtra(CameraXActivity.KEY_PREFIX, editPrefix.getText().toString());

            activity.startActivityForResult(intent, ActivityMain.ACTION_CAMERAX);
        };

        new AlertDialog.Builder(activity)
            .setView(layout)
            .setTitle("Camera X")
            .setNeutralButton("Run", clickRun)
            .setNegativeButton("Close", (dialog, which) -> dialog.dismiss())
            .show();
    }
}
