package com.krm.components;

import com.krm.model.Count;
import com.krm.model.Word;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.RelationalGroupedDataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.apache.spark.sql.functions.col;

@Component
public class WordCount {

    @Autowired
    private SparkSession sparkSession;

    public List<Count> count(String input) {

        String[] _words = input.split(" ");

        List<Word> words = Arrays.stream(_words).map(Word::new).collect(Collectors.toList());
        Dataset<Row> dataFrame = sparkSession.createDataFrame(words, Word.class);
        dataFrame.show();

        //StructType structType = dataFrame.schema();

        RelationalGroupedDataset groupedDataset = dataFrame.groupBy(col("word"));

        groupedDataset.count().show();

        List<Row> rows = groupedDataset.count().collectAsList();

        return rows.stream().map(row -> new Count(row.getString(0), row.getLong(1))).collect(Collectors.toList());
    }
}