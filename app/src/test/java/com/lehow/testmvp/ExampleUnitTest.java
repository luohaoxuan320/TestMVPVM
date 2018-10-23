package com.lehow.testmvp;

import android.util.JsonReader;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.google.gson.annotations.SerializedName;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
  @Test public void addition_isCorrect() {

    GsonBuilder gsonBuilder = new GsonBuilder();
    JsonSerializer<PagerReq> pagerReqJsonSerializer = new JsonSerializer<PagerReq>() {
      @Override
      public JsonElement serialize(PagerReq src, Type typeOfSrc, JsonSerializationContext context) {
        //这个会导致死循环
        //context.serialize(src);
        final Gson gson = new Gson();
        //用标准的Gson对象来解析SerializedName，映射字段名，方便自定义json字段名
        JsonElement serialize = gson.toJsonTree(src);
        JsonObject asJsonObject = serialize.getAsJsonObject();
        JsonElement entity = asJsonObject.get("entity");
        //移除子节点
        asJsonObject.remove("entity");
        JsonObject entityAsJsonObject = entity.getAsJsonObject();
        Set<Map.Entry<String, JsonElement>> entries1 = entityAsJsonObject.entrySet();
        //将其挂到父节点
        for (Map.Entry<String, JsonElement> stringJsonElementEntry : entries1) {
          asJsonObject.add(stringJsonElementEntry.getKey(), stringJsonElementEntry.getValue());
        }
        return serialize;
      }
    };
    gsonBuilder.registerTypeAdapter(PagerReq.class, pagerReqJsonSerializer);

    Gson customGson = gsonBuilder.create();

    PagerReq<User> userPagerReq = new PagerReq<>(1, 10, new User("AA", 18));
    HashMap<String, String> hashMap = new HashMap<>();
    hashMap.put("phone", "110");
    hashMap.put("name", "紧急电话");
    PagerReq<HashMap<String, String>> userPagerReqHashMap = new PagerReq<>(1, 10, hashMap);

    String json = customGson.toJson(userPagerReq);

    System.out.println(json);

    System.out.println(customGson.toJson(userPagerReqHashMap));



    assertEquals(4, 2 + 2);
  }

  class PagerReq<T> {
    @SerializedName("PNO") int pageNo;
    int pageSize;
    T entity;

    public PagerReq(int pageNo, int pageSize, T entity) {
      this.pageNo = pageNo;
      this.pageSize = pageSize;
      this.entity = entity;
    }
  }

  class User {
    String name;
    int age;

    public User(String name, int age) {
      this.name = name;
      this.age = age;
    }
  }

}