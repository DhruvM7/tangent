package tangent;

import java.io.IOException;

import org.apache.hadoop.conf.*;
import org.apache.hadoop.fs.*;
import org.apache.hadoop.mapreduce.*;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

public class MRJob
{
	public String jobName, inputPath, outputPath;
	protected Configuration c;
	protected Job j;
	
	protected void setupJob() throws IOException
	{
		initialize();
		setupSingleInput();
		setupOutput();
	}
	
	protected void initialize() throws IOException
	{
		c = new Configuration();
		j = new Job(c, jobName);
		outputPath = Constants.outputFolder + jobName + "/";
	}
	
	protected void setupSingleInput() throws IOException
	{
		Path input=new Path(inputPath);
		FileInputFormat.addInputPath(j, input);
	}
	
	protected void setupOutput() throws IOException
	{
		Path output=new Path(outputPath);
		FileOutputFormat.setOutputPath(j, output);		
		FileSystem fs = FileSystem.get(c);
		if(fs.exists(output))
			fs.delete(output, true);
	}
	
	public boolean doJob() throws ClassNotFoundException, IOException, InterruptedException
	{
		System.out.println("\t [STATUS] Job " + jobName + " Starting...");
		boolean done = j.waitForCompletion(true);
		System.out.println("\t [STATUS] Job " + jobName + " Exiting...");
		return done;
	}
}
