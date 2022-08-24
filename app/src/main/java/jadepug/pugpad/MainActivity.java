package jadepug.pugpad;

import android.annotation.SuppressLint;
import android.content.ContentValues;
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
    private boolean $selectedVisible = true;

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
        dv.setBackgroundColor(Colors.getDefaultBackgroundColor());
        Colors.setDefaultPalette();

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
        Button btnChangePalette = findViewById(R.id.btnChangePalette);

        btnYellow.setOnClickListener(colorListener);
        btnRed.setOnClickListener(colorListener);
        btnGreen.setOnClickListener(colorListener);
        btnPurple.setOnClickListener(colorListener);
        btnBlue.setOnClickListener(colorListener);
        btnGray.setOnClickListener(colorListener);
        btnBlack.setOnClickListener(colorListener);
        btnWhite.setOnClickListener(colorListener);

        setColorSwatches();

        btnSmall.setOnClickListener(brushListener);
        btnMed.setOnClickListener(brushListener);
        btnLarge.setOnClickListener(brushListener);

        selectedColorBtn = btnWhite;
        selectedColorBtn.setText(smileText);
        selectedSizeBtn = btnSmall;
        selectedSizeBtn.setBackgroundColor(Colors.getWHITE());

        /*
         * This listener Saves the canvas to a file
         */
        btnSave.setOnClickListener(view -> {
            if (!saving && dv.paths.size() > 0) {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage(R.string.confirm_save)
                        .setPositiveButton(R.string.save, (dialogInterface, i) -> saveCanvas())
                        .setNegativeButton(R.string.cancel, (dialogInterface, i) -> { });

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
                        .setPositiveButton(R.string.clear, (dialogInterface, i) -> {
                            dv.paths.clear();
                            dv.undone_paths.clear();
                            dv.invalidate();
                        })
                        .setNegativeButton(R.string.cancel, (dialogInterface, i) -> { });

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
         * Changes the color palette of the app
         */
        btnChangePalette.setOnClickListener(view -> swapPalette());

        /*
         * Called on user touch event. This listener
         * turns touch input into points on a path.
         */
        dv.setOnTouchListener((view, motionEvent) -> {
            float x = motionEvent.getX();
            float y = motionEvent.getY();
            int action = motionEvent.getActionMasked();

            if(action == MotionEvent.ACTION_DOWN) {
                dv.beginPath();
            }

            dv.addPointToPath(x, y);
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
                newColor = Colors.getC_yellow();
                selectedColorBtn = btnYellow;
            } else if (selectedColor == btnRed.getId()) {
                newColor = Colors.getC_red();
                selectedColorBtn = btnRed;
            } else if (selectedColor == btnGreen.getId()) {
                newColor = Colors.getC_green();
                selectedColorBtn = btnGreen;
            } else if (selectedColor == btnPurple.getId()) {
                newColor = Colors.getC_purple();
                selectedColorBtn = btnPurple;
            } else if (selectedColor == btnBlue.getId()) {
                newColor = Colors.getC_blue();
                selectedColorBtn = btnBlue;
            } else if (selectedColor == btnGray.getId()) {
                newColor = Colors.getC_gray();
                selectedColorBtn = btnGray;
            } else if (selectedColor == btnBlack.getId()) {
                newColor = Colors.getC_black();
                selectedColorBtn = btnBlack;
            } else {
                newColor = Colors.getC_white();
                selectedColorBtn = btnWhite;
            }

            dv.setCurrentStrokeColor(newColor);
            selectedColorBtn.setText(smileText);
            $selectedVisible = true;
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
            selectedSizeBtn.setBackgroundColor(Colors.getWHITE());
        }
    };

    private void setColorSwatches() {
        btnYellow.setBackgroundColor(Colors.getC_yellow());
        btnRed.setBackgroundColor(Colors.getC_red());
        btnGreen.setBackgroundColor(Colors.getC_green());
        btnPurple.setBackgroundColor(Colors.getC_purple());
        btnBlue.setBackgroundColor(Colors.getC_blue());
        btnGray.setBackgroundColor(Colors.getC_gray());
        btnBlack.setBackgroundColor(Colors.getC_black());
        btnWhite.setBackgroundColor(Colors.getC_white());
    }

    /**
     * changes the color palette
     */
    private void swapPalette() {
        Colors.changePalette();
        setColorSwatches();
        if($selectedVisible) {
            $selectedVisible = false;
            selectedColorBtn.setText("");
        } else {
            $selectedVisible = true;
            selectedColorBtn.setText(smileText);
        }
    }

    /**
     * Called when user confirms a save action
     * exports view to png on users device
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
        cv.put(MediaStore.Images.Media.DISPLAY_NAME, getString(R.string.output_file_name));
        cv.put(MediaStore.Images.Media.MIME_TYPE, getString(R.string.image_type));
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