package com.example.edcowpar.sbclub;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class DataListActivity extends AppCompatActivity {
    String strSFP, eMes;
    SqlGet sq;
    private View recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_list);
        recyclerView = findViewById(R.id.data_list);

        //get parameter in extra
        Bundle b = getIntent().getExtras();
        strSFP = b.getString("SFP");  //get Screen Design
    }

    @Override
    public void onResume() {
        super.onResume();  // Always call the superclass method first
        setupRecyclerView((RecyclerView) recyclerView);
    }

    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
        //Open sql
        sq = new SqlGet();
        eMes = sq.OpenConnection();
        if (eMes.equals("ok")) {
            List<DataRecord> Fields = sq.getDataRecord(strSFP);
            eMes = sq.get_eMes();
            if (eMes.equals("ok"))
                recyclerView.setAdapter(new SimpleItemRecyclerViewAdapter(Fields));
        }
        if (!eMes.equals("ok")) {
            Intent i = new Intent(this, ErrorActivity.class);
            i.putExtra("errMessage", eMes);
            startActivity(i);
        }
    }

    public class SimpleItemRecyclerViewAdapter
            extends RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.ViewHolder> {

        private final List<DataRecord> mValues;

        public SimpleItemRecyclerViewAdapter(List<DataRecord> items) {
            mValues = items;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.data_list_content, parent, false);

            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {
            holder.mItem = mValues.get(position);
            holder.mIdView.setText(mValues.get(position).dbField);
            holder.mContentView.setText(mValues.get(position).Description);

            // Set the color to red if row is even, or to green if row is odd.
            if (position % 2 == 0) {
                holder.mView.setBackgroundResource(R.color.colorEven);
            } else {
                holder.mView.setBackgroundResource(R.color.colorOdd);
            }

        }

        @Override
        public int getItemCount() {
            return mValues.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            public final View mView;
            public final TextView mIdView;
            public final TextView mContentView;
            public DataRecord mItem;

            public ViewHolder(View view) {
                super(view);
                mView = view;
                mIdView = (TextView) view.findViewById(R.id.id);
                mContentView = (TextView) view.findViewById(R.id.content);
            }

            @Override
            public String toString() {
                return super.toString() + " '" + mContentView.getText() + "'";
            }
        }
    }
}
