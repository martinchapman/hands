package org.kclhi.hands.utility.output;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Random;

import org.kclhi.hands.utility.Pair;

import org.mapdb.DB;
import org.mapdb.DBMaker;
import org.mapdb.HTreeMap;
import org.mapdb.Store;

/**
* This example shows how-to create off-heap cache,
* where entries expire when maximal store size is reached.
*
* It also shows howto get basic statistics about store size.
*
* It is more advanced version of previous example.
* It uses more settings, bypasses general serialization for best performance
* and discussed other performance tunning
*
*/
public class CacheOffHeapAdvanced {
  
  public static void main(String[] args) {
    
    final double cacheSizeInGB = 10.0;
    
    //first create store
    DB db = DBMaker
    .memoryDirectDB()
    // make it faster
    .transactionDisable()
    .allocateRecidReuseEnable()
    //some additional options for DB
    // .asyncWriteEnable()
    // .cacheSize(100000)
    .make();
    
    
    HTreeMap<Integer, HiderRecord> cache = db
    .hashMapCreate("cache")
    .expireStoreSize(cacheSizeInGB)
    .counterEnable() //disable this if cache.size() is not used
    //use proper serializers to and improve performance
    .keySerializer(Serializer.INTEGER)
    .valueSerializer(Serializer.FASTJAVA)
    .make();
    
    
    //generates random key and values
    Random r = new Random();
    //used to print store statistics
    Store store = Store.forDB(db);
    
    long startTime = System.currentTimeMillis();
    
    Hashtable<Double, Pair<Integer, HiderRecord>> list = new Hashtable<Double, Pair<Integer, HiderRecord>>();
    
    // insert some stuff in cycle
    double limit = 1e5;
    
    for(long counter=1; counter<limit; counter++){
      
      //long key = r.nextLong();
      //byte[] value = new byte[1000];
      //r.nextBytes(value);
      
      //System.out.println(counter);
      
      double ID = Math.random() * 10000000;
      
      HiderRecord record = new HiderRecord("" + ID);
      
      list.put(ID, new Pair<Integer, HiderRecord>(cache.size(), record));
      
      //record.addSeeker(new TraverserRecord("" + ( Math.random() * 10000000 )));
      
      cache.put(cache.size(), record);
      
      //cache.get(new ArrayList<HiderRecord>(cache.values()).indexOf(record)).addSeeker(new TraverserRecord("" + ( Math.random() * 10000000 )));
      
      list.get(ID).getElement1().addSeeker(new TraverserRecord("" + ( Math.random() * 10000000 )));
      
      cache.put(list.get(ID).getElement0(), list.get(ID).getElement1());
      
      record.addSeeker(new TraverserRecord("" + ( Math.random() * 10000000 )));
      
      /*if(counter%1e5==0){
        System.out.printf("Map size: %,d, counter %,d, curr store size: %,d, store free size: %,d\n",
        cache.sizeLong(), counter, store.getCurrSize(),  store.getFreeSize());
      }*/
      
    }
    
    System.out.println((new ArrayList<HiderRecord>(cache.values())).get(0).getSeekersAndAttributes()); //.get(0).getSeekerAttributes());
    
    long endTime   = System.currentTimeMillis();
    long totalTime = endTime - startTime;
    System.out.println(totalTime / 1000);
    
    
    // and close to release memory
    db.close();
    
  }
}
