
package info.guardianproject.fakepanicreceiver;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.ResolveInfo;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.TextView;

import info.guardianproject.panic.PanicReceiver;

import java.util.ArrayList;

public class MainActivity extends Activity {

    private static final String NONE_NAME = "NONE";
    private TrustedAppEntry NONE;

    private PackageManager pm;
    private Button choosePanicTriggerButton;
    private int iconSize;
    private int dp20;
    private int dp10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (PanicReceiver.checkForDisconnectIntent(this)) {
            finish();
            return;
        }

        Resources r = getResources();
        iconSize = r.getDrawable(R.drawable.ic_launcher).getIntrinsicHeight();
        float density = r.getDisplayMetrics().density;
        dp10 = (int) (10 * density);
        dp20 = (int) (20 * density);

        pm = getPackageManager();

        NONE = new TrustedAppEntry(this, NONE_NAME, R.string.none,
                android.R.drawable.ic_menu_close_clear_cancel, iconSize);

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
        showSelectedApp(packageName);

        choosePanicTriggerButton.setOnClickListener(new OnClickListener() {

            private ArrayList<TrustedAppEntry> list;

            private int getIndexOfProviderList() {
                for (TrustedAppEntry app : list) {
                    if (app.packageName.equals(PanicReceiver
                            .getTriggerPackageName(getApplicationContext()))) {
                        return list.indexOf(app);
                    }
                }
                return 0; // default is NONE
            }

            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle(R.string.choose_panic_trigger);

                list = new ArrayList<TrustedAppEntry>();
                list.add(0, NONE);

                for (ResolveInfo resolveInfo : PanicReceiver.resolveTriggerApps(pm)) {
                    if (resolveInfo.activityInfo == null)
                        continue;
                    list.add(new TrustedAppEntry(pm, resolveInfo.activityInfo, iconSize));
                }
                ListAdapter adapter = new ArrayAdapter<TrustedAppEntry>(MainActivity.this,
                        android.R.layout.select_dialog_singlechoice, android.R.id.text1, list) {
                    @Override
                    public View getView(int position, View convertView, ViewGroup parent) {
                        View view = super.getView(position, convertView, parent);
                        TextView textView = (TextView) view.findViewById(android.R.id.text1);
                        textView.setCompoundDrawables(list.get(position).icon, null, null, null);
                        // margin between image and text
                        textView.setCompoundDrawablePadding(dp10);

                        return view;
                    }
                };

                builder.setSingleChoiceItems(adapter, getIndexOfProviderList(),
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                TrustedAppEntry entry = list.get(which);
                                PanicReceiver.setTriggerPackageName(getBaseContext(),
                                        entry.packageName);
                                showSelectedApp(entry);
                                dialog.dismiss();
                            }
                        });

                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
    }

    private void showSelectedApp(String packageName) {
        if (TextUtils.equals(packageName, NONE.packageName)) {
            showSelectedApp(NONE);
        } else {
            try {
                PackageInfo pi = pm.getPackageInfo(packageName, PackageManager.GET_ACTIVITIES);
                showSelectedApp(new TrustedAppEntry(pm, pi.activities[0], iconSize));
            } catch (NameNotFoundException e) {
                showSelectedApp(NONE);
            }
        }
    }

    @SuppressLint("NewApi")
    private void showSelectedApp(final TrustedAppEntry entry) {
        choosePanicTriggerButton.setText(entry.simpleName);
        if (Build.VERSION.SDK_INT >= 17)
            choosePanicTriggerButton.setCompoundDrawablesRelative(entry.icon, null, null, null);
        else
            choosePanicTriggerButton.setCompoundDrawables(entry.icon, null, null, null);
        choosePanicTriggerButton.setPadding(dp20, dp20, dp20, dp20);
        choosePanicTriggerButton.setCompoundDrawablePadding(dp10);
    }
}
