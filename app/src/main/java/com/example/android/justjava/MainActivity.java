// Add your package below

package com.example.android.justjava;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import java.text.NumberFormat;

/**
 * This app displays an order form to order coffee.
 */
public class MainActivity extends AppCompatActivity {

    int numCoffees = 0;
    int coffeePrice = 5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    /**
     * This method is called when the order button is clicked.
     */
    public void submitOrder(View view)
    {
        CheckBox whippedCreamBox = (CheckBox) findViewById(R.id.whippedCream_checkBox);
        CheckBox chocolateBox = (CheckBox) findViewById(R.id.chocolate_checkBox);
        boolean hasWhippedCream = whippedCreamBox.isChecked();
        boolean hasChocolate = chocolateBox.isChecked();
        TextView nameText = (TextView) findViewById(R.id.name_editText);
        String customerName = nameText.getText().toString();
        String emailBody = createOrderSummary(hasWhippedCream, hasChocolate, customerName);

        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:")); // only email apps should handle this
        intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.email_subject, customerName));
        intent.putExtra(Intent.EXTRA_TEXT, emailBody);
        if (intent.resolveActivity(getPackageManager()) != null)
        {
            startActivity(intent);
        }
    }

    /**
     * This method displays the given quantity value on the screen.
     */
    private void displayQuantity(int number) {
        TextView quantityTextView = (TextView) findViewById(R.id.quantity_text_view);
        quantityTextView.setText("" + number);
    }

    public void increment(View view) {
        numCoffees++;
        displayQuantity(numCoffees);
    }

    public void decrement(View view) {
        if (numCoffees > 1) {
            numCoffees--;
            displayQuantity(numCoffees);
        }
        else
        {
            Toast.makeText(this, getString(R.string.toast_message), Toast.LENGTH_SHORT).show();
            return;
        }
    }

    private int calculatePrice(int basePrice)
    {
        int totalPrice = numCoffees * basePrice;
        return totalPrice;
    }

    /**
     * This method displays the given text on the screen.
     */
    private void displayMessage(String message) {
        TextView orderSummaryTextView = (TextView) findViewById(R.id.order_summary_text_view);
        orderSummaryTextView.setText(message);
    }

    private String createOrderSummary(boolean hasWhippedCream, boolean hasChoclate, String customerName)
    {
        int basePrice = coffeePrice;
        String priceMessage = getString(R.string.order_summary_name, customerName);
        String toppings = "\n" + getString(R.string.order_summary_toppings);
        priceMessage = priceMessage + "\n" + getString(R.string.order_summary_quantity) +  numCoffees;

        if(hasWhippedCream && !hasChoclate)
        {
            toppings =  toppings + getString(R.string.whipped_cream);
            basePrice = basePrice + 1;
        }
        else if(hasWhippedCream && hasChoclate)
        {
            toppings = toppings +  getString(R.string.whipped_cream_chocolate);
            basePrice = basePrice + 3;
        }
        else if(!hasWhippedCream && hasChoclate)
        {
            toppings = toppings + getString(R.string.chocolate);
            basePrice = basePrice + 2;
        }
        else
        {
            toppings = toppings + getString(R.string.none);
        }

        priceMessage = priceMessage + toppings;
        priceMessage = priceMessage + "\n" + getString(R.string.total) + calculatePrice(basePrice);
        priceMessage = priceMessage + "\n" + getString(R.string.thank_you);
        return priceMessage;
    }
}