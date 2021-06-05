package swing;

import java.util.ArrayList;

import javax.swing.table.DefaultTableModel;

import metier.Commande;

public class NumCommandeTableModel extends DefaultTableModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String[] entetes = { "Numéro" };
	private ArrayList<Commande> commandes;

	public NumCommandeTableModel(ArrayList<Commande> commandes) {
		this.commandes = commandes;
	}

	@Override
	public int getColumnCount() {
		return  this.entetes.length;
	}

	@Override
	public String getColumnName(int columnIndex) {
		return entetes[columnIndex];
	}

	@Override
	public int getRowCount() {
		return this.commandes == null ? 0 : this.commandes.size();
	}

	public Object getValueAt(int rowIndex, int columnIndex) {
		switch(columnIndex) {
		case 0:
			return this.commandes.get(rowIndex).getId();		
		default:
			throw new IllegalArgumentException();
		}
	}

	public Class<?> getColumnClass(int columnIndex) {
		switch(columnIndex) {		
		case 0 :
			return Integer.class;		
		default:
			return Object.class;
		}
	}

	@Override
	public boolean isCellEditable(int row, int column) {
		return false;
	}

}
