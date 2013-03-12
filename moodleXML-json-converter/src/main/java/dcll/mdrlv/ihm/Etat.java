package dcll.mdrlv.ihm;

/**
 * @author Emilien DUBOIS, Romain LECHIEN, Francois MANCIET,
 * Axel ROBERT, David VILLARD
 *
 */

public enum Etat {
	/** Liste des différents états de l'application.
	 * INIT_XML_JSON :
 	 * Etat initial d'une conversion XML to JSON
	 */
	INIT_XML_JSON,
	/** INIT_JSON_XML :.
	 * Etat initial d'une conversion JSON to XML
	 */
	INIT_JSON_XML,

	/** OUTPUT_XML_JSON :.
	 * Etat de choix du fichier en sortie (XML->JSON)
	 */
	OUTPUT_XML_JSON,

	/** OUTPUT_JSON_XML :.
	 * Etat de choix du fichier en sortie (JSON->XML)
	 */
	OUTPUT_JSON_XML,

	/** VIEW_XML_JSON :.
	 * Visionnage du résultat de la conversion(XML->JSON)
	 */
	VIEW_XML_JSON,

	/** VIEW_JSON_XML :.
	 * Visionnage du résultat de la conversion(JSON->XML)
	 */
	VIEW_JSON_XML
}
