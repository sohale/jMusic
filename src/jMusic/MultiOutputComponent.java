package jMusic;
	
final class TemporaryOutput implements Source
{
	MultiOutputComponent master;
	int functionIndex;
	TemporaryOutput(MultiOutputComponent c, int fIndex)
	{
		master = c;
		functionIndex = fIndex;
	}
	final public Source copy(){return null;}
	public String name()
	{
		return master.probe[functionIndex].name;
	}
	public double output()
	{
		return master.output(functionIndex);	//really SHAME!
	}
}

public abstract class MultiOutputComponent extends Component
{
	protected String outputNames[];
	public Source outputHandle(String name)
	{
		for(int i=0;i<outputNames.length;i++)
			if(outputNames[i].equals(name))
				return new TemporaryOutput(this, i);
		throw new Error("output named \""+name+"\" not found.");
	}
	//? any usage?
	public boolean outputExists(String name)
	{
		for(int i=0;i<outputNames.length;i++)
			if(outputNames[i].equals(name))
				return true;
		return false;
	}
	public int outputIndex(String name)
	{
		for(int i=0;i<outputNames.length;i++)
			if(outputNames[i].equals(name))
				return i;
		throw new Error("No such output: "+name);
	}
	abstract double output(int port);

	abstract void setOutputNames();	//FORCE!
	
	//So Component MUST have only one constructor: "Component()"
	MultiOutputComponent()
	{
		super();	//?
		setOutputNames();
	}
}
/*
	this is a weakness of OO or java.
	the long way from ADSR.output -> TemporaryOutput -> X.output ...
	TemporaryOutput is really a SHAME!
	(we need to do this just because of uniformity of function calls
	we need to convert "output()" to "output(3)"	)
*/
/*
	usage:
		...
		MultiOutputComponent X = ...;
		ADSR.probe("Time").link((X.outputHandle("NoteTime"));	//or getOutputHandle
		...

/*
//a set of Probes that has some output probes and a single output(?)

abstract class MultiOutputComponent extends Component
{
	protected Probe outputs[];
	public Probe outProbe(String name)
	{
		for(int i=0;i<outputs.length;i++)
		{
			if(outputs[i].name.equals(name))
				return outputs[i];
		}
		throw new Error("output Probe \""+name+"\" not found.");
	}
	public Source outputHandle(String name)
	{
		return new TemporaryOutput(this, i);
	}
	public double output(int port);
	/ *
	{
		switch(port)
		{
			case 0:
				return ...;
			case 1:
				return ...;
			default:
				throw new Error("Port o avazy gerefty baba.");
		}
	}
	* /
}
/ *
   protected //??
   Probe probe[];


   public 
   Probe probe(String name)         //this set must have an item locator
   {
      int i;
      for(i=0;i<probe.length;i++)
      {
         //System.out.println(probe[i].getClass().getSuperclass().getName());
         if(probe[i].name.equals(name))
            return probe[i];     //returns the first
      }
      throw new Error("Probe \""+name+"\" not found.");
      //return null;
   }
}
*/