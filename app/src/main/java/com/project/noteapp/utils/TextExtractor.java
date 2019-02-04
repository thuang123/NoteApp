package com.project.noteapp.utils;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.util.Log;
import android.util.SparseArray;

import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Image to text document conversion and processing.
 */
public class TextExtractor {

    private final static String TAG = "TextExtractor";

    private Context context;
    private File storageDir;

    public TextExtractor(Context context, File storageDir) {
        this.context = context;
        this.storageDir = storageDir;
    }

    public void generateTextFileFromImage(Bitmap imageData) {
        if(imageData != null) {
            TextRecognizer textRecognizer = new TextRecognizer.Builder(this.context).build();
            Frame imageFrame = new Frame.Builder()
                    .setBitmap(imageData)
                    .build();
            SparseArray<TextBlock> textBlocks = textRecognizer.detect(imageFrame);
            try {
                String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
                File newTxtFile = new File(this.storageDir, "TXT_" + timeStamp + ".txt");
                newTxtFile.createNewFile();
                FileOutputStream fOut = new FileOutputStream(newTxtFile, true);
                OutputStreamWriter outWriter = new OutputStreamWriter(fOut);
                for (int i = 0; i < textBlocks.size(); i++) {
                    TextBlock textBlock = textBlocks.get(textBlocks.keyAt(i));
                    outWriter.append(textBlock.getValue());
                }
                outWriter.flush();
                outWriter.close();
                dispatchSendEmailIntent(newTxtFile, timeStamp);
            } catch(IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void dispatchSendEmailIntent(File textFile, String timeStamp) {
        if (textFile != null && timeStamp != null && !timeStamp.equals("")) {
            Intent intent = new Intent(Intent.ACTION_SENDTO);
            intent.setType("text/plain");
            intent.putExtra(Intent.EXTRA_SUBJECT, "NoteApp: Text Data from Capture Image Data " + timeStamp);
            intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(textFile));
            intent.putExtra(Intent.EXTRA_TEXT, "Attached text data converted from captured NoteApp image on " + timeStamp + ".");
            intent.setData(Uri.parse("mailto:"));
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            this.context.startActivity(intent);
        } else {
            Log.d(TAG, "Failed to initialize email sending, invalid setup parameters.");
        }
    }
}
