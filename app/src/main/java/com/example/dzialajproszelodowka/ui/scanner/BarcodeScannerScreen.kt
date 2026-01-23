package com.example.dzialajproszelodowka.ui.scanner

import android.util.Log
import android.view.ViewGroup
import androidx.annotation.OptIn
import androidx.camera.core.CameraSelector
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.common.InputImage
import java.util.concurrent.Executors

@Composable
fun BarcodeScannerScreen(
    onBarcodeDetected: (String) -> Unit,
    onCancel: () -> Unit
) {
    val context = LocalContext.current
    // kiedy ekran żyje, żeby kamera widziała kiedy się wyłączyć
    val lifecycleOwner = LocalLifecycleOwner.current
    var isScanning by remember { mutableStateOf(true) }

    Box(modifier = Modifier.fillMaxSize()) {
        if (isScanning) {
            // AndroidView, bo w Compose nie ma kamery
            AndroidView(
                // funckaj tworząca widok kamery
                factory = { ctx ->
                    // ekran z aparatem
                    val previewView = PreviewView(ctx).apply {
                        layoutParams = ViewGroup.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.MATCH_PARENT
                        )
                    }

                    // tworzy osobny wątek dla obsługi kamery
                    val cameraExecutor = Executors.newSingleThreadExecutor()
                    // zapytanie czy można użyć kamery
                    val cameraProviderFuture = ProcessCameraProvider.getInstance(ctx)

                    // to się wykona jak kamera będzie gotowa
                    cameraProviderFuture.addListener({
                        // kontrola nad kamerą, włącza wyłącza podgląd itd
                        val cameraProvider = cameraProviderFuture.get()

                        // pokazuje widok z kamery na ekranie
                        val preview = Preview.Builder().build().also {
                            it.setSurfaceProvider(previewView.surfaceProvider)
                        }

                        val imageAnalyzer = ImageAnalysis.Builder()
                            // analizuj tylko najnowszą klatkę
                            .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                            .build()
                            .also { analysis ->
                                // dla każdej klatki sprawdza w ML Kit czy jest tu kod kreskowy
                                // przekazuje ten kod kreskowy do odpowiedniej funkcji
                                analysis.setAnalyzer(cameraExecutor, { imageProxy ->
                                    processImageProxy(imageProxy) { barcode ->
                                        if (isScanning) {
                                            isScanning = false
                                            onBarcodeDetected(barcode)
                                        }
                                    }
                                })
                            }

                        val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

                        try {
                            cameraProvider.unbindAll() // odpina poprzednie usecasy kamery
                            cameraProvider.bindToLifecycle( // podłącza kamerę do cyklu życia ekranu
                                lifecycleOwner, // żeby kamera sama się włączała
                                cameraSelector,
                                preview,
                                imageAnalyzer // analiza kodów
                            )
                        } catch (exc: Exception) {
                            Log.e("Scanner", "Camera error", exc)
                        }
                    }, ContextCompat.getMainExecutor(ctx))

                    previewView
                },
                modifier = Modifier.fillMaxSize()
            )
        }

        Button(
            onClick = onCancel,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(32.dp)
        ) {
            Text("Cancel Scanning")
        }
    }
}

// analiza pojedynczej klatki z kamery
@OptIn(ExperimentalGetImage::class)
private fun processImageProxy(
    imageProxy: ImageProxy,
    onBarcodeFound: (String) -> Unit
) {
    @androidx.camera.core.ExperimentalGetImage
    val mediaImage = imageProxy.image // pobiera obraz
    if (mediaImage != null) {
        // tworzy format wymagany przez ml kit
        val image = InputImage.fromMediaImage(mediaImage, imageProxy.imageInfo.rotationDegrees)
        // pobiera skaner z ml kit
        val scanner = BarcodeScanning.getClient()

        scanner.process(image)
            .addOnSuccessListener { barcodes ->
                for (barcode in barcodes) {
                    barcode.rawValue?.let { code ->
                        onBarcodeFound(code)
                        return@addOnSuccessListener
                    }
                }
            }
            .addOnFailureListener {
            }
            .addOnCompleteListener {
                imageProxy.close()
            }
    } else {
        imageProxy.close()
    }
}