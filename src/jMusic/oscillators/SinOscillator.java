package jMusic.oscillators;

import jMusic.Source;


/**
 * a Sin oscillator
 * (([Time=],Frequency,[Phase=0]),[Amplitude=1])
 */
final class SinOscillator extends Generator
{
	public double output()
	{
		return Math.sin( super.phase() * Math.PI*2.0) * probe[Amp].output();
	}
	//what a long evolution trip! see Jmusic0 and compare to this!
	public Source copy(){return new SinOscillator();}
}