package org.kclhi.hands.utility.output;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import org.mapdb.DataIO;
import org.nustaq.serialization.FSTConfiguration;

public abstract class Serializer<A> extends org.mapdb.Serializer<A> {
  
  public static final Serializer<TraverserRecord> FASTJAVA = new Serializer<TraverserRecord>() {
    
    final FSTConfiguration conf = FSTConfiguration.createDefaultConfiguration();
    
    @Override
    public void serialize(DataOutput out, TraverserRecord value) throws IOException {
      byte barray[] = conf.asByteArray(value);
      DataIO.packInt(out,barray.length);
      out.write(barray);
    }
    
    @Override
    public TraverserRecord deserialize(DataInput in, int available) throws IOException {
      int size = DataIO.unpackInt(in);
      byte[] ret = new byte[size];
      in.readFully(ret);
      return (TraverserRecord)conf.asObject(ret);
    }
    
  };
  
  /*public static final Serializer<TraverserRecord> FASTJAVA = new Serializer<TraverserRecord>() {
    @Override
    public void serialize(DataOutput out, TraverserRecord value) throws IOException {
      FSTObjectOutput out2 = new FSTObjectOutput((OutputStream) out);
      out2.writeObject(value);
      out2.flush();
    }
    
    @Override
    public TraverserRecord deserialize(DataInput in, int available) throws IOException {
      try {
        FSTObjectInput in2 = new FSTObjectInput(new DataIO.DataInputToStream(in));
        TraverserRecord record = (TraverserRecord)in2.readObject();
        return record;
      } catch (ClassNotFoundException e) {
        throw new IOException(e);
      }
    }
  };*/
  
}
