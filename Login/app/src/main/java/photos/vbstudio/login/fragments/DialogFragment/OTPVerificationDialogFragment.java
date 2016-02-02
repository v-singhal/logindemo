package photos.vbstudio.login.fragments.DialogFragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import photos.vbstudio.login.MainActivity;
import photos.vbstudio.login.R;
import photos.vbstudio.login.network.NetworkManager;
import photos.vbstudio.login.preferences.LoginDemoSharedPreferences;
import photos.vbstudio.login.receivers.SmsReceiver;
import photos.vbstudio.login.utils.UIUtils;

import static photos.vbstudio.login.network.NetworkManager.handleErrorResponse;
import static photos.vbstudio.login.network.NetworkManager.handleSuccessResponse;
import static photos.vbstudio.login.utils.StringUtils.isValidMobileString;
import static photos.vbstudio.login.utils.StringUtils.isValidString;

public class OTPVerificationDialogFragment extends BaseDialogFragment {

    private final String TAG = this.getClass().getSimpleName();

    private String sessionId;
    private String phoneNumber;
    private String countryName;
    private String countryCode;

    public static OTPVerificationDialogFragment newInstance(String sessionId, String phoneNumber, String countryName, String countryCode) {
        OTPVerificationDialogFragment otpVerificationDialogFragment = new OTPVerificationDialogFragment();

        otpVerificationDialogFragment.sessionId = sessionId;
        otpVerificationDialogFragment.phoneNumber = phoneNumber;
        otpVerificationDialogFragment.countryName = countryName;
        otpVerificationDialogFragment.countryCode = countryCode;

        return otpVerificationDialogFragment;
    }

    @Override
    public void show(FragmentManager manager, String tag) {
        super.show(manager, tag);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = View.inflate(getActivity(), R.layout.fragment_otp_verification, null);

        setupView(view);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        registerBroadcastReceivers();
    }

    @Override
    public void onPause() {
        super.onPause();

        unregisterBroadcastReceivers();
    }

    private void setupView(final View view) {

        //we can change the color of "buttons" to indicate disabled buttons
        //view.setVisibility(View.GONE);
        UIUtils.animateFadeShow(getActivity(), view);


        view.findViewById(R.id.resend_otp_layout).setOnClickListener(null);
        view.findViewById(R.id.btn_call_me).setOnClickListener(null);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                view.findViewById(R.id.resend_otp_layout).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startLoginProcess();
                    }
                });

                view.findViewById(R.id.btn_call_me).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //MAKE CALL FOR VERIFICATION
                    }
                });
            }
        }, 50 * 1000);
    }

    private void startLoginProcess() {

        if (!isValidMobileString(phoneNumber)) {
            Toast.makeText(getActivity(), "Please enter a valid phone number", Toast.LENGTH_SHORT).show();
            return;
        }

        UIUtils.showPageLoadingIndicator(getActivity());

        String url = "generate_otp.json";

        Map<String, String> getLoginOTP = new HashMap<>();

        getLoginOTP.put("mobile_no", phoneNumber);
        getLoginOTP.put("country_code", countryCode);
        getLoginOTP.put("country_name", countryName);

        getNetworkManager().jsonPOSTFormDataRequest(getActivity(), this,
                url, getLoginOTP,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            handleSuccessResponse(getActivity(), jsonResponse);

                            int responseMode = jsonResponse.optInt("status");

                            if (responseMode == 1) {

                                sessionId = jsonResponse.optJSONObject("data").optString("session_id");
                                Toast.makeText(getActivity(), "Please wait while you receive the new OTP", Toast.LENGTH_LONG).show();
                                setupView(getView());
                            } else {
                                Toast.makeText(getActivity(), "There was an error processing your request", Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        handleErrorResponse(getActivity(), error);


                    }
                }, NetworkManager.getDefaultPolicyForTransactions(), false);
    }

    public void verifyOTP(final String otp, final String sessionId) {

        if (!isValidString(otp)) {
            Toast.makeText(getActivity(), "Invalid OTP", Toast.LENGTH_SHORT).show();
            return;
        }

        unregisterBroadcastReceivers();

        ((EditText) getView().findViewById(R.id.edit_text_otp_a)).setText(String.valueOf(otp.charAt(0)));
        ((EditText) getView().findViewById(R.id.edit_text_otp_b)).setText(String.valueOf(otp.charAt(1)));
        ((EditText) getView().findViewById(R.id.edit_text_otp_c)).setText(String.valueOf(otp.charAt(2)));
        ((EditText) getView().findViewById(R.id.edit_text_otp_d)).setText(String.valueOf(otp.charAt(3)));

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                UIUtils.animateFadeShow(getActivity(), getView().findViewById(R.id.verification_for_otp_progress_layout));
                String url = "login_app.json";

                Map<String, String> loginParams = new HashMap<>();

                loginParams.put("mobile_no", phoneNumber);
                loginParams.put("country_code", countryCode);
                loginParams.put("otp", otp);
                loginParams.put("session_id", sessionId);

                getNetworkManager().jsonPOSTFormDataRequest(getActivity(), this,
                        url, loginParams,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                UIUtils.animateFadeHide(getActivity(), getView().findViewById(R.id.verification_for_otp_progress_layout));
                                try {
                                    JSONObject jsonResponse = new JSONObject(response);
                                    handleSuccessResponse(getActivity(), jsonResponse);

                                    int responseMode = jsonResponse.optInt("status");

                                    if (responseMode == 1) {

                                        Toast.makeText(getActivity(), "Successfully logged in", Toast.LENGTH_LONG).show();
                                        LoginDemoSharedPreferences.setLastLocationEventMode(getActivity(), true);

                                        new Handler().postDelayed(new Runnable() {
                                            @Override
                                            public void run() {
                                                dismiss();
                                                getActivity().startActivity(new Intent(getActivity(), MainActivity.class));
                                                getActivity().finish();
                                            }
                                        }, 2000);
                                    } else {
                                        Toast.makeText(getActivity(), "There was an error processing your request", Toast.LENGTH_LONG).show();
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                UIUtils.animateFadeHide(getActivity(), getView().findViewById(R.id.verification_for_otp_progress_layout));
                                handleErrorResponse(getActivity(), error);

                                registerBroadcastReceivers();
                            }
                        }, NetworkManager.getDefaultPolicyForTransactions(), false);
            }
        }, 2000);

    }

    private void registerBroadcastReceivers() {

        IntentFilter optReceivedIntentFilter = new IntentFilter(SmsReceiver.OTP_BROADCAST);
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(mSmsReceiver, optReceivedIntentFilter);
    }

    private void unregisterBroadcastReceivers() {
        try {
            LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(mSmsReceiver);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private BroadcastReceiver mSmsReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String otp = intent.getExtras().getString("otp");
            verifyOTP(otp, sessionId);
        }
    };
}