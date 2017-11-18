package tangent;

import java.io.IOException;

//import org.apache.hadoop.conf.Configuration;
//import org.apache.hadoop.fs.FileSystem;
//import org.apache.hadoop.fs.FileSystem;
//import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
//import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
//import org.apache.hadoop.mapreduce.Mapper.Context;
//import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
//import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
//import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

public class GetInputGenre extends MRJob
{
	public GetInputGenre()
	{
		jobName = "GetInputGenre";
		inputPath=Constants.genresFile;
	}	
	
	public boolean start() throws Exception
	{
		setupJob();
		
		j.setJarByClass(GetInputGenre.class);		
		j.setMapperClass(Map.class);
		j.setNumReduceTasks(0);
		
		return doJob();
	}
	
	private static class Map extends Mapper<LongWritable, Text, Text, IntWritable>
	{
		public void map(LongWritable key, Text value, Context con) throws IOException, InterruptedException
		{
			String line=value.toString();
			int fcomma = line.indexOf(',');
			int lcomma = line.lastIndexOf(',');
			
			String id = line.substring(0, fcomma);
			if(!Transport.inputMovie.movieID.equals(id))
				return;
			
			String movieName = line.substring(fcomma+1, lcomma);			
			String[] genres=line.substring(lcomma+1).split("[|]");
			
			Transport.inputMovie.movieName=movieName;
			Transport.inputMovie.simScore = 1;
			for(String genre:genres)
				Transport.inputGenres.add(genre);
		}
	}
}
