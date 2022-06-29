package jMusic;

/**
A "Compound Component" is a multi-output component
that has multiple inputs (`Probe`s) and multiple outputs.

It creates a (black-box) `module`. (that can be instantiated and cloned).

As in multi-output components (`MultiOutputComponent`), the function `.output(int)` can be used to fetch one output sample.

The constructor should create the necessary `Component`s, wire their inputs and outputs together, and wire the exposed I/O s to `this`.

Outputs:

There is a main output.

* `.output()`
* `.output(port_id)`

Inputs:
As in any componentm there are multiple components:
`c.probe(name).link(s);`
or use the shortcut:
`c.lnk(name,s)`

todo:
creation parameters, (slow) parameters (exposed pararameters), exposed I/O.

*/
public class MOCompound extends MultiOutputComponent
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

	void AddProbe(String name, Probe pr)
	{
		Probe[] olds = probe;
		int n = olds.length;
		probe = new Probe[n+1];
		if(n>0)
			System.arraycopy(olds,0, probe,0, n );
		;
		probe[n] = new Probe(name);
		pr.link(probe[n]);			
	};

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
		//nothing to do in the beginning
		outputNames = new String[0];
	};
}

