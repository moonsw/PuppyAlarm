package com.bitcamp.hack.puppyalarm;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;


public class AlarmDetailsActivity extends ActionBarActivity {

    private AlarmDBHelper dbHelper = new AlarmDBHelper(this);

    private AlarmModel alarmDetails;

    private TimePicker timePicker;
    private EditText edtName;
    private CustomSwitch chkWeekly;
    private CustomSwitch chkSunday;
    private CustomSwitch chkMonday;
    private CustomSwitch chkTuesday;
    private CustomSwitch chkWednesday;
    private CustomSwitch chkThursday;
    private CustomSwitch chkFriday;
    private CustomSwitch chkSaturday;
    private TextView txtToneSelection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_ACTION_BAR);

        setContentView(R.layout.activity_details);

        getActionBar().setTitle("Create New Alarm");
        getActionBar().setDisplayHomeAsUpEnabled(true);

        timePicker = (TimePicker) findViewById(R.id.alarm_details_time_picker);
        edtName = (EditText) findViewById(R.id.alarm_details_name);
        chkWeekly = (CustomSwitch) findViewById(R.id.alarm_details_repeat_weekly);
        chkSunday = (CustomSwitch) findViewById(R.id.alarm_details_repeat_sunday);
        chkMonday = (CustomSwitch) findViewById(R.id.alarm_details_repeat_monday);
        chkTuesday = (CustomSwitch) findViewById(R.id.alarm_details_repeat_tuesday);
        chkWednesday = (CustomSwitch) findViewById(R.id.alarm_details_repeat_wednesday);
        chkThursday = (CustomSwitch) findViewById(R.id.alarm_details_repeat_thursday);
        chkFriday = (CustomSwitch) findViewById(R.id.alarm_details_repeat_friday);
        chkSaturday = (CustomSwitch) findViewById(R.id.alarm_details_repeat_saturday);
        txtToneSelection = (TextView) findViewById(R.id.alarm_label_tone_selection);

        long id = getIntent().getExtras().getLong("id");

        if (id == -1) {
            alarmDetails = new AlarmModel();
        } else {
            alarmDetails = dbHelper.getAlarm(id);

            timePicker.setCurrentMinute(alarmDetails.timeMinute);
            timePicker.setCurrentHour(alarmDetails.timeHour);

            edtName.setText(alarmDetails.name);

            chkWeekly.setChecked(alarmDetails.repeatWeekly);
            chkSunday.setChecked(alarmDetails.getRepeatingDay(AlarmModel.SUNDAY));
            chkMonday.setChecked(alarmDetails.getRepeatingDay(AlarmModel.MONDAY));
            chkTuesday.setChecked(alarmDetails.getRepeatingDay(AlarmModel.TUESDAY));
            chkWednesday.setChecked(alarmDetails.getRepeatingDay(AlarmModel.WEDNESDAY));
            chkThursday.setChecked(alarmDetails.getRepeatingDay(AlarmModel.THURSDAY));
            chkFriday.setChecked(alarmDetails.getRepeatingDay(AlarmModel.FRDIAY));
            chkSaturday.setChecked(alarmDetails.getRepeatingDay(AlarmModel.SATURDAY));

            txtToneSelection.setText(RingtoneManager.getRingtone(this, alarmDetails.alarmTone).getTitle(this));
        }

        final LinearLayout ringToneContainer = (LinearLayout) findViewById(R.id.alarm_ringtone_container);
        ringToneContainer.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RingtoneManager.ACTION_RINGTONE_PICKER);
                startActivityForResult(intent, 1);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_alarm_details, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case android.R.id.home: {
                finish();
                break;
            }
            case R.id.action_save_alarm_details: {
                updateModelFromLayout();

                AlarmManagerHelper.cancelAlarms(this);

                if (alarmDetails.id < 0) {
                    dbHelper.createAlarm(alarmDetails);
                } else {
                    dbHelper.updateAlarm(alarmDetails);
                }

                AlarmManagerHelper.setAlarms(this);

                setResult(RESULT_OK);
                finish();
            }
        }

        return super.onOptionsItemSelected(item);
    }
    private void updateModelFromLayout() {
        alarmDetails.timeMinute = timePicker.getCurrentMinute().intValue();
        alarmDetails.timeHour = timePicker.getCurrentHour().intValue();
        alarmDetails.name = edtName.getText().toString();
        alarmDetails.repeatWeekly = chkWeekly.isChecked();
        alarmDetails.setRepeatingDay(AlarmModel.SUNDAY, chkSunday.isChecked());
        alarmDetails.setRepeatingDay(AlarmModel.MONDAY, chkMonday.isChecked());
        alarmDetails.setRepeatingDay(AlarmModel.TUESDAY, chkTuesday.isChecked());
        alarmDetails.setRepeatingDay(AlarmModel.WEDNESDAY, chkWednesday.isChecked());
        alarmDetails.setRepeatingDay(AlarmModel.THURSDAY, chkThursday.isChecked());
        alarmDetails.setRepeatingDay(AlarmModel.FRDIAY, chkFriday.isChecked());
        alarmDetails.setRepeatingDay(AlarmModel.SATURDAY, chkSaturday.isChecked());
        alarmDetails.isEnabled = true;
    }

}