package com.avans.easypay;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputFilter;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.avans.easypay.DomainModel.Balance;
import com.avans.easypay.InputFilter.DecimalDigitsInputFilter;
import com.avans.easypay.SQLite.BalanceDAO;
import com.avans.easypay.SQLite.DAOFactory;
import com.avans.easypay.SQLite.SQLiteDAOFactory;

import org.w3c.dom.Text;

import java.util.Date;

public class IncreaseBalanceActivity extends AppCompatActivity {

    private DAOFactory factory;
    private BalanceDAO balanceDAO;

    private TextView currentBalance;
    private EditText editText;

    private Balance b;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_balance_increase);

        editText = (EditText) findViewById(R.id.editText2);
        editText.setFilters(new InputFilter[] {new DecimalDigitsInputFilter(2)});

        factory = new SQLiteDAOFactory(getApplicationContext());
        balanceDAO = factory.createBalanceDAO();
        b = balanceDAO.selectData().get(balanceDAO.selectData().size() - 1);

        //Setting up the toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        //Current balance
        currentBalance = (TextView) findViewById(R.id.currentBalance);
        currentBalance.setText(getResources().getString(R.string.balance_wallet)
                + " " + "€" + String.format("%.2f", b.getAmount()));

    }

    @Override
    protected void onResume(){
        super.onResume();
        b = balanceDAO.selectData().get(balanceDAO.selectData().size() - 1);


        //Setting balance in toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.my_toolbar);
        TextView balanceToolbar = (TextView) toolbar.findViewById(R.id.toolbar_balance);
        balanceToolbar.setText("€" + String.format("%.2f", b.getAmount()));
    }

    //OnClick
    public void confirmButton(View v) {
        Log.i("TEXTENTERED", editText.getText().toString());

        if(editText.getText().toString().equals("") || editText.getText().toString().equals(".")){
            Toast.makeText(this, getResources().getString(R.string.isnull), Toast.LENGTH_SHORT).show();
        }else{

            float toAdd = Float.parseFloat(editText.getText().toString());

            Log.i("MONEYTOADD", editText.getText().toString());

            float beforeAmount = balanceDAO.selectData().get(balanceDAO.selectData().size() - 1).getAmount();
            float afterAmount = beforeAmount + toAdd;

            if(afterAmount <= 150f)
            {
                balanceDAO.insertData(new Balance(afterAmount, new Date()));
                finish();
            }
            else{
                Toast.makeText(this, getResources().getString(R.string.over150), Toast.LENGTH_SHORT).show();
            }
        }

    }
}
