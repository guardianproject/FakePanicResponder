
package info.guardianproject.fakepanicresponder;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;

public class TrustedAppEntry {
    public final String packageName;
    public final String simpleName;
    public final Drawable icon;

    /**
     * Create from probing for {@link Intent}s
     *
     * @param pm
     * @param activityInfo
     * @param iconSize the desired size of the app icons
     */
    public TrustedAppEntry(PackageManager pm, ActivityInfo activityInfo, int iconSize) {
        this.packageName = activityInfo.packageName;
        this.simpleName = String.valueOf(activityInfo.loadLabel(pm));
        Drawable icon = activityInfo.loadIcon(pm);
        icon.setBounds(0, 0, iconSize, iconSize);
        this.icon = icon;
    }

    /**
     * Create manual entry for non-apps, i.e. "Chooser", "None", or "Blocked"
     *
     * @param context
     * @param fakePackageName a unique {@code String} ID for non-app entries
     * @param simpleNameId
     * @param iconId the resId of the desired icon
     * @param iconSize the desired size of the icon
     */
    public TrustedAppEntry(Context context, String fakePackageName, int simpleNameId,
            int iconId, int iconSize) {
        this.packageName = fakePackageName;
        this.simpleName = context.getString(simpleNameId);
        Drawable icon = context.getResources().getDrawable(iconId);
        icon.setBounds(0, 0, iconSize, iconSize);
        this.icon = icon;
    }

    @Override
    public String toString() {
        return simpleName;
    }
}
