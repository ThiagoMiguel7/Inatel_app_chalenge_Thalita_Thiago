package com.example.inatel_app_chalenge;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PlansActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener, PlansListAdapterListener {

    private Spinner spinner;
    private ProgressBar progressBar;
    private PlansListAdapter plansListAdapter;
    private RecyclerView recyclerView;
    private Call<List<Plan>> call = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plans);

        recyclerView = findViewById(R.id.recyclerView);
        spinner = findViewById(R.id.spinner1);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,R.array.Estados, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);

        progressBar = findViewById(R.id.progressBar);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        plansListAdapter = new PlansListAdapter(this);
        recyclerView.setAdapter(plansListAdapter);
        // Configurando um dividr entre linhas, para uma melhor visualização.
        recyclerView.addItemDecoration(
                new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
    }

    private void getPlans(String currentState) {
        if(call != null) call.cancel();
        call = RetrofitClient.getInstance().getMyApi().getPlans(currentState);
        progressBar.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);
        call.enqueue(new Callback<List<Plan>>() {
            @Override
            public void onResponse(Call<List<Plan>> call, Response<List<Plan>> response) {
                progressBar.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);
                List<Plan> plans = response.body();
                plansListAdapter.setPlans(plans);
                plansListAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<List<Plan>> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(getApplicationContext(), "An error has occured", Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String text = parent.getItemAtPosition(position).toString();
        getPlans(text);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onClick(Plan plan) {
        Intent intent = new Intent(this, PlanDetailsActivity.class);
        intent.putExtra("plan", plan);
        startActivity(intent);
    }
}