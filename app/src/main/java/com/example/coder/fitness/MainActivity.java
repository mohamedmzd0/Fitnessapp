package com.example.coder.fitness;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    ArrayList<FitnessModel> fitnessModelArrayList;
    DatabaseHelper db;
    SearchView search_view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        search_view = (SearchView) findViewById(R.id.search_view);
        search_view.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                new search().execute(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.isEmpty()) {
                    recyclerView.setAdapter(new adapter(fitnessModelArrayList));
                } else
                    new search().execute(newText);
                return true;
            }
        });
        recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(), 2));
        recyclerView.setHasFixedSize(false);
        fitnessModelArrayList = new ArrayList<>();
        db = new DatabaseHelper(getApplicationContext());
        SQLiteDatabase sqLiteDatabase = db.getReadableDatabase();
        if (new CheckNetwork(getApplicationContext()).isOnline())
            getFitnessData();

        else {
            Toast.makeText(this, R.string.offline, Toast.LENGTH_SHORT).show();
            fitnessModelArrayList = db.restoreworkouts();
            recyclerView.setAdapter(new adapter(fitnessModelArrayList));
        }
        findViewById(R.id.fab).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), ProgressActivity.class));
            }
        });
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("search", search_view.getQuery().toString());
    }


    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        search_view.setQuery(savedInstanceState.getString("search"), true);
    }

    public void getFitnessData() {
        FirebaseDatabase.getInstance().getReference().addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                if (dataSnapshot != null) {
                    FitnessModel model = dataSnapshot.getValue(FitnessModel.class);
                    fitnessModelArrayList.add(model);
                    db.insertWorkout(model.name, model.desc, model.played, model.url);
                }
                recyclerView.setAdapter(new adapter(fitnessModelArrayList));
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    class adapter extends RecyclerView.Adapter<viewholder> {
        ArrayList<FitnessModel> adapterList;

        public adapter(ArrayList<FitnessModel> adapterList) {
            this.adapterList = adapterList;
        }

        @Override
        public viewholder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.main_recycleview_raw, null);
            viewholder holder = new viewholder(view);
            return holder;
        }

        @Override
        public void onBindViewHolder(viewholder holder, final int position) {
            holder.name.setText(adapterList.get(position).name);
            holder.desc.setText(adapterList.get(position).desc);
            if (adapterList.get(position).played)
                holder.image.setImageResource(R.drawable.correct);
            holder.view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getApplicationContext(), Levels.class);
                    intent.putExtra("url", adapterList.get(position).url);
                    intent.putExtra("name", adapterList.get(position).name);
                    startActivity(intent);
                }
            });
        }

        @Override
        public int getItemCount() {
            return adapterList.size();
        }
    }

    class viewholder extends RecyclerView.ViewHolder {
        TextView name, desc;
        ImageView image;
        View view;

        public viewholder(View itemView) {
            super(itemView);
            view = itemView;
            name = (TextView) itemView.findViewById(R.id.name);
            desc = (TextView) itemView.findViewById(R.id.desc);
            image = (ImageView) itemView.findViewById(R.id.image);
        }
    }

    class search extends AsyncTask<String, Void, Void> {
        ArrayList<FitnessModel> models = new ArrayList<>();

        @Override
        protected Void doInBackground(String... params) {
            for (int i = 0; i < fitnessModelArrayList.size(); i++) {
                if (fitnessModelArrayList.get(i).name.contains(params[0]))
                    models.add(fitnessModelArrayList.get(i));
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            recyclerView.setAdapter(new adapter(models));
        }
    }
}
