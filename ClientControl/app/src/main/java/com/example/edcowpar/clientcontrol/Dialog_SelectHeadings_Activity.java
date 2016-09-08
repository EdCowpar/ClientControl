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
    private ReportHeadings r;
    private List<HeadingRecord> lst;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_select_headings_layout);

        View recyclerView = findViewById(R.id.heading_list);
        setupRecyclerView((RecyclerView) recyclerView);

    }

    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
        lst = LoadList();
        recyclerView.setAdapter(new SimpleItemRecyclerViewAdapter(lst));
    }

    private List<HeadingRecord> LoadList() {
        lst = new ArrayList<HeadingRecord>();
        r = GetData.Read_ReportHeadings(this.getApplicationContext(), "ClientDetails.txt");
        lst = AddList(lst, "ClientNo", r.ClientNo);
        lst = AddList(lst, "ClientName", r.ClientName);
        lst = AddList(lst, "ContactName", r.ContactName);
        lst = AddList(lst, "EmailAddress", r.EmailAddress);
        lst = AddList(lst, "PayeNo", r.PayeNo);
        lst = AddList(lst, "Telephone", r.Telephone);
        lst = AddList(lst, "ExpiryDate", r.ExpiryDate);
        lst = AddList(lst, "Volumn", r.Volumn);
        lst = AddList(lst, "UIFNo", r.UIFNo);
        lst = AddList(lst, "SDLNo", r.SDLNo);
        lst = AddList(lst, "System", r.System);
        lst = AddList(lst, "Annual Licence", r.AnnualLicence);
        lst = AddList(lst, "Paid", r.Paid);
        lst = AddList(lst, "Postal_01", r.Postal_01);
        lst = AddList(lst, "Postal_02", r.Postal_02);
        lst = AddList(lst, "Postal_03", r.Postal_03);
        lst = AddList(lst, "PostCode", r.PostCode);
        lst = AddList(lst, "InstallPin", r.InstallPin);
        lst = AddList(lst, "PDFModule", r.PDFModule);
        lst = AddList(lst, "Consultant", r.Consultant);
        lst = AddList(lst, "InCloud", r.InCloud);
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
        r.ContactName = false;
        r.EmailAddress = false;
        r.PayeNo = false;
        r.Telephone = false;
        r.ExpiryDate = false;
        r.Volumn = false;
        r.UIFNo = false;
        r.SDLNo = false;
        r.System = false;
        r.AnnualLicence = false;
        r.Paid = false;
        r.Postal_01 = false;
        r.Postal_02 = false;
        r.Postal_03 = false;
        r.PostCode = false;
        r.InstallPin = false;
        r.PDFModule = false;
        r.Consultant = false;
        r.InCloud = false;
        Confirm(v);
    }

    public void Confirm(View v) {
        GetData.Write_ReportHeadings(this.getApplicationContext(), "ClientDetails.txt", r);
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
                        case "ContactName":
                            r.ContactName = isChecked;
                            break;
                        case "EmailAddress":
                            r.EmailAddress = isChecked;
                            break;
                        case "PayeNo":
                            r.PayeNo = isChecked;
                            break;
                        case "Telephone":
                            r.Telephone = isChecked;
                            break;
                        case "ExpiryDate":
                            r.ExpiryDate = isChecked;
                            break;
                        case "Volumn":
                            r.Volumn = isChecked;
                            break;
                        case "UIFNo":
                            r.UIFNo = isChecked;
                            break;
                        case "SDLNo":
                            r.SDLNo = isChecked;
                            break;
                        case "System":
                            r.System = isChecked;
                            break;
                        case "Annual Licence":
                            r.AnnualLicence = isChecked;
                            break;
                        case "Paid":
                            r.Paid = isChecked;
                            break;
                        case "Postal_01":
                            r.Postal_01 = isChecked;
                            break;
                        case "Postal_02":
                            r.Postal_02 = isChecked;
                            break;
                        case "Postal_03":
                            r.Postal_03 = isChecked;
                        case "PostCode":
                            r.PostCode = isChecked;
                            break;
                        case "InstallPin":
                            r.InstallPin = isChecked;
                            break;
                        case "PDFModule":
                            r.PDFModule = isChecked;
                            break;
                        case "Consultant":
                            r.Consultant = isChecked;
                            break;
                        case "InCloud":
                            r.InCloud = isChecked;
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
