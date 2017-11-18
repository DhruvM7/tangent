package tangent;

public class RatingSort extends DM implements Comparable<RatingSort>
{
	public RatingSort(DM copy)
	{
		super(copy);
	}

	@Override
	public int compareTo(RatingSort arg0)
	{
		return Double.compare(rating, arg0.rating);
	}
}
