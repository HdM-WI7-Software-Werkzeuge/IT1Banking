/*
 * Erweiterbarer Mapper für alle persistente Daten des Bankprojekts.
 * Er stellt ein Cashing für die Datenobjekte zur Verfügung, so dass bei Datenbank-
 * zugriffen geprüft wird, ob das gesuchte Objekt schon existiert.
 */

package de.hdm.bankProject.db;

import java.util.Vector;

/**
 *
 * @author Christian Rathke
 */
public abstract class DataMapper<D> {
    
    public abstract void removeTable();
    
    public abstract void reCreateTable();
    
    public abstract void fillTable(String dataString);
    
    public abstract D findByKey(int id);
    
    public abstract Vector<D> findAll();
    
    public abstract D insert(D data);
    
    public abstract D update(D data);
    
    public abstract void delete(D data);
}
