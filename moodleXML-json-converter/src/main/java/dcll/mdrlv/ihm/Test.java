package dcll.mdrlv.ihm;

/**
 * @author :
 *
 */
public final class Test {

    /**
     * Constructeur privé.
     */
	private Test() {

	}

	/**
	 * @param args :
	 */
	public static void main(final String[] args) {
		// TODO Auto-generated method stub

	java.awt.EventQueue.invokeLater(new Runnable() {
        public void run() {
            new Gui().setVisible(true);
        }
	});

	}

}
