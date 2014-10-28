package jMusic;

/**
 * an abstract cliche for distorters
 * (Input)	,...?
 */
abstract class Distortion extends Component implements RA //28.8.77.5shan.01:36XM
{
	protected int Input = 0;
	protected int probes = 1;

	Distortion()
	{
		//super();?!
		probe = new Probe[probes]; 
		probe[Input] = new Probe("Input");
	}
}

/**
 * A Sin distorter, maps [-1,+1] to [-1,+1]. higher values are repeated in this periodic function.
 */
final class Sinous extends Distortion	  //28.8.77.5shan.01:40XM
{
	public double output()
	{
		return Math.sin(probe[Input].output()*2.0*Math.PI*0.25);		//remember this is distortion!
	}
	/**
	 * UU GLY
	 */
	public Source copy()
	{
		return new Sinous();
	}
}
/**
 * An ArcSin distorter, maps [-1,+1] to [-1,+1]. higher values are not acceptable. I think it may generate an Exception?
 */
final class ArcSinous extends Distortion	  //28.8.77.5shan.01:59XM	  //after previous ones tested
{
	public double output()
	{
		return Math.asin(probe[Input].output())/(Math.PI/2.0);
	}
	public Source copy(){return new ArcSinous();}
}
/**
 * the very simple Abs distorter.
 */
final class Absolute extends Distortion		 //8.9.77.sat05:48pm
{
	public double output()
	{
		return Math.abs(probe[Input].output());
	}
	public Source copy(){return new Absolute();}
}
/*		//OBSOLETE
final class SinousN extends Distortion		 //8.9.77.sat05:48pm
{												//niddle
	double power;
	SinousN(double power)
	{
		super();
		this.power = power;
	}
	public double output()
	{
		double x = Math.sin(probe[Input].output()*2.0*Math.PI*0.25);
		double s = +1;
		if(x<0) s = -1;
		x = Math.abs(x);
		return Math.pow(x,power)*s;
	}
}
*/
/*
	//OBSOLETE
final class ISinousN extends Distortion		  //8.9.77.sat06:26-53pm
{												//Anti-niddle
	double power;
	ISinousN(double power)
	{
		super();
		this.power = 1.0/power;
	}
	public double output()
	{
		double x = Math.sin(probe[Input].output()*2.0*Math.PI*0.25);
		double s = +1;
		if(x<0) s = -1;
		x = Math.abs(x);
		return Math.pow(x,power)*s;
	}
}
*/

/**
 * a distorter made by me. I name it, niddle, fuzzyVERY,
 *    Math.pow, floor, down, reductioner, weaker, falloff, zero
 *    opposite of Ceiling
 * out = in ^ power. it keeps the sign as input.
 * constructor(power)
 */
final class Floor extends Distortion	   //8.9.77.sat07:12pm
{												//niddle, fuzzy VERY, Power, Floor
	double power;
	Floor(double power)
	{
		super();
		this.power = power;
	}
	public Source copy(){return new Floor(power);}
	public double output()
	{
		double x = probe[Input].output();
		double s = +1;
		if(x<0) s = -1;
		x = Math.abs(x);
		return Math.pow(x,power)*s;
	}
}

/**
 * a distorter made by me. I name it, Anti-niddle, fuzzyNOTVERY,
 *    Power, ceiling, up, hold-up, keep-on, reductioner, enforcer, alive, one
 *    opposite of Floor
 * out = in ^ 1/apower. it keeps the sign as input.
 * constructor(apower)
 */
final class Ceiling extends Distortion		 //8.9.77.sat07:14pm
{
											//Anti-niddle, fuzzy NOTVERY, ceiling
	//you can build this using Power(1/N) but It is easier to remember	  
	double power;
	Ceiling(double power)
	{
		super();
		this.power = 1.0/power;
	}
	public Source copy(){return new Ceiling(power);}
	public double output()
	{
		double x = probe[Input].output();
		double s = +1;
		if(x<0) s = -1;
		x = Math.abs(x);
		return Math.pow(x,power)*s;
	}
}
/**
 * note:
 *   outside areas are computed using linear exterpolation with computing the linear interplation formula for the nearest start/end regions.
 * an alternative is returning ZERO for those areas
 */
class partialLinear extends Distortion		//3 farv. 1379
{
	double[][] points;
	/**
	 * parameter points must be a sorted (by x) 2xn array of double
	 */
	public partialLinear(double[][] points)
	{
		super();
		this.points = points;
		if(points.length<2) throw new Error("points.length must >= 2");
		for(int i=0;i<points.length;i++)
			if(points[i].length!=2)
				throw new Error("the array must be 2xN");
		for(int i=0;i<points.length-1;i++)
			if(points[i][0] >= points[i+1][0])
				 throw new Error("x's must be both ascending AND non equal");
	}
	public Source copy(){return new partialLinear(points);}
	public double output()
	{
		double x = probe[Input].output();
		int i=0;
		for(i=1;i<points.length-1 && x>points[i][0];i++) ;
		//i may be len-1+1
		i--;
		double y = (x - points[i+0][0])*(points[i+1][1] - points[i+0][1])/(points[i+1][0] - points[i+0][0]) + points[i][1] ;
		//System.out.println("i= "+i+"  "+x+" -> "+y);
		return y;
	}
}
/*
class polynomial extends Distortion		//3 farv. 1379
{
	double[][] p;
	**
	 * parameter points must be 2xn array of double
	 *
	public polinomial(double[][] pp)
	{
		super();
		this.p = pp;
		for(int i=0;i<p.length;i++)
			if(p[i].length!=2)
				throw new Error("the array must be 2xN");
	}
	public double output()
	{
		double x = probe[Input].output();
		double y = 0;
		for(int i=0;i<p.length;i++)
			y = y + Math.pow(x,p[i][0])*p[i][1];
		//i may be len-1+1
		i--;
		double y = (x - points[i+0][0])*(points[i+1][1] - points[i+0][1])/(points[i+1][0] - points[i+0][0]) + points[i][1] ;
		//System.out.println("i= "+i+"  "+x+" -> "+y);
		return y;
	}
}
*/

class Polynomial extends Distortion		//3 farv. 1379
{
	double[] p;
	public Polynomial(double[] pp)
	{
		super();
		p = pp;
	}
	public Source copy(){return new Polynomial(p);}
	public double output()
	{
		double x = probe[Input].output();
		double y = 0;
		double t = 1;
		for(int i=0;i<p.length;i++)
		{
			y = y + t*p[i];
			t = t*x;
			//System.out.print(i+"*"+p[i]+"+");
		}
		//System.out.println("");
		return y;
	}
}

/*
		=	exp(-x)-exp(-2x)
class Pep extends Distortion		//3 farv. 1379
{
	double a,b,c;
	public Pep(double peakdelay, double len)
	{
		super();
		Math.exp(-peakdelay)
	}
	public double output()
	{
		double x = probe[Input].output();
		double y = 0;
		double t = 1;
		for(int i=0;i<p.length;i++)
		{
			y = y + t*p[i];
			t = t*x;
			//System.out.print(i+"*"+p[i]+"+");
		}
		//System.out.println("");
		return y;
	}
}
*/

/**
 * y=x identity!
 * Identity, Same, X, Equality, Equal, Port, Link, Keep, Same, Copy, Mult, ConstantAmp
 */
final class Copy extends Distortion		//3 Farv. 1379
{
	double a;
	Copy(double amp)
	{
		a = amp;
	}
	public Source copy(){return new Copy(a);}
	public double output()
	{
		return probe[Input].output() * a;
	}
}

/**
 * chearful Simple Single Constant Exponential  a*e^(-bx)+c
 * (Input)
 * constructor(a,b)
 */
final class ExponentialSimple extends Distortion
{
	double a,b,c;
	ExponentialSimple(double A, double B, double C)
	{
		super();
		a = A;
		b = B;
		c = C;
	}
	public Source copy(){return new ExponentialSimple(a,b,c);}
	public double output()
	{
		double x = probe[Input].output();
		return Math.exp(-b*x)*a+c;
	}
}

/**
 ...
*/
final class StomperEnvelopeDist__ extends Distortion
{
	/**
	 * constant power version
	 */
	double a/*,s,e*/;
	StomperEnvelopeDist__(/*double from, double to,*/double A)
	{
		super();
		a = A;
		/*s = from;
		e = to;*/
	}
	public Source copy(){return new StomperEnvelopeDist__(a);}
	public double output()
	{
		return 1-Math.pow(1-probe[Input].output(),a);
	}
}

/**
 * aX+c
 * constructor(a,c)
 */
final class Linear extends Distortion
{
	double a,c;
	Linear(double A, double C)
	{
		super();
		a = A;
		c = C;
	}
	public Source copy(){return new Linear(a,c);}
	public double output()
	{
		double x = probe[Input].output();
		return a*x+c;
	}
}

class Power extends Distortion	//exactly equal to class Floor
{
	double power;
	Power(double power)
	{
		super();
		this.power = power;
	}
	public Source copy(){return new Power(power);}
	public double output()
	{
		double x = probe[Input].output();
		double s = +1;
		if(x<0) s = -1;
		x = Math.abs(x);
		return Math.pow(x,power)*s;
	}
}

class Logarithm extends Distortion
{
	double base;
	Logarithm(double base)
	{
		super();
		this.base = base;
	}
	public Source copy(){return new Logarithm(base);}
	public double output()
	{
		double x = probe[Input].output();
		return Math.log(x)/Math.log(base);
	}
}
/**
 * (1-x)/(1+x)
 */
class MOP extends Distortion
{
	MOP()
	{
		super();
	}
	public Source copy(){return new MOP();}
	public double output()
	{
		double x = probe[Input].output();
		return (1.0-x)/(1.0+x);
	}
}

class Fraction extends Distortion
{
	Fraction()
	{
		super();
	}
	public Source copy(){return new Fraction();}
	public double output()
	{
		double x = probe[Input].output();
		return x-((int)x);
	}
}

/**
 * limits the value between [a,b]
 * */
class Limit extends Distortion
{
	double l,h;
	Limit(double a, double b)
	{
		super();
		this.l=a;
		this.h=b;
	}
	public Source copy(){return new Limit(l,h);}
	public double output()
	{
		double x = probe[Input].output();
		if(x<l)x=l;
		if(x>h)x=h;
		return x;
	}
}
