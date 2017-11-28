/*
 * Erweiterbarer Mapper für alle persistente Daten des Bankprojekts.
 * Er stellt ein Cashing für die Datenobjekte zur Verfügung, so dass bei Datenbank-
 * zugriffen geprüft wird, ob das gesuchte Objekt schon existiert.
 */

package de.hdm.bankProject.db;

import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

/**
 *
 * @author Christian Rathke
 */
public abstract class DataMapper<D> {
    /**
     * Object cache für Speichern und Zugriff
     */
    private Map<Integer,D> cache = new HashMap<Integer,D>();

    protected D checkFor (int id) {
        D data = cache.get(id);
        if (data == null) return null;
        return data;
    }

    protected void remember(int id, D data) {
        cache.put(id, data);
    }

    protected void remove(int id) {
        cache.remove(id);
    }
    
    public abstract void removeTable();
    
    public abstract void reCreateTable();
    
    public abstract void fillTable(String dataString);
    
    public abstract D findByKey(int id);
    
    public abstract Vector<D> findAll();
    
    public abstract D insert(D data);
    
    public abstract D update(D data);
    
    public abstract void delete(D data);
}
