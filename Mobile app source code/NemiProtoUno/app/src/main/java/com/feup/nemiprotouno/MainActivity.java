package com.feup.nemiprotouno;
// ----------------------------------------------------------------------------
//
//  NEMMI is developed by Alexandre Clément at the University of Porto's Faculty of Engineering
//  Released under GNU Affero General Public License version 3
//  https://opensource.org/licenses/AGPL-3.0
//
// ----------------------------------------------------------------------------
//
//  Main activity responsible for handling user interaction and network behaviour
//
// ----------------------------------------------------------------------------

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.PermissionChecker;

import java.io.File;

public class MainActivity extends InteractionActivity {

    private View overlayView, bgView;
    private LogManager logManager;

    //region PERMISSION MANAGEMENT
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 1) {// If request is cancelled, the result arrays are empty.
            if (grantResults.length == 0 || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
                alertDialog.setTitle("Alert");
                alertDialog.setMessage("You need to allow permission to file storage for test data logging.");
                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                ActivityCompat.requestPermissions(MainActivity.this,
                                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                                dialog.dismiss();
                            }
                        });
                alertDialog.show();
            }
        }
    }

    public static boolean selfPermissionGranted(Context context, String permission) {
        // For Android < Android M, self permissions are always granted.
        boolean result = true;
        int targetSdkVersion;
        try {
            final PackageInfo info = context.getPackageManager().getPackageInfo(
                    context.getPackageName(), 0);
            targetSdkVersion = info.applicationInfo.targetSdkVersion;
        } catch (PackageManager.NameNotFoundException e) {
            targetSdkVersion = 21;
            e.printStackTrace();
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            if (targetSdkVersion >= Build.VERSION_CODES.M) {
                // targetSdkVersion >= Android M, we can
                // use Context#checkSelfPermission
                result = context.checkSelfPermission(permission)
                        == PackageManager.PERMISSION_GRANTED;
            } else {
                // targetSdkVersion < Android M, we have to use PermissionChecker
                result = PermissionChecker.checkSelfPermission(context, permission)
                        == PermissionChecker.PERMISSION_GRANTED;
            }
        }

        return result;
    }

    //endregion

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // check permissions are granted for log writing
        if (!selfPermissionGranted(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        }

        // hide status bar and set activity to fullscreen
        getSupportActionBar().hide();
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        // set and hide the overlay view - used for user call-to-action
        overlayView = findViewById(R.id.overlayView);
        bgView = findViewById(R.id.bgView);
        overlayView.setVisibility(View.INVISIBLE);
        bgView.setVisibility(View.VISIBLE);

        /* Methods SETUP */

        // initialize UDP connection manager
        UdpClient udpMessager = new UdpClient(this, 64000);
        udpMessager.execute();

        // initialize LogManager
        File docsDir = this.getExternalFilesDir(null);
        logManager = LogManager.getInstance();
        if (logManager == null)
            logManager = LogManager.initInstance(this, "NemmiProtoUnoLog", docsDir, false);
    }

    //region application lifetime/flow methods
    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
    }
    //endregion

    // what to do when a network message is received
    // these codes are setup in the UdpClient class
    public void doMessageReceived(String msg) {
        // OK = test start
        // OL = test end
        // OM = new test
        // ON = soundoff

        switch (msg) {
            case "OK":
                overlayView.setVisibility(View.VISIBLE);
                bgView.setVisibility(View.INVISIBLE);
                logManager.startLogging();
                break;
            case "OL":
                logManager.stopLogging(true);
                bgView.setVisibility(View.VISIBLE);
                overlayView.setVisibility(View.INVISIBLE);
                break;
        }
    }

    //region Interactions methods

    @Override
    protected void doDblTap(int index, int x, int y) {
        // not used
    }

    @Override
    protected void doTouchUp(int index, int x, int y) {
        logManager.logData("touchEnd", index + " " + x + " " + y);
    }

    @Override
    protected void doTouchDown(int index, int x, int y) {
        logManager.logData("touchStart", index + " " + x + " " + y);
    }

    @Override
    protected void doMove(int index, int x, int y) {
        logManager.logData("move", index + " " + x + " " + y);
    }

    @Override
    protected void doAccel(Float X, Float Y, Float Z) {
        String value = X + " " + Y + " " + Z;
        logManager.logData("accel", value);
    }

    @Override
    protected void doShake() {
        logManager.logData("shake", "");
    }

    //endregion
}