package photos.vbstudio.login.fragments;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import photos.vbstudio.login.R;
import photos.vbstudio.login.fragments.DialogFragment.BaseDialogFragment;

import static photos.vbstudio.login.utils.UIUtils.animateBounceShow;

public class IntroPartAFragment extends BaseFragment {

    private final String TAG = this.getClass().getSimpleName();

    public static IntroPartAFragment newInstance() {
        IntroPartAFragment introPartAFragment = new IntroPartAFragment();

        return introPartAFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = View.inflate(getActivity(), R.layout.fragment_intro_part_a, null);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }
}