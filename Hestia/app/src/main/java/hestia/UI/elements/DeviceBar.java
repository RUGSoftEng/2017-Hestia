package hestia.UI.elements;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.widget.PopupMenu;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import com.rugged.application.hestia.R;
import java.io.IOException;
import hestia.UI.dialogs.ChangeNameDialog;
import hestia.UI.dialogs.SlideDialog;
import hestia.backend.ComFaultException;
import hestia.backend.models.Activator;
import hestia.backend.Cache;
import hestia.backend.models.ActivatorState;
import hestia.backend.models.Device;

/**
 *  This class takes care of the deviceBar.
 * The devicebar is the 'row' in the expandable list of a single device.
 */

public class DeviceBar extends RelativeLayout {
    private Context context;
    private Device device;
    private Cache cache;
    private final static String TAG = "DeviceBar";

    public DeviceBar(Context context, Device device, Cache cache) {
        super(context);
        this.device = device;
        this.cache = cache;
        this.context = context;
        initView();
    }

    public void initView() {
        View view = inflate(getContext(), R.layout.child_list_item, null);
        addView(view);

        TextView txtListChild = (TextView) this.findViewById(R.id.child_item_text);
        txtListChild.setText(device.getName());

        ImageView imageview = (ImageView) this.findViewById(R.id.imageview);

        final Switch switc = (Switch)this.findViewById(R.id.light_switch);
        switc.setEnabled(false);
        switc.setVisibility(View.INVISIBLE);

        Activator rankOne;
        for(final Activator activator : device.getActivators()){
            if(activator.getRank() == 0){
                if(activator.getState().getType().equals("bool")){
                    switc.setEnabled(true);
                    switc.setVisibility(View.VISIBLE);
                    switc.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                            final ActivatorState<Boolean> state = activator.getState();
                            state.setRawState(switc.isChecked());
                            checked(state, activator);
                        }
                    });
                }
                break;
            }
        }

        if(deviceHasSlider()) {
            this.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new SlideDialog(getContext(), device).show();
                }
            });
        }

        imageview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popup = createPopupMenu(view);

                popup.show();
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.delete:
                                new AsyncTask<Object, Object, Void>() {
                                    @Override
                                    protected Void doInBackground(Object... params) {
                                        try {
                                            cache.removeDevice(device);
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        } catch (ComFaultException e) {
                                            e.printStackTrace();
                                        }
                                        return null;
                                    }
                                }.execute();

                                break;
                            case R.id.change_name:
                                new ChangeNameDialog(getContext(), device).show();
                                break;
                            default:
                                break;
                        }
                        return true;
                    }
                });
            }
        });
    }

    private PopupMenu createPopupMenu(View view){
        int x  = 1;
        PopupMenu popup = new PopupMenu(getContext(), view);
        popup.getMenuInflater().inflate(R.menu.popup, popup.getMenu());
        return popup;
   }

    private boolean deviceHasSlider(){
        for(Activator activator : device.getActivators()){
            if(activator.getState().getType().equals("float")){
                return true;
            }
        }
        return false;
    }

    public Device getDevice() {
        return device;
    }

    public void setDevice(Device device) {
        this.device = device;
    }

    @Override
    public boolean equals(Object object) {
        boolean equal = false;

        if (object != null && object instanceof DeviceBar) {
            if(this.device.getId() == ((DeviceBar) object).getDevice().getId()){
                equal = true;
            }
        }
        return equal;
    }

    private void checked(final ActivatorState<Boolean> state, final Activator activator) {

        new AsyncTask<Object, Object, Integer>() {
            @Override
            protected Integer  doInBackground(Object... params) {
                Log.d(TAG, "Changed the switch to " + state);

                try {
                    activator.setState(state);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (ComFaultException e) {
                    e.printStackTrace();
                }
                Log.i(TAG, "Sending a post to the server");

                return 0;
            }

            @Override
            protected void onPostExecute(Integer result) {
                // Update GUI
            }
        }.execute();
    }
}
