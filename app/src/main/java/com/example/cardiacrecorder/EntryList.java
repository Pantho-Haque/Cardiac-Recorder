package com.example.cardiacrecorder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class EntryList {

    private List<Model> entries = new ArrayList<>();

    // Add
    public void add(Model e) {
        if (entries.contains(e)) {
            throw new IllegalArgumentException("Entry already exists");
        }
        entries.add(e);
    }

    // Delete
    public void delete(Model e) {
        if (!entries.contains(e)) {
            throw new IllegalArgumentException("Entry does not exist");
        }
        entries.remove(e);
    }

    // Count
    public int count() {
        return entries.size();
    }

    // Update
    public void update(Model e) {
        for (int i = 0; i < entries.size(); i++) {
            Model currentModel = entries.get(i);
            if (currentModel.getId().equals(e.getId())) {
                entries.set(i, e);
                return;
            }
        }
        throw new IllegalArgumentException("Entry not found");
    }

    public List<Model> getEntries() {
        List<Model> entryList = new ArrayList<>(entries);
        //Collections.sort(entryList);
        return entryList;
    }
}
