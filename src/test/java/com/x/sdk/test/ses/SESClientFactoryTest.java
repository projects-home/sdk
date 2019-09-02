package com.x.sdk.test.ses;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.x.sdk.ses.ISearchClient;
import com.x.sdk.ses.SESClientFactory;
import com.x.sdk.ses.common.JsonBuilder;
import com.x.sdk.ses.vo.*;
import com.x.sdk.util.DateUtil;
import org.junit.*;

import java.io.IOException;
import java.util.*;

import static org.junit.Assert.assertTrue;

/**
 * Created by mayt on 2018/1/25.
 */
public class SESClientFactoryTest {
    static ISearchClient client = null;
    static String indexName = "1330207074";
    static String ns = "com.x.sdk.ses.test";

    public static void main(String[] args) {
        User user = new User();
        user.setUserId("1");
        user.setAge(2);
        user.setCreated(DateUtil.getDate());
        user.setName("张三");
        client = SESClientFactory.getSearchClient(ns);
        Assert.assertTrue(client.insert(user));
        Assert.assertTrue(client.delete("1"));
    }

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        client = SESClientFactory.getSearchClient(ns);
    }

    @AfterClass
    public static void tearDownAfterClass() throws Exception {
        client = null;
    }

    @Ignore
    @Before
    public void setUp() throws Exception {
        if (client.existIndex(indexName))
            client.deleteIndex(indexName);
        client.createIndex(indexName, 1, 1);
        String mapping = "{"
                + "   \""
                + indexName
                + "\" : {"
                + "     \"properties\" : {"
                + "     	\"userId\" :  {\"type\" : \"string\", \"store\" : \"yes\",\"index\": \"not_analyzed\"},"
                + "       	\"name\" : {\"type\" : \"string\", \"store\" : \"yes\"/*,\"analyzer\":\"ik_max_word\"*/},"
                + "       	\"age\" : {\"type\" : \"integer\"},"
                + "       	\"created\" : {\"type\" : \"date\", \"format\" : \"strict_date_optional_time||epoch_millis\"}"
                + "     }" + "   }" + " }";

        client.addMapping(indexName, indexName, mapping, "userId");
    }

    @After
    public void tearDown() throws Exception {
        if (client.existIndex(indexName))
            client.deleteIndex(indexName);
    }

    @Test
    public void testInsertMapOfStringObject() {
        Map<String, Object> data = new HashMap<>();
        data.put("userId", "102");
        data.put("name", "这是一个中文测试句子，this is a text");
        data.put("age", 29);
        data.put("created", DateUtil.getDateString(DateUtil.DATE_FORMAT));
        assertTrue(client.insert(data));
    }

    @Test
    public void testInsertString() {
        String data = "{\"userId\":103,\"name\":\"爱丢恶化缺乏\",\"age\":30,\"created\":\"2016-06-17T23:15:09\"}";
        assertTrue(client.insert(data));
    }

    @Test
    public void testInsertT() {
        User user = new User("105", "当萨菲罗斯开发送发了多少分旬", 31, new Date());
        assertTrue(client.insert(user));
    }

    @Test
    public void testInsertJsonBuilder() throws IOException, Exception {
        JsonBuilder jsonBuilder = new JsonBuilder().startObject()
                .field("userId", 106).field("name", "每逢佳节倍思亲").field("age", 31)
                .field("created", new Date()).endObject();
        assertTrue(client.insert(jsonBuilder));
    }

    @Test
    public void testDeleteString() {
        User user = new User("105", "当萨菲罗斯开发送发了多少分旬", 31, new Date());
        client.insert(user);
        assertTrue(client.delete("105"));
    }

    @Test
    public void testBulkDelete() {
        User user = null;
        user = new User("105", "当萨菲罗斯开发送发了多少分旬", 31, new Date());
        client.insert(user);
        user = new User("106", "当萨菲罗斯开发送发了多少分旬", 31, new Date());
        client.insert(user);
        user = new User("107", "当萨菲罗斯开发送发了多少分旬", 31, new Date());
        List<String> ids = new ArrayList<>();
        ids.add("105");
        ids.add("106");
        ids.add("107");
        assertTrue(client.bulkDelete(ids));
    }

    @Test
    public void testDeleteListOfSearchCriteria() {
        User user = null;
        user = new User("105", "当萨菲罗斯开发送发了多少分旬123", 31, new Date());
        client.insert(user);
        user = new User("106", "当萨菲罗斯开发送发了多少分旬萨达", 32, new Date());
        client.insert(user);
        user = new User("107", "当萨菲罗斯开发送发了多少分旬萨达", 33, new Date());
        client.insert(user);
        client.refresh();
        List<SearchCriteria> searchCriterias = new ArrayList<>();
        SearchCriteria searchCriteria = new SearchCriteria();
        searchCriteria.setField("name");
        List<Object> values = new ArrayList<Object>();
        values.add("开发");
        searchCriteria.setFieldValue(values);
        searchCriterias.add(searchCriteria);
        assertTrue(client.delete(searchCriterias));
    }

    @Test
    public void testClean() {
        User user = null;
        user = new User("105", "当萨菲罗斯开发送发了多少分旬123", 31, new Date());
        client.insert(user);
        user = new User("106", "当萨菲罗斯开发送发了多少分旬萨达", 32, new Date());
        client.insert(user);
        user = new User("107", "当萨菲罗斯开发送发了多少分旬萨达", 33, new Date());
        client.insert(user);
        assertTrue(client.clean());
        user = new User("105", "当萨菲罗斯开发送发了多少分旬123", 31, new Date());
        assertTrue(client.insert(user));
    }

    @Test
    public void testUpdateStringMapOfStringObject() {
        User user = null;
        user = new User("105", "当萨菲罗斯开发送发了多少分旬123", 31, new Date());
        client.insert(user);
        Map<String, String> data = new HashMap<>();
        data.put("name", "不知道改变了没有");
        assertTrue(client.update("105", data));
    }

    @Test
    public void testUpdateStringString() {
        User user = null;
        user = new User("105", "当萨菲罗斯开发送发了多少分旬123", 31, new Date());
        client.insert(user);
        String json = "{\"name\":\"不知道改变了没有\"}";
        assertTrue(client.update("105", json));
    }

    @Test
    public void testUpdateStringT() {
        User user = null;
        user = new User("105", "当萨菲罗斯开发送发了多少分旬123", 31, new Date());
        client.insert(user);
        user.setName("不知道改变了没有");
        assertTrue(client.update("105", user));
    }

    @Test
    public void testUpdateStringJsonBuilder() throws Throwable, Exception {
        User user = null;
        user = new User("105", "当萨菲罗斯开发送发了多少分旬123", 31, new Date());
        client.insert(user);
        JsonBuilder jsonBuilder = new JsonBuilder().startObject()
                .field("name", "每逢佳节倍思亲").endObject();
        assertTrue(client.update("105", jsonBuilder));
    }

    @Test
    public void testUpsertStringMapOfStringObject() {
        Map<String, Object> data = new HashMap<>();
        data.put("name", "不知道改变了没有");
        assertTrue(client.upsert("105", data));
        data.put("name", "不知道改变了没有123");
        data.put("age", 32);
        assertTrue(client.upsert("105", data));
    }

    @Test
    public void testUpsertStringString() {
        String json = "{\"name\":\"不知道改变了没有\"}";
        assertTrue(client.upsert("105", json));
        json = "{\"name\":\"不知道改变了没有\",\"age\":34}";
        assertTrue(client.upsert("105", json));
    }

    @Test
    public void testUpsertStringT() {
        User user = new User();
        user.setUserId("105");
        user.setName("不知道改变了没有");
        assertTrue(client.upsert("105", user));
    }

    @Test
    public void testUpsertStringJsonBuilder() throws Throwable, Exception {
        JsonBuilder jsonBuilder = new JsonBuilder().startObject()
                .field("name", "每逢佳节倍思亲").endObject();
        assertTrue(client.upsert("105", jsonBuilder));
        jsonBuilder = new JsonBuilder().startObject().field("age", 56)
                .endObject();
        assertTrue(client.upsert("105", jsonBuilder));
    }

    @Test
    public void testBulkMapInsert() {
        Map<String, Object> data1 = new HashMap<>();
        data1.put("userId", "101");
        data1.put("name", "这是一个中文测试句子，this is a text1");
        data1.put("age", 29);
        data1.put("created", DateUtil.getDateString(DateUtil.DATE_FORMAT));
        Map<String, Object> data2 = new HashMap<>();
        data2.put("userId", "102");
        data2.put("name", "这是一个中文测试句子，this is a text2");
        data2.put("age", 29);
        data2.put("created", DateUtil.getDateString(DateUtil.DATE_FORMAT));
        Map<String, Object> data3 = new HashMap<>();
        data3.put("userId", "103");
        data3.put("name", "这是一个中文测试句子，this is a text3");
        data3.put("age", 29);
        data3.put("created", DateUtil.getDateString(DateUtil.DATE_FORMAT));
        List<Map<String, Object>> data = new ArrayList<>();
        data.add(data1);
        data.add(data2);
        data.add(data3);
        assertTrue(client.bulkMapInsert(data));

    }

    @Test
    public void testBulkJsonInsert() {
        List<String> datas = new ArrayList<>();
        String data1 = "{\"userId\":103,\"name\":\"当萨菲罗斯开发送发了多少分旬1234\",\"age\":30,\"created\":\"2016-06-17T23:15:09\"}";
        String data2 = "{\"userId\":104,\"name\":\"当萨菲罗斯开发送发了多少分旬1235\",\"age\":30,\"created\":\"2016-06-17T23:15:09\"}";
        String data3 = "{\"userId\":101,\"name\":\"当萨菲罗斯开发送发了多少分旬1236\",\"age\":30,\"created\":\"2016-06-17T23:15:09\"}";
        datas.add(data1);
        datas.add(data2);
        datas.add(data3);
        assertTrue(client.bulkJsonInsert(datas));
    }

    @Test
    public void testBulkInsertListOfT() {
        List<User> datas = new ArrayList<>();
        User user1 = new User("105", "当萨菲罗斯开发送发了多少分旬1234", 31, new Date());
        User user2 = new User("106", "当萨菲罗斯开发送发了多少分旬1235", 41, new Date());
        User user3 = new User("107", "当萨菲罗斯开发送发了多少分旬1236", 51, new Date());
        datas.add(user1);
        datas.add(user2);
        datas.add(user3);
        assertTrue(client.bulkInsert(datas));
    }

    @Test
    public void testBulkInsertSetOfJsonBuilder() throws Throwable, Exception {
        JsonBuilder jsonBuilder1 = new JsonBuilder().startObject()
                .field("userId", 106).field("name", "每逢佳节倍思亲").field("age", 31)
                .field("created", new Date()).endObject();
        JsonBuilder jsonBuilder2 = new JsonBuilder().startObject()
                .field("userId", 107).field("name", "每逢佳节倍思亲").field("age", 31)
                .field("created", new Date()).endObject();
        JsonBuilder jsonBuilder3 = new JsonBuilder().startObject()
                .field("userId", 108).field("name", "每逢佳节倍思亲").field("age", 31)
                .field("created", new Date()).endObject();
        Set<JsonBuilder> datas = new HashSet<>();
        datas.add(jsonBuilder1);
        datas.add(jsonBuilder2);
        datas.add(jsonBuilder3);
        assertTrue(client.bulkInsert(datas));
    }

    @Test
    public void testBulkMapUpdate() {
        testBulkMapInsert();
        List<String> ids = new ArrayList<>();
        Map<String, Object> data1 = new HashMap<>();
        ids.add("101");
        data1.put("name", "这是一个中文测试句子123，this is a text1");
        Map<String, Object> data2 = new HashMap<>();
        ids.add("102");
        data2.put("name", "这是一个中文测试句子1233，this is a text2");
        Map<String, Object> data3 = new HashMap<>();
        ids.add("103");
        data3.put("name", "这是一个中文测试句子123456，this is a text3");
        List<Map<String, Object>> data = new ArrayList<>();
        data.add(data1);
        data.add(data2);
        data.add(data3);
        assertTrue(client.bulkMapUpdate(ids, data));
    }

    @Test
    public void testBulkJsonUpdate() {
        testBulkJsonInsert();
        List<String> ids = new ArrayList<>();
        ids.add("103");
        ids.add("104");
        ids.add("105");
        List<String> datas = new ArrayList<>();
        String data1 = "{\"name\":\"爱丢恶化缺乏11\",\"age\":30,\"created\":\"2016-06-17T23:15:09\"}";
        String data2 = "{\"name\":\"爱丢恶化缺乏22\",\"age\":30,\"created\":\"2016-06-17T23:15:09\"}";
        String data3 = "{\"name\":\"爱丢恶化缺乏33\",\"age\":30,\"created\":\"2016-06-17T23:15:09\"}";
        datas.add(data1);
        datas.add(data2);
        datas.add(data3);
        assertTrue(client.bulkJsonUpdate(ids, datas));
    }

    @Test
    public void testBulkUpdateListOfStringListOfT() {
        testBulkInsertListOfT();
        List<User> datas = new ArrayList<>();
        User user1 = new User();
        User user2 = new User();
        User user3 = new User();
        user1.setName("三发松岛枫1");
        user2.setName("三发松岛枫2");
        user3.setName("三发松岛枫3");
        datas.add(user1);
        datas.add(user2);
        datas.add(user3);
        List<String> ids = new ArrayList<>();
        ids.add("105");
        ids.add("106");
        ids.add("107");
        assertTrue(client.bulkUpdate(ids, datas));
    }

    @Test
    public void testBulkUpdateListOfStringSetOfJsonBuilder() throws Throwable {
        testBulkInsertSetOfJsonBuilder();
        JsonBuilder jsonBuilder1 = new JsonBuilder().startObject()
                .field("name", "每逢佳节倍思亲1").endObject();
        JsonBuilder jsonBuilder2 = new JsonBuilder().startObject()
                .field("name", "每逢佳节倍思亲2").endObject();
        JsonBuilder jsonBuilder3 = new JsonBuilder().startObject()
                .field("name", "每逢佳节倍思亲3").endObject();
        Set<JsonBuilder> datas = new HashSet<>();
        datas.add(jsonBuilder1);
        datas.add(jsonBuilder2);
        datas.add(jsonBuilder3);
        List<String> ids = new ArrayList<>();
        ids.add("105");
        ids.add("106");
        ids.add("107");
        assertTrue(client.bulkUpdate(ids, datas));
    }

    @Test
    public void testSearchBySQL() {
        testBulkInsertListOfT();
        client.refresh();
        String qry = "age:(>=10 AND <55)";
        Result<User> result = client.searchBySQL(qry, 0, 10, null, User.class);
        assertTrue(result.getCount() == 3);
        qry = "age:(>=10 AND <55) AND name:1234";
        result = client.searchBySQL(qry, 0, 10, null, User.class);
        assertTrue(result.getCount() == 1);
        qry = "age:(>=40 AND <55) ";
        result = client.searchBySQL(qry, 0, 10, null, User.class);
        assertTrue(result.getCount() == 2);
    }

    @Test
    public void testSearch() {
        testBulkInsertListOfT();
        client.refresh();
        List<SearchCriteria> searchCriterias = new ArrayList<>();
        SearchCriteria searchCriteria = new SearchCriteria("age", "31",
                new SearchOption());
        searchCriterias.add(searchCriteria);
        Result<User> result = client.search(searchCriterias, 0, 10, null,
                User.class);
        assertTrue(result.getCount() == 1);
        // 复杂查询，name含有“开发”或者含有“1234”且年龄在31-45之间的
        searchCriteria = new SearchCriteria();
        SearchCriteria subCriteria = new SearchCriteria("name", "开发",
                new SearchOption(SearchOption.SearchLogic.should, SearchOption.SearchType.querystring));
        searchCriteria.addSubCriteria(subCriteria);
        SearchCriteria subCriteria1 = new SearchCriteria("name", "1234",
                new SearchOption(SearchOption.SearchLogic.should, SearchOption.SearchType.querystring));
        searchCriteria.addSubCriteria(subCriteria1);
        searchCriterias.clear();
        searchCriterias.add(searchCriteria);
        SearchCriteria searchCriteria1 = new SearchCriteria();
        searchCriteria1.setOption(new SearchOption(SearchOption.SearchLogic.must,
                SearchOption.SearchType.range));
        searchCriteria1.setField("age");
        searchCriteria1.addFieldValue("31");
        searchCriteria1.addFieldValue("45");
        searchCriterias.add(searchCriteria1);
        result = client.search(searchCriterias, 0, 10, null, User.class);
        assertTrue(result.getCount() == 2);
    }

    @Test
    public void testSearchByDSL() {
        testBulkInsertListOfT();
        client.refresh();
        String qry = "{" + "\"query\": { " + "\"bool\": {" + "\"must\": ["
                + "  { \"match\": { \"name\":   \"开发\"}},"
                + "        { \"match\": { \"age\": 51 }}" + "],"
                + "\"filter\": [" + "{ \"term\":  { \"userId\": \"107\" }},"
                + "{ \"range\": { \"created\": { \"gte\": \"2016-06-20\" }}}"
                + "]" + "}" + "}" + "}";
        Result<User> result = client.searchByDSL(qry, 0, 10, null, User.class);
        assertTrue(result.getCount() == 1);
    }

    @Test
    public void testSearchByDSLString() {
        testBulkInsertListOfT();
        client.refresh();
        String qry = "{" + "\"query\": { " + "\"bool\": {" + "\"must\": ["
                + "  { \"match\": { \"name\":   \"开发\"}},"
                + "        { \"match\": { \"age\": 51 }}" + "],"
                + "\"filter\": [" + "{ \"term\":  { \"userId\": \"107\" }},"
                + "{ \"range\": { \"created\": { \"gte\": \"2016-06-20\" }}}"
                + "]" + "}" + "}" + "}";
        String result = client.searchByDSL(qry, 0, 10, null);
        Gson gson = new Gson();
        JsonObject json = gson.fromJson(result, JsonObject.class);
        assertTrue(json.get("count").getAsInt() == 1);
    }

    @Test
    public void getSuggestStringString() {
        String mapping = "{"
                + "   \""
                + indexName
                + "\" : {"
                + "  \"_all\": {"
                + "\"analyzer\": \"nGram_analyzer\","
                + "\"search_analyzer\": \"ik_max_word\","
                + "\"term_vector\": \"no\","
                + "\"store\": \"false\""
                + "},"
                + "     \"properties\" : {"
                + "     	\"userId\" :  {\"type\" : \"string\", \"store\" : \"yes\",\"index\": \"not_analyzed\"},"
                + "       	\"name\" : {\"type\" : \"string\", \"analyzer\":\"nGram_analyzer\"},"
                + "       	\"age\" : {\"type\" : \"integer\"},"
                + "       	\"created\" : {\"type\" : \"date\", \"format\" : \"strict_date_optional_time||epoch_millis\"}"
                + "     }" + "   }" + " }";
        if (client.existIndex(indexName)) {
            client.deleteIndex(indexName);
            client.createIndex(indexName, 3, 1);
            client.refresh();
        }
        client.addMapping(indexName, indexName, mapping);
        client.refresh();
        testBulkInsertListOfT();
        client.refresh();
        List<String> suggests = client.getSuggest("开", 10);
        assertTrue(suggests.size() == 3);
        suggests = client.getSuggest("4", 10);
        // 因为搜索所有的字段，只要含有就显示
        assertTrue(suggests.size() == 3);
    }

    @Test
    public void getSuggestStringStringString() {
        String mapping = "{"
                + "   \""
                + indexName
                + "\" : {"
                + "  \"_all\": {"
                + "\"analyzer\": \"nGram_analyzer\","
                + "\"search_analyzer\": \"ik_max_word\","
                + "\"term_vector\": \"no\","
                + "\"store\": \"false\""
                + "},"
                + "     \"properties\" : {"
                + "     	\"userId\" :  {\"type\" : \"string\", \"store\" : \"yes\",\"index\": \"not_analyzed\"},"
                + "       	\"name\" : {\"type\" : \"string\", \"analyzer\":\"nGram_analyzer\"},"
                + "       	\"age\" : {\"type\" : \"integer\"},"
                + "       	\"created\" : {\"type\" : \"date\", \"format\" : \"strict_date_optional_time||epoch_millis\"}"
                + "     }" + "   }" + " }";
        if (client.existIndex(indexName)) {
            client.deleteIndex(indexName);
            client.createIndex(indexName, 3, 1);
        }
        client.addMapping(indexName, indexName, mapping);
        testBulkInsertListOfT();
        client.refresh();
        List<String> suggests = client.getSuggest("name", "开", 10);
        assertTrue(suggests.size() == 3);
        suggests = client.getSuggest("name", "4", 10);
        // 因为搜索所有的字段，只要含有就显示
        assertTrue(suggests.size() == 1);
    }

    @Test
    public void testAggregateListOfSearchCriteriaString() {
        String mapping = "{" + "   \""
                + indexName
                + "\" : {"
                + "  \"_all\": {"
                + "\"analyzer\": \"nGram_analyzer\","
                + "\"search_analyzer\": \"ik_max_word\","
                + "\"term_vector\": \"no\","
                + "\"store\": \"false\""
                + "},"
                + "     \"properties\" : {"
                + "     	\"userId\" :  {\"type\" : \"string\", \"store\" : \"yes\",\"index\": \"not_analyzed\"},"
                + "       	\"name\" : {\"type\" : \"string\", \"analyzer\":\"nGram_analyzer\","
                + " \"fields\": {"
                + "     \"raw\": {"
                + "        \"type\": \"string\","
                + "        \"index\": \"not_analyzed\""
                + "     }}"
                + "},"
                + "       	\"age\" : {\"type\" : \"integer\"},"
                + "       	\"created\" : {\"type\" : \"date\", \"format\" : \"strict_date_optional_time||epoch_millis\"}"
                + "     }" + "   }" + " }";
        if (client.existIndex(indexName)) {
            client.deleteIndex(indexName);
            client.createIndex(indexName, 3, 1);
        }
        client.addMapping(indexName, indexName, mapping);
        testBulkInsertListOfT();
        client.refresh();
        List<SearchCriteria> searchCriterias = new ArrayList<>();
        SearchCriteria searchCriteria = new SearchCriteria();
        searchCriteria.setField("age");
        List<Object> values = new ArrayList<Object>();
        values.add("41");
        searchCriteria.setFieldValue(values);
        searchCriterias.add(searchCriteria);
        Result<Map<String, Long>> result = client.aggregate(searchCriterias,
                "name");
        assertTrue(result.getCount() == 1);
    }

    @Test
    public void testAggregateListOfSearchCriteriaListOfString() {
        String mapping = "{" + "   \""
                + indexName
                + "\" : {"
                + "  \"_all\": {"
                + "\"analyzer\": \"nGram_analyzer\","
                + "\"search_analyzer\": \"ik_max_word\","
                + "\"term_vector\": \"no\","
                + "\"store\": \"false\""
                + "},"
                + "     \"properties\" : {"
                + "     	\"userId\" :  {\"type\" : \"string\", \"store\" : \"yes\",\"index\": \"not_analyzed\"},"
                + "       	\"name\" : {\"type\" : \"string\", \"analyzer\":\"nGram_analyzer\","
                + " \"fields\": {"
                + "     \"raw\": {"
                + "        \"type\": \"string\","
                + "        \"index\": \"not_analyzed\""
                + "     }}"
                + "},"
                + "       	\"age\" : {\"type\" : \"integer\","
                + " \"fields\": {"
                + "     \"raw\": {"
                + "        \"type\": \"integer\","
                + "        \"index\": \"not_analyzed\""
                + "     }}"
                + "},"
                + "       	\"created\" : {\"type\" : \"date\", \"format\" : \"strict_date_optional_time||epoch_millis\"}"
                + "     }" + "   }" + " }";
        if (client.existIndex(indexName)) {
            client.deleteIndex(indexName);
            client.createIndex(indexName, 3, 1);
        }
        client.addMapping(indexName, indexName, mapping);
        testBulkInsertListOfT();
        testBulkJsonInsert();
        client.refresh();
        List<SearchCriteria> searchCriterias = new ArrayList<>();
        SearchCriteria searchCriteria = new SearchCriteria();
        searchCriteria.setField("name");
        List<Object> values = new ArrayList<Object>();
        values.add("开发");
        searchCriteria.setFieldValue(values);
        searchCriterias.add(searchCriteria);
        List<AggField> fields = new ArrayList<>();
        fields.add(new AggField("name"));
        fields.add(new AggField("age"));
        Result<List<AggResult>> result = client.aggregate(searchCriterias,
                fields);
        assertTrue(result.getAggs().size() == 7);
        // 测试一下嵌套
        fields = new ArrayList<>();
        AggField name = new AggField("name");
        List<AggField> subfields = new ArrayList<>();
        subfields.add(new AggField("age"));
        name.setSubAggs(subfields);
        fields.add(name);
        fields.add(new AggField("age"));
        result = client.aggregate(searchCriterias, fields);
        assertTrue(result.getAggs().size() == 7);
    }

    @Test
    public void testFullTextSearchWithFieldWithAgg() {
        String mapping = "{" + "   \""
                + indexName
                + "\" : {"
                + "  \"_all\": {"
                + "\"analyzer\": \"nGram_analyzer\","
                + "\"search_analyzer\": \"ik_max_word\","
                + "\"term_vector\": \"no\","
                + "\"store\": \"false\""
                + "},"
                + "     \"properties\" : {"
                + "     	\"userId\" :  {\"type\" : \"string\", \"store\" : \"yes\",\"index\": \"not_analyzed\"},"
                + "       	\"name\" : {\"type\" : \"string\", \"analyzer\":\"nGram_analyzer\","
                + " \"fields\": {"
                + "     \"raw\": {"
                + "        \"type\": \"string\","
                + "        \"index\": \"not_analyzed\""
                + "     }}"
                + "},"
                + "       	\"age\" : {\"type\" : \"integer\","
                + " \"fields\": {"
                + "     \"raw\": {"
                + "        \"type\": \"integer\","
                + "        \"index\": \"not_analyzed\""
                + "     }}"
                + "},"
                + "       	\"created\" : {\"type\" : \"date\", \"format\" : \"strict_date_optional_time||epoch_millis\"}"
                + "     }" + "   }" + " }";
        if (client.existIndex(indexName)) {
            client.deleteIndex(indexName);
            client.createIndex(indexName, 3, 1);
        }
        client.addMapping(indexName, indexName, mapping);
        testBulkInsertListOfT();
        testBulkJsonInsert();
        client.refresh();
        String text = "开发";
        List<AggField> fields = new ArrayList<>();
        fields.add(new AggField("name"));
        fields.add(new AggField("age"));
        List<String> qryFields = new ArrayList<>();
        qryFields.add("name");
        List<Sort> sorts = new ArrayList<>();
        Sort sort = new Sort("age", Sort.SortOrder.ASC);
        sorts.add(sort);
        Result<User> result = client.fullTextSearch(text, fields, 0, 10, sorts,
                User.class);
        assertTrue(result.getCount() == 6);
    }

    @Test
    public void testFullTextSearchWithAgg() {
        String mapping = "{" + "   \""
                + indexName
                + "\" : {"
                + "  \"_all\": {"
                + "\"analyzer\": \"nGram_analyzer\","
                + "\"search_analyzer\": \"ik_max_word\","
                + "\"term_vector\": \"no\","
                + "\"store\": \"false\""
                + "},"
                + "     \"properties\" : {"
                + "     	\"userId\" :  {\"type\" : \"string\", \"store\" : \"yes\",\"index\": \"not_analyzed\"},"
                + "       	\"name\" : {\"type\" : \"string\", \"analyzer\":\"nGram_analyzer\","
                + " \"fields\": {"
                + "     \"raw\": {"
                + "        \"type\": \"string\","
                + "        \"index\": \"not_analyzed\""
                + "     }}"
                + "},"
                + "       	\"age\" : {\"type\" : \"integer\","
                + " \"fields\": {"
                + "     \"raw\": {"
                + "        \"type\": \"integer\","
                + "        \"index\": \"not_analyzed\""
                + "     }}"
                + "},"
                + "       	\"created\" : {\"type\" : \"date\", \"format\" : \"strict_date_optional_time||epoch_millis\"}"
                + "     }" + "   }" + " }";
        if (client.existIndex(indexName)) {
            client.deleteIndex(indexName);
            client.createIndex(indexName, 3, 1);
        }
        client.addMapping(indexName, indexName, mapping);
        testBulkInsertListOfT();
        testBulkJsonInsert();
        client.refresh();
        String text = "开发";
        List<AggField> fields = new ArrayList<>();
        fields.add(new AggField("name"));
        fields.add(new AggField("age"));
        List<String> qryFields = new ArrayList<>();
        qryFields.add("name");
        Result<User> result = client.fullTextSearch(text, qryFields, fields, 0,
                10, null, User.class);
        assertTrue(result.getCount() == 6);
    }

    @Test
    public void testFullTextSearch() {
        testBulkInsertListOfT();
        client.refresh();
        String text = "开发";

        Result<User> result = client.fullTextSearch(text, 0, 10, null,
                User.class);
        assertTrue(result.getCount() == 3);
    }

    @Test
    public void testGetByIdStringClassOfT() {
        testBulkInsertListOfT();
        client.refresh();
        User user = client.getById("105", User.class);
        assertTrue(user.getAge() == 31);
    }

    @Test
    public void testGetByIdString() {
        testBulkInsertListOfT();
        client.refresh();
        String user = client.getById("105");
        Gson gson = new Gson();
        assertTrue(gson.fromJson(user, JsonObject.class).get("age").getAsInt() == 31);
    }

    @Test
    public void testCreateIndex() {
        // 先删除
        client.deleteIndex(indexName);
        assertTrue(client.createIndex("user", 2, 1));
    }

    @Test
    public void testDeleteIndex() {
        assertTrue(client.deleteIndex("user"));
    }

    @Test
    public void testExistIndex() {
        assertTrue(client.existIndex(indexName));
    }

    @Test
    public void testAddMapping() {
        if (client.existIndex(indexName)) {
            client.deleteIndex(indexName);
            client.createIndex(indexName, 3, 1);
        }
        String mapping = "{"
                + "   \"userInfo\" : {"
                + "     \"properties\" : {"
                + "     	\"userId\" :  {\"type\" : \"string\", \"store\" : \"yes\",\"index\": \"not_analyzed\"},"
                + "       	\"name\" : {\"type\" : \"string\", \"store\" : \"yes\",\"analyzer\":\"ik_smart\"},"
                + "       	\"age\" : {\"type\" : \"integer\"},"
                + "       	\"created\" : {\"type\" : \"date\", \"format\" : \"strict_date_optional_time||epoch_millis\"}"
                + "     }" + "   }" + " }";

        assertTrue(client.addMapping(indexName, indexName, mapping));

        String mapping1 = "{"
                + "     \"properties\" : {"
                + "     	\"userId\" :  {\"type\" : \"string\", \"store\" : \"yes\",\"index\": \"not_analyzed\"},"
                + "       	\"name\" : {\"type\" : \"string\", \"store\" : \"yes\",\"analyzer\":\"ik_smart\"},"
                + "       	\"age\" : {\"type\" : \"integer\"},"
                + "       	\"created\" : {\"type\" : \"date\", \"format\" : \"strict_date_optional_time||epoch_millis\"}"
                + "     }" + " }";

        assertTrue(client.addMapping(indexName, "userInfo1", mapping1));
        String mapping2 = "{"
                + "     	\"userId\" :  {\"type\" : \"string\", \"store\" : \"yes\",\"index\": \"not_analyzed\"},"
                + "       	\"name\" : {\"type\" : \"string\", \"store\" : \"yes\",\"analyzer\":\"ik_smart\"},"
                + "       	\"age\" : {\"type\" : \"integer\"},"
                + "       	\"created\" : {\"type\" : \"date\", \"format\" : \"strict_date_optional_time||epoch_millis\"}"
                + " }";

        assertTrue(client.addMapping(indexName, "userInfo2", mapping2));
    }
}
