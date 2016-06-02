package de.hdm.bankProject.report;

/**
 * Reports ben�tigen die M�glichkeit, Text strukturiert abspeichern
 * zu k�nnen. Dieser Text kann sp�ter durch <code>ReportWriter</code> in verschiedene
 * Zielformate konvertiert werden. Die Verwendung der Klasse <code>String</code>
 * reicht hier nicht aus, da allein das Hinzuf�gen eines Zeilenumbruchs zur 
 * Markierung eines Absatzendes Kenntnisse �ber das Zielformat voraussetzt. Im
 * Falle einer rein textuellen Darstellung w�rde hierzu ein doppeltes "<code>\n</code>" 
 * gen�gen. Bei dem Zielformat HTML m�sste jedoch der gesamte Absatz in 
 * entsprechendes Markup eingef�gt werden.<p>
 * 
 * <code>Paragraph</code> ist Serializable, so das Objekte dieser Klasse durch 
 * das Netzwerk �bertragbar sind.
 * 
 * @see Report
 * @author Thies
 */
public abstract class Paragraph {

}
