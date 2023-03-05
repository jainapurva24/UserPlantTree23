package com.weare.plantree.Buyers;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.weare.plantree.Model.ProductsModal;
import com.weare.plantree.ProductAdapter;
import com.weare.plantree.R;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class SeedsActivity extends AppCompatActivity {

    private DatabaseReference mReference;
    private RecyclerView mRecyclerView;
    ArrayList<ProductsModal> list = new ArrayList<>();
    ProductAdapter adapter;
    EditText search;
    Button search_btn;
    String input;
    ArrayList<ProductsModal> getList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seeds);

        mRecyclerView = findViewById(R.id.seeds_recycler_view);
        mReference= FirebaseDatabase.getInstance().getReference().child("Products");
        search = findViewById(R.id.search_seeds);
        search_btn = findViewById(R.id.search_seeds_btn);

        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                SharedPreferences preferences = getSharedPreferences("Search",MODE_PRIVATE);
                Gson gson = new Gson();
                String listData = preferences.getString("List",null);
                Type type = new TypeToken<ArrayList<ProductsModal>>(){}.getType();
                getList = gson.fromJson(listData,type);
                ArrayList<ProductsModal> searchList = new ArrayList<>();

                for (ProductsModal modal : getList){
                    Log.d("TAG",modal.getPname().toString());
                    if (modal.getPname().contains(s)){
                        searchList.add(modal);
                    }
                }
                adapter = new ProductAdapter(searchList,SeedsActivity.this);
                mRecyclerView.setLayoutManager(new LinearLayoutManager(SeedsActivity.this));
                mRecyclerView.setAdapter(adapter);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }


    @Override
    protected void onStart() {
        super.onStart();

        Query query = mReference.orderByChild("category").equalTo("Seeds");
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot data : snapshot.getChildren()){
                    String name = data.child("pname").getValue().toString();
                    String descr = data.child("description").getValue().toString();
                    String img = data.child("image").getValue().toString();
                    String price = data.child("price").getValue().toString();

                    ProductsModal modal = new ProductsModal();
                    modal.setPname(name);
                    modal.setPrice(price);
                    modal.setDescription(descr);
                    modal.setImage(img);

                    list.add(modal);
                }

                SharedPreferences preferences = getSharedPreferences("Search",MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                Gson gson = new Gson();
                String json = gson.toJson(list);
                editor.putString("List",json);
                editor.apply();

                adapter = new ProductAdapter(list, SeedsActivity.this);
                mRecyclerView.setLayoutManager(new LinearLayoutManager(SeedsActivity.this));
                mRecyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}