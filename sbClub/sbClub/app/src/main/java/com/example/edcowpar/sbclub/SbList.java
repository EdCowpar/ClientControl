package com.example.edcowpar.sbclub;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class SbList extends AppCompatActivity {
    String strSFP,PkyName,DbTable, eMes;
    SqlGet sq;
    private View recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sblist);
        recyclerView = findViewById(R.id.data_list);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Gilbert and Sullivan Society");
        actionBar.setSubtitle("Members");

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
            List<DataKeys> Keys = sq.getDataKeys(strSFP,"");
            eMes = sq.get_eMes();
            if (eMes.equals("ok")) {
                PkyName=Keys.get(0).getPky();
                DbTable=Keys.get(0).getDbTable();
                List<DataFields> Fields = sq.getDataFields(Keys);
                eMes = sq.get_eMes();
                if (eMes.equals("ok")) {
                    List<DataRecord> Records = sq.getDataRecords(Fields);
                    eMes = sq.get_eMes();
                    if (eMes.equals("ok")) {
                        recyclerView.setAdapter(new SimpleItemRecyclerViewAdapter(Records));
                    }
                }
            }
        }

        if (!eMes.equals("ok")) {
            Intent i = new Intent(this, SbError.class);
            i.putExtra("eMes", eMes);
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
            holder.mIdView.setText(mValues.get(position)._0);
            String temp = mValues.get(position)._1 + " ";
            String txt = mValues.get(position)._2 + " ";
            temp = temp + txt;
            txt = mValues.get(position)._3;
            if (txt != null && !txt.equals("") & !txt.equals("null")) {
                temp = temp + "\n" + txt;
            }
            txt = mValues.get(position)._4;
            if (txt != null && !txt.equals("") & !txt.equals("null")) {
                temp = temp + "\n" + txt;
            }
            txt = mValues.get(position)._5;
            if (txt != null && !txt.equals("") & !txt.equals("null")) {
                temp = temp + "\n" + txt;
            }
            txt = mValues.get(position)._6;
            if (txt != null && !txt.equals("") & !txt.equals("null")) {
                temp = temp + "\n" + txt;
            }
            holder.mContentView.setText(temp);

            // Set the color to red if row is even, or to green if row is odd.
            if (position % 2 == 0) {
                holder.mView.setBackgroundResource(R.color.colorEven);
            } else {
                holder.mView.setBackgroundResource(R.color.colorOdd);
            }
            holder.mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Context context = v.getContext();
                    Intent intent = new Intent(context, SbMain.class);
                    intent.putExtra("Pky", holder.mItem._0);
                    intent.putExtra("SFP", strSFP);
                    intent.putExtra("PkyName", PkyName);
                    intent.putExtra("DbTable", DbTable);
                    context.startActivity(intent);
                }
            });

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
