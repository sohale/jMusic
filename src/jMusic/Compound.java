package jMusic;

/**
 * This second will be best in new year holidays or perhaps last few months.
 * 05:22 XM 5 Friday (Saturday)
 * EID
 * FARVARDIN 1379
 * 1379 is a great year.
 * great start. Success smiles to me!
 * 
 * changed the design to probe("Source") at 10:44pm 15 farvardin
 */
public class Compound extends Component
{
	/**
	 * source for output end point. it's actually an end point.
	 */
	Compound()
	{
		super();
		probe = new Probe[1];
		probe[0] = new Probe("Source");
	};
	Compound(Source outport)
	{
		this();
		probe[0].link(outport);
	};

	final public Source copy()
	{
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

		//This single statement really made me emotional:
		pr.link(probe[n]);
	};
	public double output()
	{
		return probe[0].output();
	}
}

