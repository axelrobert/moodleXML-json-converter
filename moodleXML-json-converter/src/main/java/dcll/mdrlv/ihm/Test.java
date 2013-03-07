package dcll.mdrlv.ihm;

public class Test {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
	Gui g = new Gui();
	
	java.awt.EventQueue.invokeLater(new Runnable() {
        public void run() {
            new Gui().setVisible(true);
        }
	});
		
	}

}
