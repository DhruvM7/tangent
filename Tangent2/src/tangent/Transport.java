package tangent;

import java.util.HashSet;
import java.util.PriorityQueue;

public class Transport
{
	static DM inputMovie = null;
	static HashSet<String> userids=new HashSet<>();
	static HashSet<String> inputGenres=new HashSet<>(); 
	
	static PriorityQueue<FavourSimilar> keepSimilar = new PriorityQueue<>();
	static PriorityQueue<DiscardSimilar> discardSimilar = new PriorityQueue<>();
	static PriorityQueue<RatingSort> ratingSort = new PriorityQueue<>();
}
