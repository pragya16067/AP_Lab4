//Name : PRAGYA PRAKASH
//Roll No. : 2016067
//Group : 3

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Random;
import java.util.StringTokenizer;

class PQueue {
	
	private Animal[] Heap;
	private int size;
	private int maxsize;
	
	private static final int FRONT = 0;
	
	public PQueue(int maxsize)
	{
	    this.maxsize = maxsize;
	    this.size = 0;
	    Heap = new Animal[this.maxsize + 1];
	    //Heap[0] = new Animal(Integer.MAX_VALUE,Integer.MAX_VALUE,Integer.MAX_VALUE,Integer.MAX_VALUE);
	    
	}
	
	public int getSize(){
		return this.size;
	}
	
	public Animal get(int i) {
		return Heap[i];
	}
	
	public boolean isEmpty() {
		if(size==0)
		{
			return true;
		}
		else
			return false;
	}
	
	public double dist(int[] p) {
		return Math.sqrt(p[0]*p[0] + p[1]*p[1]);
	}

	public void heapify(int i) {
		int heapsize=size;
		boolean flag=true;
		
		while(i<=(heapsize-1)/2 && flag==true)
		{
			int max=i;
			int l=2*i;
			int r=2*i+1;
			if(l<=heapsize && Heap[l].TimeOfStart < Heap[i].TimeOfStart)
			{
				max=l;
			}
			if(r<=heapsize && Heap[r].TimeOfStart < Heap[max].TimeOfStart)
			{
				max=r;
			}
			if(max==i) {
				if(l<=heapsize && Heap[l].Health > Heap[i].Health)
				{
					max=l;
				}
				if(r<=heapsize && Heap[r].Health > Heap[max].Health)
				{
					max=r;
				}
				if(max==i)
				{
					if(l<=heapsize && Heap[l] instanceof Herbivore && Heap[i] instanceof Carnivore)
					{
						max=l;
					}
					if(r<=heapsize && Heap[r] instanceof Herbivore && Heap[max] instanceof Carnivore)
					{
						max=r;
					}
					if(max==i) {
						if(l<=heapsize && Heap[l] instanceof Herbivore && Heap[i] instanceof Herbivore && dist(Heap[l].Position) < dist(Heap[i].Position));
						{
							max=l;
						}
						if(r<=heapsize && Heap[r] instanceof Herbivore && Heap[max] instanceof Herbivore && dist(Heap[r].Position) < dist(Heap[max].Position));
						{
							max=r;
						}
						if(max==i) {
							if(l<=heapsize && Heap[l] instanceof Carnivore && Heap[i] instanceof Carnivore && dist(Heap[l].Position) < dist(Heap[i].Position));
							{
								max=l;
							}
							if(r<=heapsize && Heap[r] instanceof Carnivore && Heap[max] instanceof Carnivore && dist(Heap[r].Position) < dist(Heap[max].Position));
							{
								max=r;
							}
						}
					}
				}
			}
			if(max!=i)
			{
				Animal temp=Heap[max];
				Heap[max]=Heap[i];
				Heap[i]=temp;
				i=max;
				//heap=heapify(heap,min);
			}
			else
			{
				flag=false;
			}
		}
	}
	
    	
	public void enqueue(Animal element)
	    {
	        Heap[++size] = element;
	        //int current = size;
	        //int parent = current / 2;
	 
	        heapify(FRONT);
	        /*while(Heap[current].TimeOfStart > Heap[parent].TimeOfStart)
	        {
	            swap(current,parent);
	            current = parent;
	            parent = current / 2;
	        }*/
	    }
	
	public void Build_Heap()
    {
        for (int pos = (size / 2); pos >= 1; pos--)
        {
            heapify(pos);
        }
    }
 
    public void dequeue()
    {
        //Teacher popped = Heap[FRONT];
    	if(size==1)
    	{
    		//Heap[FRONT]=new Teacher(0,0,0);
    		size--;
    	}
    	else
    	{
    		Heap[FRONT] = Heap[size];
    		//Heap[size] = new Teacher(0,0,0);
    		size--;
    		heapify(FRONT);
    	}
        return;
    }

}


abstract class Animal {
	protected int TimeOfStart;
	protected int Position[]=new int[2];
	protected int Health;
	public Random random=new Random();
	
	public Animal(int tStart, int x, int y, int h)
	{
		TimeOfStart = tStart;
		Position[0]=x;
		Position[1]=y;
		Health=h;
	}
	
	public double getDistance(int x, int y){
		int xd=(this.Position[0]-x);
		int yd=(this.Position[1]-y);
		return Math.sqrt((xd*xd)+(yd*yd));
	}
	
	public int[] getNewCoord(int[] dest, int distance) {
		double tan=(dest[1]-this.Position[1]) / (dest[0]-this.Position[0]);
		double cos=Math.sqrt(1/(1+tan*tan));
		double sin=Math.sqrt(1-cos*cos);
		
		int newX1=(int) Math.round(this.Position[0] + (distance*cos));
		int newY1=(int) Math.round(this.Position[1] + (distance*sin));
		int newX2=(int) Math.round(this.Position[0] - (distance*cos));
		int newY2=(int) Math.round(this.Position[1] - (distance*sin));
		int[] ans=new int[2];
		
		if(this.getDistance(newX1,newY1) < this.getDistance(newX2,newY2))
		{
			ans[0]=newX1;
			ans[1]=newY1;
		}
		else
		{
			ans[0]=newX2;
			ans[1]=newY2;
		}
		
		return ans;
		
	}
	
	public abstract void TakeTurn(Grassland[] grasslands, PQueue p) ;
}


class Herbivore extends Animal {
	protected int maxGrassCap;
	protected Grassland curr;
	
	
	public Herbivore( int x, int y,int tStart, int h, int grass) {
		super(tStart, x, y, h);
		this.maxGrassCap=grass;
	}
	
	
	public Grassland isInGrassland(Grassland[] g) {
		for(int i=0; i<2; i++)
		{
			if(Position[0] < g[i].getx() && Position[0]> (-1)*g[i].getx() && Position[1] < g[i].gety() && Position[1]> (-1)*g[i].gety())
			{
				return g[i];
			}
		}
		return null;
		
	}
	
	
	public void TakeTurn(Grassland[] grasslands, PQueue p) {
		int size=p.getSize();
		curr=isInGrassland(grasslands);
		//To find the neighbouring grassland
		Grassland neighbour;
		for(int i=0;i<2; i++)
		{
			if(!grasslands[i].equals(curr))
			{
				neighbour=grasslands[i];
			}
		}
		
		Boolean flag=true;
		for(int i=0; i<size; i++)
		{
			if(p.get(i) instanceof Carnivore)
			{
				//There is still a carnivore left
				flag=false;
				break;
			}
		}
		
		if(flag==true) //There is no carnivore left
		{
			
			int prob=random.nextInt(101);
			if(prob<=50) //move to neighbouring grassland with 50% chance
			{
				if(curr!=null)//if the herbivore is in a grassland
				{
					this.Health=this.Health-25;
				}
				curr=neighbour;
				this.eat();
			}
			else
			{
				if (curr!=null)//If the herbivore is inside a grassland;
				{
					this.eat();
				}
			}
			return;
		}
		
	
	
		//If there is at least 1 carnivore left
		else
		{
			if(curr==null) //Herbivore is not inside any grassland
			{
				int prob= random.nextInt(101);
				if(prob<=5) //5% chance that herbivore will stay at its position
				{
					return;
				}
				else
				{
					prob = random.nextInt(101);
					if (prob<=65)//should i generate another random here???
					{
						//65% chance that herbivore moves towards nearest grassland
						Grassland nearest;
						if(this.getDistance(grasslands[0].getx(), grasslands[0].gety()) < this.getDistance(grasslands[1].getx(), grasslands[1].gety())) 
						{
							nearest=grasslands[0];
						}
						else
						{
							nearest = grasslands[1];
						}
						int[] newCoord=
					}
					else
					{
						//35% chance that herbivore moves away from nearest carnivore
						
					}
				}
			}
		}
		
	}
}

class Carnivore extends Animal {
	
	public Carnivore( int x, int y,int tStart, int h) {
		super(tStart, x, y, h);
	}
	
	public void TakeTurn() {
		return;
	}
	
}

class Grassland {
	private int x;
	private int y;
	private int r;
	private int GrassAvbl;
	
	public Grassland(int x, int y, int r, int GrassAvbl) {
		this.x=x;
		this.y=y;
		this.r=r;
		this.GrassAvbl=GrassAvbl;
	}
	
	public void setGAvbl(int n) {
		this.GrassAvbl=n;
		return;
	}
	
	public int getGrassAvbl() {
		return this.GrassAvbl;
	}
	
	public int getx() {
		return this.x;
	}
	
	public int getr() {
		return this.r;
	}
	
	public int gety() {
		return this.y;
	}
	
}

//A class World to run the simulation
class World {
	private PQueue Animals;
	private int TotTime;
	private int TotTurns;
	
	public World(int Ttime, int Tturn) {
		TotTime=Ttime;
		TotTurns=Tturn;
	}
	
	public void SimulateGame() {
		int turns=0;
		int time=0;
		while(!Animals.isEmpty() && turns<TotTime)
		{
			Animal a=Animals.get(0);
			Animals.dequeue();
			a.TakeTurn();
		}
	}
}


public class Lab4 {

	public static void main(String[] args) throws IOException {
		Reader.init(System.in);
		System.out.println("Enter Total Final Time for Simulation:");
		int TotalTime=Reader.nextInt();
		System.out.println("Enter x, y centre, radius and Grass Available for First Grassland:");
		Grassland G1=new Grassland(Reader.nextInt(),Reader.nextInt(),Reader.nextInt(),Reader.nextInt());
		System.out.println("Enter x, y centre, radius and Grass Available for Second Grassland:");
		Grassland G2=new Grassland(Reader.nextInt(),Reader.nextInt(),Reader.nextInt(),Reader.nextInt());
		
		System.out.println("Enter Health and Grass Capacity for Herbivores:");
		int Hhealth=Reader.nextInt();
		int HG=Reader.nextInt();
		System.out.println("Enter x, y position and timestamp for First Herbivore:");
		Herbivore H1=new Herbivore(Reader.nextInt(), Reader.nextInt(),Reader.nextInt(), Hhealth, HG);
		System.out.println("Enter x, y position and timestamp for Second Herbivore:");
		Herbivore H2=new Herbivore(Reader.nextInt(), Reader.nextInt(),Reader.nextInt(), Hhealth, HG);
		
		System.out.println("Enter Health and Grass Capacity for Carnivores:");
		int Chealth=Reader.nextInt();
		System.out.println("Enter x, y position and timestamp for First Carnivore:");
		Carnivore C1=new Carnivore(Reader.nextInt(), Reader.nextInt(),Reader.nextInt(), Chealth);
		System.out.println("Enter x, y position and timestamp for Second Carnivore:");
		Carnivore C2=new Carnivore(Reader.nextInt(), Reader.nextInt(),Reader.nextInt(), Chealth);
		
		
		System.out.println("The simulation begins");
	}
}


/** Class for buffered reading int and double values */
class Reader {
    static BufferedReader reader;
    static StringTokenizer tokenizer;

    /** call this method to initialize reader for InputStream */
    static void init(InputStream input) {
        reader = new BufferedReader(
                     new InputStreamReader(input) );
        tokenizer = new StringTokenizer("");
    }

    /** get next word */
    static String next() throws IOException {
        while ( ! tokenizer.hasMoreTokens() ) {
            //TODO add check for eof if necessary
            tokenizer = new StringTokenizer(
                   reader.readLine() );
        }
        return tokenizer.nextToken();
    }

    static int nextInt() throws IOException {
        return Integer.parseInt( next() );
    }
    
    static long nextLong() throws IOException {
        return Long.parseLong( next() );
    }
	
    static double nextDouble() throws IOException {
        return Double.parseDouble( next() );
    }
}

