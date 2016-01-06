
package info.guardianproject.fakepanicresponder;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import info.guardianproject.panic.PanicResponder;

public class PanicConfigActivity extends Activity {

    public static final String PREF_LOCK_AND_EXIT = "pref_lock_and_exit";
    public static final String PREF_CLEAR_APP_DATA = "pref_clear_app_data";
    public static final String PREF_UNINSTALL_THIS_APP = "pref_uninstall_this_app";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_panic_config);

        final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);

        final TextView contentView = (TextView) findViewById(R.id.fullscreen_content);
        final Intent intent = getIntent();
        final String action = intent.getAction();
        if (TextUtils.isEmpty(action)) {
            // TODO this app is initiating the connection to the Panic Button
            contentView.setText("App-local config");
        } else {
            contentView.setText(action);
        }

        final CheckBox showToast = (CheckBox) findViewById(R.id.show_toast);
        showToast.setChecked(prefs.getBoolean(PREF_LOCK_AND_EXIT, true));
        final CheckBox clearAppData = (CheckBox) findViewById(R.id.clear_app_data);
        clearAppData.setChecked(prefs.getBoolean(PREF_CLEAR_APP_DATA, false));
        final CheckBox uninstallThisApp = (CheckBox) findViewById(R.id.uninstall_this_app);
        uninstallThisApp.setChecked(prefs.getBoolean(PREF_UNINSTALL_THIS_APP, false));

        final Button cancelButton = (Button) findViewById(R.id.cancel_button);
        cancelButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                PanicConfigActivity.this.setResult(Activity.RESULT_CANCELED, intent);
                finish();
            }
        });

        final Button connectButton = (Button) findViewById(R.id.connect_button);
        connectButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                prefs.edit()
                        .putBoolean(PREF_LOCK_AND_EXIT, showToast.isChecked())
                        .putBoolean(PREF_CLEAR_APP_DATA, clearAppData.isChecked())
                        .putBoolean(PREF_UNINSTALL_THIS_APP, uninstallThisApp.isChecked())
                        .apply();

                Activity activity = PanicConfigActivity.this;
                PanicResponder.setTriggerPackageName(activity);
                activity.setResult(Activity.RESULT_OK, intent);
                finish();
            }
        });
    }
}
