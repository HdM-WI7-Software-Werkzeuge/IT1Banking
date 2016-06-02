package de.hdm.bankProject.report;

/**
 * Diese Klasse stellt einzelne Abs�tze dar. Der Absatzinhalt wird als String gespeichert. Der Anwender sollte in diesem Strinig keinerlei Formatierungssymbole einf�gen, da diese in der Regel zielformatspezifisch sind.
 * @author  Thies
 */
public class SimpleParagraph extends Paragraph {

	/**
	 * Inhalt des Absatzes.
	 */
	private String text = "";

	/**
	 * Dieser Konstruktor erm�glicht es, bereits bei Instantiierung von 
	 * <code>SimpleParagraph</code>-Objekten deren Inhalt angeben zu k�nnen.
	 * 
	 * @param value der Inhalt des Absatzes
	 */
	public SimpleParagraph(String value) {
		this.text = value;
	}

	/**
     * Auslesen des Inhalts.
     * @return  Inhalt als String
     * @uml.property  name="text"
     */
	public String getText() {
		return text;
	}

	/**
     * �berschreiben des Inhalts.
     * @param text  der neue Inhalt des Absatzes.
     * @uml.property  name="text"
     */
	public void setText(String text) {
		this.text = text;
	}

	/**
	 * Umwandeln des <code>SimpleParagraph</code>-Objekts in einen String.
	 */
	public String toString() {
		return this.text;
	}
}
