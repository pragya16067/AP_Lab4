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
	
	private static final int FRONT = 1;
	
	public PQueue(int maxsize)
	{
	    this.maxsize = maxsize;
	    this.size = 0;
	    Heap = new Animal[this.maxsize + 1];
	    Heap[0] = new Herbivore(Integer.MAX_VALUE,Integer.MAX_VALUE,Integer.MAX_VALUE,Integer.MAX_VALUE,Integer.MAX_VALUE);
	    
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
	protected Grassland curr;
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
	
	public int[] getNewCoord(int[] dest, int distance, String s) {
		double tan=(dest[1]-this.Position[1]) / (dest[0]-this.Position[0]);
		double cos=Math.sqrt(1/(1+tan*tan));
		double sin=Math.sqrt(1-cos*cos);
		
		int newX1=(int) Math.round(this.Position[0] + (distance*cos));
		int newY1=(int) Math.round(this.Position[1] + (distance*sin));
		int newX2=(int) Math.round(this.Position[0] - (distance*cos));
		int newY2=(int) Math.round(this.Position[1] - (distance*sin));
		int[] ans=new int[2];
		
		if(s.equals("Towards"))
		{
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
		}
		else
		{
			if(this.getDistance(newX1,newY1) > this.getDistance(newX2,newY2))
			{
				ans[0]=newX1;
				ans[1]=newY1;
			}
			else
			{
				ans[0]=newX2;
				ans[1]=newY2;
			}
		}
		
		return ans;
		
	}
	
	public Grassland isInGrassland(Grassland[] g) {
		for(int i=0; i<2; i++)
		{
			if(Position[0] < (g[i].getx()+g[i].getr()) && Position[0]> (g[i].getx()-g[i].getr()) && Position[1] < (g[i].gety()+g[i].getr()) && Position[1]> (g[i].gety()-g[i].getr()))
			{
				return g[i];
			}
		}
		return null;
		
	}
	
	
	public abstract void TakeTurn(Grassland[] grasslands, PQueue p) ;
}


class Herbivore extends Animal {
	protected int maxGrassCap;
	
	
	
	public Herbivore( int x, int y,int tStart, int h, int grass) {
		super(tStart, x, y, h);
		this.maxGrassCap=grass;
	}
	

	
	public void TakeTurn(Grassland[] grasslands, PQueue p) {
		int size=p.getSize();
		curr=isInGrassland(grasslands);
		//To find the neighbouring grassland
		Grassland neighbour=null;
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
			if(prob<=50) //50% chance that it stays at its own position
			{
				///WHAT SHOULD WE RETURN HERE
				return;
			}
			else
			{
				if (curr!=null)//If the herbivore is inside a grassland;
				{
					//Move 5 units towards the neighbouring grassland
					int[] dest={neighbour.getx(),neighbour.gety()};
					int[] newC=getNewCoord(dest, 5, "Towards");
					this.Position[0]=newC[0];
					this.Position[1]=newC[1];
					this.Health=this.Health-25;
					
				}
				else //If the herbivore is not inside any grassland then move to the nearest
				{
					Grassland nearest;
					if(this.getDistance(grasslands[0].getx(), grasslands[0].gety()) < this.getDistance(grasslands[1].getx(), grasslands[1].gety())) 
					{
						nearest=grasslands[0];
					}
					else
					{
						nearest = grasslands[1];
					}
					int[] dest={nearest.getx(),nearest.gety()};
					int[] newCoord = this.getNewCoord(dest, 5, "Towards");
					Position[0]=newCoord[0];
					Position[1]=newCoord[1];
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
					if (prob<=65)
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
						int[] dest={nearest.getx(),nearest.gety()};
						int[] newCoord = this.getNewCoord(dest, 5, "Towards");
						Position[0]=newCoord[0];
						Position[1]=newCoord[1];
					}
					else
					{
						//35% chance that herbivore moves away from nearest carnivore
						Carnivore nearest=null;
						int min=Integer.MAX_VALUE;
						for(int i=0; i<size; i++)
						{
							if(p.get(i) instanceof Carnivore)
							{
								if(this.getDistance(p.get(i).Position[0], p.get(i).Position[1]) < min)
								{
									nearest=(Carnivore) p.get(i);
								}
							}
						}
						int[] newCoord = this.getNewCoord(nearest.Position, 4, "Away");
						Position[0]=newCoord[0];
						Position[1]=newCoord[1];
					}
				}
			}
			
			
			else //The Herbivore is already inside a Grassland
			{
				if(curr.getGrassAvbl() >= this.maxGrassCap)
				{
					int prob=random.nextInt(101);
					if(prob<=90)
					{
						//90% chance that Herbivore stays and eats its full capacity of grass 
						curr.setGAvbl(curr.getGrassAvbl() - this.maxGrassCap);
						//health increases by 50% of original value
						this.Health = this.Health * 3 / 2;
					}
					else
					{
						prob=random.nextInt(101);
						if(prob<=50)
						{
							//50% chance that it moves away from the carnivore
							Carnivore nearest=null;
							int min=Integer.MAX_VALUE;
							for(int i=0; i<size; i++)
							{
								if(p.get(i) instanceof Carnivore)
								{
									if(this.getDistance(p.get(i).Position[0], p.get(i).Position[1]) < min)
									{
										nearest=(Carnivore) p.get(i);
									}
								}
							}
							int[] newCoord = this.getNewCoord(nearest.Position, 2, "Away");
							Position[0]=newCoord[0];
							Position[1]=newCoord[1];
						}
						else
						{
							int[] dest={neighbour.getx(),neighbour.gety()};
							int[] newCoord = this.getNewCoord(dest, 3, "Towards");
							Position[0]=newCoord[0];
							Position[1]=newCoord[1];
						}
					}
				}
				else //Available grass is less than the Herbivore's capacity
				{
					int prob=random.nextInt(101);
					if(prob < 20)
					{
						curr.setGAvbl(0);
						//Increase health by 20% of original
						this.Health = 6 / 5 * this.Health;
					}
					else
					{
						prob=random.nextInt(101);
						if(prob <= 70)
						{//70% chance that Herbivore moves away from nearest carnivore
							Carnivore nearest=null;
							int min=Integer.MAX_VALUE;
							for(int i=0; i<size; i++)
							{
								if(p.get(i) instanceof Carnivore)
								{
									if(this.getDistance(p.get(i).Position[0], p.get(i).Position[1]) < min)
									{
										nearest=(Carnivore) p.get(i);
									}
								}
							}
							int[] newCoord = this.getNewCoord(nearest.Position, 4, "Away");
							Position[0]=newCoord[0];
							Position[1]=newCoord[1];
						}
						else
						{
							int[] dest={neighbour.getx(),neighbour.gety()};
							int[] newCoord = this.getNewCoord(dest, 2, "Towards");
							Position[0]=newCoord[0];
							Position[1]=newCoord[1];
						}
					}
				}
				
				//Code to check if the Herbivore has now moved to a new grassland now, and decrease health if that is the case
				Grassland change=isInGrassland(grasslands);
				if(!change.equals(curr))
				{
					this.Health=this.Health-25;
					curr=change;
				}
			}
			return;
		}
		
	}
}

class Carnivore extends Animal {
	
	
	public Carnivore( int x, int y,int tStart, int h) {
		super(tStart, x, y, h);
	}
	
	public void TakeTurn(Grassland[] grasslands, PQueue p) {
		int size=p.getSize();
		curr=isInGrassland(grasslands);
		//To find the neighbouring grassland
		Grassland neighbour=null;
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
			if(p.get(i) instanceof Herbivore)
			{
				//There is still a Herbivore left
				flag=false;
				break;
			}
		}
		
		if(flag==true) //There is no herbivore left
			return;
		else //There is at least 1 Herbivore left
		{
			//if Herbivore is within 1 unit distance of Carnivore
			int ctr=0;
			for(int i=0; i<size; i++)
			{
				if(p.get(i) instanceof Herbivore)
				{
					if( this.getDistance(p.get(i).Position[0], p.get(i).Position[1]) <= 1 )
					{
						ctr++;
					}
				}
			}
			
			if(ctr==1)
			{
				//there is one herbivore that carnivore will kill and eat
				for(int i=0; i<size; i++)
				{
					if(p.get(i) instanceof Herbivore)
					{
						this.Health = this.Health + 2*p.get(i).Health/3; //Carnivore gets additional 2/3rds of health of herbivore
						p.get(i).Health=-1; //Carnivore has killed the herbivore
					}
				}
			}
			else if(ctr==2)
			{
				//Both herbivores are within the killing radius
				int[] HerbivoreKill=new int[2];
				int k=0;
				for(int i=0; i<size; i++)
				{
					if(p.get(i) instanceof Herbivore)
					{
						HerbivoreKill[k++]=(i);
					}
				}
				
				//Now to check which herbivore is nearest and kill it
				if(this.getDistance(p.get(HerbivoreKill[0]).Position[0], p.get(HerbivoreKill[0]).Position[1]) <  this.getDistance(p.get(HerbivoreKill[1]).Position[0], p.get(HerbivoreKill[1]).Position[1]))
				{
					this.Health = this.Health + 2*p.get(HerbivoreKill[0]).Health/3; //Carnivore gets additional 2/3rds of health of herbivore
					p.get(HerbivoreKill[0]).Health=-1;
				}
				else
				{
					this.Health = this.Health + 2*p.get(HerbivoreKill[1]).Health/3; //Carnivore gets additional 2/3rds of health of herbivore
					p.get(HerbivoreKill[1]).Health=-1;
				}
				
			}
			else
			{
				//No herbivore exists in Killing radius
				if(curr==null) // Carnivore is not inside any grassland
				{
					int prob=random.nextInt(101);
					if(prob<=92)
					{
						Herbivore nearest=null;
						int min=Integer.MAX_VALUE;
						for(int i=0; i<size; i++)
						{
							if(p.get(i) instanceof Herbivore)
							{
								if(this.getDistance(p.get(i).Position[0], p.get(i).Position[1]) < min)
								{
									nearest=(Herbivore) p.get(i);
								}
							}
						}
						int[] newCoord = this.getNewCoord(nearest.Position, 4, "Towards");
						Position[0]=newCoord[0];
						Position[1]=newCoord[1];
						return;
					}
					else
					{
						this.Health=this.Health-60;
						return;
					}
					
				}
				else // If carnivore is inside a grassland
				{
					int prob=random.nextInt(101);
					if(prob <= 25)
					{
						this.Health=this.Health-30;
						return;
					}
					else
					{
						Herbivore nearest=null;
						int min=Integer.MAX_VALUE;
						for(int i=0; i<size; i++)
						{
							if(p.get(i) instanceof Herbivore)
							{
								if(this.getDistance(p.get(i).Position[0], p.get(i).Position[1]) < min)
								{
									nearest=(Herbivore) p.get(i);
								}
							}
						}
						int[] newCoord = this.getNewCoord(nearest.Position, 2, "Towards");
						Position[0]=newCoord[0];
						Position[1]=newCoord[1];
					}
					return;
				}
			}
			return;
		}
		
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
	
	public boolean equals(Grassland g) {
		return (x==g.x && y==g.y && r==g.r && GrassAvbl==g.GrassAvbl);
	}
	
}

//A class World to run the simulation
class World {
	private PQueue Animals;
	private int TotTime;
	private int TotTurns;
	
	public World(int Ttime, int Tturn,PQueue p) {
		TotTime=Ttime;
		TotTurns=Tturn;
		Animals=p;
	}
	
	public void SimulateGame(Grassland[] g) {
		int turns=0;
		int time=0;
		while(!Animals.isEmpty() && turns<TotTime)
		{
			Animal a=Animals.get(0);
			Animals.dequeue();
			a.TakeTurn(g,Animals);
		}
	}
}


public class Lab4 {

	public static void main(String[] args) throws IOException {
		Reader.init(System.in);
		/*System.out.println("Enter Total Final Time for Simulation:");
		int TotalTime=Reader.nextInt();
		System.out.println("Enter x, y centre, radius and Grass Available for First Grassland:");
		Grassland G1=new Grassland(Reader.nextInt(),Reader.nextInt(),Reader.nextInt(),Reader.nextInt());
		System.out.println("Enter x, y centre, radius and Grass Available for Second Grassland:");
		Grassland G2=new Grassland(Reader.nextInt(),Reader.nextInt(),Reader.nextInt(),Reader.nextInt());
		*/
		System.out.println("Enter Health and Grass Capacity for Herbivores:");
		int Hhealth=Reader.nextInt();
		int HG=Reader.nextInt();
		System.out.println("Enter x, y position and timestamp for First Herbivore:");
		Herbivore H1=new Herbivore(Reader.nextInt(), Reader.nextInt(),Reader.nextInt(), Hhealth, HG);
		System.out.println("Enter x, y position and timestamp for Second Herbivore:");
		Herbivore H2=new Herbivore(Reader.nextInt(), Reader.nextInt(),Reader.nextInt(), Hhealth, HG);
		
		System.out.println("Enter Health for Carnivores:");
		int Chealth=Reader.nextInt();
		System.out.println("Enter x, y position and timestamp for First Carnivore:");
		Carnivore C1=new Carnivore(Reader.nextInt(), Reader.nextInt(),Reader.nextInt(), Chealth);
		System.out.println("Enter x, y position and timestamp for Second Carnivore:");
		Carnivore C2=new Carnivore(Reader.nextInt(), Reader.nextInt(),Reader.nextInt(), Chealth);
		
		Animal[] animals={H1,H2,C1,C2};
		//Grassland[] g={G1,G2};
		PQueue p=new PQueue(4);
		p.enqueue(animals[0]);
		//System.out.println(p.get(1).TimeOfStart);
		p.enqueue(animals[1]);
		//System.out.println(p.get(1).TimeOfStart+" "+p.get(2).TimeOfStart);
		p.enqueue(animals[2]);
		//System.out.println(p.get(1).TimeOfStart+" "+p.get(2).TimeOfStart+" "+p.get(3).TimeOfStart);
		p.enqueue(animals[3]);
		//System.out.println(p.get(1).TimeOfStart+" "+p.get(2).TimeOfStart+" "+p.get(3).TimeOfStart+" "+p.get(4).TimeOfStart);
		
		p.dequeue();
		System.out.println(p.get(1).TimeOfStart+" "+p.get(2).TimeOfStart+" "+p.get(3).TimeOfStart);
		p.dequeue();
		System.out.println(p.get(1).TimeOfStart+" "+p.get(2).TimeOfStart);
		p.enqueue(animals[0]);
		System.out.println(p.get(1).TimeOfStart+" "+p.get(2).TimeOfStart+" "+p.get(3).TimeOfStart);
		p.dequeue();
		p.dequeue();
		System.out.println(p.get(1).TimeOfStart);
		
		
		//World W= new World(TotalTime, TotalTime, p);
		
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

