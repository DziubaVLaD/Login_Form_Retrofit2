package com.sourcey.registration_form;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;

import java.io.IOException;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "LoginActivity";
    private static final int REQUEST_SIGNUP = 0;

    @BindView(R.id.input_email)
    EditText _emailText;
    @BindView(R.id.input_password)
    EditText _passwordText;
    @BindView(R.id.btn_login)
    Button _loginButton;
    @BindView(R.id.checkBox)
    CheckBox _passwordCheckbox;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        _loginButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                login();
            }
        });

        _passwordCheckbox.setOnCheckedChangeListener(new CheckBox.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) {
                    // show password
                    _passwordText.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                } else {
                    // hide password
                    _passwordText.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
            }

        });

    }

    public void login() {
        Log.d(TAG, "Login");

//        if (!validate()) {
//            onLoginFailed();
//            return;
//        }

        _loginButton.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(MainActivity.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Authenticating...");
        progressDialog.show();

        String email = _emailText.getText().toString();
        String password = _passwordText.getText().toString();

        // TODO: Implement your own authentication logic here.
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        // set your desired log level
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.addInterceptor(logging);  // <-- this is the important line!

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClient.build())
                .build();

        final RequestInterface[] requestInterface = {retrofit.create(RequestInterface.class)};

        User user = new User();
        user.setEmail(email);
        user.setPassword(password);

        Call<SignupResponseDTO> response = requestInterface[0].operation(user);

        response.enqueue(new Callback<SignupResponseDTO>() {
            @Override
            public void onResponse(Call<SignupResponseDTO> call, retrofit2.Response<SignupResponseDTO> response) {

                Log.d("Response", "not fail");
                SignupResponseDTO resp = response.body();
                if (response.code() == 400) {
                    Log.d("Response", "OnResponse - Status : " + response.code());
                    Gson gson = new Gson();
                    TypeAdapter<SignupResponseDTO> adapter = gson.getAdapter(SignupResponseDTO.class);
                    try {
                        if (response.errorBody() != null)
                            resp = adapter.fromJson(response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                // continue validation
                System.out.println(resp);
                if (!resp.getValid().isEmpty()) {
                    List<SignupResponseDTO.Valid> invalidList = resp.getValid();
                    for (SignupResponseDTO.Valid invalidElement : invalidList) {
                        String field = invalidElement.getField();
                        switch (field) {
                            case "login": {
                                _emailText.setError(invalidElement.getMessage());
                                break;
                            }
                            case "password": {
                                _passwordText.setError(invalidElement.getMessage());
                                break;
                            }
                            default: {
                                System.out.println(field);
                                break;
                            }
                        }

                    }


                } else {
                    System.out.println("login success");
                }

            }

            @Override
            public void onFailure(Call<SignupResponseDTO> call, Throwable t) {
                Log.d("Response", "fail");

            }
        });

        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        Log.d("Response", "close progressdialog");
                        // On complete call either onLoginSuccess or onLoginFailed
                        onLoginSuccess();
                        // onLoginFailed();
                        progressDialog.dismiss();
                    }
                }, 3000);

        Log.d("Response", "login end");
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_SIGNUP) {
            if (resultCode == RESULT_OK) {

                // TODO: Implement successful signup logic here
                // By default we just finish the Activity and log them in automatically
                this.finish();
            }
        }
    }

    @Override
    public void onBackPressed() {
        // Disable going back to the MainActivity
        moveTaskToBack(true);
    }

    public void onLoginSuccess() {
        _loginButton.setEnabled(true);
        // finish();
    }

    public void onLoginFailed() {
        Toast.makeText(getBaseContext(), "Login failed", Toast.LENGTH_LONG).show();

        _loginButton.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        String email = _emailText.getText().toString();
        String password = _passwordText.getText().toString();

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _emailText.setError("enter a valid email address");
            valid = false;
        } else {
            _emailText.setError(null);
        }

        if (password.isEmpty() || password.length() < 4 || password.length() > 20) {
            _passwordText.setError("between 4 and 20 alphanumeric characters");
            valid = false;
        } else {
            _passwordText.setError(null);
        }

        return valid;
    }

}
