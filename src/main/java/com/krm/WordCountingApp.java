package com.krm;

import com.krm.model.Word;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.streaming.Durations;
import org.apache.spark.streaming.api.java.JavaDStream;
import org.apache.spark.streaming.api.java.JavaInputDStream;
import org.apache.spark.streaming.api.java.JavaPairDStream;
import org.apache.spark.streaming.api.java.JavaStreamingContext;
import org.apache.spark.streaming.kafka010.ConsumerStrategies;
import org.apache.spark.streaming.kafka010.ConsumerStrategy;
import org.apache.spark.streaming.kafka010.KafkaUtils;
import org.apache.spark.streaming.kafka010.LocationStrategies;
import scala.Tuple2;

import java.util.*;

import static com.datastax.spark.connector.japi.CassandraJavaUtil.javaFunctions;
import static com.datastax.spark.connector.japi.CassandraJavaUtil.mapToRow;

public class WordCountingApp {

    public static String SPARK_HOME = "B:/Internship/spark-2.4.1-bin-hadoop2.7";
    public static String APP_NAME = "FindWork";
    public static String MASTER_URI = "local";
    public static String CASSANDRA_HOST = "127.0.0.1";

    public WordCountingApp() throws Exception {

        // Spark configuration
        System.setProperty("hadoop.home.dir", SPARK_HOME);
        SparkConf sparkConf = new SparkConf()
                .setAppName(APP_NAME)
                .setSparkHome(SPARK_HOME)
                .setMaster(MASTER_URI)
                .set("spark.cassandra.connection.host", CASSANDRA_HOST);

        // Kafka configuration
        Map<String, Object> kafkaParams = new HashMap<>();
        kafkaParams.put("bootstrap.servers", "localhost:9092");
        kafkaParams.put("key.deserializer", StringDeserializer.class);
        kafkaParams.put("value.deserializer", StringDeserializer.class);
        kafkaParams.put("group.id", "job-post");
        kafkaParams.put("auto.offset.reset", "latest");
        kafkaParams.put("enable.auto.commit", false);

        Collection<String> topics = Arrays.asList("post", "test", "topic_x");

        JavaStreamingContext streamingContext = new JavaStreamingContext(sparkConf, Durations.seconds(5));

        ConsumerStrategy subscribe = ConsumerStrategies.Subscribe(topics, kafkaParams);
        JavaInputDStream<ConsumerRecord<String, String>> messages = KafkaUtils.createDirectStream(streamingContext, LocationStrategies.PreferConsistent(), subscribe);

        // Analyse data here
        JavaPairDStream<String, String> results = messages.mapToPair(record -> new Tuple2<>(record.key(), record.value()));

        JavaDStream<String> lines = results.map(tuple2 -> tuple2._2());
        JavaDStream<String> words = lines.flatMap(x -> Arrays.asList(x.split("\\s+")).iterator());

        JavaPairDStream<String, Integer> wordCounts = words.mapToPair(s -> new Tuple2<>(s, 1)).reduceByKey((i1, i2) -> i1 + i2);

        wordCounts.foreachRDD(javaRdd -> {
            Map<String, Integer> wordCountMap = javaRdd.collectAsMap();

            for (String key : wordCountMap.keySet()) {
                List<Word> wordList = Arrays.asList(new Word(key, wordCountMap.get(key)));
                JavaRDD<Word> rdd = streamingContext.sparkContext().parallelize(wordList);

                // Store to cassandra
                javaFunctions(rdd).writerBuilder("vocabulary", "words", mapToRow(Word.class)).saveToCassandra();
            }

        });

        streamingContext.start();
        streamingContext.awaitTermination();
    }

    public static void main(String[] args) throws Exception {
        WordCountingApp wordCountingApp = new WordCountingApp();

    }
}