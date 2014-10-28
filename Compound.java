package jMusic;

/**
 * This second will be best in my Noruz or perhaps last few months
 * 05:22 XM 5 Friday (Saturday)
 * EID
 * FARVARDIN 1379
 * 1379 is a great year.
 * great start. I am a hero. alien success smiles to me!
 * I am marching
 * 
 * changed to probe("Source") at 10:44pm 15 farvardin
 */
public class Compound extends Component
{
	/**
	 * source for output end. it's actually an end
	 */
	//Source source;

	Compound()
	{
		super();
		probe = new Probe[1];
		probe[0] = new Probe("Source");
	};
	Compound(Source outport)
	{
		this();
		//source = outport;
		probe[0].link(outport);
	};
	/*private Compound(Compound orig)
	{
		probe = new Probe[orig.probe.length];
		source = null;
	}
	final public Source copy(){return new Compound(this);}
	*/

	final public Source copy()
	{
		//Compound cc = 
		//return source.copy();
		return new Compound(null);
	}

	void AddProbe(String name, Probe pr)
	{
		Probe[] olds = probe;
		int n = olds.length;
		probe = new Probe[n+1];
		if(n>0)
			System.arraycopy(olds,0,probe,0,olds.length);
		;
		probe[n] = new Probe(name);
		//probe[n].link(pr);
		pr.link(probe[n]);			//This single statement really made me cry. I did Heghghgh
	};
	public double output()
	{
		//return source.output();
		return probe[0].output();
	}
}

/**
 * not tested yet
 */
class MOCompound extends MultiOutputComponent
{
	Source[] source;
	Source mainSource;

	MOCompound(Source s0)
	{
		super();
		probe = new Probe[0];
		mainSource = s0;
		source = new Source[0];
	}
	final public Source copy(){return null;}
	//final public Source copy(){return source.copy();}
	void AddProbe(String name, Probe pr)
	{
		Probe[] olds = probe;
		int n = olds.length;
		probe = new Probe[n+1];
		if(n>0)
			System.arraycopy(olds,0, probe,0, n );
		;
		probe[n] = new Probe(name);
		//probe[n].link(pr);
		pr.link(probe[n]);			//This single statement really made me cry. I did Heghghgh
	};
	//outputNames[]
	void AddOutput(String name, Source s)
	{
		int n;
		{
			String[] olds = outputNames;
			n = olds.length;
			outputNames = new String[n+1];
			if(n>0)
				System.arraycopy(olds,0, outputNames,0, n );
			;
			outputNames[n] = name;
		}

		{
			Source[] olds = source;
			if(n != source.length) throw new Error();
			source = new Source[n+1];
			if(n>0)
				System.arraycopy(olds,0, source,0, n );
			;
			source[n] = s;
		}
	}

	double output(int port)
	{
		return source[port].output();
	};
	public double output()
	{
		return mainSource.output();
	};

	void setOutputNames()
	{
		//nothing to do initially
		outputNames = new String[0];
	};
}

/*
 
 * *  N O - M O R E  * *

 * PseudoProbe or CompoundProbe or AliasProbe or ParamProbe or
 * or TempProbe
 * PProbe CProbe AProbe
 * SimpleProbe
 * 

class PProbe extends Probe
{
	void link(Probe p)	//new mid. link
	{
	}
	void link(Source s)
	{
	}
}
 */
