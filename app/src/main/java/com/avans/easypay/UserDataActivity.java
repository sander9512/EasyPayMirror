package com.avans.easypay;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.avans.easypay.DomainModel.Balance;
import com.avans.easypay.DomainModel.Customer;
import com.avans.easypay.SQLite.BalanceDAO;
import com.avans.easypay.SQLite.DAOFactory;
import com.avans.easypay.SQLite.SQLiteDAOFactory;

import org.iban4j.IbanFormatException;
import org.iban4j.IbanUtil;
import org.iban4j.InvalidCheckDigitException;
import org.iban4j.UnsupportedCountryException;

import java.util.regex.Pattern;

import es.dmoral.toasty.Toasty;

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

    private EasyPayAPIPUTConnector putRequest;
    private EasyPayAPIDELETEConnector deleteRequest;

    Customer customer;

    SharedPreferences customerPref;
    SharedPreferences.Editor customerEdit;

    public static final String PREFRENCE = "CUSTOMER";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_data);


        customerPref = getSharedPreferences(PREFRENCE, Context.MODE_PRIVATE);
        customerEdit = customerPref.edit();

        customer = new Customer(customerPref.getInt("ID", 0),customerPref.getString("Username", "")
                , customerPref.getString("Password", ""), customerPref.getString("Email", "")
                , customerPref.getString("FirstName", ""), customerPref.getString("LastName", "")
                , customerPref.getString("Bank", ""));
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

        //Setting text with given customer
        firstnameText.setText(customer.getFirstname());
        lastnameText.setText(customer.getLastname());
        usernameText.setText(customer.getUsername());
        if (customer.getEmail().equals("null")){
            emailInput.setText("");
        } else{
            emailInput.setText(customer.getEmail());
        }
        if (customer.getBankAccountNumber().equals("null")){
            bankNumberInput.setText("");
        }else {
            bankNumberInput.setText(customer.getBankAccountNumber());
        }
        //Password *
        int passLength = customer.getPassword().trim().length();
        String passPlaceholder = "";
        while (passLength > 0) {
            passPlaceholder += "*";
            passLength--;
        } passwordText.setText(passPlaceholder);


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
        if (balanceDAO.selectData().size() == 0){
            Toolbar toolbar = (Toolbar) findViewById(R.id.my_toolbar);
            TextView balanceToolbar = (TextView) toolbar.findViewById(R.id.toolbar_balance);
            balanceToolbar.setText("€0.00");
        }else{
            Balance b = balanceDAO.selectData().get(balanceDAO.selectData().size() - 1);
            Toolbar toolbar = (Toolbar) findViewById(R.id.my_toolbar);
            TextView balanceToolbar = (TextView) toolbar.findViewById(R.id.toolbar_balance);
            balanceToolbar.setText("€" + String.format("%.2f", b.getAmount()));
        }
    }

    public void deleteAcc(View v){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Weet u zeker dat uw account verwijderd moet worden?")
                .setTitle("Verwijdering account");
        builder.setPositiveButton("Ja", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                deleteRequest = new EasyPayAPIDELETEConnector();
                deleteRequest.execute("https://easypayserver.herokuapp.com/api/klant/delete/" + customerPref.getInt("ID", 0));
                Toasty.success(UserDataActivity.this, "Account verwijderd", Toast.LENGTH_SHORT).show();
                finishAffinity();
                startActivity(intent);
            }
        });

        builder.setNegativeButton("Nee", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private boolean validEmail(String email) {
        Pattern pattern = Patterns.EMAIL_ADDRESS;
        return pattern.matcher(email).matches();
    }

    private boolean validIBAN(String iban){
        try{
            IbanUtil.validate(iban);
            Log.i("IBAN", "VALID IBAN");
            return true;
        }catch (IbanFormatException |
                InvalidCheckDigitException |
                UnsupportedCountryException e) {
            Log.i("IBAN", "UNVALID IBAN");
            return false;
        }
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
                    putRequest = new EasyPayAPIPUTConnector();
                    putRequest.execute("https://easypayserver.herokuapp.com/api/klant/id="
                            + customer.getCustomerId()+"/wachtwoord="+newPassInput1.getText());
                    customerEdit.putString("Password", newPassInput1.getText().toString());
                    customerEdit.commit();
                    passwordText.setText(passPlaceholder);

                    //restore default values
                    newPassLayout.setVisibility(View.INVISIBLE);
                    passwordEditBtn.setVisibility(View.VISIBLE);
                    newPassInput1.setText("");
                    newPassInput2.setText("");
                    passEditable = false;
                    Toasty.success(this, "Wachtwoord gewijzigd.", Toast.LENGTH_LONG).show();

                    //if an input is empty...
                } else if (newPassInput1.getText().toString().equals("") ||
                        newPassInput2.getText().toString().trim().equals("")) {
                    Toasty.error(this, "Een of meer velden zijn niet ingevuld.", Toast.LENGTH_SHORT).show();

                    //else... (passwords are unequal)
                } else {
                    Toasty.error(this, "Wachtwoorden zijn ongelijk.", Toast.LENGTH_SHORT).show();
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
                        if (validEmail(emailInput.getText().toString().trim())){
                            putRequest = new EasyPayAPIPUTConnector();
                            putRequest.execute("https://easypayserver.herokuapp.com/api/klant/id="
                                    + customer.getCustomerId()+"/email="+emailInput.getText());
                            customerEdit.putString("Email", emailInput.getText().toString());
                            customerEdit.commit();
                            Toasty.success(this, "Email gewijzigd.", Toast.LENGTH_LONG).show();
                            currentEmail = emailInput.getText().toString().trim();
                        }else {
                            Toasty.error(this, "Geen geldig email address", Toast.LENGTH_SHORT).show();
                            emailInput.setEnabled(true);
                            emailInput.requestFocus();
                            emailEditBtn.setBackgroundResource(R.drawable.ic_check);
                            emailEditable = true;
                            emailInput.setText(currentEmail);
                        }
                    } else {
                        Toasty.error(this, "Email is zelfde als huidig address", Toast.LENGTH_SHORT).show();
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
                        if (validIBAN(bankNumberInput.getText().toString().trim())){
                            putRequest = new EasyPayAPIPUTConnector();
                            putRequest.execute("https://easypayserver.herokuapp.com/api/klant/id="
                                    + customer.getCustomerId()+"/bank="+bankNumberInput.getText());
                            customerEdit.putString("Bank", bankNumberInput.getText().toString());
                            customerEdit.commit();
                            Toasty.success(this, "Bankrekeningnummer gewijzigd.", Toast.LENGTH_SHORT).show();
                            currentBankNumber = bankNumberInput.getText().toString().trim();
                        } else {
                            Toasty.error(this, "Geen geldig bankrekeningnummer", Toast.LENGTH_SHORT).show();
                            bankNumberInput.setEnabled(true);
                            bankNumberInput.requestFocus();
                            bankNumberEditBtn.setBackgroundResource(R.drawable.ic_check);
                            bankNumberEditable = true;
                            bankNumberInput.setText(currentBankNumber);
                        }

                    }
                }
                break;
        }
    }
}
