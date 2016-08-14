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
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Dialog_SelectHeadings_Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_select_headings_layout);

        View recyclerView = findViewById(R.id.heading_list);
        setupRecyclerView((RecyclerView) recyclerView);

    }

    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
        List<HeadingRecord> lst = LoadList();
        recyclerView.setAdapter(new SimpleItemRecyclerViewAdapter(lst));
    }

    private List<HeadingRecord> LoadList() {
        List<HeadingRecord> lst = new ArrayList<HeadingRecord>();

        lst = AddList(lst, "RecNo", false);
        lst = AddList(lst, "ClientNo", true);
        lst = AddList(lst, "ClientName", true);
        lst = AddList(lst, "ContactName", false);
        lst = AddList(lst, "EmailAddress", false);
        lst = AddList(lst, "PayeNo", false);
        lst = AddList(lst, "Telephone", false);
        lst = AddList(lst, "ExpiryDate", false);
        lst = AddList(lst, "Volumn", false);
        lst = AddList(lst, "UIFNo", false);
        lst = AddList(lst, "SDLNo", false);
        lst = AddList(lst, "System", false);
        lst = AddList(lst, "Annual Licence", false);
        lst = AddList(lst, "Paid", false);
        lst = AddList(lst, "Postal_01", false);
        lst = AddList(lst, "Postal_02", false);
        lst = AddList(lst, "Postal_03", false);
        lst = AddList(lst, "PostCode", false);
        lst = AddList(lst, "InstallPin", false);
        lst = AddList(lst, "PDFModule", false);
        lst = AddList(lst, "Consultant", false);
        lst = AddList(lst, "InCloud", false);
        return lst;
    }

    private List<HeadingRecord> AddList(List<HeadingRecord> lst, String Description, boolean Checked) {
        HeadingRecord c = new HeadingRecord(Description, Description, Checked);
        lst.add(c);
        return lst;
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

        public HeadingRecord() {

        }

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
