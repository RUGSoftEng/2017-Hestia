package hestia.UI.activities.home;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import hestia.UI.activities.login.LoginActivity;
import hestia.UI.dialogs.ChangeCredentialsDialog;
import hestia.UI.dialogs.ChangeIpDialog;
import hestia.backend.AdditionalKeyStoresSSLSocketFactory;
import hestia.backend.ServerCollectionsInteractor;
import hestia.backend.NetworkHandler;

public  class HomeActivity extends AppCompatActivity implements OnMenuItemClickListener {
    private ContextMenuDialogFragment mMenuDialogFragment;
    private FragmentManager fragmentManager;
    private List<MenuObject> menuObjects;
    private ServerCollectionsInteractor serverCollectionsInteractor;

    private static final String HESTIA_IP = "HESTIA.IP";
    private static final String SERVER_IP = "IP_OF_SERVER";
    private final String changeIpText = "Set IP ";
    public static final String logoutText = "Logout ";
    public static final String changeCredentialsText = "Change user/pass";
    private final String extraName = "login";
    private final String logoutExtraValue = "logout";
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
        Fragment fragment = fragmentManager.findFragmentById(R.id.fragment_container);

        if (fragment == null) {
            fragment = new DeviceListFragment(this.getApplicationContext(), this.serverCollectionsInteractor);
            fragmentManager.beginTransaction().add(R.id.fragment_container, fragment).commit();
        }
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
        SharedPreferences prefs = getSharedPreferences(HESTIA_IP, Context.MODE_PRIVATE);
        prefs.edit().putString(SERVER_IP,serverCollectionsInteractor.getHandler().getIp()).apply();
    }

    private void setupCache() {
        SharedPreferences prefs = getSharedPreferences(HESTIA_IP, Context.MODE_PRIVATE);
        String ip = prefs.getString(SERVER_IP, getApplicationContext().getString(R.string.default_ip));
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
        MenuObject close = new MenuObject();
        close.setResource(R.drawable.ic_action);
        objects.add(close);

        MenuObject ip = new MenuObject(changeIpText);
        ip.setResource(R.mipmap.ic_router);
        objects.add(ip);

        MenuObject changeCredentials = new MenuObject(changeCredentialsText);
        changeCredentials.setResource(R.mipmap.ic_key);
        objects.add(changeCredentials);

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
        toIntent.putExtra(extraName, logoutExtraValue);
        startActivity(toIntent);
        finish();
    }

    private void showIpDialog() {
//        ChangeIpDialog d = new ChangeIpDialog(HomeActivity.this, serverCollectionsInteractor);
//        HestiaDialog2 alertdFragment = new HestiaDialog2();
        // Show Alert DialogFragment
//        ChangeIpDialog alertdFragment = new ChangeIpDialog();
        String ip = this.serverCollectionsInteractor.getHandler().getIp();
//        alertdFragment.show(getFragmentManager(), "Alert Dialog Fragment");
//        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ChangeIpDialog fragment = ChangeIpDialog.newInstance(ip);
        fragment.setInteractor(serverCollectionsInteractor);
        fragment.show(getSupportFragmentManager(), "dialog");
    }

    private void showChangeCredentialsDialog() {
//        ChangeCredentialsDialog changeCredentialsDialog = new ChangeCredentialsDialog(HomeActivity.this);
//        changeCredentialsDialog.show();
        ChangeCredentialsDialog fragment = ChangeCredentialsDialog.newInstance();
        fragment.show(getSupportFragmentManager(), "dialog");
    }

    private void registerCertificate() {
        final SchemeRegistry schemeRegistry = new SchemeRegistry();
        schemeRegistry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
        schemeRegistry.register(new Scheme("https", createAdditionalCertsSSLSocketFactory(), 8000));

    // and then however you create your connection manager, I use ThreadSafeClientConnManager
        final HttpParams params = new BasicHttpParams();
        final ThreadSafeClientConnManager cm = new ThreadSafeClientConnManager(params, schemeRegistry);

    }

    protected AdditionalKeyStoresSSLSocketFactory createAdditionalCertsSSLSocketFactory() {

        AdditionalKeyStoresSSLSocketFactory factory = null;
        try {
            final KeyStore ks = KeyStore.getInstance("BKS");

            // the bks file we generated above
            final InputStream in = getResources().openRawResource(R.raw.mystore);
            try {
                // don't forget to put the password used above in strings.xml/mystore_password
                ks.load(in, Resources.getSystem().getString(R.string.mystore_password).toCharArray());
            } finally {
                in.close();
            }

            factory = new AdditionalKeyStoresSSLSocketFactory(ks);

        } catch (IOException | CertificateException | NoSuchAlgorithmException | UnrecoverableKeyException | KeyStoreException | KeyManagementException e) {
            e.printStackTrace();
        }
        this.factory = factory;
        return factory;

    }

}
