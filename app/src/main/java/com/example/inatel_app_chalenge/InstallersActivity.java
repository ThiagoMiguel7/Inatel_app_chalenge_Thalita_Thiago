package com.example.inatel_app_chalenge;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class InstallersActivity extends AppCompatActivity {

    private TextView nameTextView, ratingTextView, pricePerKmTextView;

    private Button btnSendMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_installers);

        nameTextView = findViewById(R.id.nameTextView);
        ratingTextView = findViewById(R.id.ratingTextView);
        pricePerKmTextView = findViewById(R.id.pricePerKmTextView);

        Installer installer = (Installer) getIntent().getSerializableExtra("installer");

        nameTextView.setText(installer.name);
        ratingTextView.setText(Integer.toString(installer.rating));
        pricePerKmTextView.setText(Integer.toString(installer.pricePerKm));

        btnSendMessage = findViewById(R.id.button);
        btnSendMessage.setOnClickListener(view -> {
            Toast.makeText(getApplicationContext(), "Sua mensagem foi enviada ao Instalador!", Toast.LENGTH_LONG).show();
        });
    }
}