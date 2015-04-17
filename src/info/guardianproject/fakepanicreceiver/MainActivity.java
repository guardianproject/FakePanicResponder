
package info.guardianproject.fakepanicreceiver;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import info.guardianproject.panic.PanicReceiver;

public class MainActivity extends Activity {

    private static final String NONE = "None";

    private Button choosePanicTriggerButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        choosePanicTriggerButton = (Button) findViewById(R.id.choosePanicTriggerButton);
        choosePanicTriggerButton.setCompoundDrawablePadding(10);

        Button configButton = (Button) findViewById(R.id.configButton);
        configButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, PanicConfigActivity.class);
                startActivity(intent);
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        String packageName = PanicReceiver.getTriggerPackageName(this);
        Drawable icon = null;
        if (TextUtils.isEmpty(packageName)) {
            icon = getResources().getDrawable(android.R.drawable.ic_menu_close_clear_cancel);
            choosePanicTriggerButton.setText(NONE);
        } else {
            try {
                PackageManager pm = getPackageManager();
                icon = pm.getApplicationIcon(packageName);
                ApplicationInfo ai = pm.getApplicationInfo(packageName, 0);
                choosePanicTriggerButton.setText(ai.loadLabel(pm));
            } catch (NameNotFoundException e) {
                // no longer exists, so remove pref
                PanicReceiver.setTriggerPackageName(this, null);
                e.printStackTrace();
            }
        }
        choosePanicTriggerButton.setCompoundDrawablesWithIntrinsicBounds(icon,
                null, null, null);
    }
}
