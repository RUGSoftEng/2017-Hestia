package hestia.UI.dialogs;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;
import com.rugged.application.hestia.R;

import java.io.IOException;
import java.util.ArrayList;

import hestia.backend.Cache;
import hestia.backend.ComFaultException;
import hestia.backend.models.RequiredInfo;

/**
* This class opens the dialog to enter the collection name and plugin name.
* It then sends this to the networkHandler which tries to get the required info.
* If this works it consecutively opens a new dialog for the other info.
* @see AddDeviceInfo
 */

public class AddDeviceDialog extends HestiaDialog {
    private AutoCompleteTextView collectionField, pluginField;
    private ArrayAdapter<String> adapterCollections;
    private ArrayAdapter<String> adapterPlugins;
    private Cache cache;

    public AddDeviceDialog(Context context, Cache cache) {
        super(context, R.layout.add_device_dialog, "Add a device");
        this.cache = cache;
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        adapterCollections = new ArrayAdapter<String>(context, android.R.layout.simple_expandable_list_item_1);
        collectionField = (AutoCompleteTextView) findViewById(R.id.collection);
        collectionField.setAdapter(adapterCollections);
        collectionField.setThreshold(1);

        adapterPlugins = new ArrayAdapter<String>(context, android.R.layout.simple_expandable_list_item_1);
        pluginField = (AutoCompleteTextView)findViewById(R.id.pluginName);
        pluginField.setAdapter(adapterPlugins);
        pluginField.setThreshold(1);

        getCollections();

        pluginField.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                getPlugins(collectionField.getText().toString());
            }
        });
    }

    @Override
    void pressCancel() {
        Toast.makeText(context, "Cancel pressed", Toast.LENGTH_SHORT).show();
    }

    @Override
    void pressConfirm() {

        final String collection = collectionField.getText().toString();
        final String pluginName = pluginField.getText().toString();

         new AsyncTask<Object, Object, RequiredInfo>() {
            @Override
            protected RequiredInfo doInBackground(Object... params) {
                RequiredInfo info = null;
                try {
                    info = cache.getRequiredInfo(collection, pluginName);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return info;
            }

            @Override
            protected void onPostExecute(RequiredInfo info) {
                if(info != null){
                    new AddDeviceInfo(context, info, cache).show();
                }
            }
        }.execute();
    }


    private void getCollections() {
        new AsyncTask<Object, Object, ArrayList<String>>() {
            @Override
            protected ArrayList<String> doInBackground(Object... params) {
                ArrayList<String> list = null;
                try {
                    list = cache.getCollections();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (ComFaultException e) {
                    Toast.makeText(context, e.getError() + ": " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
                return list;
            }

            @Override
            protected void onPostExecute(ArrayList<String> collections) {
                adapterCollections.clear();
                adapterCollections.addAll(collections);
            }
        }.execute();
    }

    private void getPlugins(final String collection) {
        new AsyncTask<Object, Object, ArrayList<String>>() {
            @Override
            protected ArrayList<String> doInBackground(Object... params) {
                ArrayList<String> list = null;
                try {
                    list = cache.getPlugins(collection);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (ComFaultException e) {
                    Toast.makeText(context, e.getError() + ": " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
                return list;
            }

            @Override
            protected void onPostExecute(ArrayList<String> collections) {
                adapterPlugins.clear();
                adapterPlugins.addAll(collections);
            }
        }.execute();
    }
}

