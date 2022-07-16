package comp208.lalonde;

import android.graphics.Path;

/**
 * DrawingPath class describes the qualities of a Path.
 */
public class DrawingPath extends Path {
    int color;
    float lineWidth;

    public DrawingPath(int color, float lineWidth) {
        this.color = color;
        this.lineWidth = lineWidth;
    }

}
