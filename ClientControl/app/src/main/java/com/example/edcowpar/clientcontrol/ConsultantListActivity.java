package com.example.edcowpar.clientcontrol;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class ConsultantListActivity extends AppCompatActivity {

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private boolean mTwoPane;
    private SqlGet sq;
    private String eMes, strSearchText, strSequence, strTable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consultant_list);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //get parameter in extra
        Bundle b = getIntent().getExtras();
        strSearchText = b.getString("SearchText");  //get any Search text
        strSequence = b.getString("Sequence");      //get Sequence
        strTable = b.getString("Table");            //get Database Table

        FloatingActionButton fabAddNew = (FloatingActionButton) findViewById(R.id.fabAddNew);
        fabAddNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), AddConsultantActivity.class);
                startActivity(intent);
            }
        });

        View recyclerView = findViewById(R.id.item_list);
        assert recyclerView != null;
        setupRecyclerView((RecyclerView) recyclerView);

        if (findViewById(R.id.item_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;
        }
    }

    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
        //Open sql
        sq = new SqlGet();
        eMes = sq.OpenConnection();
        if (eMes.equals("ok")) {
            List<ConsultantRecord> CONSULTANTS = sq.getListConsultants();
            eMes = sq.get_eMes();
            if (eMes.equals("ok"))
                recyclerView.setAdapter(new SimpleItemRecyclerViewAdapter(CONSULTANTS));
        }
        if (!eMes.equals("ok")) {
            Intent i = new Intent(this, ErrorActivity.class);
            i.putExtra("errMessage", eMes);
            startActivity(i);
        }
    }

    public class SimpleItemRecyclerViewAdapter
            extends RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.ViewHolder> {

        private final List<ConsultantRecord> mValues;

        public SimpleItemRecyclerViewAdapter(List<ConsultantRecord> items) {
            mValues = items;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_list_content, parent, false);

            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {
            holder.mItem = mValues.get(position);
            holder.mIdView.setText(mValues.get(position).UserCode);
            holder.mContentView.setText(mValues.get(position).UserName);

            // Set the color to red if row is even, or to green if row is odd.
            if (position % 2 == 0) {
                holder.mView.setBackgroundResource(R.color.colorEven);
            } else {
                holder.mView.setBackgroundResource(R.color.colorOdd);
            }

            holder.mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mTwoPane) {
                        Bundle arguments = new Bundle();
                        arguments.putString("ClientNo", holder.mItem.UserCode);
                        ItemDetailFragment fragment = new ItemDetailFragment();
                        fragment.setArguments(arguments);
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.item_detail_container, fragment)
                                .commit();
                    } else {
                        Context context = v.getContext();
                        Intent intent = new Intent(context, ClientDetailActivity.class);
                        intent.putExtra("ClientNo", holder.mItem.UserCode);

                        context.startActivity(intent);
                    }
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
            public ConsultantRecord mItem;

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
