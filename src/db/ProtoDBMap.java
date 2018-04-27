package snowblossom.db;


import com.google.protobuf.Parser;
import com.google.protobuf.Message;
import com.google.protobuf.ByteString;
import com.google.protobuf.InvalidProtocolBufferException;

import java.util.Map;
import java.util.TreeMap;

public class ProtoDBMap<M extends Message>
{
  Parser<M> parser;
  DBMap inner;

  public ProtoDBMap(Parser<M> parser, DBMap inner)
  {
    this.parser = parser;
    this.inner = inner;
  }
  
  public void put(String key, Message m)
  {
    inner.put(key, m.toByteString());
  }

  public M get(String key)
  {
    ByteString bs = inner.get(key);
    if (bs == null) return null;

    try
    {
      return parser.parseFrom(bs);
    }
    catch(InvalidProtocolBufferException e)
    {
      throw new RuntimeException(e);
    }
  }

  public boolean containsKey(String key)
  {
    return inner.containsKey(key);
  }

  public void putAll(Map<String, M> map)
  {
    TreeMap<String, ByteString> put_map = new TreeMap<>();

    for(Map.Entry<String, M> me : map.entrySet())
    {
      put_map.put(me.getKey(), me.getValue().toByteString());
    }

    inner.putAll(put_map);

  }


}
