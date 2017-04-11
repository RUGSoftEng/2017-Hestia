package hestia.UI;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.PopupMenu;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import hestia.UIWidgets.HestiaSeekbar;
import hestia.UIWidgets.HestiaSwitch;
import hestia.backend.Activator;
import hestia.backend.ActivatorState;
import hestia.backend.ClientInteractionController;
import com.rugged.application.hestia.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import hestia.backend.Device;

public class ExpandableListAdapter extends BaseExpandableListAdapter{
    // child data in format of header title, child title
    private ArrayList<ArrayList<DeviceBar>> listDataChild;
    private Context context;

    private ClientInteractionController c;
    private final static String TAG = "ExpandableList";

    public ExpandableListAdapter(ArrayList<ArrayList<DeviceBar>> listChildData, Context context) {
        Log.i(TAG, "Constructor");
        this.listDataChild = listChildData;
        this.context = context;
        this.c = ClientInteractionController.getInstance();
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return this.listDataChild.get(groupPosition).get(childPosition);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(final int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {
        final DeviceBar dBar = (DeviceBar) getChild(groupPosition, childPosition);

        Log.i(TAG, "Looking at device: " + dBar.getDevice().getName());

        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            //inflate depending on toggle or slider
            convertView = infalInflater.inflate(R.layout.child_list_item, null);
        }

        TextView txtListChild = (TextView) convertView.findViewById(R.id.child_item_text);
        txtListChild.setText(dBar.getDevice().getName());

        ImageView imageview = (ImageView) convertView.findViewById(R.id.imageview);

//        Switch s = dBar.getHestiaSwitch().getActivatorSwitch();
        Boolean state = Boolean.parseBoolean(dBar.getDevice().getActivator(0).getState().toString());
        dBar.setLayout(convertView, R.id.light_switch,state);

        imageview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popup = new PopupMenu(context, view);
                popup.getMenuInflater().inflate(R.menu.popup,
                        popup.getMenu());

                final Device d = ((DeviceBar) getChild(groupPosition, childPosition)).getDevice();
                if (d.getSliders()==null) {
                    popup.getMenu().findItem(R.id.sliders).setEnabled(false);
                    popup.getMenu().findItem(R.id.sliders).setVisible(false);
                }
                else{
                    popup.getMenu().findItem(R.id.sliders).setEnabled(true);
                    popup.getMenu().findItem(R.id.sliders).setVisible(true);
                }


                popup.show();
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.sliders:
                                Log.i(TAG,d+d.getSliders().toString());
                                new SlideDialog(context,d.getSliders(),d).show();
                                //Settings
                                break;
                            case R.id.delete:
                                c.deleteDevice((Device)((DeviceBar) getChild(groupPosition, childPosition)).getDevice());
                                break;
                            default:
                                break;
                        }
                        return true;
                    }
                });
            }
        });
        return convertView;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return this.listDataChild.get(groupPosition).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this.listDataChild.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return this.listDataChild.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        DeviceBar dBar = ((ArrayList<DeviceBar>) getGroup(groupPosition)).get(0);
        String headerTitle = dBar.getDevice().getType();
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            convertView = infalInflater.inflate(R.layout.list_group, null);
        }

        TextView lblListHeader = (TextView) convertView
                .findViewById(R.id.lblListHeader);
        lblListHeader.setTypeface(null, Typeface.BOLD);
        lblListHeader.setText(headerTitle);

        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    public void setListData(ArrayList<ArrayList<DeviceBar>> listDataChild){
        this.listDataChild = listDataChild;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}