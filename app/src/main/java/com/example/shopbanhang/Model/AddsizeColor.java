package com.example.shopbanhang.Model;

import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

public class AddsizeColor {
    private TextView txtname;
    private Spinner spnsize;
    private Spinner spncolor;
    private EditText edtsoluong;

    public AddsizeColor(){

    }


    public AddsizeColor(TextView txtname, Spinner spnsize, Spinner spncolor, EditText edtsoluong) {
        this.txtname = txtname;
        this.spnsize = spnsize;
        this.spncolor = spncolor;
        this.edtsoluong = edtsoluong;
    }

    public TextView getTxtname() {
        return txtname;
    }

    public void setTxtname(TextView txtname) {
        this.txtname = txtname;
    }

    public Spinner getSpnsize() {
        return spnsize;
    }

    public void setSpnsize(Spinner spnsize) {
        this.spnsize = spnsize;
    }

    public Spinner getSpncolor() {
        return spncolor;
    }

    public void setSpncolor(Spinner spncolor) {
        this.spncolor = spncolor;
    }

    public EditText getEdtsoluong() {
        return edtsoluong;
    }

    public void setEdtsoluong(EditText edtsoluong) {
        this.edtsoluong = edtsoluong;
    }
}
