package com.example.jira_dashboard_backend.Service;

import com.example.jira_dashboard_backend.model.Counter;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import static org.springframework.data.mongodb.core.query.Criteria.where;

@Service
public class SequenceGeneratorService {

    private final MongoOperations mongoOperations;

    public SequenceGeneratorService(MongoOperations mongoOperations){
        this.mongoOperations = mongoOperations;

    }

    public long generateSequence(String seqName){
        Counter counter = mongoOperations.findAndModify(Query.query(where("_id").is(seqName)),
                new Update().inc("seq",1),
                FindAndModifyOptions.options().returnNew(true).upsert(true),
                Counter.class);
        return !java.util.Objects.isNull(counter) ? counter.getSeq() : 1;
    }
}
