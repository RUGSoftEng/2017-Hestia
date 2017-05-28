package hestia.UI.dialogs;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;
import com.rugged.application.hestia.R;

import java.io.IOException;
import java.util.ArrayList;

import hestia.backend.ServerCollectionsInteractor;
import hestia.backend.exceptions.ServerExceptions.DeviceNotFoundException;
import hestia.backend.models.RequiredInfo;

/**
* This class opens the dialog to enter the collection name and plugin name.
* It then sends this to the networkHandler which tries to get the required info.
* If this works it consecutively opens a new dialog for the other info.
* @see AddDeviceInfo
 */

public class AddDeviceDialog extends HestiaDialog {
    private static final String TAG = "AddDeviceDialog";
    private AutoCompleteTextView collectionField, pluginField;
    private ArrayAdapter<String> adapterCollections;
    private ArrayAdapter<String> adapterPlugins;
    private ServerCollectionsInteractor serverCollectionsInteractor;

    public AddDeviceDialog(Context context, ServerCollectionsInteractor serverCollectionsInteractor) {
        super(context, R.layout.add_device_dialog, "Add a device");
        this.serverCollectionsInteractor = serverCollectionsInteractor;
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
    void pressConfirm() {

        final String collection = collectionField.getText().toString();
        final String pluginName = pluginField.getText().toString();

         new AsyncTask<Object, String, RequiredInfo>() {
            @Override
            protected RequiredInfo doInBackground(Object... params) {
                RequiredInfo info = null;
                try {
                    info = serverCollectionsInteractor.getRequiredInfo(collection, pluginName);
                } catch (IOException e) {
                  Log.e(TAG,e.toString());
                    String exceptionMessage = "Could not connect to the server";
                    publishProgress(exceptionMessage);
                    e.printStackTrace();
                } catch (DeviceNotFoundException e) {
                    e.printStackTrace();
                    String error = e.getError();
                    String message = e.getMessage();
                    Toast.makeText(context, error + ":" + message, Toast.LENGTH_SHORT).show();

                    
                }
                return info;
            }

             @Override
             protected void onProgressUpdate(String... exceptionMessage) {
                 Toast.makeText(context, exceptionMessage[0], Toast.LENGTH_SHORT).show();
             }

            @Override
            protected void onPostExecute(RequiredInfo info) {
                if(info != null){
                    new AddDeviceInfo(context, info, serverCollectionsInteractor).show();
                }
            }
        }.execute();
    }

    private void getCollections() {
        new AsyncTask<Object, String, ArrayList<String>>() {
            @Override
            protected ArrayList<String> doInBackground(Object... params) {
                ArrayList<String> collections = null;
                try {
                    collections = serverCollectionsInteractor.getCollections();
                } catch (IOException e) {
                   Log.e(TAG,e.toString());
                    String exceptionMessage = "Could not connect to the server";
                    publishProgress(exceptionMessage);
                    e.printStackTrace();
                } catch (DeviceNotFoundException e) {
                    Toast.makeText(context, e.getError() + ": " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    e.printStackTrace(); 
                } 
                return collections;
            }

            @Override
            protected void onProgressUpdate(String... exceptionMessage) {
                Toast.makeText(context, exceptionMessage[0], Toast.LENGTH_SHORT).show();
            }

            @Override
            protected void onPostExecute(ArrayList<String> collections) {
                adapterCollections.clear();
                if(collections != null){
                    adapterCollections.addAll(collections);
                }
            }
        }.execute();
    }

    private void getPlugins(final String collection) {
        new AsyncTask<Object, String, ArrayList<String>>() {
            @Override
            protected ArrayList<String> doInBackground(Object... params) {
                ArrayList<String> plugins = null;
                try {
                    plugins = serverCollectionsInteractor.getPlugins(collection);
                } catch (IOException e) {
                   Log.e(TAG,e.toString());
                    String exceptionMessage = "Could not connect to the server";
                    publishProgress(exceptionMessage);
                    e.printStackTrace();
                } catch (DeviceNotFoundException e) {
                    Toast.makeText(context, e.getError() + ": " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    e.printStackTrace();                   
                }
                return plugins;
            }

            @Override
            protected void onProgressUpdate(String... exceptionMessage) {
                Toast.makeText(context, exceptionMessage[0], Toast.LENGTH_SHORT).show();
            }

            @Override
            protected void onPostExecute(ArrayList<String> plugins) {
                adapterPlugins.clear();
                if(plugins != null) {
                    adapterPlugins.addAll(plugins);
                }
            }
        }.execute();
    }
}

