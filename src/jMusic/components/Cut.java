package jMusic.components;
import jMusic.Component;
import jMusic.Probe;
import jMusic.Source;


/**
 * a Cut distorter, fixes output between [-1,+1] (a Component)
 * (Input)
 */
public final class Cut extends Component implements RA	  //later: lbound,hbound
{
	private int Inp=0;
	private int probes = 1;
	Cut()
	{
		super();
		probe = new Probe[probes]; 

		probe[Inp] = new Probe("Input");
	}
	public Source copy()
	{
		return new Cut();
	}
	public double output()
	{
		double v = probe[Inp].output();
		if (v > +1.0) v = +1.0;
		if (v < -1.0) v = -1.0;
		return v;
	}
}