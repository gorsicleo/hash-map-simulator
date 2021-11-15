package hr.fer.oprpp1.customs.collections;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import hr.fer.oprpp1.custom.collections.Dictionary;

public class DictionaryTest {
	
	@Test
	public void dictionaryPutAndGetTest() {
		Dictionary<String, String> dictionary = new Dictionary<String, String>();
		dictionary.put("key1", "value1");
		dictionary.put("key2", "value2");
		dictionary.put("key3", "value3");
		
		assertEquals("value1", dictionary.get("key1"));
		assertEquals("value2", dictionary.get("key2"));
		assertEquals("value3", dictionary.get("key3"));
		
	}
	
	
	@Test
	public void dictionaryIsEmptyTest() {
		Dictionary<String, String> dictionary = new Dictionary<String, String>();
		assertEquals(true, dictionary.isEmpty());
		dictionary.put("key1", "value1");
		assertEquals(false, dictionary.isEmpty());
		dictionary.clear();
		assertEquals(true, dictionary.isEmpty());
		dictionary.put("key1", "value1");
		assertEquals(false, dictionary.isEmpty());
		dictionary.remove("key1");
		assertEquals(true, dictionary.isEmpty());
	}
	
	@Test
	public void dictionaryRemoveTest() {
		Dictionary<String, String> dictionary = new Dictionary<String, String>();
		dictionary.put("key1", "value1");
		dictionary.remove("key1");
		assertEquals(null, dictionary.get("key1"));
	}
	
	@Test
	public void dictionaryGetTest() {
		Dictionary<String, String> dictionary = new Dictionary<String, String>();
		dictionary.put("key1", "value1");
		assertEquals("value1", dictionary.get("key1"));
		assertEquals(null, dictionary.get(new Object()));
	}

}
