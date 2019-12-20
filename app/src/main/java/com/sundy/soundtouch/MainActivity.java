package com.sundy.soundtouch;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.yanzhenjie.permission.Action;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.runtime.Permission;
import com.yanzhenjie.permission.runtime.PermissionDef;

import java.io.File;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    TextView textViewConsole = null;
    EditText editSourceFile = null;
    EditText editOutputFile = null;
    EditText editTempo = null;
    EditText editPitch = null;
    CheckBox checkBoxPlay = null;

    StringBuilder consoleText = new StringBuilder();

    @SuppressLint("WrongConstant")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        requestPermission(Permission.Group.STORAGE);
        textViewConsole = (TextView) findViewById(R.id.textViewResult);
        editSourceFile = (EditText) findViewById(R.id.editTextSrcFileName);
        editOutputFile = (EditText) findViewById(R.id.editTextOutFileName);

        editTempo = (EditText) findViewById(R.id.editTextTempo);
        editPitch = (EditText) findViewById(R.id.editTextPitch);

        Button buttonFileSrc = (Button) findViewById(R.id.buttonSelectSrcFile);
        Button buttonFileOutput = (Button) findViewById(R.id.buttonSelectOutFile);
        Button buttonProcess = (Button) findViewById(R.id.buttonProcess);
        buttonFileSrc.setOnClickListener(this);
        buttonFileOutput.setOnClickListener(this);
        buttonProcess.setOnClickListener(this);

        checkBoxPlay = (CheckBox) findViewById(R.id.checkBoxPlay);
        String sdDir = Environment.getExternalStorageDirectory() + "/Download/test.wav";//获取跟目录
        editSourceFile.setText(sdDir);
        editOutputFile.setText(sdDir);
        // Check soundtouch library presence & version
        checkLibVersion();
    }

    /// Function to append status text onto "console box" on the Activity
    public void appendToConsole(final String text) {
        // run on UI thread to avoid conflicts
        runOnUiThread(new Runnable() {
            public void run() {
                consoleText.append(text);
                consoleText.append("\n");
                textViewConsole.setText(consoleText);
            }
        });
    }


    /// print SoundTouch native library version onto console
    protected void checkLibVersion() {
        String ver = SoundTouch.getVersionString();
        appendToConsole("SoundTouch native library version = " + ver);
    }


    /// Button click handler
    @Override
    public void onClick(View arg0) {
        switch (arg0.getId()) {
            case R.id.buttonSelectSrcFile:
            case R.id.buttonSelectOutFile:
                // one of the file select buttons clicked ... we've not just implemented them ;-)
                Toast.makeText(this, "File selector not implemented, sorry! Enter the file path manually ;-)", Toast.LENGTH_LONG).show();
                break;

            case R.id.buttonProcess:
                // button "process" pushed
                process();
                break;
        }

    }


    /// Play audio file
    protected void playWavFile(String fileName) {
        File file2play = new File(fileName);
        Intent i = new Intent();
        i.setAction(android.content.Intent.ACTION_VIEW);
        i.setDataAndType(Uri.fromFile(file2play), "audio/wav");
        startActivity(i);
    }


    /// Helper class that will execute the SoundTouch processing. As the processing may take
    /// some time, run it in background thread to avoid hanging of the UI.
    protected class ProcessTask extends AsyncTask<ProcessTask.Parameters, Integer, Long> {
        /// Helper class to store the SoundTouch file processing parameters
        public final class Parameters {
            String inFileName;
            String outFileName;
            float tempo;
            float pitch;
        }


        /// Function that does the SoundTouch processing
        public final long doSoundTouchProcessing(Parameters params) {

            SoundTouch st = new SoundTouch();
            st.setTempo(params.tempo);
            st.setPitchSemiTones(params.pitch);
            Log.i("SoundTouch", "process file " + params.inFileName);
            long startTime = System.currentTimeMillis();
            int res = st.processFile(params.inFileName, params.outFileName);
            long endTime = System.currentTimeMillis();
            float duration = (endTime - startTime) * 0.001f;

            Log.i("SoundTouch", "process file done, duration = " + duration);
            appendToConsole("Processing done, duration " + duration + " sec.");
            if (res != 0) {
                String err = SoundTouch.getErrorString();
                appendToConsole("Failure: " + err);
                return -1L;
            }

            // Play file if so is desirable
            if (checkBoxPlay.isChecked()) {
                playWavFile(params.outFileName);
            }
            return 0L;
        }


        /// Overloaded function that get called by the system to perform the background processing
        @Override
        protected Long doInBackground(Parameters... aparams) {
            return doSoundTouchProcessing(aparams[0]);
        }

    }


    /// process a file with SoundTouch. Do the processing using a background processing
    /// task to avoid hanging of the UI
    protected void process() {
        try {
            ProcessTask task = new ProcessTask();
            ProcessTask.Parameters params = task.new Parameters();
            // parse processing parameters
            params.inFileName = editSourceFile.getText().toString();
            params.outFileName = editOutputFile.getText().toString();
            params.tempo = 0.01f * Float.parseFloat(editTempo.getText().toString());
            params.pitch = Float.parseFloat(editPitch.getText().toString());

            // update UI about status
            appendToConsole("Process audio file :" + params.inFileName + " => " + params.outFileName);
            appendToConsole("Tempo = " + params.tempo);
            appendToConsole("Pitch adjust = " + params.pitch);

            Toast.makeText(this, "Starting to process file " + params.inFileName + "...", Toast.LENGTH_SHORT).show();

            // start SoundTouch processing in a background thread
            task.execute(params);
//			task.doSoundTouchProcessing(params);	// this would run processing in main thread

        } catch (Exception exp) {
            exp.printStackTrace();
        }

    }

    /**
     * Request permissions.
     */
    private void requestPermission(@PermissionDef String... permissions) {
        AndPermission.with(this)
                .runtime()
                .permission(permissions)
                // .rationale(new RuntimeRationale())
                .onGranted(new Action<List<String>>() {
                    @Override
                    public void onAction(List<String> permissions) {

                    }
                })
                .onDenied(new Action<List<String>>() {
                    @Override
                    public void onAction(@NonNull List<String> permissions) {

                    }
                })
                .start();
    }
}
