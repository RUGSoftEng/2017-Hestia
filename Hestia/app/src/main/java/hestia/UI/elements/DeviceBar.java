package hestia.UI.elements;

import android.app.Activity;
import android.os.AsyncTask;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.PopupMenu;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.rugged.application.hestia.R;

import java.io.IOException;

import hestia.UI.dialogs.ChangeNameDialog;
import hestia.UI.dialogs.SlidersDialog;
import hestia.backend.ServerCollectionsInteractor;
import hestia.backend.exceptions.ComFaultException;
import hestia.backend.models.Activator;
import hestia.backend.models.ActivatorState;
import hestia.backend.models.Device;

/**
 *  This class takes care of the deviceBar.
 * The DeviceBar is the 'row' in the expandable list of a single device.
 */

public class DeviceBar extends RelativeLayout {
    private Activity context;
    private Device device;
    private ServerCollectionsInteractor serverCollectionsInteractor;
    private final static String TAG = "DeviceBar";
    private FragmentManager fm;

    public DeviceBar(FragmentManager fm, Activity context, Device device, ServerCollectionsInteractor serverCollectionsInteractor) {
        super(context);
        this.device = device;
        this.serverCollectionsInteractor = serverCollectionsInteractor;
        this.context = context;
        this.fm = fm;
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

        for(final Activator activator : device.getActivators()){
            int toggleRank = Integer.valueOf(context.getResources().getString(R.string.toggleRank));
            if(activator.getRank() == toggleRank) {
                if(activator.getState().getType().equals("bool")){
                    switc.setEnabled(true);
                    switc.setVisibility(View.VISIBLE);
                    final ActivatorState<Boolean> state = activator.getState();
                    switc.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                            state.setRawState(switc.isChecked());
                            checked(state, activator);
                        }
                    });
                    switc.setChecked(state.getRawState());
                }
                break;
            }
        }

        if(deviceHasSlider()) {
            this.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    new SlidersDialog(getContext(), device).show();
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
                                doDeleteRequest();
                                break;
                            case R.id.change_name:
                                ChangeNameDialog fragment = ChangeNameDialog.newInstance();
                                fragment.setDevice(device);
                                fragment.show(fm, "dialog");
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

    private void checked(final ActivatorState<Boolean> state, final Activator activator) {
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

    public void doDeleteRequest() {
        new AsyncTask<Object, String, Boolean>() {
            @Override
            protected Boolean doInBackground(Object... params) {
                Boolean isSuccessful = false;
                try {
                    serverCollectionsInteractor.removeDevice(device);
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

    @Override
    public boolean equals(Object object) {
        if(!(object instanceof DeviceBar)) return false;
        DeviceBar deviceBar = (DeviceBar) object;
        return (this == deviceBar || (this.getDevice().equals(deviceBar.getDevice())));
    }

    @Override
    public int hashCode() {
        int result = getDevice().hashCode();
        return result;
    }
}
