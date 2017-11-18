package tangent;

import java.io.IOException;
import java.util.HashSet;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;

public class MovieRatings extends MRJob
{
	static HashSet<String> userids = Transport.userids;
	
	public MovieRatings()
	{
		jobName = "MovieRatings";
		inputPath = Constants.ratingsFile;
	}
	
	public boolean start() throws Exception
	{
		setupJob();
		
		j.setJarByClass(MovieRatings.class);		
		j.setMapperClass(Map.class);
		j.setReducerClass(Reduce.class);
		j.setOutputKeyClass(Text.class);
		j.setOutputValueClass(Text.class);
		
		return doJob();
	}
	
	private static class Map extends Mapper<LongWritable, Text, Text, Text>
	{
		public void map(LongWritable key, Text value, Context con) throws IOException, InterruptedException
		{
			String line=value.toString();
			String fields[]=line.split("[,]");
			if(!userids.contains(fields[0]))
					return;
			con.write(new Text(fields[1]), new Text(fields[2])); //movieID, rating
		}
	}
	
	private static class Reduce extends Reducer<Text, Text, Text, Text>
	{
		public void reduce(Text key, Iterable<Text> values, Context con) throws IOException, InterruptedException
		{
			double avg=0; int i=0;
			for(Text value:values)
			{
				avg+=Double.parseDouble(value.toString());
				i++;
			}
			avg=avg/i;
			if(i<3 || avg<4.5)
				return;
			con.write(key, new Text(avg+""));
		}
	}
}
