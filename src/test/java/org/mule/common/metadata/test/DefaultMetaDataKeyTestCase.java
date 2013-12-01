package org.mule.common.metadata.test;

import static org.junit.Assert.*;

import java.util.TreeMap;

import org.junit.Test;
import org.mule.common.metadata.DefaultMetaDataKey;
import org.mule.common.metadata.MetaDataKey;

public class DefaultMetaDataKeyTestCase {

    @Test
    public void testMetaDataKeyComparisson() {
        MetaDataKey key1 = new DefaultMetaDataKey("Aa", "Key #A");
        MetaDataKey key2 = new DefaultMetaDataKey("Ab", "Key #B");
        MetaDataKey key3 = new DefaultMetaDataKey("Ac", "Key #C");
        
        assertTrue("A should be > B", key1.compareTo(key2) < 0);
        assertTrue("B should be > C", key2.compareTo(key3) < 0);
        assertTrue("A should be > C", key1.compareTo(key3) < 0);
        assertTrue("A should be == A", key1.compareTo(key1) == 0);
        assertTrue("B should be == C", key1.compareTo(key1) == 0);
        assertTrue("C should be == B", key1.compareTo(key1) == 0);
        assertTrue("C should be < B", key3.compareTo(key2) > 0);
        assertTrue("C should be < A", key3.compareTo(key1) > 0);
        assertTrue("B should be < A", key2.compareTo(key1) > 0);
    }

    @Test
    public void testMetaDataKeyTree() {
        TreeMap<MetaDataKey, String> map = new TreeMap<MetaDataKey, String>();
        MetaDataKey key1 = new DefaultMetaDataKey("Aa", "Key #A");
        MetaDataKey key2 = new DefaultMetaDataKey("Ab", "Key #B");
        MetaDataKey key3 = new DefaultMetaDataKey("Ac", "Key #C");
        map.put(key3, "Ac");
        map.put(key2, "Ab");
        map.put(key1, "Aa");
        assertTrue(map.firstKey().equals(key1));
        map.remove(key1);
        assertTrue(map.firstKey().equals(key2));
        map.remove(key2);
        assertTrue(map.firstKey().equals(key3));
    }

    @Test
    public void testMetadataKeyEquality() {
        MetaDataKey key1 = new DefaultMetaDataKey("Aaa", "Key #A");
        MetaDataKey key2 = new DefaultMetaDataKey("Aaa", "Key #B");
        assertTrue(key1.equals(key2));
        key1 = new DefaultMetaDataKey("A", "Key #A");
        key2 = new DefaultMetaDataKey("B", "Key #A");
        assertFalse(key1.equals(key2));
    }

}
