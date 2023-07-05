package com.example.cardiacrecorder;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class EntryList {

    private List<Model> entries = new ArrayList<>();

    // add
    public void add(Model e) {
        if (entries.contains(e)) {
            throw new IllegalArgumentException();
        }
        entries.add(e);
    }

    // delete
    public void delete(Model city){
        if (!entries.contains(city)) {
            throw new IllegalArgumentException();
        }
        entries.remove(city);
    }

    // count
    public int count(){
        return entries.size();
    }
}
