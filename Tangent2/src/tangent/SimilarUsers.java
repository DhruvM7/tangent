package tangent;

import java.io.IOException;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;

public class SimilarUsers extends MRJob
{
	public SimilarUsers()
	{
		jobName = "SimilarUsers";
		inputPath = Constants.ratingsFile;
	}
	
	public boolean start() throws Exception
	{
		setupJob();
		
		j.setJarByClass(SimilarUsers.class);		
		j.setMapperClass(Map.class);
		j.setReducerClass(Reduce.class);
		j.setOutputKeyClass(Text.class);
		j.setOutputValueClass(IntWritable.class);
		
		return doJob();
	}
	
	private static class Map extends Mapper<LongWritable, Text, Text, IntWritable>
	{
		public void map(LongWritable key, Text value, Context con) throws IOException, InterruptedException
		{
			String line=value.toString();
			String fields[]=line.split("[,]");
			String movieID=fields[1];
			String rating=fields[2];
			if(!Transport.inputMovie.movieID.equals(movieID))
				return;
			if(!(Transport.inputMovie.rating==Double.parseDouble(rating)))
				return;
			con.write(new Text(fields[0]), new IntWritable(1));
		}
	}
	
	private static class Reduce extends Reducer<Text, IntWritable, Text, IntWritable>
	{
		public void reduce(Text key, Iterable<IntWritable> values, Context con) throws IOException, InterruptedException
		{
			int sum=0;
			for(IntWritable value:values)
				sum+=Integer.parseInt(value.toString());
			con.write(key, new IntWritable(sum));
			Transport.userids.add(key.toString());
		}
	}
}
