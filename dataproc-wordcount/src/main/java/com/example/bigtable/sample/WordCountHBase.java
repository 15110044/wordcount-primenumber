package com.example.bigtable.sample;

import java.io.IOException;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.GenericOptionsParser;

public class WordCountHBase {
 public static void main(String [] args) throws Exception
 {
   Configuration c=new Configuration();
   String[] files=new GenericOptionsParser(c,args).getRemainingArgs();
   Path input=new Path(files[0]);
   Path output=new Path(files[1]);
   Job j=new Job(c,"wordcount");
   j.setJarByClass(WordCountHBase.class);
   j.setMapperClass(MapForWordCount.class);
   j.setReducerClass(ReduceForWordCount.class);
   j.setOutputKeyClass(Text.class);
   j.setOutputValueClass(IntWritable.class);
   FileInputFormat.addInputPath(j, input);
   FileOutputFormat.setOutputPath(j, output);
   System.exit(j.waitForCompletion(true)?0:1);
 }
 //class Map
 public static class MapForWordCount extends Mapper<LongWritable, Text, Text, IntWritable>
 {
   public void map(LongWritable key, Text value, Context con) throws IOException, InterruptedException
   {
     //lay thong tin trong file input chuyen sang 1 chuoi kieu string
     String line = value.toString();
     //tach tung so bo vao mang string cu dung 1 dau phay la lay duoc 1 so
     String[] numbers=line.split(",");
     for(String number: numbers )
     {
       //chuyen sang kieu so
       int num = Integer.parseInt(number);
       //khoi tao key out put
       Text outputKey = new Text(number.toUpperCase().trim());
       //khoi tao bien luu gia tri cua key out put
       IntWritable outputValue = new IntWritable(1);
       //kiem tra xem so tren phai so nguyen to hay khong
       //neu true thi ghi vao cap gia tri <key,value>
       if(isPrimeNumber(num)){
         con.write(outputKey, outputValue);
       }       
     }
   }
   //ham kiem tra so nguyen to
   public static boolean isPrimeNumber(int n) {
     // so nguyen n < 2 khong phai la so nguyen to
     if (n < 2) {
         return false;
     }
     // check so nguyen to khi n >= 2
     int squareRoot = (int) Math.sqrt(n);
     for (int i = 2; i <= squareRoot; i++) {
         if (n % i == 0) {
             return false;
         }
     }
     return true;
   }

 }
 //class Reduce
 public static class ReduceForWordCount extends Reducer<Text, IntWritable, Text, IntWritable>
 {
   public void reduce(Text word, Iterable<IntWritable> values, Context con) throws IOException, InterruptedException
   {
     int sum = 0;
       for(IntWritable value : values)
       {
    	   sum += value.get();
       }
       con.write(word, new IntWritable(sum));
   }
 }
}
