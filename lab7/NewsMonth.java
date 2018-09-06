
//Set appropriate package name

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.function.FlatMapFunction;
import org.apache.spark.api.java.function.MapFunction;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Encoders;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.catalyst.encoders.ExpressionEncoder;
import org.apache.spark.sql.catalyst.encoders.RowEncoder;
import org.apache.spark.sql.types.DataTypes;
import org.apache.spark.sql.types.StructType;



/**
 * This class uses Dataset APIs of spark to count number of articles per month
 * The year-month is obtained as a dataset of String
 * */

public class NewsMonth {

	public static void main(String[] args) {
		
		//Input dir - should contain all input json files
		String inputPath="/home/deepesh/Downloads/newsdata"; //Use absolute paths 
		
		//Ouput dir - this directory will be created by spark. Delete this directory between each run
		String outputPath="/home/deepesh/Downloads/output1";   //Use absolute paths
		
		SparkSession sparkSession = SparkSession.builder()
				.appName("Month wise news articles")		//Name of application
				.master("local")								//Run the application on local node
				.config("spark.sql.shuffle.partitions","2")		//Number of partitions
				.getOrCreate();
		
		//Read multi-line JSON from input files to dataset
		Dataset<Row> inputDataset=sparkSession.read().option("multiLine", true).json(inputPath);   
		
		
		// Apply the map function to extract the year-month
		
		Dataset<String> yearMonthDataset=inputDataset.flatMap(new FlatMapFunction<Row, String>(){
			public Iterator<String> call(Row row) throws Exception {
				// The first 7 characters of date_published gives the year-month 
				String sourcename=((String)row.getAs("source_name"));
				String articleBody=((String)row.getAs("article_body"));
				
				//copied this part from wordcount.java file
				articleBody= articleBody.toLowerCase().replaceAll("[^A-Za-z]", " ");  //Remove all punctuation and convert to lower case
				articleBody = articleBody.replaceAll("( )+", " ");   //Remove all double spaces
				articleBody = articleBody.trim();
				  
				//copied it from wordcount
				List<String> wordList = Arrays.asList(articleBody.split(" ")); //Get words
				//System.out.println(yearMonthPublished1);
				List<String> concatedList = new ArrayList<String>();
				for(int i=0;i<wordList.size();i++){  
			         concatedList.add(sourcename +"," +wordList.get(i)) ;
			    }
				return concatedList.iterator();
				
			}
			
		}, Encoders.STRING());

		//JavaRDD<String> lines = sparkSession.read().textFile(inputPath).javaRDD();
		//Alternative way use flatmap using lambda function
		/*Dataset<String> yearMonthDataset=inputDataset.map(
				row-> {return ((String)row.getAs("date_published")).substring(0, 7);}, 
						Encoders.STRING());
		*/
		
		
		//The column name of the dataset for string encoding is value
		// Group by the desired column(s) and take count. groupBy() takes 1 or more parameters
		//Rename the result column as year_month_count
		Dataset<Row> count=yearMonthDataset.groupBy("value").count().as("year_month_count");
		
		
		//Outputs the dataset to the standard output
		//count.show();
		
		
		//Ouputs the result to a file
		count.toJavaRDD().saveAsTextFile(outputPath);	
		
	}
	
}
