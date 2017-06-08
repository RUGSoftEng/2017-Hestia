package com.rugged.application.hestia.UI;

import hestia.UI.HestiaApplication;

public class UiTest {
    public final String PACKAGE_NAME = "com.rugged.application.hestia";
    public final String HOME_ACTIVITY = "hestia.UI.activities.home.HomeActivity";
    public final String LOGIN_ACTIVITY = "hestia.UI.activities.login.LoginActivity";

    public String getStr(int id) {
        return HestiaApplication.getContext().getString(id);
    }
}
