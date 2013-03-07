package dcll.mdrlv.ihm;


import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

public class Gui extends JFrame{
	
	private Etat etat;
	private JPanel pan;
	private JPanel panInPut;
	private JPanel panOutPut;
	private CustomFileFilter xmlFilter;
	private CustomFileFilter jsonFilter;
	private JButton valider;
	private JButton convertir;
	private JButton reset;
	private JButton fermer;
	private JRadioButton sensXMLtoJSON;
	private JRadioButton sensJSONtoXML;
	private JLabel inPut;
	private JLabel outPut;
	private JButton parcourir;
	
	

	public Gui (){
		
		
		initComponents();
		
		this.setTitle("Converter XML et JSON");
	    this.setSize(800, 600);
	    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    this.setLocationRelativeTo(null);
		this.setContentPane(pan);
		this.setVisible(true);
		
		
		
		
		etat = Etat.INIT_XML_JSON;
		gestionEtat(etat);
		
	}
	
	public void initComponents(){
		//Filtre de fichiers
		xmlFilter = new CustomFileFilter("XML", new String[]{"xml", "html", "xhtml"});
		jsonFilter =new CustomFileFilter("JSON", "json");
		
		
		//Boutons
		valider = new JButton("Valider");
		valider.setSize(30, 10);
		convertir = new JButton("Convertir");
		convertir.setSize(30, 10);
		reset = new JButton("reset");
		fermer = new JButton("fermer");
		parcourir = new JButton("Parcourir");
		sensXMLtoJSON = new JRadioButton("XML to JSON", true);
		sensJSONtoXML = new JRadioButton("JSON to XML", false);
		
		//JLabel
		inPut = new JLabel("Ficher en entrée");
		outPut = new JLabel("Fichier en sortie");
		
		//Panel Input
		panInPut = new JPanel();
		panInPut.setLayout(new BoxLayout(panInPut, BoxLayout.PAGE_AXIS));
		panInPut.add(inPut);
		panInPut.add(parcourir);
		panInPut.add(valider);
		
		
		//Panel Output
		panOutPut = new JPanel();
		panOutPut.setLayout(new BoxLayout(panOutPut, BoxLayout.PAGE_AXIS));
		panOutPut.add(outPut);
		panOutPut.add(convertir);

		
		/*
		 * *Création et gestion du Panel Général
		 */
		pan = new JPanel();
		pan.setLayout(new BorderLayout());
		pan.add(panOutPut, BorderLayout.EAST);
		pan.add(reset, BorderLayout.SOUTH);
		pan.add(fermer, BorderLayout.SOUTH);
		pan.add(sensXMLtoJSON, BorderLayout.SOUTH);
		pan.add(sensJSONtoXML, BorderLayout.SOUTH);
		pan.add(panInPut, BorderLayout.WEST);
		pan.setVisible(true);
		
		
		
		
		//Les listeners
		parcourir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                parcourirActionPerformed(evt);
            }
        });
		
		
		
		
		
	}
	
	private void parcourirActionPerformed(ActionEvent evt) {
		SelecteurDeFichier selct = new SelecteurDeFichier(xmlFilter);
		selct.setSize(300, 300);
		selct.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
	    selct.setLocationRelativeTo(null);
	    selct.setResizable(false);
		
	}
	public void gestionEtat(Etat e){
		
		switch(e){
		case INIT_JSON_XML: break;
		case INIT_XML_JSON: break;
		case OUTPUT_JSON_XML: break;
		case OUTPUT_XML_JSON: break;
		case VIEW_JSON_XML: break;
		case VIEW_XML_JSON: break;
		}
		
	}
	


}
