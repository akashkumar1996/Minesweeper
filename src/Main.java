
public class Main implements Runnable{
	
	Gui gui = new Gui();
	
	public static void main(String args[]){
		new Thread(new Main()).start();
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		while(true){
			gui.repaint();
			if(gui.resetter==false&&gui.victory==false&&gui.defeat==false){
				gui.checkVictoryStatus();
				gui.reveal();
				if(gui.defeat==true)
					gui.revealMines();
			}
		}
	}
}