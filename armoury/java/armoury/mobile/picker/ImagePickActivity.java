package armoury.mobile.picker;

import android.Manifest;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ViewAnimator;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import armoury.mobile.PermissionActivity;
import xinlake.armoury.R;


/**
 * todo, select count, image viewer
 * Fragment show local videos, Support refresh
 * @author XinLake
 * @version 2020.09
 */
public class ImagePickActivity extends PermissionActivity {
    public static final String TAG = ImagePickActivity.class.getSimpleName();
    // parameters
    public static final String KEY_TITLE = "title";
    public static final String KEY_PRIMARY_COLOR = "primaryColor";
    public static final String KEY_MAX_SELECT = "maxSelect";
    /** Uri Parcelable ArrayList Extra */
    public static final String KEY_RESULT = "uriList";

    private ImagePickRVAdapter mImagePickRVAdapter;

    private final ImagePickRVAdapter.Listener onImagePick = image -> {
        // todo, view image
    };

    private void setupView() {
        List<ModelImage> imageList = ModelImage.loadLocalDB(getApplicationContext());

        ViewAnimator viewAnimator = findViewById(R.id.imagePick_viewAnimator);
        if (imageList.size() > 0) {
            viewAnimator.setDisplayedChild(0);
            mImagePickRVAdapter.setData(imageList);
        } else {
            viewAnimator.setDisplayedChild(1);
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.menuImagePick_confirm) {
            List<ModelImage> imageList = mImagePickRVAdapter.getSelection();

            ArrayList<Uri> uriList = new ArrayList<>();
            imageList.forEach(image -> uriList.add(image.getUri()));

            // set result
            setResult(Activity.RESULT_OK, new Intent() {{
                putExtra("name", TAG);
                putParcelableArrayListExtra(KEY_RESULT, uriList);
            }});

            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.image_pick, menu);
        return true;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_imagepick);

        // get parameters
        Intent intent = getIntent();
        final String title = intent.getStringExtra(KEY_TITLE);
        final int primaryColor = intent.getIntExtra(KEY_PRIMARY_COLOR, 0);
        final int maxSelect = intent.getIntExtra(KEY_MAX_SELECT, -1);
        // set parameters
        getWindow().setStatusBarColor(primaryColor);
        ActionBar actionBar = getActionBar();
        if (actionBar != null) {
            actionBar.setTitle(title);
            actionBar.setBackgroundDrawable(new ColorDrawable(primaryColor));
        }

        mImagePickRVAdapter = new ImagePickRVAdapter(this, 3, 0.8f, maxSelect, onImagePick);
        RecyclerView recyclerView = findViewById(R.id.imagePick_recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        recyclerView.setAdapter(mImagePickRVAdapter);

        setResult(Activity.RESULT_CANCELED);
        acquirePermissions(
            new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
            new PermissionActivity.Listener() {
                @Override
                public void onPermissionGranted() {
                    setupView();
                }

                @Override
                public void onPermissionDenied(String[] permissionsDenied) {
                    finish();
                }
            });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Glide.get(this).clearMemory();
    }
}
