package metier;

public class Produit {

	private int id;
	private String libelle;
	private double prixHT;
	private int stock;
	private boolean archive;
	
	
	public int getStock() {
		return stock;
	}
	public void setStock(int stock) {
		this.stock = stock;
	}
	public String getLibelle() {
		return libelle;
	}
	public void setLibelle(String libelle) {
		this.libelle = libelle;
	}
	public double getPrixHT() {
		return prixHT;
	}
	public void setPrixHT(double prixHT) {
		this.prixHT = prixHT;
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public boolean isArchive() {
		return archive;
	}
	public void setArchive(boolean archive) {
		this.archive = archive;
	}
	public Produit(int ref, String libelle, double prixHT, int stock) {
		super();
		this.id = ref;
		this.libelle = libelle;
		this.prixHT = prixHT;
		this.stock = stock;
	}
	

}
