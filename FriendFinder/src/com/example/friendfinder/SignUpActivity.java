package com.example.friendfinder;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.parse.ParseObject;
import com.parse.ParseUser;

public class SignUpActivity extends Activity {

	private ParseUser user = null;
	private final String DebugLoginTag = "LOGIN";
	private final int PASSWORD_MIN_LENGTH = 2;
	private final String phoneRegex = "^[+][0-9]{8,20}$";
	private final String emailRegex = "^[a-zA-Z0-9._-]+@[a-z]+[.]+[a-z]+$";
	
	private EditText etEmail;
	private EditText etPassword;
	private EditText etPasswordRepeat;
	private EditText etFirstName;
	private EditText etLastName;
	private EditText etPhone;
	private View vSignupStatus;
	private View vSignupForm;
	private Button btnSignUp;
	private CheckBox signMeIn;
	
	private String email;
	private String password;
	private String passwordRepeat;
	private String firstName;
	private String lastName;
	private String phone;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sign_up);
		etEmail = (EditText) findViewById(R.id.email);
		etPassword = (EditText) findViewById(R.id.password);
		etPasswordRepeat = (EditText) findViewById(R.id.passwordRepeat);
		etFirstName = (EditText) findViewById(R.id.firstName);
		etLastName = (EditText) findViewById(R.id.lastName);
		etPhone = (EditText) findViewById(R.id.phone);
		vSignupStatus = (View) findViewById(R.id.signup_status);
		vSignupForm = (View) findViewById(R.id.signup_form);
		btnSignUp = (Button) findViewById(R.id.sign_up_button);
		signMeIn = (CheckBox) findViewById(R.id.sign_me_in);
		
		btnSignUp.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				attemptSignUp();
			}
		});
		
	}
	
	public void attemptSignUp() {
		// Reset errors.
		etEmail.setError(null);
		etPassword.setError(null);
		etPasswordRepeat.setError(null);
		etFirstName.setError(null);
		etLastName.setError(null);
		etPhone.setError(null);

		// Store values at the time of the login attempt.
		email = etEmail.getText().toString();
		password = etPassword.getText().toString();
		passwordRepeat = etPasswordRepeat.getText().toString();
		firstName = etFirstName.getText().toString();
		lastName = etLastName.getText().toString();
		phone = etPhone.getText().toString();

		boolean cancel = false;
		View focusView = null;

		// Check for a valid email address.
		if (TextUtils.isEmpty(email)) {
			etEmail.setError(getString(R.string.error_field_required));
			focusView = etEmail;
			cancel = true;
		}/* else if (!email.matches(emailRegex)) {
			etEmail.setError(getString(R.string.error_invalid_email));
			focusView = etEmail;
			cancel = true;
		}*/
		
		// Check for a valid password.
		if (TextUtils.isEmpty(password)) {
			etPassword.setError(getString(R.string.error_field_required));
			focusView = etPassword;
			cancel = true;
		} else if (password.length() < PASSWORD_MIN_LENGTH) {
			etPassword.setError(getString(R.string.error_invalid_password));
			focusView = etPassword;
			cancel = true;
		}
		
		// Check for valid password repeat
		if (!password.equals(passwordRepeat)) {
			etPasswordRepeat.setError(getString(R.string.error_incorrect_password_repeat));
			focusView = etPasswordRepeat;
			cancel = true;
		}
		
		//Check for FirstName and LastName
		if(TextUtils.isEmpty(firstName))
		{
			etFirstName.setError(getString(R.string.error_field_required));
			focusView = etFirstName;
			cancel = true;
		}
		if(TextUtils.isEmpty(lastName))
		{
			etLastName.setError(getString(R.string.error_field_required));
			focusView = etLastName;
			cancel = true;
		}
		
		if(!TextUtils.isEmpty(phone))
		{
			if(!phone.matches(phoneRegex))
			{
				etPhone.setError(getString(R.string.error_phone_format));
				focusView = etPhone;
				cancel = true;
			}
		}

		if (cancel) {
			// There was an error; don't attempt login and focus the first
			// form field with an error.
			focusView.requestFocus();
		} else {
			SharedPreferences pref = getSharedPreferences("Settings", MODE_PRIVATE);
			SharedPreferences.Editor editor = pref.edit();			
			editor.putBoolean("keepLogin", signMeIn.isChecked());
			editor.commit();
			
			user = new ParseUser();
			user.setEmail(email);
			user.setUsername(email);
			user.setPassword(password);			
			
			ParseObject metaData = new ParseObject("Metadata");
			metaData.put("FirstName", firstName);
			metaData.put("LastName", lastName);
			if(!TextUtils.isEmpty(phone))
				metaData.put("Phone", phone);
			
			Business.NewUser(user, metaData, this);
			
			showProgress(true);
			//mAuthTask = new UserLoginTask();
			//mAuthTask.execute((Void) null);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.sign_up, menu);
		return true;
	}
	
	public void signUpSuccessfull()
	{
		Log.d(DebugLoginTag, "Sign up successfull");
		SharedPreferences pref = getSharedPreferences("Settings", MODE_PRIVATE);
		boolean keep_login = pref.getBoolean("Settings", false);
		if(!keep_login && ParseUser.getCurrentUser() != null)
		{
			ParseUser.logOut();
		}
		
		showProgress(false);
		finish();//go back to first activity
		//do other UI stuff;
	}
	
	public void signUpFailed(String msg)
	{
		Log.d(DebugLoginTag, "Sign up failed");
		Log.d(DebugLoginTag, msg);
		user = null;
		showProgress(false);
		etEmail.setError(getString(R.string.error_username_already_taken));
		etEmail.requestFocus();
		//do other UI stuff;
	}
	
	@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
	private void showProgress(final boolean show) {
		// On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
		// for very easy animations. If available, use these APIs to fade-in
		// the progress spinner.
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
			int shortAnimTime = getResources().getInteger(
					android.R.integer.config_shortAnimTime);

			vSignupStatus.setVisibility(View.VISIBLE);
			vSignupStatus.animate().setDuration(shortAnimTime)
					.alpha(show ? 1 : 0)
					.setListener(new AnimatorListenerAdapter() {
						@Override
						public void onAnimationEnd(Animator animation) {
							vSignupStatus.setVisibility(show ? View.VISIBLE
									: View.GONE);
						}
					});

			vSignupForm.setVisibility(View.VISIBLE);
			vSignupForm.animate().setDuration(shortAnimTime)
					.alpha(show ? 0 : 1)
					.setListener(new AnimatorListenerAdapter() {
						@Override
						public void onAnimationEnd(Animator animation) {
							vSignupForm.setVisibility(show ? View.GONE
									: View.VISIBLE);
						}
					});
		} else {
			// The ViewPropertyAnimator APIs are not available, so simply show
			// and hide the relevant UI components.
			vSignupStatus.setVisibility(show ? View.VISIBLE : View.GONE);
			vSignupForm.setVisibility(show ? View.GONE : View.VISIBLE);
		}
	}

}
