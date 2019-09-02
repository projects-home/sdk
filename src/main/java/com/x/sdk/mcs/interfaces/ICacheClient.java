package com.x.sdk.mcs.interfaces;

import redis.clients.jedis.JedisPubSub;
import redis.clients.jedis.params.sortedset.ZAddParams;
import redis.clients.jedis.params.sortedset.ZIncrByParams;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

public interface ICacheClient {

    /**
     * 设置一个key的value值 如果key已经存在了，它会被覆盖，而不管它是什么类型。
     *
     * @param key   cache中存储数据的key
     * @param value cache中存储数据的value，javabean建议做成json串
     * @return 总是OK，因为SET不会失败。
     * @
     */
    String set(String key, String value);

    /**
     * 设置一个key的value值,设置key对应字符串value,并设置有效期。
     *
     * @param key
     * @param seconds 秒 有效期
     * @param value
     * @return
     * @
     */
    String setex(String key, int seconds, String value);

    /**
     * 获取key的值 如果key不存在，返回null
     *
     * @param key cache中存储数据的key
     * @return cache中key为key的value
     * @
     */
    String get(String key);

    /**
     * 删除一个key
     *
     * @param key cache中存储数据的key
     * @return 被删除的key的value的数量
     * @
     */
    Long del(String key);

    /**
     * 删除一个或多个key
     *
     * @param keys
     * @return 被删除的key的value的数量
     * @
     */
    Long del(String... keys);

    /**
     * 设置key的过期时间。如果key已过期，将会被自动删除。
     *
     * @param key     cache中存储数据的key
     * @param seconds 过期的秒数
     * @return 被设置key的数量
     * @
     */
    Long expire(String key, int seconds);

    /**
     * 设置key的过期时间。如果key已过期，将会被自动删除。
     *
     * @param key     cache中存储数据的key
     * @param timestamp unix 时间戳，1970/01/01以来的秒数
     * @return 被设置key的数量
     * @
     */
    Long expireAt(String key, long timestamp);

    /**
     * @return 当 key 不存在时，返回 -2 。 当 key 存在但没有设置剩余生存时间时，返回 -1 。 否则，以秒为单位，返回 key
     * 的剩余生存时间。
     * @
     */
    Long ttl(String key);

    /**
     * 返回key是否存在
     *
     * @param key cache中存储数据的key
     * @return true存在
     * @
     */
    boolean exists(String key);

    /**
     * 对key对应的数字做加1操作。如果key不存在，那么在操作之前，这个key对应的值会被置为0。
     * 如果key有一个错误类型的value或者是一个不能表示成数字的字符串，就返回错误。 这个操作最大支持在64位有符号的整型数字。
     *
     * @param key cache中存储数据的key
     * @return 增加之后的value
     * @
     */
    Long incr(String key);

    /**
     * 将key对应的数字加decrement。如果key不存在，操作之前，key就会被置为0。
     * 如果key的value类型错误或者是个不能表示成数字的字符串，就返回错误。 这个操作最多支持64位有符号的正型数字。
     *
     * @param key       cache中存储数据的key
     * @param increment 增加大小
     * @return 增加之后的value值
     * @
     */
    Long incrBy(String key, long increment);

    /**
     * 对key对应的数字做减1操作。如果key不存在，那么在操作之前，这个key对应的值会被置为0。
     * 如果key有一个错误类型的value或者是一个不能表示成数字的字符串，就返回错误。 这个操作最大支持在64位有符号的整型数字。
     *
     * @param key cache中存储数据的key
     * @return 减小之后的value
     * @
     */
    Long decr(String key);

    /**
     * 将key对应的数字减decrement。如果key不存在，操作之前，key就会被置为0。
     * 如果key的value类型错误或者是个不能表示成数字的字符串，就返回错误。 这个操作最多支持64位有符号的正型数字。
     *
     * @param key       cache中存储数据的key
     * @param decrement 减小大小
     * @return 减少之后的value值
     * @
     */
    Long decrBy(String key, long decrement);


    /**
     * list 将所有指定的值插入到存于 key 的列表的头部。如果 key 不存在，那么在进行 push 操作前会创建一个空列表。 如果 key
     * 对应的值不是一个 list 的话，那么会返回一个错误。可以使用一个命令把多个元素 push 进入列表，
     * 只需在命令末尾加上多个指定的参数。元素是从最左端的到最右端的、一个接一个被插入到 list 的头部。 所以对于这个命令例子 LPUSH
     * mylist a b c，返回的列表是 c 为第一个元素， b 为第二个元素， a 为第三个元素。
     *
     * @param key     用于获取list的key
     * @param strings 向list中添加的对象
     * @return 在 push 操作后的 list 长度。
     * @
     */
    Long lpush(String key, String... strings);

    /**
     * list 向存于 key 的列表的尾部插入所有指定的值。如果 key 不存在，那么在进行 push 操作前会创建一个空列表。 如果 key
     * 对应的值不是一个 list 的话，那么会返回一个错误。可以使用一个命令把多个元素 push 进入列表，
     * 只需在命令末尾加上多个指定的参数。元素是从最左端的到最右端的、一个接一个被插入到 list 的头部。 所以对于这个命令例子 LPUSH
     * mylist a b c，返回的列表 其第一个元素是 a ，第二个元素是 b ，第三个元素是 c。
     *
     * @param key     用于获取list的key
     * @param strings 向list中添加的对象
     * @return 在 push 操作后的 list 长度。
     * @
     */
    Long rpush(String key, String... strings);

    /**
     * list 从存于 key 的列表里移除前 count 次出现的值为 value 的元素。
     *
     * @param key   cache中存储list的key
     * @param count 这个 count 参数通过下面几种方式影响这个操作：
     *              count > 0: 从头往尾移除值为 value 的元素。
     *              count < 0: 从尾往头移除值为 value 的元素。
     *              count = 0: 移除所有值为 value 的元素。
     *              比如,LREM list -2 "hello" 会从存于 list 的列表里移除最后两个出现的 "hello"。
     *              需要注意的是，如果list里没有存在key就会被当作空list处理，所以当 key 不存在的时候，这个命令会返回 0。
     * @param value 准备删除的元素
     * @return 被移除的元素个数。
     */
    Long lrem(String key, long count, String value);

    /**
     * list 返回存储在 key 里的list的长度。 如果 key 不存在，那么就被看作是空list，并且返回长度为 0。 当存储在 key
     * 里的值不是一个list的话，会返回error。
     *
     * @param key cache中存储数据的key
     * @return list 长度。
     * @
     */
    Long llen(String key);

    /**
     * list 移除并且返回 key 对应的 list 的第一个元素。
     *
     * @param key 用于获取list的key
     * @return 返回第一个元素的值，或者当 key 不存在时返回 null。
     * @
     */
    String lpop(String key);

    /**
     * list 移除并返回存于 key 的 list 的最后一个元素。
     *
     * @param key 用于获取list的key
     * @return 返回第一个元素的值，或者当 key 不存在时返回 null。集群时java基本类型
     * @
     */
    String rpop(String key);

    /**
     * list 指定范围里的列表元素。
     *
     * @param key   用于获取list的key
     * @param start 起始下标
     * @param end   结束下标 start 和 end
     *              偏移量都是基于0的下标，即list的第一个元素下标是0（list的表头），第二个元素下标是1，以此类推。
     *              偏移量也可以是负数，表示偏移量是从list尾部开始计数。 例如， -1 表示列表的最后一个元素，-2
     *              是倒数第二个，以此类推。
     * @return 返回指定范围里的列表元素。
     * @
     */
    List<String> lrange(String key, long start, long end);

    /**
     * list 获得列表里所有的元素。(0,-1)
     *
     * @param key 用于获取list的key
     * @return 返回指定范围里的列表元素。
     * @
     */
    List<String> lrangeAll(String key);


    /**
     * map 设置 key 指定的哈希集中指定字段的值。如果 key 指定的哈希集不存在，会创建一个新的哈希集并与 key 关联。
     * 如果字段在哈希集中存在，它将被重写。
     *
     * @param key
     * @param field 二级key
     * @param value
     * @return 1如果field是一个新的字段 0如果field原来在map里面已经存在
     */
    Long hset(String key, String field, String value);

    /**
     * map 设置hash的一个字段，只有当这个字段不存在时有效
     *
     * @param key
     * @param field 二级key
     * @param value
     * @return 1如果field是一个新的字段 0如果field原来在map里面已经存在
     */
    Long hsetnx(String key, String field, String value);

    /**
     * map 将hash集合 追加到 key指定的哈希集中。
     *
     * @param key
     * @param field 二级key
     * @param value
     * @return "ok"正常
     */
    String hmset(String key, Map<String, String> hash);

    /**
     * map 获得hash的一个字段对应的value值
     *
     * @param key
     * @param field
     * @return
     * @
     */
    String hget(String key, String field);

    /**
     * map 获得hash的多个字段对应的value值
     *
     * @param key
     * @param fields
     * @return
     * @
     */
    List<String> hmget(final String key, final String... fields);

    /**
     * map hash的字段是否存在
     *
     * @param key
     * @param field
     * @return
     * @
     */
    Boolean hexists(String key, String field);

    /**
     * map 删除hash的字段
     *
     * @param key
     * @param fields
     * @return 删除的个数
     * @
     */
    Long hdel(String key, String... fields);

    /**
     * map hash的长度
     *
     * @param key
     * @return 长度
     * @
     */
    Long hlen(String key);

    /**
     * map 获得hash全集
     *
     * @param key
     * @return Map<String, String>
     * @
     */
    Map<String, String> hgetAll(String key);

    /**
     * set 添加一个或多个指定的member元素到集合的 key中. 指定的一个或者多个元素member 如果已经在集合key中存在则忽略.
     * 如果集合key 不存在，则新建集合key,并添加member元素到集合key中.
     *
     * @param key
     * @param members
     * @return 返回新成功添加到集合里元素的数量，不包括已经存在于集合中的元素.
     * @
     */
    Long sadd(String key, String... members);

    /**
     * set 返回key集合所有的元素.
     *
     * @param key
     * @return 集合中的所有元素.
     * @
     */
    Set<String> smembers(String key);

    /**
     * set 在key集合中移除指定的元素. 如果指定的元素不是key集合中的元素则忽略 如果key集合不存在则被视为一个空的集合，该命令返回0.
     *
     * @param key
     * @param members
     * @return 从集合中移除元素的个数，不包括不存在的成员.
     * @
     */
    Long srem(String key, String... members);

    /**
     * set 返回集合存储的key的基数 (集合元素的数量).
     *
     * @param key
     * @return 集合的基数(元素的数量), 如果key不存在, 则返回 0.
     * @
     */
    Long scard(String key);

    /**
     * set 返回给定的多个集合的并集中的所有成员.
     *
     * @param keys
     * @return 并集的成员列表
     * @
     */
    Set<String> sunion(String... keys);

    /**
     * set 返回一个集合与给定集合的差集的元素.
     *
     * @param keys
     * @return 第一个集合比其余集合多出的元素
     * @
     */
    Set<String> sdiff(String... keys);

    /**
     * set 类似于 sdiff, 不同之处在于该命令不返回结果集，而是将结果存放在destination集合中. 如果destination
     * 已经存在, 则将其覆盖重写.
     *
     * @param dstkey
     * @param keys
     * @return 结果集元素的个数.
     * @
     */
    Long sdiffstore(String dstkey, String... keys);


    /**
     * 设置一个key的value值 如果key已经存在了，它会被覆盖，而不管它是什么类型。
     *
     * @param key   cache中存储数据的key
     * @param value cache中存储数据的value
     * @return 总是OK，因为SET不会失败。
     * @
     */
    String set(byte[] key, byte[] value);

    /**
     * 设置key对应字符串value，并且设置key在给定的seconds时间之后超时过期。
     *
     * @param key
     * @param seconds 秒 有效期
     * @param value
     * @return
     * @
     */
    String setex(byte[] key, int seconds, byte[] value);

    /**
     * 获取key的值 如果key不存在，返回null
     *
     * @param key cache中存储数据的key
     * @return cache中key为key的value
     * @
     */
    byte[] get(byte[] key);

    /**
     * 删除一个key
     *
     * @param key cache中存储数据的key
     * @return 被删除的key的value的数量
     * @
     */
    Long del(byte[] key);

    /**
     * 删除一个或多个key
     *
     * @param keys
     * @return 被删除的key的value的数量
     * @
     */
    Long del(byte[]... keys);

    /**
     * 设置key的过期时间。如果key已过期，将会被自动删除。
     *
     * @param key     cache中存储数据的key
     * @param seconds 过期的秒数
     * @return 被设置key的数量
     * @
     */
    Long expire(byte[] key, int seconds);

    /**
     * 设置key的过期时间。如果key已过期，将会被自动删除。
     *
     * @param key     cache中存储数据的key
     * @param timestamp 过期的秒数1970/01/01
     * @return 被设置key的数量
     * @
     */
    Long expireAt(byte[] key, long timestamp);

    /**
     * 以秒为单位，返回给定 key 的剩余生存时间(TTL, time to live)。
     *
     * @param key     cache中存储数据的key
     * @param seconds 过期的秒数
     * @return 当 key 不存在时，返回 -2 。 当 key 存在但没有设置剩余生存时间时，返回 -1 。 否则，以秒为单位，返回 key
     * 的剩余生存时间。
     * @
     */
    Long ttl(byte[] key);

    /**
     * 返回key是否存在
     *
     * @param key cache中存储数据的key
     * @return true存在
     * @
     */
    boolean exists(byte[] key);

    /**
     * 对key对应的数字做加1操作。如果key不存在，那么在操作之前，这个key对应的值会被置为0。
     * 如果key有一个错误类型的value或者是一个不能表示成数字的字符串，就返回错误。 这个操作最大支持在64位有符号的整型数字。
     *
     * @param key cache中存储数据的key
     * @return 增加之后的value
     * @
     */
    Long incr(byte[] key);

    /**
     * 将key对应的数字加decrement。如果key不存在，操作之前，key就会被置为0。
     * 如果key的value类型错误或者是个不能表示成数字的字符串，就返回错误。 这个操作最多支持64位有符号的正型数字。
     *
     * @param key       cache中存储数据的key
     * @param increment 增加大小
     * @return 增加之后的value值
     * @
     */
    Long incrBy(byte[] key, long increment);

    /**
     * 对key对应的数字做减1操作。如果key不存在，那么在操作之前，这个key对应的值会被置为0。
     * 如果key有一个错误类型的value或者是一个不能表示成数字的字符串，就返回错误。 这个操作最大支持在64位有符号的整型数字。
     *
     * @param key cache中存储数据的key
     * @return 减小之后的value
     * @
     */
    Long decr(byte[] key);

    /**
     * 将key对应的数字减decrement。如果key不存在，操作之前，key就会被置为0。
     * 如果key的value类型错误或者是个不能表示成数字的字符串，就返回错误。 这个操作最多支持64位有符号的正型数字。
     *
     * @param key       cache中存储数据的key
     * @param decrement 减小大小
     * @return 减少之后的value值
     * @
     */
    Long decrBy(byte[] key, long decrement);


    /**
     * list 将所有指定的值插入到存于 key 的列表的头部。如果 key 不存在，那么在进行 push 操作前会创建一个空列表。 如果 key
     * 对应的值不是一个 list 的话，那么会返回一个错误。可以使用一个命令把多个元素 push 进入列表，
     * 只需在命令末尾加上多个指定的参数。元素是从最左端的到最右端的、一个接一个被插入到 list 的头部。 所以对于这个命令例子 LPUSH
     * mylist a b c，返回的列表是 c 为第一个元素， b 为第二个元素， a 为第三个元素。
     *
     * @param key     用于获取list的key
     * @param strings 向list中添加的对象
     * @return 在 push 操作后的 list 长度。
     * @
     */
    Long lpush(byte[] key, byte[]... strings);

    /**
     * list 向存于 key 的列表的尾部插入所有指定的值。如果 key 不存在，那么在进行 push 操作前会创建一个空列表。 如果 key
     * 对应的值不是一个 list 的话，那么会返回一个错误。可以使用一个命令把多个元素 push 进入列表，
     * 只需在命令末尾加上多个指定的参数。元素是从最左端的到最右端的、一个接一个被插入到 list 的头部。 所以对于这个命令例子 LPUSH
     * mylist a b c，返回的列表 其第一个元素是 a ，第二个元素是 b ，第三个元素是 c。
     *
     * @param key     用于获取list的key
     * @param strings 向list中添加的对象
     * @return 在 push 操作后的 list 长度。
     * @
     */
    Long rpush(byte[] key, byte[]... strings);

    /**
     * list 返回存储在 key 里的list的长度。 如果 key 不存在，那么就被看作是空list，并且返回长度为 0。 当存储在 key
     * 里的值不是一个list的话，会返回error。
     *
     * @param key cache中存储数据的key
     * @return list 长度。
     * @
     */
    Long llen(byte[] key);

    /**
     * list 从存于 key 的列表里移除前 count 次出现的值为 value 的元素。
     *
     * @param key   cache中存储list的key
     * @param count 这个 count 参数通过下面几种方式影响这个操作：
     *              count > 0: 从头往尾移除值为 value 的元素。
     *              count < 0: 从尾往头移除值为 value 的元素。
     *              count = 0: 移除所有值为 value 的元素。
     *              比如,LREM list -2 "hello" 会从存于 list 的列表里移除最后两个出现的 "hello"。
     *              需要注意的是，如果list里没有存在key就会被当作空list处理，所以当 key 不存在的时候，这个命令会返回 0。
     * @param value 准备删除的元素
     * @return 被移除的元素个数。
     */
    Long lrem(byte[] key, long count, byte[] value);

    /**
     * list 移除并且返回 key 对应的 list 的第一个元素。
     *
     * @param key 用于获取list的key
     * @return 返回第一个元素的值，或者当 key 不存在时返回 null。
     * @
     */
    byte[] lpop(byte[] key);

    /**
     * list 移除并返回存于 key 的 list 的最后一个元素。
     *
     * @param key 用于获取list的key
     * @return 返回第一个元素的值，或者当 key 不存在时返回 null。集群时java基本类型
     * @
     */
    byte[] rpop(byte[] key);

    /**
     * list 指定范围里的列表元素。
     *
     * @param key   用于获取list的key
     * @param start 起始下标
     * @param end   结束下标 start 和 end
     *              偏移量都是基于0的下标，即list的第一个元素下标是0（list的表头），第二个元素下标是1，以此类推。
     *              偏移量也可以是负数，表示偏移量是从list尾部开始计数。 例如， -1 表示列表的最后一个元素，-2
     *              是倒数第二个，以此类推。
     * @return 返回指定范围里的列表元素。
     * @
     */
    List<byte[]> lrange(byte[] key, long start, long end);

    /**
     * list 获得列表里所有的元素。(0,-1)
     *
     * @param key 用于获取list的key
     * @return 返回指定范围里的列表元素。
     * @
     */
    List<byte[]> lrangeAll(byte[] key);


    /**
     * map 设置 key 指定的哈希集中指定字段的值。如果 key 指定的哈希集不存在，会创建一个新的哈希集并与 key 关联。
     * 如果字段在哈希集中存在，它将被重写。
     *
     * @param key
     * @param field 二级key
     * @param value
     * @return 1如果field是一个新的字段 0如果field原来在map里面已经存在
     */
    Long hset(byte[] key, byte[] field, byte[] value);

    /**
     * map 设置hash的一个字段，只有当这个字段不存在时有效
     *
     * @param key
     * @param field 二级key
     * @param value
     * @return 1如果field是一个新的字段 0如果field原来在map里面已经存在
     */
    Long hsetnx(byte[] key, byte[] field, byte[] value);

    Long setnx(byte[] key, byte[] value);

    Long setnx(String key, String value);

    /**
     * map 将hash集合 追加到 key指定的哈希集中。
     *
     * @param key
     * @param field 二级key
     * @param value
     * @return "ok"正常
     */
    String hmset(byte[] key, Map<byte[], byte[]> hash);

    /**
     * map 获得hash的一个字段对应的value值
     *
     * @param key
     * @param field
     * @return
     * @
     */
    byte[] hget(byte[] key, byte[] field);

    /**
     * map 获得hash的多个字段对应的value值
     *
     * @param key
     * @param fields
     * @return
     * @
     */
    List<byte[]> hmget(final byte[] key, final byte[]... fields);

    /**
     * map hash的字段是否存在
     *
     * @param key
     * @param field
     * @return
     * @
     */
    Boolean hexists(byte[] key, byte[] field);

    /**
     * map 删除hash的字段
     *
     * @param key
     * @param fields
     * @return 删除的个数
     * @
     */
    Long hdel(byte[] key, byte[]... fields);

    /**
     * map hash的长度
     *
     * @param key
     * @return 长度
     * @
     */
    Long hlen(byte[] key);

    /**
     * map 获得hash全集
     *
     * @param key
     * @return Map<String, String>
     * @
     */
    Map<byte[], byte[]> hgetAll(byte[] key);

    /**
     * set 添加一个或多个指定的member元素到集合的 key中. 指定的一个或者多个元素member 如果已经在集合key中存在则忽略.
     * 如果集合key 不存在，则新建集合key,并添加member元素到集合key中.
     *
     * @param key
     * @param members
     * @return 返回新成功添加到集合里元素的数量，不包括已经存在于集合中的元素.
     * @
     */
    Long sadd(byte[] key, byte[]... members);

    /**
     * set 返回key集合所有的元素.
     *
     * @param key
     * @return 集合中的所有元素.
     * @
     */
    Set<byte[]> smembers(byte[] key);

    /**
     * set 在key集合中移除指定的元素. 如果指定的元素不是key集合中的元素则忽略 如果key集合不存在则被视为一个空的集合，该命令返回0.
     *
     * @param key
     * @param members
     * @return 从集合中移除元素的个数，不包括不存在的成员.
     * @
     */
    Long srem(byte[] key, byte[]... members);

    /**
     * set 返回集合存储的key的基数 (集合元素的数量).
     *
     * @param key
     * @return 集合的基数(元素的数量), 如果key不存在, 则返回 0.
     * @
     */
    Long scard(byte[] key);

    /**
     * set 返回给定的多个集合的并集中的所有成员.
     *
     * @param keys
     * @return 并集的成员列表
     * @
     */
    Set<byte[]> sunion(byte[]... keys);

    /**
     * set 返回一个集合与给定集合的差集的元素.
     *
     * @param keys
     * @return 第一个集合比其余集合多出的元素
     * @
     */
    Set<byte[]> sdiff(byte[]... keys);

    /**
     * set 类似于 sdiff, 不同之处在于该命令不返回结果集，而是将结果存放在destination集合中. 如果destination
     * 已经存在, 则将其覆盖重写.
     *
     * @param dstkey
     * @param keys
     * @return 结果集元素的个数.
     * @
     */
    Long sdiffstore(byte[] dstkey, byte[]... keys);


    /**
     * 对key的hashmap 的键值field进行原子增
     * @param key
     * @param field
     * @param value
     * @return
     */
    Long hincrBy(String key, String field, long value);


    /**
     * 原子加浮点数
     * @param key
     * @param value
     * @return
     */
    Double incrByFloat(final String key, final double value);


    /**
     * 对key的hashmap 的键值field进行原子增
     * @param key
     * @param field
     * @param value
     * @return
     */
    Double hincrByFloat(final String key, final String field, final double value);

    /**
     * 对键值为key的有序集合，添加值为score的member元素。
     * @param key
     * @param score
     * @param member
     * @return
     */
    Long zadd(final String key, final double score, final String member);
    
    Long zadd(final String key, final double score, final String member, final ZAddParams params);
    
    Long zadd(String key, Map<String, Double> scoreMembers);
    
    Long zadd(final String key, final Map<String, Double> scoreMembers, final ZAddParams params);
    
    /**
     * 计算键值为key的有序集合中，指定分数区间在min到max范围内的成员数量。
     * @param key
     * @param min
     * @param max
     */
    Long zcount(final String key, final double min, final double max);
    
    Long zcount(final String key, final String min, final String max);
    
    /**
     * 对键值为key的有序集合中,指定的member成员的分数，加上score。
     * @param key
     * @param score
     * @param member
     */
    Double zincrby(final String key, final double score, final String member);
    
    Double zincrby(String key, double score, String member, ZIncrByParams params);
    
    /**
     * 获取键值为key的有序集合中，指定区间内的成员。
     * @param key
     * @param start
     * @param end
     */
    Set<String> zrange(final String key, final long start, final long end);
    
    /**
     * 获取键值为key的有序集合中，指定分数区间的成员列表。
     * @param key
     * @param min
     * @param max
     */
    Set<String> zrangeByScore(final String key, final double min, final double max);

    Set<String> zrangeByScore(final String key, final String min, final String max);

    Set<String> zrangeByScore(final String key, final double min, final double max, final int offset, int count);
    
    /**
     * 返回键值为key的有序集中，区间在start到end内的成员。
     * @param key
     * @param start
     * @param end
     */
    Set<String> zrevrange(final String key, final long start, final long end);
    
    /**
     * 返回键值为key的有序集中，分数区间在max到min内的所有的成员。
     * @param key
     * @param max
     * @param min
     */
    Set<String> zrevrangeByScore(final String key, final double max, final double min);

    Set<String> zrevrangeByScore(final String key, final String max, final String min);

    Set<String> zrevrangeByScore(final String key, final double max, final double min, final int offset, int count);
    
    /**
     * 返回有序集中成员的排名。按分数值递减(从大到小)排序。排名以 0 为底, 也就是说, 分数值最大的成员排名为 0 。
     * @param key
     * @param member
     */
    Long zrevrank(final String key, final String member);
    
    /**
     * 删除键值为key的有序集中，指定的member。
     * @param key
     * @param member
     * @return
     */
    Long zrem(final String key, final String... members);
    
    Long zremrangeByRank(final String key, final long start, final long end);

    Long zremrangeByScore(final String key, final double start, final double end);

    Long zremrangeByScore(final String key, final String start, final String end);
    
    /**
	 * 获取分布式锁
	 * @param lockName 竞争获取锁key
	 * @param acquireTimeoutInMS 获取锁超时时间(ms)
	 * @param lockTimeoutInMS 锁的超时时间(ms)
	 * @return 获取锁标识
	 */
    String acquireLock(String lockName, long acquireTimeoutInMS, long lockTimeoutInMS);
    
    /**
	 * 释放锁
	 * @param lockName 竞争获取锁key
	 * @param identifier 释放锁标识
	 * @return boolean
	 */
    boolean releaseLock(String lockName, String identifier);
    
    /**
     * 发布
     * @param channel
     * @param message
     * @return
     */
    public Long publish(final String channel, final String message);

    /**
     * 订阅
     * @param jedisPubSub
     * @param channels
     */
    public void subscribe(final JedisPubSub jedisPubSub, final String... channels);

    /**
     * 模式订阅
     * @param jedisPubSub
     * @param patterns
     */
    public void psubscribe(final JedisPubSub jedisPubSub, final String... patterns);
    
    /**
     * 返回map对象中的所有key
     * @param key
     * @return
     * @author gucl
     * @ApiDocMethod
     * @ApiCode
     * @RestRelativeURL
     */
    public Set<String> hkeys(final String key);
    
    /**
     * 返回map对象中的所有value
     * @param key
     * @return
     * @author gucl
     * @ApiDocMethod
     * @ApiCode
     * @RestRelativeURL
     */
    public List<String> hvals(final String key);
    
    /**
     * Return all the fields in a hash.
     * <p>
     * <b>Time complexity:</b> O(N), where N is the total number of entries
     * @param key
     * @return All the fields names contained into a hash.
     */
    public Set<byte[]> hkeys(final byte[] key);

      /**
       * Return all the values in a hash.
       * <p>
       * <b>Time complexity:</b> O(N), where N is the total number of entries
       * @param key
       * @return All the fields values contained into a hash.
       */
      public Collection<byte[]> hvals(final byte[] key);
      
      /**
       * 查询Redis的键值列表，注意此方法不不能使用* 
       * Glob style patterns examples:
       *  h?llo will match hello hallo hhllo
       *  h*llo will match hllo heeeello
       *  h[ae]llo will match hello and hallo, but not hillo
       * @param pattern
       * @return
       */
      public Set<String> keys(String pattern);
    
}
