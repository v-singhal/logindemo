package photos.vbstudio.login.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Build;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import photos.vbstudio.login.R;
import photos.vbstudio.login.LoginDemoApp;
import photos.vbstudio.login.network.NetworkManager;

import static photos.vbstudio.login.BaseActivity.resetCurrentDialogTag;
import static photos.vbstudio.login.utils.UIUtils.getColorRedId;
import static photos.vbstudio.login.utils.UIUtils.getDisplayMetrics;


public class BaseDialog extends Dialog implements View.OnClickListener {

    protected Context context;
    protected NetworkManager networkManager;
    protected int computedWidth = 0;

    public BaseDialog(Context context) {
        super(context);
        this.context = context;
        this.networkManager = NetworkManager.newInstance(context, LoginDemoApp.BASE_URL);
    }

    @Override
    public void show() {
        super.show();
    }

    @Override
    public void dismiss() {
        super.dismiss();

        resetCurrentDialogTag();
    }

    protected void setupView(String dialogTitle, int layoutId) {
        requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(layoutId);
        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.row_layout_dialog_header);

        ((TextView) findViewById(R.id.txtTitle)).setText(dialogTitle);


        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            int divierId = context.getResources().getIdentifier("android:id/titleDivider", null, null);
            View divider = findViewById(divierId);
            divider.setBackgroundColor(getColorRedId(context, R.color.white));
        }

        heightAdjust();
    }

    protected void setupView(int layoutId) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(layoutId);

        heightAdjust();
    }

    protected void heightAdjust() {

        int screenWidth = getDisplayMetrics(context).widthPixels;
        int widthMargin = 40;
        computedWidth = screenWidth - widthMargin;
        getWindow().setLayout(computedWidth, WindowManager.LayoutParams.WRAP_CONTENT);
    }

    @Override
    public void onClick(View v) {

    }
}
