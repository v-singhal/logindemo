package photos.vbstudio.login.fragments.DialogFragment;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;

import photos.vbstudio.login.LoginDemoApp;

import photos.vbstudio.login.network.NetworkManager;

public class BaseDialogFragment extends DialogFragment {

    private NetworkManager networkManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NO_FRAME, android.R.style.Theme_Light_NoTitleBar);
        networkManager = NetworkManager.newInstance(getActivity(), LoginDemoApp.BASE_URL);
    }

    public NetworkManager getNetworkManager() {
        return networkManager;
    }

    public void setNetworkManager(NetworkManager networkManager) {
        this.networkManager = networkManager;
    }
}