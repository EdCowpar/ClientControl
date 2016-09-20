package com.example.edcowpar.clientcontrol;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Dialog_SelectAuditHeadings extends AppCompatActivity {
    private AuditHeadings r;
    private List<HeadingRecord> lst;
    private String filename;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_select_headings_layout);
        //get parameter in extra
        Bundle b = getIntent().getExtras();
        filename = b.getString("FileName");

        View recyclerView = findViewById(R.id.heading_list);
        setupRecyclerView((RecyclerView) recyclerView);


    }

    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
        lst = LoadList();
        recyclerView.setAdapter(new SimpleItemRecyclerViewAdapter(lst));
    }

    private List<HeadingRecord> LoadList() {
        lst = new ArrayList<HeadingRecord>();
        r = GetData.Read_AuditHeadings(this.getApplicationContext(), filename);
        lst = AddList(lst, "ClientNo", r.ClientNo);
        lst = AddList(lst, "ClientName", r.ClientName);
        lst = AddList(lst, "Action", r.Action);
        lst = AddList(lst, "Remarks", r.Remarks);
        lst = AddList(lst, "runDate", r.runDate);
        lst = AddList(lst, "runTime", r.runTime);
        lst = AddList(lst, "UserName", r.UserName);
        return lst;
    }

    private List<HeadingRecord> AddList(List<HeadingRecord> lst, String Description, boolean Checked) {
        HeadingRecord c = new HeadingRecord(Description, Description, Checked);
        lst.add(c);
        return lst;
    }

    public void ResetAll(View v) {
        r.ClientNo = true;
        r.ClientName = true;
        r.UserName = false;
        r.Action = false;
        r.Remarks = false;
        r.runDate = false;
        r.runTime = false;
        Confirm(v);
    }

    public void Confirm(View v) {
        GetData.Write_AuditHeadings(this.getApplicationContext(), filename, r);
        finish();
    }

    public class SimpleItemRecyclerViewAdapter
            extends RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.ViewHolder> {

        private final List<HeadingRecord> mValues;

        public SimpleItemRecyclerViewAdapter(List<HeadingRecord> items) {
            mValues = items;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.dialog_select_headings_list_content, parent, false);

            return new ViewHolder(view);
        }

        public void onBindViewHolder(final ViewHolder holder, int position) {

            holder.mItem = mValues.get(position);
            holder.mIdView.setText(mValues.get(position).Id);
            holder.mChkBox.setChecked(mValues.get(position).isSelected());

            //in some cases, it will prevent unwanted situations
            holder.mChkBox.setOnCheckedChangeListener(null);

            //if true, your checkbox will be selected, else unselected
            holder.mChkBox.setChecked(mValues.get(position).isSelected());

            holder.mChkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    mValues.get(holder.getAdapterPosition()).setSelected(isChecked);
                    switch (holder.mIdView.getText().toString()) {
                        case "ClientNo":
                            r.ClientNo = isChecked;
                            break;
                        case "ClientName":
                            r.ClientName = isChecked;
                            break;
                        case "UserName":
                            r.UserName = isChecked;
                            break;
                        case "runDate":
                            r.runDate = isChecked;
                            break;
                        case "runTime":
                            r.runTime = isChecked;
                            break;
                        case "Action":
                            r.Action = isChecked;
                            break;
                        case "Remarks":
                            r.Remarks = isChecked;
                            break;
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
            public final CheckBox mChkBox;
            public final TextView mIdView;
            public HeadingRecord mItem;

            public ViewHolder(View view) {
                super(view);
                mView = view;
                mChkBox = (CheckBox) view.findViewById(R.id.chk);
                mIdView = (TextView) view.findViewById(R.id.id);
            }

        }
    }

    private class HeadingRecord implements Serializable {
        public CheckBox ckselected;
        public String Id;
        public String Description;
        private boolean isSelected;

        public HeadingRecord(String id, String description) {

            this.Id = id;
            this.Description = description;

        }

        public HeadingRecord(String id, String description, boolean isSelected) {

            this.Id = id;
            this.Description = description;
            this.isSelected = isSelected;
        }

        public CheckBox getCkselected() {
            return ckselected;
        }

        public void setCkselected(CheckBox ckselected) {
            this.ckselected = ckselected;
        }

        public String getId() {
            return Id;
        }

        public void setId(String id) {
            Id = id;
        }

        public String getDescription() {
            return Description;
        }

        public void setDescription(String description) {
            Description = description;
        }

        public boolean isSelected() {
            return isSelected;
        }

        public void setSelected(boolean isSelected) {
            this.isSelected = isSelected;
        }
    }
}
