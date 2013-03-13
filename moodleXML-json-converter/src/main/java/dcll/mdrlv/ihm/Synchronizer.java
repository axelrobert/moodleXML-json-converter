package dcll.mdrlv.ihm;

import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;

import javax.swing.JScrollBar;
import javax.swing.JScrollPane;

/**
 * @author Emilien DUBOIS, Romain LECHIEN,
 * Francois MANCIET, Axel ROBERT, David VILLARD
 *
 */
class Synchronizer implements AdjustmentListener {

    /**
     * vertical1 : ScrollBarre verticale du ScrollPane1.
     * horizon1 : ScrollBarre horizontale du ScrollPane1
     * vertical2 : ScrollBarre verticale du ScrollPane2
     * horizon2 : ScrollBarre horizontale du ScrollPane2
     */
    private final JScrollBar vertical1, horizon1, vertical2, horizon2;

    /**
     * @param sp1 :
     * @param sp2 :
     * Constructeur : Synchronise* les deux scollPane sp1 et sp2
     */
    public Synchronizer(final JScrollPane sp1, final JScrollPane sp2) {
        vertical1 = sp1.getVerticalScrollBar();
        horizon1 = sp1.getHorizontalScrollBar();
        vertical2 = sp2.getVerticalScrollBar();
        horizon2 = sp2.getHorizontalScrollBar();
    }

    /**
     * @param pEvent :
     */
    public void adjustmentValueChanged(final AdjustmentEvent pEvent) {
        final JScrollBar scrollBar = (JScrollBar) pEvent.getSource();
        final int value = scrollBar.getValue();
        JScrollBar target = null;

        if (scrollBar == vertical1) {
            target = vertical2;
        }
        if (scrollBar == horizon1) {
            target = horizon2;
        }
        if (scrollBar == vertical2) {
            target = vertical1;
        }
        if (scrollBar == horizon2) {
            target = horizon1;
        }
        target.setValue(value);
    }
}
