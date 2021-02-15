package armoury.vision;

import android.media.Image;

import androidx.annotation.NonNull;
import androidx.camera.core.ImageAnalysis;
import androidx.camera.core.ImageProxy;

import com.google.android.gms.tasks.Task;
import com.google.mlkit.vision.barcode.Barcode;
import com.google.mlkit.vision.barcode.BarcodeScanner;
import com.google.mlkit.vision.barcode.BarcodeScanning;
import com.google.mlkit.vision.common.InputImage;

import java.util.List;


/**
 * MLKit may need internet connection to work properly
 * @author XinLake
 * @version 2020.08
 */
final class MLBarcodeAnalyzer implements ImageAnalysis.Analyzer {
    private static final String Tag = MLBarcodeAnalyzer.class.getSimpleName();

    private final String matchPrefix;
    private final IBarcodeResult listener;

    public MLBarcodeAnalyzer(String matchPrefix, @NonNull IBarcodeResult listener) {
        this.matchPrefix = matchPrefix;
        this.listener = listener;
    }

    @Override
    @androidx.camera.core.ExperimentalGetImage
    public void analyze(@NonNull ImageProxy imageProxy) {
        Image mediaImage = imageProxy.getImage();
        if (mediaImage != null) {
            InputImage image = InputImage.fromMediaImage(mediaImage, imageProxy.getImageInfo().getRotationDegrees());
            BarcodeScanner scanner = BarcodeScanning.getClient();

            Task<List<Barcode>> result = scanner.process(image)
                .addOnSuccessListener(barCodes -> {
                    for (Barcode barcode : barCodes) {
                        String rawValue = barcode.getRawValue();
                        if (rawValue == null) {
                            continue;
                        }

                        if (matchPrefix == null || rawValue.startsWith(matchPrefix)) {
                            listener.onMatch(rawValue);
                        }
                    }
                })
                .addOnFailureListener(exception -> {
                    // Task failed with an exception
                    exception.printStackTrace();
                })
                .addOnCompleteListener(task -> {
                    mediaImage.close();
                    imageProxy.close();
                });
        }
    }
}
