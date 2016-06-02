package de.hdm.bankProject.report;

import java.io.Serializable;


/**
 * Spalte eines <code>Row</code>-Objekts. <code>Column</code>-Objekte implementieren das <code>Serializable</code>-Interface und k�nnen daher als Kopie z.B. vom Server an den Client �bertragen werden.
 * @see Row
 * @author  Thies
 */
public class Column implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * Der Wert eines Spaltenobjekts entspricht dem Zelleneintrag einer Tabelle.
	 * In dieser Realisierung handelt es sich um einen einfachen textuellen Wert.
	 */
	private String value = "";

	/**
	 * Konstruktor, der die Angabe eines Werts (Spalteneintrag) erzwingt.
	 * @param s der Wert, der durch das Column-Objekt dargestellt werden soll.
	 */
	public Column(String s) {
		this.value = s;
	}

	/**
     * Auslesen des Spaltenwerts.
     * @return  der Eintrag als String
     * @uml.property  name="value"
     */
	public String getValue() {
		return value;
	}

	/**
     * �berschreiben des aktuellen Spaltenwerts.
     * @param value  neuer Spaltenwert
     * @uml.property  name="value"
     */
	public void setValue(String value) {
		this.value = value;
	}

	/**
	 * Umwandeln des <code>Column</code>-Objekts in einen String.
	 * 
	 * @see java.lang.Object
	 */
	public String toString() {
		return this.value;
	}
}
