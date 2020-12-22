package armoury.vision;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.camera.core.AspectRatio;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageAnalysis;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LifecycleRegistry;

import com.google.common.util.concurrent.ListenableFuture;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import armoury.mobile.PermissionActivity;
import xinlake.armoury.R;


/**
 * Detected qrcode will put into the result intent,
 * use Intent.getStringExtra("result") to get.
 */
public class CameraXActivity extends PermissionActivity implements LifecycleOwner {
    public static final String TAG = CameraXActivity.class.getSimpleName();
    // parameters
    public static final String KEY_FACING = "facing";
    public static final String KEY_ANALYZER = "analyzer";
    public static final String KEY_PREFIX = "prefix";
    public static final String KEY_RESULT = "qrCode";
    public static final String ANALYZER_ZXING = "zxing";
    public static final String ANALYZER_MLKIT = "mlkit";

    private int lensFacing;
    private String imageAnalyzer;
    private String codePrefix;

    private LifecycleRegistry lifecycleRegistry;
    private ExecutorService analyzeExecutor; //Blocking camera operations are performed using this executor
    private ProcessCameraProvider cameraProvider;

    private final IBarcodeResult barcodeListener = code -> {
        Intent intent = new Intent() {{
            putExtra("name", TAG);
            putExtra(KEY_RESULT, code);
        }};
        setResult(Activity.RESULT_OK, intent);
        finish();
    };

    /**
     * Enabled or disabled a button to switch cameras depending on the available cameras
     */
    private void updateCameraSwitchButton() {
        boolean enableSwitch = hasBackCamera() && hasFrontCamera();
        ImageButton buttonSwitchLens = findViewById(R.id.armouryCamera_buttonLens);
        buttonSwitchLens.setEnabled(enableSwitch);
        buttonSwitchLens.setOnClickListener(view -> {
            if (lensFacing == CameraSelector.LENS_FACING_FRONT) {
                lensFacing = CameraSelector.LENS_FACING_BACK;
            } else {
                lensFacing = CameraSelector.LENS_FACING_FRONT;
            }

            try {
                bindUseCases();
            } catch (Exception exception) {
                Log.e(TAG, "bindUseCases error", exception);
            }
        });
    }

    private void bindUseCases() {
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);

        int rotation = getWindowManager().getDefaultDisplay().getRotation();
        int screenAspectRatio = aspectRatio(metrics.widthPixels, metrics.heightPixels);

        // choose the camera by requiring a lens facing
        CameraSelector cameraSelector = new CameraSelector.Builder()
            .requireLensFacing(lensFacing)
            .build();

        // set up the view finder use case to display camera preview
        Preview preview = new Preview.Builder()
            .setTargetAspectRatio(screenAspectRatio)
            .setTargetRotation(rotation)
            .build();

        // set up image analysis
        ImageAnalysis imageAnalysis = new ImageAnalysis.Builder()
            .setTargetAspectRatio(screenAspectRatio)
            .setTargetRotation(rotation)
            .build();
        // select analyzer
        if (imageAnalyzer != null && imageAnalyzer.equals(ANALYZER_MLKIT)) {
            imageAnalysis.setAnalyzer(analyzeExecutor,
                new MLBarcodeAnalyzer(codePrefix, barcodeListener));
        } else {
            imageAnalysis.setAnalyzer(analyzeExecutor,
                new ZXingAnalyzer(codePrefix, barcodeListener));
        }

        // Attach use cases to the camera with the same lifecycle owner
        cameraProvider.unbindAll();
        cameraProvider.bindToLifecycle(
            this,
            cameraSelector,
            preview,
            imageAnalysis);

        // Connect the preview use case to the previewView
        PreviewView previewView = findViewById(R.id.armouryCamera_previewView);
        preview.setSurfaceProvider(previewView.getSurfaceProvider());
    }

    private void setupCamera() {
        ListenableFuture<ProcessCameraProvider> cameraProviderFuture = ProcessCameraProvider.getInstance(this);
        cameraProviderFuture.addListener(() -> {
            try {
                // camera provider is now guaranteed to be available
                cameraProvider = cameraProviderFuture.get();

                // select camera
                if (lensFacing == CameraSelector.LENS_FACING_BACK) {
                    if (!hasBackCamera()) {
                        throw new Exception("Back camera unavailable");
                    }
                } else if (lensFacing == CameraSelector.LENS_FACING_FRONT) {
                    if (!hasFrontCamera()) {
                        throw new Exception("Front camera unavailable");
                    }
                } else {
                    if (hasBackCamera()) {
                        lensFacing = CameraSelector.LENS_FACING_BACK;
                    } else if (hasFrontCamera()) {
                        lensFacing = CameraSelector.LENS_FACING_FRONT;
                    } else {
                        throw new Exception("Back and front camera are unavailable");
                    }
                }

                updateCameraSwitchButton();
                bindUseCases();
            } catch (Exception exception) {
                // Currently no exceptions thrown. cameraProviderFuture.get() should
                // not block since the listener is being called, so no need to
                // handle InterruptedException.
                Log.e(TAG, "setupCamera error", exception);
            }
        }, ContextCompat.getMainExecutor(this));
    }

    /**
     * Returns true if the device has an available back camera. False otherwise
     */
    private boolean hasBackCamera() {
        try {
            return cameraProvider.hasCamera(CameraSelector.DEFAULT_BACK_CAMERA);
        } catch (Exception ignored) {
            return false;
        }
    }

    /**
     * Returns true if the device has an available front camera. False otherwise
     */
    private boolean hasFrontCamera() {
        try {
            return cameraProvider.hasCamera(CameraSelector.DEFAULT_FRONT_CAMERA);
        } catch (Exception ignored) {
            return false;
        }
    }

    /**
     * [androidx.camera.core.ImageAnalysisConfig] requires enum value of
     * [androidx.camera.core.AspectRatio]. Currently it has values of 4:3 & 16:9.
     * Detecting the most suitable ratio for dimensions provided in @params by counting absolute
     * of preview ratio to one of the provided values.
     */
    private int aspectRatio(int width, int height) {
        float RATIO_4_3_VALUE = 4.0f / 3.0f;
        float RATIO_16_9_VALUE = 16.0f / 9.0f;
        float previewRatio = (float) Math.max(width, height) / Math.min(width, height);
        if (Math.abs(previewRatio - RATIO_4_3_VALUE) <= Math.abs(previewRatio - RATIO_16_9_VALUE)) {
            return AspectRatio.RATIO_4_3;
        }
        return AspectRatio.RATIO_16_9;
    }

    /**
     * Returns the Lifecycle of the provider.
     */
    @NonNull
    @Override
    public Lifecycle getLifecycle() {
        return lifecycleRegistry;
    }

    @Override
    public void onStart() {
        super.onStart();
        lifecycleRegistry.setCurrentState(Lifecycle.State.STARTED);
    }

    @Override
    public void onStop() {
        super.onStop();
        lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_STOP);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.armoury_activity_camera);

        // get parameters
        Intent intent = getIntent();
        lensFacing = intent.getIntExtra(KEY_FACING, -1);
        imageAnalyzer = intent.getStringExtra(KEY_ANALYZER);
        codePrefix = intent.getStringExtra(KEY_PREFIX);

        analyzeExecutor = Executors.newSingleThreadExecutor();
        lifecycleRegistry = new LifecycleRegistry(this);
        lifecycleRegistry.setCurrentState(Lifecycle.State.CREATED);

        setResult(Activity.RESULT_CANCELED);
        acquirePermissions(new String[]{Manifest.permission.CAMERA}, new PermissionActivity.Listener() {
            @Override
            public void onPermissionGranted() {
                setupCamera();
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
        lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_DESTROY);

        // Shut down background executor
        analyzeExecutor.shutdown();
    }
}
