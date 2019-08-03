package com.zikrabyte.organic.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.transition.Slide;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.transition.Visibility;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.zikrabyte.organic.R;
import com.zikrabyte.organic.api_requests.AddAddress;
import com.zikrabyte.organic.api_requests.EditAddress;
import com.zikrabyte.organic.api_requests.ViewCart;
import com.zikrabyte.organic.api_responses.addaddress.AddAddressResponse;
import com.zikrabyte.organic.api_responses.apartmentlist.ApartmentListResponse;
import com.zikrabyte.organic.api_responses.apartmentlist.Response;
import com.zikrabyte.organic.api_responses.editaddress.EditAddressResponse;
import com.zikrabyte.organic.api_responses.viewcart.ViewCartResponse;
import com.zikrabyte.organic.beanclasses.AddressBean;
import com.zikrabyte.organic.utils.EzzShoppUtils;
import com.zikrabyte.organic.utils.OnResponseListener;
import com.zikrabyte.organic.utils.WebServices;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EditAddressActivity extends AppCompatActivity implements View.OnClickListener,AdapterView.OnItemSelectedListener,OnResponseListener{

    Button mUpdate;
    ImageView mActionBack;
    int spinnerPosition;

    EditText mName,mPhoneNum,mCity,mHouseNum,mPinCode;
    Spinner mApartmentsSpinner;
    String receivedName,receivedPhoneNum,receivedCity,receivedApartmentName,receivedHouseNum,receivedPinCode;

    String[] apartments={"select Apartment","Apartment1","Apartment2","Apartment3","Apartment4","Apartment5"};

    ArrayList<AddressBean> mList=new ArrayList<>();

    List<Response> apartmentList=new ArrayList<>();

    List<String> apartmentNames=new ArrayList<>();
    List<String> apartmentId=new ArrayList<>();

    String name,phoneNumber,houseNumber,apartmentName,city,pinCode;
     private static String selectedApartmentId;

    String authKey,userId,addressId;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_address);
        setupWindowAnimations();
        initializeViews();
    }

    private boolean receiveIntentData() {
        Bundle bundle=getIntent().getExtras();
        if(bundle!=null)
        {
            receivedName=bundle.getString("name",null);
            receivedPhoneNum=bundle.getString("phone",null);
            receivedCity=bundle.getString("city",null);
            receivedApartmentName=bundle.getString("apartment",null);
            receivedHouseNum=bundle.getString("house",null);
            receivedPinCode=bundle.getString("pincode",null);
            //spinnerPosition=bundle.getInt("spinner_position",0);
            addressId=bundle.getString("addressid",null);

            return true;
        }
        else
            return false;

    }

    private void initializeViews()
    {

        mName=findViewById(R.id.vE_aea_name);
        mPhoneNum=findViewById(R.id.vE_aea_phone_number);
        mCity=findViewById(R.id.vE_aea_city);
        mHouseNum=findViewById(R.id.vE_aea_house_number);
        mApartmentsSpinner=findViewById(R.id.vS_aea_select_apartment);
        mPinCode=findViewById(R.id.vE_aea_pincode);

       /* ArrayAdapter arrayAdapter = new ArrayAdapter(this,android.R.layout.simple_spinner_item,apartments);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner
        mApartmentsSpinner.setAdapter(arrayAdapter);
*/
       mApartmentsSpinner.setOnItemSelectedListener(this);
        mActionBack=findViewById(R.id.vI_aea_back_icon);
        
        mUpdate=findViewById(R.id.vB_aea_update);
        mUpdate.setEnabled(true);
        
        mUpdate.setOnClickListener(this);
        mActionBack.setOnClickListener(this);

        if(receiveIntentData())
        {
            setValuesToViews();

        }

        getSharedPreferenceData();

    }

    private void setValuesToViews() {
        mName.setText(receivedName);
        mPhoneNum.setText(receivedPhoneNum);
        mCity.setText(receivedCity);
      //  mApartmentsSpinner.setSelection(spinnerPosition);
        mHouseNum.setText(receivedHouseNum);
        mPinCode.setText(receivedPinCode);
    }

    private void getSharedPreferenceData()
    {
        SharedPreferences preferences=getSharedPreferences("LOGIN_PREFERENCE",MODE_PRIVATE);
        authKey=preferences.getString("AUTH_KEY",null);
        userId=preferences.getString("USER_ID",null);

        callViewApartmentsAPI(authKey,userId);
    }

    public void callViewApartmentsAPI(String authKey,String userId)
    {
        if (EzzShoppUtils.isConnectedToInternet(getApplicationContext())) {

            WebServices<ApartmentListResponse> webServices = new WebServices<ApartmentListResponse>(EditAddressActivity.this);
            webServices.viewApartments(WebServices.BASE_URL, WebServices.ApiType.viewApartments,authKey,userId);
        } else {
            //Snackbar.make(mSignup,R.string.err_msg_nointernet, Snackbar.LENGTH_SHORT).show();
            Toast.makeText(this, R.string.err_msg_nointernet+"", Toast.LENGTH_SHORT).show();
        }

    }

    public void callAddAddressAPI(String authKey,String userId)
    {
        name=mName.getText().toString();
        phoneNumber=mPhoneNum.getText().toString();
        houseNumber=mHouseNum.getText().toString();
        //apartmentName=mApartmentsSpinner.getSelectedItem().toString();
        apartmentName=selectedApartmentId;
        city=mCity.getText().toString();
        pinCode=mPinCode.getText().toString();

        AddAddress addAddress=new AddAddress(userId,name,phoneNumber,houseNumber,apartmentName,city,pinCode);
        if (EzzShoppUtils.isConnectedToInternet(getApplicationContext())) {

            WebServices<AddAddressResponse> webServices = new WebServices<AddAddressResponse>(EditAddressActivity.this);
            webServices.addAddress(WebServices.BASE_URL, WebServices.ApiType.addAddress,authKey,userId,addAddress);
        } else {
            //Snackbar.make(mSignup,R.string.err_msg_nointernet, Snackbar.LENGTH_SHORT).show();
            Toast.makeText(this, R.string.err_msg_nointernet+"", Toast.LENGTH_SHORT).show();
        }

    }

    private void callEditAddressAPI(String authKey,String userId,String addressId)
    {
        name=mName.getText().toString();
        phoneNumber=mPhoneNum.getText().toString();
        houseNumber=mHouseNum.getText().toString();
        //apartmentName=mApartmentsSpinner.getSelectedItem().toString();
        apartmentName=selectedApartmentId;
        city=mCity.getText().toString();
        pinCode=mPinCode.getText().toString();

        EditAddress editAddress=new EditAddress(addressId,name,phoneNumber,houseNumber,apartmentName,city,pinCode);
        if (EzzShoppUtils.isConnectedToInternet(getApplicationContext())) {

            WebServices<EditAddressResponse> webServices = new WebServices<EditAddressResponse>(EditAddressActivity.this);
            webServices.editAddress(WebServices.BASE_URL, WebServices.ApiType.editAddress,authKey,userId,editAddress);
        } else {
            //Snackbar.make(mSignup,R.string.err_msg_nointernet, Snackbar.LENGTH_SHORT).show();
            Toast.makeText(this, R.string.err_msg_nointernet+"", Toast.LENGTH_SHORT).show();
        }


    }

    @Override
    public void onClick(View v) {

        switch (v.getId())
        {
            case R.id.vB_aea_update:
                updateAddress();
                break;

            case R.id.vI_aea_back_icon:
                onBackPressed();
                break;
        }

    }

    private void updateAddress() {

        if(!validateUserName()) {
            return;
        }
        else if (!validatePhoneNumber()) {
            return;
        }
        else if (!validateAddress()) {
            return;
        }
        else if (!validateApartmentSpinner()) {
            return;
        }
        else if (!validateCity()) {
            return;
        }
        else if (!validatePincode()) {
            return;
        }
        else {
            mUpdate.setEnabled(false);

            if (receiveIntentData()) {
                callEditAddressAPI(authKey, userId, addressId);
            } else {
                callAddAddressAPI(authKey, userId);
            }
        }
       /* updateList();
        startActivity(SelectAddressActivity.class);*/


      /*  // get selected radio button from radioGroup
        int selectedId = mRadioGroup.getCheckedRadioButtonId();

        // find the radiobutton by returned id
        mRadioButton = (RadioButton) findViewById(selectedId);

        Toast.makeText(this,
                mRadioButton.getText(), Toast.LENGTH_SHORT).show();*/
    }

    private boolean validateUserName() {

        String userName=mName.getText().toString().trim();

        if (userName.isEmpty() || userName.length()<3 || !isValidUserName(userName)) {
            Snackbar.make(mUpdate,R.string.err_msg_name, Snackbar.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }
    private boolean isValidUserName(String name)
    {
        String regexUserName = "^[A-Za-z\\s]+$";
        Pattern p = Pattern.compile(regexUserName, Pattern.CASE_INSENSITIVE);
        Matcher m = p.matcher(name);
        return m.matches();
    }

    private boolean validatePhoneNumber()
    {
        String phoneNumber=mPhoneNum.getText().toString().trim();

        if (phoneNumber.isEmpty()||phoneNumber.length()<10 ||!isValidPhoneNumber(phoneNumber)) {
            Snackbar.make(mUpdate,R.string.err_msg_phonenumber, Snackbar.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private static boolean isValidPhoneNumber(String number)
    {

        /*String expression = "[6-9][0-9]{9}";
        Pattern p = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher m = p.matcher(number);
        return m.matches();*/
        return number.matches("[6-9][0-9]{9}"); //1st digit should be 6 to 9 then 9 digits any combination of 0-9
    }
    private boolean validateAddress()
    {
        if (mHouseNum.getText().toString().trim().isEmpty()||mHouseNum.getText().toString().trim().length()<1) {
            Snackbar.make(mUpdate,"Please Provide Door number", Snackbar.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
    private boolean validateCity()
    {
        if (mCity.getText().toString().trim().isEmpty()) {
            Snackbar.make(mUpdate,"Please Enter a City", Snackbar.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
    private boolean validatePincode()
    {
        if (mPinCode.getText().toString().trim().isEmpty() || mPinCode.getText().toString().trim().length()<6) {
            Snackbar.make(mUpdate,"Please Enter Pin code", Snackbar.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private boolean validateApartmentSpinner()
    {

        if(mApartmentsSpinner.getSelectedItem().toString().trim().equals("Select Apartment"))
        {
            Snackbar.make(mUpdate,"Please select an apartment", Snackbar.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {


        selectedApartmentId=apartmentId.get(position);
        //Toast.makeText(this, selectedApartmentId+"", Toast.LENGTH_SHORT).show();

        //Toast.makeText(getApplicationContext(),apartments[position] ,Toast.LENGTH_LONG).show();
        //Toast.makeText(this, apartmentList.get(position).getName()+"", Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private void updateList()
    {
        String enteredName=mName.getText().toString();
        String enteredNumber=mPhoneNum.getText().toString();
        String enteredHouseNum=mHouseNum.getText().toString();
        String enteredCity=mCity.getText().toString();
        String enteredApartmentName=mApartmentsSpinner.getSelectedItem().toString();

        AddressBean object =new AddressBean(enteredName,enteredNumber,enteredHouseNum,
                enteredApartmentName,enteredCity);
        mList.add(object);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void setupWindowAnimations() {
        Transition transition,returnTransition;
        //transition = buildEnterTransition();
        transition = TransitionInflater.from(this).inflateTransition(R.transition.slide_from_bottom);
        returnTransition=buildReturnTransition();

       /* if (type == TYPE_PROGRAMMATICALLY) {
            transition = buildEnterTransition();
        }  else {
            transition = TransitionInflater.from(this).inflateTransition(R.transition.explode);
        }*/
        getWindow().setEnterTransition(transition);
        getWindow().setReturnTransition(returnTransition);
    }
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private Visibility buildReturnTransition() {
        Visibility enterTransition = new Slide();
        enterTransition.setDuration(getResources().getInteger(R.integer.anim_duration_long));
        return enterTransition;
    }
    private void startActivity(Class<?> tClass)
    {
        ActivityOptionsCompat transitionActivityOptions = ActivityOptionsCompat.makeSceneTransitionAnimation(this,null);
        Intent intent=new Intent(EditAddressActivity.this,tClass);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent,transitionActivityOptions.toBundle());

        //overridePendingTransition(R.anim.slide_in,R.anim.slide_in);
        //this.finish();
    }

    @Override
    public void onResponse(Object response, WebServices.ApiType URL, boolean isSucces) {

        switch (URL)
        {
            case viewApartments:
                ApartmentListResponse apartmentListResponse= (ApartmentListResponse) response;
                if(isSucces)
                {

                    if(apartmentListResponse !=null) {
                        if (apartmentListResponse.getResponsecode() != null) {
                            if (apartmentListResponse.getResponsecode().equalsIgnoreCase("200")) {
                                apartmentList = apartmentListResponse.getResponse();
                                updateApartmentSpinner(apartmentList);
                            }
                            else {
                               if( apartmentListResponse.getViewAddressMessage()!=null)
                               {
                                   Snackbar.make(mUpdate,apartmentListResponse.getViewAddressMessage(), Snackbar.LENGTH_LONG).show();

                               }



                            }
                        }
                    }
                }
                else {

                   //API Call fail
                    if(apartmentListResponse !=null) {
                        if (apartmentListResponse.getViewAddressMessage() != null) {
                            Snackbar.make(mUpdate, apartmentListResponse.getViewAddressMessage(), Snackbar.LENGTH_LONG).show();

                        }
                    }
                }
                break;

            case addAddress:
                AddAddressResponse addAddressResponse= (AddAddressResponse) response;
                if(isSucces)
                {
                    if(addAddressResponse!=null)
                    {
                        if(addAddressResponse.getResponse()!=null) {
                            if(addAddressResponse.getResponse()!=null) {
                                if (addAddressResponse.getResponse().equalsIgnoreCase("200")) {
                                    if (addAddressResponse.getAddAddressMessage() != null) {
                                        Snackbar.make(mUpdate, addAddressResponse.getAddAddressMessage() + "", 2000).show();

                                        new Handler().postDelayed(new Runnable() {
                                            @Override
                                            public void run() {
                                                startActivity(SelectAddressActivity.class);
                                            }
                                        },2000);

                                    } else {
                                        if (addAddressResponse.getAddAddressMessage() != null) {
                                            Snackbar.make(mUpdate, addAddressResponse.getAddAddressMessage(), Snackbar.LENGTH_LONG).show();
                                        }
                                    }

                                } else {
                                    if (addAddressResponse.getAddAddressMessage() != null) {
                                        Snackbar.make(mUpdate, addAddressResponse.getAddAddressMessage(), Snackbar.LENGTH_LONG).show();
                                    }
                                }
                            }
                        }

                    }

                }
                else
                {
                    //API call failed
                    if(addAddressResponse!=null) {

                        if (addAddressResponse.getAddAddressMessage() != null) {
                            Snackbar.make(mUpdate, addAddressResponse.getAddAddressMessage(), Snackbar.LENGTH_LONG).show();
                        }
                    }
                }
                break;

            case editAddress:
                EditAddressResponse editAddressResponse= (EditAddressResponse) response;
                if(isSucces)
                {

                    if(editAddressResponse!=null)
                    {
                        if (editAddressResponse.getResponse() != null) {
                            if (editAddressResponse.getResponse().equalsIgnoreCase("200")) {
                                if (editAddressResponse.getEditAddressMessage() != null) {
                                    Snackbar.make(mUpdate, editAddressResponse.getEditAddressMessage(), Snackbar.LENGTH_LONG).show();

                                }
                                startActivity(SelectAddressActivity.class);
                            } else {
                                if (editAddressResponse.getEditAddressMessage() != null) {
                                    Snackbar.make(mUpdate, editAddressResponse.getEditAddressMessage(), Snackbar.LENGTH_LONG).show();

                                }
                            }
                        }


                    }else {
                        //EMPTY RESPONSE
                    }

                }
                else {
                    if(editAddressResponse!=null)
                    {
                        if(editAddressResponse.getEditAddressMessage()!=null)
                        {
                            Snackbar.make(mUpdate,editAddressResponse.getEditAddressMessage() , Snackbar.LENGTH_LONG).show();

                        }
                    }
                    //API call failed
                }
                break;

        }

    }

    private void updateApartmentSpinner(List<Response> apartmentList) {


        for(int i=1;i<apartmentList.size();i++)
        {
            apartmentNames.add(apartmentList.get(i).getName());
            apartmentId.add(apartmentList.get(i).getId());

        }


        apartmentNames.add(0,"Select Apartment");
        apartmentId.add(0,"1");
        ArrayAdapter arrayAdapter = new ArrayAdapter(this,android.R.layout.simple_spinner_item,apartmentNames);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner

        mApartmentsSpinner.setAdapter(arrayAdapter);
        for(int i=0;i<apartmentNames.size();i++)
        {
            if(apartmentNames.get(i).equals(receivedApartmentName))
            {
                spinnerPosition=i;
                mApartmentsSpinner.setSelection(spinnerPosition);

            }
        }


    }
}
