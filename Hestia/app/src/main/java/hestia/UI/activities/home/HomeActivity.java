package hestia.UI.activities.home;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.rugged.application.hestia.R;
import com.yalantis.contextmenu.lib.ContextMenuDialogFragment;
import com.yalantis.contextmenu.lib.MenuObject;
import com.yalantis.contextmenu.lib.MenuParams;
import com.yalantis.contextmenu.lib.interfaces.OnMenuItemClickListener;

import java.util.ArrayList;
import java.util.List;

import hestia.UI.HestiaApplication;
import hestia.UI.activities.login.LoginActivity;
import hestia.UI.dialogs.ChangeCredentialsDialog;
import hestia.backend.NetworkHandler;
import hestia.backend.ServerCollectionsInteractor;

import static com.rugged.application.hestia.R.id.fragment_container;
import static com.rugged.application.hestia.R.id.swipe_refresh;

/**
 * This activity marks the main screen for the app. It contains the serverCollectionsInteractor for
 * talking to the server.
 */
public class HomeActivity extends AppCompatActivity implements OnMenuItemClickListener {
    private ContextMenuDialogFragment mMenuDialogFragment;
    private FragmentManager fragmentManager;
    private List<MenuObject> menuObjects;
    private ServerCollectionsInteractor serverCollectionsInteractor;
    private final int CHANGECREDENTIALS = 1;
    private final int LOGOUT = 2;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_activity);

        setupServerCollectionInteractor();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        fragmentManager = getSupportFragmentManager();
        DeviceListFragment fragment = DeviceListFragment.newInstance();
        fragment.setServerCollectionsInteractor(this.serverCollectionsInteractor);
        fragmentManager.beginTransaction().add(fragment_container, fragment).commit();
        menuObjects = getMenuObjects();

        initMenuFragment();

        mMenuDialogFragment.setItemClickListener(this);
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    protected void onStop() {
        storeIP();
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        storeIP();
        super.onDestroy();
    }

    /**
     * This method retrieves the IP as it is currently set in the networkHandler of the
     * serverCollectionsInteractor and adds it to the SharedPreferences. The SharedPreferences
     * object can be used as a local database on the Android platform.
     */
    private void storeIP() {
        SharedPreferences prefs = getSharedPreferences(getString(R.string.hestiaIp), Context.MODE_PRIVATE);
        prefs.edit().putString(getString(R.string.ipOfServer), serverCollectionsInteractor.getHandler().getIp()).apply();
    }

    /**
     * Creates a new networkHandler and serverCollectionsInteractor. These classes are used for
     * communicating with the server.
     */
    private void setupServerCollectionInteractor() {
        NetworkHandler handler = ((HestiaApplication) this.getApplication()).getNetworkHandler();
        this.serverCollectionsInteractor = new ServerCollectionsInteractor(handler);
    }

    /**
     * This method contacts the server to update the list of devices in the GUI if a change has
     * occurred.
     */
    public void refreshUserInterface(){
        DeviceListFragment fragment = (DeviceListFragment) fragmentManager.findFragmentById(fragment_container);
        if(fragment != null){
            fragment.populateUI();
        }
    }

    /**
     * This method creates the fragment for the Menu in the top right corner of the application.
     */
    private void initMenuFragment() {
        MenuParams menuParams = new MenuParams();
        menuParams.setActionBarSize((int) getResources().getDimension(R.dimen.tool_bar_height));
        menuParams.setMenuObjects(menuObjects);
        menuParams.setClosableOutside(true);
        mMenuDialogFragment = ContextMenuDialogFragment.newInstance(menuParams);
    }

    private List<MenuObject> getMenuObjects() {
        List<MenuObject> objects = new ArrayList<>();
        addMenuObjects(objects, R.drawable.ic_action, null);
        addMenuObjects(objects, R.mipmap.ic_key, getString(R.string.changeUserPass));
        addMenuObjects(objects, R.mipmap.ic_exit_to_app, getString(R.string.logout));
        return objects;
    }

    private void addMenuObjects(List<MenuObject> objectList, int id, String title) {
        MenuObject obj = new MenuObject(title);
        obj.setResource(id);
        objectList.add(obj);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.context_menu:
                if (fragmentManager.findFragmentByTag(ContextMenuDialogFragment.TAG) == null) {
                    mMenuDialogFragment.show(fragmentManager, "ContextMenuDialogFragment");
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onMenuItemClick(View clickedView, int position) {
        switch (position) {
            case CHANGECREDENTIALS:
                showChangeCredentialsDialog();
                break;
            case LOGOUT:
                gotoLoginActivity();
                break;
            default:
                break;
        }
    }

    public void gotoLoginActivity() {
        Intent toIntent = new Intent(HomeActivity.this, LoginActivity.class);
        toIntent.putExtra(getString(R.string.login), getString(R.string.logoutExtraValue));
        startActivity(toIntent);
        finish();
    }

    public void showChangeCredentialsDialog() {
        ChangeCredentialsDialog fragment = ChangeCredentialsDialog.newInstance();
        fragment.show(getSupportFragmentManager(), "dialog");
    }

    public void setServerCollectionsInteractor(ServerCollectionsInteractor serverCollectionsInteractor){
        this.serverCollectionsInteractor = serverCollectionsInteractor;
        DeviceListFragment fragment = (DeviceListFragment) fragmentManager.findFragmentById(fragment_container);
        if(fragment != null){
            fragment.setServerCollectionsInteractor(serverCollectionsInteractor);
        }
    }
}
