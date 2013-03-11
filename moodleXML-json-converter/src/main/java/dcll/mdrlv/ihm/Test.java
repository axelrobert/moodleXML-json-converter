package dcll.mdrlv.ihm;

/**
 * @author :
 *
 */
public class Test {

	/**
	 * Constructeur
	 */
	public Test() {

	}

	/**
	 * @param args :
	 */
	public static void main(final String[] args) {
		// TODO Auto-generated method stub
	Gui g = new Gui();

	java.awt.EventQueue.invokeLater(new Runnable() {
        public void run() {
            new Gui().setVisible(true);
        }
	});

	}

}
