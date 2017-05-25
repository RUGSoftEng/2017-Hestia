package hestia.UI.activities.home;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;
import hestia.UI.elements.DeviceBar;
import hestia.backend.Cache;
import hestia.backend.NetworkHandler;
import com.rugged.application.hestia.R;
import java.util.ArrayList;

/**
 * The ExpandableDeviceList creates the expendable list. It receives an Arraylist with deviceBars
 * and adds them to the Expandable List. It also checks which options should be visible, and
 * adds listeners to these options in the popup menu.
 */

public class ExpandableDeviceList extends BaseExpandableListAdapter{
    private Cache cache;
    private ArrayList<ArrayList<DeviceBar>> listDataChild;
    private Context context;

    public ExpandableDeviceList(ArrayList<ArrayList<DeviceBar>> listChildData, Context context, Cache cache) {
        this.listDataChild = listChildData;
        this.context = context;
        this.cache = cache;
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
        // TODO move the slide diaog into the device class

        final DeviceBar dBar = (DeviceBar) getChild(groupPosition, childPosition);

        return dBar;
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
}