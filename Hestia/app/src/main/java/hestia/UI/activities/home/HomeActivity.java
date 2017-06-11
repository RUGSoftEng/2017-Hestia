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

import hestia.UI.activities.login.LoginActivity;
import hestia.UI.dialogs.ChangeCredentialsDialog;
import hestia.UI.dialogs.ChangeIpDialog;
import hestia.backend.NetworkHandler;
import hestia.backend.ServerCollectionsInteractor;

public  class HomeActivity extends AppCompatActivity implements OnMenuItemClickListener {
    private ContextMenuDialogFragment mMenuDialogFragment;
    private FragmentManager fragmentManager;
    private List<MenuObject> menuObjects;
    private ServerCollectionsInteractor serverCollectionsInteractor;

    private final int IP = 1;
    private final int CHANGECREDENTIALS = 2;
    private final int LOGOUT = 3;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment);

        setupCache();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        fragmentManager = getSupportFragmentManager();
        DeviceListFragment fragment = DeviceListFragment.newInstance();
        fragment.setServerCollectionsInteractor(this.serverCollectionsInteractor);
        fragmentManager.beginTransaction().add(R.id.fragment_container, fragment).commit();
        menuObjects = getMenuObjects();
        initMenuFragment();

        mMenuDialogFragment.setItemClickListener(this);
    }

    @Override
    public void onResume(){
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

    private void storeIP() {
        SharedPreferences prefs = getSharedPreferences(getString(R.string.hestiaIp), Context.MODE_PRIVATE);
        prefs.edit().putString(getString(R.string.ipOfServer)
                ,serverCollectionsInteractor.getHandler().getIp()).apply();
    }

    private void setupCache() {
        SharedPreferences prefs = getSharedPreferences(getString(R.string.hestiaIp), Context.MODE_PRIVATE);
        String ip = prefs.getString(getString(R.string.ipOfServer)
                , getApplicationContext().getString(R.string.default_ip));
        NetworkHandler handler = new NetworkHandler(ip, Integer.valueOf(
                getApplicationContext().getString(R.string.default_port)));
        this.serverCollectionsInteractor = new ServerCollectionsInteractor(handler);
    }

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
        addMenuObjects(objects, R.mipmap.ic_router, getString(R.string.setIp));
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
        switch(position) {
            case IP:
                showIpDialog();
                break;
            case CHANGECREDENTIALS:
                showChangeCredentialsDialog();
                break;
            case LOGOUT:
                gotoLoginActivity();
                break;
            default: break;
        }
    }

    private void gotoLoginActivity() {
        Intent toIntent = new Intent(HomeActivity.this, LoginActivity.class);
        toIntent.putExtra(getString(R.string.login), getString(R.string.logoutExtraValue));
        startActivity(toIntent);
        finish();
    }

    private void showIpDialog() {
        ChangeIpDialog fragment = ChangeIpDialog.newInstance();
        fragment.setInteractor(serverCollectionsInteractor);
        fragment.show(getSupportFragmentManager(), "dialog");
    }

    private void showChangeCredentialsDialog() {
        ChangeCredentialsDialog fragment = ChangeCredentialsDialog.newInstance();
        fragment.show(getSupportFragmentManager(), "dialog");
    }
}
