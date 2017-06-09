package com.avans.easypay;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.avans.easypay.DomainModel.Order;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Felix on 9-3-2017.
 */

public class OrderOverviewAdapter extends ArrayAdapter<Order> {

    private SharedPreferences locationPref;
    private CheckBox checkbox;
    private ImageView xCheckbox;

    public OrderOverviewAdapter(Context context, ArrayList<Order> orders) {
        super(context, 0, orders);
        locationPref = context.getSharedPreferences(LoginActivity.PREFERENCELOCATION, context.MODE_PRIVATE);
    }

    @Override public View getView(int position, View convertView, ViewGroup parent) {
        Order order = getItem(position);

        long dateInMillis = order.getDate().getTime() + new Double(2.16e+7).longValue();
        Date date = new Date(dateInMillis);

        //create an order item
        if(convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.order_overview_list_item, parent, false);
        }

        //initialise xml elements
        TextView orderNumberOutput = (TextView) convertView.findViewById(R.id.order_number_textview);
        TextView orderLocationOutput = (TextView) convertView.findViewById(R.id.order_location_textview);
        TextView orderDateOutput = (TextView) convertView.findViewById(R.id.order_date_textview);
        checkbox = (CheckBox) convertView.findViewById(R.id.status_checkbox);
        xCheckbox = (ImageView) convertView.findViewById(R.id.status_imageview);

        //add content to the xml elements
        orderNumberOutput.setText(order.getOrderNumber()+"");
        orderLocationOutput.setText(locationPref.getString(order.getLocation(), "Geen Locatie"));
        orderDateOutput.setText(formatDate(date));
        checkStatusForCheckbox(order.getStatus());

        return convertView;
    }

    public void checkStatusForCheckbox(String status) {
        switch (status) {
            case "PAID":
                checkbox.setChecked(true);
                checkbox.setVisibility(View.VISIBLE);
                break;
            case "WAITING":
                checkbox.setVisibility(View.VISIBLE);
                checkbox.setChecked(false);
                break;
            default:
                xCheckbox.setVisibility(View.VISIBLE);
                checkbox.setVisibility(View.INVISIBLE);
                break;
        }
    }

    public String formatDate(Date date) {
        //format the date
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm - dd/MM/yyyy");
        return sdf.format(date);
    }
}
