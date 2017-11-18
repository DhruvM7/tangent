package tangent;

import java.io.IOException;

import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.MultipleInputs;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;

public class ReduceJoin extends MRJob
{
	String inputPath2;
	public ReduceJoin(String avgRatings)
	{
		jobName = "SortByRating";
		inputPath = avgRatings + "part-r-00000";
		inputPath2 = Constants.genresFile;
	}
	
	protected void setupJob() throws IOException
	{
		initialize();
		setupOutput();
	}
	
	public boolean start() throws Exception
	{
		setupJob();
		
		j.setJarByClass(ReduceJoin.class);		
		
		MultipleInputs.addInputPath(j, new Path(inputPath), TextInputFormat.class, Map1.class);
		MultipleInputs.addInputPath(j, new Path(inputPath2), TextInputFormat.class, Map2.class);
		
		j.setReducerClass(Reduce.class);
		j.setOutputKeyClass(Text.class);
		j.setOutputValueClass(Text.class);
		
		return doJob();
	}
	
	private static class Map1 extends Mapper<LongWritable, Text, Text, Text>
	{
		public void map(LongWritable key, Text value, Context con) throws IOException, InterruptedException
		{
			String line = value.toString();
			String fields[] = line.split("[\t]");
			con.write(new Text(fields[0]), new Text(fields[1]));
		}
	}
	
	private static class Map2 extends Mapper<LongWritable, Text, Text, Text>
	{
		public void map(LongWritable key, Text value, Context con) throws IOException, InterruptedException
		{
			String line = value.toString();
			int fcomma = line.indexOf(',');
			String id = line.substring(0, fcomma);
			String rest = line.substring(fcomma+1);
			con.write(new Text(id), new Text(rest));
		}
	}
	
	private static class Reduce extends Reducer<Text, Text, Text, Text>
	{
		public void reduce(Text key, Iterable<Text> values, Context con) throws IOException, InterruptedException
		{
			String movieID = key.toString();
			String ratingVal = "";
			String genreVal = "";
			for(Text value: values)
			{
				String str = value.toString();
				if(str.contains(","))
					genreVal = str;
				else
					ratingVal = str;
			}
			if(ratingVal.isEmpty())
				return;
			
			double rating = Double.parseDouble(ratingVal);
			int lcomma = genreVal.lastIndexOf(',');
			String[] genres=genreVal.substring(lcomma+1).split("[|]");
			double simScore = simScore(genres);
			String movieName = genreVal.substring(0, lcomma);
			
			DM data = new DM(movieName, movieID, rating, simScore);
						
			Transport.keepSimilar.add(new FavourSimilar(data));
			if (Transport.keepSimilar.size() > 10)
				Transport.keepSimilar.poll();
			Transport.discardSimilar.add(new DiscardSimilar(data));
			if (Transport.discardSimilar.size() > 10)
				Transport.discardSimilar.poll();
			Transport.ratingSort.add(new RatingSort(data));
			if (Transport.ratingSort.size() > 10)
				Transport.ratingSort.poll();
		}
		
		public double simScore(String genres[])
		{
			double totalGenres = genres.length + Transport.inputGenres.size();
			int matches = 0;
			for(String genre:genres)
			{
				if(Transport.inputGenres.contains(genre))
					matches++;
			}
			return matches*2/totalGenres;
		}
	}
}
