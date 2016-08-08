package com.example.edcowpar.clientcontrol;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class ClientListActivity extends AppCompatActivity {

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private boolean mTwoPane;
    private SqlGet sq;
    private String eMes, strSearchText, strSequence, strTable, strWhere, strTitle;
    private Intent i;
    private AppSettings a;
    private FloatingActionButton fab;
    private View recyclerView;
    private Context ctx;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_list);
        handleIntent(getIntent());
        ctx = this;


        //get parameter in extra
        //Bundle b = getIntent().getExtras();
        strWhere = ""; //b.getString("Where");  //get any Search text
        strSearchText = ""; //b.getString("SearchText");  //get any Search text

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Clients");

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), AddClientActivity.class);
                startActivity(intent);
            }
        });

        recyclerView = findViewById(R.id.client_list);

        if (findViewById(R.id.client_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;
        }
    }

    @Override
    public void onResume() {
        super.onResume();  // Always call the superclass method first
        setSequence();      //get Sequence
        setupRecyclerView((RecyclerView) recyclerView);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.select_client_menu, menu);
        return true;
    }

    private void setSequence() {
        a = GetData.Read(this.getApplicationContext());
        switch (a.ClientSeq) {
            case 0: //rbClientNo
                strSequence = "ORDER BY ClientNo";
                break;
            case 1: //rbClientNameAsc:
                strSequence = "ORDER BY ClientName";
                break;
            case 2: //R.id.rbClientNameDsc:
                strSequence = "ORDER BY ClientName DESC";
                break;
            case 3: //R.id.rbExpiryDateAsc:
                strSequence = "ORDER BY ExpiryDate, ClientName";
                break;
            case 4: //R.id.rbExpiryDateDsc:
                strSequence = "ORDER BY ExpiryDate DESC, ClientName";
                break;
            case 5: //R.id.rbConsultant:
                strSequence = "ORDER BY Consultant, ClientName";
                break;
            case 6: //R.id.rbSystemType:
                strSequence = "ORDER BY System, ClientName";
                break;

        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.Sort:
                i = new Intent(this, SortDialog.class);
                startActivity(i);
                return true;

            case R.id.Search:
                i = new Intent(this, SelectClientActivity.class);
                startActivity(i);
                return true;

            case R.id.Headings:
                i = new Intent(this, SelectHeadingsActivity.class);
                startActivity(i);
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {

        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            //use the query to search your data somehow
            intent.putExtra("Where", "");
            intent.putExtra("SearchText", query);
            intent.putExtra("Sequence", "ClientName");
            intent.putExtra("Table", "Clients");
        }
    }

    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
        //Open sql
        sq = new SqlGet();
        eMes = sq.OpenConnection();
        if (eMes.equals("ok")) {
            List<ClientRecord> CLIENTS = sq.getAllClients(strWhere, strSearchText, strSequence);
            eMes = sq.get_eMes();
            if (eMes.equals("ok"))
                recyclerView.setAdapter(new SimpleItemRecyclerViewAdapter(CLIENTS));
        }
        if (!eMes.equals("ok")) {
            Intent i = new Intent(this, ErrorActivity.class);
            i.putExtra("eMes", eMes);
            startActivity(i);
            finish();
        }
    }

    public class SimpleItemRecyclerViewAdapter
            extends RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.ViewHolder> {

        private final List<ClientRecord> mValues;

        public SimpleItemRecyclerViewAdapter(List<ClientRecord> items) {
            mValues = items;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.client_list_content, parent, false);

            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {
            holder.mItem = mValues.get(position);
            holder.mClientNoView.setText(mValues.get(position).ClientNo);
            String txt = mValues.get(position).ExpiryDate;
            txt = SubRoutines.FmtString(txt, "a");
            txt = mValues.get(position).ClientName + "\n" + "Expires: " + txt;
            if (a.ClientSeq.equals(5)) {
                String c = sq.getConsultantName(mValues.get(position).Consultant);
                txt = txt + "\nConsultant: " + c;
            }
            if (a.ClientSeq.equals(6)) {
                String sType = mValues.get(position).System;
                String v = sq.getSystemType(ctx, sType);
                txt = txt + "\nSystemType: " + v;
            }
            holder.mClientNameView.setText(txt);

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
                        arguments.putString("ClientNo", holder.mItem.ClientNo);
                        ClientDetailFragment fragment = new ClientDetailFragment();
                        fragment.setArguments(arguments);
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.client_detail_container, fragment)
                                .commit();
                    } else {
                        Context context = v.getContext();
                        Intent intent = new Intent(context, ClientDetailActivity.class);
                        intent.putExtra("ClientNo", holder.mItem.ClientNo);

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
            public final TextView mClientNoView;
            public final TextView mClientNameView;
            public ClientRecord mItem;

            public ViewHolder(View view) {
                super(view);
                mView = view;
                mClientNoView = (TextView) view.findViewById(R.id.ClientNo);
                mClientNameView = (TextView) view.findViewById(R.id.ClientName);
            }
        }
    }
}
