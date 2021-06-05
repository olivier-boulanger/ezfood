package swing;

import java.util.ArrayList;

import javax.swing.table.DefaultTableModel;

import metier.Produit;

public class ListeProduitTableModel extends DefaultTableModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String[] entetes = { "Libelle", "PrixHT", "Stock" };
	private ArrayList<Produit> produits;
	
	public ListeProduitTableModel(ArrayList<Produit> produits) {
		this.produits = produits;
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
		return this.produits == null ? 0 : this.produits.size();
	}
	
	public Object getValueAt(int rowIndex, int columnIndex) {
		switch(columnIndex) {
		case 0:
			return this.produits.get(rowIndex).getLibelle();
		case 1:
			return this.produits.get(rowIndex).getPrixHT();
		case 2:
			return this.produits.get(rowIndex).getStock();
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
