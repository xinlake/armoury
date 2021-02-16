package armoury.ready.vision;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import armoury.common.XinText;
import armoury.vision.ZXingEncoder;
import armoury.vision.ZXingFormat;
import xinlake.armoury.ready.R;

public class ZxingEncodeRunner implements View.OnClickListener {
    private final Activity activity;

    public ZxingEncodeRunner(Activity activity) {
        this.activity = activity;
    }

    @Override
    public void onClick(View view) {
        final View layout = View.inflate(activity, R.layout.dialog_zxing_encode, null);
        final EditText editText = layout.findViewById(R.id.zxing_encode_text);
        final ImageView qrImage = layout.findViewById(R.id.zxing_encode_image);

        AlertDialog alertDialog = new AlertDialog.Builder(activity)
            .setView(layout)
            .setTitle("Generate QR Image")
            .setNeutralButton("Generate", null)
            .setPositiveButton("Run", null)
            .show();

        alertDialog.getButton(DialogInterface.BUTTON_NEUTRAL).setOnClickListener(button -> {
            String ip = XinText.generateIp();
            editText.setText(ip);
        });

        alertDialog.getButton(DialogInterface.BUTTON_POSITIVE).setOnClickListener(button -> {
            String content = editText.getText().toString();
            Bitmap bitmap = ZXingEncoder.encodeText(content, ZXingFormat.QR_CODE, 400);
            qrImage.setImageBitmap(bitmap);
        });
    }
}
