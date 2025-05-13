package com.example.WebLearn.config.Redis;

import com.example.WebLearn.service.QuizSubmitRedis.QuizSubmitRedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

@Component
public class RedisExpiredEventListener implements MessageListener {

    @Autowired
    private QuizSubmitRedisService quizSubmitRedisService;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        String expiredKey = message.toString();
        if (expiredKey.startsWith("quiz:expiration:")) {
            Long quizId = Long.valueOf(expiredKey.split(":")[expiredKey.split(":").length - 1]);
            String email = String.valueOf(expiredKey.split(":")[expiredKey.split(":").length - 2]);
            quizSubmitRedisService.submitQuiz(quizId, email);
        }
    }
}

