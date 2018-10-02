import java.io.*;
import java.util.*;
import java.lang.*;
import java.math.*;
import java.text.*;
import java.awt.*;
//-----------------------
//AP Lab-4
//Name: Arushi Chauhan
//Roll no: 2016019
//-----------------------
class Reader {
    static BufferedReader reader;
    static StringTokenizer tokenizer;

    /** call this method to initialize reader for InputStream */
    static void init(InputStream input) {
        reader = new BufferedReader(new InputStreamReader(input));
        tokenizer = new StringTokenizer("");
    }
    static String next() throws IOException {
        while ( ! tokenizer.hasMoreTokens() ) {
            tokenizer = new StringTokenizer(
                   reader.readLine() );
        }
        return tokenizer.nextToken();
    }

    static int nextInt() throws IOException {
        return Integer.parseInt( next() );
    }
	
    static double nextDouble() throws IOException {
        return Double.parseDouble( next() );
    }
    static long nextLong() throws IOException{
        return Long.parseLong(next());
    }
}
abstract class animal implements Comparator<animal>{
    protected int timestamp;
    protected double health;
    protected Point loc;
    protected char status;
    public animal(Point loc,int timestamp,double health){
        this.timestamp=timestamp;
        this.health=health;
        this.loc=loc;
        if(health>0)this.status='a';
        else this.status='d';
    }
    @Override
    public int compare(animal a1,animal a2){
        if (a1.get_time()!=a2.get_time()){
            return a2.get_time()-a1.get_time();
            //lower timestamp-greater
        }
        else if (a1.get_health()!=a2.get_health()){
            return (int)(a1.get_health()-a2.get_health());
        }
        else if (!(String.valueOf(a1.getClass()).equals(String.valueOf(a2.getClass())))){
            if (String.valueOf(a1.getClass()).equals("class herbivore"))return 1;
            else return -1;
        }
        else{
            double dist1=Math.pow(a1.loc.x,2)+Math.pow(a1.loc.y,2);
            double dist2=Math.pow(a2.loc.x,2)+Math.pow(a2.loc.y,2);
            if (dist1>=dist2)return 1;
            else return -1;
        }
    }
    public void update_time(int total,int cur){
        int new_time=new Random().nextInt(total-cur);
        new_time+=timestamp;
        timestamp=new_time;
    }
    public int get_time(){
        return timestamp;
    }
    public double get_health(){
        return health;
    }
    public abstract boolean is_it_alive();
    public abstract void choice(animal a1,animal a2,grassland g1,grassland g2);
}
class herbivore extends animal{
    int grass_capacity;
    int turn_out;
    grassland current;
    public herbivore(Point loc,int timestamp,double health,int grass_capacity){
        super(loc,timestamp,health);
        this.grass_capacity=grass_capacity;
        this.turn_out=0;
    }
    public void move_to_grassland(grassland g1,grassland g2){
        double dist1=Math.pow(g1.center.x-loc.x,2)+Math.pow(g1.center.y-loc.y,2);
        double dist2=Math.pow(g2.center.x-loc.x,2)+Math.pow(g2.center.y-loc.y,2);
        if (dist1<dist2){
            loc=new Point(g1.center.x,g1.center.y);
            current=g1;
        }
        else{
            loc=new Point(g2.center.x,g2.center.y);
            current=g2;
        }
    }
    public grassland nearest(grassland g1,grassland g2){
        double dist1=Math.pow(g1.center.x-loc.x,2)+Math.pow(g1.center.y-loc.y,2);
        double dist2=Math.pow(g2.center.x-loc.x,2)+Math.pow(g2.center.y-loc.y,2);
        if (dist1>dist2)return g2;
        else return g1;
    }
    public carnivore get_nearest(carnivore c1,carnivore c2){
        double dist1=Math.pow(c1.loc.x-loc.x,2)+Math.pow(c1.loc.y-loc.y,2);
        double dist2=Math.pow(c2.loc.x-loc.x,2)+Math.pow(c2.loc.y-loc.y,2);
        if (dist1>dist2)return c2;
        else return c1;
    }
    public void move(grassland g1,grassland g2,int r){
        grassland g=nearest(g1,g2);
        int old_h=(int)Math.sqrt(Math.pow(g.center.x-loc.x,2)+Math.pow(g.center.y-loc.y,2));
        int base=(int)Math.abs(g.center.x-loc.x);
        int leg=(int)Math.abs(g.center.y-loc.y);
        int xnew=loc.x;
        if (g.center.x>loc.x)xnew+=r*(base)/old_h;
        else xnew-=r*(base)/old_h;
        int ynew=loc.y;
        if (g.center.y>loc.y)ynew+=r*(leg)/old_h;
        else ynew-=r*(leg)/old_h;
        loc=new Point(xnew,ynew);
    }
    public void move_away(carnivore c1,carnivore c2,int r){
        carnivore c=get_nearest(c1,c2);
        int old_h=(int)Math.sqrt(Math.pow(c.loc.x-loc.x,2)+Math.pow(c.loc.y-loc.y,2));
        int base=(int)Math.abs(c.loc.x-loc.x);
        int leg=(int)Math.abs(c.loc.y-loc.y);
        int xnew=loc.x;
        if (c.loc.x>loc.x)xnew-=r*(base)/old_h;
        else xnew+=r*(base)/old_h;
        int ynew=loc.y;
        if (c.loc.y>loc.y)ynew-=r*(leg)/old_h;
        else ynew+=r*(leg)/old_h;
        loc=new Point(xnew,ynew);
    }
    public void set_current(grassland g1,grassland g2){
        grassland g=is_in_grassland(g1,g2);
        current=g;
    }
    public void eat(int val,grassland g){
        grass_capacity-=val;
        g.reduce_grass(val);
    }
    public grassland is_in_grassland(grassland g1,grassland g2){
        double dist1=Math.pow(g1.center.x-loc.x,2)+Math.pow(g1.center.y-loc.y,2);
        double dist2=Math.pow(g2.center.x-loc.x,2)+Math.pow(g2.center.y-loc.y,2);
        if (dist1<=g1.radius*g1.radius)return g1;
        else if (dist2<=g2.radius*g2.radius)return g2;
        else return null;
    }
    @Override
    public boolean is_it_alive(){
        if (status=='d')return false;
        else return true;
    }
    public void reduce_health(int val){
        health-=val;
        if (health<=0)status='d';
    }
    public void increase_health(int val){
        health+=(health)*(val)/100;
    }
    @Override
    public void choice(animal c11,animal c21,grassland g1,grassland g2){
        //check health before invoking choice
        carnivore c1=(carnivore)c11;
        carnivore c2=(carnivore)c21;
        grassland g=is_in_grassland(g1,g2);
        if ((c1==null)&&(c2==null)){
            System.out.println("Both the carnivores are dead");
            int chance=new Random().nextInt(100);
            System.out.println("chance= "+chance);
            if (chance<50){
                System.out.println("moving to nearest grassland");
                move_to_grassland(g1,g2);
                if (g!=null)reduce_health(25);
            }
        }
        else if (g==null){
            System.out.println("herbivore not inside grassland");
            turn_out++;
            if (turn_out>=7){
                reduce_health(5);
            }
            int chance=new Random().nextInt(100);
            System.out.println("chance= "+chance);
            if (chance>=5){
                System.out.println("herbivore outside grassland and will not stay");
                int chance2=new Random().nextInt(100);
                System.out.println("chance "+chance2);
                if (chance2>=35){
                    System.out.println("move 5 units towards nearest grassland");
                    move(g1,g2,5);
                    set_current(g1,g2);
                }
                else{
                    System.out.println("move 4 units away from nearest carnivore");
                    move_away(c1,c2,4);
                    set_current(g1,g2);
                }
            }
            else{
                //condition 3a
                System.out.println("herbivore outside grassland and will stay");
                //....
            }
        }
        else{
            turn_out=0;
            System.out.println("herbivore inside the grassland");
            if (g.grass>=grass_capacity){
                System.out.println("grass available greater than capacity");
                int chance2=new Random().nextInt(100);
                System.out.println("chance= "+chance2);
                if (chance2<90){
                    System.out.println("eat the grass");
                    int temp=grass_capacity;
                    eat(temp,g);
                    increase_health(50);
                }
                else{
                    System.out.println("move from grassland");
                    reduce_health(25);
                    int chance3=new Random().nextInt(100);
                    System.out.println("chance "+chance3);
                    if (chance3<50){
                        System.out.println("move away from carnivore");
                        move_away(c1,c2,2);
                        set_current(g1,g2);
                    }
                    else{
                        System.out.println("move towards grassland");
                        move(g1,g2,3);
                        set_current(g1,g2);
                    }
                }
            }
            else{
                System.out.println("grass available is less than grass capacity of herbivore");
                int chance3=new Random().nextInt(100);
                System.out.println("chance "+chance3);
                if (chance3<20){
                    System.out.println("eat the grass");
                    int temp=g.grass;
                    eat(temp,g);
                    increase_health(20);
                }
                else{
                    System.out.println("less grass and herbivore doesnt want to stay");
                    reduce_health(25);
                    int chance4=new Random().nextInt(100);
                    System.out.println("chance= "+chance4);
                    if (chance4<70){
                        System.out.println("move away from carnivore");
                        move_away(c1,c2,4);
                        set_current(g1,g2);
                    }
                    else{
                        System.out.println("move towards grassland");
                        move(g1,g2,2);
                        set_current(g1,g2);
                    }
                }
            }
            //-------------
        }


    }
}
class carnivore extends animal{
    int turn;
    public carnivore(Point loc,int timestamp,double health){
        super(loc,timestamp,health);
        this.turn=0;
    }
    public void reduce_health(int val){
        health-=val;
        if (health<=0)status='d';
    }
    public void eat(herbivore h){
        h.status='d';
        health+=h.health*2/3;
    }
    @Override
    public boolean is_it_alive(){
        if (status=='d')return false;
        else return true;
    }
    public herbivore get_nearest(herbivore h1,herbivore h2){
        double dist1=Math.pow(h1.loc.x-loc.x,2)+Math.pow(h1.loc.y-loc.y,2);
        double dist2=Math.pow(h2.loc.x-loc.x,2)+Math.pow(h2.loc.y-loc.y,2);
        if (dist1>dist2)return h2;
        else return h1;
    }
    public void move_away(herbivore h1,herbivore h2,int r){
        herbivore h=get_nearest(h1,h2);
        int old_h=(int)Math.sqrt(Math.pow(h.loc.x-loc.x,2)+Math.pow(h.loc.y-loc.y,2));
        int base=(int)Math.abs(h.loc.x-loc.x);
        int leg=(int)Math.abs(h.loc.y-loc.y);
        int xnew=loc.x;
        if (h.loc.x>loc.x)xnew+=r*(base)/old_h;
        else xnew-=r*(base)/old_h;
        int ynew=loc.y;
        if (h.loc.y>loc.y)ynew+=r*(leg)/old_h;
        else ynew-=r*(leg)/old_h;
        loc=new Point(xnew,ynew);
    }
    public grassland is_in_grassland(grassland g1,grassland g2){
        double dist1=Math.pow(g1.center.x-loc.x,2)+Math.pow(g1.center.y-loc.y,2);
        double dist2=Math.pow(g2.center.x-loc.x,2)+Math.pow(g2.center.y-loc.y,2);
        if (dist1<=g1.radius*g1.radius)return g1;
        else if (dist2<=g2.radius*g2.radius)return g2;
        else return null;
    }
    public herbivore get_nearest_up1(herbivore h1,herbivore h2){
        double dist1=Math.pow(h1.loc.x-loc.x,2)+Math.pow(h1.loc.y-loc.y,2);
        dist1=Math.sqrt(dist1);
        double dist2=Math.pow(h2.loc.x-loc.x,2)+Math.pow(h2.loc.y-loc.y,2);
        dist2=Math.sqrt(dist2);
        if ((dist1<=1)&&(dist2<=1)){
            if (dist1<dist2)return h1;
            else return h2;
        }
        else if (dist1<=1)return h1;
        else if (dist2<=1)return h2;
        else return null;
    }
    public herbivore herb_in_5(herbivore h1,herbivore h2){
        double dist1=Math.pow(h1.loc.x-loc.x,2)+Math.pow(h1.loc.y-loc.y,2);
        dist1=Math.sqrt(dist1);
        double dist2=Math.pow(h2.loc.x-loc.x,2)+Math.pow(h2.loc.y-loc.y,2);
        dist2=Math.sqrt(dist2);
        if ((dist1<=5)&&(dist2<=5)){
            if (dist1<dist2)return h1;
            else return h2;
        }
        else if (dist1<=5)return h1;
        else if (dist2<=5)return h2;
        else return null;
    }
    @Override
    public void choice(animal h11,animal h21,grassland g1,grassland g2){
        herbivore h1=(herbivore)h11;
        herbivore h2=(herbivore)h21;
        if (herb_in_5(h1,h2)==null){
            turn++;
            if (turn>=7){
                reduce_health(6);
            }
        }
        else turn=0;
        if ((h1==null)&&(h2==null)){
            //no herbivore left
            //do nothing
            grassland g=is_in_grassland(g1,g2);
            if (g==null)reduce_health(60);
            else reduce_health(30);
        }
        else{
            herbivore h=get_nearest_up1(h1,h2);
            if (h!=null){
                //at least one herb is within 1 unit radius
                eat(h);
                loc=new Point(h.loc.x,h.loc.y);
                h=null;
            }
            else{
                //none of the herbs was within 1 unit radius
                grassland g=is_in_grassland(g1,g2);
                if (g==null){
                    //carnivore outside grassland
                    int chance3=new Random().nextInt(100);
                    System.out.println("chance= "+chance3);
                    if (chance3<92){
                        //move towards herbivore
                        move_away(h1,h2,4);
                    }
                    else{
                        //stay at the current position
                        reduce_health(60);
                    }
                }
                else{
                    //carnivore inside grassland
                    int chance3=new Random().nextInt(25);
                    System.out.println("chance= "+chance3);
                    if (chance3>=25){
                        move_away(h1,h2,2);
                    }
                    else{
                        reduce_health(30);
                    }
                }
            }
            //--------------------------------------------
        }
    }
}
class grassland{
    Point center;
    int grass;
    double radius;
    public grassland(Point center,double radius,int grass){
        this.center=center;
        this.grass=grass;
        this.radius=radius;
    }
    public void reduce_grass(int val){
        grass-=(double)val;
    }
}
class world{
    protected animal[] queue;
    protected int count,total;
    protected herbivore h1,h2,h11;
    protected carnivore c1,c2;
    protected grassland g1,g2;
    public world(int total,herbivore h1,herbivore h2,carnivore c1,carnivore c2,grassland g1,grassland g2){
        queue=new animal[5];
        count=0;
        this.total=total;
        this.h1=h1;
        this.h2=h2;
        this.c1=c1;
        this.c2=c2;
        this.g1=g1;
        this.g2=g2;
        this.h11=new herbivore(new Point(0,0),0,0,0);
    }
    public void turn(animal a){
        System.out.println(String.valueOf(a.getClass()));
        if (String.valueOf(a.getClass()).equals("class herbivore")){
            a.choice(c1,c2,g1,g2);
        }
        else{
            a.choice(h1,h2,g1,g2);
        }
    }
    public void ins(animal a,int ind){
         if (ind==0)queue[1]=a;
        else if (ind==1){
            queue[2]=a;
            if (h11.compare(queue[1],queue[2])<0){
                animal temp=queue[2];
                queue[2]=queue[1];
                queue[1]=temp;
            }
        }
        else{
            int chd=ind+1;
            int par=chd/2;
            queue[chd]=a;
            while(par>=1){
                if (h11.compare(queue[chd],queue[par])>0){
                    animal temp=queue[chd];
                    queue[chd]=queue[par];
                    queue[par]=temp;
                }
                else break;
                chd=par;
                par=chd/2;
            }
        }
        //for (int i=1;i<=ind+1;i++)System.out.println(c[i].sad+" "+c[i].day); 
        count++;
    }
     public animal getmax(){
        animal temp=queue[1];
        queue[1]=queue[count];
        count--;
        //sink down you douchebag
        int par=1;
        while(par<=count){
            if (par*2+1<=count){
                if ((h11.compare(queue[par],queue[par*2])<0)||(h11.compare(queue[par],queue[par*2+1])<0)){
                    int anc=h11.compare(queue[par*2],queue[par*2+1])>0?par*2:par*2+1;
                    animal tem=queue[anc];
                    queue[anc]=queue[par];
                    queue[par]=tem;
                    par=anc;
                }
                else break;
            }
            else if (par*2<=count){
                if (h11.compare(queue[par],queue[par*2])<0){
                animal tem=queue[par];
                queue[par]=queue[par*2];
                queue[par*2]=tem;
                par=par*2;
                }
                else break;
            }
            else break;
        }
        //for (int i=1;i<=count;i++)System.out.println(c[i].sad+" "+c[i].day);
        return temp;
    }
    public int get_count(){
        return count;
    }
    public void run(){
        int i=0;
        int tur=0;
        while((count!=0)&&(i!=total)&&(tur<total)){
            System.out.println("time "+i);
            while((count!=0)&&(queue[1].get_time()==i)&&(tur<total)){
            animal a=getmax();
            if (a==h1){
                System.out.println("It is first herbivore");
            }
            if (a==h2){
                System.out.println("It is second herbivore");
            }
            if (a==c1){
                System.out.println("It is first carnivore");
            }
            if (a==c2){
                System.out.println("It is second carnivore");
            }
            turn(a);
            System.out.println(a.loc.x+" "+a.loc.y);
            tur++;
            System.out.println("It's health after taking turn is "+a.get_health());
            if (a.is_it_alive()){
                a.update_time(total,i);
                if (a.get_time()<total-1)ins(a,get_count());
            }
            else{
                a=null;
                System.out.println("It is dead");
            }
            }
            i++;
        }
    }
}
class aplab4{
    public static void main(String[] args)throws IOException{
        Reader.init(System.in);
        System.out.println("Enter total final time for simulation");
        int total=Reader.nextInt();
        System.out.println("Enter x,y center,radius and grass available for first grassland");
        grassland g1=new grassland(new Point(Reader.nextInt(),Reader.nextInt()),Reader.nextDouble(),Reader.nextInt());
        System.out.println("Enter x,y center,radius and grass available for second grassland");
        grassland g2=new grassland(new Point(Reader.nextInt(),Reader.nextInt()),Reader.nextDouble(),Reader.nextInt());
        System.out.println("Enter health and grass capacity for herbivores");
        int h_health=Reader.nextInt();
        int h_grass=Reader.nextInt();
        System.out.println("Enter x,y position and timestamp for first herbivore");
        herbivore h1=new herbivore(new Point(Reader.nextInt(),Reader.nextInt()),Reader.nextInt(),h_health,h_grass);
        System.out.println("Enter x,y position and timestamp for second herbivore");
        herbivore h2=new herbivore(new Point(Reader.nextInt(),Reader.nextInt()),Reader.nextInt(),h_health,h_grass);
        System.out.println("Enter health for carnivores");
        int c_health=Reader.nextInt();
        System.out.println("Enter x,y position and timestamp for first carnivore");
        carnivore c1=new carnivore(new Point(Reader.nextInt(),Reader.nextInt()),Reader.nextInt(),c_health);
        System.out.println("Enter x,y position and timestamp for first carnivore");
        carnivore c2=new carnivore(new Point(Reader.nextInt(),Reader.nextInt()),Reader.nextInt(),c_health);
        System.out.println("The simulation begins-");
        world w=new world(total,h1,h2,c1,c2,g1,g2);
        w.ins(h1,0);
        w.ins(h2,1);
        w.ins(c1,2);
        w.ins(c2,3);
        w.run();
    }
}
