package jMusic;
/**
 * Select,Switch,Morph,"Switcher",Multiplex
 * (Source0,Source1, ... ,Select)
 */
class Selector extends Component implements RA
{
	/*
		[0] Source0
		[n]	Select
	 */
	int sources;
	Selector(int probes)
	{
		super();
		sources=probes;
		probe = new Probe[sources+1];

		int i;
		for(i=0;i<sources;i++)
			probe[i] = new Probe("Source"+i);
		probe[i] = new Probe("Select");
	}
	public Source copy()
	{
		return new Selector(sources);
	}
	public double output()
	{
		double selector = probe[probe.length-1].output();
		int i1 = (int)selector;
		int i2 = i1+1;if(i2>=sources) i2=0;

		double blend = selector-i1;

		return probe[i1].output() * (1-blend) + probe[i2].output() * (0+blend);
	}
}
