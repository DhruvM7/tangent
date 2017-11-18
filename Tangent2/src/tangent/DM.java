package tangent;

public class DM
{
	public String movieName, movieID;
	double rating, simScore;
	public DM()
	{
		;
	}
	public DM(String movieName, String movieID, double rating, double simScore)
	{
		this.movieName=movieName;
		this.movieID=movieID;
		this.rating=rating;
		this.simScore=simScore;
	}
	public DM(String movie, String movieID, String rating, double simScore)
	{
		this.movieName=movie;
		this.movieID=movieID;
		this.rating=Double.parseDouble(rating);
		this.simScore=simScore;
	}
	public DM(DM copy)
	{
		this.movieName=copy.movieName;
		this.movieID=copy.movieID;
		this.rating=copy.rating;
		this.simScore=copy.simScore;
	}
	
	public String toString()
	{
		return String.format("%-10.10s %-80.80s %-10.10s  %-10.10s", movieID, movieName, String.format("%.3f", rating), String.format("%.3f", simScore));
		//return movieID + "\t\t" + movieName + "\t\t\t" + String.format("%.3f", rating) + "\t" + String.format("%.3f", simScore);
	}
	
	public static String explain()
	{
		return String.format("%-10.10s %-80.80s %-10.10s  %-10.10s", "Movie ID", "Movie Name", "Rating", "Similarity");
		//return "Movie ID" + "\t\t" + "Movie Name" + "\t\t\t" + "Rating" + "\t" + "Similarity";
	}
}
