
package info.guardianproject.fakepanicresponder;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

import info.guardianproject.panic.PanicResponder;

public class ResponseActivity extends Activity {
    public static final String TAG = "ResponseActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String msg = "FakePanicResponder got a panic trigger!";
        Log.i(TAG, msg);
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        if (PanicResponder.receivedTriggerFromConnectedApp(this)) {
            if (prefs.getBoolean(PanicConfigActivity.PREF_UNINSTALL_THIS_APP, false)) {
                Log.i(TAG, PanicConfigActivity.PREF_UNINSTALL_THIS_APP);
                PanicResponder.deleteAllAppData(this);
                Intent uninstall = new Intent(Intent.ACTION_DELETE);
                uninstall.setData(Uri.parse("package:" + getPackageName()));
                startActivity(uninstall);
                ExitActivity.exitAndRemoveFromRecentApps(this);
            } else if (prefs.getBoolean(PanicConfigActivity.PREF_LOCK_AND_EXIT, true)) {
                Log.i(TAG, PanicConfigActivity.PREF_LOCK_AND_EXIT);
                ExitActivity.exitAndRemoveFromRecentApps(this);
            }
            // add other responses here, paying attention to if/else order
        } else if (PanicResponder.shouldUseDefaultResponseToTrigger(this)) {
            if (prefs.getBoolean(PanicConfigActivity.PREF_LOCK_AND_EXIT, true)) {
                Log.i(TAG, PanicConfigActivity.PREF_LOCK_AND_EXIT);
                ExitActivity.exitAndRemoveFromRecentApps(this);
            }
        }

        finish();
    }
}
