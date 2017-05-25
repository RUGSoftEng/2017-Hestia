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
import com.rugged.application.hestia.R;

import java.io.IOException;
import java.util.ArrayList;

import hestia.backend.ComFaultException;
import hestia.backend.models.Activator;
import hestia.backend.models.ActivatorState;
import hestia.backend.models.Device;

/**
 * This class handles the dialog which is opened if a Device has the 'slide' option.
 * It loads all the slider activators, and sends the new state onRelease.
 */

public class SlideDialog extends Dialog implements android.view.View.OnClickListener{
    private Device device;
    private Context context;

    public SlideDialog(Context context, Device device) {
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
                new AsyncTask<Object, Object, Void>() {
                    @Override
                    protected Void doInBackground(Object... params) {
                        try {
                            activator.setState(state);
                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (ComFaultException e) {
                            e.printStackTrace();
                        }
                        return null;
                    }
                }.execute();
            }
        });
        return bar;
    }

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