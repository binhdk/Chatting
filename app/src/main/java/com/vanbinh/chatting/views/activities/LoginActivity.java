package com.vanbinh.chatting.views.activities;

import android.annotation.TargetApi;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.databinding.DataBindingUtil;
import android.net.ParseException;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;

import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.auth.TwitterAuthProvider;

import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;

import com.vanbinh.chatting.apiconnect.connects.DeviceTokenAPI;
import com.vanbinh.chatting.R;
import com.vanbinh.chatting.common.sharedpreference.MySharedPreference;
import com.vanbinh.chatting.common.singletons.SingleTonUser;
import com.vanbinh.chatting.common.sqlite.UserSQLiteManager;
import com.vanbinh.chatting.models.User;
import com.vanbinh.chatting.viewmodels.LoginActivityVM;
import com.vanbinh.chatting.databinding.ActivityLoginBinding;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static android.Manifest.permission.READ_CONTACTS;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class LoginActivity extends BaseActivity implements
        GoogleApiClient.OnConnectionFailedListener,
        android.app.LoaderManager.LoaderCallbacks<Cursor> {
    private LoginActivityVM viewModel;
    private ActivityLoginBinding binding;
    private static final int REQUEST_PERMISSION_CODE = 0;
    private FirebaseAuth mAuth;
    private static final String TAG = "LoginActivity";
    private GoogleApiClient mGoogleApiClient;
    private static final int RC_SIGN_IN = 9001;
    private CallbackManager mCallbackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login);
        viewModel = new LoginActivityVM(this, binding);
        binding.setViewModel(viewModel);
        mAuth = FirebaseAuth.getInstance();

        /*email password sign in*/
        populateAutoComplete();
        binding.email.setOnTouchListener(new View.OnTouchListener(){
            @Override
            public boolean onTouch(View v, MotionEvent event){
                binding.email.showDropDown();
                return false;
            }
        });

        binding.password.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
                    validateForm();
                    return true;
                }
                return false;
            }
        });

        binding.emailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                signInWithEmailPassword(binding.email.getText().toString(), binding.password.getText().toString());
            }
        });
        binding.emailSignUpButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                createAccount(binding.email.getText().toString(), binding.password.getText().toString());
            }
        });

        /*google sign in*/
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
        binding.googleSignInButton.setSize(SignInButton.SIZE_STANDARD);
        binding.googleSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                signInWithGoogle();
            }
        });

        /*twitter sign in*/
        binding.twitterLoginButton.setCallback(new Callback<TwitterSession>() {
            @Override
            public void success(Result<TwitterSession> result) {
                Log.d(TAG, "twitterLogin:success" + result);
                signInWithTwitter(result.data);
            }

            @Override
            public void failure(TwitterException exception) {
                Log.w(TAG, "twitterLogin:failure", exception);
            }
        });

        /*facebook login*/
        mCallbackManager = CallbackManager.Factory.create();
        binding.fbLoginButton.setReadPermissions("email", "public_profile");
        binding.fbLoginButton.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d(TAG, "facebook:onSuccess:" + loginResult);
                handleFacebookAccessToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                Log.d(TAG, "facebook:onCancel");
            }

            @Override
            public void onError(FacebookException error) {
                Log.d(TAG, "facebook:onError", error);
            }
        });

    }

    private void handleFacebookAccessToken(AccessToken accessToken) {
        AuthCredential credential = FacebookAuthProvider.getCredential(accessToken.getToken());
        signInWithCredential(credential);
    }

    private void signInWithEmailPassword(String email, String password) {
        Log.d(TAG, "signInWithEmailPassword:" + email);
        if (!validateForm()) {
            return;
        }

        AuthCredential credential = EmailAuthProvider.getCredential(email, password);
        signInWithCredential(credential);
    }

    private void signInWithTwitter(TwitterSession session) {
        AuthCredential credential = TwitterAuthProvider.getCredential(
                session.getAuthToken().token,
                session.getAuthToken().secret);
        signInWithCredential(credential);
    }

    private void signInWithGoogle() {
        Intent loginIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(loginIntent, RC_SIGN_IN);
    }

    private void handleGoogleSignIn(GoogleSignInResult result) {
        Log.d(TAG, "handleGoogleSignIn: " + result.isSuccess());
        if (result.isSuccess()) {
            GoogleSignInAccount account = result.getSignInAccount();
            AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
            signInWithCredential(credential);
        } else {
            Log.e(TAG, "handleGoogleSignIn" + result.getStatus());
            Toast.makeText(this, "Authentication fail!", Toast.LENGTH_LONG).show();
        }
    }

    private void signInWithCredential(AuthCredential credential) {
        showProgressDialog();
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "signInWithCredential:success");
                            startActivity(new Intent(LoginActivity.this, MainActivity.class));
                            LoginActivity.this.finish();
                        } else {
                            Exception exception = task.getException();
                            if (exception instanceof com.google.firebase.auth.FirebaseAuthInvalidCredentialsException)
                                Toast.makeText(LoginActivity.this, "Invalid password",
                                        Toast.LENGTH_SHORT).show();
                            else if (exception instanceof com.google.firebase.auth.FirebaseAuthInvalidUserException)
                                Toast.makeText(LoginActivity.this, "No user account with this email",
                                        Toast.LENGTH_SHORT).show();
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                        }
                        hideProgressDialog();
                    }
                });
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.d(TAG, "onConnectionFailed:" + connectionResult);
        Toast.makeText(this, "Google Play Services error.", Toast.LENGTH_SHORT).show();
    }

    private void populateAutoComplete() {
        if (!mayRequestPermissions()) {
            return;
        }
        getLoaderManager().initLoader(0,null,this);

        ArrayList<String> emailAddressesCollection = new ArrayList<>();
        Cursor cursor = getContentResolver().query(ContactsContract.CommonDataKinds.Email.CONTENT_URI, null, null, null, null);
        if(cursor!=null) cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            String email = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Email.ADDRESS));
            emailAddressesCollection.add(email);
            cursor.moveToNext();
        }
        cursor.close();
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_dropdown_item_1line, emailAddressesCollection);
        binding.email.setAdapter(adapter);
    }

    private boolean validateForm() {
        binding.email.setError(null);
        binding.password.setError(null);

        String email = binding.email.getText().toString();
        String password = binding.password.getText().toString();
        boolean valid = true;
        boolean cancel = false;
        View focusView = null;
        if (TextUtils.isEmpty(email)) {
            binding.email.setError(getString(R.string.error_field_required));
            focusView = binding.email;
            cancel = true;
            valid = false;
        } else if (!isEmailValid(email)) {
            binding.email.setError(getString(R.string.error_invalid_email));
            focusView = binding.email;
            cancel = true;
            valid = false;
        } else if (TextUtils.isEmpty(password)) {
            binding.password.setError(getString(R.string.error_field_required));
            focusView = binding.password;
            cancel = true;
            valid = false;
        } else if (!isPasswordValid(password)) {
            binding.password.setError(getString(R.string.error_incorrect_password));
            focusView = binding.password;
            cancel = true;
            valid = false;
        }
        if (cancel) {
            focusView.requestFocus();
        }
        return valid;
    }

    private void createAccount(final String email, final String password) {
        Log.d(TAG, "createAccount:" + email);
        if (!validateForm()) {
            return;
        }
        showProgressDialog();
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "createUserWithEmail:success");
                            Toast.makeText(LoginActivity.this, "Create Account success", Toast.LENGTH_SHORT).show();
                            signInWithEmailPassword(email, password);
                        } else {
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }

    private boolean isEmailValid(String email) {
        if (email == null) {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
        }
    }

    private boolean isPasswordValid(String password) {
        Pattern pattern;
        Matcher matcher;
//        final String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{4,}$";
        final String PASSWORD_PATTERN = "^.{4,}$";
        pattern = Pattern.compile(PASSWORD_PATTERN);
        matcher = pattern.matcher(password);
        return matcher.matches();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        binding.twitterLoginButton.onActivityResult(requestCode, resultCode, data);
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case RC_SIGN_IN:
                GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
                handleGoogleSignIn(result);
                break;
        }
    }

    //    save user information to sqlite and to server
    private void saveUserSignIn(FirebaseUser firebaseUser) {
        String token = new MySharedPreference.BuildShare()
                .init(this, MySharedPreference.SR_MAIN)
                .get()
                .getString(MySharedPreference.SR_TOKEN, null);
        User user = new User();
        try {
            user.setName(firebaseUser.getDisplayName());
            user.setEmail(firebaseUser.getEmail());
            user.setAvatar(String.valueOf(firebaseUser.getPhotoUrl()));
            user.setToken(token);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        UserSQLiteManager sqLiteManager = UserSQLiteManager.getInstance(this);
        sqLiteManager.addUser(user);
        SingleTonUser.setInstance(this);
        new DeviceTokenAPI(this).execute();
    }

    //    require permission
    private boolean mayRequestPermissions() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }
        if (checkSelfPermission(READ_CONTACTS) == PackageManager.PERMISSION_GRANTED
                && checkSelfPermission(WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        if (shouldShowRequestPermissionRationale(READ_CONTACTS)) {
            Snackbar.make(binding.email, R.string.permission_rationale, Snackbar.LENGTH_INDEFINITE)
                    .setAction(android.R.string.ok, new View.OnClickListener() {
                        @Override
                        @TargetApi(Build.VERSION_CODES.M)
                        public void onClick(View v) {
                            requestPermissions(new String[]{READ_CONTACTS, WRITE_EXTERNAL_STORAGE}, REQUEST_PERMISSION_CODE);
                        }
                    });
        } else {
            requestPermissions(new String[]{READ_CONTACTS, WRITE_EXTERNAL_STORAGE}, REQUEST_PERMISSION_CODE);
        }
        return false;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == REQUEST_PERMISSION_CODE) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                populateAutoComplete();
            }
        }
    }

    @Override
    public android.content.Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(this,
                Uri.withAppendedPath(ContactsContract.Profile.CONTENT_URI,
                        ContactsContract.Contacts.Data.CONTENT_DIRECTORY),ProfileQuery.PROJECTION,
                ContactsContract.Contacts.Data.MIMETYPE +
                     " = ?", new String[]{ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE},
                ContactsContract.Contacts.Data.IS_PRIMARY + " DESC");
    }

    @Override
    public void onLoadFinished(android.content.Loader<Cursor> loader, Cursor cursor) {
        List<String> emails = new ArrayList<>();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            emails.add(cursor.getString(ProfileQuery.ADDRESS));
            cursor.moveToNext();
        }

        addEmailsToAutoComplete(emails);
    }

    private void addEmailsToAutoComplete(List<String> emailAddressCollection) {
        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(LoginActivity.this,
                        android.R.layout.simple_dropdown_item_1line, emailAddressCollection);

        binding.email.setAdapter(adapter);
    }

    @Override
    public void onLoaderReset(android.content.Loader<Cursor> loader) {

    }
    private interface ProfileQuery {
        String[] PROJECTION = {
                ContactsContract.CommonDataKinds.Email.ADDRESS,
                ContactsContract.CommonDataKinds.Email.IS_PRIMARY,
        };

        int ADDRESS = 0;
        int IS_PRIMARY = 1;
    }
}

