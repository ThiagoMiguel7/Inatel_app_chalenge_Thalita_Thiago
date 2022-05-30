package com.example.inatel_app_chalenge;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.inatel_app_chalenge.databinding.ActivityPlanDetailsBinding;

public class PlanDetailsActivity extends AppCompatActivity {

    private TextView ispTextView, dataCapacityTextView, downloadSpeedTextView, uploadSpeedTextView,
            descriptionTextView, pricePerMonthTextView, typeOfInternetTextView;
    private Button btnInstaller;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plan_details);

        ispTextView = findViewById(R.id.ispTextView);
        dataCapacityTextView = findViewById(R.id.dataCapacityTextView);
        downloadSpeedTextView = findViewById(R.id.downloadSpeedTextView);
        uploadSpeedTextView = findViewById(R.id.uploadSpeedTextView);
        descriptionTextView = findViewById(R.id.descriptionTextView);
        pricePerMonthTextView = findViewById(R.id.pricePerMonthTextView);
        typeOfInternetTextView = findViewById(R.id.typeOfInternetTextView);
        btnInstaller = findViewById(R.id.btnInstaller);


        Plan plan = (Plan) getIntent().getSerializableExtra("plan");
        ispTextView.setText(plan.isp);
        dataCapacityTextView.setText(Double.toString(plan.data_capacity));
        downloadSpeedTextView.setText(Double.toString(plan.download_speed));
        uploadSpeedTextView.setText(Double.toString(plan.upload_speed));
        descriptionTextView.setText(plan.description);
        pricePerMonthTextView.setText(Double.toString(plan.price_per_month));
        typeOfInternetTextView.setText(plan.type_of_internet);

        btnInstaller.setOnClickListener(view -> {
            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra("plan", plan);
            startActivity(intent);
        });
    }
}