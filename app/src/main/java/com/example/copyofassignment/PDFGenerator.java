package com.example.copyofassignment;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.widget.Toast;

import com.example.fintrack.db.AccountItem;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;

import java.io.File;
import java.io.OutputStream;
import java.util.List;

public class PDFGenerator {

    public static void createPDF(Context context, List<AccountItem> accountList) {
        OutputStream outputStream = null;
        Uri pdfUri = null;

        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                // Scoped Storage for Android 10 and above
                ContentValues values = new ContentValues();
                values.put(MediaStore.MediaColumns.DISPLAY_NAME, "AccountReport.pdf");
                values.put(MediaStore.MediaColumns.MIME_TYPE, "application/pdf");
                values.put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS + "/PDFReports");

                pdfUri = context.getContentResolver().insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, values);

                if (pdfUri != null) {
                    outputStream = context.getContentResolver().openOutputStream(pdfUri);
                }
            } else {
                // Legacy storage for Android 9 and below
                File pdfFolder = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "PDFReports");
                if (!pdfFolder.exists()) {
                    pdfFolder.mkdirs();
                }

                File pdfFile = new File(pdfFolder, "AccountReport.pdf");
                pdfUri = Uri.fromFile(pdfFile);
                outputStream = new java.io.FileOutputStream(pdfFile);
            }

            if (outputStream == null) {
                Toast.makeText(context, "Unable to create PDF file.", Toast.LENGTH_SHORT).show();
                return;
            }

            // Initialize PDFWriter and Document
            PdfWriter writer = new PdfWriter(outputStream);
            PdfDocument pdfDocument = new PdfDocument(writer);
            Document document = new Document(pdfDocument);

            // Add title
            Paragraph title = new Paragraph("Account Report")
                    .setBold()
                    .setFontSize(20)
                    .setMarginBottom(20)
                    .setTextAlignment(TextAlignment.CENTER);
            document.add(title);

            // Create a table with 8 columns
            float[] columnWidths = {2, 3, 2, 3, 2, 2, 2, 2};
            Table table = new Table(columnWidths);
            table.setWidth(UnitValue.createPercentValue(100));

            // Add table headers
            String[] headers = {"Type", "Remark", "Money", "Time", "Year", "Month", "Day", "Kind"};
            for (String header : headers) {
                table.addCell(new Cell().add(new Paragraph(header).setBold().setFontColor(ColorConstants.WHITE))
                        .setBackgroundColor(ColorConstants.BLUE));
            }

            // Populate the table
            for (AccountItem item : accountList) {
                table.addCell(item.getTypename());
                table.addCell(new Cell().add(new Paragraph(item.getRemark() != null ? item.getRemark() : "N/A")));
                table.addCell(String.valueOf(item.getMoney()));
                table.addCell(item.getTime());
                table.addCell(String.valueOf(item.getYear()));
                table.addCell(String.valueOf(item.getMonth()));
                table.addCell(String.valueOf(item.getDay()));
                table.addCell(item.getKind() == 0 ? "Expense" : "Income");
            }

            // Add table to the document
            document.add(table);

            // Close the document
            document.close();
            Toast.makeText(context, "PDF created successfully.", Toast.LENGTH_LONG).show();

            // Open the PDF
            openPdf(context, pdfUri);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(context, "Error creating PDF: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        } finally {
            try {
                if (outputStream != null) {
                    outputStream.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void openPdf(Context context, Uri uri) {
        try {
            // Create intent
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setDataAndType(uri, "application/pdf");
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION); // Grant permission

            // Start PDF viewer
            context.startActivity(Intent.createChooser(intent, "Open PDF"));
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(context, "Unable to open PDF", Toast.LENGTH_SHORT).show();
        }
    }
}
