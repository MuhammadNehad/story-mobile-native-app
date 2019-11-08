package mainproject.mainroject.story;


import android.Manifest;
import android.content.pm.PackageManager;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import me.dm7.barcodescanner.zbar.BarcodeFormat;
import me.dm7.barcodescanner.zbar.Result;
import me.dm7.barcodescanner.zbar.ZBarScannerView;

public class barcodeScannerActivity extends AppCompatActivity implements
        ZBarScannerView.ResultHandler
        {
    private static final String FLASH_STATE = "FLASH_STATE";
    private static final String AUTO_FOCUS_STATE = "AUTO_FOCUS_STATE";
    private static final String SELECTED_FORMATS = "SELECTED_FORMATS";
    private static final String CAMERA_ID = "CAMERA_ID";
            private static final int PERMISSION_CAMERA = 12;
            private ZBarScannerView mScannerView;
    private boolean mFlash;
    private boolean mAutoFocus;
    private ArrayList<Integer> mSelectedIndices;
    private int mCameraId = -1;
    boolean ISBNCHECKED = false;
    boolean EANCHECKED = false;
    String ISBNCODE ="";
    String EANCODE ="";

    @Override
    public void onCreate(Bundle state) {
        super.onCreate(state);
        if(state != null) {
            mFlash = state.getBoolean(FLASH_STATE, false);
            mAutoFocus = state.getBoolean(AUTO_FOCUS_STATE, true);
            mSelectedIndices = state.getIntegerArrayList(SELECTED_FORMATS);
            mCameraId = state.getInt(CAMERA_ID, -1);
        } else {
            mFlash = false;
            mAutoFocus = true;
            mSelectedIndices = null;
            mCameraId = -1;
        }

        if(ContextCompat.checkSelfPermission(barcodeScannerActivity.this,Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
        if(ActivityCompat.shouldShowRequestPermissionRationale(barcodeScannerActivity.this,Manifest.permission.CAMERA))
        {

        }else {
            ActivityCompat.requestPermissions(barcodeScannerActivity.this, new String[]{Manifest.permission.CAMERA}, PERMISSION_CAMERA);
        }
        }
        //        ViewGroup contentFrame = (ViewGroup) findViewById(R.id.content_frame);
        mScannerView = new ZBarScannerView(this);
        setupFormats();
//        contentFrame.addView(mScannerView);
        setContentView(mScannerView);

    }

    @Override
    public void onResume() {
        super.onResume();
        mScannerView.setResultHandler(this);
        mScannerView.startCamera(mCameraId);
        mScannerView.setFlash(mFlash);
        mScannerView.setAutoFocus(mAutoFocus);
        mScannerView.resumeCameraPreview(this);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(FLASH_STATE, mFlash);
        outState.putBoolean(AUTO_FOCUS_STATE, mAutoFocus);
        outState.putIntegerArrayList(SELECTED_FORMATS, mSelectedIndices);
        outState.putInt(CAMERA_ID, mCameraId);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
                return super.onOptionsItemSelected(item);
        }


    @Override
    public void handleResult(Result rawResult) {

        try {
//            Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
//            Ringtone r = RingtoneManager.getRingtone(getApplicationContext(), notification);
//            r.play();
        } catch (Exception e) {}
//        showMessageDialog("Contents = " + rawResult.getContents() + ", Format = " + rawResult.getBarcodeFormat().getName());
        if(!ISBNCHECKED) {
            if (rawResult.getBarcodeFormat().getName().contains("ISBN")) {
                ISBNCHECKED = true;
                ISBNCODE = rawResult.getContents();
                home.ISBNEDITText.setText(rawResult.getContents());

                Toast.makeText(barcodeScannerActivity.this,"ISBN Checked",Toast.LENGTH_LONG).show();
                if(!EANCHECKED)
                {

                }
//        onBackPressed();
            }
        }
        if(!EANCHECKED){
            if (rawResult.getBarcodeFormat().getName().contains("EAN")) {
                EANCHECKED = true;
                EANCODE = rawResult.getContents();
                home.EANEDITBOX.setText(rawResult.getContents());
//        onBackPressed();
                Toast.makeText(barcodeScannerActivity.this,"EAN Checked",Toast.LENGTH_LONG).show();
                if(!ISBNCHECKED)
                {
                    mScannerView.resumeCameraPreview(this);

                }

            }
        }
        if(!EANCHECKED || !ISBNCHECKED)
        {
            mScannerView.resumeCameraPreview(this);

        }

        if(EANCHECKED && ISBNCHECKED)
        {
            onBackPressed();
            finish();
        }

    }

    public void showMessageDialog(String message) {
        Toast.makeText(barcodeScannerActivity.this,message,Toast.LENGTH_LONG).show();
//        fragment.show(getSupportFragmentManager(), "scan_results");
    }

    public void closeMessageDialog() {
        closeDialog("scan_results");
    }

    public void closeFormatsDialog() {
        closeDialog("format_selector");
    }

    public void closeDialog(String dialogName) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        DialogFragment fragment = (DialogFragment) fragmentManager.findFragmentByTag(dialogName);
        if(fragment != null) {
            fragment.dismiss();
        }
    }

    public void onDialogPositiveClick(DialogFragment dialog) {
        // Resume the camera
        mScannerView.resumeCameraPreview(this);
    }

    public void onFormatsSaved(ArrayList<Integer> selectedIndices) {
        mSelectedIndices = selectedIndices;
        setupFormats();
    }

    public void onCameraSelected(int cameraId) {
        mCameraId = cameraId;
        mScannerView.startCamera(mCameraId);
        mScannerView.setFlash(mFlash);
        mScannerView.setAutoFocus(mAutoFocus);

    }


    public void setupFormats() {
        List<BarcodeFormat> formats = new ArrayList<BarcodeFormat>();
        if(mSelectedIndices == null || mSelectedIndices.isEmpty()) {
            mSelectedIndices = new ArrayList<Integer>();

            for(int i = 0; i < BarcodeFormat.ALL_FORMATS.size(); i++) {
                mSelectedIndices.add(i);
            }
        }
        for(int index : mSelectedIndices) {
            formats.add(BarcodeFormat.ALL_FORMATS.get(index));
        }
//            formats.add(BarcodeFormat.ISBN13);
//            formats.add(BarcodeFormat.I25);

//        formats.add(BarcodeFormat.CODABAR);
//        formats.add(BarcodeFormat.QRCODE);
//        formats.add(BarcodeFormat.DATABAR);
//
////        formats.add(BarcodeFormat.UPCE);
//        formats.add(BarcodeFormat.DATABAR_EXP);
//        formats.add(BarcodeFormat.EAN8);
//        formats.add(BarcodeFormat.PDF417);
//            formats.add(BarcodeFormat.ISBN10);
        if(mScannerView != null) {
            mScannerView.setFormats(formats);
        }
    }


    @Override
    public void onPause() {
        super.onPause();
        mScannerView.stopCamera();
        closeMessageDialog();
        closeFormatsDialog();
    }

            @Override
            public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
                Log.i("barcodeScanner","onRequestPermissionResult");
                if (requestCode == PERMISSION_CAMERA) {
                    if (grantResults.length <= 0) {
                        Log.i("barcodeScanner", "User interaction was cancelled.");
                    } else if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                        Log.i("barcodeScanner", "User interaction was accepted.");

                    } else {

                    }
                }
            }
        }