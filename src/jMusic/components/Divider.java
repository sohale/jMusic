package jMusic.components;
import jMusic.Component;
import jMusic.Probe;
import jMusic.Source;


class Divider extends Component implements RA
{
	Divider()
	{
		super();
		probe = new Probe[2]; 
		probe[0] = new Probe("Source0");
		probe[1]	= new Probe("Source1");
	}
	public Source copy()
	{
		return new Divider();
	}
	public double output()
	{
		return probe[0].output() / probe[1].output();
	}
}
