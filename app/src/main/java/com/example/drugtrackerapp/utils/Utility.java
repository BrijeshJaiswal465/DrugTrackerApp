package com.example.drugtrackerapp.utils;

import static android.content.Context.INPUT_METHOD_SERVICE;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

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

    /**
     * Returns a Typeface object for the SF Bold font.
     * This method loads the 'sf_bold.TTF' font from the assets/fonts directory.
     * Use this font for bold-weight text styling across the app.
     *
     * @param context the context from which assets are accessed
     * @return the Typeface instance of SF Bold
     */
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

    /**
     * Displays an error alert with custom styling using the Alerter library.
     * The alert includes a red icon, white background, and custom text styling with SF Bold font.
     *
     * @param context the context in which the alert should be displayed (must be an Activity)
     * @param text    the error message to display in the alert
     */
    public static void alerter(Context context, String text) {
        Typeface typeface = ResourcesCompat.getFont(context, R.font.sf_bold);
        if (typeface != null) {
            Alerter.create((Activity) context).setTitle(R.string.error).setText(text).setIcon(R.drawable.ic_mark)
                    .setBackgroundColorRes(R.color.white).setIconColorFilter(Color.red(R.color.red))
                    .setRightIconColorFilter(Color.red(R.color.red)).setTextAppearance(R.style.MyRedTextColor)
                    .setTitleAppearance(R.style.MyRedTitleColor).setTitleTypeface(typeface).setTextTypeface(typeface)
                    .setBackgroundDrawable(Objects.requireNonNull(ContextCompat.getDrawable(context, R.drawable.alert_bg_with_bottom_radius))).show();
        }
    }

    /**
     * This method to manage the visibility of a view based on the soft keyboard's visibility.
     * When the keyboard is open, the target view becomes visible.
     * When the keyboard is closed, the target view is hidden.
     *
     * @param rootView The root view of the layout (usually the Activity or Fragment's main layout).
     * @param view     The view whose visibility needs to be controlled.
     */

    public static void manageVisibilityWhenKeyboardOpen(View rootView, View view) {
        rootView.getViewTreeObserver().addOnGlobalLayoutListener(() -> {
            Rect r = new Rect();
            rootView.getWindowVisibleDisplayFrame(r);
            int screenHeight = rootView.getRootView().getHeight();
            int keypadHeight = screenHeight - r.bottom;

            if (keypadHeight > screenHeight * 0.15) {
                // Keyboard is opened
                view.setVisibility(View.VISIBLE);
            } else {
                // Keyboard is closed
                view.setVisibility(View.GONE);
            }
        });
    }

    /**
     * Sets the status bar color and ensures the status bar
     *
     * @param activity The activity where the status bar color needs to be applied.
     * @param color    The color to apply to the status bar.
     */
    public static void statusBarColor(Activity activity, int color) {
        Window window = activity.getWindow();
        window.setStatusBarColor(color);  // Set status bar color to white

        // Set status bar icons to dark (for visibility on white background)
        View decor = window.getDecorView();
        decor.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
    }

    /**
     * Displays a short toast message.
     * This is a convenience method for showing quick feedback to the user.
     *
     * @param context the context in which the toast should be displayed
     * @param text    the message to display in the toast
     */
    public static void toast(Context context, String text) {
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
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


    /**
     * Returns HTML-formatted dummy medication details.
     *
     * @return HTML-formatted string containing detailed information
     */
    public static String getDummyDetails() {
        return "<b>Tablet</b><br>" +
                "• Adults: 1–2 tablets every 4 to 6 hours<br>" +
                "Max: 4 gm/day (8 tablets)<br><br>" +
                "• Children (6–12 years): ½ to 1 tablet, 3 to 4 times daily<br>" +
                "Max: 2.6 gm/day (for long-term use)<br><br>" +

                "<b>Extended Release Tablet</b><br>" +
                "• Adults & children >12 years:<br>" +
                "2 tablets every 6 to 8 hours (swallowed whole)<br>" +
                "Do not crush<br>" +
                "Max: 6 tablets/day (in 24 hrs)<br><br>" +

                "<b>Syrup / Suspension</b><br>" +
                "• Children <3 months:<br>" +
                "10 mg/kg, 3–4 times daily<br>" +
                "If jaundiced → reduce to 5 mg/kg<br><br>" +

                "• 3 months to <1 year: ½ to 1 teaspoonful, 3–4 times daily<br>" +
                "• 1–5 years: 1–2 teaspoonful, 3–4 times daily<br>" +
                "• 6–12 years: 2–3 teaspoonful, 3–4 times daily<br>" +
                "• Adults: 4–8 teaspoonful, 3–4 times daily<br><br>" +

                "<b>Suppository</b><br>" +
                "• 3–12 months: 60–120 mg, 4 times daily<br>" +
                "• 1–5 years: 125–250 mg, 4 times daily<br>" +
                "• 6–12 years: 250–500 mg, 4 times daily<br>" +
                "• Adults & children >12 years: 0.5–1 gm, 4 times daily<br><br>" +

                "<b>Paediatric Drop</b><br>" +
                "• Up to 3 months: 0.5 ml (40 mg)<br>" +
                "• 4–11 months: 1.0 ml (80 mg)<br>" +
                "• 7 months–2 years: 1.5 ml (120 mg)<br>" +
                "Max: 5 doses/day for max 5 days<br><br>" +

                "<b>Paracetamol Tablet with Actizorb Technology</b><br>" +
                "• Dissolves 5x faster than standard tablets<br><br>" +
                "• Adults & children ≥12 years: 1–2 tablets every 4–6 hours<br>" +
                "Max: 8 caplets in 24 hours<br><br>" +
                "• Children (7–11 years): ½ to 1 tablet every 4–6 hours<br>" +
                "Max: 4 caplets/day<br><br>" +
                "Not recommended for children under 7 years<br>" +
                "Suitable for patients intolerant to aspirin or other analgesics";
    }

}
