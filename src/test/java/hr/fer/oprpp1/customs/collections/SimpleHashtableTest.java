package hr.fer.oprpp1.customs.collections;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ConcurrentModificationException;
import java.util.Iterator;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import hr.fer.oprpp1.custom.collections.SimpleHashtable;
import hr.fer.oprpp1.custom.collections.SimpleHashtable.TableEntry;

public class SimpleHashtableTest {

	@Disabled
	@Test
	public void constructorTest() {
		assertEquals(16, new SimpleHashtable<String, String>().size());
	}

	@Disabled
	@Test
	public void constructorWithArgumentTest() {
		assertThrows(IllegalArgumentException.class, () -> new SimpleHashtable<String, String>(0));
		assertEquals(32, new SimpleHashtable<String, String>(32).size());
		assertEquals(32, new SimpleHashtable<String, String>(30).size());
		assertEquals(64, new SimpleHashtable<String, String>(33).size());
	}

	@Test
	public void putAndGetTest() {
		SimpleHashtable<String, String> table = new SimpleHashtable<String, String>();
		for (int i = 0; i < 100; i++) {
			table.put("key" + i, "value" + i);
		}

		for (int i = 0; i < 100; i++) {
			assertEquals("value" + i, table.get("key" + i));
		}
	}

	@Test
	public void getPutKeyNullTest() {
		SimpleHashtable<String, String> table = new SimpleHashtable<String, String>();
		assertThrows(NullPointerException.class, () -> table.put(null, "this Should break!"));
		assertEquals(null, table.get(null));
	}

	@Test
	public void removeTest() {
		SimpleHashtable<String, String> table = new SimpleHashtable<String, String>();
		for (int i = 0; i < 100; i++) {
			table.put("key" + i, "value" + i);
			if (i % 2 == 0) {
				assertEquals("value" + i, table.remove("key" + i));
			}
		}

		for (int i = 0; i < 100; i++) {
			if (i % 2 == 0) {
				assertEquals(null, table.get("key" + i));
			} else {
				assertEquals("value" + i, table.get("key" + i));
			}
		}
	}

	@Test
	public void putOverwriteTest() {
		SimpleHashtable<String, String> table = new SimpleHashtable<String, String>();

		for (int i = 0; i < 100; i++) {
			table.put("key" + i, "value" + i);
			assertEquals("value" + i, table.put("key" + i, "xxx"));
		}

		for (int i = 0; i < 100; i++) {
			assertEquals("xxx", table.get("key" + i));
		}
	}

	@Test
	public void containsMethodsTest() {
		SimpleHashtable<String, String> table = new SimpleHashtable<String, String>();

		for (int i = 0; i < 100; i = i + 2) {
			table.put("key" + i, "value" + i);
		}

		for (int i = 0; i < 100; i++) {
			if (i % 2 == 0) {
				assertEquals(true, table.containsKey("key" + i));
				assertEquals(true, table.containsValue("value" + i));
			} else {
				assertEquals(false, table.containsKey("key" + i));
				assertEquals(false, table.containsValue("value" + i));
			}
		}
	}

	@Test
	public void isEmptyTest() {
		SimpleHashtable<String, String> table = new SimpleHashtable<String, String>();
		assertEquals(true, table.isEmpty());
		for (int i = 0; i < 100; i = i + 2) {
			table.put("key" + i, "value" + i);
			assertEquals(false, table.isEmpty());
			table.remove("key" + i);
			assertEquals(true, table.isEmpty());
		}
	}

	@Test
	public void toArrayTest() {
		SimpleHashtable<String, String> table = new SimpleHashtable<String, String>();
		table.put("key0", "value1");
		table.put("key1", "value1");
		table.put("key2", "value1");

		TableEntry<String, String>[] array = table.toArray();
		assertEquals(3, array.length);
		assertEquals("key0", array[0].getKey());
		assertEquals("key1", array[1].getKey());
		assertEquals("key2", array[2].getKey());

		for (int i = 0; i < 16; i++) {
			if (i < 3) {
				assertEquals("key" + i, array[i].getKey());
			}
		}
		
		table.clear();
		
		for (int i=0;i<100;i++) {
			table.put("key"+i, "value"+i);
		}
		array = table.toArray();
		
		assertEquals(100, array.length);
	}

	@Test
	public void toStringTest() {
		SimpleHashtable<String, String> table = new SimpleHashtable<String, String>();
		table.put("key1", "value1");
		table.put("key2", "value2");
		table.put("key3", "value3");

		assertEquals("[key1=value1, key2=value2, key3=value3]", table.toString());
	}

	@Test
	public void clearTest() {
		SimpleHashtable<String, String> table = new SimpleHashtable<String, String>();
		for (int i = 0; i < 100; i++) {
			table.put("key" + i, "value" + i);
		}
		table.clear();
		for (int i = 0; i < 100; i++) {
			assertEquals(null, table.get("key" + i));
			assertEquals(false, table.containsKey("key" + i));
		}

	}

	@Test
	public void iteratorIterationTest() {
		SimpleHashtable<String, Integer> examMarks = new SimpleHashtable<>(2);
		// fill data:
		examMarks.put("Ivana", 2);
		examMarks.put("Ante", 2);
		examMarks.put("Jasna", 2);
		examMarks.put("Kristina", 5);
		examMarks.put("Ivana", 5); // overwrites old grade for Ivana
		int count=0;
		for (SimpleHashtable.TableEntry<String, Integer> pair : examMarks) {
			assertEquals(true, examMarks.containsKey(pair.getKey()),"Key not found");
			count++;
		}
		assertEquals(4, count,"Not complete iteration");
	}

	@Test
	public void iteratorValidRemoveTest() {
		SimpleHashtable<String, Integer> examMarks = new SimpleHashtable<>(2);
		examMarks.put("Ivana", 2);
		examMarks.put("Ante", 2);
		examMarks.put("Jasna", 2);
		examMarks.put("Kristina", 5);
		examMarks.put("Ivana", 5); // overwrites old grade for Ivana

		Iterator<SimpleHashtable.TableEntry<String, Integer>> iter = examMarks.iterator();
		while (iter.hasNext()) {
			SimpleHashtable.TableEntry<String, Integer> pair = iter.next();
			if (pair.getKey().equals("Ivana")) {
				iter.remove(); // sam iterator kontrolirano uklanja trenutni element
			}
		}

		assertEquals(false, examMarks.containsKey("Ivana"));
		assertEquals(null, examMarks.get("Ivana"));
	}

	@Test
	public void iteratorDoubleRemoveCallTest() {
		SimpleHashtable<String, Integer> examMarks = new SimpleHashtable<>(2);
		examMarks.put("Ivana", 2);
		examMarks.put("Ante", 2);
		examMarks.put("Jasna", 2);
		examMarks.put("Kristina", 5);
		examMarks.put("Ivana", 5); // overwrites old grade for Ivana

		Iterator<SimpleHashtable.TableEntry<String, Integer>> iter = examMarks.iterator();
		while (iter.hasNext()) {
			SimpleHashtable.TableEntry<String, Integer> pair = iter.next();
			if (pair.getKey().equals("Ivana")) {
				iter.remove(); // sam iterator kontrolirano uklanja trenutni element
				assertEquals(false, examMarks.containsKey("Ivana"));
				assertEquals(null, examMarks.get("Ivana"));
				assertThrows(IllegalStateException.class, () -> iter.remove());
			}
		}
		assertEquals(null, examMarks.get("Ivana"),"Ivana is not removed!");
	}

	@Test
	public void iteratorInvalidRemoveTest() {
		SimpleHashtable<String, Integer> examMarks = new SimpleHashtable<>(2);
		examMarks.put("Ivana", 2);
		examMarks.put("Ante", 2);
		examMarks.put("Jasna", 2);
		examMarks.put("Kristina", 5);
		examMarks.put("Ivana", 5); // overwrites old grade for Ivana

		Iterator<SimpleHashtable.TableEntry<String, Integer>> iter = examMarks.iterator();
		while (iter.hasNext()) {
			SimpleHashtable.TableEntry<String, Integer> pair = iter.next();
			if (pair.getKey().equals("Ivana")) {
				examMarks.remove("Ivana");
				assertThrows(ConcurrentModificationException.class, () -> iter.hasNext());
				break;
			}
		}
		assertEquals(null, examMarks.get("Ivana"),"Ivana is not removed!");
	}

	@Test
	public void iteratorRemoveAllTest() {
		SimpleHashtable<String, Integer> examMarks = new SimpleHashtable<>(2);
		examMarks.put("Ivana", 2);
		examMarks.put("Ante", 2);
		examMarks.put("Jasna", 2);
		examMarks.put("Kristina", 5);
		examMarks.put("Ivana", 5); // overwrites old grade for Ivana

		Iterator<SimpleHashtable.TableEntry<String, Integer>> iter = examMarks.iterator();
		String result = new String("Ante => 2 , Ivana => 5, Jasna => 2, Kristina => 5");
		while (iter.hasNext()) {
			SimpleHashtable.TableEntry<String, Integer> pair = iter.next();
			assertEquals(true, result.contains(pair.getKey() + " => " + pair.getValue()));
			System.out.printf("%s => %d%n", pair.getKey(), pair.getValue());
			iter.remove();
		}
		assertEquals(0,examMarks.size());
	}

}
