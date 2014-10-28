package jMusic;
/**
 * (Source,[Time])
 * a branch point.
 * static version
 *
 * (Source,[Time])
 */
class CacheOutput extends Component
{
	double cache;
	double cacheTime=-1;

	private int Src=0;
	private int Tim=1;
	CacheOutput()
	{
		super();
		probe = new Probe[2];
		probe[Src] = new Probe("Source");
		probe[Tim] = new Probe("Time");
		probe[Tim].linkD(new Timer());
	}
	public double output()
	{
		double t = probe[Tim].output();
		if(cacheTime != t)
		{
		   cacheTime = t;
		   cache = probe[Src].output();
		}
		return cache;
	}
	public Source copy()
	{
		return new CacheOutput();
	}
}