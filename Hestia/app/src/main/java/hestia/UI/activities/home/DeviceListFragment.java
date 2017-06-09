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
     * This method replaces the non-default constructor in Android. A bundle is constructed and
     * passed to the fragment. This ensures that we will get the same
     * @param serverCollectionsInteractor The interactor which has to be reinstantiated
     * @return The fragment which was constructed
     */

    public static DeviceListFragment newInstance(ServerCollectionsInteractor serverCollectionsInteractor) {
        DeviceListFragment fragment = new DeviceListFragment();
        Bundle bundle = new Bundle(1);
        bundle.putSerializable("serverCollectionsInteractor", serverCollectionsInteractor);
        fragment.setArguments(bundle);
        return fragment ;
    }

    /**
     * This method is called when the fragment is created.
     * @param savedInstanceState The bundle containing the serverCollectionsInteractor
     */
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        serverCollectionsInteractor = (ServerCollectionsInteractor) getArguments().getSerializable("serverCollectionsInteractor");
        context = getActivity().getApplicationContext();
    }

    /**
     * This method is called when the fragment view is created.
     * @param inflater The layout inflater used to generate the layout hierarchy
     * @param container The viewgroup with which the layout is instantiated
     * @return A view of an expandable list linked to the listDataHeader and listDataChild
     *         variables. Filling these lists will generate the GUI.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater,container,savedInstanceState);
        View deviceListView = inflater.inflate(R.layout.fragment_device_list, container, false);

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
    private void populateUI() {
        new AsyncTask<Object, String, ArrayList<Device> >() {
            @Override
            protected ArrayList<Device>  doInBackground(Object... params) {
                ArrayList<Device> devices = new ArrayList<>();
                try {
                    devices = serverCollectionsInteractor.getDevices();
                } catch (IOException e) {
                    Log.e(TAG, e.toString());
                    String exceptionMessage = "Could not connect to the server";
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
                    DeviceBar bar = new DeviceBar(getActivity().getSupportFragmentManager(), getActivity(), device, serverCollectionsInteractor);
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

    private void setOnScrollListeners() {
        getExpListView().setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {}

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
        setExpListView((ExpandableListView) deviceListView.findViewById(R.id.lvExp));
        setListAdapter(new ExpandableDeviceList(listDataChild, surroundingActivity, this.serverCollectionsInteractor));
        getExpListView().setAdapter(getListAdapter());
        setOnScrollListeners();
    }

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
        floatingActionButton = (FloatingActionButton)view.findViewById(R.id.floating_action_button);
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

    public ServerCollectionsInteractor getServerCollectionsInteractor() {
        return serverCollectionsInteractor;
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
