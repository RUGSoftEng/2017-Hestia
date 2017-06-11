package hestia.UI.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import com.rugged.application.hestia.R;
import java.io.IOException;
import java.util.ArrayList;
import hestia.backend.exceptions.ComFaultException;
import hestia.backend.models.Activator;
import hestia.backend.models.ActivatorState;
import hestia.backend.models.Device;

/**
 * This class handles the dialog which is opened if a Device has the 'slide' option.
 * It loads all the slider activators, and sends the new state onRelease.
 */

public class SlidersDialog extends Dialog implements android.view.View.OnClickListener{
    private Device device;
    private Context context;
    private final String TAG = "SlidersDialog";

    public SlidersDialog(Context context, Device device) {
        super(context);
        this.context = context;
        this.device = device;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.slide_dialog);

        int count = 0;
        final LinearLayout mainLayout = (LinearLayout) findViewById(R.id.linearMain);

        for (Activator activator : getSliders()) {
            LinearLayout subLayout = new LinearLayout(context);

            TextView name = new TextView(context);
            name.setText(activator.getName());
            subLayout.addView(name);

            Float currState = Float.parseFloat(activator.getState().getRawState().toString());
            SeekBar bar = createSeekBar(currState ,count, activator);
            subLayout.addView(bar);

            mainLayout.addView(subLayout);
            count++;
        }
    }

    private SeekBar createSeekBar(float progress, int count, final Activator activator){
        final Activator act = activator;
        SeekBar bar = new SeekBar(context);
        final int maxInt = 100;
        bar.setMax(maxInt);
        bar.setProgress((int)(progress * maxInt));
        bar.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 80));
        bar.setId(count);
        bar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {}

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                float value = (float)seekBar.getProgress()/maxInt;
                final ActivatorState<Float> state = act.getState();
                state.setRawState(value);
                new AsyncTask<Object, String, Boolean>() {
                    @Override
                    protected Boolean doInBackground(Object... params) {
                        Boolean isSuccessful = false;
                        try {
                            activator.setState(state);
                            isSuccessful = true;
                        } catch (IOException e) {
                            Log.e(TAG,e.toString());
                            String exceptionMessage = context.getString(R.string.ioExceptionMessage);
                            publishProgress(exceptionMessage);
                        } catch (ComFaultException comFaultException) {
                            Log.e(TAG, comFaultException.toString());
                            String error = comFaultException.getError();
                            String message = comFaultException.getMessage();
                            String exceptionMessage = error + ":" + message;
                            publishProgress(exceptionMessage);
                        }
                        return isSuccessful;
                    }

                    @Override
                    protected void onProgressUpdate(String... exceptionMessage) {
                        Toast.makeText(context, exceptionMessage[0], Toast.LENGTH_SHORT).show();
                    }
                }.execute();
            }
        });
        return bar;
    }

    /**
     * This methods creates a list of sliders, i.e. a list of activators of type "float".
     * @return the list of sliders of the device.
     */
    private ArrayList<Activator> getSliders(){
        ArrayList<Activator> sliders =  new ArrayList<>();
        for(Activator activator : device.getActivators()){
            if(activator.getState().getType().equals("float")){
                sliders.add(activator);
            }
        }
        return sliders;
    }

    @Override
    public void onClick(View view) {
        dismiss();
    }
}