import javax.swing.*;
import java.util.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

public class Gui extends JFrame{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -5890012412832498042L;
	int spacing=5;
	public int mx=-100;
	public int my=-100;
	
	public int smileyX=568;
	public int smileyY=5;
	
	public int smileyCentreX = smileyX+35;
	public int smileyCentreY=smileyY+35;
	
	public int flaggerX=648;
	public int flaggerY=5;
	public int currentFlagged=0;
	
	public int flaggerCentreX = flaggerX+35;
	public int flaggerCentreY=flaggerY+35;
	
	public int timeX=893;
	public int timeY=6;
	
	public int flagScoreX=733;
	public int flagScoreY=6;
	
	public String message="Welcome to Minesweeper...";
	
	public int sec=0;
	
	Date startDate = new Date();
	
	public boolean hapiness=true;
	public boolean victory=false;
	public boolean defeat=false;
	public boolean resetter=false;
	public boolean flagger=false;
	
	Random rand = new Random();
	
	int mines[][] = new int[13][7];
	int neighbours[][] = new int[13][7];
	boolean flagged[][] = new boolean[13][7];
	boolean revealed[][] = new boolean[13][7];
	
	
	
	public Gui(){
		this.setTitle("Minesweeper");
		this.setSize(1046, 729);
		this.setVisible(true);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setResizable(false);
		
		for(int i=0;i<13;i++){
			for(int j=0;j<7;j++){
				if( rand.nextInt(100) < 20){
					mines[i][j]=1;
				}
				else{
					mines[i][j]=0;
				}
				revealed[i][j]=false;
				flagged[i][j]=false;
			}
		}
		
		
		for(int i=0;i<13;i++){
			for(int j=0;j<7;j++){
				neighbours[i][j]=neighbourCount(i,j);
			}
		}
		
		Board board = new Board();
		this.setContentPane(board);
		
		Click click = new Click();
		this.addMouseListener(click);
		
		Move move = new Move();
		this.addMouseMotionListener(move);
	}
	
	public class Board extends JPanel{
		/**
		 * 
		 */
		private static final long serialVersionUID = -6410363879172718206L;

		public void paintComponent(Graphics g){
			g.setColor(new Color(96,96,96));
			g.fillRect(0, 0, 1280, 800);
			for(int i=0;i<13;i++){
				for(int j=0;j<7;j++){
					g.setColor(new Color(160,160,160));
					/*if(mines[i][j]==1)
						g.setColor(Color.GREEN);*/
					if(revealed[i][j]==true){
						g.setColor(Color.WHITE);
						if(mines[i][j]==1){
							g.setColor(Color.RED);
						}
					}
					if(mx>=spacing+i*80 && mx<=spacing+i*80+80-spacing && my>=spacing+j*80+106 && my<=spacing+j*80+186-spacing)
						g.setColor(new Color(224,224,224));
					g.fillRect(spacing+i*80, spacing+j*80+80, 80-2*spacing, 80-2*spacing);
					if(revealed[i][j]==true){
						g.setColor(Color.BLACK);
						if(mines[i][j]==0 && neighbours[i][j]!=0){
							if(neighbours[i][j]==1)
								g.setColor(Color.BLUE);
							else if(neighbours[i][j]==2)
								g.setColor(Color.green);
							else if(neighbours[i][j]==3)
								g.setColor(Color.red);
							else if(neighbours[i][j]==4)
								g.setColor(new Color(0,0,128));
							else if(neighbours[i][j]==5)
								g.setColor(new Color(178,34,34));
							else if(neighbours[i][j]==6)
								g.setColor(new Color(72,209,204));
							else if(neighbours[i][j]==8)
								g.setColor(Color.DARK_GRAY);
							g.setFont(new Font("Tahoma",Font.BOLD,40));
							g.drawString(Integer.toString(neighbours[i][j]), i*80+27, j*80+80+55);
						}
						else if(mines[i][j]==1){
							g.fillRect(i*80+30, j*80+100, 20, 40);
							g.fillRect(i*80+20, j*80+110, 40, 20);
							g.fillRect(i*80+25, j*80+105, 30, 30);
							g.fillRect(i*80+38, j*80+95, 4, 50);
							g.fillRect(i*80+15, j*80+118, 50, 4);
						}
					}
					if(flagged[i][j]==true){
						g.setColor(Color.BLACK);
						g.fillRect(i*80-20+40, j*80+20+120, 40, 5);
						g.fillRect(i*80+40, j*80-25+120, 5, 50);
						g.setColor(Color.red);
						g.fillRect(i*80-20+40, j*80-20+120, 20, 20);
					}
				}
			}
			g.setColor(Color.blue);
			g.fillRect(smileyX-3, smileyY-2, 74, 74);
			g.setColor(Color.YELLOW);
			g.fillOval(smileyX, smileyY, 68, 68);
			g.setColor(Color.BLACK);
			g.fillOval(smileyX+15, smileyY+20, 10, 10);
			g.fillOval(smileyX+45, smileyY+20, 10, 10);
			if(hapiness==true){
				g.fillRect(smileyX+20, smileyY+50, 30, 5);
				g.fillRect(smileyX+16, smileyY+45, 5, 5);
				g.fillRect(smileyX+49, smileyY+45, 5, 5);
			}
			else{
				g.fillRect(smileyX+20, smileyY+45, 30, 5);
				g.fillRect(smileyX+16, smileyY+50, 5, 5);
				g.fillRect(smileyX+49, smileyY+50, 5, 5);
			}
			
			g.setColor(Color.BLACK);
			g.fillRect(timeX, timeY, 138, 70);
			if(defeat==false&&victory==false)
				sec=(int)(new Date().getTime()-startDate.getTime())/1000;
			if(sec>999)
				sec=999;
			g.setColor(Color.WHITE);
			if(defeat==true)
				g.setColor(Color.red);
			else if(victory==true)
				g.setColor(Color.GREEN);
			g.setFont(new Font("Tahoma",Font.PLAIN,70));
			if(sec<10)
				g.drawString("00"+Integer.toString(sec), timeX+10, timeY+60);
			else if(sec<100)
				g.drawString("0"+Integer.toString(sec), timeX+10, timeY+60);
			else
				g.drawString(Integer.toString(sec), timeX+10, timeY+60);
			if(victory==true || defeat==true){
				if(victory==true)
					g.setColor(Color.green);
				else if(defeat==true)
					g.setColor(Color.RED);
				g.setFont(new Font("Tahoma",Font.BOLD,50));
				g.drawString(message, 0, 60);
				
			}
			else{
				g.setColor(new Color(204,204,0));
				g.setFont(new Font("Tahoma",Font.BOLD,40));
				g.drawString(message, 0, 60);
			}
			g.setColor(new Color(204,204,0));
			g.setFont(new Font("Tahoma",Font.ITALIC,30));
			g.drawString("A game developed by Akash Kumar", 300, 679);
			if(flagger==false)
				g.drawOval(flaggerX, flaggerY, 70, 70);
			else
				g.fillOval(flaggerX, flaggerY, 70, 70);
			g.setColor(Color.BLACK);
			g.fillRect(flaggerCentreX-20, flaggerCentreY+20, 40, 5);
			g.fillRect(flaggerCentreX, flaggerCentreY-25, 5, 50);
			g.setColor(Color.red);
			g.fillRect(flaggerCentreX-20, flaggerCentreY-20, 20, 20);
		
			g.setColor(Color.BLACK);
			g.fillRect(flagScoreX, flagScoreY, 138, 70);
			g.setColor(Color.WHITE);
			if(defeat==true)
				g.setColor(Color.RED);
			else if(victory==true)
				g.setColor(Color.GREEN);
			g.setFont(new Font("Tahoma",Font.PLAIN,50));
			if(currentFlagged<10)
				g.drawString("0"+Integer.toString(currentFlagged)+"/"+Integer.toString(totalMines()), flagScoreX+5, flagScoreY+50);
			else
				g.drawString(Integer.toString(currentFlagged)+"/"+Integer.toString(totalMines()), flagScoreX+5, flagScoreY+50);
		}
	}
	
	public class Move implements MouseMotionListener{

		@Override
		public void mouseDragged(MouseEvent arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mouseMoved(MouseEvent e) {
			// TODO Auto-generated method stub
			mx=e.getX();
			my=e.getY();
		}
		
	}
	
	public class Click implements MouseListener{

		@Override
		public void mouseClicked(MouseEvent e) {
			// TODO Auto-generated method stub
			mx=e.getX();
			my=e.getY();
			if(inBoxX()!=-1 && inBoxY()!=-1&&defeat==false&&victory==false){
				if(flagger==false){
					if(flagged[inBoxX()][inBoxY()]==false)
						revealed[inBoxX()][inBoxY()]=true;
				}
				else{
					if(flagged[inBoxX()][inBoxY()]==true){
						flagged[inBoxX()][inBoxY()]=false;
						currentFlagged--;
					}
					else{
						flagged[inBoxX()][inBoxY()]=true;
						currentFlagged++;
					}
					
				}
				//System.out.println(neighbours[inBoxX()][inBoxY()]);
			}
			if(inSmiley()==true){
				defeat=false;
				victory=false;
				resetAll();
			}
			if(inFlagger()==true){
				if(flagger==true)
					flagger=false;
				else
					flagger=true;
			}
		}

		@Override
		public void mouseEntered(MouseEvent arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mouseExited(MouseEvent arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mousePressed(MouseEvent arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mouseReleased(MouseEvent arg0) {
			// TODO Auto-generated method stub
			
		}
		
	}
	
	public int inBoxX(){
		for(int i=0;i<13;i++){
			for(int j=0;j<7;j++){
				if(mx>=spacing+i*80 && mx<=spacing+i*80+80-spacing && my>=spacing+j*80+106 && my<=spacing+j*80+186-spacing)
					return i;
			}
		}
		return -1;
	}
	
	public void resetAll(){
		startDate = new Date();
		resetter=true;
		hapiness=true;
		victory=false;
		defeat=false;
		currentFlagged=0;
		message="Welcome to Minesweeper...";
		
		for(int i=0;i<13;i++){
			for(int j=0;j<7;j++){
				if( rand.nextInt(100) < 20){
					mines[i][j]=1;
				}
				else{
					mines[i][j]=0;
				}
				revealed[i][j]=false;
				flagged[i][j]=false;
			}
		}
		for(int i=0;i<13;i++){
			for(int j=0;j<7;j++){
				neighbours[i][j]=neighbourCount(i,j);
			}
		}
		resetter=false;
	}
	
	public int inBoxY(){
		for(int i=0;i<13;i++){
			for(int j=0;j<7;j++){
				if(mx>=spacing+i*80 && mx<=spacing+i*80+80-spacing && my>=spacing+j*80+106 && my<=spacing+j*80+186-spacing)
					return j;
			}
		}
		return -1;
	}
	
	public int neighbourCount(int i,int j){
		int count=0;
		if(i-1>=0&&j-1>=0 && mines[i-1][j-1]==1)
			count++;
		if(i-1>=0&&j+1<7 && mines[i-1][j+1]==1)
			count++;
		if(i-1>=0 && mines[i-1][j]==1)
			count++;
		if(i+1<13&&j+1<7 && mines[i+1][j+1]==1)
			count++;
		if(i+1<13 && j-1>=0 && mines[i+1][j-1]==1)
			count++;
		if(i+1<13 && mines[i+1][j]==1)
			count++;
		if(j-1>=0 && mines[i][j-1]==1)
			count++;
		if(j+1<7 && mines[i][j+1]==1)
			count++;
		return count;
	}
	
	public void checkVictoryStatus(){
		for(int i=0;i<13;i++){
			for(int j=0;j<7;j++){
				if(mines[i][j]==1&&revealed[i][j]==true){
					hapiness=false;
					defeat=true;
					message="You Lost the game!!!";
				}
			}
		}
		if(totalBoxesRevealed()>=91-totalMines()){
			message="You won the game!!!";
			victory=true;
		}
	}
	
	public int totalMines(){
		int total=0;
		for(int i=0;i<13;i++){
			for(int j=0;j<7;j++){
				if(mines[i][j]==1)
					total++;
			}
		}
		return total;
	}
	
	public int totalBoxesRevealed(){
		int total=0;
		for(int i=0;i<13;i++){
			for(int j=0;j<7;j++){
				if(revealed[i][j]==true)
					total++;
			}
		}
		return total;
	}
	
	public boolean inSmiley(){
		if(mx>=smileyX-3&&mx<smileyX+71&&my>=smileyY-2&&my<smileyY+72)
			return true;
		return false;
	}
	public boolean inFlagger(){
		if(mx>=flaggerX-3&&mx<flaggerX+71&&my>=flaggerY-2&&my<flaggerY+72)
			return true;
		return false;
	}
	public void reveal(){
		for(int i=0;i<13;i++){
			for(int j=0;j<7;j++){
				if(revealed[i][j]==true&&neighbours[i][j]==0&&mines[i][j]==0){
					if(i-1>=0&&j-1>=0 && mines[i-1][j-1]==0){
						revealed[i-1][j-1]=true;
						if(flagged[i-1][j-1]==true){
							flagged[i-1][j-1]=false;
							currentFlagged--;
						}
							
					}
					if(i-1>=0&&j+1<7 && mines[i-1][j+1]==0){
						revealed[i-1][j+1]=true;
						if(flagged[i-1][j+1]==true){
							flagged[i-1][j+1]=false;
							currentFlagged--;
						}
					}
					if(i-1>=0 && mines[i-1][j]==0){
						revealed[i-1][j]=true;
						if(flagged[i-1][j]==true){
							flagged[i-1][j]=false;
							currentFlagged--;
						}
					}
					if(i+1<13&&j+1<7 && mines[i+1][j+1]==0){
						revealed[i+1][j+1]=true;
						if(flagged[i+1][j+1]==true){
							flagged[i+1][j+1]=false;
							currentFlagged--;
						}
					}
					if(i+1<13 && j-1>=0 && mines[i+1][j-1]==0){
						revealed[i+1][j-1]=true;
						if(flagged[i+1][j-1]==true){
							flagged[i+1][j-1]=false;
							currentFlagged--;
						}
					}
					if(i+1<13 && mines[i+1][j]==0){
						revealed[i+1][j]=true;
						if(flagged[i+1][j]==true){
							flagged[i+1][j]=false;
							currentFlagged--;
						}
					}
					if(j-1>=0 && mines[i][j-1]==0){
						revealed[i][j-1]=true;
						if(flagged[i][j-1]==true){
							flagged[i][j-1]=false;
							currentFlagged--;
						}
					}
					if(j+1<7 && mines[i][j+1]==0){
						revealed[i][j+1]=true;
						if(flagged[i][j+1]==true){
							flagged[i][j+1]=false;
							currentFlagged--;
						}
					}
				}
			}
		}
	}
	public void revealMines(){
		for(int i=0;i<13;i++){
			for(int j=0;j<7;j++){
				if(mines[i][j]==1){
					flagged[i][j]=false;
					revealed[i][j]=true;
				}
			}
		}
	}
}