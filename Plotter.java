package jMusic;

import java.awt.*;
import java.io.*;

/**
 * A Java (realtime?) wave monitor.
 * first "new", then addSource(src),start to show, then stop to disapear.
 * bugs: (see Plotter,BufferRecorder,Recorder)
 * 1-it must reset all at first. (it doesnt). it just starts and runsTimer
 * 2-it must consider mutual-exclution for generating. (it doesnt)
 * 3-it shows only the start of wave. It cant even scroll.
 * 4-it may be better to use a buffer for avoiding re-calculation? or, it does not consider type: RA/Se/St
 * constructor(source)
 * constructor(source,scale)
 */

final class Plotter extends Frame
{
	Source source[] = new Source[1];
	Color plotColor[] = {Color.white,Color.red,Color.blue,Color.yellow,Color.green,Color.pink,Color.cyan,Color.gray,Color.gray,Color.orange,Color.magenta};
	double scale = 1.0;	 //jMusic7.java:7.9.77fri7:16
	Plotter(Source source)
	{
		super("jMusic: Plotter");
		this.source[0] = source;
		start();
	}
	Plotter(Source source,double scale)	 //jMusic7.java:7.9.77fri7:16
	{
		super("jMusic: Plotter "+"scale="+scale);
		this.source[0] = source;
		this.scale = scale;
		start();
	}
	void addSource(Source newSrc)
	{
		System.out.println("}:3(");
		Source newsource[] = new Source[source.length+1];
		System.arraycopy(source,0, newsource,0, source.length);
		source = newsource;
		newsource = null;
		source[source.length-1] = newSrc;
		repaint();
	}
	void start()
	{
		this.addWindowListener(
			new java.awt.event.WindowListener()
			{
				public void windowClosing(java.awt.event.WindowEvent e){};
				public void windowOpenning(java.awt.event.WindowEvent e){};
				public void windowOpened(java.awt.event.WindowEvent e){};
				public void windowClosed(java.awt.event.WindowEvent e){stop();};
				public void windowDeiconified(java.awt.event.WindowEvent e){}
				public void windowActivated(java.awt.event.WindowEvent e){}
				public void windowInconified(java.awt.event.WindowEvent e){}
				public void windowDeactivated(java.awt.event.WindowEvent e){}
				public void windowIconified(java.awt.event.WindowEvent e){}
			}
		);
		setBackground(Color.black);
		setSize(320,200);
		show();
	}
	void stop()
	{
		setVisible(false);
		source = null;
		plotColor = null;
		dispose();
	}
	/*
	public boolean handleEvent(Event evtObj)
	{
		if(evtObj.id == Event.WINDOW_DESTROY)
		{
			stop();
			return true;
		}
		return super.handleEvent(evtObj);
	}
	*/
	int Y(Rectangle rect, double value)
	{
		return (int)( (0.5 - value/2.0) * rect.height );
	}
	public void paint(Graphics g)
	{
		Rectangle rect = g.getClipBounds();	//g.getClipRect();
		System.out.println(""+scale);
		Timer.reset_();
		int x=0;
		int i;
		//for(i=0;x<rect.width;i+=1,x=(int)((i+1)*scale)) //bounds
		for(x=i=0;x<rect.width;x=(int)((i+=1)*scale))
		{
			Timer.tick();
			//Timer.tick();
			int s;
			for(s=0;s<source.length;s++)
			{
				g.setColor(plotColor[s]);
				double value = source[s].output();

				if(s==0)
					g.drawLine(x, Y(rect, 1.0) ,x,Y(rect, value));
				else
					g.drawOval( x, Y(rect, value), 1,1);
			}
			g.setColor(Color.gray);
			g.drawLine(x, Y(rect, 0.0) ,x-2,Y(rect, 0.0));
		}
		//setTitle(""+rect.width /* *Timer.output()*/ );
	};
}

/**
 * A raw/pcm wave recorder. (in a real file).
 * first "new", then doRecord(filename,duration)
 * bugs: (see Plotter,BufferRecorder,Recorder)
 * 1-it must reset all at first. (it doesnt). it just starts and runsTimer
 * 2-it must consider mutual-exclution for generating. (it doesnt)
 * constructor(source)
 */
class Recorder
{
	Source source[] = new Source[1];
	Recorder(Source source)
	{
		this.source[0] = source;
	}
//	  Recorder(Source source_[])
//	  {
//		  this.source = new Source[source_.length];
//		  System.arraycopy(this.source,0, source_,0, source.length);
//	  }
	//Recorder(Plotter plotter)
/*	  
	void addSource(Source newSrc, String filename)
	{
		Source newsource[] = new Source[source.length+1];
		System.arraycopy(source,0, newsource,0, source.length);
		source = newsource;
		newsource = null;
		source[source.length-1] = newSrc;
	}
	void stop()
	{
		//source = null;
		//dispose();
	}
*/
	int LVLS=255;
	// 8 bit
	public void doRecord(String filename,double duration)
	{
		LVLS = 255;
		record(filename,duration,false);
	}
	public void doRecord16(String filename,double duration)
	{
		LVLS = 65535;
		record(filename,duration,true);
	}
	private void record(String filename,double duration, boolean bit16)
	{
		
		//final int LVLS = 65535;
		try{
			System.out.println("Trying to open file:"+filename);
			BufferedOutputStream fileout	= new BufferedOutputStream(new FileOutputStream(filename));
			//Timer FusingTimer = new Timer(Timer.sampleFrequency()); //here the real 22050.0 //was 1.0/0.0100
			Timer FusingTimer = new Timer();
			Timer.reset_();
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
				byteToWrite = (int)(/*0.5+*/(sum*/*0.9*/1.0+1.0)/2.0*((double)LVLS)/source.length);
				if(byteToWrite<0) byteToWrite=0;
				if(byteToWrite>LVLS) byteToWrite=LVLS;

				//fileout.write(byteToWrite);
				
				//if(i%1000==0)System.out.println(source[0].output()/*sum*/);
	
				fileout.write(byteToWrite % 256);
				if(bit16)
					fileout.write(byteToWrite / 256);
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
