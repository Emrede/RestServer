package de.emre.restserver;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.mongodb.Block;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.apache.commons.lang3.time.DateUtils;
import org.bson.Document;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.*;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.*;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.*;
import static org.springframework.data.mongodb.core.query.Criteria.where;

@RestController
public class LogsController {
    //    @Autowired
//    private LogsRepository repository;
    private static int logCount = 0;
    private List<LocalTime> timeList = new ArrayList<LocalTime>();
    private List<Integer> hours = new ArrayList<Integer>();

    private MongoCollection<Document> connectDB() {
        final String uriString = "mongodb://localhost:27017/TEBLogs";
        MongoClientURI uri = new MongoClientURI(uriString);
        MongoClient mongoClient = new MongoClient(uri);
        MongoDatabase mongoDB = mongoClient.getDatabase("TEBLogs");
        MongoCollection<Document> collection = mongoDB.getCollection("logs");
        return collection;
    }

    @RequestMapping("/testlogs")
    public List<Logs> testlogs() {
        List<Logs> logs = new ArrayList<Logs>();
        Logs log1 = new Logs("test", "test", "test", "test",
                "test", "test", " test");
        logs.add(log1);
        Logs log2 = new Logs("test2", "test2", "test2", "test2",
                "test2", "test2", " test2");
        logs.add(log2);
        return logs;
    }

    //This func returns city name and its log counts
    @RequestMapping("/sum")
    public String sum() {
        ApplicationContext ctx = new AnnotationConfigApplicationContext(SpringMongoConfig.class);
        //MongoOperations mongoOperation = (MongoOperations) ctx.getBean("mongoTemplate");
        MongoTemplate mongoOperation = (MongoTemplate) ctx.getBean("mongoTemplate");
        List<List<Summary>> listOfSum = new ArrayList<List<Summary>>();
        List<String> listOfString = new ArrayList<>();
        String json = null;

//        Query query = new Query();
//                long diffInMillies = startDate.getTime() - endDate.getTime();



//        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
//        simpleDateFormat.setTimeZone((TimeZone.getTimeZone("GMT")));

        DateTimeFormatter parser = ISODateTimeFormat.dateTime();
        String date1 = "2019-08-21T00:00:30.874Z";
        String date2 = "2019-08-25T19:43:30.874Z";

        DateTime startDate = new DateTime();
        DateTime endDate = new DateTime();

        startDate = parser.parseDateTime(date1);
        endDate = parser.parseDateTime(date2);

//        long sd = startDate.getTime();
//        long ed = endDate.getTime();

        while (startDate.isBefore(endDate)) {
            DateTime intervalDate = startDate.plusMinutes(2);

            Aggregation aggregate = newAggregation(
                    match(where("date").gt(startDate).lte(intervalDate)),
                    group("name").count().as("count"),
                    project("count").and("name").previousOperation()
            );
            AggregationResults<Summary> groupResults = mongoOperation.aggregate(aggregate, "logs", Summary.class);
            List<Summary> result = groupResults.getMappedResults();

//            SimpleDateFormat localDateFormat = new SimpleDateFormat("HH:mm:ss");
//            String time = localDateFormat.format(startDate);

            for (Summary s : result) {
                s.setDate(startDate);
                System.out.println(s.getName() + ": " + s.getCount() + " - " + s.getDate());
                listOfString.add("\"" + s.getName() + "\": { \"" + startDate + "\": " + s.getCount() + "}");
//                listOfSum.add(result);
            }
            startDate = startDate.plusMinutes(2); //add minute
        }
        System.out.println(listOfString);
//        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
//        try {
//            json = ow.writeValueAsString(listOfString);
//            System.out.println(json);
//        } catch (JsonProcessingException e) {
//            e.printStackTrace();
//        }
        String formattedString = listOfString.toString()
                .replace("[", "")  //remove the right bracket
                .replace("]", "")  //remove the left bracket
                .trim();

        return "{ " + formattedString + " }";
    }
}