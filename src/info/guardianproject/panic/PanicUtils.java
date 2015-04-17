
package info.guardianproject.panic;

import android.app.Activity;
import android.text.TextUtils;
import android.util.Log;

public class PanicUtils {

    static String getCallingPackageName(Activity activity) {
        // getCallingPackage() was unstable until android-18, use this
        String packageName = activity.getCallingActivity().getPackageName();
        if (TextUtils.isEmpty(packageName)) {
            packageName = activity.getIntent().getPackage();
        }
        if (TextUtils.isEmpty(packageName)) {
            Log.e(activity.getPackageName(),
                    "Received blank Panic.ACTION_DISCONNECT Intent, it must be sent using startActivityForResult()!");
        }
        return packageName;
    }
}
