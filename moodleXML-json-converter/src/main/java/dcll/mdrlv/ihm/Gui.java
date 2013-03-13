package dcll.mdrlv.ihm;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.log4j.Logger;
import org.xml.sax.SAXException;

import dcll.mdrlv.jsontoxml.JsonToXmlConverter;
import dcll.mdrlv.tools.FileConformity;
import dcll.mdrlv.tools.OS;
import dcll.mdrlv.tools.Tools;
import dcll.mdrlv.xmltojson.XmlToJsonByDOM;
import dcll.mdrlv.xmltojson.XmlToJsonConverter;

/**
 *
 * @author Emilien DUBOIS, Romain LECHIEN, Francois MANCIET, Axel ROBERT, David
 *         VILLARD.
 *
 *         Interface permettant de convertir un moodle xml en JSON et un JSON en
 *         XML
 */
public class Gui extends javax.swing.JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2035510316925047349L;

	/**
	 * etat: Etat de le fenêtre.
	 */
	private Etat etat;

	/**
	 * xmlFilter : filtre pour les fichiers en entrée
	 * utilisé dans le SélecteurDeFichier dans l'état INIT_XML_JSON.
	 */
	private CustomFileFilter xmlFilter;

	/**
	 * jsonFilter : filtre pour les fichiers en entrée
	 * utilisé dans le SélecteurDeFichier dans l'état INIT_JSON_XML.
	 */
	private CustomFileFilter jsonFilter;

	/**
	 * selct : SelecteurDeFichier utilisé par le bouton ParcourirIN
	 * dans les Etats INIT.
	 */
	private SelecteurDeFichier selct;


	/**
	 * selctD : SelecteurDeDossier utilisé par le bouton ParcourirOut
	 * Dans les Etats Out.
	 */
	private SelecteurDeDossier selctD;

	/**
	 * sync : Object permettant de synchroniser les scrollbar
	 * des 2 textArea.
	 */
	private Synchronizer sync;

	/**
	 * xmlConverter : Convertisseur de fichier xml et json.
	 */
	private XmlToJsonConverter xmlConverter;

	/**
	 * jsonConverter : Convertisseur de fichier json en xml.
	 */
	private JsonToXmlConverter jsonConverter;

	/**
	 * xmlByDomConverter : Convertisseur de fichier xml et json.
	 * VIA DOM
	 */
	private XmlToJsonByDOM xmlByDomConverter;

	/**
	 * LOOGER : permet d'écrire dans le journal(.log).
	 */
	private static Logger lOGGER = Logger.getLogger(Gui.class);

	// Variables declaration - do not modify


	/**
	 * 
	 */
	private javax.swing.ButtonGroup buttonGroupWAY;

	/**
	 * 
	 */
	private javax.swing.JRadioButton jRadioViaDOM;
	
	/**
	 * 
	 */
	private javax.swing.JRadioButton jRadioViaXSLT;
	/**
	 * bouton regroupant les 2 radiobouton
	 * xml_vers_json / json_vers_xml.
	 */
	private javax.swing.ButtonGroup groupSensConverter;

	/**
	 * bouton lançant la convertion du fichier en entrée.
	 */
	private javax.swing.JButton jButtonConvertir;

	/**
	 * bouton permettant de quitter l'application.
	 */
	private javax.swing.JButton jButtonFermer;

	/**
	 * bouton permettant de chercher un fichier à convertir.
	 */
	private javax.swing.JButton jButtonParcourirIN;

	
	/**
	 * bouton permettant de chercher un dossier
	 * où envoyer le fichier une fois converti.
	 */
	private javax.swing.JButton jButtonParcourirOut;

	/**
	 * bouton permettant de nettoyer la fenêtre
	 * pour pouvoir effectuer une nouvelle convertion.
	 */
	private javax.swing.JButton jButtonReset;

	/**
	 * bouton permettant de valider la sélection du fichier en entrée.
	 */
	private javax.swing.JButton jButtonValider;

	/**
	 * Label affichant le type de fichier en entrée.
	 */
	private javax.swing.JLabel jLabelFichierIN;

	/**
	 * Label affichant le type de fichier en sortie.
	 */
	private javax.swing.JLabel jLabelFichierOut;

	/**
	 * Panel comptenant les boutons : ParcourirIN, Valider
	 * et le TextField pathIN.
	 */
	private javax.swing.JPanel jPanel1;

	/**
	 * Panel comptenant les boutons : ParcourirOut, Convertir
	 * et le TextField pathOut.
	 */
	private javax.swing.JPanel jPanel2;

	/**
	 * Radiobouton définissant le sens de conversion JSON -> XML.
	 */
	private javax.swing.JRadioButton jRadioButtonJSONtoXML;

	/**
	 * RadioBouton définissant le sens de la conversion XML -> JSON.
	 */
	private javax.swing.JRadioButton jRadioButtonXMLtoJSON;

	/**
	 * ScrollPane comprenant le TextArea1.
	 */
	private javax.swing.JScrollPane jScrollPane1;

	/**
	 * ScrollPane Comprenant le TextArea2.
	 */
	private javax.swing.JScrollPane jScrollPane2;

	/**
	 * TextArea permettant d'afficher le fichier en entrée.
	 */
	private javax.swing.JTextArea jTextArea1;

	/**
	 * TextArea permettant d'afficher le fichier en sortie.
	 */
	private javax.swing.JTextArea jTextArea2;

	/**
	 * TextField permettant d'afficher l'adresse
	 * absolue du fichier en entrée.
	 */
	private javax.swing.JTextField jTextFieldPathIN;

	/**
	 * TextField permettant d'afficher l'adresse
	 * absolue du fichier en sortie.
	 */
	private javax.swing.JTextField jTextFieldPathOut;

	/**
	 * String permettant d'enregistrer les délimiteur de chaque OS
	 * pour les adresse de fichier/dossier.
	 */
	private String delimiteur;

	// End of variables declaration

	/** Creates new form ConverterGui. */
	public Gui() {
		super();
		initComponents();
		this.setTitle("Converter XML et JSON");

		this.setSize(600, 550);
		this.setResizable(false);
		etat = Etat.INIT_XML_JSON;
		gestionEtat(etat);
	}

	/**
	 * This method is called from within the constructor
	 *  to initialize the form.
	 * WARNING: Do NOT modify this code.
	 * The content of this method is always
	 * regenerated by the Form Editor.
	 */
	@SuppressWarnings("unchecked")
	// <editor-fold defaultstate="collapsed" desc="Generated Code">
	/*
	 * Interface réalisée avec Netbeans, la méthode fait plus de ligne que
	 * recommandé et les lignes font plus de 80 caracteres.
	 */
	private void initComponents() {

		groupSensConverter = new javax.swing.ButtonGroup();
		jRadioButtonXMLtoJSON = new javax.swing.JRadioButton();
		jRadioButtonJSONtoXML = new javax.swing.JRadioButton();
		jPanel1 = new javax.swing.JPanel();
		jButtonParcourirIN = new javax.swing.JButton();
		jButtonValider = new javax.swing.JButton();
		jTextFieldPathIN = new javax.swing.JTextField();
		jPanel2 = new javax.swing.JPanel();
		jButtonParcourirOut = new javax.swing.JButton();
		jTextFieldPathOut = new javax.swing.JTextField();
		jButtonConvertir = new javax.swing.JButton();
		jButtonReset = new javax.swing.JButton();
		jButtonFermer = new javax.swing.JButton();
		jLabelFichierIN = new javax.swing.JLabel();
		jLabelFichierOut = new javax.swing.JLabel();
		jScrollPane1 = new javax.swing.JScrollPane();
		jTextArea1 = new javax.swing.JTextArea();
		jScrollPane2 = new javax.swing.JScrollPane();
		jTextArea2 = new javax.swing.JTextArea();

		xmlConverter = new XmlToJsonConverter("xmltojsonml.xslt");
		jsonConverter = new JsonToXmlConverter();
		jsonConverter = new JsonToXmlConverter();
		xmlByDomConverter = new XmlToJsonByDOM();


		if (Tools.determineOS() == OS.WINDOWS_OS) {
			delimiteur = "\\";
		} else {
			delimiteur = "/";
		}

		jTextArea1.setEditable(false);
		jTextArea2.setEditable(false);

		sync = new Synchronizer(jScrollPane1, jScrollPane2);
		jScrollPane1.getVerticalScrollBar().addAdjustmentListener(sync);
		jScrollPane1.getHorizontalScrollBar().
			addAdjustmentListener(sync);
		jScrollPane2.getVerticalScrollBar().
			addAdjustmentListener(sync);
		jScrollPane2.getHorizontalScrollBar().
			addAdjustmentListener(sync);

		setDefaultCloseOperation(
				javax.swing.WindowConstants.EXIT_ON_CLOSE);

		// Filtre de fichiers
		xmlFilter = new CustomFileFilter(
				"XML", new String[] {"xml", "html",
				"xhtml" });
		jsonFilter = new CustomFileFilter("JSON", "json");

		groupSensConverter.add(jRadioButtonXMLtoJSON);
		jRadioButtonXMLtoJSON.setSelected(true);
		jRadioButtonXMLtoJSON.setText("XML vers JSON");
		jRadioButtonXMLtoJSON
				.addActionListener(
						new java.awt.
						event.ActionListener() {
					public void actionPerformed(
							final java.awt.
							event.ActionEvent evt) {
				jRadioButtonXMLtoJSONActionPerformed(evt);
					}
				});

		groupSensConverter.add(jRadioButtonJSONtoXML);
		jRadioButtonJSONtoXML.setText("JSON vers XML");
		jRadioButtonJSONtoXML
				.addActionListener(
						new java.awt.
						event.ActionListener() {
					public void actionPerformed(
							final java.awt.
							event.ActionEvent evt) {
				jRadioButtonJSONtoXMLActionPerformed(evt);
					}
				});

		jPanel1.setBorder(
				new javax.swing.border.
				LineBorder(new java.awt.Color(
				0, 0, 0), 1, true));

		jButtonParcourirIN.setText("Parcourir");
		jButtonParcourirIN
				.addActionListener(
						new java.awt.event.
						ActionListener() {
					public void actionPerformed(
							final java.awt.event.
							ActionEvent evt) {
				jButtonParcourirINActionPerformed(evt);
					}
				});

		jButtonValider.setText("Valider");
		jButtonValider.addActionListener(
				new java.awt.event.ActionListener() {
			public void actionPerformed(
					final java.awt.event.ActionEvent evt) {
				jButtonValiderActionPerformed(evt);
			}
		});


		final javax.swing.GroupLayout jPanel1Layout =
				new javax.swing.GroupLayout(jPanel1);
		jPanel1.setLayout(jPanel1Layout);
		jPanel1Layout
				.setHorizontalGroup(jPanel1Layout
						.createParallelGroup(
				javax.swing.GroupLayout.Alignment.LEADING)
						.addGroup(
								jPanel1Layout
							.createSequentialGroup()
								.addGroup(
								jPanel1Layout
							.createParallelGroup(
								javax.swing.
								GroupLayout.Alignment.LEADING)
														.addGroup(
																jPanel1Layout
																		.createSequentialGroup()
																		.addContainerGap()
																		.addComponent(
																				jTextFieldPathIN,
																				javax.swing.GroupLayout.DEFAULT_SIZE,
																				269,
																				Short.MAX_VALUE))
														.addGroup(
																jPanel1Layout
																		.createSequentialGroup()
																		.addGap(110,
																				110,
																				110)
																		.addComponent(
																				jButtonValider,
																				javax.swing.GroupLayout.PREFERRED_SIZE,
																				75,
																				javax.swing.GroupLayout.PREFERRED_SIZE)
																		.addGap(31,
																				31,
																				31))
														.addGroup(
																jPanel1Layout
																		.createSequentialGroup()
																		.addGap(106,
																				106,
																				106)
																		.addComponent(
																				jButtonParcourirIN)))
										.addContainerGap()));
		jPanel1Layout
				.setVerticalGroup(jPanel1Layout
						.createParallelGroup(
								javax.swing.GroupLayout.Alignment.LEADING)
						.addGroup(
								javax.swing.GroupLayout.Alignment.TRAILING,
								jPanel1Layout
										.createSequentialGroup()
										.addContainerGap(12, Short.MAX_VALUE)
										.addComponent(jButtonParcourirIN)
										.addPreferredGap(
												javax.swing.LayoutStyle.ComponentPlacement.RELATED)
										.addComponent(
												jTextFieldPathIN,
												javax.swing.GroupLayout.PREFERRED_SIZE,
												javax.swing.GroupLayout.DEFAULT_SIZE,
												javax.swing.GroupLayout.PREFERRED_SIZE)
										.addPreferredGap(
												javax.swing.LayoutStyle.ComponentPlacement.RELATED)
										.addGroup(
												jPanel1Layout
														.createParallelGroup(
																javax.swing.GroupLayout.Alignment.BASELINE)
														.addComponent(
																jButtonValider))
										.addGap(5, 5, 5)));

		jPanel2.setBorder(
				new javax.swing.border.
				LineBorder(new java.awt.Color(
				0, 0, 0), 1, true));

		jButtonParcourirOut.setText("Parcourir");
		jButtonParcourirOut
				.addActionListener(
						new java.awt.event.
						ActionListener() {
					public void actionPerformed(
							final java.awt.event.
							ActionEvent evt) {
			jButtonParcourirOutActionPerformed(evt);
					}
				});

		jButtonConvertir.setText("Convertir");
		jButtonConvertir.addActionListener(
				new java.awt.event.ActionListener() {
			public void actionPerformed(
					final java.awt.event.
					ActionEvent evt) {
				jButtonConvertirActionPerformed(evt);
			}
		});

		buttonGroupWAY = new javax.swing.ButtonGroup();

		jRadioViaXSLT = new javax.swing.JRadioButton();
		        jRadioViaDOM = new javax.swing.JRadioButton();

				 buttonGroupWAY.add(jRadioViaXSLT);
		        jRadioViaXSLT.setText("Via XSLT");
		        jRadioViaXSLT.addActionListener(
						new java.awt.
						event.ActionListener() {
					public void actionPerformed(
							final java.awt.
							event.ActionEvent evt) {
				jRadioViaXSLTActionPerformed(evt);
					}
				});

		        buttonGroupWAY.add(jRadioViaDOM);
		        jRadioViaDOM.setText("Via DOM");
		        jRadioViaDOM.addActionListener(
						new java.awt.
						event.ActionListener() {
					public void actionPerformed(
							final java.awt.
							event.ActionEvent evt) {
				jRadioViaDOMActionPerformed(evt);
					}
				});

		        final javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
		        jPanel2.setLayout(jPanel2Layout);
		        jPanel2Layout.setHorizontalGroup(
		            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
		            .addGroup(jPanel2Layout.createSequentialGroup()
		                .addContainerGap()
		                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
		                    .addGroup(jPanel2Layout.createSequentialGroup()
		                        .addComponent(jTextFieldPathOut, javax.swing.GroupLayout.DEFAULT_SIZE, 227, Short.MAX_VALUE)
		                        .addContainerGap())
		                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
		                        .addComponent(jButtonParcourirOut)
		                        .addGap(92, 92, 92))
		                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
		                        .addComponent(jRadioViaXSLT)
		                        .addGap(60, 60, 60)
		                        .addComponent(jRadioViaDOM)
		                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
		                        .addComponent(jButtonConvertir)
		                        .addContainerGap())))
		        );
		        jPanel2Layout.setVerticalGroup(
		            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
		            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
		                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
		                .addComponent(jButtonParcourirOut)
		                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
		                .addComponent(jTextFieldPathOut, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
		                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
		                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
		                    .addComponent(jButtonConvertir)
		                    .addComponent(jRadioViaXSLT)
		                    .addComponent(jRadioViaDOM))
		                .addGap(5, 5, 5))
		        );

		jButtonReset.setText("Reset");
		jButtonReset.addActionListener(
				new java.awt.event.ActionListener() {
			public void actionPerformed(
					final java.awt.event.ActionEvent evt) {
				jButtonResetActionPerformed(evt);
			}
		});

		jButtonFermer.setText("Fermer");
		jButtonFermer.addActionListener(
				new java.awt.event.ActionListener() {
			public void actionPerformed(
					final java.awt.event.ActionEvent evt) {
				jButtonFermerActionPerformed(evt);
			}
		});

		jLabelFichierIN.setText("Fichier XML");

		jLabelFichierOut.setText("Fichier JSON");

		jTextArea1.setColumns(20);
		jTextArea1.setRows(5);
		jScrollPane1.setViewportView(jTextArea1);

		jTextArea2.setColumns(20);
		jTextArea2.setRows(5);
		jScrollPane2.setViewportView(jTextArea2);

		final javax.swing.GroupLayout layout = new javax.swing.GroupLayout(
				getContentPane());
		getContentPane().setLayout(layout);
		layout.setHorizontalGroup(layout
				.createParallelGroup(
						javax.swing.GroupLayout.
						Alignment.LEADING)
				.addGroup(
						layout.createSequentialGroup()
								.addContainerGap()
								.addGroup(
										layout.createParallelGroup(
												javax.swing.GroupLayout.Alignment.LEADING)
												.addGroup(
														layout.createSequentialGroup()
																.addComponent(
																		jScrollPane1,
																		javax.swing.GroupLayout.PREFERRED_SIZE,
																		279,
																		javax.swing.GroupLayout.PREFERRED_SIZE)
																.addPreferredGap(
																		javax.swing.LayoutStyle.ComponentPlacement.RELATED)
																.addPreferredGap(
																		javax.swing.LayoutStyle.ComponentPlacement.RELATED)
																.addComponent(
																		jScrollPane2,
																		javax.swing.GroupLayout.DEFAULT_SIZE,
																		257,
																		Short.MAX_VALUE))
												.addGroup(
														layout.createSequentialGroup()
																.addGroup(
																		layout.createParallelGroup(
																				javax.swing.GroupLayout.Alignment.LEADING)
																				.addGroup(
																						layout.createParallelGroup(
																								javax.swing.GroupLayout.Alignment.TRAILING)
																								.addComponent(
																										jPanel1,
																										javax.swing.GroupLayout.DEFAULT_SIZE,
																										javax.swing.GroupLayout.DEFAULT_SIZE,
																										Short.MAX_VALUE)
																								.addGroup(
																										layout.createParallelGroup(
																												javax.swing.GroupLayout.Alignment.LEADING)
																												.addComponent(
																														jRadioButtonXMLtoJSON,
																														javax.swing.GroupLayout.Alignment.TRAILING)
																												.addComponent(
																														jButtonReset,
																														javax.swing.GroupLayout.PREFERRED_SIZE,
																														72,
																														javax.swing.GroupLayout.PREFERRED_SIZE)))
																				.addGroup(
																						layout.createSequentialGroup()
																								.addComponent(
																										jLabelFichierIN)
																								.addPreferredGap(
																										javax.swing.LayoutStyle.ComponentPlacement.RELATED)))
																.addGroup(
																		layout.createParallelGroup(
																				javax.swing.GroupLayout.Alignment.LEADING)
																				.addGroup(
																						layout.createSequentialGroup()
																								.addPreferredGap(
																										javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
																								.addGroup(
																										layout.createParallelGroup(
																												javax.swing.GroupLayout.Alignment.LEADING)
																												.addComponent(
																														jRadioButtonJSONtoXML)
																												.addComponent(
																														jPanel2,
																														javax.swing.GroupLayout.PREFERRED_SIZE,
																														javax.swing.GroupLayout.DEFAULT_SIZE,
																														javax.swing.GroupLayout.PREFERRED_SIZE)
																												.addComponent(
																														jLabelFichierOut)))
																				.addGroup(
																						layout.createSequentialGroup()
																								.addGap(20,
																										20,
																										20)
																								.addComponent(
																										jButtonFermer,
																										javax.swing.GroupLayout.PREFERRED_SIZE,
																										78,
																										javax.swing.GroupLayout.PREFERRED_SIZE)))))
								.addContainerGap()));
		layout.setVerticalGroup(layout
				.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(
						javax.swing.GroupLayout.Alignment.TRAILING,
						layout.createSequentialGroup()
								.addContainerGap()
								.addGroup(
										layout.createParallelGroup(
												javax.swing.GroupLayout.Alignment.BASELINE)
												.addComponent(jLabelFichierOut)
												.addComponent(jLabelFichierIN))
								.addPreferredGap(
										javax.swing.LayoutStyle.ComponentPlacement.RELATED)
								.addGroup(
										layout.createParallelGroup(
												javax.swing.GroupLayout.Alignment.LEADING)
												.addComponent(
														jScrollPane2,
														javax.swing.GroupLayout.DEFAULT_SIZE,
														168, Short.MAX_VALUE)
												.addComponent(
														jScrollPane1,
														javax.swing.GroupLayout.DEFAULT_SIZE,
														168, Short.MAX_VALUE))
								.addPreferredGap(
										javax.swing.LayoutStyle.ComponentPlacement.RELATED)
								.addGroup(
										layout.createParallelGroup(
												javax.swing.GroupLayout.Alignment.LEADING)
												.addComponent(
														jPanel2,
														javax.swing.GroupLayout.PREFERRED_SIZE,
														javax.swing.GroupLayout.DEFAULT_SIZE,
														javax.swing.GroupLayout.PREFERRED_SIZE)
												.addComponent(
														jPanel1,
														javax.swing.GroupLayout.PREFERRED_SIZE,
														javax.swing.GroupLayout.DEFAULT_SIZE,
														javax.swing.GroupLayout.PREFERRED_SIZE))
								.addPreferredGap(
										javax.swing.LayoutStyle.ComponentPlacement.RELATED)
								.addGroup(
										layout.createParallelGroup(
												javax.swing.GroupLayout.Alignment.BASELINE)
												.addComponent(
														jRadioButtonJSONtoXML)
												.addComponent(
														jRadioButtonXMLtoJSON))
								.addPreferredGap(
										javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
								.addGroup(
										layout.createParallelGroup(
												javax.swing.GroupLayout.Alignment.BASELINE)
												.addComponent(jButtonReset)
												.addComponent(jButtonFermer))
								.addContainerGap()));

		pack();
	} // </editor-fold>

	/**
	 * @return
	 */
	public final Etat getEtat() {
		return etat;
	}

	/**
	 * @return
	 */
	public final CustomFileFilter getXmlFilter() {
		return xmlFilter;
	}

	/**
	 * @return
	 */
	public final CustomFileFilter getJsonFilter() {
		return jsonFilter;
	}

	/**
	 * @return
	 */
	public final SelecteurDeFichier getSelct() {
		return selct;
	}

	/**
	 * @return
	 */
	public final SelecteurDeDossier getSelctD() {
		return selctD;
	}

	/**
	 * @return
	 */
	public final Synchronizer getSync() {
		return sync;
	}

	/**
	 * @return
	 */
	public final XmlToJsonConverter getXmlConverter() {
		return xmlConverter;
	}

	/**
	 * @return
	 */
	public final JsonToXmlConverter getJsonConverter() {
		return jsonConverter;
	}

	/**
	 * @return
	 */
	public final XmlToJsonByDOM getXmlByDomConverter() {
		return xmlByDomConverter;
	}

	/**
	 * @return
	 */
	public final javax.swing.ButtonGroup getButtonGroupWAY() {
		return buttonGroupWAY;
	}

	/**
	 * @return
	 */
	public final javax.swing.JRadioButton getjRadioViaDOM() {
		return jRadioViaDOM;
	}

	/**
	 * @return
	 */
	public final javax.swing.JRadioButton getjRadioViaXSLT() {
		return jRadioViaXSLT;
	}

	/**
	 * @return
	 */
	public final javax.swing.ButtonGroup getgroupSensConverter() {
		return groupSensConverter;
	}

	/**
	 * @return
	 */
	public final javax.swing.JButton getjButtonConvertir() {
		return jButtonConvertir;
	}

	/**
	 * @return
	 */
	public final javax.swing.JButton getjButtonFermer() {
		return jButtonFermer;
	}

	/**
	 * @return
	 */
	public final javax.swing.JButton getjButtonParcourirIN() {
		return jButtonParcourirIN;
	}

	/**
	 * @return
	 */
	public final javax.swing.JButton getjButtonParcourirOut() {
		return jButtonParcourirOut;
	}

	/**
	 * @return
	 */
	public final javax.swing.JButton getjButtonReset() {
		return jButtonReset;
	}

	/**
	 * @return
	 */
	public final javax.swing.JButton getjButtonValider() {
		return jButtonValider;
	}

	/**
	 * @return
	 */
	public final javax.swing.JLabel getjLabelFichierIN() {
		return jLabelFichierIN;
	}

	/**
	 * @return
	 */
	public final javax.swing.JLabel getjLabelFichierOut() {
		return jLabelFichierOut;
	}

	/**
	 * @return
	 */
	public final javax.swing.JPanel getjPanel1() {
		return jPanel1;
	}

	/**
	 * @return
	 */
	public final javax.swing.JPanel getjPanel2() {
		return jPanel2;
	}

	/**
	 * @return
	 */
	public final javax.swing.JRadioButton getjRadioButtonJSONtoXML() {
		return jRadioButtonJSONtoXML;
	}

	/**
	 * @return
	 */
	public final javax.swing.JRadioButton getjRadioButtonXMLtoJSON() {
		return jRadioButtonXMLtoJSON;
	}

	/**
	 * @return
	 */
	public final javax.swing.JScrollPane getjScrollPane1() {
		return jScrollPane1;
	}

	/**
	 * @return
	 */
	public final javax.swing.JScrollPane getjScrollPane2() {
		return jScrollPane2;
	}

	/**
	 * @return
	 */
	public final javax.swing.JTextArea getjTextArea1() {
		return jTextArea1;
	}

	/**
	 * @return
	 */
	public final javax.swing.JTextArea getjTextArea2() {
		return jTextArea2;
	}

	/**
	 * @return
	 */
	public final javax.swing.JTextField getjTextFieldPathIN() {
		return jTextFieldPathIN;
	}

	/**
	 * @return
	 */
	public final javax.swing.JTextField getjTextFieldPathOut() {
		return jTextFieldPathOut;
	}

	/**
	 * @return
	 */
	public final String getDelimiteur() {
		return delimiteur;
	}
	/**
	 * @param etat
	 */
	public final void setEtat(final Etat etat) {
		this.etat = etat;
	}

	/**
	 * @param xmlFil
	 */
	public final void setXmlFilter(
			final CustomFileFilter xmlFil) {
		this.xmlFilter = xmlFil;
	}

	/**
	 * @param jsonFil
	 */
	public final void setJsonFilter(
			final CustomFileFilter jsonFil) {
		this.jsonFilter = jsonFil;
	}

	/**
	 * @param sel
	 */
	public final void setSelct(
			final SelecteurDeFichier sel) {
		this.selct = sel;
	}

	/**
	 * @param selc
	 */
	public final void setSelctD(
			final SelecteurDeDossier selc) {
		this.selctD = selc;
	}

	/**
	 * @param buttonGrou
	 */
	public final void setButtonGroupWAY(
			final javax.swing.ButtonGroup buttonGrou) {
		this.buttonGroupWAY = buttonGrou;
	}

	/**
	 * @param jRadioVia
	 */
	public final void setjRadioViaDOM(
			final javax.swing.JRadioButton jRadioVia) {
		this.jRadioViaDOM = jRadioVia;
	}

	/**
	 * @param jRadioViaX
	 */
	public final void setjRadioViaXSLT(
			final javax.swing.JRadioButton jRadioViaX) {
		this.jRadioViaXSLT = jRadioViaX;
	}

	/**
	 * @param groupSensConvert
	 */
	public final void setgroupSensConverter(
			final javax.swing.ButtonGroup groupSensConvert) {
		this.groupSensConverter = groupSensConvert;
	}

	/**
	 * @param jButtonConvert
	 */
	public final void setjButtonConvertir(
			final javax.swing.JButton jButtonConvert) {
		this.jButtonConvertir = jButtonConvert;
	}

	/**
	 * @param jRadioButtonJSONtoX
	 */
	public final void setjRadioButtonJSONtoXML(
			final javax.swing.JRadioButton jRadioButtonJSONtoX) {
		this.jRadioButtonJSONtoXML = jRadioButtonJSONtoX;
	}

	/**
	 * @param jRadioButtonXMLtoJ
	 */
	public final void setjRadioButtonXMLtoJSON(
			final javax.swing.JRadioButton jRadioButtonXMLtoJ) {
		this.jRadioButtonXMLtoJSON = jRadioButtonXMLtoJ;
	}

	/**
	 * @param syn
	 */
	public final void setSync(final Synchronizer syn) {
		this.sync = syn;
	}

	/**
	 * @param xmlConvert
	 */
	public final void setXmlConverter(
			final XmlToJsonConverter xmlConvert) {
		this.xmlConverter = xmlConvert;
	}

	/**
	 * @param jsonConvert
	 */
	public final void setJsonConverter(
			final JsonToXmlConverter jsonConvert) {
		this.jsonConverter = jsonConvert;
	}

	/**
	 * @param xmlByDomConvert
	 */
	public final void setXmlByDomConverter(
			final XmlToJsonByDOM xmlByDomConvert) {
		this.xmlByDomConverter = xmlByDomConvert;
	}


	/**
	 * @return
	 */
	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	/**
	 * @param jButtonFerm
	 */
	public final void setjButtonFermer(
			final javax.swing.JButton jButtonFerm) {
		this.jButtonFermer = jButtonFerm;
	}
	
	/**
	 * @return
	 */
	public static Logger getlOGGER() {
		return lOGGER;
	}

	/**
	 * @param lOGGE
	 */
	public static void setlOGGER(
			final Logger lOGGE) {
		Gui.lOGGER = lOGGE;
	}


	/**
	 * @param jButtonParcourir
	 */
	public final void setjButtonParcourirIN(
			final javax.swing.JButton jButtonParcourir) {
		this.jButtonParcourirIN = jButtonParcourir;
	}

	/**
	 * @param jButtonParcourir
	 */
	public final void setjButtonParcourirOut(
			final javax.swing.JButton jButtonParcourir) {
		this.jButtonParcourirOut = jButtonParcourir;
	}

	/**
	 * @param jButtonRes
	 */
	public final void setjButtonReset(
			final javax.swing.JButton jButtonRes) {
		this.jButtonReset = jButtonRes;
	}

	/**
	 * @param jButtonValid
	 */
	public final void setjButtonValider(
			final javax.swing.JButton jButtonValid) {
		this.jButtonValider = jButtonValid;
	}

	/**
	 * @param jLabelFichier
	 */
	public final void setjLabelFichierIN(
			final javax.swing.JLabel jLabelFichier) {
		this.jLabelFichierIN = jLabelFichier;
	}

	/**
	 * @param jLabelFichier
	 */
	public final void setjLabelFichierOut(
			final javax.swing.JLabel jLabelFichier) {
		this.jLabelFichierOut = jLabelFichier;
	}

	/**
	 * @param jPanel
	 */
	public final void setjPanel1(
			final javax.swing.JPanel jPanel) {
		this.jPanel1 = jPanel;
	}

	/**
	 * @param jPanel
	 */
	public final void setjPanel2(
			final javax.swing.JPanel jPanel) {
		this.jPanel2 = jPanel;
	}

	/**
	 * @param jScrollPane
	 */
	public final void setjScrollPane1(
			final javax.swing.JScrollPane jScrollPane) {
		this.jScrollPane1 = jScrollPane;
	}

	/**
	 * @param jScrollPane
	 */
	public final void setjScrollPane2(
			final javax.swing.JScrollPane jScrollPane) {
		this.jScrollPane2 = jScrollPane;
	}

	/**
	 * @param jTextArea
	 */
	public final void setjTextArea1(
			final javax.swing.JTextArea jTextArea) {
		this.jTextArea1 = jTextArea;
	}

	/**
	 * @param jTextArea
	 */
	public final void setjTextArea2(
			final javax.swing.JTextArea jTextArea) {
		this.jTextArea2 = jTextArea;
	}

	/**
	 * @param jTextFieldPath
	 */
	public final void setjTextFieldPathIN(
			final javax.swing.JTextField jTextFieldPath) {
		this.jTextFieldPathIN = jTextFieldPath;
	}

	/**
	 * @param jTextFieldPath
	 */
	public final void setjTextFieldPathOut(
			final javax.swing.JTextField jTextFieldPath) {
		this.jTextFieldPathOut = jTextFieldPath;
	}

	/**
	 * @param delimite
	 */
	public final void setDelimiteur(
			final String delimite) {
		this.delimiteur = delimite;
	}

	/**
	 * Gestionnaire d'evenements sur le bouton Valider.
	 * @param evt
	 * L'ActionEvent contenant les informations sur l'evenement.
	 */
	private void jButtonValiderActionPerformed(
			final java.awt.event.ActionEvent evt) {
		// TODO add your handling co
		final String mss1 = "Veuillez renseigner"
				+ " un chemin pour le "
				+ "fichier à convertir";
		
		final String mss2 = "Le fichier n'est "
				+ "n'a pas pu être "
				+ "trouvé";
		
		final String mss3 = "Bouton Valider : "
				+ "action interdite car";
		switch (etat) {
		case INIT_JSON_XML:
			if (jTextFieldPathIN.getText().equals("")) {
				JOptionPane.showMessageDialog(
						this, mss1);
			} else {
				try {
					afficherTextArea(
						jTextArea1,
						jTextFieldPathIN.getText());
				} catch (FileNotFoundException e) {
					JOptionPane.showMessageDialog(
							this, mss2);

					lOGGER.warn(mss2);
				}
				jTextFieldPathOut

				.setText(getEmplacementFile(
						jTextFieldPathIN.getText()));
				etat = Etat.OUTPUT_JSON_XML;
			}
			break;
		case INIT_XML_JSON:
			if (jTextFieldPathIN.getText().equals("")) {
				JOptionPane.showMessageDialog(
						this, mss1);
			} else {

				try {
					afficherTextArea(
						jTextArea1,
						jTextFieldPathIN.getText());
				} catch (FileNotFoundException e) {
					JOptionPane.showMessageDialog(
							this, mss2);

					lOGGER.warn(mss2);
				}

				jTextFieldPathOut.setText(
						getEmplacementFile(
								jTextFieldPathIN
								.getText()));
				etat = Etat.OUTPUT_XML_JSON;
			}
			break;
		case OUTPUT_JSON_XML:
			throw new RuntimeException(mss3
					+ " Etat OUTPUT_JSON_XML");
		case OUTPUT_XML_JSON:
			throw new RuntimeException(mss3
					+ "Etat OUTPUT_XML_JSON");
		case OUTPUT_XML_JSON_VIA_DOM:
			throw new RuntimeException(mss3
					+ "Etat OUTPUT_XML_JSON_VIA_DOM");
		case VIEW_JSON_XML:
			throw new RuntimeException(mss3
					+ "Etat VIEW_JSON_XML");
		case VIEW_XML_JSON:
			throw new RuntimeException(mss3
					+ " Etat VIEW_XML_JSON");

		default:
			break;
		}
		gestionEtat(etat);
	}

	/**
	 * Gestionnaire d'evenements sur le bouton Convertir.
	 * @param evt
	 * ActionEvent contenant les informations sur l'evenement
	 */
	private void jButtonConvertirActionPerformed(
			final java.awt.event.ActionEvent evt) {
		String mss = "Bouton Convertir : "
				+ "action interdite car ";
		
		switch (etat) {
		case INIT_JSON_XML:
			throw new RuntimeException(mss
					+ "Etat INIT_JSON_XML");
		case INIT_XML_JSON:
			throw new RuntimeException(mss
					+ "Etat INIT_XML_JSON");
		case OUTPUT_JSON_XML:
			if (jTextFieldPathOut.getText().equals("")) {
				JOptionPane.showMessageDialog(this,
						"Veuillez renseigner"
						+ " un chemin pour le "
						+ "fichier en sortie");
			} else {

				final String fichierOut = jTextFieldPathOut.getText();
				final String fichierIN = jTextFieldPathIN.getText();
				final FileConformity result
				= jsonConverter
					.fileValidation(
						new File(
								fichierIN));
				try {
					if (result == FileConformity.OK) {
						jsonConverter.convert(
							fichierIN,
							fichierOut);
						lOGGER.info(
							"Fin de la conversion :"
							+ " le ficher a bien "
							+ "été converti");
						etat = Etat.VIEW_JSON_XML;
						afficherTextArea(jTextArea2,
								fichierOut);
						gestionEtat(etat);
					} else {
						switch (result) {

					case WRONG_STANDARD:
						lOGGER.warn(
						  "Le fichier n'est "
						  + "pas valide "
						  + "(Standard JSON),"
						  + " conversion "
						  + "annulée.");
						JOptionPane
						.showMessageDialog(
							this,
							"Le fichier "
							+ "n'est pas "
							+ "valide "
							+ "(Standard "
							+ "JSON),"
							+ " conversion "
							+ "annulée.");
						break;
					case WRONG_MOODLE:

						lOGGER.warn(
						"Le ficher n'est"
							+ " pas valide "
							+ "(Standard "
							+ "MOODLE), "
							+ "conversion "
							+ "annulée.");
						JOptionPane
						.showMessageDialog(
							this,
							"Le ficher "
							+ "n'est pas "
							+ "valide "
							+ "(Standard "
							+ "MOODLE), "
							+ "conversion "
							+ "annulée.");
						break;
					default:
						break;
					}
						etat = Etat.INIT_JSON_XML;
						gestionEtat(etat);
						jTextFieldPathIN.setText(
							fichierIN);
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					etat = Etat.INIT_JSON_XML;
					gestionEtat(etat);
					jTextFieldPathIN.setText(fichierIN);
					JOptionPane.showMessageDialog(this,
							"Problème de création "
							+ "du fichier,"
							+ " impossible "
							+ "d'afficher");
					e.printStackTrace();

				}
			}
			break;
		case OUTPUT_XML_JSON:
			if (jTextFieldPathOut.getText().equals("")) {
				JOptionPane
						.showMessageDialog(this,
							"Veuillez renseigner"
						  + " un chemin pour le "
						  +	"fichier en sortie");
			} else {

				String fichierOut = jTextFieldPathOut.getText();
				String fichierIN = jTextFieldPathIN.getText();
				FileConformity result
				= xmlConverter
					.fileValidation(
						new File(
								fichierIN));
				try {
					if (result == FileConformity.OK) {
						xmlConverter.convert(
								fichierIN,
								fichierOut);
						lOGGER.info("Fin de la "
							+ "conversion,"
							+ " : le ficher a bien "
							+ "été converti.");
						afficherTextArea(jTextArea2,
								fichierOut);
						etat = Etat.VIEW_XML_JSON;
						gestionEtat(etat);
					} else {

						switch (result) {

						case WRONG_STANDARD:
							lOGGER.warn(
							  "Le fichier n'est "
							  + "pas valide "
							  + "(Standard XML),"
							  + " conversion "
							  + "annulée.");
							JOptionPane
							.showMessageDialog(
								this,
								"Le fichier "
								+ "n'est pas "
								+ "valide "
								+ "(Standard "
								+ "XML),"
								+ " conversion "
								+ "annulée.");
							break;
						case WRONG_MOODLE:

							lOGGER.warn(
							"Le ficher n'est"
								+ " pas valide "
								+ "(Standard "
								+ "MOODLE), "
								+ "conversion "
								+ "annulée.");
							JOptionPane
							.showMessageDialog(
								this,
								"Le ficher "
								+ "n'est pas "
								+ "valide "
								+ "(Standard "
								+ "MOODLE), "
								+ "conversion "
								+ "annulée.");
							break;
						default:
							break;
						}
						etat = Etat.INIT_XML_JSON;
						gestionEtat(etat);
						jTextFieldPathIN.
							setText(fichierIN);
					}
				} catch (IOException e) {
					lOGGER.warn("");
					// TODO Auto-generated catch block
					etat = Etat.INIT_XML_JSON;
					gestionEtat(etat);
					jTextFieldPathIN.setText(fichierIN);
					JOptionPane.showMessageDialog(this,
							"Problème de création "
							+ "du fichier,"
							+ " impossible "
							+ "d'afficher");
					e.printStackTrace();

				}
			}
			break;
		case OUTPUT_XML_JSON_VIA_DOM:

			if (jTextFieldPathOut.getText().equals("")) {
				JOptionPane
						.showMessageDialog(this,
							"Veuillez renseigner"
						  + " un chemin pour le "
						  +	"fichier en sortie");
			} else {
				String fichierOut = jTextFieldPathOut.getText();
				String fichierIN = jTextFieldPathIN.getText();
				FileConformity result
				= xmlConverter
					.fileValidation(
						new File(
					     fichierIN));
				try {
					if (result == FileConformity.OK) {
						xmlByDomConverter
						.convertXMLtoJSON(
								fichierIN,
								fichierOut);
						lOGGER.info("Fin de la "
							+ "conversion,"
							+ " : le ficher a bien "
							+ "été converti.");
						afficherTextArea(jTextArea2,
								fichierOut);
						etat = Etat.VIEW_XML_JSON;
						gestionEtat(etat);
					} else {

						switch (result) {

						case WRONG_STANDARD:
							lOGGER.warn(
							  "Le fichier n'est "
							  + "pas valide "
							  + "(Standard XML),"
							  + " conversion "
							  + "annulée.");
							JOptionPane
							.showMessageDialog(
								this,
								"Le fichier "
								+ "n'est pas "
								+ "valide "
								+ "(Standard "
								+ "XML),"
								+ " conversion "
								+ "annulée.");
							break;
						case WRONG_MOODLE:

							lOGGER.warn(
							"Le ficher n'est"
								+ " pas valide "
								+ "(Standard "
								+ "MOODLE), "
								+ "conversion "
								+ "annulée.");
							JOptionPane
							.showMessageDialog(
								this,
								"Le ficher "
								+ "n'est pas "
								+ "valide "
								+ "(Standard "
								+ "MOODLE), "
								+ "conversion "
								+ "annulée.");
							break;
						default:
							break;
						}
						etat = Etat.INIT_XML_JSON;
						gestionEtat(etat);
						jTextFieldPathIN.
							setText(fichierIN);
					}
				} catch (IOException e) {
					lOGGER.warn("");
				} catch (ParserConfigurationException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (SAXException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			break;

		case VIEW_JSON_XML:
			throw new RuntimeException("Bouton Convertir :"
					+ " action interdite "
					+ "car Etat VIEW_JSON_XML");
		case VIEW_XML_JSON:
			throw new RuntimeException("Bouton Convertir :"
					+ " action interdite "
					+ "car Etat VIEW_XML_JSON");
		default:
			break;
		}

	}

	/**
	 * Gestionnaire d'evenements sur le bouton Radio XML to JSON.
	 * @param evt
	 * L'ActionEvent contenant les informations sur l'evenement.
	 */
	private void jRadioButtonXMLtoJSONActionPerformed(
			final java.awt.event.ActionEvent evt) {
		switch (etat) {
		case INIT_JSON_XML:
			etat = Etat.INIT_XML_JSON;
			break;
		case INIT_XML_JSON:
			etat = Etat.INIT_XML_JSON;
			break;
		case OUTPUT_JSON_XML:
			throw new RuntimeException("Bouton Convertir : "
					+ "action interdite car "
					+ "Etat OUTPUT_JSON_XML");
		case OUTPUT_XML_JSON:
			throw new RuntimeException("Bouton Convertir : "
					+ "action interdite car "
					+ "Etat OUTPUT_XML_JSON");
		case OUTPUT_XML_JSON_VIA_DOM:
			throw new RuntimeException("Bouton Convertir : "
					+ "action interdite car "
					+ "Etat OUTPUT_XML_JSON_VIA_DOM");
		case VIEW_JSON_XML:
			throw new RuntimeException("Bouton Convertir : "
					+ "action interdite car"
					+ "Etat VIEW_JSON_XML");
		case VIEW_XML_JSON:
			throw new RuntimeException("Bouton Convertir : "
					+ "action interdite car "
					+ "Etat VIEW_XML_JSON");
		default:
			break;
		}
		gestionEtat(etat);
	}

	/**
	 * Gestionnaire d'evenements sur le bouton radio JSON to XML.
	 * @param evt
	 * L'ActionEvent contenant les informations sur l'evenement.
	 */
	private void jRadioButtonJSONtoXMLActionPerformed(
			final java.awt.event.ActionEvent evt) {
		switch (etat) {
		case INIT_JSON_XML:
			etat = Etat.INIT_JSON_XML;
			break;
		case INIT_XML_JSON:
			etat = Etat.INIT_JSON_XML;
			break;
		case OUTPUT_JSON_XML:
			throw new RuntimeException("Bouton Convertir : "
					+ "action interdite car "
					+ "Etat OUTPUT_JSON_XML");
		case OUTPUT_XML_JSON:
			throw new RuntimeException("Bouton Convertir : "
					+ "action interdite car "
					+ "Etat OUTPUT_XML_JSON");
		case OUTPUT_XML_JSON_VIA_DOM:
			throw new RuntimeException("Bouton Convertir : "
					+ "action interdite car "
					+ "Etat OUTPUT_XML_JSON");
		case VIEW_JSON_XML:
			throw new RuntimeException("Bouton Convertir : "
					+ "action interdite car "
					+ "Etat VIEW_JSON_XML");
		case VIEW_XML_JSON:
			throw new RuntimeException("Bouton Convertir : "
					+ "action interdite car "
					+ "Etat VIEW_XML_JSON");
		default:
			break;
		}
		gestionEtat(etat);
	}

	/**
	 * Gestionnaire d'evenements sur le bouton radio.
	 * de transformation de xml vers JSON par XSLT
	 * @param evt
	 * L'ActionEvent contenant les informations sur l'evenement.
	 */
	private void jRadioViaXSLTActionPerformed(
			final java.awt.event.ActionEvent evt) {
		switch (etat) {
		case INIT_JSON_XML:
			throw new RuntimeException("Bouton ViaXSLT : "
					+ "action interdite car "
					+ "Etat INIT_XML_JSON");
		case INIT_XML_JSON:
			throw new RuntimeException("Bouton ViaXSLT : "
					+ "action interdite car "
					+ "Etat INIT_JSON_XML");
		case OUTPUT_JSON_XML:
			throw new RuntimeException("Bouton ViaXSLT : "
					+ "action interdite car "
					+ "Etat OUTPUT_JSON_XML");
		case OUTPUT_XML_JSON_VIA_DOM :
			etat = Etat.OUTPUT_XML_JSON;
			break;
		case OUTPUT_XML_JSON:
			etat = Etat.OUTPUT_XML_JSON;
			break;
		case VIEW_JSON_XML:
			throw new RuntimeException("Bouton ViaXSLT : "
					+ "action interdite car"
					+ "Etat VIEW_JSON_XML");
		case VIEW_XML_JSON:
			throw new RuntimeException("Bouton ViaXSLT : "
					+ "action interdite car "
					+ "Etat VIEW_XML_JSON");
		default:
			break;
		}
		gestionEtat(etat);
	}

	/**
	 * Gestionnaire d'evenements sur le bouton radio.
	 * de transformation de xml vers JSON par DOM
	 * @param evt
	 * L'ActionEvent contenant les informations sur l'evenement.
	 */
	private void jRadioViaDOMActionPerformed(
			final java.awt.event.ActionEvent evt) {
		switch (etat) {
		case INIT_JSON_XML:
			throw new RuntimeException("Bouton ViaDOM : "
					+ "action interdite car "
					+ "Etat INIT_XML_JSON");
		case INIT_XML_JSON:
			throw new RuntimeException("Bouton ViaDOM : "
					+ "action interdite car "
					+ "Etat INIT_JSON_XML");
		case OUTPUT_JSON_XML:
			throw new RuntimeException("Bouton ViaDOM : "
					+ "action interdite car "
					+ "Etat OUTPUT_JSON_XML");
		case OUTPUT_XML_JSON_VIA_DOM :
			etat = Etat.OUTPUT_XML_JSON_VIA_DOM;
			break;
		case OUTPUT_XML_JSON:
			etat = Etat.OUTPUT_XML_JSON_VIA_DOM;
			break;
		case VIEW_JSON_XML:
			throw new RuntimeException("Bouton ViaXSLT : "
					+ "action interdite car"
					+ "Etat VIEW_JSON_XML");
		case VIEW_XML_JSON:
			throw new RuntimeException("Bouton ViaXSLT : "
					+ "action interdite car "
					+ "Etat VIEW_XML_JSON");
		default:
			break;
		}
		gestionEtat(etat);
	}


	/**
	 * Gestionnaire d'evenements sur le bouton Parcourir de l'entree.
	 * @param evt
	 * L'ActionEvent contenant les informations sur l'evenement.
	 */
	private void jButtonParcourirINActionPerformed(
			final java.awt.event.ActionEvent evt) {
		// TODO add your handling code here:
		final String mss = "Bouton Parcourir : "
				+ "action interdite car";
		final int nb = 300;
		switch (etat) {
		case INIT_JSON_XML:
			selct = new SelecteurDeFichier(jsonFilter, this);
			selct.setSize(nb, nb);
			selct.setVisible(true);
			break;
		case INIT_XML_JSON:
			selct = new SelecteurDeFichier(xmlFilter, this);
			selct.setSize(nb, nb);
			selct.setVisible(true);
			break;
		case OUTPUT_JSON_XML:
			throw new RuntimeException(mss
					+ " Etat OUTPUT_JSON_XML");
		case OUTPUT_XML_JSON:
			throw new RuntimeException(mss
					+ "Etat OUTPUT_XML_JSON");
		case OUTPUT_XML_JSON_VIA_DOM:
			throw new RuntimeException(mss
					+ "Etat OUTPUT_XML_JSON");
		case VIEW_JSON_XML:
			throw new RuntimeException(mss
					+ "Etat VIEW_JSON_XML");
		case VIEW_XML_JSON:
			throw new RuntimeException(mss
					+ "Etat VIEW_XML_JSON");
		default:
			break;
		}

	}


	/**
	 * Gestionnaire d'evenements sur le bouton Parcourir de la sortie.
	 * @param evt
	 * L'ActionEvent contenant les informations sur l'evenement.
	 */
	private void jButtonParcourirOutActionPerformed(
			final java.awt.event.ActionEvent evt) {
		// TODO add your handling code here:
		final int nb = 300;
		final String mss = "Bouton Parcourir : "
				+ "action interdite car ";
		String currentDir = "";
		currentDir = jTextFieldPathOut.getText();
		currentDir = currentDir.substring(0,
				currentDir.lastIndexOf(delimiteur));
		switch (etat) {
		case INIT_JSON_XML:
			throw new RuntimeException(mss
					+ "Etat INIT_JSON_XML");
		case INIT_XML_JSON:
			throw new RuntimeException(mss
					+ "Etat INIT_XML_JSON");
		case OUTPUT_JSON_XML:
			selctD = new SelecteurDeDossier(this, currentDir);
			selctD.setSize(nb, nb);
			selctD.setVisible(true);
			jTextFieldPathIN.setEditable(false);
			break;
		case OUTPUT_XML_JSON:
			selctD = new SelecteurDeDossier(this, currentDir);
			selctD.setSize(nb, nb);
			selctD.setVisible(true);
			jTextFieldPathIN.setEditable(false);
			break;

		case OUTPUT_XML_JSON_VIA_DOM:
			selctD = new SelecteurDeDossier(this, currentDir);
			selctD.setSize(nb, nb);
			selctD.setVisible(true);
			jTextFieldPathIN.setEditable(false);
			break;
		case VIEW_JSON_XML:
			throw new RuntimeException(mss
					+ "Etat VIEW_JSON_XML");
		case VIEW_XML_JSON:
			throw new RuntimeException(mss
					+ "Etat VIEW_XML_JSON");
		default:
			break;
		}

	}

	/**
	 * Gestionnaire d'evenements sur le bouton Fermer.
	 * @param evt
	 * L'ActionEvent contenant les informations sur l'evenement.
	 */
	private void jButtonFermerActionPerformed(
			final java.awt.event.ActionEvent evt) {
		int response = JOptionPane.showConfirmDialog(null,
				"Voulez vous vraiment quitter?", "Confirmation",
				JOptionPane.YES_NO_OPTION,
				JOptionPane.QUESTION_MESSAGE);
		if (response == JOptionPane.YES_OPTION) {
			System.exit(0);
		}
	}


	/**
	 * Gestionnaire d'evenements sur le bouton Reset.
	 * @param evt
	 * L'ActionEvent contenant les informations sur l'evenement.
	 */
	private void jButtonResetActionPerformed(
			final java.awt.event.ActionEvent evt) {
		int response = JOptionPane.showConfirmDialog(null,
				"Voulez vous vraiment "
				+ "réinitialiser le converter?",
				"Confirmation", JOptionPane.YES_NO_OPTION,
				JOptionPane.QUESTION_MESSAGE);
		if (response == JOptionPane.YES_OPTION) {
			switch (etat) {
			case INIT_JSON_XML:
			case INIT_XML_JSON:
			case OUTPUT_JSON_XML:
			case OUTPUT_XML_JSON:
			case OUTPUT_XML_JSON_VIA_DOM:
			case VIEW_JSON_XML:
			case VIEW_XML_JSON:
				etat = Etat.INIT_XML_JSON;
				break;
			default:
				break;
			}
		}
		gestionEtat(etat);
	}

	/**
	 * Methode permettant de changer le zone.
	 * de texte de sortie pour afficher le chemin
	 * @param s
	 * Le chemin absolu du fichier choisi
	 */
	public final void setPathIN(final String s) {
		jTextFieldPathIN.setText(s);
	}

	/**
	 * Methode permettant de changer le zone.
	 * de texte d'entrée pour afficher le chemin
	 * @param s
	 * Le chemin absolu du fichier choisi en sortie
	 */
	public final void setPathOut(final String s) {

		String fichier = jTextFieldPathIN.getText().substring(
				jTextFieldPathIN.getText().
					lastIndexOf(delimiteur),
				jTextFieldPathIN.getText().lastIndexOf("."));
		String extend = "";
		switch (etat) {
		case OUTPUT_XML_JSON:
		case OUTPUT_XML_JSON_VIA_DOM:
			extend = ".json";
			break;
		case OUTPUT_JSON_XML:
			extend = ".xml";
			break;
		default:
			break;
		}
		
		fichier += extend;
		jTextFieldPathOut.setText(s.concat(fichier));
	}


	/**
	 * @return la zone de texte correspondant 
	 * au chemin du fichier en entrée
	 */
	public final JTextField getPathIN() {
		return jTextFieldPathIN;
	}

	/**
	 * @return la zone  de texte correspondant
	 * au chemin du fichier en sortie
	 */
	public final JTextField getPathOut() {
		return jTextFieldPathOut;
	}

	/**
	 * Methode permettant d'afficher le contenu d'un.
	 * fichier dans le zone de texte correspondante
	 * @param txt
	 * la zone de texte à modifier
	 * @param file
	 * le fichier à lire et à afficher 
	 * dans la zone de texte
	 * @throws FileNotFoundException
	 * Exception correspondant à un fichier introuvable
	 */
	public final void afficherTextArea(
			final JTextArea txt,
			final String file)
			throws FileNotFoundException {

		FileReader flux = null;
		BufferedReader input = null;
		String str;
		txt.setText("");
		try {
			flux = new FileReader(file);

			input = new BufferedReader(flux);

			while ((str = input.readLine()) != null) {
				txt.append(str + '\n');
			}
		} catch (IOException e) {
			System.out.println("Impossible d'ouvrir le fichier : "
					+ e.toString());
		} finally {
			try {
				flux.close();
			} catch (IOException ex) {
				System.out.println("Impossible d'ouvrir "
						+ "le fichier : "
						+ ex.toString());
			}

			txt.setCaretPosition(0);
		}

	}

	/**
	 * A partir du nom du fichier en entrée, retourne une adresse par défaut.
	 * @param pathO : adresse du fichier en entrée
	 * @return : L'adresse par défaut du fichier en sortie
	 */
	public final String getEmplacementFile(final String pathO) {
		String extend = "";
		String fichier = "";
		String path = new File(pathO).getAbsolutePath();
		switch (etat) {
		case INIT_XML_JSON:
			extend = ".json";
			break;
		case INIT_JSON_XML:
			extend = ".xml";
			break;
		default:
			break;
		}
		// On récupère l'adresse absolue du dossier
		// On remonte d'un niveau comme "cd .."
		path = path.substring(0, path.lastIndexOf(delimiteur));
		// On récupère le nom du fichier
		// contenu dans l'adresse en paramètre
		fichier = pathO.substring(pathO.lastIndexOf(delimiteur),
				pathO.lastIndexOf("."));
		fichier += extend;
		// On return la concatenation de ces 3 variables
		return path.concat(fichier);
	}

	/**
	 * Methode permettant la mise à jour des éléments.
	 * graphiques de l'application
	 * @param e
	 * L'état en cours.
	 */
	public final void gestionEtat(final Etat e) {

		switch (e) {
		case INIT_JSON_XML:
			jTextArea1.setText("");
			jTextArea2.setText("");
			jTextFieldPathIN.setText("");
			jTextFieldPathOut.setText("");

			jButtonParcourirIN.setEnabled(true);
			jButtonValider.setEnabled(true);
			jTextFieldPathIN.setEnabled(true);
			jTextFieldPathIN.setEditable(true);

			jButtonParcourirOut.setEnabled(false);
			jTextFieldPathOut.setEnabled(false);
			jTextFieldPathOut.setEditable(false);
			jButtonConvertir.setEnabled(false);
			jButtonReset.setEnabled(true);
			jButtonFermer.setEnabled(true);

			jRadioButtonJSONtoXML.setEnabled(true);
			jRadioButtonXMLtoJSON.setEnabled(true);
			jRadioButtonJSONtoXML.setSelected(true);
			jRadioButtonXMLtoJSON.setSelected(false);
			jRadioViaDOM.setEnabled(false);
			jRadioViaXSLT.setEnabled(false);
			jRadioViaDOM.setSelected(false);
			jRadioViaXSLT.setSelected(true);

			jLabelFichierIN.setText("Fichier JSON");
			jLabelFichierOut.setText("Fichier XML");

			break;
		case INIT_XML_JSON:
			jTextArea1.setText("");
			jTextArea2.setText("");
			jTextFieldPathIN.setText("");
			jTextFieldPathOut.setText("");

			jButtonParcourirIN.setEnabled(true);
			jButtonValider.setEnabled(true);
			jTextFieldPathIN.setEnabled(true);
			jTextFieldPathIN.setEditable(true);

			jButtonParcourirOut.setEnabled(false);
			jTextFieldPathOut.setEnabled(false);
			jTextFieldPathOut.setEditable(false);
			jButtonConvertir.setEnabled(false);
			jButtonReset.setEnabled(true);
			jButtonFermer.setEnabled(true);

			jRadioButtonJSONtoXML.setEnabled(true);
			jRadioButtonXMLtoJSON.setEnabled(true);
			jRadioButtonJSONtoXML.setSelected(false);
			jRadioButtonXMLtoJSON.setSelected(true);
			jRadioViaDOM.setEnabled(false);
			jRadioViaXSLT.setEnabled(false);
			jRadioViaDOM.setSelected(false);
			jRadioViaXSLT.setSelected(true);

			jLabelFichierIN.setText("Fichier XML");
			jLabelFichierOut.setText("Fichier JSON");

			break;
		case OUTPUT_JSON_XML:
			jButtonParcourirIN.setEnabled(false);
			jButtonValider.setEnabled(false);
			jTextFieldPathIN.setEnabled(false);
			jTextFieldPathIN.setEditable(false);

			jButtonParcourirOut.setEnabled(true);
			jTextFieldPathOut.setEnabled(true);
			jTextFieldPathOut.setEditable(true);
			jButtonConvertir.setEnabled(true);
			jButtonReset.setEnabled(true);
			jButtonFermer.setEnabled(true);

			jRadioButtonJSONtoXML.setEnabled(false);
			jRadioButtonXMLtoJSON.setEnabled(false);
			jRadioViaDOM.setEnabled(false);
			jRadioViaXSLT.setEnabled(false);
			jRadioViaDOM.setSelected(false);
			jRadioViaXSLT.setSelected(true);

			break;
		case OUTPUT_XML_JSON:
			jButtonParcourirIN.setEnabled(false);
			jButtonValider.setEnabled(false);
			jTextFieldPathIN.setEnabled(false);
			jTextFieldPathIN.setEditable(false);

			jButtonParcourirOut.setEnabled(true);
			jTextFieldPathOut.setEnabled(true);
			jTextFieldPathOut.setEditable(true);
			jButtonConvertir.setEnabled(true);
			jButtonReset.setEnabled(true);
			jButtonFermer.setEnabled(true);

			jRadioButtonJSONtoXML.setEnabled(false);
			jRadioButtonXMLtoJSON.setEnabled(false);
			jRadioViaDOM.setEnabled(true);
			jRadioViaXSLT.setEnabled(true);
			jRadioViaDOM.setSelected(false);
			jRadioViaXSLT.setSelected(true);
			break;
		case OUTPUT_XML_JSON_VIA_DOM:
			jButtonParcourirIN.setEnabled(false);
			jButtonValider.setEnabled(false);
			jTextFieldPathIN.setEnabled(false);
			jTextFieldPathIN.setEditable(false);

			jButtonParcourirOut.setEnabled(true);
			jTextFieldPathOut.setEnabled(true);
			jTextFieldPathOut.setEditable(true);
			jButtonConvertir.setEnabled(true);
			jButtonReset.setEnabled(true);
			jButtonFermer.setEnabled(true);

			jRadioButtonJSONtoXML.setEnabled(false);
			jRadioButtonXMLtoJSON.setEnabled(false);
			jRadioViaDOM.setEnabled(true);
			jRadioViaXSLT.setEnabled(true);
			jRadioViaDOM.setSelected(true);
			jRadioViaXSLT.setSelected(false);
			break;
		case VIEW_JSON_XML:
			jButtonParcourirIN.setEnabled(false);
			jButtonValider.setEnabled(false);
			jTextFieldPathIN.setEnabled(false);
			jTextFieldPathIN.setEditable(false);

			jButtonParcourirOut.setEnabled(false);
			jTextFieldPathOut.setEnabled(false);
			jTextFieldPathOut.setEditable(false);
			jButtonConvertir.setEnabled(false);
			jButtonReset.setEnabled(true);
			jButtonFermer.setEnabled(true);

			jRadioButtonJSONtoXML.setEnabled(false);
			jRadioButtonXMLtoJSON.setEnabled(false);
			jRadioViaDOM.setEnabled(false);
			jRadioViaXSLT.setEnabled(false);
			break;
		case VIEW_XML_JSON:
			jButtonParcourirIN.setEnabled(false);
			jButtonValider.setEnabled(false);
			jTextFieldPathIN.setEnabled(false);
			jTextFieldPathIN.setEditable(false);

			jButtonParcourirOut.setEnabled(false);
			jTextFieldPathOut.setEnabled(false);
			jTextFieldPathOut.setEditable(false);
			jButtonConvertir.setEnabled(false);
			jButtonReset.setEnabled(true);
			jButtonFermer.setEnabled(true);

			jRadioButtonJSONtoXML.setEnabled(false);
			jRadioButtonXMLtoJSON.setEnabled(false);
			jRadioViaDOM.setEnabled(false);
			jRadioViaXSLT.setEnabled(false);
			break;

		default:
			break;
		}

	}

}
