
package info.guardianproject.fakepanicreceiver;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

public class PanicTriggerActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String msg = "FakePanicReceiver got a panic trigger!";
        Log.i("PanicTriggerActivity", msg);
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
        finish();
    }
}
