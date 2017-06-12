package hestia.UI.activities.home;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.rugged.application.hestia.R;

import java.util.ArrayList;

import hestia.UI.elements.DeviceBar;

/**
 * The ExpandableDeviceList represent the expandable list. It receives an Arraylist with deviceBars
 * and adds compiles them to an Expandable List. It also checks which options should be visible, and
 * adds listeners to these options in the popup menu.
 */
public class ExpandableDeviceList extends BaseExpandableListAdapter {
    private ArrayList<ArrayList<DeviceBar>> listDataChild;
    private Context context;

    public ExpandableDeviceList(ArrayList<ArrayList<DeviceBar>> listChildData, Context context) {
        this.listDataChild = listChildData;
        this.context = context;
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return this.listDataChild.get(groupPosition).get(childPosition);
    }

    /**
     * Simply returns the position of the child we are looking for
     *
     * @param groupPosition Enforced by Android, we don' t use this here
     * @param childPosition We simply return this parameter
     * @return The value of childPosition
     */
    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    /**
     * Returns the deviceBar, as a view, of a particular group and childposition.
     *
     * @param groupPosition The position of the group in the list of deviceBars
     * @param childPosition The position of the child in the group
     * @param isLastChild   A boolean indicating whether the indicated child is the last one
     * @param convertView   The view which could be reused, this comes from Android and we don't use
     *                      it here
     * @param parent        The parent view, which we also don't need to get our DeviceBar
     * @return Returns the deviceBar indicated
     */
    @Override
    public View getChildView(final int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {
        final DeviceBar dBar = (DeviceBar) getChild(groupPosition, childPosition);
        return dBar;
    }

    /**
     * Returns the number of children in a particular group.
     *
     * @param groupPosition The group in which we are interested
     * @return The number of children in the group
     */
    @Override
    public int getChildrenCount(int groupPosition) {
        return this.listDataChild.get(groupPosition).size();
    }

    /**
     * Returns the data associated with the specified group, in this case the ArrayList of type
     * DeviceBar associated with the groupPosition.
     *
     * @param groupPosition The index of the ArrayList of DeviceBars we want
     * @return The DeviceBar list as an Object
     */
    @Override
    public Object getGroup(int groupPosition) {
        return this.listDataChild.get(groupPosition);
    }

    /**
     * @return The amount of groups of lists of DeviceBars
     */
    @Override
    public int getGroupCount() {
        return this.listDataChild.size();
    }

    /**
     * @param groupPosition The index of the group, which in our implementation is also the GroupId
     * @return The parameter groupPosition
     */
    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    /**
     * This method returns a specific groupView based on its index.
     *
     * @param groupPosition The index of the List of DeviceBar in the List of Lists
     * @param isExpanded    A boolean indicating whether the list we are referring to is expanded
     * @param convertView   The view which can be reused, we return it after modifying the header
     * @param parent        The parent to which this view will be attached
     * @return The specific groupView, consisting of a main bar and some devices under it
     */
    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        DeviceBar dBar = ((ArrayList<DeviceBar>) getGroup(groupPosition)).get(0);
        String headerTitle = dBar.getDevice().getType();
        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            convertView = layoutInflater.inflate(R.layout.expandable_device_list, null);
        }

        TextView lblListHeader = (TextView) convertView.findViewById(R.id.lblListHeader);
        lblListHeader.setTypeface(null, Typeface.BOLD);
        lblListHeader.setText(headerTitle);

        return convertView;
    }

    /**
     * @return A boolean indicating whether our Id's are stable. They aren't, but this is not a
     * problem for our own implementation
     */
    @Override
    public boolean hasStableIds() {
        return false;
    }

    /**
     * A simple setter for the listData
     *
     * @param listDataChild The listData which we will use to replace the current data
     */
    public void setListData(ArrayList<ArrayList<DeviceBar>> listDataChild) {
        this.listDataChild = listDataChild;
    }

    /**
     * Shows us whether the child at the specified position is selectable. In our code, this is
     * always the case.
     *
     * @param groupPosition The group index
     * @param childPosition The child index within the specified group
     * @return Always returns true, we never make children unselectable
     */
    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}