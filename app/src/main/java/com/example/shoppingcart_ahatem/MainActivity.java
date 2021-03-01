package com.example.shoppingcart_ahatem;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Spinner;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import static java.security.AccessController.getContext;

public class MainActivity extends AppCompatActivity {
    Spinner spinner;
    RecyclerView rec_view;
    static FloatingActionButton f_btn;
    ImageButton img_btn;
    List<ShoppingItem> spinnerItems;
    static List<ShoppingItem> shoppingItems;
    RecyclerAdapter adapter;
    public static final String DATA_KEY = "DATA_KEY";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewIds();
        f_btn.setVisibility(View.INVISIBLE);
        manageListeners();
        setupSpinner();
        setupRecycler();
    }

    private void findViewIds() {
        spinner = findViewById(R.id.spinner);
        rec_view = findViewById(R.id.rec_view);
        img_btn = findViewById(R.id.img_btn);
        f_btn = findViewById(R.id.f_btn);
    }

    private void setupSpinner() {
        spinnerItems = new ArrayList<>();
        ShoppingItem item1 = new ShoppingItem("Apple", 10);
        ShoppingItem item2 = new ShoppingItem("Orange", 5);
        ShoppingItem item3 = new ShoppingItem("Banana", 15);
        ShoppingItem item4 = new ShoppingItem("Mango", 20);
        spinnerItems.add(item1);
        spinnerItems.add(item2);
        spinnerItems.add(item3);
        spinnerItems.add(item4);
        SpinnerAdapter spinnerAdapter = new SpinnerAdapter(this,
                R.layout.spinner_item,
                spinnerItems
        );
        spinner.setAdapter(spinnerAdapter);
    }

    private void manageListeners() {
        img_btn.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //get from spinner
                        ShoppingItem item = (ShoppingItem) spinner.getSelectedItem();
                        //push to recyclerView
                        shoppingItems.add(item);
                        adapter.notifyDataSetChanged();
                    }
                }
        );
        f_btn.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(MainActivity.this, ReciteActivity.class);
                        ShoppingItemWrapper shoppingItemWrapper = new ShoppingItemWrapper();
                        shoppingItemWrapper.shoppingItems = MainActivity.this.shoppingItems;
                        intent.putExtra(DATA_KEY, shoppingItemWrapper);
                        startActivityForResult(intent, 1);
                    }
                }
        );
    }

    private void setupRecycler() {
        shoppingItems = new ArrayList<>();
        adapter = new RecyclerAdapter(shoppingItems);
        rec_view.setHasFixedSize(true);
        rec_view.setLayoutManager(new LinearLayoutManager(this));
        rec_view.setAdapter(adapter);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        shoppingItems.clear();
        adapter.notifyDataSetChanged();
        f_btn.setVisibility(View.INVISIBLE);
        createNotificationChannel();
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

    //ToControlFloatingButton
    static void controlFloatActionButton() {
        if (shoppingItems.size() > 0) {
            f_btn.setVisibility(View.VISIBLE);
        } else {
            f_btn.setVisibility(View.INVISIBLE);
        }
    }

}