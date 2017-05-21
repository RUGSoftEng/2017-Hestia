
package hestia.UI;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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

import hestia.backend.Cache;

/**
 * This abstract class is used as an abstract wrapper around the device list activity class.
 * @see DeviceListActivity
 */
public abstract class SingleFragmentActivity extends AppCompatActivity implements
        OnMenuItemClickListener {
    private ContextMenuDialogFragment mMenuDialogFragment;
    private FragmentManager fragmentManager;
    private List<MenuObject> menuObjects;
    private Cache cache;
    private final String changeIpText = "Set IP ";
    private final String logoutText = "Logout ";
    private final String extraName = "login";
    private final String logoutExtraValue = "logout";
    private final int IP = 1;
    private final int LOGOUT = 2;

    protected abstract Fragment createFragment();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment);

        cache = Cache.getInstance();
        if(cache.getIp() == null) {
            showIpDialog();
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        fragmentManager = getSupportFragmentManager();
        Fragment fragment = fragmentManager.findFragmentById(R.id.fragment_container);

        if (fragment == null) {
            fragment = createFragment();
            fragmentManager.beginTransaction().add(R.id.fragment_container, fragment).commit();
        }
        menuObjects = getMenuObjects();
        initMenuFragment();

        mMenuDialogFragment.setItemClickListener(this);
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
        if (position == IP){
            showIpDialog();
        } else if(position == LOGOUT) {
            gotoLoginActivity();
        }
    }

    private void gotoLoginActivity() {
        Intent toIntent = new Intent(SingleFragmentActivity.this, LoginActivity.class);
        toIntent.putExtra(extraName, logoutExtraValue);
        startActivity(toIntent);
        finish();
    }

    private void showIpDialog() {
        IpDialog d = new IpDialog(SingleFragmentActivity.this);
        d.show();
    }
}
