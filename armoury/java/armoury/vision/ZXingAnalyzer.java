package armoury.vision;

import androidx.annotation.NonNull;
import androidx.camera.core.ImageAnalysis;
import androidx.camera.core.ImageProxy;

import com.google.zxing.BinaryBitmap;
import com.google.zxing.NotFoundException;
import com.google.zxing.PlanarYUVLuminanceSource;
import com.google.zxing.Result;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.QRCodeReader;

import java.nio.ByteBuffer;

/**
 * @author XinLake
 * @version 2020.12
 */
final class ZXingAnalyzer implements ImageAnalysis.Analyzer {
    private final String Tag = getClass().getSimpleName();

    private final String matchPrefix;
    private final IBarcodeResult listener;

    public ZXingAnalyzer(String matchPrefix, @NonNull IBarcodeResult listener) {
        this.matchPrefix = matchPrefix;
        this.listener = listener;
    }

    @Override
    public void analyze(@NonNull ImageProxy image) {
        final ByteBuffer buffer = image.getPlanes()[0].getBuffer();
        final byte[] imageBytes = new byte[buffer.remaining()];
        try {
            buffer.get(imageBytes);
        } catch (Exception exception) {
            exception.printStackTrace();
            return;
        }

        final int width = image.getWidth();
        final int height = image.getHeight();
        final PlanarYUVLuminanceSource source = new PlanarYUVLuminanceSource(imageBytes, width, height,
            0, 0, width, height, false);

        final BinaryBitmap zxingBinaryBitmap = new BinaryBitmap(new HybridBinarizer(source));
        try {
            final Result decodedBarcode = new QRCodeReader().decode(zxingBinaryBitmap);
            final String codeText = decodedBarcode.getText();
            if (matchPrefix == null || codeText.startsWith(matchPrefix)) {
                listener.onMatch(codeText);
            }
        } catch (NotFoundException ignored) {
        } catch (Exception exception) {
            exception.printStackTrace();
        }

        image.close();
    }
}
