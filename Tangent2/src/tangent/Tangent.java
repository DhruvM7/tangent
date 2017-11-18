package tangent;

import java.util.PriorityQueue;

import org.apache.hadoop.conf.*;
import org.apache.hadoop.util.GenericOptionsParser;

public class Tangent 
{
	
	static String jobName = "SimilarUsers";
	
	public static void main(String args[]) throws Exception
	{
		Configuration c = new Configuration();
		String[] realArgs = new GenericOptionsParser(c, args).getRemainingArgs();		
		Transport.inputMovie = new DM();
		Transport.inputMovie.movieID=realArgs[0];
		Transport.inputMovie.rating = Double.parseDouble(realArgs[1]);		
		
		boolean done = true;
		
		GetInputGenre j1=new GetInputGenre();
		done = j1.start();		
		
		SimilarUsers j2=new SimilarUsers();
		done = j2.start();	
		
		MovieRatings j3=new MovieRatings();
		done=j3.start();
		
		String prevOutput=j3.outputPath;
		ReduceJoin j4=new ReduceJoin(prevOutput);
		done=j4.start();
		
		System.out.println("Input:");
		System.out.println(DM.explain());
		System.out.println(Transport.inputMovie);
		System.out.println("-----------------------------");
		
		printQ(Transport.keepSimilar, "Most Similar Movies:");
		printQ(Transport.discardSimilar, "Most Disimilar Movies:");
		printQ(Transport.ratingSort, "Best Rated Movies:");
		
		System.exit(done?0:1);
	}
	
	public static void printQ(PriorityQueue<?> q, String heading)
	{
		System.out.println(heading);
		System.out.println("-----------------------------");
		System.out.println(DM.explain());
		while(!q.isEmpty())
		{
			Object x=q.poll();
			System.out.println(x);
		}
		System.out.println("-----------------------------");
	}
}
