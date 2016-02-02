package photos.vbstudio.login;

import android.app.Activity;
import android.app.Dialog;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;

import photos.vbstudio.login.fragments.BaseFragment;
import photos.vbstudio.login.network.ImageRequestManager;
import photos.vbstudio.login.network.NetworkManager;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

import static photos.vbstudio.login.utils.UIUtils.animateFadeHide;
import static photos.vbstudio.login.utils.UIUtils.animateFadeShow;
import static photos.vbstudio.login.utils.UIUtils.hideKeyboard;

public class BaseActivity extends AppCompatActivity {

    public static final String TAG = BaseActivity.class.getSimpleName();

    public static final int MAIN_ACTIVITY_CONTAINER_ID = R.id.main_frame;
    public static final int LOGIN_ACTIVITY_CONTAINER_ID = R.id.main_frame;

    protected Toolbar toolbar;
    private NetworkManager networkManager;
    private ImageRequestManager imageRequestManager;
    protected static Dialog currentDialog;
    private static String currentDialogTag = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.networkManager = NetworkManager.newInstance(this, LoginDemoApp.BASE_URL);
        this.imageRequestManager = ImageRequestManager.getInstance(this);

        hideKeyboard(this);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);


        ProgressBar appWideCircularProgressBar = (ProgressBar) findViewById(R.id.pageLoadingIndicator).findViewById(R.id.app_wide_circular_progress_bar);
        ProgressBar appWideEmbeddedCircularProgressBar = (ProgressBar) findViewById(R.id.embeddedFragmentLoadingIndicator).findViewById(R.id.app_wide_circular_progress_bar);

        //changeProgressBarDrawableInPreLollipop(this, appWideCircularProgressBar);
        //changeProgressBarDrawableInPreLollipop(this, appWideEmbeddedCircularProgressBar);
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "RESUMED");

        hideKeyboard(this);
    }

    @Override
    protected void onPause() {
        clearActivityReferenceInApplication();
        super.onPause();

        Log.d(TAG, "PAUSED");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        setToolbarTitle();

        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {

        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDestroy() {
        clearActivityReferenceInApplication();

        super.onDestroy();
    }

    @Override
    protected void onChildTitleChanged(Activity childActivity, CharSequence title) {
        super.onChildTitleChanged(childActivity, title);
    }

    @Override
    public void onEnterAnimationComplete() {
        super.onEnterAnimationComplete();
    }

    @Override
    public void onAttachFragment(Fragment fragment) {
        super.onAttachFragment(fragment);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);

        return;
    }

    public void configureToolbar(int toolbarId, int custom_layout) {
        toolbar = (Toolbar) findViewById(toolbarId);

        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        getSupportActionBar().setCustomView(custom_layout);
    }

    private void setToolbarTitle() {
        View customView = getSupportActionBar().getCustomView();

        TextView textView = (TextView) customView.findViewById(R.id.my_toolbar_title);
        BaseFragment currFragment = getBaseFragmentInContainer();
        if (currFragment != null) {
            animateFadeHide(this, textView);
            textView.setText(currFragment.getToolBarTitle());
            animateFadeShow(this, textView);
        }
    }

    public BaseFragment getBaseFragmentInContainer() {
        String activityName = this.getClass().getSimpleName();

        Log.i(TAG, activityName);

        return getFragmentFromCurrentActivity(this);
    }

    public static BaseFragment getFragmentFromCurrentActivity(Context context) {
        BaseFragment currentFragment;
        int containerId = getContainerIdForCurrentActivity(context);

        currentFragment = (BaseFragment) ((BaseActivity) context).getSupportFragmentManager().findFragmentById(containerId);

        return currentFragment;
    }

    public static int getContainerIdForCurrentActivity(Context context) {
        int containerId = BaseActivity.MAIN_ACTIVITY_CONTAINER_ID;

        if (context instanceof MainActivity) {
            containerId = BaseActivity.MAIN_ACTIVITY_CONTAINER_ID;
        }

        return containerId;
    }

    public ImageLoader getImageLoader() {
        return this.imageRequestManager.getImageLoader();
    }

    public static void openAnimatedDialog(Dialog dialog) {
        try {
            String dialogName = dialog.getClass().getSimpleName();
            if (!currentDialogTag.equals(dialogName)) {
                dialog.getWindow().getAttributes().windowAnimations = R.style.PauseDialogAnimation;
                dialog.show();
                currentDialog = dialog;
                currentDialogTag = dialog.getClass().getSimpleName();

                Log.i(TAG, "CURRENT DIALOG SHOWN: " + currentDialogTag);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void resetCurrentDialogTag() {

        currentDialog = null;
        currentDialogTag = "";
    }

    private void clearActivityReferenceInApplication() {
        Activity currActivity = ((LoginDemoApp) getApplicationContext()).getCurrentActivity();
        if (this.equals(currActivity)) {
            ((LoginDemoApp) getApplicationContext()).setCurrentActivity(null);
        }
    }

    public Toolbar getToolbar() {
        return toolbar;
    }

    public NetworkManager getNetworkManager() {
        return networkManager;
    }
}