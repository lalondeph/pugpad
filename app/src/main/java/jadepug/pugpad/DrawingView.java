package jadepug.pugpad;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;
import androidx.annotation.Nullable;
import java.util.ArrayList;

/**
 * DrawingView provides the methods used to express user input
 * onto the main activity canvas.
 *
 * Author: Philip lalonde
 * Organization: Jade Pug
 */
public class DrawingView extends View {

    private final Paint paint = new Paint();
    public ArrayList<DrawingPath> paths = new ArrayList<>();
    public ArrayList<DrawingPath> undone_paths = new ArrayList<>();
    private DrawingPath path;
    public Bitmap bitmap;
    private int currentStrokeColor;
    private float currentStrokeSize;

    /**
     * Class constructor
     *
     * @param context - DrawingView
     * @param attrs   - dv attributes
     */
    public DrawingView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setCurrentStrokeColor(Colors.getWHITE());
        setCurrentStrokeSize(StrokeSize.getSMALL());

        paint.setColor(currentStrokeColor);
        paint.setStrokeWidth(currentStrokeSize);

        paint.setAntiAlias(true);
        paint.setDither(true);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeJoin(Paint.Join.ROUND);
        paint.setStrokeCap(Paint.Cap.ROUND);
    }

    /**
     * addPontToPath receives an x and y coordinate and
     * adds them as a point to an a DrawingPath.
     * It's other main function is to cause the screen to be redrawn.
     *
     * @param x
     * @param y
     */
    public void addPointToPath(float x, float y) {
        if (path.isEmpty())
            path.moveTo(x, y);
        else
            path.lineTo(x, y);
        invalidate();
    }

    /**
     * beginPath receives an x and y coordinate and adds a
     * a new DrawingPath with those coordinates to an ArrayList of DrawingPaths.
     *
     * @param x - x coordinate of input
     * @param y - y coordinate of input
     */
    public void beginPath(float x, float y) {
        path = new DrawingPath(currentStrokeColor, currentStrokeSize);
        paths.add(path);
    }

    /**
     * onDraw iterates through an ArrayList of and draws
     * each path onto the canvas.
     * It is called automatically when the canvas is redrawn.
     *
     * @param canvas - the drawing canvas
     */
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        for (DrawingPath path : paths) {
            paint.setColor(path.color);
            paint.setStrokeWidth(path.lineWidth);
            canvas.drawPath(path, paint);
        }
    }

    /**
     * Convert the view into a bitmap
     *
     * @return Bitmap
     */
    public Bitmap viewToBitmap() {
        bitmap = Bitmap.createBitmap(this.getWidth(), this.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        this.draw(canvas);
        return bitmap;
    }

    /**
     * Remove last path from paths ArrayList
     * Add removed path to undone_paths Arraylist
     */
    public void undoPath() {
        undone_paths.add(paths.remove(paths.size() - 1));
    }

    /**
     * Remove last path from undone_paths ArrayList
     * Add removed path to paths Arraylist
     */
    public void redoPath() {
        paths.add(undone_paths.remove(undone_paths.size() - 1));
    }

    public void setCurrentStrokeColor(int currentStrokeColor) {
        this.currentStrokeColor = currentStrokeColor;
    }

    public void setCurrentStrokeSize(float currentStrokeSize) {
        this.currentStrokeSize = currentStrokeSize;
    }
}
