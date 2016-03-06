package yu.cleaner.util;

import android.text.TextUtils;
import android.view.View;

/**
 * 文本工具类,用于控制ArrayWheelAdapter(TextUtil.getStringArray(R.array.time_picker_date));
 */
public class TextUtil {
    /**
     * Returns true if the string is null or 0-length.
     *
     * @param str the string to be examined
     * @return true if str is null or zero length
     */
    public static boolean isEmpty(String str) {
        if (str == null) {
            return true;
        }
        str = str.trim();
        return str.length() == 0 || str.equals("null");
    }


}
