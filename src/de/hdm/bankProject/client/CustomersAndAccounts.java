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
	private JTree tree;

	private DefaultMutableTreeNode selectedCustomerNode;
	private DefaultMutableTreeNode selectedAccountNode;

	/**
	 * Erzeugen des Kunden-und-Konten-Baumes.
	 * 
	 * @param bi
	 * @return
	 */
	Component generateComponents(BankInterface bi) {
		tree = new JTree();
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
	DefaultMutableTreeNode getLastSelectedPathComponent() {
		return (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
	}

	/**
	 * Hinzufuegen eines einzelnen Kundenknotens mit allen Kontenknoten.
	 * 
	 * @param customer
	 */
	DefaultMutableTreeNode addCustomerTreeNode(CustomerInfo customer, Vector<AccountInfo> accounts) {
		DefaultMutableTreeNode customerNode = new DefaultMutableTreeNode(customer);
		if (selectedCustomerNode != null) {
			treeModel.insertNodeInto(customerNode, rootNode,
					treeModel.getIndexOfChild(rootNode, selectedCustomerNode) + 1);
		} else {
			rootNode.add(customerNode);
		}

		DefaultMutableTreeNode accountNode = null;
		for (int j = 1; j < accounts.size(); j++) {
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
		if (selectedCustomerNode != null) {
			DefaultMutableTreeNode childNode = new DefaultMutableTreeNode(infoObject);
			selectedCustomerNode.add(childNode);
			setSelectedAccountNode(childNode);
			treeModel.reload(selectedCustomerNode);
			return childNode;
		}
		return null;
	}

	/**
	 * Ersetzen des <code>UserObject<code> fuer den aktuell selektierten
	 * Kundenknoten nach einer Modifikation des Kundennamens.
	 * 
	 * @param c
	 */
	void modifyCustomerTreeNode(Customer c) {
		if (selectedCustomerNode != null) {
			Object infoObject = selectedCustomerNode.getUserObject();
			if ((infoObject instanceof CustomerInfo) && ((CustomerInfo) infoObject).getCustomer().equals(c)) {
				selectedCustomerNode.setUserObject(new CustomerInfo(c));
				treeModel.nodeChanged(selectedCustomerNode);
			}
		}
	}

	/**
	 * Entfernen eines Kundenknotes nach Loeschen des Kunden.
	 * 
	 * @param c
	 */
	void removeCustomerTreeNode(Customer c) {
		if (selectedCustomerNode != null) {
			Object infoObject = selectedCustomerNode.getUserObject();
			if ((infoObject instanceof CustomerInfo) && ((CustomerInfo) infoObject).getCustomer().equals(c)) {
				treeModel.removeNodeFromParent(selectedCustomerNode);
				setSelectedCustomerNode(null);
			}
		}
	}

	/**
	 * Entfernen eines Kontoknotens nach Loeschen des Kontos.
	 * 
	 * @param a
	 */
	void removeAccountTreeNode(Account a) {
		if (selectedAccountNode != null) {
			Object infoObject = selectedAccountNode.getUserObject();
			if ((infoObject instanceof AccountInfo) && ((AccountInfo) infoObject).getAccount().equals(a)) {
				DefaultMutableTreeNode customerNode = (DefaultMutableTreeNode)selectedAccountNode.getParent();
				treeModel.removeNodeFromParent(selectedAccountNode);
				setSelectedCustomerNode(customerNode);
				setSelectedAccountNode(null);
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
			System.out.println(c.hashCode() + " " + ci.getCustomer().hashCode());
			boolean isEqual = (ci.getCustomer().getId() == c.getId());
			if (isEqual) {
				setSelectedCustomerNode(node);
				setSelectedAccountNode(null);
				tree.setSelectionPath(new TreePath(selectedCustomerNode.getPath()));
				return;
			}
		}
	}

	/**
	 * Setter of the property <tt>selectedCustomerNode</tt>
	 * 
	 * @param node
	 *            The selectedCustomerNode to set.
	 */
	void setSelectedCustomerNode(DefaultMutableTreeNode node) {
		this.selectedCustomerNode = node;
		if (node != null) {
			tree.setSelectionPath(new TreePath(node.getPath()));
		}
	}

	/**
	 * Setter of the property <tt>selectedAccountNode</tt>
	 * 
	 * @param selectedAccountNode
	 *            The selectedAccountNode to set.
	 */
	void setSelectedAccountNode(DefaultMutableTreeNode node) {
		this.selectedAccountNode = node;
		if (node != null) {
			tree.setSelectionPath(new TreePath(node.getPath()));
		}
	}
}
