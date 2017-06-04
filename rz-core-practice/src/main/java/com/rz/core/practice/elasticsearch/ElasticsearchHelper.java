package com.rz.core.practice.elasticsearch;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.security.auth.message.callback.PrivateKeyCallback.AliasRequest;

import org.apache.http.entity.ContentType;
import org.elasticsearch.action.ActionFuture;
import org.elasticsearch.action.admin.indices.alias.Alias;
import org.elasticsearch.action.admin.indices.alias.IndicesAliasesRequest;
import org.elasticsearch.action.admin.indices.alias.IndicesAliasesResponse;
import org.elasticsearch.action.admin.indices.alias.get.GetAliasesRequest;
import org.elasticsearch.action.admin.indices.alias.get.GetAliasesResponse;
import org.elasticsearch.action.admin.indices.create.CreateIndexRequest;
import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.elasticsearch.action.admin.indices.get.GetIndexRequest;
import org.elasticsearch.action.admin.indices.get.GetIndexResponse;
import org.elasticsearch.action.admin.indices.mapping.get.GetMappingsRequest;
import org.elasticsearch.action.admin.indices.mapping.get.GetMappingsResponse;
import org.elasticsearch.action.admin.indices.mapping.put.PutMappingRequest;
import org.elasticsearch.action.admin.indices.mapping.put.PutMappingResponse;
import org.elasticsearch.action.admin.indices.settings.get.GetSettingsRequest;
import org.elasticsearch.action.admin.indices.settings.get.GetSettingsResponse;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.count.CountRequest;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexRequestBuilder;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.action.support.IndicesOptions;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.cluster.metadata.AliasMetaData;
import org.elasticsearch.cluster.metadata.MappingMetaData;
import org.elasticsearch.common.collect.ImmutableOpenMap;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.index.get.GetField;
import org.elasticsearch.index.get.GetResult;
import org.elasticsearch.index.mapper.MapperBuilders;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.QueryStringQueryBuilder;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;

import com.alibaba.fastjson.JSONObject;
import com.carrotsearch.hppc.cursors.ObjectObjectCursor;

// Error: Alias [asdAlias] has more than one indices associated with it [[newindex, newindex1]], can't execute a single index op
public class ElasticsearchHelper {
	private static String clusterName = "elasticsearch";
	private static String hostName = "192.168.36.161";//"192.168.127.138";//"192.168.36.214";//
	private static int port = 10293;
	private static String indexName = "portal_0000000";//"crm";//
	private static String aliasName = "portal";
	private static String typeName = "article";
	
	public static void main(String[] args){
		try {
			ElasticsearchHelper.Test();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		System.out.println("End_ElasticsearchHelper...");
	}
	
	public static void Test() throws Exception {
		Settings settings = Settings.builder().put("cluster.name", ElasticsearchHelper.clusterName).build();
		TransportClient client = TransportClient.builder().settings(settings).build();
		client.addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName(ElasticsearchHelper.hostName), ElasticsearchHelper.port));
		
//		// get mapping
//		GetMappingsRequest getMappingsRequest = new GetMappingsRequest();
//		getMappingsRequest.indices(ElasticsearchHelper.indexName);
//		getMappingsRequest.types(ElasticsearchHelper.typeName);
//		GetMappingsResponse getMappingsResponse = client.admin().indices().getMappings(getMappingsRequest).actionGet();
//		//ImmutableOpenMap<String, ImmutableOpenMap<String, MappingMetaData>> mappings = getMappingsResponse.getMappings();
//		
		GetIndexRequest getIndexRequest = new GetIndexRequest();
		getIndexRequest.indices(ElasticsearchHelper.indexName);
		//getIndexRequest.types(ElasticsearchHelper.typeName);
		GetIndexResponse getIndexResponse = client.admin().indices().getIndex(getIndexRequest).actionGet();
		ImmutableOpenMap<String, ImmutableOpenMap<String, MappingMetaData>> mappings = getIndexResponse.getMappings();		
		for (ObjectObjectCursor<String, ImmutableOpenMap<String, MappingMetaData>> objectObjectCursor : mappings) {
			
		}
		
		// get settings
		GetSettingsRequest req = new GetSettingsRequest().indices(ElasticsearchHelper.indexName);
        GetSettingsResponse resp = client.admin().indices().getSettings(req).actionGet();
        ImmutableOpenMap<String, Settings> indexSettings = resp.getIndexToSettings();
        
		Map<String, String> map = null;
		//ImmutableOpenMap<String, Settings> indexSettings = getIndexResponse.getSettings();
        for (ObjectObjectCursor<String, Settings> objectObjectCursor : indexSettings) {
            System.out.println(objectObjectCursor.key + "---------------------");
            map = objectObjectCursor.value.getAsMap();
            for (Map.Entry<String, String> setting : map.entrySet()) {
                System.out.println(setting.getKey() + ": " + setting.getValue());
            }
        }
		
		Iterator<ObjectObjectCursor<String, ImmutableOpenMap<String, MappingMetaData>>> aliasIterator = mappings.iterator();
//		while(aliasIterator.hasNext()){
//			ObjectObjectCursor<String, ImmutableOpenMap<String, MappingMetaData>> aliasObjectObjectCursor = aliasIterator.next();
//			
//			System.out.println(aliasObjectObjectCursor.key);
//			
//			Iterator<ObjectObjectCursor<String, MappingMetaData>> mappingIterator = aliasObjectObjectCursor.value.iterator();
//			while(true == mappingIterator.hasNext()){
//				ObjectObjectCursor<String, MappingMetaData> mappingObjectObjectCursor = mappingIterator.next();
//				System.out.println("    " + mappingObjectObjectCursor.key + ": " + mappingObjectObjectCursor.value.source().toString());
//			}
//			
////			for (AliasMetaData aliasMetaData : indexAliasesMap.value) {
////				if (true == StringUtils.equalsIgnoreCase(alias, aliasMetaData.getAlias())) {
////					if (null == indexName) {
////						indexName = indexAliasesMap.key;
////					} else {
////						throw new Exception("There are two index(" + indexName + ") and (" + indexAliasesMap.key
////								+ ") are map to alias(" + alias + ").");
////					}
////				}
////			}
//		}
		
		// create index by mapping
		CreateIndexRequest createIndexRequest = new CreateIndexRequest();
		createIndexRequest.index("testindex");
		map.put("index.refresh_interval", "-1");
		createIndexRequest.settings(map);
		//createIndexRequest.mapping("article", "{\"article\": {\"dynamic\": \"strict\",\"_all\": {\"enabled\": false},\"properties\": {\"title\": {\"type\": \"string\"},\"pubDate\": {\"format\": \"strict_date_optional_time||epoch_millis\",\"type\": \"date\"},\"content\": {\"type\": \"string\"},\"dateAdded\": {\"format\": \"strict_date_optional_time||epoch_millis\",\"type\": \"date\"},\"lastUpdateTime\": {\"format\": \"strict_date_optional_time||epoch_millis\",\"type\": \"date\"}}}}");
		CreateIndexResponse createIndexResponse = client.admin().indices().create(createIndexRequest).actionGet();
		
//		PutMappingRequest putMappingRequest = new PutMappingRequest();
//		putMappingRequest.indices(new String[] {"houhouIndex"});
//		putMappingRequest.type("default111");
//		putMappingRequest.source("{'default111':{'properties':{'leader':{'type':'long'},'leaderType':{'type':'long'},'tags':{'type':'long'},'update_date':{'type':'date','format':'strict_date_optional_time||epoch_millis'}}}}");
//		PutMappingResponse putMappingResponse = client.admin().indices().putMapping(putMappingRequest).actionGet();
		
//		GetAliasesRequest getAliasesRequest = new GetAliasesRequest();		
//		GetAliasesResponse getAliasesResponse = client.admin().indices().getAliases(getAliasesRequest).actionGet();
//		ImmutableOpenMap<String, List<AliasMetaData>> asd = getAliasesResponse.getAliases();
//		List<AliasMetaData> aliasMetaDatum = asd.getOrDefault("newindex", null);
//		for(AliasMetaData aliasMetaData : aliasMetaDatum){
//			System.out.println(aliasMetaData.getAlias());
//		}
		
		
		
//		CountRequest countRequest = new CountRequest();
//		countRequest.
//		client.count(request)
		//client.inde

		// SearchRequestBuilder builder =
		// client.prepareSearch("comment_index").setTypes("comment_ugc").setSearchType(SearchType.DEFAULT).setFrom(0).setSize(100);
		// BoolQueryBuilder qb = QueryBuilders.boolQuery().must(new
		// QueryStringQueryBuilder("北京").field("body"))
		// .should(new QueryStringQueryBuilder("太多").field("body"));
		// builder.setQuery(qb);
		// SearchResponse response = builder.execute().actionGet();
		// System.out.println(" " + response);
		// System.out.println(response.getHits().getTotalHits());

		XContentBuilder xContentBuilder = null;
//		IndexResponse indexResponse = null;
//		xContentBuilder = XContentFactory.jsonBuilder().startObject()
//				.field("leader", 66666)
//				.field("leaderType", 45)
//				.field("tags", Arrays.asList(12345, 2222222, Integer.MIN_VALUE, Integer.MAX_VALUE))
//				.field("update_date", new Date()).endObject();
//	    indexResponse = client.prepareIndex(ElasticsearchHelper.indexName, ElasticsearchHelper.typeName, "10000").setSource(xContentBuilder).setId("2").get();	
	    
//		xContentBuilder = XContentFactory.jsonBuilder().startObject()
//				.field("leader", 66666)
//				.field("leaderType", 45)
//				.field("tags", Arrays.asList(12345, 2222222, Integer.MIN_VALUE, Integer.MAX_VALUE))
//				.field("update_date", new Date()).endObject();
//		indexResponse = client.prepareIndex("newindex", ElasticsearchHelper.typeName, "10000").setSource(xContentBuilder).setId("3").get();	
//	    IndicesAliasesRequest indicesAliasesRequest = new IndicesAliasesRequest();
//		indicesAliasesRequest.addAlias("asdAlias", "newindex");
//		IndicesAliasesResponse indicesAliasesResponse = client.admin().indices().aliases(indicesAliasesRequest).actionGet();
		
//		xContentBuilder = XContentFactory.jsonBuilder().startObject()
//				.field("leader", 66666)
//				.field("leaderType", 45)
//				.field("tags", Arrays.asList(12345, 2222222, Integer.MIN_VALUE, Integer.MAX_VALUE))
//				.field("update_date", new Date()).endObject();
//		indexResponse = client.prepareIndex("newindex1", ElasticsearchHelper.typeName, "10000").setSource(xContentBuilder).setId("4").get();	
//	    IndicesAliasesRequest indicesAliasesRequest = new IndicesAliasesRequest();
//		indicesAliasesRequest.addAlias("asdAlias", "newindex1");
//		IndicesAliasesResponse indicesAliasesResponse = client.admin().indices().aliases(indicesAliasesRequest).actionGet();

//		GetRequest getRequest = new GetRequest();
//		getRequest.index("asdAlias"); // ElasticsearchHelper.indexName
//		getRequest.type(ElasticsearchHelper.typeName);
//		getRequest.id("4");
//		ActionFuture<GetResponse> getResponseActionFuture = client.get(getRequest);
//		GetResponse getResponse = getResponseActionFuture.actionGet();
//		System.out.println(getResponse.getSourceAsString());
	    
//		BulkRequest bulkRequest = new BulkRequest();		
//		for(int i = 0; i < 100000; i++){
//			xContentBuilder = XContentFactory.jsonBuilder().startObject()
//				.field("leader", 100000000 + i)
//				.field("leaderType", 45)
//				.field("tags", Arrays.asList(12345, 2222222, Integer.MIN_VALUE, Integer.MAX_VALUE))
//				.field("update_date", new Date()).endObject();
//			
//			IndexRequest indexRequest = new IndexRequest();
//			indexRequest.index(ElasticsearchHelper.indexName);
//			indexRequest.type(ElasticsearchHelper.typeName);
//			indexRequest.id(String.valueOf(i));
//			indexRequest.source(xContentBuilder);
//			bulkRequest.add(indexRequest);
//		}
//		long begin = System.currentTimeMillis();
//		ActionFuture<BulkResponse> bulkResponseActionFuture = client.bulk(bulkRequest);
//		BulkResponse bulkResponse = bulkResponseActionFuture.actionGet();
//		System.out.println(System.currentTimeMillis() - begin);
		
//		BulkRequestBuilder bulk = client.prepareBulk();
//        IndexRequestBuilder irb = client.prepareIndex("testindex_new", "article", "697362");
//        //irb.setSource("{\"_id\":\"697362\",\"autoId\":697362,\"content\":\"\",\"dateAdded\":\"2012-07-11T21:52:20+0800\",\"lastUpdateTime\":\"2012-07-11T21:52:20+0800\",\"pubDate\":\"2014-09-19T21:50:35+0800\",\"title\":\"自荐书的英文怎么说\"}");
//        irb.setSource("{\"content\":\"\",\"dateAdded\":\"2012-07-11T21:52:20+0800\",\"lastUpdateTime\":\"2012-07-11T21:52:20+0800\",\"pubDate\":\"2014-09-19T21:50:35+0800\",\"title\":\"自荐书的英文怎么说\"}");
//        bulk.add(irb);
//        BulkResponse bulkResponse = bulk.execute().actionGet();
//        System.out.println(bulkResponse.hasFailures());
//        System.out.println(bulkResponse.buildFailureMessage());
        

//		int length = 100;
//		Thread[] thread = new Thread[length];
//		for(int j = 0; j < length; j++){
//			thread[j] = new Thread(() -> {
//				for(int i = 0; i < 5000; i++){
//					XContentBuilder tempXContentBuilder = null;
//					try{
//						tempXContentBuilder = XContentFactory.jsonBuilder().startObject()
//							.field("user", String.valueOf(Thread.currentThread().getName()) + String.valueOf(i))
//							//.field("leaderType", 45)
//							.field("tags", Arrays.asList("12345", "2222222", "Integer.MIN_VALUE", "Integer.MAX_VALUE"))
//							.field("update_date", new Date()).endObject();
//					}
//					catch(Exception exception){
//						System.out.println(exception.getMessage());
//					}
//					IndexRequest indexRequest = new IndexRequest();
//					indexRequest.index(ElasticsearchHelper.indexName);
//					indexRequest.type(ElasticsearchHelper.typeName);
//					indexRequest.id(String.valueOf(Thread.currentThread().getName()) + String.valueOf(i));
//					indexRequest.source(tempXContentBuilder);
//					ActionFuture<IndexResponse> indexRequestActionFuture = client.index(indexRequest);
//					IndexResponse indexResponse = indexRequestActionFuture.actionGet();
//					
////					UpdateRequest updateRequest = new UpdateRequest();
////					updateRequest.index(ElasticsearchHelper.indexName);
////					updateRequest.type(ElasticsearchHelper.typeName);
////					updateRequest.id(String.valueOf(Thread.currentThread().getName()) + String.valueOf(i));
////					updateRequest.doc(tempXContentBuilder);
////					ActionFuture<UpdateResponse> updateResponseActionFuture = client.update(updateRequest);
////					UpdateResponse updateResponse = updateResponseActionFuture.actionGet();
//					
////					GetRequest getRequest = new GetRequest();
////					getRequest.index(ElasticsearchHelper.indexName);
////					getRequest.type(ElasticsearchHelper.typeName);
////					getRequest.id(String.valueOf(Thread.currentThread().getName()) + String.valueOf(i));
////					getRequest.fields("user", "tags", "update_date");
////					ActionFuture<GetResponse> getResponseActionFuture = client.get(getRequest);
////					GetResponse getResponse = getResponseActionFuture.actionGet();
//					
////					SearchRequest searchRequest = new SearchRequest();
////					searchRequest.indices(ElasticsearchHelper.indexName);
////					searchRequest.types(ElasticsearchHelper.typeName);
////					SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
////					searchSourceBuilder.query(QueryBuilders.termQuery("user", String.valueOf(Thread.currentThread().getName()) + String.valueOf(i)));
////					searchRequest.source(searchSourceBuilder);
////					ActionFuture<SearchResponse> searchResponseActionFuture = client.search(searchRequest);
////					SearchResponse searchResponse = searchResponseActionFuture.actionGet();
//				}
//		    }, String.valueOf(j) + "_");
//		}
//		
//		long begin = System.currentTimeMillis();
//		for(int i = 0; i < length; i++){
//			thread[i].start();
//		}
//		for(int i = 0; i < length; i++){
//			thread[i].join();
//		}
//		System.out.println(System.currentTimeMillis() - begin);
		
//		IndexRequest indexRequest = new IndexRequest();
//		indexRequest.index(ElasticsearchHelper.indexName);
//		indexRequest.type(ElasticsearchHelper.typeName);
//		indexRequest.id("2");
//		indexRequest.source(xContentBuilder);
//		ActionFuture<IndexResponse> indexRequestActionFuture = client.index(indexRequest);
//		IndexResponse indexResponse = indexRequestActionFuture.actionGet();
		
		
//	    IndicesAliasesRequest indicesAliasesRequest = new IndicesAliasesRequest();
//	    indicesAliasesRequest.addAlias("fristAlias", ElasticsearchHelper.indexName);
//	    indicesAliasesRequest.addAlias("secondAlias", ElasticsearchHelper.indexName);
//	    indicesAliasesRequest.addAlias("thirdAlias", ElasticsearchHelper.indexName);
//	    IndicesAliasesResponse indicesAliasesResponse = client.admin().indices().aliases(indicesAliasesRequest).actionGet();
		
//	    IndicesAliasesRequest indicesAliasesRequest = new IndicesAliasesRequest();
//		indicesAliasesRequest.removeAlias(ElasticsearchHelper.indexName, "thirdAlias");
//		IndicesAliasesResponse indicesAliasesResponse = client.admin().indices().aliases(indicesAliasesRequest).actionGet();
//	    System.out.println(indicesAliasesRequest.toString());
		
//		IndicesAliasesRequest indicesAliasesRequest = new IndicesAliasesRequest();
//	    indicesAliasesRequest.addAlias("asdAlias", "newindex1");
//	    indicesAliasesRequest.removeAlias("newindex", "asdAlias");
//	    IndicesAliasesResponse indicesAliasesResponse = client.admin().indices().aliases(indicesAliasesRequest).actionGet();
		
//		GetRequest getRequest = new GetRequest();
//		getRequest.index(ElasticsearchHelper.aliasName); // ElasticsearchHelper.indexName
//		getRequest.type(ElasticsearchHelper.typeName);
//		getRequest.id("2");
//		ActionFuture<GetResponse> getResponseActionFuture = client.get(getRequest);
//		GetResponse getResponse = getResponseActionFuture.actionGet();
//		System.out.println(getResponse.getSourceAsString());

//		UpdateRequest updateRequest = new UpdateRequest();
//		updateRequest.index(ElasticsearchHelper.indexName);
//		updateRequest.type(ElasticsearchHelper.typeName);
//		updateRequest.id("2");
//		updateRequest.fields("user", "tags", "update_date");
//		updateRequest.doc(xContentBuilder);
//		ActionFuture<UpdateResponse> updateResponseActionFuture = client.update(updateRequest);
//		UpdateResponse updateResponse = updateResponseActionFuture.actionGet();
	    
//		SearchRequest searchRequest = new SearchRequest();
//		searchRequest.indices(ElasticsearchHelper.indexName);
//		searchRequest.types(ElasticsearchHelper.typeName);
//		SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
//		searchSourceBuilder.query(QueryBuilders.termQuery("user", "100000026"));
//		searchRequest.source(searchSourceBuilder);
//		ActionFuture<SearchResponse> searchResponseActionFuture = client.search(searchRequest);
//		SearchResponse searchResponse = searchResponseActionFuture.actionGet();
//		System.out.println(searchResponse.toString());
		
		// GetResult getResult = updateResponse.getGetResult();
		// Map<String, GetField> fields = getResult.getFields();
		// if (null != fields) {
		// System.out.println(fields.size());
		// for (Entry<String, GetField> field : fields.entrySet()) {
		// System.out.println(field.getKey() + " " +
		// field.getValue().getValue());
		// }
		// }

		// client.prepareUpdate(ElasticsearchHelper.indexName, ElasticsearchHelper.typeName, id)
		// System.out.println(indexResponse.getIndex());

		// IndexRequest indexRequest = client.prepareIndex(ElasticsearchHelper.indexName,
		// ElasticsearchHelper.typeName).request();
		// String[] assd = indexRequest.indices();
		// for (String aaa : assd) {
		// System.out.println(aaa);
		// }
		//
		// String indexName = indexRequest.index();
		// String typeName = indexRequest.type();
		// System.out.println(indexName + " " + typeName);
		//
		// GetResponse getResponse = client.prepareGet(ElasticsearchHelper.indexName, ElasticsearchHelper.typeName,
		// "1").setFields("user", "tags", "update_date").get();
		// Map<String, GetField> fields = getResponse.getFields();
		// if (null != fields) {
		// System.out.println(fields.size());
		// for (Entry<String, GetField> field : fields.entrySet()) {
		// System.out.println(field.getKey() + " " +
		// field.getValue().getValue());
		// }
		// }
	//}

	// private static ExecutorService exector =
	// ThreadPool.newFixedThreadPool("multi-ds-execute", 1000, 100000);
	//
	// public static void main(String[] args) throws UnknownHostException {
	// Client client = TransportClient.builder().build()
	// .addTransportAddress(new
	// InetSocketTransportAddress(InetAddress.getByName("192.168.36.167"),
	// 9300));
	// List<Map<String, Object>> datas = new ArrayList<>(50100);
	//
	// ESTest2 test = new ESTest2();
	// long begin = System.currentTimeMillis();
	// for (int i = 50000; i < 100000; i++) {
	// Map<String, Object> map = new HashMap<>();
	// map.put("user", i);
	// map.put("tags", Arrays.asList("很帅", "无敌", "老王").toString());
	// map.put("update_date", DateUtil.nowDate("yyyy-MM-dd'T'HH:mm:ss"));
	// datas.add(map);
	// }
	// test.addDocuments(client, datas, "crm", "tag");
	//
	// // CountDownLatch cdl = new CountDownLatch(50000);
	// // for(int i= 0; i<50000; i++){
	// // final String aa = String.valueOf(i);
	// // exector.submit(new Callable<String>() {
	// // @Override
	// // public String call() throws Exception {
	// // test.queryDocuments(client, "crm", "tag", "user", aa);
	// // test.deleteDocument(client, "crm", "tag", aa);
	// // test.updateDocument(client, "crm", "tag", aa);
	// // cdl.countDown();
	// // return "";
	// // }
	// // });
	// // }
	//
	// // try {
	// // cdl.await();
	// // } catch (InterruptedException e) {
	// // e.printStackTrace();
	// // }
	//
	// // for(int i=0; i<50000; i++){
	// // Map<String, Object> map = new HashMap<>();
	// // map.put("user", i);
	// // datas.add(map);
	// // }
	//
	// System.out.println(System.currentTimeMillis() - begin);
	// }

	// public void deleteDocument(Client client, String index, String type,
	// String id){
	// //删除_id为1的类型
	// client.prepareDelete(index, type, id).get();
	// System.out.println(id);
	// }

	// public void updateDocument(Client client, String index, String type,
	// String id) throws IOException{
	// client.prepareUpdate(index, type,
	// id).setDoc(XContentFactory.jsonBuilder()
	// .startObject()
	// .field("tags", Arrays.asList("老王","上海","隔壁").toString())
	// .endObject())
	// .get();
	// System.out.println(id);
	// }

	// public void addDocuments(Client client, List<Map<String, Object>> list,
	// String index, String type) {
	// try {
	//
	// BulkRequestBuilder bulkRequest = client.prepareBulk();
	//
	// for (Map<String, Object> map : list) {
	// // 遍历map所有field,构造插入对象
	// XContentBuilder xb = XContentFactory.jsonBuilder().startObject();
	// String userId = null;
	// for (String key : map.keySet()) {
	// xb.field(key, map.get(key));
	// if(key == "user"){
	// userId = map.get(key)+"";
	// }
	// }
	// xb.endObject();
	// // id尽量为物理表的主键
	// bulkRequest.add(client.prepareIndex(index, type, userId).setSource(xb));
	// }
	// BulkResponse bulkResponse = bulkRequest.execute().actionGet();
	// if (bulkResponse.hasFailures()) {
	// System.err.println("");
	// }
	// bulkRequest.request().requests().clear();
	// } catch (UnknownHostException e) {
	// e.printStackTrace();
	// } catch (IOException e) {
	// e.printStackTrace();
	// }
	//
	// }

	// public void queryDocuments(Client client,String index, String type,
	// String key, String value){
	// SearchResponse response =
	// client.prepareSearch(index)//可以同时搜索多个索引prepareSearch("index","index2")
	// .setTypes(type)//可以同时搜索多个类型
	// .setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
	// .setQuery(QueryBuilders.termQuery(key, value)) // Query
	// .setSize(1)
	// .execute()
	// .actionGet();
	// System.out.println(response.getHits().getAt(0).getSource());
	// }

	// public long rearch(Client client, List<Map<String, Object>> datas){
	// MultiSearchRequestBuilder msbuilder = client.prepareMultiSearch();
	// for(Map<String, Object> map : datas){
	// for(Map.Entry<String, Object> entry : map.entrySet()){
	// SearchRequestBuilder srb =
	// client.prepareSearch().setQuery(QueryBuilders.matchQuery(entry.getKey(),
	// entry.getValue())).setSize(1);
	// msbuilder.add(srb);
	// }
	// }
	//
	// MultiSearchResponse sr = msbuilder.execute().actionGet();
	//
	// long nbHits = 0;
	// for (MultiSearchResponse.Item item : sr.getResponses()) {
	// SearchResponse response1 = item.getResponse();
	//// forSearchResponse(response1);
	// nbHits += response1.getHits().getTotalHits();
	// }
	// return nbHits;
	// }

	// public void forSearchResponse(SearchResponse response) {
	// for (SearchHit hit1 : response.getHits()) {
	// System.out.println(hit1.getSource().toString());
	// }
	// }
	}
}
