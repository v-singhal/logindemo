package photos.vbstudio.login.fragments;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.android.volley.toolbox.ImageLoader;

import photos.vbstudio.login.BaseActivity;
import photos.vbstudio.login.LoginDemoApp;
import photos.vbstudio.login.R;
import photos.vbstudio.login.network.ImageRequestManager;
import photos.vbstudio.login.network.NetworkManager;

import static photos.vbstudio.login.utils.UIUtils.hideEmbeddedLoadingIndicator;
import static photos.vbstudio.login.utils.UIUtils.hideKeyboard;
import static photos.vbstudio.login.utils.UIUtils.hidePageLoadingIndicator;
import static photos.vbstudio.login.utils.UIUtils.showToolbarShadow;

public class BaseFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener, View.OnClickListener {

    public final String TAG = this.getClass().getSimpleName();

    private NetworkManager networkManager;
    private ImageRequestManager imageRequestManager;

    private String toolBarTitle;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.networkManager = NetworkManager.newInstance(getActivity(), LoginDemoApp.BASE_URL);
        this.imageRequestManager = ImageRequestManager.getInstance(getActivity());

    }

    @Override
    public void onClick(View view) {
        hideKeyboard(getActivity());
        flipOnClickListener(view, this);
        return;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        getActivity().invalidateOptionsMenu();
        showToolbarShadow((BaseActivity) getActivity());
    }

    @Override
    public void onResume() {
        super.onResume();
        hideKeyboard(getActivity());
    }

    @Override
    public void onPause() {
        super.onPause();

        /*getNetworkManager().cancelFragmentRequests();
        hideEmbeddedLoadingIndicator(getActivity());
        hidePageLoadingIndicator(getActivity());*/
    }

    @Override
    public void onStop() {
        super.onStop();

        /*getNetworkManager().cancelFragmentRequests();
        hideEmbeddedLoadingIndicator(getActivity());
        hidePageLoadingIndicator(getActivity());*/
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        getNetworkManager().cancelFragmentRequests();
        hideEmbeddedLoadingIndicator(getActivity());
        hidePageLoadingIndicator(getActivity());
    }

    @Override
    public void onRefresh() {
    }

    private void flipOnClickListener(final View view, final BaseFragment baseFragment) {
        view.setOnClickListener(null);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                view.setOnClickListener(baseFragment);
            }
        }, 1000);
    }

    public void popBack(FragmentManager manager) {
        FragmentManager fragmentManager = manager;
        if (fragmentManager.getBackStackEntryCount() > 0) {
            fragmentManager.popBackStack(fragmentManager.getBackStackEntryAt(0).getId(), FragmentManager.POP_BACK_STACK_INCLUSIVE);
        }
    }

    public void popBack(Context context) {
        FragmentManager fragmentManager = ((AppCompatActivity) context).getSupportFragmentManager();
        if (fragmentManager.getBackStackEntryCount() > 0) {
            fragmentManager.popBackStack(fragmentManager.getBackStackEntryAt(0).getId(), FragmentManager.POP_BACK_STACK_INCLUSIVE);
        }
    }

    public void popBackRestart(FragmentManager manager) {
        FragmentManager fragmentManager = manager;
        if (fragmentManager.getBackStackEntryCount() > 0) {
            fragmentManager.beginTransaction().replace(fragmentManager.getBackStackEntryAt(0).getId(), this).commit();
        }
    }

    public static void addToBackStackFromNavigationDrawer(final Context context, final BaseFragment fragment) {
        BaseFragment currentFragment = BaseActivity.getFragmentFromCurrentActivity(context);

        if (!fragment.getClass().getSimpleName().equals(currentFragment.getClass().getSimpleName())) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    addToBackStack(context, fragment);
                }
            }, 200);
        }
    }

    protected void addToBackStack(BaseFragment fragment) {

        addToBackStack(getActivity(), fragment);
    }

    public static void addToBackStack(Context context, BaseFragment fragment) {
        FragmentManager fragmentManager = ((BaseActivity) context).getSupportFragmentManager();
        android.support.v4.app.FragmentTransaction transaction = fragmentManager.beginTransaction();
        //transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE);
        transaction.setCustomAnimations(R.anim.slide_left_in, R.anim.slide_left_out, R.anim.slide_right_out, R.anim.slide_right_in);
        transaction.replace(BaseActivity.getContainerIdForCurrentActivity(context), fragment);
        transaction.addToBackStack(null).commit();
    }

    public static void addToBackStackAllowingStateLoss(Context context, BaseFragment fragment) {
        FragmentManager fragmentManager = ((BaseActivity) context).getSupportFragmentManager();
        android.support.v4.app.FragmentTransaction transaction = fragmentManager.beginTransaction();
        //transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE);
        transaction.setCustomAnimations(R.anim.slide_left_in, R.anim.slide_left_out, R.anim.slide_right_out, R.anim.slide_right_in);
        transaction.replace(BaseActivity.getContainerIdForCurrentActivity(context), fragment);
        transaction.addToBackStack(null).commitAllowingStateLoss();
    }

    public void replaceFragmentInStack(Context context, BaseFragment newFragment) {
        FragmentManager fragmentManager = ((AppCompatActivity) context).getSupportFragmentManager();

        fragmentManager.popBackStack();
        addToBackStack(newFragment);
    }

    public static void replaceStack(Context context, BaseFragment fragment) {
        FragmentManager fragmentManager = ((AppCompatActivity) context).getSupportFragmentManager();
        if (fragmentManager.getBackStackEntryCount() > 0) {
            fragmentManager.popBackStack(fragmentManager.getBackStackEntryAt(0).getId(), FragmentManager.POP_BACK_STACK_INCLUSIVE);
        }

        android.support.v4.app.FragmentTransaction transaction = fragmentManager.beginTransaction();
        //transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE);
        transaction.setCustomAnimations(R.anim.slide_left_in, R.anim.slide_left_out, R.anim.slide_right_out, R.anim.slide_right_in);
        transaction.replace(BaseActivity.getContainerIdForCurrentActivity(context), fragment);
        transaction.commit();
    }

    public static void replaceStackAllowingStateLoss(Context context, BaseFragment fragment) {
        FragmentManager fragmentManager = ((AppCompatActivity) context).getSupportFragmentManager();
        if (fragmentManager.getBackStackEntryCount() > 0) {
            fragmentManager.popBackStack(fragmentManager.getBackStackEntryAt(0).getId(), FragmentManager.POP_BACK_STACK_INCLUSIVE);
        }

        android.support.v4.app.FragmentTransaction transaction = fragmentManager.beginTransaction();
        //transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE);
        transaction.setCustomAnimations(R.anim.slide_left_in, R.anim.slide_left_out, R.anim.slide_right_out, R.anim.slide_right_in);
        transaction.replace(BaseActivity.getContainerIdForCurrentActivity(context), fragment);
        transaction.commitAllowingStateLoss();
    }

    public NetworkManager getNetworkManager() {
        return networkManager;
    }

    public ImageLoader getImageLoader() {
        return this.imageRequestManager.getImageLoader();
    }

    public String getToolBarTitle() {
        return toolBarTitle;
    }

    public void setToolBarTitle(String toolBarTitle) {
        this.toolBarTitle = toolBarTitle;
    }
}