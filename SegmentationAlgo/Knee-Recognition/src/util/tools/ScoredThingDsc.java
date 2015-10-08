/**
 * Used as a way to collect a number of objects (each of which is associated with some kind of score) 
 * and then inspect the set sorted by the scores
 * Note: sorts in DESCENDING order
 * 
 * Michael O'Mahony
 * 10/01/2013
 */
package util.tools;

public class ScoredThingDsc implements Comparable<Object>
{
	public double score;
	public int thing;
	public boolean abs;

	public ScoredThingDsc(double s, int t)
	{
		score = s;
		thing = t;
		abs = false;
	}

	public ScoredThingDsc(double s, int t, boolean a)
	{
		score = s;
		thing = t;
		abs = a;
	}
	
	public int compareTo(Object o)
	{
		ScoredThingDsc st = (ScoredThingDsc) o;
		if(abs)
			return (Math.abs(score) > Math.abs(st.score)) ? -1 : +1;
		else
			return (score > st.score) ? -1 : +1;
	}

	public String toString()
	{
		return "[" + score + "; " + thing + "]";
	}
}
