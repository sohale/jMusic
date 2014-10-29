//3 shahr 1379 friday
package jMusic;
import jMusic.components.ADSR;
import jMusic.components.Constant;
import jMusic.oscillators.SawtoothOscillator;
import jMusic.filters.ButterworthLowpass;

class JMusicMain
{
	public static void main(String[] cl) throws Exception
	{
		Source timer = new Timer(44100); //not used, but affects the static (not elegant).
		Component saw = new SawtoothOscillator();
		saw.lnk("Frequency",new Constant(440*Math.pow(2,3/12.0-1)));
		ADSR adsr=new ADSR();
		saw.lnk("Amplitude",adsr);

		adsr.lnk("Rise",new Constant(0));
		adsr.lnk("Amplitude",new Constant(0.2));
		adsr.lnk("Duration",new Constant(0.1));
		adsr.lnk("Decay",new Constant(0.1));

		Component filter1 = new ButterworthLowpass();
		filter1.lnk("fc",new Constant(7796.0));
		filter1.lnk("Input",saw);

		//System.out.println(filter1.listOfNames());
		//System.in.read();

		Comb c=new Comb(0.5);
		c.lnk("INPUT",filter1);
		c.lnk("REVERB TIME",new Constant(5));
		c.lnk("LOOP TIME",new Constant(0.2));

		Component out = c;
		new Recorder(out).doRecord16("C:\\OUT.raw",1);
	}
}
