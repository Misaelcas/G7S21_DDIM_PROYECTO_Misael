package com.example.g7s21_ddim_proyecto_misael;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

public class InicioActivity extends AppCompatActivity {

    private Spinner snombres;
    private ImageView lblfotos;
    private Button btnrecargar;
    private List<String> nombres = new ArrayList<>();
    private List<String> urls = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicio);

        snombres = findViewById(R.id.snombres);
        lblfotos = findViewById(R.id.lblfotos);
        btnrecargar = findViewById(R.id.btnrecargar);


        String url = "https://sheets.googleapis.com/v4/spreadsheets/1VG3rCIyHB4h9CrJzVc9oG_tMXV2BOaNxg0cJYB9zg6M/values/'ci'!A1:B?key=AIzaSyCdQGxcM_2RQNisQs15hk8yMWSlA5iOj48";

        loadSheetData(url);


        btnrecargar.setOnClickListener(v -> loadSheetData(url));
    }

    private void loadSheetData(String url) {
        RequestQueue queue = Volley.newRequestQueue(this);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                response -> {
                    nombres.clear();
                    urls.clear();

                    try {
                        JSONArray rows = response.getJSONArray("values");
                        for (int i = 0; i < rows.length(); i++) {
                            JSONArray row = rows.getJSONArray(i);
                            nombres.add(row.getString(0));
                            urls.add(row.getString(1));
                        }
                        populateSpinner();
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(InicioActivity.this, "Error al procesar los datos", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> {
                    Toast.makeText(InicioActivity.this, "Error al cargar los datos: " + error.getMessage(), Toast.LENGTH_LONG).show();
                }
        );

        queue.add(jsonObjectRequest);
    }

    private void populateSpinner() {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, nombres);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        snombres.setAdapter(adapter);

        snombres.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedUrl = urls.get(position);
                loadImage(selectedUrl);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private void loadImage(String url) {
        Glide.with(this).load(url).into(lblfotos);
    }
}