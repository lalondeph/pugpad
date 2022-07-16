package jadepug.pugpad;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;
import androidx.annotation.Nullable;
import java.util.ArrayList;

/**
 * DrawingView provides the methods used to express user input
 * onto the main activity canvas.
 */
public class DrawingView extends View {

    Paint paint = new Paint();
    ArrayList<DrawingPath> paths = new ArrayList<>();
    DrawingPath path;

    public DrawingView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        paint.setColor(StrokeColor.getWHITE());
        paint.setStrokeWidth(StrokeSize.getSMALL());
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeJoin(Paint.Join.ROUND);
        paint.setStrokeCap(Paint.Cap.ROUND);
    }

    /**
     * addPontToPath receives an x and y coordinate and
     * adds them as a point to an a DrawingPath.
     * It's other main function is to cause the screen to be redrawn.
     * @param x
     * @param y
     */
    public void addPointToPath(float x, float y) {
        if(path.isEmpty())
            path.moveTo(x,y);
        else
            path.lineTo(x,y);
        invalidate();
    }

    /**
     * beginPath receives an x and y coordinate and adds a
     * a new DrawingPath with those coordinates to an ArrayList of DrawingPaths.
     * @param x
     * @param y
     */
    public void beginPath(float x, float y) {
        path = new DrawingPath(paint.getColor(), paint.getStrokeWidth());
        path.moveTo(x,y);
        paths.add(path);
    }

    /**
     * onDraw iterates through an ArrayList of and draws
     * each path onto the canvas.
     * It is called automatically when the canvas is redrawn.
     * @param canvas
     */
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        for(DrawingPath path : paths) {
            paint.setColor(path.color);
            paint.setStrokeWidth(path.lineWidth);
            canvas.drawPath(path,paint);
        }
    }

    public void setDrawingColor(int color) { paint.setColor(color); }
    public void setDrawingWidth(float width) { paint.setStrokeWidth(width); }

}
