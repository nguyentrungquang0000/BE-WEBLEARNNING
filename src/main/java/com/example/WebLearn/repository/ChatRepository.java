package com.example.WebLearn.repository;

import com.example.WebLearn.entity.ChatDocument;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
@Repository
public interface ChatRepository extends MongoRepository<ChatDocument, String> {

    @Query(value = """
        {
            classId: ?0,
            $or: [
                { date: { $lt: ?1 } },
                { date: ?1,  _id: { $lt: ObjectId(?2) } }
            ]
        }
    """, sort = "{ date: -1, _id: -1 }")
    List<ChatDocument> getMessageOld(
            String classId,
            Date beforeDate,
            String beforeId,
            Pageable pageable
    );

    List<ChatDocument> findByClassId(String classId, Pageable pageable);
}
