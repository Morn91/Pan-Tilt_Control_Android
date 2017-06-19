package morn91.com.github.pan_tilt_control;

import android.app.Activity;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.TextView;

import morn91.com.github.pan_tilt_control.MyTimePickerDialog;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Calendar;
import java.util.Scanner;
import java.util.TimeZone;

public class MainActivity extends Activity {
    public Typeface timerFont;
    public TextView timerBg;
    public TextView timer0;
    public TextView timer1;
    public TextView timer2;
    public TextView colon0;
    public TextView colon1;
    public TextView shotsText;
    public TextView degrees;
    public ProgressBar progressBar;
    public Switch pause;
    public Button startButton;
    public Button stopButton;
    public Button batteryButton;
    public Button setStartButton;
    public Button setEndButton;
    public Button toStartButton;
    public Button toEndButton;
    public ImageButton smoothButton;
    public ImageButton rightButton;
    public ImageButton leftButton;
    public ImageButton upButton;
    public ImageButton downButton;
    public EditText shotsField;
    public EditText periodField;

    public AlertDialog.Builder alertDialog;
    public BluetoothSocket bluetoothSocket = null;
    public BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    public InputStream input;
    public OutputStream output;

    public String address = "20:15:02:05:70:69";
    public int progress = 0, color[] = {Color.parseColor("#80cbc4"), Color.parseColor("#ffffff"), Color.parseColor("#c4cb80")};
    public boolean go = false, smooth = false;
    Calendar timer[] = new Calendar[4];

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        timerFont = Typeface.createFromAsset(getAssets(), "fonts/digital_clock.ttf");

        timerBg = (TextView) findViewById(R.id.timerBg);
        timerBg.setTypeface(timerFont);
        timerBg.setTextColor(color[1]);

        timer0 = (TextView) findViewById(R.id.timer0);
        timer0.setTypeface(timerFont);
        timer0.setTextColor(color[0]);

        colon0 = (TextView) findViewById(R.id.colon0);
        colon0.setTypeface(timerFont);
        colon0.setTextColor(color[1]);

        timer1 = (TextView) findViewById(R.id.timer1);
        timer1.setTypeface(timerFont);
        timer1.setTextColor(color[1]);

        colon1 = (TextView) findViewById(R.id.colon1);
        colon1.setTypeface(timerFont);
        colon1.setTextColor(color[1]);

        timer2 = (TextView) findViewById(R.id.timer2);
        timer2.setTypeface(timerFont);
        timer2.setTextColor(color[2]);

        shotsText = (TextView) findViewById(R.id.shotsText);
        degrees = (TextView) findViewById(R.id.degrees);

        timer0.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                resetTimer();
                return true;
            }
        });

        timer2.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                resetTimer();
                return true;
            }
        });

        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar.setProgress(progress);

        pause = (Switch) findViewById(R.id.pause);

        startButton = (Button) findViewById(R.id.start);
        stopButton = (Button) findViewById(R.id.stop);
        batteryButton = (Button) findViewById(R.id.battery);
        setStartButton = (Button) findViewById(R.id.setStart);
        setEndButton = (Button) findViewById(R.id.setEnd);
        toStartButton = (Button) findViewById(R.id.toStart);
        toEndButton = (Button) findViewById(R.id.toEnd);

        smoothButton = (ImageButton) findViewById(R.id.smoothButton);
        smoothButton.setImageResource(R.mipmap.smooth0);
        smoothButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (smooth) {
                    smooth = false;
                    smoothButton.setImageResource(R.mipmap.smooth0);
                } else {
                    smooth = true;
                    smoothButton.setImageResource(R.mipmap.smooth1);
                }
            }
        });

        rightButton = (ImageButton) findViewById(R.id.right);
        rightButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        try {
                            output.write('r');
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        return true;
                    case MotionEvent.ACTION_UP:

                        try {
                            output.write('s');
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        return true;
                }
                return false;
            }
        });

        leftButton = (ImageButton) findViewById(R.id.left);
        leftButton.setRotation(180);
        leftButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        try {
                            output.write('l');
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        return true;
                    case MotionEvent.ACTION_UP:

                        try {
                            output.write('s');
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        return true;
                }
                return false;
            }
        });

        upButton = (ImageButton) findViewById(R.id.up);
        upButton.setRotation(270);
        upButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        try {
                            output.write('u');
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        return true;
                    case MotionEvent.ACTION_UP:

                        try {
                            output.write('s');
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        return true;
                }
                return false;
            }
        });

        downButton = (ImageButton) findViewById(R.id.down);
        downButton.setRotation(90);
        downButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        try {
                            output.write('d');
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        return true;
                    case MotionEvent.ACTION_UP:

                        try {
                            output.write('s');
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        return true;
                }
                return false;
            }
        });

        shotsField = (EditText) findViewById(R.id.shotsField);
        shotsField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                calculate(-1);
            }
        });

        periodField = (EditText) findViewById(R.id.periodField);
        periodField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                calculate(-1);
            }
        });

        for (int i = 0; i < timer.length; i++) {
            timer[i] = Calendar.getInstance();
            timer[i].setTimeInMillis(0);
        }
        timer[1].setTimeZone(TimeZone.getTimeZone("UTC"));

        alertDialog = new AlertDialog.Builder(this);

        calculate(-1);

        new connect().execute();

    }

    @Override
    public void onBackPressed() {
        if (go) {
            moveTaskToBack(true);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == -1) {
            new connect().execute();
        } else {
            finish();
        }
    }

    class connect extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setIndeterminate(true);
            lock();
            batteryButton.setText("");
            stopButton.setEnabled(false);
            startButton.setVisibility(View.VISIBLE);
            stopButton.setVisibility(View.INVISIBLE);
            timer0.setEnabled(false);
            timer1.setEnabled(false);
            timer2.setEnabled(false);
        }

        @Override
        protected Void doInBackground(Void... params) {
            if (bluetoothAdapter.isEnabled()) {
                if (!(bluetoothSocket != null && bluetoothSocket.isConnected())) {
                    try {
                        BluetoothDevice device = BluetoothAdapter.getDefaultAdapter().getRemoteDevice(address);
                        Method m = device.getClass().getMethod("createRfcommSocket", new Class[]{int.class});
                        bluetoothSocket = (BluetoothSocket) m.invoke(device, 1);
                        bluetoothSocket.connect();
                    } catch (IOException e) {
                        Log.d("BLUETOOTH", e.getMessage());
                    } catch (SecurityException e) {
                        Log.d("BLUETOOTH", e.getMessage());
                    } catch (NoSuchMethodException e) {
                        Log.d("BLUETOOTH", e.getMessage());
                    } catch (IllegalArgumentException e) {
                        Log.d("BLUETOOTH", e.getMessage());
                    } catch (IllegalAccessException e) {
                        Log.d("BLUETOOTH", e.getMessage());
                    } catch (InvocationTargetException e) {
                        Log.d("BLUETOOTH", e.getMessage());
                    }
                }
                try {
                    input = bluetoothSocket.getInputStream();
                    output = bluetoothSocket.getOutputStream();
                } catch (IOException e) {
                    Log.d("BLUETOOTH", e.getMessage());
                }
            } else
                startActivityForResult(new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE), 0);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if (bluetoothSocket != null && bluetoothSocket.isConnected()) {
                progressBar.setIndeterminate(false);
                if (go) {
                    stopButton.setEnabled(true);
                } else {
                    unlock();
                }
                timer0.setEnabled(true);
                timer1.setEnabled(true);
                timer2.setEnabled(true);
                request(null);
            } else if (bluetoothAdapter.isEnabled()) {
                alertDialog.setTitle(getString(R.string.error))
                        .setMessage(getString(R.string.noConnectDialog))
                        .setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                new connect().execute();
                            }
                        })
                        .setNegativeButton(getString(R.string.no), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (!go)
                                    finish();
                            }
                        })
                        .setOnCancelListener(new DialogInterface.OnCancelListener() {
                            @Override
                            public void onCancel(DialogInterface dialog) {
                                finish();
                            }
                        })
                        .show();
            }
        }
    }

    public void setStart(View view) {
        try {
            output.write('x');
        } catch (IOException e) {
            e.printStackTrace();
        }
        request(null);
    }

    public void setEnd(View view) {
        try {
            output.write('y');
        } catch (IOException e) {
            e.printStackTrace();
        }
        request(null);
    }

    public void toStart(View view) {
        try {
            output.write('a');
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void toEnd(View view) {
        try {
            output.write('b');
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void request(View view) {
        if (bluetoothSocket != null && bluetoothSocket.isConnected()) {
            String buff = "";
            try {
                output.write('*');
                int i = 0;
                char a;
                while (input.available() == 0 && i < 100) {
                    i++;
                    SystemClock.sleep(10);
                }
                SystemClock.sleep(100);
                if (input.available() > 0) {
                    while (true) {
                        if (input.available() > 0) {
                            a = (char) input.read();
                            if (a == '\n') {
                                break;
                            } else
                                buff += a;
                        }
                    }
                } else {
                    new connect().execute();
                }
            } catch (IOException e) {
                Log.d("BLUETOOTH", e.getMessage());
            }
            Scanner scanner = new Scanner(buff);
            long elapsed = scanner.nextInt();
            int number = scanner.nextInt();
            long period = scanner.nextInt();
            int isSmooth = scanner.nextInt();
            int isPause = scanner.nextInt();
            int angleX = scanner.nextInt();
            int angleY = scanner.nextInt();
            int voltage = scanner.nextInt();
            degrees.setText(String.format("%+.1f°\n%+.1f°", angleX / 10f, angleY / 10f));
            batteryButton.setText(String.format("%.1f", voltage / 200f));
            if (number > 0) {
                shotsField.setText(String.format("%d", number));
                smooth = isSmooth > 0;
                if (smooth) {
                    smoothButton.setImageResource(R.mipmap.smooth1);
                } else {
                    smoothButton.setImageResource(R.mipmap.smooth0);
                }
                if (isPause > 0) {
                    pause.setChecked(true);
                } else {
                    pause.setChecked(false);
                }
            }
            if (period > 0) {
                periodField.setText(String.format("%s", period / 1000f));
            }
            if (elapsed == 0) {
                unlock();
                go = false;
                progress = 0;
                progressBar.setProgress(progress);
                progressBar.setIndeterminate(false);
                shotsText.setText(R.string.shots);
            } else {
                lock();
                go = true;
                timer[3] = Calendar.getInstance();
                timer[3].set(1970, 0, 1);
                timer[0].setTimeInMillis(timer[3].getTimeInMillis() - elapsed);
                timer[3].setTimeZone(TimeZone.getTimeZone("UTC"));
                if (timer[0].getTimeInMillis() < 0) {
                    timer[0].setTimeInMillis(timer[0].getTimeInMillis() + 24 * 3600000L);
                }
                timer[2].setTimeInMillis(0);
                calculate(0);
                if (elapsed > 0) {
                    shotsText.setText(String.format("%d", Math.round((double) elapsed / period)));
                    if (smooth) {
                        if (elapsed * 2 < timer[1].getTimeInMillis()) {
                            progress = (int) Math.round(2000 * Math.pow((double) elapsed / timer[1].getTimeInMillis(), 2));
                        } else {
                            progress = (int) Math.round(1000 * (1 - 2 * Math.pow((double) elapsed / timer[1].getTimeInMillis() - 1, 2)));
                        }
                    } else {
                        progress = Math.round(1000 * (float) elapsed / number / period);
                    }
                    timer[3].setTimeInMillis(timer[1].getTimeInMillis() - elapsed);
                    timerText(timer[3], 1);
                    progressBar.setIndeterminate(false);
                    progressBar.setProgress(progress);
                } else {
                    timer[3].setTimeInMillis(-elapsed);
                    timerText(timer[3], 0);
                    progressBar.setIndeterminate(true);
                }
            }
        } else
            new connect().execute();
    }

    public void start(View view) {
        try {
            while (input.available() > 0) {
                input.read();
            }
            if (timer[0].getTimeInMillis() > 0) {
                timer[3] = Calendar.getInstance();
                timer[3].set(1970, 0, 1);
                timer[3].setTimeInMillis(timer[0].getTimeInMillis() - timer[3].getTimeInMillis());
                if (timer[3].getTimeInMillis() < 0) {
                    timer[3].setTimeInMillis(timer[3].getTimeInMillis() + 24 * 3600000L);
                }
                if (timer[3].getTimeInMillis() > 23 * 3600000L) {
                    timer[3].setTimeInMillis(0);
                }
            } else {
                timer[3].setTimeInMillis(0);
            }
            output.write(String.format("+ %d %d %d %d",
                    Integer.parseInt(shotsField.getText().toString()),
                    Math.round(Float.parseFloat(periodField.getText().toString()) * 1000),
                    timer[3].getTimeInMillis(),
                    smooth ? 1 : 0,
                    pause.isChecked() ? 1 : 0).getBytes());
            SystemClock.sleep(500);
            request(null);
        } catch (IOException e) {
            Log.d("BLUETOOTH", e.getMessage());
        }
    }

    public void stop(View view) {
        new AlertDialog.Builder(this)
                .setTitle(getString(R.string.stop))
                .setMessage(getString(R.string.stopDialog))
                .setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        try {
                            output.write('-');
                            request(null);
                        } catch (IOException e) {
                            Log.d("BLUETOOTH", e.getMessage());
                        }
                    }
                })
                .setNegativeButton(getString(R.string.no), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .show();
    }

    public void calculate(int priority) {
        int shotsNumber;
        double period;
        long duration = 0;
        if (periodField.getText().toString().equals(".")) {
            periodField.setText("0.");
            periodField.setSelection(2);
            return;
        }
        if (periodField.getText().toString().equals("")) {
            period = 0;
        } else {
            period = Double.parseDouble(periodField.getText().toString());
        }
        if (shotsField.getText().toString().equals("")) {
            shotsNumber = 0;
        } else {
            shotsNumber = Integer.parseInt(shotsField.getText().toString());
        }
        switch (priority) {
            case 0:
                break;
            case 2:
                duration = timer[2].getTimeInMillis() - timer[0].getTimeInMillis();
                if (duration < 0) {
                    duration += 24 * 3600000L;
                }
            case 1:
                if (duration == 0) {
                    duration = timer[1].getTimeInMillis();
                }
                if (period > 0) {
                    shotsField.setText("" + (int) (Math.floor(duration / 1000d / period) + 1));
                    return;
                }
                if (shotsNumber > 1) {
                    periodField.setText("" + Math.round(duration / 10 / shotsNumber) / 100f);
                    return;
                }
            case -1:
                if (shotsNumber == 0) {
                    duration = 0;
                }
                if (shotsNumber == 1) {
                    duration = 1;
                }
                if (shotsNumber > 1) {
                    duration = Math.round((shotsNumber - 1) * period * 1000L);
                }
                if (duration >= 24 * 3600000L) {
                    duration = 0;
                }
                timer[1].setTimeInMillis(duration);
        }
        if (timer[0].getTimeInMillis() > 0) {
            timer[2].setTimeInMillis(timer[0].getTimeInMillis() + timer[1].getTimeInMillis());
        }
        if (timerBg.getCurrentTextColor() == color[0]) {
            timerText(timer[0], 0);
        }
        if (timerBg.getCurrentTextColor() == color[1]) {
            timerText(timer[1], 1);
        }
        if (timerBg.getCurrentTextColor() == color[2]) {
            timerText(timer[2], 2);
        }
        if (timer[1].getTimeInMillis() > 0) {
            startButton.setEnabled(true);
        } else {
            startButton.setEnabled(false);
        }
    }

    public void timePicker(View view) {
        int n = 0;
        if (view.equals(timer1)) {
            n = 1;
        }
        if (view.equals(timer2)) {
            n = 2;
        }
        if (!go && timerBg.getCurrentTextColor() == color[n]) {
            if (n == 2 && timer[0].getTimeInMillis() == 0) {
                return;
            }
            int h, m, s;
            if (n == 0 && timer[0].getTimeInMillis() == 0) {
                Calendar current = Calendar.getInstance();
                h = current.get(Calendar.HOUR_OF_DAY);
                m = current.get(Calendar.MINUTE);
                s = current.get(Calendar.SECOND);
            } else {
                h = timer[n].get(Calendar.HOUR_OF_DAY);
                m = timer[n].get(Calendar.MINUTE);
                s = timer[n].get(Calendar.SECOND);
            }
            final int finalN = n;
            MyTimePickerDialog mTimePicker = new MyTimePickerDialog(this, new MyTimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(morn91.com.github.pan_tilt_control.TimePicker view, int hourOfDay, int minute, int seconds) {
                    timer[finalN].set(Calendar.HOUR_OF_DAY, hourOfDay);
                    timer[finalN].set(Calendar.MINUTE, minute);
                    timer[finalN].set(Calendar.SECOND, seconds);
                    calculate(finalN);
                }
            }, h, m, s, true, n);
            mTimePicker.show();
        } else {
            timerText(timer[n], n);
        }
    }

    public void timerText(Calendar t, int n) {
        if (t.getTimeInMillis() == 0) {
            timer0.setText("--");
            timer1.setText("--");
            timer2.setText("--");
        } else {
            timer0.setText(String.format("%02d", t.get(Calendar.HOUR_OF_DAY)));
            timer1.setText(String.format("%02d", t.get(Calendar.MINUTE)));
            timer2.setText(String.format("%02d", t.get(Calendar.SECOND)));
        }
        timerBg.setTextColor(color[n]);
        timer0.setTextColor(color[n]);
        timer1.setTextColor(color[n]);
        timer2.setTextColor(color[n]);
        if (n == 1 && timer[0].getTimeInMillis() > 0) {
            colon0.setTextColor(color[0]);
            colon1.setTextColor(color[2]);
        } else {
            colon0.setTextColor(color[n]);
            colon1.setTextColor(color[n]);
        }
    }

    public void resetTimer() {
        if (timer[0].getTimeInMillis() != 0) {
            new AlertDialog.Builder(this)
                    .setTitle(getString(R.string.reset))
                    .setMessage(getString(R.string.resetDialog))
                    .setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            timer[0].setTimeInMillis(0);
                            timer[2].setTimeInMillis(0);
                            timerText(timer[1], 1);
                        }
                    })
                    .setNegativeButton(getString(R.string.no), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    })
                    .show();
        }
    }

    public void lock() {
        shotsField.setEnabled(false);
        periodField.setEnabled(false);
        pause.setEnabled(false);
        startButton.setEnabled(false);
        startButton.setVisibility(View.INVISIBLE);
        stopButton.setEnabled(true);
        stopButton.setVisibility(View.VISIBLE);
        setStartButton.setEnabled(false);
        setEndButton.setEnabled(false);
        toStartButton.setEnabled(false);
        toEndButton.setEnabled(false);
        smoothButton.setEnabled(false);
        rightButton.setEnabled(false);
        leftButton.setEnabled(false);
        upButton.setEnabled(false);
        downButton.setEnabled(false);
    }

    public void unlock() {
        shotsField.setEnabled(true);
        periodField.setEnabled(true);
        pause.setEnabled(true);
        startButton.setEnabled(true);
        startButton.setVisibility(View.VISIBLE);
        stopButton.setEnabled(false);
        stopButton.setVisibility(View.INVISIBLE);
        setStartButton.setEnabled(true);
        setEndButton.setEnabled(true);
        toStartButton.setEnabled(true);
        toEndButton.setEnabled(true);
        smoothButton.setEnabled(true);
        rightButton.setEnabled(true);
        leftButton.setEnabled(true);
        upButton.setEnabled(true);
        downButton.setEnabled(true);
    }
}