package com.example.drugtrackerapp.utils;

import static android.content.Context.INPUT_METHOD_SERVICE;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.Log;
import android.view.inputmethod.InputMethodManager;

import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;

import com.example.drugtrackerapp.R;
import com.tapadoo.alerter.Alerter;

import java.util.Objects;

public class Utility {

    /**
     * Returns a Typeface object for the SF Regular font.
     * This method loads the 'sf_regular.TTF' font from the assets/fonts directory.
     * Use this font for regular-weight text styling across the app.
     *
     * @param context the context from which assets are accessed
     * @return the Typeface instance of SF Regular
     */
    public static Typeface setFontSfRegularText(Context context) {
        return Typeface.createFromAsset(context.getAssets(), "fonts/sf_regular.TTF");
    }

    public static Typeface setFontSfBoldText(Context context) {
        return Typeface.createFromAsset(context.getAssets(), "fonts/sf_bold.TTF");
    }

    /**
     * Returns a Typeface object for the NHU Medium font.
     * This method loads the 'nhu_medium.ttf' font from the assets/fonts directory.
     * Use this font for medium-weight text to emphasize certain UI elements.
     *
     * @param context the context from which assets are accessed
     * @return the Typeface instance of NHU Medium
     */
    public static Typeface setFontNhuMediumText(Context context) {
        return Typeface.createFromAsset(context.getAssets(), "fonts/nhu_medium.TTF");
    }

    public static void alerter(Context context, String text) {
        Typeface typeface = ResourcesCompat.getFont(context, R.font.sf_bold);
        if (typeface != null) {
            Alerter.create((Activity) context).setTitle(R.string.error).setText(text).setIcon(R.drawable.notification_icon_red)
                    .setBackgroundColorRes(R.color.white).setIconColorFilter(Color.red(R.color.red))
                    .setRightIconColorFilter(Color.red(R.color.red)).setTextAppearance(R.style.MyRedTextColor)
                    .setTitleAppearance(R.style.MyRedTitleColor).setTitleTypeface(typeface).setTextTypeface(typeface)
                    .setBackgroundDrawable(Objects.requireNonNull(ContextCompat.getDrawable(context, R.drawable.alert_bg_with_bottom_radius))).show();
        }
    }

    /**
     * This method using for hide keyboard in click outSide screen
     *
     * @param activity required
     * @return boolean value true
     */
    public static boolean onTouchEventHideKeyBoard(Activity activity) {
        try {
            InputMethodManager imm = (InputMethodManager) activity.getSystemService(INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(Objects.requireNonNull(activity.getCurrentFocus()).getWindowToken(), 0);

        } catch (Exception e) {
            Log.e("An error occurred:", Objects.requireNonNull(e.getMessage()));
        }
        return true;
    }
    /**
     * Set dimen programmatically
     *
     * @param context Context
     * @return int
     */
    public static int getDimensionPixelSize(Context context, int size) {
        return context.getResources().getDimensionPixelSize(size);
    }
}
