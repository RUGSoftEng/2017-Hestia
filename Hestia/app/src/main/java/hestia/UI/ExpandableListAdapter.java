package hestia.UI;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.PopupMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import hestia.backend.NetworkHandler;
import com.rugged.application.hestia.R;
import java.util.ArrayList;
import hestia.backend.Device;

/**
 * The ExpandableListAdapter creates the expendable list. It receives an Arraylist with deviceBars
 * and adds them to the Expandable List. It also checks which options should be visible, and
 * adds listeners to these options in the popup menu.
 */

public class ExpandableListAdapter extends BaseExpandableListAdapter{
    private ArrayList<ArrayList<DeviceBar>> listDataChild;
    private Context context;
    private NetworkHandler networkHandler;

    public ExpandableListAdapter(ArrayList<ArrayList<DeviceBar>> listChildData, Context context) {
        this.listDataChild = listChildData;
        this.context = context;
        this.networkHandler = NetworkHandler.getInstance();
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

        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.child_list_item, null);
        }

        TextView txtListChild = (TextView) convertView.findViewById(R.id.child_item_text);
        txtListChild.setText(dBar.getDevice().getName());

        ImageView imageview = (ImageView) convertView.findViewById(R.id.imageview);

        Boolean state = Boolean.parseBoolean(dBar.getDevice().getToggle().getState().getRawState()
                .toString());
        dBar.setLayout(convertView, R.id.light_switch,state);
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Device device = ((DeviceBar) getChild(groupPosition, childPosition)).getDevice();
                if(!device.getSliders().isEmpty()) {
                    new SlideDialog(context, device).show();
                }
            }
        });

        imageview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Device device = ((DeviceBar) getChild(groupPosition, childPosition)).getDevice();
                PopupMenu popup = createPopupMenu(view);

                popup.show();
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.delete:
                                networkHandler.deleteDevice(device);
                                break;
                            case R.id.change_name:

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
            LayoutInflater layoutInflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            convertView = layoutInflater.inflate(R.layout.list_group, null);
        }

        TextView lblListHeader = (TextView) convertView .findViewById(R.id.lblListHeader);
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

    private PopupMenu createPopupMenu(View view){
        PopupMenu popup = new PopupMenu(context, view);
        popup.getMenuInflater().inflate(R.menu.popup, popup.getMenu());
        return popup;
    }
}