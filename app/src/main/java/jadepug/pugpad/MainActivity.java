package jadepug.pugpad;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;

import java.io.OutputStream;
import java.util.Objects;

/**
 * MainActivity hosts the running logic for the app.
 * Here we define elements on the screen and
 * capture / apply user input
 *
 * Author: Philip lalonde
 * Organization: Jade Pug
 */
public class MainActivity extends AppCompatActivity {

    private DrawingView dv;
    private View activity_main;

    private Button btnYellow,
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
    private boolean saving = false;

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
        dv = findViewById(R.id.drawingView);
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
            if (!saving && dv.paths.size() > 0) {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage(R.string.confirm_save)
                        .setPositiveButton(R.string.save, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                saveCanvas();
                            }
                        })
                        .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) { }
                        });

                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

        /*
         * This listener clears out the paths Arraylist
         * and prompts the canvas to redraw
         */
        btnClear.setOnClickListener(view -> {
            AlertDialog.Builder clearBuilder = new AlertDialog.Builder(this);
            clearBuilder.setMessage(R.string.confirm_clear)
                        .setPositiveButton(R.string.clear, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dv.paths.clear();
                                dv.undone_paths.clear();
                                dv.invalidate();
                            }
                        })
                        .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) { }
                        });

            AlertDialog clearDialog = clearBuilder.create();
            clearDialog.show();
        });

        /*
         * This listener removes the last path
         * from the paths Arraylist
         * and prompts the canvas to redraw
         */
        btnUndo.setOnClickListener(view -> {
            if (dv.paths.size() > 0) {
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
            if (dv.undone_paths.size() > 0) {
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

            if (selectedColor == btnYellow.getId()) {
                newColor = StrokeColor.getYELLOW();
                selectedColorBtn = btnYellow;
            } else if (selectedColor == btnRed.getId()) {
                newColor = StrokeColor.getRED();
                selectedColorBtn = btnRed;
            } else if (selectedColor == btnGreen.getId()) {
                newColor = StrokeColor.getGREEN();
                selectedColorBtn = btnGreen;
            } else if (selectedColor == btnPurple.getId()) {
                newColor = StrokeColor.getPURPLE();
                selectedColorBtn = btnPurple;
            } else if (selectedColor == btnBlue.getId()) {
                newColor = StrokeColor.getBLUE();
                selectedColorBtn = btnBlue;
            } else if (selectedColor == btnGray.getId()) {
                newColor = StrokeColor.getGRAY();
                selectedColorBtn = btnGray;
            } else if (selectedColor == btnBlack.getId()) {
                newColor = StrokeColor.getBLACK();
                selectedColorBtn = btnBlack;
            } else {
                newColor = StrokeColor.getWHITE();
                selectedColorBtn = btnWhite;
            }

            dv.setCurrentStrokeColor(newColor);
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

            if (selectedSize == btnMed.getTag()) {
                newWidth = StrokeSize.getMed();
                selectedSizeBtn = btnMed;
            } else if (selectedSize == btnLarge.getTag()) {
                newWidth = StrokeSize.getLARGE();
                selectedSizeBtn = btnLarge;
            } else {
                newWidth = StrokeSize.getSMALL();
                selectedSizeBtn = btnSmall;
            }

            dv.setCurrentStrokeSize(newWidth);
            selectedSizeBtn.setBackgroundColor(StrokeColor.getWHITE());
        }
    };

    /**
     *
     */
    @RequiresApi(api = Build.VERSION_CODES.Q)
    private void saveCanvas() {
        saving = true;
        // Show file save message
        Snackbar.make(activity_main, R.string.saving_image,
                Snackbar.LENGTH_SHORT)
                .show();
        // Get bitmap from DrawView class
        Bitmap bmp = dv.viewToBitmap();
        // Open OutputStream to write into the file
        OutputStream imageOutStream;
        // Instantiate ContentView
        ContentValues cv = new ContentValues();
        // Set file name, type and save location
        cv.put(MediaStore.Images.Media.DISPLAY_NAME, "pug_pad.png");
        cv.put(MediaStore.Images.Media.MIME_TYPE, "image/png");
        cv.put(MediaStore.Images.Media.RELATIVE_PATH, Environment.DIRECTORY_PICTURES);

        // Get file URI
        Uri uri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, cv);
        try {
            imageOutStream = getContentResolver().openOutputStream(uri);
            bmp.compress(Bitmap.CompressFormat.PNG, 100, imageOutStream);
            imageOutStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        saving = false;
    }
}