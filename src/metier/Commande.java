package metier;

import java.util.Date;
import java.util.HashMap;

public class Commande {
	
	private int id;
	private Client client;
	private Date date;
	private HashMap<Produit, Integer> listProduit;
	private boolean paye = false;
	
	

	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public Client getClient() {
		return client;
	}
	public void setClient(Client client) {
		this.client = client;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public HashMap<Produit, Integer> getListProduit() {
		return listProduit;
	}
	public void setListProduit(HashMap<Produit, Integer> listProduit) {
		this.listProduit = listProduit;
	}
	
	public boolean isPaye() {
		return paye;
	}
	public void setPaye(boolean paye) {
		this.paye = paye;
	}
	
	public Commande(int id, Client client, Date date, HashMap<Produit, Integer> listProduit, boolean paye) {
		super();
		this.id = id;
		this.client = client;
		this.date = date;
		this.listProduit = listProduit;
		this.paye = paye;
	}
		

}
