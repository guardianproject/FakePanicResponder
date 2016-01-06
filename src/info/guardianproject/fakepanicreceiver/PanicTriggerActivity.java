
package info.guardianproject.fakepanicreceiver;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

public class PanicTriggerActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        if (prefs.getBoolean(PanicConfigActivity.PREF_SHOW_TOAST, true)) {
            String msg = "FakePanicResponder got a panic trigger!";
            Log.i("PanicTriggerActivity", msg);
            Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
        }
        if (prefs.getBoolean(PanicConfigActivity.PREF_UNINSTALL_THIS_APP, false)) {
            Intent intent = new Intent(Intent.ACTION_DELETE);
            intent.setData(Uri.parse("package:" + getPackageName()));
            startActivity(intent);
        }
        finish();
    }
}
