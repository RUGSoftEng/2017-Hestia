package hestia.UI.activities.home;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ExpandableListView;
import android.widget.Toast;

import hestia.UI.elements.DeviceBar;
import hestia.UI.dialogs.AddDeviceDialog;
import hestia.backend.Cache;
import hestia.backend.ComFaultException;
import hestia.backend.models.Device;

import com.rugged.application.hestia.R;

import java.io.IOException;
import java.util.ArrayList;

/**
 * This fragment takes care of generating the list of peripherals on the phone. It sends an HTTP
 * GET request to the server to populate the device list.
 */
public class DeviceListFragment extends Fragment{
    private Cache cache;
    private Context context;

    private SwipeRefreshLayout swipeRefreshLayout;
    private ExpandableDeviceList listAdapter;
    private ExpandableListView expListView;
    private ArrayList<ArrayList<DeviceBar>> listDataChild;
    private FloatingActionButton floatingActionButton;
    private final static String TAG = "DeviceListFragment";
    private Activity surroundingActivity;

    public DeviceListFragment() {
        super();
    }

    public DeviceListFragment(Context context, Cache cache) {
        super();
        this.context = context;
        this.cache = cache;
    }

    /**
     *
     * @param inflater The layout inflater used to generate the layout hierarchy
     * @param container The viewgroup with which the layout is instantiated
     * @return A view of an expandable list linked to the listDataHeader and listDataChild
     *         variables. Filling these lists will generate the GUI.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View deviceListView = inflater.inflate(R.layout.fragment_device_list, container, false);

        createFloatingButton(deviceListView);

        initRefreshLayou(deviceListView);

        initDeviceList(deviceListView);

        populateUI();

        return deviceListView;
    }
    
    private void populateUI() {
        new AsyncTask<Object, Object, ArrayList<Device> >() {
            @Override
            protected ArrayList<Device>  doInBackground(Object... params) {
                ArrayList<Device> devices = new ArrayList<>();
                try {
                    devices = cache.getDevices();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (ComFaultException e) {
                    e.printStackTrace();
                }
                return devices;
            }

            @Override
            protected void onPostExecute(ArrayList<Device> devices) {
                listDataChild = new ArrayList<>();
                for (Device device : devices) {
                    Log.i(TAG, "device found");
                    DeviceBar bar = new DeviceBar(getActivity(), device, cache);
                    if(!listDataChild.contains(bar)) {
                        if (!typeExists(device)) {
                            listDataChild.add(new ArrayList<DeviceBar>());
                            listDataChild.get(listDataChild.size() - 1).add(bar);
                        } else {
                            listDataChild.get(getDeviceType(device)).add(bar);
                        }
                    }
                }
                listAdapter.setListData(listDataChild);
                expListView.setAdapter(listAdapter);
            }
        }.execute();
    }

    private void setOnScrollListeners() {
        expListView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {


            }

            @Override
            public void onScroll(AbsListView absListView, int i, int i1, int i2) {
                int topRowVerticalPosition = (absListView == null ||
                        absListView.getChildCount() == 0) ? 0 :
                        absListView.getFirstVisiblePosition() == 0 ?
                                absListView.getChildAt(0).getTop() : - 1;
                swipeRefreshLayout.setEnabled(topRowVerticalPosition >= 0);
            }
        });
    }

    private void initDeviceList(View deviceListView) {
        listDataChild = new ArrayList<>();
        expListView = (ExpandableListView) deviceListView.findViewById(R.id.lvExp);
        listAdapter = new ExpandableDeviceList(listDataChild, surroundingActivity, this.cache);
        expListView.setAdapter(listAdapter);
        setOnScrollListeners();
    }

    private void initRefreshLayou(View deviceListView) {
        swipeRefreshLayout = (SwipeRefreshLayout) deviceListView.findViewById(R.id.swipe_refresh);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(true);
                Log.i(TAG, "Currently refreshing");
                populateUI();
                Log.i(TAG, "Refresh stopped");
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        surroundingActivity = context instanceof Activity ? (Activity) context : null;
    }

    private boolean typeExists(Device device) {
        String deviceType = device.getType();
        for(ArrayList<DeviceBar> groupOfDevices : listDataChild) {
            Device checkDevice = groupOfDevices.get(0).getDevice();
            if (checkDevice.getType().equals(deviceType)) {
                return true;
            }
        }
        return false;
    }

    private int getDeviceType(Device device) {
        String deviceType = device.getType();
        for(ArrayList<DeviceBar> groupOfDevices : listDataChild) {
            Device checkDevice = groupOfDevices.get(0).getDevice();
            if (checkDevice.getType().equals(deviceType)) {
                return listDataChild.indexOf(groupOfDevices);
            }
        }
        return -1;
    }

    private void createFloatingButton(View view) {
        floatingActionButton = (FloatingActionButton)view.findViewById(R.id.floating_action_button);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AddDeviceDialog(getActivity(), cache).show();
            }
        });
    }
}
