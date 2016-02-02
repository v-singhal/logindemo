package photos.vbstudio.login.fragments;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.AppCompatEditText;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import photos.vbstudio.login.R;
import photos.vbstudio.login.adpaters.IntroPagerAdapter;
import photos.vbstudio.login.fragments.DialogFragment.OTPVerificationDialogFragment;
import photos.vbstudio.login.network.NetworkManager;
import photos.vbstudio.login.pojo.EmbeddedFragmentData;
import photos.vbstudio.login.utils.UIUtils;

import static photos.vbstudio.login.network.NetworkManager.handleErrorResponse;
import static photos.vbstudio.login.network.NetworkManager.handleSuccessResponse;
import static photos.vbstudio.login.utils.StringUtils.getTextFromStringResource;
import static photos.vbstudio.login.utils.StringUtils.isValidMobileString;
import static photos.vbstudio.login.utils.StringUtils.isValidString;


public class LoginFragment extends BaseFragment implements AdapterView.OnItemSelectedListener, ViewPager.OnPageChangeListener {

    private static final String TAG = LoginFragment.class.getSimpleName();

    private List<String> countryNameList = new ArrayList<>();
    private HashMap<String, String> countryISDCodeHashMap = new HashMap<>();
    private HashMap<String, String> countryCodeHashMap = new HashMap<>();
    private List<RadioButton> currentPageIndicator = new ArrayList<>();

    public static LoginFragment newInstance() {
        LoginFragment loginFragment = new LoginFragment();

        return loginFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_login, container, false);
        setupView(rootView);

        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);

        int id = view.getId();
        if (id == R.id.btn_login) {

            startLoginProcess();
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        Spinner spinner = (Spinner) parent;
        int spinnerId = spinner.getId();
        if (spinnerId == R.id.spinner_country_selection) {

            ((EditText) getView().findViewById(R.id.edit_text_country_isd_code)).setText(countryISDCodeHashMap.get(countryNameList.get(position)));
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    @Override
    public void onPageSelected(int position) {

        currentPageIndicator.get(position).setChecked(true);
    }

    @Override
    public void onPageScrollStateChanged(int state) {
    }

    private void setupView(View rootView) {
        Button btnLogin = (Button) rootView.findViewById(R.id.btn_login);
        setupCountryData();

        List<EmbeddedFragmentData> embeddedFragmentDataList = new ArrayList<>();
        EmbeddedFragmentData introPartAData = new EmbeddedFragmentData();
        EmbeddedFragmentData introPartBData = new EmbeddedFragmentData();
        EmbeddedFragmentData introPartCData = new EmbeddedFragmentData();
        EmbeddedFragmentData introPartDData = new EmbeddedFragmentData();

        introPartAData.setBaseFragment(IntroPartAFragment.newInstance());
        introPartAData.setFragmentTitle(getTextFromStringResource(getActivity(), R.string.app_name));

        introPartBData.setBaseFragment(IntroPartAFragment.newInstance());
        introPartBData.setFragmentTitle(getTextFromStringResource(getActivity(), R.string.app_name));

        introPartCData.setBaseFragment(IntroPartAFragment.newInstance());
        introPartCData.setFragmentTitle(getTextFromStringResource(getActivity(), R.string.app_name));

        introPartDData.setBaseFragment(IntroPartAFragment.newInstance());
        introPartDData.setFragmentTitle(getTextFromStringResource(getActivity(), R.string.app_name));

        embeddedFragmentDataList.add(introPartAData);
        embeddedFragmentDataList.add(introPartBData);
        embeddedFragmentDataList.add(introPartCData);
        embeddedFragmentDataList.add(introPartDData);

        setupPageIndicator(rootView, embeddedFragmentDataList.size());

        // Get the ViewPager and set it's IntroPagerAdapter so that it can display items
        ViewPager viewPager = (ViewPager) rootView.findViewById(R.id.view_pager_intro);
        viewPager.setAdapter(new IntroPagerAdapter(getChildFragmentManager(), getActivity(), embeddedFragmentDataList));
        viewPager.addOnPageChangeListener(this);

        Spinner spinnerCountrySelection = (Spinner) rootView.findViewById(R.id.spinner_country_selection);
        ArrayAdapter<String> countrySelectionDataAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, countryNameList);
        countrySelectionDataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCountrySelection.setAdapter(countrySelectionDataAdapter);
        spinnerCountrySelection.setOnItemSelectedListener(this);

        spinnerCountrySelection.setSelection(countryNameList.indexOf(getResources().getConfiguration().locale.getDisplayCountry()));

        btnLogin.setOnClickListener(this);
    }

    private void setupPageIndicator(View rootView, int size) {
        RadioGroup radioGroupCurrentPageIndicator = (RadioGroup) rootView.findViewById(R.id.radio_group_current_page_indicator);

        radioGroupCurrentPageIndicator.removeAllViews();
        for (int counter = 0; counter < size; counter++) {

            RadioButton radioButton = new RadioButton(getActivity());

            radioButton.setClickable(false);
            radioButton.setPadding(5, 5, 5, 5);
            radioGroupCurrentPageIndicator.addView(radioButton);
            currentPageIndicator.add(radioButton);
        }

        currentPageIndicator.get(0).setChecked(true);
    }

    private void setupCountryData() {
        Locale[] locales = Locale.getAvailableLocales();
        for (Locale locale : locales) {
            String country = locale.getDisplayCountry();
            if (isValidString(country)) {
                String countryCode = locale.getCountry();
                String countryISDCode = getCountryISDCode(countryCode);
                if (isValidString(countryISDCode) && !countryNameList.contains(country)) {
                    countryCodeHashMap.put(country, countryCode);
                    countryISDCodeHashMap.put(country, countryISDCode);

                    countryNameList.add(country);
                }
            }
        }
        Collections.sort(countryNameList);
    }

    private String getCountryISDCode(String countryID) {

        String[] countryCodeArray = this.getResources().getStringArray(R.array.array_country_code);
        for (int i = 0; i < countryCodeArray.length; i++) {
            String[] countryCodeString = countryCodeArray[i].split(",");
            if (countryCodeString[1].trim().equals(countryID.trim())) {
                return countryCodeString[0];
            }
        }

        return "";
    }

    private void startLoginProcess() {

        final String phoneNumber = ((AppCompatEditText) getView().findViewById(R.id.edit_text_login_phone_number)).getText().toString();
        final String countryName = ((TextView) ((Spinner) getView().findViewById(R.id.spinner_country_selection)).getSelectedView()).getText().toString();
        final String countryCode = countryISDCodeHashMap.get(countryName);

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

                                String sessionId = jsonResponse.optJSONObject("data").optString("session_id");

                                OTPVerificationDialogFragment.newInstance(sessionId, phoneNumber, countryName, countryCode).show(getActivity().getSupportFragmentManager(), OTPVerificationDialogFragment.class.getSimpleName());
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
}