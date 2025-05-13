package com.example.WebLearn.repository;

import com.example.WebLearn.entity.AnswerDetail;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.util.Pair;

import java.util.List;

public interface AnswerDetailRepository extends JpaRepository<AnswerDetail, Long> {
    @Modifying
    @Transactional
    @Query("UPDATE AnswerDetail a " +
            "SET a.statusAnswer = CASE " +
            "   WHEN a.answer = (SELECT q.result FROM Question q WHERE q.id = a.question.id) THEN 'TRUE' " +
            "   ELSE 'FALSE' " +
            "END " +
            "WHERE a.quizSubmit.id = :quizSubmitId")
    void updateStatusAnswer(@Param("quizSubmitId") Long quizSubmitId);

    @Query(value = "SELECT a.quizsubmit_id, COUNT(a.status_answer) AS correct_answers" +
            " FROM answerdetail a" +
            " WHERE a.quizsubmit_id = :quizSubmitId AND a.status_answer = 'TRUE'" +
            " GROUP BY a.quizsubmit_id;",
            nativeQuery = true
    )
    Pair<Long, Integer> correctAnswers(@Param("quizSubmitId") Long quizSubmitId);

    List<AnswerDetail> findByQuizSubmit_Id(Long quizSubmitId);
}
