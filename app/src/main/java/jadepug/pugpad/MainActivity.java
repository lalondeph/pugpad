package jadepug.pugpad;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

import com.google.android.material.snackbar.Snackbar;

import java.io.OutputStream;
import java.util.Objects;

/**
 * MainActivity hosts the running logic for the app.
 * Here we define elements on the screen and
 * capture / apply user input
 */
public class MainActivity extends AppCompatActivity {

    private DrawingView dv;
    private View activity_main;

    private Button  btnYellow,
                    btnRed,
                    btnGreen,
                    btnPurple,
                    btnBlue,
                    btnGray,
                    btnBlack,
                    btnWhite,
                    btnSmall,
                    btnMed,
                    btnLarge,
                    selectedColorBtn,
                    selectedSizeBtn;

    private final String smileText = "\u263B";

    /**
     * onCreate links variables with on-screen elements and
     * assigns them to various listeners. It also sets the initial
     * value for variables the user will later reassign.
     *
     * @param savedInstanceState : A mapping from String keys to various Parcelable values
     */
    @RequiresApi(api = Build.VERSION_CODES.Q)
    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Objects.requireNonNull(getSupportActionBar()).hide();

        activity_main = findViewById(R.id.activity_main);
        dv =  findViewById(R.id.drawingView);
        dv.setDrawingCacheEnabled(true);

        btnYellow = findViewById(R.id.btnYellow);
        btnRed = findViewById(R.id.btnRed);
        btnGreen = findViewById(R.id.btnGreen);
        btnPurple = findViewById(R.id.btnPurple);
        btnBlue = findViewById(R.id.btnBlue);
        btnGray = findViewById(R.id.btnGray);
        btnBlack = findViewById(R.id.btnBlack);
        btnWhite = findViewById(R.id.btnWhite);
        btnSmall = findViewById(R.id.btnSmall);
        btnMed = findViewById(R.id.btnMed);
        btnLarge = findViewById(R.id.btnLarge);
        Button btnSave = findViewById(R.id.btnSave);
        Button btnClear = findViewById(R.id.btnClear);
        Button btnUndo = findViewById(R.id.btnUndo);
        Button btnRedo = findViewById(R.id.btnRedo);

        btnYellow.setOnClickListener(colorListener);
        btnRed.setOnClickListener(colorListener);
        btnGreen.setOnClickListener(colorListener);
        btnPurple.setOnClickListener(colorListener);
        btnBlue.setOnClickListener(colorListener);
        btnGray.setOnClickListener(colorListener);
        btnBlack.setOnClickListener(colorListener);
        btnWhite.setOnClickListener(colorListener);

        btnYellow.setBackgroundColor(StrokeColor.getYELLOW());
        btnRed.setBackgroundColor(StrokeColor.getRED());
        btnGreen.setBackgroundColor(StrokeColor.getGREEN());
        btnPurple.setBackgroundColor(StrokeColor.getPURPLE());
        btnBlue.setBackgroundColor(StrokeColor.getBLUE());
        btnGray.setBackgroundColor(StrokeColor.getGRAY());
        btnBlack.setBackgroundColor(StrokeColor.getBLACK());
        btnWhite.setBackgroundColor(StrokeColor.getWHITE());

        btnSmall.setOnClickListener(brushListener);
        btnMed.setOnClickListener(brushListener);
        btnLarge.setOnClickListener(brushListener);

        selectedColorBtn = btnWhite;
        selectedColorBtn.setText(smileText);
        selectedSizeBtn = btnSmall;
        selectedSizeBtn.setBackgroundColor(StrokeColor.getWHITE());

        /*
         * This listener Saves the canvas to a file
         */
        btnSave.setOnClickListener(view -> {
            Snackbar.make(activity_main, R.string.saving_image,
                    Snackbar.LENGTH_SHORT)
                    .show();

            //getting the bitmap from DrawView class
            Bitmap bmp = dv.viewToBitmap();
            //opening a OutputStream to write into the file
            OutputStream imageOutStream = null;

            ContentValues cv = new ContentValues();
            //name of the file
            cv.put(MediaStore.Images.Media.DISPLAY_NAME, "drawing.png");
            //type of the file
            cv.put(MediaStore.Images.Media.MIME_TYPE, "image/png");
            //location of the file to be saved
            cv.put(MediaStore.Images.Media.RELATIVE_PATH, Environment.DIRECTORY_PICTURES);

            //ge the Uri of the file which is to be v=created in the storage
            Uri uri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, cv);
            try {
                //open the output stream with the above uri
                imageOutStream = getContentResolver().openOutputStream(uri);
                //this method writes the files in storage
                bmp.compress(Bitmap.CompressFormat.PNG, 100, imageOutStream);
                //close the output stream after use
                imageOutStream.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        /*
         * This listener clears out the paths Arraylist
         * and prompts the canvas to redraw
         */
        btnClear.setOnClickListener(view -> {
            dv.paths.clear();
            dv.invalidate();
        });

        /*
         * This listener removes the last path
         * from the paths Arraylist
         * and prompts the canvas to redraw
         */
        btnUndo.setOnClickListener(view -> {
            if(dv.paths.size() > 0) {
                dv.undoPath();
                dv.invalidate();
            }
        });

        /*
         * This listener restores the the last
         * removed path from the paths Arraylist
         * and prompts the canvas to redraw
         */
        btnRedo.setOnClickListener(view -> {
            if(dv.undone_paths.size() > 0) {
                dv.redoPath();
                dv.invalidate();
            }
        });

        /*
         * Called on user touch event. This listener
         * turns touch input into points on a path.
         */
        dv.setOnTouchListener((view, motionEvent) -> {
            int action = motionEvent.getActionMasked();

            switch (action) {
                case MotionEvent.ACTION_DOWN:
                    dv.beginPath(motionEvent.getX(), motionEvent.getY());
                    break;
                case MotionEvent.ACTION_MOVE:
                case MotionEvent.ACTION_UP:
                    dv.addPointToPath(motionEvent.getX(), motionEvent.getY());
                    break;
            }
            return true;
        });
    }

    /**
     * colorListener catches all clicks to the color swatch buttons
     * and changes the stroke color accordingly. This change does not effect
     * strokes already on the canvas.
     */
    View.OnClickListener colorListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            int selectedColor = view.getId();
            int newColor;

            selectedColorBtn.setText("");

            if(selectedColor == btnYellow.getId()) {
                newColor = StrokeColor.getYELLOW();
                selectedColorBtn = btnYellow;
            }
            else if(selectedColor == btnRed.getId()) {
                newColor = StrokeColor.getRED();
                selectedColorBtn = btnRed;
            }
            else if(selectedColor == btnGreen.getId()) {
                newColor = StrokeColor.getGREEN();
                selectedColorBtn = btnGreen;
            }
            else if(selectedColor == btnPurple.getId()) {
                newColor = StrokeColor.getPURPLE();
                selectedColorBtn = btnPurple;
            }
            else if(selectedColor == btnBlue.getId()) {
                newColor = StrokeColor.getBLUE();
                selectedColorBtn = btnBlue;
            }
            else if(selectedColor == btnGray.getId()) {
                newColor = StrokeColor.getGRAY();
                selectedColorBtn = btnGray;
            }
            else if(selectedColor == btnBlack.getId()) {
                newColor = StrokeColor.getBLACK();
                selectedColorBtn = btnBlack;
            }
            else {
                newColor = StrokeColor.getWHITE();
                selectedColorBtn = btnWhite;
            }

            dv.setDrawingColor(newColor);
            selectedColorBtn.setText(smileText);
        }
    };

    /**
     * brushListener captures button clicks on the 3 stroke size buttons
     * and changes the size of the stroke accordingly.
     * This change does not effect strokes already on the canvas.
     */
    View.OnClickListener brushListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            Object selectedSize = view.getTag();
            float newWidth;

            selectedSizeBtn.setBackgroundColor(0);

            if(selectedSize == btnMed.getTag()) {
                newWidth = StrokeSize.getMed();
                selectedSizeBtn = btnMed;
            }
            else if(selectedSize == btnLarge.getTag()) {
                newWidth = StrokeSize.getLARGE();
                selectedSizeBtn = btnLarge;
            }
            else {
                newWidth = StrokeSize.getSMALL();
                selectedSizeBtn = btnSmall;
            }

            dv.setDrawingWidth(newWidth);
            selectedSizeBtn.setBackgroundColor(StrokeColor.getWHITE());
        }
    };

    @RequiresApi(api = Build.VERSION_CODES.Q)
    public void saveDrawing(Bitmap bitmap, Context context) {

        // opening a OutputStream to write into the file
        OutputStream imageOutStream = null;

        ContentValues cv = new ContentValues();

        // name of the file
        cv.put(MediaStore.Images.Media.DISPLAY_NAME, "drawing.png");

        // type of the file
        cv.put(MediaStore.Images.Media.MIME_TYPE, "image/png");

        // location of the file to be saved
        cv.put(MediaStore.Images.Media.RELATIVE_PATH, Environment.DIRECTORY_PICTURES);

        // get the Uri of the file which is to be created in the storage
        Uri uri = context.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, cv);
        try {
            // open the output stream with the above uri
            imageOutStream = context.getContentResolver().openOutputStream(uri);

            // this method writes the files in storage
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, imageOutStream);

            // close the output stream after use
            imageOutStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}