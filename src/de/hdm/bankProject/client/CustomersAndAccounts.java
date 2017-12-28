/*
 * Created on 11.12.2004
 *
 */
package de.hdm.bankProject.client;

import java.awt.Component;
import java.util.Enumeration;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import de.hdm.bankProject.data.Account;
import de.hdm.bankProject.data.Customer;

/**
 * Struktur und Darstellung aller bekannten Kunden und Konten als Baum. In einer
 * realistischen Anwendung muesste dafuer Sorge getragen werden, dass nicht auf
 * alle (!) Datensaetze zugegriffen wird. Denkbar wuere z.B. das Einziehen einer
 * weiteren Ebene in die Baumstruktur, die alle Kunden nach Anfangsbuchstaben
 * auflistet. Dann wuerden die Datensaetze erst bemueht, wenn die entsprechende
 * Unterstruktur des Baumes sichtbar gemacht wird.
 * 
 * @author Christian Rathke
 */
public class CustomersAndAccounts {

	private DefaultMutableTreeNode rootNode = new DefaultMutableTreeNode();;
	private DefaultTreeModel treeModel = new DefaultTreeModel(rootNode);
	private JTree tree = new JTree();


	/**
	 * Erzeugen des Kunden-und-Konten-Baumes.
	 * 
	 * @param bi
	 * @return
	 */
	Component generateComponents(BankInterface bi) {
		tree.setRootVisible(false);
		tree.setShowsRootHandles(true);
		
		tree.setModel(treeModel);

		tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
		tree.addTreeSelectionListener(bi);

		JScrollPane treeView = new JScrollPane(tree);
		treeView.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

		return treeView;
	}
	
	void resetRoot() {
		rootNode.removeAllChildren();
		treeModel.reload();
	}

	/**
	 * Ergibt den im Baum zuletzt selektierten Knoten.
	 * 
	 * @return
	 */
	DefaultMutableTreeNode getSelectedNode() {
		return (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
	}
	
	/**
	 * Prüft, ob ein Knoten ein Customer-Knoten ist
	 */
	boolean isCustomerNode(DefaultMutableTreeNode node) {
		return (node != null) && (node.getUserObject() instanceof CustomerInfo);
	}
	
	/**
	 * Prüft, ob ein Knoten ein Acocunt-Knoten ist
	 */
	boolean isAccountNode(DefaultMutableTreeNode node) {
		return (node != null) && (node.getUserObject() instanceof AccountInfo);
	}

	/**
	 * Hinzufuegen eines einzelnen Kundenknotens mit allen Kontenknoten.
	 * 
	 * @param customer
	 */
	DefaultMutableTreeNode addCustomerTreeNode(CustomerInfo customer, Vector<AccountInfo> accounts) {
		DefaultMutableTreeNode customerNode = new DefaultMutableTreeNode(customer);
		if (isCustomerNode(getSelectedNode())) {
			treeModel.insertNodeInto(customerNode, rootNode,
					treeModel.getIndexOfChild(rootNode, getSelectedNode()) + 1);
		} else {
			rootNode.add(customerNode);
		}

		DefaultMutableTreeNode accountNode = null;
		for (int j = 0; j < accounts.size(); j++) {
			accountNode = new DefaultMutableTreeNode(accounts.elementAt(j));
			customerNode.add(accountNode);
		}

		treeModel.reload();
		return customerNode;
			
		
	}

	/**
	 * Hinzufuegen eines Kontenknotens unter dem aktuell selektierten Kundenknoten.
	 * 
	 * @param infoObject
	 */
	DefaultMutableTreeNode addAccountTreeNode(AccountInfo infoObject) {
		DefaultMutableTreeNode childNode = new DefaultMutableTreeNode(infoObject);
		if (isCustomerNode(getSelectedNode())) {
			getSelectedNode().add(childNode);
			treeModel.reload(getSelectedNode());
			
			selectNode(childNode);
		} else if (isAccountNode(getSelectedNode())) {
			((DefaultMutableTreeNode)getSelectedNode().getParent()).add(childNode);
			treeModel.reload(getSelectedNode().getParent());
			
			selectNode(childNode);
		}
		return childNode;
	}

	/**
	 * Ersetzen des <code>UserObject<code> fuer den aktuell selektierten
	 * Kundenknoten nach einer Modifikation des Kundennamens.
	 * 
	 * @param c
	 */
	void modifyCustomerTreeNode(Customer c) {
		if (isCustomerNode(getSelectedNode())) {
			CustomerInfo infoObject = (CustomerInfo)getSelectedNode().getUserObject();
			if (infoObject.getCustomer().equals(c)) {
				getSelectedNode().setUserObject(new CustomerInfo(c));
				treeModel.nodeChanged(getSelectedNode());
			}
		}
	}

	/**
	 * Entfernen eines Kundenknotes nach Loeschen des Kunden.
	 * 
	 * @param c
	 */
	void removeCustomerTreeNode(Customer c) {
		if (isCustomerNode(getSelectedNode())) {
			CustomerInfo infoObject = (CustomerInfo)getSelectedNode().getUserObject();
			if (infoObject.getCustomer().equals(c)) {
				treeModel.removeNodeFromParent(getSelectedNode());
			}
		}
	}

	/**
	 * Entfernen eines Kontoknotens nach Loeschen des Kontos.
	 * 
	 * @param a
	 */
	void removeAccountTreeNode(Account a) {
		if (isAccountNode(getSelectedNode())) {
			AccountInfo infoObject = (AccountInfo)getSelectedNode().getUserObject();
			if (infoObject.getAccount().equals(a)) {
				DefaultMutableTreeNode customerNode = (DefaultMutableTreeNode)getSelectedNode().getParent();
				treeModel.removeNodeFromParent(getSelectedNode());
				selectNode(customerNode);
			}
		}
	}

	/**
	 * Selektieren eins Kundenknotens fuer einen gefundenen Kunden. Die Methode
	 * vergleicht die Kunden-Ids miteinander, um den richtigen Baumknoten
	 * aufzufinden.
	 * 
	 * @param c
	 */
	void selectCustomerTreeNode(Customer c) {
		Enumeration<?> enumer = rootNode.children();
		DefaultMutableTreeNode node = null;
		while (enumer.hasMoreElements()) {
			node = (DefaultMutableTreeNode) enumer.nextElement();
			CustomerInfo ci = (CustomerInfo) node.getUserObject();
			if (ci.getCustomer().equals(c)) {
				selectNode(node);
				break;
			}
		}
	}
	
	
	/**
	 * Select a node by setting its tree selection path
	 * @param node
	 */
	
	void selectNode(DefaultMutableTreeNode node) {
		if (node != null) {
			tree.setSelectionPath(new TreePath(node.getPath()));
		}
	}
}
