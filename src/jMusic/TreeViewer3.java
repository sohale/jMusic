package jMusic;
import java.util.Vector;
import java.awt.*;

class TreeViewer3 extends Frame

{
	final boolean expandCompound = true;
	final String v = "|";//"\u00ba";	//was"|"
	Vector expanded = new Vector();
	Component source;
	public TreeViewer3(Component src)
	{
		source=src;
		setSize(300,400);
		show();
	}
	public void paint(Graphics gr)
	{
		nc=0;
		gr.setColor(Color.black);
		trace(source,"",gr);
	}
	int nc=0;
	void print(String x,Graphics g)
	{
		int w=20;int h=20;
		int cols=5;
		g.setColor(new Color((int)(Math.random()*255),(int)(Math.random()*255),(int)(Math.random()*255)));
		g.fillOval(30+(nc%cols)*w,30+(nc/cols)*h, w,h);
		nc++;
		System.out.println(x);
	}

	void trace(Component c, String prefix,Graphics gr)
	{
		if(expanded.contains(c))
		{
			print(									prefix+"("+c+")",gr);
			return;
		}
		print(										prefix+""+c,gr);
		expanded.addElement(c);
		String pr0 = prefix;
		//if( c instanceof Compound ) prefix = prefix + "["+" "; else
		if( c instanceof Compound ) prefix = prefix + "[";
		
		prefix = prefix + v+" ";

		if(expandCompound)
			if( c instanceof Compound )
			{
				Compound cc = (Compound)c;
				//out.println(							prefix+" +");
				traceSource(/*cc.source*/cc.probe[0].source,							prefix+" (",gr);
			}


		Probe[] p = c.getAllProbes();
		for(int i=0;i<p.length;i++)
		if(!p[i].defaultSource)
		{
			String u = /*"---"+*/ "<"+p[i].name+">";
			print(									prefix +u+" =",gr);
			Source s = p[i].source;
			String space = "                                                                                                                        ";
			traceSource(s,										prefix+ space.substring(0,u.length())+" ",gr);
		}
		
		print(
		/*"---------------------------------------------------------------------------------------------------"
		.substring(0,pr0.length())*/
			pr0+".------------",gr);
	}
	void traceSource(Source s, String prefix,Graphics gr)
	{
		if( s instanceof Probe )
		{
			traceSource(((Probe)s).source,						prefix+"->",gr);
		}
		else if( s instanceof TemporaryOutput )
		{
			TemporaryOutput t = (TemporaryOutput)s;
			print(	prefix+t.name()+" from "+t,gr);	//multi output
			//expands only if main output...
			if(!expanded.contains(t.master))
				trace((t).master,								prefix,gr);
		}
		else if( s instanceof Component )
		{
			//out.println();
			trace((Component)s,									prefix+"",gr); //or " "
		}
		else
			print(	prefix+""+s,gr);
	}
}
