package tangent;

public class DiscardSimilar extends DM implements Comparable<DiscardSimilar>
{
	public DiscardSimilar(DM copy)
	{
		super(copy);
	}

	@Override
	public int compareTo(DiscardSimilar arg0)
	{
		if(simScore == arg0.simScore)
			return Double.compare(rating, arg0.rating);;
		if(simScore < arg0.simScore)
			return 1;
		return -1;
	}
}
