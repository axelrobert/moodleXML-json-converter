package dcll.mdrlv.ihm;

import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import org.apache.log4j.Logger;

/**
 * @author : Emilien Dubois, Romain Lechien
 */
public final class Main {

	/**
	 * Constructeur public.
	 */
	public Main() {
		final Logger logger = Logger.getLogger(Main.class);
		java.awt.EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					// Set cross-platform Java
					// L&F (also called "Metal")
					UIManager.setLookAndFeel("" + "javax.swing.plaf."
							+ "nimbus.NimbusLookAndFeel");
				} catch (UnsupportedLookAndFeelException e) {
					logger.warn("UnsupportedLook" + "AndFeelException");
				} catch (ClassNotFoundException e) {
					logger.warn("ClassNotFoundException");
				} catch (InstantiationException e) {
					logger.warn("InstanciationException");
				} catch (IllegalAccessException e) {
					logger.warn("IllegalAccessException");
				}
				new Gui().setVisible(true);
			}
		});

	}

	/**
	 * @param args
	 *            :
	 */
	public static void main(final String[] args) {
		// TODO Auto-generated method stub
		new Main();
	}
}
