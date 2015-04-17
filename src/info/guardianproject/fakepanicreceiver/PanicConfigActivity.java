
package info.guardianproject.fakepanicreceiver;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import info.guardianproject.panic.PanicReceiver;

public class PanicConfigActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_panic_config);

        final TextView contentView = (TextView) findViewById(R.id.fullscreen_content);
        final Intent intent = getIntent();
        final String action = intent.getAction();
        if (TextUtils.isEmpty(action)) {
            // TODO this app is initiating the connection to the Panic Button
            contentView.setText("I'm the decider!");
        } else {
            contentView.setText(action);
        }

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
                Activity activity = PanicConfigActivity.this;
                PanicReceiver.setTriggerPackageName(activity);
                activity.setResult(Activity.RESULT_OK, intent);
                finish();
            }
        });

    }
}
