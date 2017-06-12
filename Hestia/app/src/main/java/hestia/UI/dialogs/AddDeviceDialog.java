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

import hestia.backend.ServerCollectionsInteractor;
import hestia.backend.exceptions.ComFaultException;
import hestia.backend.models.RequiredInfo;

/**
 * This class opens a dialog to enter the collection name and plugin name.
 * It then sends this information to the networkHandler which tries to obtain the required info from
 * the server. If this works it consecutively opens a new dialog so the user can enter the specific
 * information relevant to the device.
 *
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
        return new AddDeviceDialog();
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

    /**
     * When we press confirm on the dialog, this method is called. Using the information entered in
     * the fields, the client contacts the server to ask for the information the server requires to
     * create a new device. To get the correct required info from the server we need to ask specify
     * the device and the collection to look at.
     */
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
                return info;
            }

            @Override
            protected void onProgressUpdate(String... exceptionMessage) {
                Toast.makeText(getContext(), exceptionMessage[0], Toast.LENGTH_SHORT).show();
            }

            /**
             * After executing the AsyncTask and obtaining the result, the information will be
             * passed to the EnterRequiredInfo dialog, which will further handle the addition of a
             * new device.
             */
            @Override
            protected void onPostExecute(RequiredInfo info) {
                if (info != null) {
                    EnterRequiredInfoDialog fragment = EnterRequiredInfoDialog.newInstance();
                    fragment.setData(info, serverCollectionsInteractor);
                    if (fragmentManager == null) {
                        Log.d(TAG, "FragmentManager is null");
                        Toast.makeText(getContext(), getString(R.string.errorDataSend), Toast.LENGTH_SHORT).show();
                        return;
                    }
                    fragment.show(fragmentManager, "dialog");
                }
            }
        }.execute();
    }

    /**
     * Method for creating a text field for the plugin based on the plugins we retrieved from the
     * server.
     *
     * @param view The view in which the list of plugins will be shown.
     */
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

    /**
     * Method for creating the text field based on the collections we retrieved from the server.
     *
     * @param view The view in which we will create the collections field.
     */
    private void buildCollectionsField(View view) {
        adapterCollections = new ArrayAdapter<String>(getContext(), android.R.layout.simple_expandable_list_item_1);
        collectionField = (AutoCompleteTextView) view.findViewById(R.id.collection);
        collectionField.setAdapter(adapterCollections);
        collectionField.setThreshold(1);
    }

    /**
     * This method uses an AsyncTask to retrieve the list of collections as an ArrayList of strings
     * from the server.
     */
    private void getCollections() {
        new AsyncTask<Object, String, ArrayList<String>>() {
            @Override
            protected ArrayList<String> doInBackground(Object... params) {
                ArrayList<String> collections = new ArrayList<String>();
                try {
                    collections = serverCollectionsInteractor.getCollections();
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
                return collections;
            }

            @Override
            protected void onProgressUpdate(String... exceptionMessage) {
                if (getContext() != null) {
                    Toast.makeText(getContext(), exceptionMessage[0], Toast.LENGTH_SHORT).show();
                }
            }

            /**
             * After a connection to the server is established, we can set the collections we found
             * as the current collections.
             * @param collections An ArrayAdapter of strings containing the representations of the
             *                    collections
             */
            @Override
            protected void onPostExecute(ArrayList<String> collections) {
                adapterCollections.clear();
                if (adapterCollections != null) {
                    adapterCollections.addAll(collections);
                }
            }
        }.execute();
    }

    /**
     * This method retrieves, for a specific collection, the possible plugins from which devices can
     * be constructed from the server.
     *
     * @param collection The specific collection of which we want to know the possible plugins.
     */
    private void getPlugins(final String collection) {
        new AsyncTask<Object, String, ArrayList<String>>() {
            @Override
            protected ArrayList<String> doInBackground(Object... params) {
                ArrayList<String> plugins = null;
                try {
                    plugins = serverCollectionsInteractor.getPlugins(collection);
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
                return plugins;
            }

            @Override
            protected void onProgressUpdate(String... exceptionMessage) {
                if (getContext() != null) {
                    Toast.makeText(getContext(), exceptionMessage[0], Toast.LENGTH_SHORT).show();
                }
            }

            /**
             * After we have obtained the plugins from the server, we set our local plugins to them.
             * @param plugins An ArrayAdapter of strings containing the representations of the
             *                plugins
             */
            @Override
            protected void onPostExecute(ArrayList<String> plugins) {
                adapterPlugins.clear();
                if (plugins != null) {
                    adapterPlugins.addAll(plugins);
                }
            }
        }.execute();
    }
}

