package hestia.UI.activities.home;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
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
import hestia.UI.dialogs.IpDialog;
import hestia.backend.Cache;
import hestia.backend.NetworkHandler;

public  class HomeActivity extends AppCompatActivity implements OnMenuItemClickListener {
    private ContextMenuDialogFragment mMenuDialogFragment;
    private FragmentManager fragmentManager;
    private List<MenuObject> menuObjects;
    private Cache cache;

    private static final String HESTIA_IP = "HESTIA.IP";
    private static final String SERVER_IP = "IP_OF_SERVER";
    private final String changeIpText = "Set IP ";
    private final String logoutText = "Logout ";
    private final String extraName = "login";
    private final String logoutExtraValue = "logout";
    private final int IP = 1;
    private final int LOGOUT = 2;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment);

        setupCache();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        fragmentManager = getSupportFragmentManager();
        Fragment fragment = fragmentManager.findFragmentById(R.id.fragment_container);

        if (fragment == null) {
            fragment = new DeviceListFragment(this.getApplicationContext(), this.cache);
            fragmentManager.beginTransaction().add(R.id.fragment_container, fragment).commit();
        }
        menuObjects = getMenuObjects();
        initMenuFragment();

        mMenuDialogFragment.setItemClickListener(this);
    }

    @Override
    public void onResume(){
        super.onResume();
        // TODO update the device list fragment
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
        SharedPreferences prefs = getSharedPreferences(HESTIA_IP, 0);
        cache.getHandler().setIp(prefs.getString(SERVER_IP, cache.getHandler().getIp()));
    }

    private void setupCache() {
        SharedPreferences prefs = getSharedPreferences(HESTIA_IP, 0);
        String ip = prefs.getString(SERVER_IP, "192.168.178.31");
        NetworkHandler handler = new NetworkHandler(ip, 8000);
        this.cache = new Cache(handler);
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
        MenuObject close = new MenuObject();
        close.setResource(R.drawable.ic_action);
        objects.add(close);

        MenuObject ip = new MenuObject(changeIpText);
        ip.setResource(R.mipmap.ic_router);
        objects.add(ip);

        MenuObject logout = new MenuObject(logoutText);
        logout.setResource(R.mipmap.ic_exit_to_app);
        objects.add(logout);

        return objects;
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
            case LOGOUT:
                gotoLoginActivity();
                break;
            default: break;
        }
    }

    private void gotoLoginActivity() {
        Intent toIntent = new Intent(HomeActivity.this, LoginActivity.class);
        toIntent.putExtra(extraName, logoutExtraValue);
        startActivity(toIntent);
        finish();
    }

    private void showIpDialog() {
        IpDialog d = new IpDialog(HomeActivity.this, cache);
        d.show();
    }
}
