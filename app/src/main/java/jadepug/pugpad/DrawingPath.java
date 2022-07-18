package jadepug.pugpad;

import android.graphics.Path;

/**
 * DrawingPath class describes the qualities of a Path.
 * <p>
 * Author: Philip lalonde
 * Organization: Jade Pug
 */
public class DrawingPath extends Path {
    int color;
    float lineWidth;

    /**
     * Class constructor
     *
     * @param color     - path color
     * @param lineWidth - path width
     */
    public DrawingPath(int color, float lineWidth) {
        this.color = color;
        this.lineWidth = lineWidth;
    }
}
