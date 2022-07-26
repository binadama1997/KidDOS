package com.rex1997.kiddos;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;

import com.rex1997.kiddos.facedetection.FaceDetectionActivity;
import com.rex1997.kiddos.utils.AppAdapter;
import com.rex1997.kiddos.utils.AppList;
import com.rex1997.kiddos.utils.BaseActivity;
import com.rex1997.kiddos.utils.MySharedPreferences;
import com.rex1997.kiddos.utils.SettingFragment;

import java.util.ArrayList;
import java.util.List;

public class ModeActivity extends BaseActivity {
    private static final String TAG = "@string/app_name";
    final private FragmentManager fragmentManager = getSupportFragmentManager();

    private List<AppList> installedApps;
    ListView userInstalledApps;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        Bundle faceDetection = getIntent().getExtras();
        boolean isReady = faceDetection.getBoolean("Age", true);
        if (!isReady){
            startActivity(new Intent(this, FaceDetectionActivity.class)
                    .setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT));
        }
        setContentView(R.layout.activity_mode);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        TextView statusTitle = findViewById(R.id.status_title);
        statusTitle.setText(R.string.allowed_app);
        setUpKioskMode();
        userInstalledApps = findViewById(R.id.app_list);
        getAppsList();
    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    public boolean onCreateOptionsMenu(@NonNull Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            showSettingsDialog();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (!kioskMode.isLocked(this)) {
            super.onBackPressed();
        }
        super.finish();
    }

    private void setUpKioskMode() {
        if (!MySharedPreferences.isAppLaunched(this)) {
            Log.d(TAG, "onCreate() locking the app first time");
            kioskMode.lockUnlock(this, true);
            MySharedPreferences.saveAppLaunched(this, true);
        } else {
            //check if app was locked
            if (MySharedPreferences.isAppInKioskMode(this)) {
                Log.d(TAG, "onCreate() locking the app");
                kioskMode.lockUnlock(this, true);
            }
        }
    }

    /**
     * show settings dialog
     */
    private void showSettingsDialog() {
        SettingFragment settingFragment = new SettingFragment();
        Bundle args = new Bundle();
        args.putBoolean(SettingFragment.LOCKED_BUNDLE_KEY, kioskMode.isLocked(this));
        settingFragment.setArguments(args);
        settingFragment.show(fragmentManager, settingFragment.getClass().getSimpleName());
        settingFragment.setActionHandler(isLocked -> {
            int msg = isLocked ? R.string.setting_device_locked : R.string.setting_device_unlocked;
            kioskMode.lockUnlock(this, isLocked);
            Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
        });
    }

    /**
     * Show installed apps
     */
    private void getAppsList(){
        installedApps = getInstalledApps();
        AppAdapter installedAppAdapter = new AppAdapter(this, installedApps);
        userInstalledApps.setAdapter(installedAppAdapter);
        userInstalledApps.setOnItemClickListener((adapterView, view, i, l) -> {
            kioskMode.lockUnlock(this, true);
            finish(); // Close app before opening intent
            Intent intent = new Intent(getPackageManager().getLaunchIntentForPackage(installedApps.get(i).packages));
            startActivity(intent);
        });
    }

    private List<AppList> getInstalledApps() {
        // Initiate main/app list
        List<AppList> apps = new ArrayList<>();
        // Get what apps installed in te phone
        PackageManager pm = getPackageManager();
        @SuppressLint("QueryPermissionsNeeded")
        List<PackageInfo> packs = pm.getInstalledPackages(0);
        // Get data from xml resources
        Resources res = getResources();
        List<String> allowApps = new ArrayList<>();
        Bundle extras = getIntent().getExtras();
        double result = extras.getDouble("Age");
        if (result > 3 && result < 7) {
            allowApps = List.of(res.getStringArray(R.array.threeplus));
        } else if (result >= 7 && result < 12){
            allowApps = List.of(res.getStringArray(R.array.sevenplus));
        } else if (result >= 12 && result < 16){
            allowApps = List.of(res.getStringArray(R.array.twelveplus));
        } else if (result >= 16 && result < 30){
            allowApps = List.of(res.getStringArray(R.array.sixtenplus));
        } else {
            Toast.makeText(ModeActivity.this, "You're in normal mode", Toast.LENGTH_LONG).show();
            super.finish();
        }
        for (int i = 0; i < packs.size(); i++) {
            PackageInfo p = packs.get(i);
            if ((!isSystemPackage(p)) && allowApps.contains(p.applicationInfo.packageName)) {
                // Filtering only allowed app in the main list
                String appName = p.applicationInfo.loadLabel(getPackageManager()).toString();
                Drawable icon = p.applicationInfo.loadIcon(getPackageManager());
                String packages = p.applicationInfo.packageName;
                apps.add(new AppList(appName, icon, packages));
            }
        }
        return apps;
    }

    private boolean isSystemPackage(PackageInfo pkgInfo) {
        return (pkgInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 0;
    }
}