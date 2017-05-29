package com.avans.easypay;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Image;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputFilter;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.avans.easypay.DomainModel.Balance;
import com.avans.easypay.InputFilter.DecimalDigitsInputFilter;
import com.avans.easypay.SQLite.BalanceDAO;
import com.avans.easypay.SQLite.DAOFactory;
import com.avans.easypay.SQLite.SQLiteDAOFactory;

import org.w3c.dom.Text;

import java.util.Date;

import es.dmoral.toasty.Toasty;

public class IncreaseBalanceActivity extends AppCompatActivity {

    private DAOFactory factory;
    private BalanceDAO balanceDAO;

    private ImageView home;
    private TextView currentBalance;
    private EditText editText;

    private Balance b;

    EasyPayAPIPUTConnector put;

    SharedPreferences customer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_balance_increase);

        customer = getSharedPreferences("CUSTOMER", Context.MODE_PRIVATE);

        editText = (EditText) findViewById(R.id.editText2);
        editText.setFilters(new InputFilter[] {new DecimalDigitsInputFilter(2)});

        factory = new SQLiteDAOFactory(getApplicationContext());
        balanceDAO = factory.createBalanceDAO();

        currentBalance = (TextView) findViewById(R.id.currentBalance);

        if(balanceDAO.selectData().size() == 0){
            Log.i("BALANCE", "No balance yet");
            currentBalance.setText(getResources().getString(R.string.balance_wallet)
                    + " " + "€0.00");
        }
        else{
            b = balanceDAO.selectData().get(balanceDAO.selectData().size() - 1);
            currentBalance.setText(getResources().getString(R.string.balance_wallet)
                    + " " + "€" + String.format("%.2f", b.getAmount()));
        }

        //Setting up the toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        home = (ImageView) findViewById(R.id.home);
        home.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view){
                Intent intent = new Intent(IncreaseBalanceActivity.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
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

    //OnClick
    public void confirmButton(View v) {
        Log.i("TEXTENTERED", editText.getText().toString());

        if(editText.getText().toString().equals("") || editText.getText().toString().equals(".")){
            Toasty.error(this, getResources().getString(R.string.isnull), Toast.LENGTH_SHORT).show();
        }else{

            float toAdd = Float.parseFloat(editText.getText().toString());

            Log.i("MONEYTOADD", editText.getText().toString());

            float beforeAmount;

            if (balanceDAO.selectData().size() == 0){
                beforeAmount = 0f;
            }else {
                beforeAmount = balanceDAO.selectData().get(balanceDAO.selectData().size() - 1).getAmount();
            }

            float afterAmount = beforeAmount + toAdd;

            if(afterAmount <= 150f)
            {
                Balance afterBalance = new Balance(afterAmount, new Date());
                put = new EasyPayAPIPUTConnector();
                put.execute("https://easypayserver.herokuapp.com/api/klant/id=" + customer.getInt("ID", 0)
                        + "/saldo=" + afterBalance.getAmount() * 100 + "&datum=" + afterBalance.getTimeLog());
                balanceDAO.insertData(afterBalance);
                finish();
            }
            else{
                Toasty.error(this, getResources().getString(R.string.over150), Toast.LENGTH_SHORT).show();
            }
        }

    }
}
