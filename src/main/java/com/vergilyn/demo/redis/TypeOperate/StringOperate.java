package com.vergilyn.demo.redis.TypeOperate;

/**
 * Created by VergiLyn on 2016/8/22.
 */
public class StringOperate extends TypeOperateObject {
    public void show() {

        //jedis具备的功能shardedJedis中也可直接使用，下面测试一些前面没用过的方法
        System.out.println("======================String_2==========================");
        // 清空数据
        System.out.println("清空库中所有数据："+jedis.flushDB());

        System.out.println("=============新增键值对时防止覆盖原先值=============");
        System.out.println("原先key301不存在时，新增key301："+shardedJedis.setnx("key301", "value301"));
        System.out.println("原先key302不存在时，新增key302："+shardedJedis.setnx("key302", "value302"));
        System.out.println("当key302存在时，尝试新增key302："+shardedJedis.setnx("key302", "value302_new"));
        System.out.println("获取key301对应的值："+shardedJedis.get("key301"));
        System.out.println("获取key302对应的值："+shardedJedis.get("key302"));

        System.out.println("=============超过有效期键值对被删除=============");
        // 设置key的有效期，并存储数据
        System.out.println("新增key303，并指定过期时间为2秒"+shardedJedis.setex("key303", 2, "key303-2second"));
        System.out.println("获取key303对应的值："+shardedJedis.get("key303"));
        try{
            Thread.sleep(3000);
        }
        catch (InterruptedException e){
        }
        System.out.println("3秒之后，获取key303对应的值："+shardedJedis.get("key303"));

        System.out.println("=============获取原值，更新为新值一步完成=============");
        System.out.println("key302原值："+shardedJedis.getSet("key302", "value302-after-getset"));
        System.out.println("key302新值："+shardedJedis.get("key302"));

        System.out.println("=============获取子串=============");
        System.out.println("获取key302对应值中的子串："+shardedJedis.getrange("key302", 5, 7));
    }
}
