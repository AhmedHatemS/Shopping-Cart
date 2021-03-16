package com.example.shoppingcart_ahatem;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class ReciteActivity extends AppCompatActivity {
    TextView tv_reciteTop, tv_name, tv_quantity, tv_price, tv_reciteBottom;
    Button btn;
    ShoppingItemWrapper shoppingItemWrapper;
    List<ShoppingItem> shoppingItems;
    String name = "name\n---------\n", QTY = "QTY\n------\n", price = "price\n---------\n";
    int totalPrice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recite);
        findViewIds();
        manageItems();
        setViewsText();
        manageListeners();
    }

    private void manageListeners() {
        btn.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        shoppingItems.clear();
                        Toast.makeText(ReciteActivity.this, "Payment done", Toast.LENGTH_LONG).show();
                        createNotificationChannel();
                        Intent intent = new Intent();
                        setResult(RESULT_OK, intent);
                        finish();
                    }
                }
        );
    }

    private void setViewsText() {
        String currentDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
        String currentTime = new SimpleDateFormat("HH:mm", Locale.getDefault()).format(new Date());
        tv_reciteTop.setText("Shopping cart recite\n" +
                "REG " + currentDate + " " + currentTime);
        tv_name.setText(name);
        tv_quantity.setText(QTY);
        tv_price.setText(price);
        tv_reciteBottom.setText("Total price is " + totalPrice + " L.E,\n" +
                "There are no refunds and exchanges,\n" +
                "Thanks for your trust in us.");
        btn.setText("pay and print?");
    }

    private void manageItems() {
        shoppingItemWrapper = (ShoppingItemWrapper)
                getIntent().getSerializableExtra(MainActivity.DATA_KEY);
        shoppingItems = shoppingItemWrapper.shoppingItems;
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
        for (Map.Entry<ShoppingItem, Integer> entry : shoppingMap.entrySet()) {
            ShoppingItem item = entry.getKey();
            int quantity = entry.getValue();
            name += item.itemName + "\n";
            QTY += quantity + "\n";
            price += (quantity * item.itemPrice) + "\n";
            totalPrice += item.itemPrice * quantity;
        }
    }

    private void findViewIds() {
        tv_reciteTop = findViewById(R.id.tv_reciteTop);
        tv_name = findViewById(R.id.tv_name);
        tv_quantity = findViewById(R.id.tv_quantity);
        tv_price = findViewById(R.id.tv_price);
        tv_reciteBottom = findViewById(R.id.tv_reciteBottom);
        btn = findViewById(R.id.button);
    }
    protected void createNotificationChannel() {
        int NOTIFICATION_ID = 234;
        NotificationManager notificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
        String CHANNEL_ID = "my_channel_01";
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            CharSequence name = "my_channel";
            String Description = "This is my channel";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel mChannel = new NotificationChannel(CHANNEL_ID, name, importance);
            mChannel.setDescription(Description);
            mChannel.enableLights(true);
            mChannel.setLightColor(Color.RED);
            mChannel.enableVibration(true);
            mChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
            mChannel.setShowBadge(false);
            notificationManager.createNotificationChannel(mChannel);
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.app_icon)
                .setContentTitle("New message")
                .setContentText("Payment is done, thank you!");

        Intent resultIntent = new Intent(this, MainActivity.class);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addParentStack(MainActivity.class);
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(resultPendingIntent);
        notificationManager.notify(NOTIFICATION_ID, builder.build());
    }
}