package swing;

import java.util.ArrayList;

import javax.swing.table.DefaultTableModel;

import metier.Client;

public class ListeClientTableModel extends DefaultTableModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String[] entetes = { "Nom", "Prenom", "Mail", "Date de naissance" };
	private ArrayList<Client> clients;
	
	public ListeClientTableModel(ArrayList<Client> clients) {
		this.clients = clients;
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
		return this.clients == null ? 0 : this.clients.size();
		}
	
	public Object getValueAt(int rowIndex, int columnIndex) {
		switch(columnIndex) {
		case 0:
			return this.clients.get(rowIndex).getName();
		case 1:
			return this.clients.get(rowIndex).getPrenom();
		case 2:
			return this.clients.get(rowIndex).getMail();
		case 3:
			return this.clients.get(rowIndex).getDate();
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
