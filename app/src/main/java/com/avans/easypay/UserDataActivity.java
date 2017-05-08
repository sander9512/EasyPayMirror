package com.avans.easypay;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.avans.easypay.DomainModel.Balance;
import com.avans.easypay.SQLite.BalanceDAO;
import com.avans.easypay.SQLite.DAOFactory;
import com.avans.easypay.SQLite.SQLiteDAOFactory;

public class UserDataActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView firstnameText, lastnameText, usernameText, passwordText;
    private EditText emailInput, bankNumberInput, newPassInput1, newPassInput2;
    private Button passwordEditBtn, emailEditBtn, confirmPassBtn, cancelPassBtn;
    private ImageView bankNumberEditBtn, home;

    private LinearLayout newPassLayout;

    private Boolean passEditable = false;
    private Boolean emailEditable = false;
    private Boolean bankNumberEditable = false;

    private String currentEmail, currentBankNumber;

    private DAOFactory factory;
    private BalanceDAO balanceDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_data);

        factory = new SQLiteDAOFactory(getApplicationContext());
        balanceDAO = factory.createBalanceDAO();

        //Setting up the toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        home = (ImageView) findViewById(R.id.home);
        home.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view){
                Intent intent = new Intent(UserDataActivity.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });

        //initialise xml elements
        firstnameText = (TextView) findViewById(R.id.firstname_textview);
        lastnameText = (TextView) findViewById(R.id.lastname_textview);
        usernameText = (TextView) findViewById(R.id.username_textview);
        passwordText = (TextView) findViewById(R.id.password_textview);
        emailInput = (EditText) findViewById(R.id.email_edittext);
        bankNumberInput = (EditText) findViewById(R.id.banknumber_edittext);

        passwordEditBtn = (Button) findViewById(R.id.password_edit_btn);
        emailEditBtn = (Button) findViewById(R.id.email_edit_btn);
        bankNumberEditBtn = (ImageView) findViewById(R.id.banknumber_edit_btn);

        newPassInput1 = (EditText) findViewById(R.id.new_password_edittext_1);
        newPassInput2 = (EditText) findViewById(R.id.new_password_edittext_2);
        confirmPassBtn = (Button) findViewById(R.id.confirm_password_btn);
        cancelPassBtn = (Button) findViewById(R.id.cancel_password_btn);

        newPassLayout = (LinearLayout) findViewById(R.id.new_password_layout);

        //add listeners to elements
        passwordEditBtn.setOnClickListener(this);
        emailEditBtn.setOnClickListener(this);
        bankNumberEditBtn.setOnClickListener(this);

        confirmPassBtn.setOnClickListener(this);
        cancelPassBtn.setOnClickListener(this);

        //start with uneditable EditTexts
        emailInput.setEnabled(false);
        bankNumberInput.setEnabled(false);

        //get current user data
        currentEmail = emailInput.getText().toString();
        currentBankNumber = bankNumberInput.getText().toString();

        //hide keyboard on start
        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
        );
    }

    @Override
    protected void onResume(){
        super.onResume();

        //Setting balance in toolbar
        Balance b = balanceDAO.selectData().get(balanceDAO.selectData().size() - 1);
        Toolbar toolbar = (Toolbar) findViewById(R.id.my_toolbar);
        TextView balanceToolbar = (TextView) toolbar.findViewById(R.id.toolbar_balance);
        balanceToolbar.setText("€" + String.format("%.2f", b.getAmount()));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.password_edit_btn:
                //set password editable
                if (!passEditable) {
                    passwordEditBtn.setVisibility(View.INVISIBLE);
                    newPassLayout.setVisibility(View.VISIBLE);
                    newPassInput1.requestFocus();
                    passEditable = true;
                }
                break;

            case R.id.confirm_password_btn:
                //if passwords are equal...
                if (newPassInput1.getText().toString().trim().equals(newPassInput2.getText().toString().trim())) {

                    //make a ***** placeholder for password TextView
                    int passLength = newPassInput1.getText().toString().trim().length();
                    String passPlaceholder = "";
                    while (passLength > 0) {
                        passPlaceholder += "*";
                        passLength--;
                    }
                    passwordText.setText(passPlaceholder);

                    //restore default values
                    newPassLayout.setVisibility(View.INVISIBLE);
                    passwordEditBtn.setVisibility(View.VISIBLE);
                    newPassInput1.setText("");
                    newPassInput2.setText("");
                    passEditable = false;
                    Toast.makeText(this, "Wachtwoord gewijzigd.", Toast.LENGTH_LONG).show();

                    //if an input is empty...
                } else if (newPassInput1.getText().toString().equals("") ||
                        newPassInput2.getText().toString().trim().equals("")) {
                    Toast.makeText(this, "Een of meer velden zijn niet ingevuld.", Toast.LENGTH_SHORT).show();

                    //else... (passwords are unequal)
                } else {
                    Toast.makeText(this, "Wachtwoorden zijn ongelijk.", Toast.LENGTH_SHORT).show();
                }
                break;

            case R.id.cancel_password_btn:
                //restore default values
                newPassLayout.setVisibility(View.INVISIBLE);
                passwordEditBtn.setVisibility(View.VISIBLE);
                newPassInput1.setText("");
                newPassInput2.setText("");
                passEditable = false;
                break;

            case R.id.email_edit_btn:
                //set email editable
                if (!emailEditable) {
                    emailInput.setEnabled(true);
                    emailInput.requestFocus();
                    emailEditBtn.setBackgroundResource(R.drawable.ic_check);
                    emailEditable = true;

                    //confirm changes, set email uneditable
                } else {
                    emailInput.setEnabled(false);
                    emailInput.clearFocus();
                    emailEditBtn.setBackgroundResource(R.drawable.ic_data_editable);
                    emailEditable = false;
                    if (!currentEmail.equals(emailInput.getText().toString().trim())) {
                        Toast.makeText(this, "Email gewijzigd.", Toast.LENGTH_LONG).show();
                        currentEmail = emailInput.getText().toString().trim();
                    }
                }
                break;

            case R.id.banknumber_edit_btn:
                //set banknumber editable
                if (!bankNumberEditable) {
                    bankNumberInput.setEnabled(true);
                    bankNumberInput.requestFocus();
                    bankNumberEditBtn.setBackgroundResource(R.drawable.ic_check);
                    bankNumberEditable = true;

                    //confirm changes, set banknumber uneditable
                } else {
                    bankNumberInput.setEnabled(false);
                    bankNumberInput.clearFocus();
                    bankNumberEditBtn.setBackgroundResource(R.drawable.ic_data_editable);
                    bankNumberEditable = false;
                    if (!currentBankNumber.equals(bankNumberInput.getText().toString().trim())) {
                        Toast.makeText(this, "Bankrekeningnummer gewijzigd.", Toast.LENGTH_SHORT).show();
                        currentBankNumber = bankNumberInput.getText().toString().trim();
                    }
                }
                break;
        }
    }
}
