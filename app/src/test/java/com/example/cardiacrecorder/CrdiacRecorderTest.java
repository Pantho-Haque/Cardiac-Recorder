package com.example.cardiacrecorder;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

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

        Model e = new Model("116", "178", "34", "09/03/2002", "Condition not OK", "1907028");
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
    public void testDelete() {
        EntryList entryList=new EntryList();

        entryList.add(new Model("112", "140", "31", "02/09/2002", "Condition not OK", "1907025"));

        Model city=new Model("99", "155", "35", "07/03/2002", "Condition not OK", "1907021");
        entryList.add(city);

        assertEquals(2, entryList.getEntries().size());

        entryList.delete(city);

        assertEquals(1, entryList.getEntries().size());
        assertTrue(!entryList.getEntries().contains(city));
    }

    @Test
    public void testDeleteException() {
        EntryList entryList = new EntryList();
        Model e =new Model("111", "173", "33", "01/03/2002", "Condition not OK", "1907026");
        entryList.add(e);

        assertThrows(IllegalArgumentException.class, () -> {
            entryList.delete(new Model("110", "170", "30", "02/03/2002", "Condition not OK", "1907027"));
        });
    }

    @Test
    public void testUpdate() {
        EntryList entryList = mockEntryList();

        Model originalEntry = entryList.getEntries().get(0);
        Model updatedEntry = new Model("120", "180", "32", "02/03/2002", "Updated condition", "1907029");
        entryList.update(updatedEntry);

        assertTrue(entryList.getEntries().contains(updatedEntry));
        assertFalse(entryList.getEntries().contains(originalEntry));
    }

    @Test
    public void testUpdateException() {
        EntryList entryList = new EntryList();
        Model entry = new Model("110", "170", "30", "02/03/2002", "Condition not OK", "1907029");
        entryList.add(entry);

        assertThrows(IllegalArgumentException.class, () -> {
            entryList.update(new Model("120", "180", "32", "02/03/2002", "Updated condition", "1907028"));
        });
    }

}
