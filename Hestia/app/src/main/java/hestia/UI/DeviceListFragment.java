package hestia.UI;

import android.app.Activity;
import android.content.Context;
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
import hestia.backend.Activator;
import hestia.backend.NetworkHandler;
import hestia.backend.Cache;
import hestia.backend.Device;
import hestia.backend.DevicesChangeListener;
import hestia.backend.DevicesEvent;

import com.rugged.application.hestia.R;
import java.util.ArrayList;

/**
 * This fragment takes care of generating the list of peripherals on the phone. It sends an HTTP
 * GET request to the server to populate the device list.
 *
 * @see DeviceListActivity
 */
public class DeviceListFragment extends Fragment implements DevicesChangeListener{
    private SwipeRefreshLayout swipeRefreshLayout;
    private ExpandableListAdapter listAdapter;
    private ExpandableListView expListView;
    private ArrayList<ArrayList<DeviceBar>> listDataChild;
    private NetworkHandler networkHandler =  NetworkHandler.getInstance();
    private Cache cache = Cache.getInstance();
    private FloatingActionButton floatingActionButton;
    private final static String TAG = "DeviceListFragment";
    private Activity surroundingActivity;

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

        swipeRefreshLayout = (SwipeRefreshLayout) deviceListView.findViewById(R.id.swipe_refresh);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(true);
                Log.i(TAG, "Currently refreshing");
                networkHandler.updateDevices();
                Log.i(TAG, "Refresh stopped");
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        listDataChild = new ArrayList<>();
        expListView = (ExpandableListView) deviceListView.findViewById(R.id.lvExp);
        listAdapter = new ExpandableListAdapter(listDataChild, surroundingActivity);

        expListView.setAdapter(listAdapter);
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

        cache.addDevicesChangeListener(this);
        populateUI();

        return deviceListView;
    }



    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        surroundingActivity = context instanceof Activity ? (Activity) context : null;
    }

    private void populateUI() {
        listDataChild = new ArrayList<>();
        ArrayList<Device> devices = cache.getDevices();
        for (Device device : devices) {
            Activator activator = device.getToggle();
            HestiaSwitch hestiaSwitch = new HestiaSwitch(device, activator, surroundingActivity);
            DeviceBar bar = new DeviceBar(device, hestiaSwitch);
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

    @Override
    public void changeEventReceived(DevicesEvent evt) {
        populateUI();
    }

    private boolean typeExists(Device device) {
        String deviceType = device.getType();
        for (int i = 0; i < listDataChild.size(); i++) {
            Device checkDevice = listDataChild.get(i).get(0).getDevice();
            if (checkDevice.getType().equals(deviceType)) {
                return true;
            }
        }
        return false;
    }

    private int getDeviceType(Device device) {
        String deviceType = device.getType();
        for (int i = 0; i < listDataChild.size(); i++) {
            Device checkDevice = listDataChild.get(i).get(0).getDevice();
            if (checkDevice.getType().equals(deviceType)) {
                return i;
            }
        }
        return -1;
    }

    private void createFloatingButton(View view) {
        floatingActionButton = (FloatingActionButton)view.findViewById(R.id.floating_action_button);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AddDeviceDialog(surroundingActivity).show();
            }
        });
    }
}
