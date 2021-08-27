package com.atguigu.crowd.handler;

import com.atguigu.crowd.util.ResultEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import java.util.concurrent.TimeUnit;

@RestController
public class RedisHandler {
    @Autowired
    private StringRedisTemplate redisTemplate;

    @RequestMapping("/set/redis/key/value/remote")
    public ResultEntity<String> setRedisKeyValueRemote(
            @RequestParam("key") String key,
            @RequestParam("value") String value) {

        try {
            ValueOperations<String, String> operations = redisTemplate.opsForValue();

            operations.set(key, value);

            return ResultEntity.successWithoutData();
        } catch (Exception e) {
            e.printStackTrace();

            return ResultEntity.failed(e.getMessage());
        }

    }

    @RequestMapping("/set/redis/key/value/remote/with/timeout")
   public ResultEntity<String> setRedisKeyValueRemoteWithTimeout(
            @RequestParam("key") String key,
            @RequestParam("value") String value,
            @RequestParam("time") long time,
            @RequestParam("timeUnit") TimeUnit timeUnit) {

        try {
            //若提交相同的key，对应的value会将之前的覆盖掉
            ValueOperations<String, String> operations = redisTemplate.opsForValue();

            operations.set(key, value, time, timeUnit);

            return ResultEntity.successWithoutData();
        } catch (Exception e) {
            e.printStackTrace();

            return ResultEntity.failed(e.getMessage());
        }
    }

    @RequestMapping("/get/redis/string/value/by/key")
    public ResultEntity<String> getRedisStringValueByKeyRemote(@RequestParam("key") String key) {

        try {
            ValueOperations<String, String> operations = redisTemplate.opsForValue();

            String value = operations.get(key);

            return ResultEntity.successWithData(value);
        } catch (Exception e) {
            e.printStackTrace();

            return ResultEntity.failed(e.getMessage());
        }

    }

    @RequestMapping("/remove/redis/key/remote")
    public ResultEntity<String> removeRedisKeyRemote(@RequestParam("key") String key) {
        try {

            redisTemplate.delete(key);

            return ResultEntity.successWithoutData();

        } catch (Exception e) {
            e.printStackTrace();

            return ResultEntity.failed(e.getMessage());
        }
    }
}
