package tangent;

public class FavourSimilar extends DM implements Comparable<FavourSimilar>
{
	public FavourSimilar(DM copy)
	{
		super(copy);
	}
	@Override
	public int compareTo(FavourSimilar arg0)
	{
		if(simScore == arg0.simScore)
			return Double.compare(rating, arg0.rating);
		if(simScore > arg0.simScore)
			return 1;
		return -1;
	}
}
