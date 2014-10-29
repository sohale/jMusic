package jMusic.players;
import jMusic.Source;
import jMusic.Timer;

import sun.audio.*;

/*
class RealtimePlayer	//	just now it sticked to my mind
{
	Source source[] = new Source[1];
	RealtimePlayer(Source source)
	{
		this.source[0] = source;
	}

	public void play(double duration)
	{
		System.out.println("Playing while generating");
		Timer FusingTimer = new Timer();
		Timer.reset();
		int i;
		for(i=0;FusingTimer.output()<=duration;i+=1) //bounds
		{
			Timer.tick();
			double sum=0.0;
			int s;
			for(s=0;s<source.length;s++)
			{
				double value = source[s].output();
				sum = sum + value;
			}
			int byteToWrite;
			byteToWrite = (int)((sum*0.9+1.0)/2.0*255.0/source.length);
			if(byteToWrite<0) byteToWrite=0;
			if(byteToWrite>255) byteToWrite=255;
			fileout.write(byteToWrite);
		}
			fileout.close();
			System.out.println("Done Writing to file:"+filename);
		}catch(FileNotFoundException e){
			System.out.println("File could not created");
			return;
		}catch(IOException e){
			System.out.println("File error");
			return;
		}
	};
}
*/

//class BufferedPlayer
//class RecordToBuffer

/**
 * A Java semi-realtime audio player.
 * first "generate(dur,sampleRate)" then "playBuffer(isloop)"
 * bugs: (see Plotter,BufferRecorder,Recorder)
 * 1-it must reset all at first. (it doesnt). it just starts and runsTimer
 * 2-it must consider mutual-exclution for generating. (it doesnt)
 * constructor(source)
 */
class BufferRecorder
{
	Source source[] = new Source[1];
	BufferRecorder(Source source)
	{
		this.source[0] = source;
	}

	byte[] wavbuffer = null;

	public void generate(double duration, double sampleFreq)
	{
		Timer fusingTimer = new Timer(sampleFreq);
		int buflen = (int)(duration * fusingTimer.getSamplingFrequency())+1;
		wavbuffer = new byte[buflen];
		System.out.print("Generarting "+buflen+" samples...");

		Timer.reset_();
		for(int i=0;fusingTimer.output()<duration;i+=1)	//was <=duration and caused exception
		{
			Timer.tick();
			double sum=0.0;
			for(int s=0;s<source.length;s++)
			{
				double value = source[s].output();
				sum = sum + value;
			}
			int byteToWrite;
			byteToWrite = (int)((sum*0.9+1.0)/2.0*255.0/source.length);
			if(byteToWrite<0) byteToWrite=0;
			if(byteToWrite>255) byteToWrite=255;

			byteToWrite = byteToWrite - 127;

			//try
			//{
				wavbuffer[i] = (byte)byteToWrite;
				//if(i>=wavBuffer.length ...
			//}
			//catch(Exception e)
			//{
			//	System.out.println(i);
			//}
		}
		System.out.println("done.");
	}

	public void playBuffer(boolean loop)
	{
		AudioData ad = new AudioData(wavbuffer);
		AudioDataStream ads = new AudioDataStream(ad);
		ContinuousAudioDataStream cads = new ContinuousAudioDataStream(ad);
		AudioPlayer ap = AudioPlayer.player;
		//ap.setPriority(Thread.MAX_PRIORITY);

		if(loop)
			ap.start(cads);
		else
			ap.start(ads);

		/*
		ap.start(cads);	//YES! it can be done!
		ap.stop(cads);
		
		try{Thread.sleep(1000);}catch(Exception e){};

		ap.start(cads);

		try{Thread.sleep(1000);}catch(Exception e){};

		ap.stop(cads);
		*/
		//but dont do it. it bothers

		System.out.print("playing...");
		try
		{
			//ap.wait();		//Monitor Exception!
			ap.join();
			
			//ap wont stop until it reaches cads be ".close()" ed
		}
		catch(InterruptedException e)
		{
			System.out.println(e);
		}
		System.out.println("DONE");
	}
}
