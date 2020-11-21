package armoury.mobile;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.NonNull;


/**
 * todo - check
 * Definition dialog styles
 * @author XinLake
 * @version 2020.08
 */
public abstract class TransparentDialog extends Dialog {
    private static final String Tag = TransparentDialog.class.getSimpleName();
    private final int mBgColor;

    public TransparentDialog(@NonNull Context context, int bgColor) {
        super(context);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        mBgColor = bgColor;
    }

    @Override
    public void onStart() {
        super.onStart();

        Window dialogWindow = getWindow();
        if (dialogWindow != null) {
            android.graphics.Point size = new android.graphics.Point();
            dialogWindow.getWindowManager().getDefaultDisplay().getSize(size);

            //set window attributes
            if (size.x > size.y) {
                // horizontal
                dialogWindow.getAttributes().width = (int) ((size.x + size.y) * 0.4);
            } else {
                //vertical
                dialogWindow.getAttributes().width = (int) (size.x * 0.8);
            }

            // The background must be set by Window.setBackground
            dialogWindow.setGravity(Gravity.CENTER);
            dialogWindow.setBackgroundDrawable(new ColorDrawable(mBgColor));
            dialogWindow.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        } else {
            Log.e(Tag, "onStart, getWindow() returns null");
        }
    }
}
