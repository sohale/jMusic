package jMusic.oscillators;

import jMusic.Source;


public final class Pulse extends Generator
{
	public double output()
	{
		return ( (super.phase() >=0.5) ? 0.999:-0.999 ) * probe[Amp].output();
	}
	public Source copy(){return new Pulse();}
}
