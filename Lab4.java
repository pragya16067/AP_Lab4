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
	
	public abstract void TakeTurn(Grassland[] grasslands, PQueue p) ;
}

class Herbivore extends Animal {
	protected int maxGrassCap;
	protected Grassland curr;
	
	public Herbivore( int x, int y,int tStart, int h, int grass) {
		super(tStart, x, y, h);
		this.maxGrassCap=grass;
		curr=null;
	}
	
	public void TakeTurn(Grassland[] grasslands, Pqueue p) {
		int size=PQueue.size();
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
		while(!Pqueue.isEmpty() && turns<TotTime)
		{
			Animal a=Pqueue.dequeue();
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

