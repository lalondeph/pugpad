package jadepug.pugpad;

import android.graphics.Color;
import android.util.ArrayMap;

import java.util.ArrayList;
import java.util.Map;

/**
 * Colors class sets constants used to describe
 * and set the color of the paths stroke.
 *
 * Author: Philip lalonde
 * Organization: Jade Pug
 */
public class Colors {

    // Default palette
    static final int YELLOW = Color.parseColor("#fff200");
    static final int RED = Color.parseColor("#Ed1C24");
    static final int GREEN = Color.parseColor("#22B14C");
    static final int PURPLE = Color.parseColor("#6F3198");
    static final int BLUE = Color.parseColor("#2F3699");
    static final int GRAY = Color.parseColor("#787878");
    static final int BLACK = Color.parseColor("#000000");
    static final int WHITE = Color.parseColor("#FFFFFF");
//    static final int YELLOW = Color.parseColor("#F8DF4B");
//    static final int RED = Color.parseColor("#E32D3B");
//    static final int GREEN = Color.parseColor("#3FE233");
//    static final int PURPLE = Color.parseColor("#9869CC");
//    static final int BLUE = Color.parseColor("#3186EC");
//    static final int GRAY = Color.parseColor("#5F6B5B");
//    static final int BLACK = Color.parseColor("#212420");
//    static final int WHITE = Color.parseColor("#f3f8f1");
    static final int DEFAULT_BACKGROUND_COLOR = Color.parseColor("#3E3B37");
    // Alternate palette
    static final int ALT_YELLOW = Color.parseColor("#ffc20e");
    static final int ALT_RED = Color.parseColor("#990030");
    static final int ALT_GREEN = Color.parseColor("#9dbb61");
    static final int ALT_PURPLE = Color.parseColor("#b5a5d5");
    static final int ALT_BLUE = Color.parseColor("#99d9ea");
    static final int ALT_GRAY = Color.parseColor("#464646");
    static final int ALT_BLACK = Color.parseColor("#ff7e00");
    static final int ALT_WHITE = Color.parseColor("#ffa3b1");
    // Current palette
    static int c_yellow;
    static int c_red;
    static int c_green;
    static int c_purple;
    static int c_blue;
    static int c_gray;
    static int c_black;
    static int c_white;
    static int c_background;

    public static int getDefaultBackgroundColor() {
        return DEFAULT_BACKGROUND_COLOR;
    }

    public static int getC_background() {
        return c_background;
    }


    public static int getC_yellow() {
        return c_yellow;
    }

    public static int getC_red() {
        return c_red;
    }

    public static int getC_green() {
        return c_green;
    }

    public static int getC_purple() {
        return c_purple;
    }

    public static int getC_blue() {
        return c_blue;
    }

    public static int getC_gray() {
        return c_gray;
    }

    public static int getC_black() {
        return c_black;
    }

    public static int getC_white() {
        return c_white;
    }

    // Current palette flag
    static boolean default_palette = false;

    static public int getYELLOW() {
        return YELLOW;
    }

    static public int getRED() {
        return RED;
    }

    static public int getGREEN() {
        return GREEN;
    }

    static public int getPURPLE() {
        return PURPLE;
    }

    static public int getBLUE() {
        return BLUE;
    }

    static public int getGRAY() {
        return GRAY;
    }

    static public int getBLACK() {
        return BLACK;
    }

    static public int getWHITE() {
        return WHITE;
    }

    public static int getAltYellow() {
        return ALT_YELLOW;
    }

    public static int getAltRed() {
        return ALT_RED;
    }

    public static int getAltGreen() {
        return ALT_GREEN;
    }

    public static int getAltPurple() {
        return ALT_PURPLE;
    }

    public static int getAltBlue() {
        return ALT_BLUE;
    }

    public static int getAltGray() {
        return ALT_GRAY;
    }

    public static int getAltBlack() {
        return ALT_BLACK;
    }

    public static int getAltWhite() {
        return ALT_WHITE;
    }

    public static void changePalette() {
        if(default_palette) {
            setDefaultPalette();
            default_palette = false;
        } else {
            setAltPalette();
            default_palette = true;
        }
    }

    public static void setDefaultPalette() {
        c_yellow = YELLOW;
        c_red = RED;
        c_green = GREEN;
        c_purple = PURPLE;
        c_blue = BLUE;
        c_gray = GRAY;
        c_black = BLACK;
        c_white = WHITE;
    }

    public static void setAltPalette() {
        c_yellow = ALT_YELLOW;
        c_red = ALT_RED;
        c_green = ALT_GREEN;
        c_purple = ALT_PURPLE;
        c_blue = ALT_BLUE;
        c_gray = ALT_GRAY;
        c_black = ALT_BLACK;
        c_white = ALT_WHITE;
    }
}
