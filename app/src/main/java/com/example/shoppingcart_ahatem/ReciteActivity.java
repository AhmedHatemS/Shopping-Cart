package com.example.shoppingcart_ahatem;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class ReciteActivity extends AppCompatActivity {
    TextView tv_reciteTop, tv_name, tv_quantity, tv_price, tv_reciteBottom;
    Button btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recite);
        tv_reciteTop = findViewById(R.id.tv_reciteTop);
        tv_name = findViewById(R.id.tv_name);
        tv_quantity = findViewById(R.id.tv_quantity);
        tv_price = findViewById(R.id.tv_price);
        tv_reciteBottom = findViewById(R.id.tv_reciteBottom);
        btn = findViewById(R.id.button);
        ShoppingItemWrapper shoppingItemWrapper = (ShoppingItemWrapper)
                getIntent().getSerializableExtra(MainActivity.DATA_KEY);
        List<ShoppingItem> shoppingItems = shoppingItemWrapper.shoppingItems;
        Map<ShoppingItem, Integer> shoppingMap = new HashMap<>();
        for (int i = 0; i < shoppingItems.size(); i++) {
            ShoppingItem item = shoppingItems.get(i);
            Integer number = shoppingMap.get(item);
            if (number == null)
                number = 1;
            else
                number++;
            shoppingMap.put(shoppingItems.get(i), number);
        }
        String name = "name\n---------\n", QTY = "QTY\n------\n", price = "price\n---------\n";
        int totalPrice = 0;
        for (Map.Entry<ShoppingItem, Integer> entry : shoppingMap.entrySet()) {
            ShoppingItem item = entry.getKey();
            int quantity = entry.getValue();
            name += item.itemName + "\n";
            QTY += quantity + "\n";
            price += (quantity * item.itemPrice) + "\n";
            totalPrice += item.itemPrice * quantity;
        }
        String currentDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
        String currentTime = new SimpleDateFormat("HH:mm", Locale.getDefault()).format(new Date());
        tv_reciteTop.setText("Shopping cart recite\n" +
                "REG " + currentDate + " " + currentTime + "\n");
        tv_name.setText(name);
        tv_quantity.setText(QTY);
        tv_price.setText(price);
        tv_reciteBottom.setText("Total price is " + totalPrice + " L.E,\n" +
                "There are no refunds and exchanges,\n" +
                "Thanks for your trust in us.");
        btn.setText("pay and print?");
        btn.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        shoppingItems.clear();
                        Toast.makeText(ReciteActivity.this, "Payment done", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent();
                        setResult(RESULT_OK, intent);
                        finish();
                    }
                }
        );

    }
}