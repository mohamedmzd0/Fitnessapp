package com.example.coder.fitness;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class ProgressActivity extends AppCompatActivity {

    ArrayList<ProgressModel> progressModels;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_progress);
        progressModels = new ArrayList<>();
        recyclerView = (RecyclerView) findViewById(R.id.progress_recyclerview);
        DatabaseHelper db = new DatabaseHelper(getApplicationContext());
        SQLiteDatabase sqLiteDatabase = db.getReadableDatabase();
        progressModels = db.restorprogress();
        recyclerView.setAdapter(new adapter());
    }

    class adapter extends RecyclerView.Adapter<viewholder> {

        @Override
        public viewholder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.progress_recycler_raw, null);
            viewholder holder = new viewholder(view);
            return holder;
        }

        @Override
        public void onBindViewHolder(viewholder holder, final int position) {
            holder.name.setText(progressModels.get(position).getName());
            holder.date.setText(progressModels.get(position).getDate());
            holder.status.setText(progressModels.get(position).getStatus());
        }

        @Override
        public int getItemCount() {
            return progressModels.size();
        }
    }

    class viewholder extends RecyclerView.ViewHolder {
        TextView name, date, status;

        public viewholder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.name);
            date = (TextView) itemView.findViewById(R.id.date);
            status = (TextView) itemView.findViewById(R.id.status);
        }
    }
}
