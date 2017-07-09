package com.vergilyn.demo.jedis;

import com.vergilyn.demo.redis.RedisApplication;

import java.util.Iterator;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import redis.clients.jedis.Jedis;

/**
 * 防止刷票。
 * 注意问题1: redis与事务, 从代码可以看出会修改redis缓存。
 *  假设规则是: 1min内投票不超过10;
 *  现在的投票记录是: 0~50s投了9次。
 *  某人A在51s对其投票, redis记录的结果是 0~51s内投了10次; 不存在刷票
 *  但更新数据库时因为某个原因回滚了,前台提示投票失败。
 *  此时某人A又在55s对其投票,但redis记录的结果是0~51s内投了10次, 55s这次检测出刷票.
 *  但从上面可以看出51s那次不应该在redis中记录,
 *  所以问题是 事务回滚后, redis被修改的缓存要怎么回滚？
 *
 * @author VergiLyn
 * @bolg http://www.cnblogs.com/VergiLyn/
 * @date 2017/7/4
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = RedisApplication.class)
public class PreventBrushVotesTest {

    @Autowired
    private Jedis jedis;

    @Before
    public void before() {
        // 清空数据
        System.out.println("before test >>>> clear: " + jedis.flushDB());
        // 准备数据
        initVoteNumber();
    }

    @Test
    public void brushVotes(){
        VoteRules rules = new VoteRules(10,120);
        boolean rs1 = isBrushVotes(VoteEnum.VOTE_001,100, rules);
        System.out.println(rs1);

        boolean rs2 = isBrushVotes(VoteEnum.VOTE_001,110, rules);
        System.out.println(rs2);

        boolean rs3 = isBrushVotes(VoteEnum.VOTE_001,115, rules);
        System.out.println(rs3);

        boolean rs4 = isBrushVotes(VoteEnum.VOTE_001,118, rules);
        System.out.println(rs4);

        // 投票途中刷票检测规则改变
        VoteRules rules2 = new VoteRules(5,60);
        boolean rs5 = isBrushVotes(VoteEnum.VOTE_001,118, rules2);
        System.out.println(rs5);
    }

    /**
     * 判断是否存在刷票
     * @param voteEnum
     * @param date
     * @param rules
     * @return true: 存在刷票嫌疑;
     */
    private boolean isBrushVotes(VoteEnum voteEnum, int date, VoteRules rules) {
        // 取得投票项的投票记录
        List<String> records = jedis.lrange(voteEnum.key, 0, -1);

        // 1. 未超过限制, 不存在刷票。
        if (records.size() < rules.maxVotes) {
            jedis.rpush(voteEnum.key, date+""); // 记录本次投票
            return false;
        }
        // else : records.size() >= rules.maxVotes

        // 2. 得到在 间隔时间内的 投票记录;
        int beginDate = date - rules.interval;
        for (String record : records){
            if(Integer.parseInt(record) <= beginDate){   // 间隔时间之前的记录移除
                jedis.lrem(voteEnum.key,0,record);
            }else{// 标准构想的是 jedis的list是有序的(时间越早的记录index越小),所以检测到第一个时间内的记录后可以break(加快判断效率)
                break;
            }

        }
        // 得到间隔时间内的投票记录
        records = jedis.lrange(voteEnum.key, 0, -1);

        if (records.size() < rules.maxVotes) {  // 间隔时间内未超过票数限制, 不存在刷票。
            jedis.rpush(voteEnum.key, date+""); // 记录本次投票
            return false;
        }

        /* 间隔时间内超过票数限制
         * 注意: 标准构想的是records.size() = interval;
         * 原因:
         *  假设1min内限制10票。  现在 0~50s就投了10票, 所以size = 10;
         *  51s又投一票, 51s的这票开始被检测出此投票项有刷票嫌疑。所以, 这票作废。
         *  (可视情况, 投票项被检测刷票后的投票, 需不需要记录到缓存中, 虽然这票没有投起。
         *   但可以用于投票项解除限制后,用于作为刷票记录检测(蛋疼逻辑, 虽然几条记录又没真正投起, 也没有影响到得票数))
         */
//        jedis.rpush(voteEnum.key, date+""); // 不记录本次投票; 因为这票并未投成功(检测出刷票,不能再投票)

        return true;
    }

    /**
     * 初始化测试数据:
     * <br>0-60s内投了5票; 60-90s投了3票
     * <br>value: 表示投票的时间
     */
    private void initVoteNumber() {
        // 0-60s内投了5票, 
        jedis.rpush(VoteEnum.VOTE_001.key, "10");
        jedis.rpush(VoteEnum.VOTE_001.key, "20");
        jedis.rpush(VoteEnum.VOTE_001.key, "25");
        jedis.rpush(VoteEnum.VOTE_001.key, "45");
        jedis.rpush(VoteEnum.VOTE_001.key, "52");
        // 60-90s投了3票
        jedis.rpush(VoteEnum.VOTE_001.key, "65");
        jedis.rpush(VoteEnum.VOTE_001.key, "72");
        jedis.rpush(VoteEnum.VOTE_001.key, "80");

        // 0-60s内投了5票,
        jedis.rpush(VoteEnum.VOTE_002.key, "8");
        jedis.rpush(VoteEnum.VOTE_002.key, "12");
        jedis.rpush(VoteEnum.VOTE_002.key, "24");
        jedis.rpush(VoteEnum.VOTE_002.key, "50");
        jedis.rpush(VoteEnum.VOTE_002.key, "55");
        // 60-90s投了3票
        jedis.rpush(VoteEnum.VOTE_002.key, "70");
        jedis.rpush(VoteEnum.VOTE_002.key, "80");
        jedis.rpush(VoteEnum.VOTE_002.key, "90");
    }
}

/**
 * 投票项
 */
enum VoteEnum {
    VOTE_001("REDIS_VOTE_001"), VOTE_002("REDIS_VOTE_002");

    VoteEnum(String key) {
        this.key = key;
    }

    public final String key;

}

/**
 * 投票规则: 在interval内获得票数超过maxVotes即视为存在刷票嫌疑。
 */
class VoteRules {
    public final int maxVotes;
    public final int interval;

    /**
     * @param maxVotes 最大投票数
     * @param interval 间隔
     */
    public VoteRules(int maxVotes, int interval) {
        this.maxVotes = maxVotes;
        this.interval = interval;
    }
}