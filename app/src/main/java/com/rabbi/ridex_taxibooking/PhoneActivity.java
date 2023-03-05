package com.rabbi.ridex_taxibooking;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.IntentSender;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.auth.api.credentials.Credential;
import com.google.android.gms.auth.api.credentials.Credentials;
import com.google.android.gms.auth.api.credentials.CredentialsApi;
import com.google.android.gms.auth.api.credentials.HintRequest;
import com.google.android.gms.auth.api.identity.GetPhoneNumberHintIntentRequest;
import com.google.android.gms.auth.api.identity.Identity;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.hbb20.CountryCodePicker;
import com.rabbi.ridex_taxibooking.databinding.ActivityPhoneBinding;

import java.util.concurrent.TimeUnit;

public class PhoneActivity extends AppCompatActivity {

    ActivityPhoneBinding binding;
    CountryCodePicker ccp;
    private static final int CREDENTIAL_PICKER_REQUEST =120 ;
    private String selected_country_code = "+880";
    ProgressDialog pd;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    FirebaseAuth auth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPhoneBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        pd = new ProgressDialog(this);
        auth = FirebaseAuth.getInstance();

        ccp = findViewById(R.id.countryCodePicker);
        ccp.setOnCountryChangeListener(new CountryCodePicker.OnCountryChangeListener() {
            @Override
            public void onCountrySelected() {
                selected_country_code = ccp.getSelectedCountryCodeWithPlus();
                Toast.makeText(PhoneActivity.this, selected_country_code, Toast.LENGTH_SHORT).show();
            }
        });


        HintRequest hintRequest = new HintRequest.Builder()
                .setPhoneNumberIdentifierSupported(true)
                .build();


        PendingIntent intent = Credentials.getClient(PhoneActivity.this).getHintPickerIntent(hintRequest);
        try
        {
            startIntentSenderForResult(intent.getIntentSender(), CREDENTIAL_PICKER_REQUEST, null, 0, 0, 0,new Bundle());
        }
        catch (IntentSender.SendIntentException e)
        {
            e.printStackTrace();
        }



        binding.continueNumberButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Toast.makeText(PhoneActivity.this, binding.editTextPhone.getText().toString().trim(), Toast.LENGTH_SHORT).show();

                binding.verificationLinearLayout.setVisibility(View.VISIBLE);
                binding.editTextPhone.setFocusable(false);
                binding.editTextPhone.setEnabled(false);
                binding.editTextPhone.setCursorVisible(false);
                binding.editTextPhone.setKeyListener(null);
                binding.editTextPhone.setBackgroundColor(Color.TRANSPARENT);
                binding.continueNumberButton.setEnabled(false);
                binding.continueNumberButton.setFocusable(false);
                binding.continueNumberButton.setCursorVisible(false);
                binding.continueNumberButton.setKeyListener(null);

                if (binding.editTextPhone.getText().toString().trim().isEmpty()){
                    Toast.makeText(PhoneActivity.this, "phone number must not be empty!", Toast.LENGTH_SHORT).show();
                }else if (binding.editTextPhone.getText().toString().trim().length() != 10){
                    Toast.makeText(PhoneActivity.this, "Invalid phone number!", Toast.LENGTH_SHORT).show();
                }else{
                    otpSend();
                }

            }
        });
        binding.verifyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(PhoneActivity.this, binding.firstPinView.getText().toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void otpSend() {
        pd.setTitle("Sending OTP");
        pd.setMessage("A 6 digit verification code is sending to "+ selected_country_code+binding.editTextPhone.getText().toString().trim());
        pd.setCanceledOnTouchOutside(false);
        pd.show();


        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential credential) {

            }

            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {
                pd.dismiss();
                Toast.makeText(PhoneActivity.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onCodeSent(@NonNull String verificationId,
                                   @NonNull PhoneAuthProvider.ForceResendingToken token) {

                pd.dismiss();

            }
        };
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(auth)
                        .setPhoneNumber(selected_country_code+binding.editTextPhone.getText().toString().trim())       // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(this)                 // Activity (for callback binding)
                        .setCallbacks(mCallbacks)          // OnVerificationStateChangedCallbacks
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CREDENTIAL_PICKER_REQUEST && resultCode == RESULT_OK)
        {
            Credential credentials = data.getParcelableExtra(Credential.EXTRA_KEY);

            binding.editTextPhone.setText(credentials.getId().substring(3).substring(1));



        }
        else if (requestCode == CREDENTIAL_PICKER_REQUEST && resultCode == CredentialsApi.ACTIVITY_RESULT_NO_HINTS_AVAILABLE)
        {
            // *** No phone numbers available ***
            Toast.makeText(PhoneActivity.this, "No phone numbers found", Toast.LENGTH_LONG).show();
        }


    }
}