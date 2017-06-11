package hestia.UI.dialogs;

import android.os.AsyncTask;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import com.rugged.application.hestia.R;

import java.io.IOException;
import java.util.ArrayList;

import hestia.UI.HestiaApplication;
import hestia.backend.ServerCollectionsInteractor;
import hestia.backend.exceptions.ComFaultException;
import hestia.backend.models.RequiredInfo;

/**
* This class opens the dialog to enter the collection name and plugin name.
* It then sends this to the networkHandler which tries to get the required info.
* If this works it consecutively opens a new dialog for the other info.
* @see EnterRequiredInfoDialog
 */

public class AddDeviceDialog extends HestiaDialog {
    private AutoCompleteTextView collectionField, pluginField;
    private ArrayAdapter<String> adapterCollections;
    private ArrayAdapter<String> adapterPlugins;
    private ServerCollectionsInteractor serverCollectionsInteractor;
    private final static String TAG = "AddDeviceDialog";
    private FragmentManager fragmentManager;

    public static AddDeviceDialog newInstance() {
        AddDeviceDialog fragment = new AddDeviceDialog();
        return fragment;
    }

    public void setInteractor(ServerCollectionsInteractor interactor) {
        serverCollectionsInteractor = interactor;
    }

    public void setFragmentManager(FragmentManager fragmentManager) {
        this.fragmentManager = fragmentManager;
    }

    @Override
    String buildTitle() {
        return getString(R.string.addDeviceTitle);
    }

    @Override
    View buildView() {
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        View view = inflater.inflate(R.layout.add_device_dialog, null);

        getCollections();

        buildCollectionsField(view);
        buildPluginField(view);

        return view;
    }

    @Override
    void pressCancel() {
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
                    String exceptionMessage = getString(R.string.serverNotFound);
                    publishProgress(exceptionMessage);
                } catch (ComFaultException comFaultException) {
                    Log.e(TAG, comFaultException.toString());
                    String error = comFaultException.getError();
                    String message = comFaultException.getMessage();
                    String exceptionMessage = error + ":" + message;
                    publishProgress(exceptionMessage);
                }
                return info;
            }

            @Override
            protected void onProgressUpdate(String... exceptionMessage) {
                Toast.makeText(getContext(), exceptionMessage[0], Toast.LENGTH_SHORT).show();
            }

            @Override
            protected void onPostExecute(RequiredInfo info) {
                if(info != null){
                    EnterRequiredInfoDialog fragment = EnterRequiredInfoDialog.newInstance();
                    fragment.setData(info, serverCollectionsInteractor);
                    if(fragmentManager == null){
                    }
                    fragment.show(fragmentManager, "dialog");
                }
            }
        }.execute();
    }

    private void buildPluginField(View view) {
        adapterPlugins = new ArrayAdapter<String>(getContext(), android.R.layout.simple_expandable_list_item_1);
        pluginField = (AutoCompleteTextView) view.findViewById(R.id.pluginName);
        pluginField.setAdapter(adapterPlugins);
        pluginField.setThreshold(1);

        pluginField.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                getPlugins(collectionField.getText().toString());
            }
        });
    }

    private void buildCollectionsField(View view){
        adapterCollections = new ArrayAdapter<String>(getContext(), android.R.layout.simple_expandable_list_item_1);
        collectionField = (AutoCompleteTextView) view.findViewById(R.id.collection);
        collectionField.setAdapter(adapterCollections);
        collectionField.setThreshold(1);
    }

    private void getCollections() {
        new AsyncTask<Object, String, ArrayList<String>>() {
            @Override
            protected ArrayList<String> doInBackground(Object... params) {
                ArrayList<String> collections = new ArrayList<String>();
                try {
                    collections = serverCollectionsInteractor.getCollections();
                } catch (IOException e) {
                    Log.e(TAG,e.toString());
                    String exceptionMessage = getString(R.string.serverNotFound);
                    publishProgress(exceptionMessage);
                } catch (ComFaultException comFaultException) {
                    Log.e(TAG, comFaultException.toString());
                    String error = comFaultException.getError();
                    String message = comFaultException.getMessage();
                    String exceptionMessage = error + ":" + message;
                    publishProgress(exceptionMessage);
                }
                return collections;
            }

            @Override
            protected void onProgressUpdate(String... exceptionMessage) {
                Toast.makeText(HestiaApplication.getContext(), exceptionMessage[0], Toast.LENGTH_SHORT).show();
            }

            @Override
            protected void onPostExecute(ArrayList<String> collections) {
                adapterCollections.clear();
                if (adapterCollections != null) {
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
                    String exceptionMessage = getString(R.string.serverNotFound);
                    publishProgress(exceptionMessage);
                } catch (ComFaultException comFaultException) {
                    Log.e(TAG, comFaultException.toString());
                    String error = comFaultException.getError();
                    String message = comFaultException.getMessage();
                    String exceptionMessage = error + ":" + message;
                    publishProgress(exceptionMessage);
                }
                return plugins;
            }

            @Override
            protected void onProgressUpdate(String... exceptionMessage) {
                Toast.makeText(HestiaApplication.getContext(), exceptionMessage[0], Toast.LENGTH_SHORT).show();
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

