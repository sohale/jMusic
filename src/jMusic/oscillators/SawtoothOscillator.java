package jMusic.oscillators;

import jMusic.Source;


/**
 * a SawTooth oscillator. starts at -1, comes up to +1, then restarts from -1.
 * (([Time=],Frequency,[Phase=0]),[Amplitude=1])
 */
public final class SawtoothOscillator extends Generator
{
	public double output()
	{
		return ( super.phase() * 2.0 - 1.0 ) * probe[Amp].output();
	}
	public Source copy(){return new SawtoothOscillator();}
}