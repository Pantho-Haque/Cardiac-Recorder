package com.example.cardiacrecorder;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CrdiacRecorderTest {

    private EntryList mockEntryList() {
        EntryList entryList = new EntryList();
        entryList.add(mockEntry());
        return entryList;
    }

    private Model mockEntry() {
        return new Model("110", "170", "30", "02/03/2002", "Condition not OK", "1907029");
    }

    @Test
    public void testAdd() {
        EntryList entryList = mockEntryList();
        assertEquals(1, entryList.getEntries().size());

        Model e = new Model("110", "170", "30", "02/03/2002", "Condition not OK", "1907029");
        entryList.add(e);

        assertEquals(2, entryList.getEntries().size());
        assertTrue(entryList.getEntries().contains(e));
    }

    @Test
    public void testAddException() {
        EntryList entryList = new EntryList();
        Model e = mockEntry();
        entryList.add(e);

        assertThrows(IllegalArgumentException.class, () -> {
            entryList.add(e);
        });
    }

    @Test
    public void testGetEntries() {
        EntryList entryList = mockEntryList();
        assertEquals(0, mockEntry().compareTo(entryList.getEntries().get(0)));

        Model e = new Model("110", "170", "30", "02/03/2002", "Condition not OK", "1907029");
        entryList.add(e);

        assertEquals(0, e.compareTo(entryList.getEntries().get(0)));
        assertEquals(0, mockEntry().compareTo(entryList.getEntries().get(1)));
    }
}
