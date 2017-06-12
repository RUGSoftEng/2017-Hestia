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

import com.rugged.application.hestia.R;

import java.io.IOException;
import java.util.ArrayList;

import hestia.UI.dialogs.AddDeviceDialog;
import hestia.UI.elements.DeviceBar;
import hestia.backend.ServerCollectionsInteractor;
import hestia.backend.exceptions.ComFaultException;
import hestia.backend.models.Device;

/**
 * This fragment takes care of generating the list of peripherals on the phone. It sends an HTTP
 * GET request to the server to populate the device list.
 */
public class DeviceListFragment extends Fragment {
    private ServerCollectionsInteractor serverCollectionsInteractor;
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

    /**
     * This method replaces the non-default constructor in Android. The default constructor should
     * always be used in Android fragments, so we need to pass any other arguments using this method
     *
     * @return The fragment which was constructed
     */
    public static DeviceListFragment newInstance() {
        DeviceListFragment fragment = new DeviceListFragment();
        return fragment;
    }

    public void setServerCollectionsInteractor(ServerCollectionsInteractor
                                                       serverCollectionsInteractor) {
        this.serverCollectionsInteractor = serverCollectionsInteractor;
    }

    /**
     * This method is called when the fragment view is created.
     *
     * @param inflater  The layout inflater used to generate the layout hierarchy
     * @param container The viewgroup with which the layout is instantiated
     * @return A view of an expandable list linked to the listDataHeader and listDataChild
     * variables. Filling these lists will generate the GUI.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        this.context = getContext();
        super.onCreateView(inflater, container, savedInstanceState);
        View deviceListView = inflater.inflate(R.layout.device_list_fragment, container, false);

        createFloatingButton(deviceListView);
        initRefreshLayout(deviceListView);
        initDeviceList(deviceListView);
        populateUI();

        return deviceListView;
    }

    /**
     * Connects to the server using the serverCollectionsInteractor. We run the method in the
     * background so
     */
    public void populateUI() {
        new AsyncTask<Object, String, ArrayList<Device>>() {
            @Override
            protected ArrayList<Device> doInBackground(Object... params) {
                ArrayList<Device> devices = new ArrayList<>();
                try {
                    devices = serverCollectionsInteractor.getDevices();
                } catch (IOException e) {
                    Log.e(TAG, e.toString());
                    String exceptionMessage = getString(R.string.serverNotFound);
                    publishProgress(exceptionMessage);
                } catch (ComFaultException comFaultException) {
                    Log.e(TAG, comFaultException.toString());
                    String error = comFaultException.getError();
                    String message = comFaultException.getMessage();
                    String exceptionMessage = error + ":" + message;
                    publishProgress(exceptionMessage);
                }
                return devices;
            }

            @Override
            protected void onProgressUpdate(String... exceptionMessage) {
                Toast.makeText(context, exceptionMessage[0], Toast.LENGTH_SHORT).show();
            }

            @Override
            protected void onPostExecute(ArrayList<Device> devices) {
                listDataChild = new ArrayList<>();
                for (Device device : devices) {
                    Log.i(TAG, "device found");
                    DeviceBar bar = new DeviceBar(getActivity().getSupportFragmentManager(),
                            getActivity(), device, serverCollectionsInteractor);
                    if (!listDataChild.contains(bar)) {
                        if (!typeExists(device)) {
                            listDataChild.add(new ArrayList<DeviceBar>());
                            listDataChild.get(listDataChild.size() - 1).add(bar);
                        } else {
                            listDataChild.get(getDeviceBarIndex(device)).add(bar);
                        }
                    }
                }
                listAdapter.setListData(listDataChild);
                expListView.setAdapter(listAdapter);
            }
        }.execute();
    }

    /**
     * This method checks whether a device of this type already exists. If it does, we can add the
     * device under this bar, if it does not, we need to make a new bar for this type.
     *
     * @param device The device we are checking against the existing device bars
     * @return A boolean indicating whether a device of this type is already in a devicebar
     */
    private boolean typeExists(Device device) {
        String deviceType = device.getType();
        for (ArrayList<DeviceBar> groupOfDevices : listDataChild) {
            Device checkDevice = groupOfDevices.get(0).getDevice();
            if (checkDevice.getType().equals(deviceType)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Returns the index for the device with the current type, if a bar for this type already exists
     * in the UI. If it does not, we return -1.
     *
     * @param device The device whose type we are checking
     * @return The index of the bar in the UI
     */
    private int getDeviceBarIndex(Device device) {
        String deviceType = device.getType();
        for (ArrayList<DeviceBar> groupOfDevices : listDataChild) {
            Device checkDevice = groupOfDevices.get(0).getDevice();
            if (checkDevice.getType().equals(deviceType)) {
                return listDataChild.indexOf(groupOfDevices);
            }
        }
        return -1;
    }

    private void setOnScrollListeners() {
        getExpListView().setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {
            }

            @Override
            public void onScroll(AbsListView absListView, int i, int i1, int i2) {
                int topRowVerticalPosition = (absListView == null ||
                        absListView.getChildCount() == 0) ? 0 :
                        absListView.getFirstVisiblePosition() == 0 ?
                                absListView.getChildAt(0).getTop() : -1;
                swipeRefreshLayout.setEnabled(topRowVerticalPosition >= 0);
            }
        });
    }

    /**
     * This method creates a new, empty listAdapter and adds it to the view it receives as a
     * parameter.
     *
     * @param deviceListView The view which is to be filled with a listAdapter
     */
    private void initDeviceList(View deviceListView) {
        listDataChild = new ArrayList<>();
        setExpListView((ExpandableListView) deviceListView.findViewById(R.id.lvExp));
        setListAdapter(new ExpandableDeviceList(listDataChild, surroundingActivity));
        getExpListView().setAdapter(getListAdapter());
        setOnScrollListeners();
    }

    /**
     * This method refreshes the deviceList, possibly resulting in new devices being added, by
     * calling the populateUI method.
     *
     * @param deviceListView
     */
    private void initRefreshLayout(View deviceListView) {
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

    private void createFloatingButton(View view) {
        floatingActionButton = (FloatingActionButton) view.findViewById(R.id.floating_action_button);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AddDeviceDialog fragment = AddDeviceDialog.newInstance();
                fragment.setInteractor(serverCollectionsInteractor);
                fragment.setFragmentManager(getActivity().getSupportFragmentManager());
                fragment.show(getActivity().getSupportFragmentManager(), "dialog");
            }
        });
    }

    public ExpandableDeviceList getListAdapter() {
        return listAdapter;
    }

    public void setListAdapter(ExpandableDeviceList listAdapter) {
        this.listAdapter = listAdapter;
    }

    public ExpandableListView getExpListView() {
        return expListView;
    }

    public void setExpListView(ExpandableListView expListView) {
        this.expListView = expListView;
    }
}
