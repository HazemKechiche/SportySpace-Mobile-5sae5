package com.example.blogappll;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.os.Environment;
import android.util.Log;

import com.example.blogappll.Entity.Terrain;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class PdfGenerator {
    private static final String TAG = "PdfGenerator";

    public static File generateTerrainPdf(Context context, Terrain terrain) {
        // 1. Create PDF Document
        PdfDocument pdfDocument = new PdfDocument();
        Paint paint = new Paint();

        // 2. Add a page with dimensions
        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(300, 600, 1).create();
        PdfDocument.Page page = pdfDocument.startPage(pageInfo);
        Canvas canvas = page.getCanvas();

        // 3. Write data to PDF
        canvas.drawText("Terrain Details", 80, 50, paint);
        canvas.drawText("Title: " + terrain.title, 50, 100, paint);
        canvas.drawText("Description: " + terrain.description, 50, 150, paint);
        canvas.drawText("Price: " + terrain.prix, 50, 200, paint);

        pdfDocument.finishPage(page);

        // 4. Save PDF to device storage
        File file = new File(context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), "TerrainDetails.pdf");

        try {
            // Write to file and log the file path
            pdfDocument.writeTo(new FileOutputStream(file));
            Log.d(TAG, "PDF saved to: " + file.getAbsolutePath());
        } catch (IOException e) {
            Log.e(TAG, "Error writing PDF", e);
        } finally {
            pdfDocument.close();
        }

        return file;
    }
}
