package dcll.mdrlv.ihm;

import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;

import javax.swing.JScrollBar;
import javax.swing.JScrollPane;

/**
 * @author Emilien DUBOIS, Romain LECHIEN, Francois MANCIET, Axel ROBERT, David VILLARD
 *
 */
class Synchronizer implements AdjustmentListener  
{  
    /**
     * v1 : ScrollBarre verticale du ScrollPane1
     * h1 : ScrollBarre horizontale du ScrollPane1
     * v2 : ScrollBarre verticale du ScrollPane2
     * h2 : ScrollBarre horizontale du ScrollPane2
     */
    JScrollBar v1, h1, v2, h2;  
   
    /**
     * @param sp1
     * @param sp2
     * Constructeur : Synchronise* les deux scollPane sp1 et sp2
     */
    public Synchronizer(JScrollPane sp1, JScrollPane sp2)  
    {  
        v1 = sp1.getVerticalScrollBar();  
        h1 = sp1.getHorizontalScrollBar();  
        v2 = sp2.getVerticalScrollBar();  
        h2 = sp2.getHorizontalScrollBar();  
    }  
   
    /* (non-Javadoc)
     * @see java.awt.event.AdjustmentListener#adjustmentValueChanged(java.awt.event.AdjustmentEvent)
     * 
     */
    public void adjustmentValueChanged(AdjustmentEvent e)  
    {  
        JScrollBar scrollBar = (JScrollBar)e.getSource();  
        int value = scrollBar.getValue();  
        JScrollBar target = null;  
   
        if(scrollBar == v1)  
            target = v2;  
        if(scrollBar == h1)  
            target = h2;  
        if(scrollBar == v2)  
            target = v1;  
        if(scrollBar == h2)  
            target = h1;  
   
        target.setValue(value);  
    }  
} 