package jMusic.components;
import jMusic.Component;
import jMusic.Probe;
import jMusic.Source;


/**
 * a note to frerquency filter. uses 12 notes in octav!! base freq=440Hz
 * (Note)
 */
final class NoteFrequency extends Component implements RA		//jMusic9.java: a bit to music: 8.9.77.sat07:37pm
{
	static double baseFrequency = 440.0;
	static double notesPerOctave = 12;			// ! !
	private int Note=0;
	private int probes = 1;
	NoteFrequency()
	{
		super();
		probe = new Probe[probes]; 
		probe[Note] = new Probe("Note");
	}
	public Source copy()
	{
		return new NoteFrequency();
	}
	//and another constructor with "base freq"
	public double output()
	{
		double v = probe[Note].output();
		return baseFrequency*Math.pow( 2.0 , v / notesPerOctave );
	}
}
