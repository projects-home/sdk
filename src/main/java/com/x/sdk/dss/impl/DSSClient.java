package com.x.sdk.dss.impl;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.mongodb.BasicDBObject;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.MongoCredential;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoIterable;
import com.mongodb.client.gridfs.GridFSBucket;
import com.mongodb.client.gridfs.GridFSBuckets;
import com.mongodb.client.gridfs.GridFSDownloadStream;
import com.mongodb.client.gridfs.GridFSFindIterable;
import com.mongodb.client.gridfs.model.GridFSFile;
import com.mongodb.client.gridfs.model.GridFSUploadOptions;
import com.mongodb.client.model.IndexOptions;
import com.mongodb.client.model.UpdateOptions;
import com.x.sdk.dss.exception.DSSRuntimeException;
import com.x.sdk.dss.interfaces.IDSSClient;
import com.x.sdk.util.Assert;
import com.x.sdk.util.StringUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.io.*;
import java.util.*;

import static com.mongodb.client.model.Filters.eq;

public class DSSClient implements IDSSClient {

	private static final Logger log = LogManager.getLogger(DSSClient.class);
	private static final String FILE_NAME = "filename";

	private final static String REMARK = "remark";
	private MongoClient mongoClient;
	private MongoDatabase db;
	private String defaultCollection = null;
	private final int MAX_QUERY_SIZE = 1000;
	Gson gson = new GsonBuilder().registerTypeAdapter(ObjectId.class, new ObjectIdTypeAdapter()).create();

	public DSSClient(String addr, String database, String userName, String password, String bucket) {
		MongoClientOptions.Builder builder = new MongoClientOptions.Builder();

		// build the connection options
		builder.maxConnectionIdleTime(60000);// set the max wait time in (ms)
		MongoClientOptions opts = builder.build();
		MongoCredential credential = MongoCredential.createCredential(userName, database, password.toCharArray());
		mongoClient = new MongoClient(DSSHelper.Str2SAList(addr), Arrays.asList(credential), opts);
		db = mongoClient.getDatabase(database);
		// 默认表就是服务标识
		defaultCollection = bucket;
	}

	@Override
	public String save(File file, String remark) {
		Assert.notNull(file, "The insert file is null!");
		String fileType = DSSHelper.getFileType(file.getName());
		GridFSBucket gridBucket = GridFSBuckets.create(db);
		ObjectId fileId = null;
		InputStream inputStream = null;
		try {
			inputStream = new FileInputStream(file);
			GridFSUploadOptions uploadOptions = new GridFSUploadOptions().chunkSizeBytes(1024)
					.metadata(new Document("type", fileType).append(REMARK, remark).append(FILE_NAME, file.getName()));
			fileId = gridBucket.uploadFromStream(file.getName(), inputStream, uploadOptions);
			return fileId.toString();
		} catch (Exception e) {
			log.error("", e);
			throw new DSSRuntimeException(e);
		} finally {
			if (null != inputStream) {
				try {
					inputStream.close();
				} catch (IOException e) {
					log.error("", e);
				}
			}
		}
	}

	@Override
	public String save(byte[] bytes, String remark) {
		if (bytes == null || bytes.length <= 0) {
			throw new DSSRuntimeException(new Exception("bytes illegal"));
		}
		GridFSBucket gridBucket = GridFSBuckets.create(db);
		ObjectId fileId = null;
		InputStream inputStream = null;
		try {
			inputStream = new ByteArrayInputStream(bytes);
			GridFSUploadOptions uploadOptions = new GridFSUploadOptions().chunkSizeBytes(1024)
					.metadata(new Document("remark", remark));
			fileId = gridBucket.uploadFromStream("", inputStream, uploadOptions);
			return fileId.toString();
		} catch (Exception e) {
			log.error(e.toString());
			throw new DSSRuntimeException(e);
		} finally {
			if (null != inputStream) {
				try {
					inputStream.close();
				} catch (IOException e) {
					log.error("", e);
				}
			}
		}
	}

	@Override
	public byte[] read(String id) {
		if (id == null || "".equals(id)) {
			log.error("id illegal");
			throw new DSSRuntimeException(new Exception("id illegal"));
		}
		GridFSBucket gridBucket = GridFSBuckets.create(db);
		GridFSDownloadStream stream = null;
		ByteArrayOutputStream output = null;
		try {
			stream = gridBucket.openDownloadStream(new ObjectId(id));
			output = new ByteArrayOutputStream();
			byte[] buffer = new byte[4096];
			int n = 0;
			while (-1 != (n = stream.read(buffer))) {
				output.write(buffer, 0, n);
			}
			return output.toByteArray();
		} catch (Exception e) {
			log.error(e.toString());
			throw new DSSRuntimeException(e);
		} finally {
			if (null != output) {
				try {
					output.close();
				} catch (IOException e) {
					log.error("", e);
				}
			}
			if (null != stream) {
				stream.close();
			}
		}
	}

	@Override
	public boolean delete(String id) {
		if (id == null || "".equals(id)) {
			log.error("id illegal");
			throw new DSSRuntimeException(new Exception("id or bytes illegal"));
		}
		try {
			GridFSBucket gridBucket = GridFSBuckets.create(db);
			gridBucket.delete(new ObjectId(id));
			return true;
		} catch (Exception e) {
			log.error("delete id: " + id, e);
			return false;
		}

	}

	@Override
	public String update(String id, byte[] bytes) {
		if (bytes == null || id == null || "".equals(id)) {
			log.error("id or bytes illegal");
			throw new DSSRuntimeException(new Exception("id or bytes illegal"));
		}
		delete(id);
		return save(bytes, "");
	}

	@Override
	public Date getLastUpdateTime(String id) {
		if (id == null) {
			log.error("id is null");
			throw new DSSRuntimeException(new Exception("id illegal"));
		}
		GridFSBucket gridBucket = GridFSBuckets.create(db);
		GridFSFindIterable files = gridBucket.find(eq("_id", new ObjectId(id)));
		if (files == null || null == files.first()) {
			log.error("file missing");
			throw new DSSRuntimeException(new Exception("file missing"));
		}
		GridFSFile gridFSFile = files.first();
		return gridFSFile.getUploadDate();
	}

	@Override
	public String update(String id, File file) {
		if (file == null || id == null || "".equals(id)) {
			log.error("id or file illegal");
			throw new DSSRuntimeException(new Exception("id or file illegal"));
		}
		delete(id);
		return save(file, "");
	}

	@Override
	public long getFileSize(String id) {
		GridFSBucket gridBucket = GridFSBuckets.create(db);
		GridFSFindIterable files = gridBucket.find(eq("_id", new ObjectId(id)));
		if (files == null || null == files.first()) {
			return 0;
		}
		return files.first().getLength();
	}

	@Override
	public boolean isFileExist(String id) {
		GridFSBucket gridBucket = GridFSBuckets.create(db);
		GridFSFindIterable files = gridBucket.find(eq("_id", new ObjectId(id)));
		if (files == null || null == files.first()) {
			return false;
		}
		return true;
	}

	@Override
	public String insert(String content) {
		Document doc = new Document();
		ObjectId id = new ObjectId();
		doc.put("_id", id);
		doc.put("content", content);
		db.getCollection(defaultCollection).insertOne(doc);
		return id.toString();
	}

	@Override
	public String insertJSON(String doc) {
		Document dbObj = Document.parse(doc);
		ObjectId id = new ObjectId();
		dbObj.put("_id", id);
		db.getCollection(defaultCollection).insertOne(dbObj);
		return id.toString();
	}

	@Override
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public String insert(Map doc) {
		if (null == doc || doc.size() <= 0)
			throw new IllegalArgumentException();
		Document dbObj = new Document(doc);
		ObjectId id = new ObjectId();
		dbObj.put("_id", id);
		db.getCollection(defaultCollection).insertOne(dbObj);
		return id.toString();
	}

	@Override
	public void insertBatch(List<Map<String, Object>> docs) {
		if (docs == null || docs.isEmpty()) {
			throw new IllegalArgumentException();
		}
		List<Document> documents = new ArrayList<Document>();
		for (int i = 0; i < docs.size(); i++) {
			Document dbObj = new Document(docs.get(i));
			documents.add(dbObj);
		}
		db.getCollection(defaultCollection).insertMany(documents);
	}

	@Override
	public long deleteById(String id) {
		return db.getCollection(defaultCollection).deleteOne(eq("_id", new ObjectId(id))).getDeletedCount();
	}

	@Override
	public long deleteByJson(String doc) {
		Document dbObj = Document.parse(doc);
		if (dbObj.containsKey("_id")) {
			String id = dbObj.getString("_id");
			dbObj.remove("_id");
			dbObj.append("_id", new ObjectId(id));
		}
		return db.getCollection(defaultCollection).deleteMany(dbObj).getDeletedCount();
	}

	@Override
	@SuppressWarnings("rawtypes")
	public long deleteByMap(Map doc) {
		if (null == doc || doc.size() <= 0)
			throw new IllegalArgumentException();
		@SuppressWarnings("unchecked")
		Document dbObj = new Document(doc);
		if (dbObj.containsKey("_id")) {
			String id = dbObj.getString("_id");
			dbObj.remove("_id");
			dbObj.append("_id", new ObjectId(id));
		}
		return db.getCollection(defaultCollection).deleteMany(dbObj).getDeletedCount();
	}

	public boolean collectionExists(final String collectionName) {
		if (StringUtil.isBlank(collectionName)) {
			return false;
		}

		final MongoIterable<String> iterable = db.listCollectionNames();
		try (final MongoCursor<String>it = iterable.iterator()) {
			while (it.hasNext()) {
				if (it.next().equalsIgnoreCase(collectionName)) {
					return true;
				}
			}
		}

		return false;
	}

	@Override
	public long deleteAll() {
		if (collectionExists(defaultCollection)) {
			// 慎重使用，不可恢复
			Document dbObj = new Document();
			return db.getCollection(defaultCollection).deleteMany(dbObj).getDeletedCount();
		}
		return 0;
	}

	@Override
	public long deleteBatch(List<Map<String, Object>> docs) {
		if (docs == null || docs.isEmpty()) {
			throw new IllegalArgumentException();
		}
		int total = 0;
		for (int i = 0; i < docs.size(); i++) {
			Document dbObj = new Document(docs.get(i));
			total += db.getCollection(defaultCollection).deleteMany(dbObj).getDeletedCount();
		}
		return total;
	}

	@Override
	public long updateById(String id, String doc) {
		Document dbObj = Document.parse(doc);
		Document modifiedObject = new Document("$set", dbObj);
		return db.getCollection(defaultCollection).updateOne(eq("_id", new ObjectId(id)), modifiedObject)
				.getModifiedCount();
	}

	@Override
	public long update(String query, String doc) {
		Document qryObj = Document.parse(query);
		Document dbObj = Document.parse(doc);
		Document modifiedObject = new Document("$set", dbObj);
		return db.getCollection(defaultCollection).updateMany(qryObj, modifiedObject).getModifiedCount();
	}

	@Override
	public long upsert(String query, String doc) {
		Document qryObj = Document.parse(query);
		Document dbObj = Document.parse(doc);
		Document modifiedObject = new Document("$set", dbObj);
		UpdateOptions options = new UpdateOptions().upsert(true);
		return db.getCollection(defaultCollection).updateMany(qryObj, modifiedObject, options).getModifiedCount();
	}

	@Override
	public String findById(String id) {
		FindIterable<Document> docs = db.getCollection(defaultCollection).find(eq("_id", new ObjectId(id)));
		if (docs == null || null == docs.first()) {
			return null;
		}
		return gson.toJson(docs.first());
	}

	@Override
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public String find(Map doc) {
		Document query = new Document(doc);
		List<Document> documents = db.getCollection(defaultCollection).find(query).limit(MAX_QUERY_SIZE)
				.into(new ArrayList<Document>());
		if (null != documents)
			return gson.toJson(documents);
		else
			return null;
	}

	@Override
	public String find(String query) {
		// 慎用，可能很大量
		if (StringUtil.isBlank(query))
			query = "{}";
		Document qryObj = Document.parse(query);
		List<Document> documents = (List<Document>) db.getCollection(defaultCollection).find(qryObj)
				.limit(MAX_QUERY_SIZE).into(new ArrayList<Document>());
		if (null != documents) {
			return gson.toJson(documents);
		} else
			return null;
	}

	@Override
	public String query(String query, int pageNumber, int pageSize) {
		Document qryObj = Document.parse(query);
		List<Document> documents = db.getCollection(defaultCollection).find(qryObj)
				.skip((pageNumber >= 1 ? (pageNumber - 1) * pageSize : 0)).limit(pageSize)
				.into(new ArrayList<Document>());
		if (null != documents) {
			return gson.toJson(documents);
		} else
			return null;
	}

	@Override
	public long getCount(String query) {
		if (StringUtil.isBlank(query))
			query = "{}";
		Document qryObj = Document.parse(query);
		return db.getCollection(defaultCollection).count(qryObj);
	}

	@Override
	public void addIndex(String field, boolean unique) {
		IndexOptions options = new IndexOptions();

		// ensure the index is unique
		options.unique(true);
		BasicDBObject dbo = new BasicDBObject(field, 1);
		String idx = db.getCollection(defaultCollection).createIndex(dbo, options);
		log.info("Index on field:" + field + "created! name:" + idx);
	}

	@Override
	public void dropAllIndex() {
		db.getCollection(defaultCollection).dropIndexes();
	}

	@Override
	public void dropIndex(String field) {
		BasicDBObject dbo = new BasicDBObject(field, 1);
		db.getCollection(defaultCollection).dropIndex(dbo);
	}

	public boolean isIndexExist(String field) {
		String indexName = field;
		List<Document> indexs = db.getCollection(defaultCollection).listIndexes().into(new ArrayList<Document>());
		if (null == indexs || indexs.size() <= 0)
			return false;
		else {
			boolean found = false;
			for (Document dbo : indexs) {
				if (dbo.get("name").toString().indexOf(indexName) >= 0) {
					found = true;
					break;
				}
			}
			return found;
		}
	}

	public Long getSize() {
		long size = -1;
		// 此处需要取得多个的大小
		Document tableResult = db.runCommand(new Document("collStats", defaultCollection));
		Document fileResult = db.runCommand(new Document("collStats", "fs.chunks"));
		int dataSize = 0;
		int fileSize = 0;
		if (null != tableResult) {
			if (null != tableResult.get("size"))
				dataSize = tableResult.getInteger("size");
		}
		if (null != fileResult) {
			if (null != fileResult.get("size"))
				fileSize = fileResult.getInteger("size");
		}
		size = Math.round(dataSize + fileSize);
		return size;
	}

	public static void main(String[] args) {
		IDSSClient dss = new DSSClient("10.1.228.200:37017;10.1.228.202:37017", "admin", "sa", "sa", "fs");
		byte[] byte0 = "123456789".getBytes();
		String str1 = "thenormaltest";
		dss.save(byte0, str1);
	}

	@Override
	public void close() {
		if (null != mongoClient)
			mongoClient.close();
	}

	@Override
	public long count(String query) {
		if (StringUtil.isBlank(query))
			query = "{}";
		Document qryObj = Document.parse(query);
		return db.getCollection(defaultCollection).count(qryObj);
	}


	private class ObjectIdTypeAdapter extends TypeAdapter<ObjectId> {
		@Override
		public void write(final JsonWriter out, final ObjectId value) throws IOException {
			out.beginObject().name("$oid").value(value.toString()).endObject();
		}

		@Override
		public ObjectId read(final JsonReader in) throws IOException {
			in.beginObject();
			assert "$oid".equals(in.nextName());
			String objectId = in.nextString();
			in.endObject();
			return new ObjectId(objectId);
		}
	}
}
