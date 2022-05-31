package com.example.memorite;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.memorite.model.Entry;
import com.google.android.material.textfield.TextInputLayout;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.Locale;

public class EntryEditView extends AppCompatActivity {

    private DatePickerDialog datePickerDialog;
    private Button dateButton;
    private int position;
    private TextView memoTitle;
    private EditText memoContent;
    private Button mapButton;
    private RadioGroup moodButtons;
    private TextInputLayout pinInputBox;
    private Button saveButton;
    private ImageView memoImage;
    private Context context;
    private Boolean imageSetted = false;
    private String imagePath;
    private double lt = -1.0, ln = -1.0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getApplicationContext();
        setContentView(R.layout.activity_entry_edit_view);
        dateButton = findViewById(R.id.datePickerButton);
        dateButton.setText(getTodaysDate());

        memoTitle = findViewById(R.id.memoTitle);
        memoContent = findViewById(R.id.memoContent);
        mapButton = findViewById(R.id.mapButton);
        moodButtons = findViewById(R.id.moodButtons);
        pinInputBox = findViewById(R.id.pinInputBox);
        saveButton = findViewById(R.id.saveButton);
        memoImage = findViewById(R.id.memoImage);

        initDatePicker();
        getIntentMethod();
    }

    private void getIntentMethod() {
        position = getIntent().getIntExtra("position", -1);
        fillAreas(position);
    }

    private void fillAreas(int position) {
        Entry entry = MemoListView.entryArrayList.get(position);

        if(entry.getTitle() != null){
            memoTitle.setText(entry.getTitle());
        }

        if(entry.getMemo() != null){
            memoContent.setText(entry.getMemo());
        }

        if(entry.getDate() != null){
            dateButton.setText(entry.getDate());
        }

        if(entry.getImage() != null){
//            Bitmap bmp = BitmapFactory.decodeByteArray(entry.getImage(), 0, entry.getImage().length);
//            memoImage.setImageBitmap(bmp);
            memoImage.setImageBitmap(BitmapFactory.decodeFile(entry.getImage()));
        }

        if(entry.getMood() != null){
            moodButtons.check(entry.getMood());
        }

        if(entry.getPassword() != null){
            pinInputBox.getEditText().setText(entry.getPassword());
        }

        if(entry.getLatitude() != null && entry.getLongitude() != null){
            lt = entry.getLatitude();
            ln = entry.getLongitude();

            mapButton.setBackgroundColor(ContextCompat.getColor(context, R.color.green));
            mapButton.setText("Change Location");
            Geocoder geocoder = new Geocoder(context, Locale.getDefault());
            try {
                mapButton.setTooltipText(geocoder.getFromLocation(lt, ln, 1).get(0).getAddressLine(0));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        memoContent.setText(entry.getMemo());
    }

    private String getTodaysDate() {
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        month += 1;
        int day = cal.get(Calendar.DAY_OF_MONTH);
        return makeDateString(day, month, year);
    }

    private void initDatePicker() {
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month += 1;
                String date = makeDateString(day, month, year);
                dateButton.setText(date);
            }
        };

        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        datePickerDialog = new DatePickerDialog(this, dateSetListener, year, month, day);
        datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
    }

    private String makeDateString(int day, int month, int year) {
        return day + "." + month + "." + year;
    }

    public void openDatePicker(View view){
        datePickerDialog.show();
    }

    public void onImageEditButtonPressed(View view){
        Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        imageActivityResultLauncher.launch(i);
    }

    ActivityResultLauncher<Intent> imageActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if(result.getResultCode() == RESULT_OK && result.getData() != null){
                        Uri selectedImage = result.getData().getData();
                        String[] filePathColumn = { MediaStore.Images.Media.DATA };
                        Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
                        cursor.moveToFirst();
                        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                        String picturePath = cursor.getString(columnIndex);
                        imagePath = picturePath;
                        cursor.close();
                        memoImage.setImageBitmap(BitmapFactory.decodeFile(picturePath));
                        imageSetted = true;
                    }
                }
            }
    );

    public void onMapButtonPressed(View view){
        Intent intent = new Intent(getApplicationContext(), MapsActivity.class);
        mapActivityResultLauncher.launch(intent);

    }

    ActivityResultLauncher<Intent> mapActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if(result.getResultCode() == RESULT_OK && result.getData() != null){
                        lt = result.getData().getDoubleExtra("lt", -1.0);
                        ln = result.getData().getDoubleExtra("ln", -1.0);
                        mapButton.setBackgroundColor(ContextCompat.getColor(context, R.color.green));
                        mapButton.setText("Change Location");
                        Geocoder geocoder = new Geocoder(context, Locale.getDefault());
                        try {
                            mapButton.setTooltipText(geocoder.getFromLocation(lt, ln, 1).get(0).getAddressLine(0));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
    );

    public void saveEntryButtonPressed(View view){
        if(lt == -1.0 || ln == -1.0){
            Toast.makeText(context, "Please choose a location", Toast.LENGTH_SHORT).show();
            return;
        }
        if(moodButtons.getCheckedRadioButtonId() == -1){
            Toast.makeText(context, "Please choose a mood", Toast.LENGTH_SHORT).show();
            return;
        }
        if(memoContent.getText().toString().isEmpty()){
            Toast.makeText(context, "Please fill the content of the diary entry", Toast.LENGTH_SHORT).show();
            return;
        }

        Entry currEntry = MemoListView.entryArrayList.get(position);
        currEntry.setDate(dateButton.getText().toString());
        currEntry.setLatitude(lt);
        currEntry.setLongitude(ln);
        currEntry.setMemo(memoContent.getText().toString());
        currEntry.setMood(moodButtons.getCheckedRadioButtonId());
        currEntry.setTitle(memoTitle.getText().toString());

        if(imageSetted){
            currEntry.setImage(imagePath);
        }
        if(!pinInputBox.getEditText().getText().toString().isEmpty()){
            currEntry.setPassword(pinInputBox.getEditText().getText().toString());
        }
        MemoListView.entryAdapter.notifyItemChanged(position);
        finish();
    }
}