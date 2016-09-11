/*
 * Copyright (C) 2014-2016 Appgramming. All rights reserved.
 * http://www.appgramming.com
 */
package com.appgramming.lonecolor;

import android.content.ClipData;
import android.content.ClipDescription;
import android.content.ClipboardManager;
import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;

/**
 * A static method to get a valid color value from the clipboard.
 */
final class ColorClipParameter {

    /**
     * Gets the current clip parameter from the clipboard.
     *
     * @return The text value of the clip parameter, or null if there is no text on the clipboard.
     */
    private static String getClipParameter(Context context) {
        final ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        if ((clipboard != null) && clipboard.hasPrimaryClip()) {

            // Get the current primary clip on the clipboard
            final ClipData clip = clipboard.getPrimaryClip();
            if ((clip != null) && (clip.getItemCount() > 0)) {

                final ClipDescription description = clip.getDescription();

                // Return null if the clipboard does not contain plain text or html text
                if (!description.hasMimeType(ClipDescription.MIMETYPE_TEXT_PLAIN) &&
                        !description.hasMimeType(ClipDescription.MIMETYPE_TEXT_HTML)) {
                    return null;
                }

                // Ignore and return null if it's a clip previously copied by LoneColor
                final CharSequence label = description.getLabel();
                if ((label != null) && (label.equals(context.getString(R.string.app_name)))) {
                    return null;
                }

                // Get the text from the clipboard
                final CharSequence sequence = clip.getItemAt(0).getText();
                if (sequence != null) {
                    return sequence.toString();
                }
            }
        }

        return null;
    }

    /**
     * Returns the value of the color clip parameter.
     *
     * @return An Integer color value, or null.
     */
    public static Integer getColor(Context context) {

        // Get the clip parameter from the clipboard
        final String clipText = ColorClipParameter.getClipParameter(context);

        // Return null if the clip is null or empty
        if (TextUtils.isEmpty(clipText)) {
            return null;
        }

        // Try to parse the clip parameter to a color value, "as it is"
        try {
            return Color.parseColor(clipText);
        } catch (IllegalArgumentException ignored) {
            // Ignore error, try next color format
        }

        // Try to parse the clip parameter with a "#" in front
        try {
            return Color.parseColor('#' + clipText);
        } catch (IllegalArgumentException ignored) {
            // Ignore error, we will return false
        }

        // No valid color, return null
        return null;
    }
}