package com.doubleclick.audiorecordinganimation;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ContextWrapper;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.doubleclick.audiorecordinganimation.audio_record_view.AttachmentOption;
import com.doubleclick.audiorecordinganimation.audio_record_view.AttachmentOptionsListener;
import com.doubleclick.audiorecordinganimation.audio_record_view.AudioRecordView;

import java.io.File;
import java.io.IOException;
import java.util.Date;

public class MainActivity extends AppCompatActivity implements AudioRecordView.RecordingListener, View.OnClickListener, AttachmentOptionsListener {

    private AudioRecordView audioRecordView;
    private RecyclerView recyclerViewMessages;
    private long time;
    private MediaRecorder mediaRecorder;
    private String audioPath;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        audioRecordView = new AudioRecordView();
        audioRecordView.initView(findViewById(R.id.layoutMain));
        /////////////////////////////////////////////////////////////////////////////////
        View containerView = audioRecordView.setContainerView(R.layout.layout_chatting);
        recyclerViewMessages = containerView.findViewById(R.id.recyclerViewMessages);
        containerView.findViewById(R.id.imageViewTitleIcon).setOnClickListener(this);
        containerView.findViewById(R.id.imageViewMenu).setOnClickListener(this);
        ///////////////////////////////////////////////////////////////////////////////
        audioRecordView.setRecordingListener(this);
        audioRecordView.setAttachmentOptions(AttachmentOption.getDefaultList(), this);
        setListener();
        audioRecordView.removeAttachmentOptionAnimation(false);


    }

    private void setListener() {
        audioRecordView.getEmojiView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                audioRecordView.hideAttachmentOptionView();
                showToast("Emoji Icon Clicked");
            }
        });

        audioRecordView.getCameraView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                audioRecordView.hideAttachmentOptionView();
                showToast("Camera Icon Clicked");
            }
        });

        audioRecordView.getSendView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String msg = audioRecordView.getMessageView().getText().toString().trim();
                showToast(msg);
                audioRecordView.getMessageView().setText("");

            }
        });
    }

    private void showToast(String message) {
        Toast toast = Toast.makeText(this, message, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

    private void debug(String log) {
        Log.d("Recording", log);
    }


    @Override
    public void onClick(View view) {

    }

    @Override
    public void onClick(AttachmentOption attachmentOption) {
        switch (attachmentOption.getId()) {
            case AttachmentOption.DOCUMENT_ID:
                showToast("Document Clicked");
                break;
            case AttachmentOption.CAMERA_ID:
                showToast("Camera Clicked");
                break;
            case AttachmentOption.GALLERY_ID:
                showToast("Gallery Clicked");
                break;
            case AttachmentOption.AUDIO_ID:
                showToast("Audio Clicked");
                break;
            case AttachmentOption.LOCATION_ID:
                showToast("Location Clicked");
                break;
            case AttachmentOption.CONTACT_ID:
                showToast("Contact Clicked");
                break;
        }
    }

    @Override
    public void onRecordingStarted() {
        showToast("started");
        debug("started");
        setUpRecording();
        try {
            mediaRecorder.prepare();
            mediaRecorder.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onRecordingLocked() {
        showToast("locked");
        debug("locked");
    }

    @Override
    public void onRecordingCompleted() {
        showToast("completed");
        debug("completed");
        try {
            mediaRecorder.stop();
            mediaRecorder.release();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onRecordingCanceled() {
        mediaRecorder.reset();
        mediaRecorder.release();
    }

    private void setUpRecording() {
        mediaRecorder = new MediaRecorder();
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath() + "/RECORDING/"); // String f = "/storage/emulated/0/Download";
        if (!file.exists()) {
            file.mkdirs();
        }
        File f = new File(file, "chat" + new Date().getTime() + ".mp3");
        audioPath = getFilePath(); //file.getAbsolutePath() + File.separator + System.currentTimeMillis() + ".3gp";
        Log.e("audioPath", f.getPath());
        mediaRecorder.setOutputFile(f.getPath());
    }

    private String getFilePath() {
        ContextWrapper contextWrapper = new ContextWrapper(this);
        File file = contextWrapper.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS);
        Date date = new Date();
        File f = new File(file, "chat" + date.getTime() + ".mp3");
        return f.getPath();
    }
}