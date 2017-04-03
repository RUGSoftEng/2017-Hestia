package hestia.UI;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import hestia.UIWidgets.HestiaSwitch;
import hestia.backend.Activator;
import hestia.backend.ActivatorState;
import hestia.backend.ClientInteractionController;
import hestia.backend.Device;
import hestia.backend.DevicesChangeListener;
import hestia.backend.DevicesEvent;

import com.rugged.application.hestia.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * This fragment takes care of generating the list of peripherals on the phone. It sends an HTTP
 * GET request to the server to populate the device list.
 *
 * @see DeviceListActivity
 */
public class DeviceListFragment extends Fragment implements DevicesChangeListener{

    private final static String TAG = "DeviceListFragment";

    private ExpandableListAdapter listAdapter;
    private ExpandableListView expListView;
    private ArrayList<ArrayList<DeviceBar>> listDataChild;
    private ClientInteractionController cic;
    private FloatingActionButton fab;

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
        View view = inflater.inflate(R.layout.fragment_peripheral_list, container, false);
        createFloatingButton(view);

        listDataChild = new ArrayList<>();
        expListView = (ExpandableListView) view.findViewById(R.id.lvExp);
        listAdapter = new ExpandableListAdapter(listDataChild, getActivity());

        expListView.setAdapter(listAdapter);

        ClientInteractionController.getInstance().addDevicesChangeListener(this);

        populateUI();

        return view;
    }

    private void populateUI() {
        listDataChild = new ArrayList<>();

        cic = ClientInteractionController.getInstance();

        ArrayList<Device> devices = cic.getDevices();
        for (Device d : devices) {
            Activator a = d.getActivator(0);
//            HestiaSwitch hestiaSwitch = new HestiaSwitch(d, a, getActivity());
            DeviceBar bar = new DeviceBar(d, a);
            if(!listDataChild.contains(bar)) {
                if (!typeExists(d)) {
                    listDataChild.add(new ArrayList<DeviceBar>());
                    listDataChild.get(listDataChild.size() - 1).add(bar);
                } else {
                    listDataChild.get(getDeviceType(d)).add(bar);
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

    private boolean typeExists(Device d) {
        String deviceType = d.getType();
        for (int i = 0; i < listDataChild.size(); i++) {
            Device checkDevice = listDataChild.get(i).get(0).getDevice();
            if (checkDevice.getType().equals(deviceType)) {
                return true;
            }
        }
        return false;
    }

    private int getDeviceType(Device d) {
        String deviceType = d.getType();
        for (int i = 0; i < listDataChild.size(); i++) {
            Device checkDevice = listDataChild.get(i).get(0).getDevice();
            if (checkDevice.getType().equals(deviceType)) {
                return i;
            }
        }
        return -1;
    }

    private void createFloatingButton(View v) {
        fab = (FloatingActionButton)v.findViewById(R.id.floating_action_button);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // add device dialog
                //new AddBridgeDialog(getActivity()).show();
                new AddDeviceDialog(getActivity()).show();
                populateUI();
            }
        });
    }



}
