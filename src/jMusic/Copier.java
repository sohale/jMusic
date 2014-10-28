/**
 * 13 farvardin 1379
 */
package jMusic;
public class Copier
{
	java.util.Vector MARKED = new java.util.Vector(100);
	class Pair
	{
		Source original;
		Source copy;
		Pair(Source a, Source c)
		{
			original=a;
			copy=c;
		}
		
	}
	Component marked(Source c)
	{
		for(int i=0;i<MARKED.size();i++)
		{
			Pair p = (Pair)(MARKED.elementAt(i));
			if(p.original == c)
				return (Component)(p.copy);
		}
		return null;
	}
	void mark(Source c, Source cop)
	{
		MARKED.addElement(new Pair(c,cop));
		System.out.println(c +" -> "+ cop);
	}

	public Source traceS(Source c)
	{
		if(marked(c)!=null)
			return null;
		if(c instanceof Component)
			return traceC((Component)c);
		else if(c instanceof Probe)
			//return traceC(c);
			//return traceP((Probe)c);	//bypass consequent probes
			return traceP((Probe)c);
		else
			return c.copy();
	}
	private Component traceC(Component c)
	{
		//if(marked(c)!=null)
		//	return;
		Component c_ = (Component)(c.copy());
		mark(c,c_);
		for(int i=0;i<c.getAllProbes().length;i++)
		{
			Probe e = c.getAllProbes()[i];
			Source c3 = traceE(e);
			c_.lnk( e.name, c3);
		}
		return c_;
	}
	private Source traceE(Probe p)
	{
//		if(marked(p)!=null)	//if already marked.
//			return null;
//		mark(p,null);

		Source c = p.source;
		if(marked(c)==null)
			traceS(c);
		//in return,
		return marked(c);
	}
	/*
	//bypass consequent Probe
	private Source traceP(Probe p)
	{
		if(p.source instanceof Probe)
			return traceP(p.source);
		//return p.source;
		return traceS(p.source);????????
	}
	*/
	private Source traceP(Probe p)
	{
		if(p.source instanceof Probe)
		{
			Probe q = new Probe(p.name);
			q.link(traceS(p.source));
			return q;
		}
		return traceS(p.source);
	}
}

