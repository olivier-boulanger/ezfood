package connect;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import metier.Produit;

public class ProduitBdd {

	private int id;
	Connection con = ConnectSGBD.getConnect();

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void ajouterProduit(String libelle, double prix, int stock) {
		try {
			Statement stmt=con.createStatement(); 
			ResultSet nb=stmt.executeQuery("SELECT max(idProduit) FROM produit");
			int a = 0;
			if (nb.next()) {
				a = nb.getInt(1);
				a++;
			}
			PreparedStatement prepStmt =con.prepareStatement("insert into produit values (?, ?, ?, ?, ?);"); 
			prepStmt.setInt(1, a);
			prepStmt.setString(2, libelle);
			prepStmt.setDouble(3, prix);
			prepStmt.setBoolean(4, false);
			prepStmt.setInt(5, stock);
			prepStmt.executeUpdate();
		} catch(Exception e){
			System.out.println(e);
		}
	}

	public ArrayList<Produit> listeProduits() {
		ArrayList<Produit> produits = new ArrayList<>();
		Statement stmt;
		try {
			stmt = con.createStatement();
			ResultSet rs=stmt.executeQuery("select * from produit where archiveProduit = false");
			while(rs.next()) {
				int ref = rs.getInt("idProduit");
				String libelle = rs.getString("libelleProduit");
				double prixHT = rs.getDouble("prixHtProduit");
				int stock = rs.getInt("stockProduit");
				produits.add(new Produit(ref, libelle, prixHT, stock));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} 		
		return produits;
	}

	/**
	 * Supprimer un produit
	 * @param idProduit
	 */
	public void deletePoduct(String idProduit) {
		try {
			PreparedStatement prepStmt = con.prepareStatement("delete  from produit where produitId ='" + idProduit + "'");
			prepStmt.executeUpdate();
		} catch (Exception e) {
			System.out.println("delete produit" + e);
		}
	}

	/**
	 * Vérifie si un produit à été commandé
	 * @param idProduit
	 * @return
	 */
	public int verifIfProductCmd(String idProduit) {
		ResultSet nb = null;
		int resp = 0;
		try {
			Statement stmt = con.createStatement();
			nb = stmt.executeQuery("SELECT concerner.idProduit, libelleProduit from concerner join produit on concerner.idProduit = produit.idProduit where produit.idProduit ='" + idProduit + "'");
			while (nb.next()) {
				resp = nb.getInt(1);
			}
		} catch (Exception e) {
			System.out.println("verifIfProductCmd " + e);
		}
		return resp;
	}

	/**
	 * Fonction qui archive un produit
	 * @param idProduit
	 * @param libelle
	 * @return
	 */
	public boolean archiveProduct(String idProduit, String libelle) {
		int idP = 0;
		boolean resp = false;
		try {
			Statement stmt = con.createStatement();
			ResultSet nb = stmt.executeQuery("select idProduit from Produit where  ='" + idProduit + "' and libelleProduit='" + libelle + "'");
			while (nb.next()) {
				idP = nb.getInt("idProduit");
				System.out.println(idP);
				resp = true;
			}
			PreparedStatement prepStmt = con.prepareStatement("update produit set archiveProduit = true where idProduit = ?");
			prepStmt.setInt(1, idP);
			prepStmt.executeUpdate();
		} catch (Exception e) {
			System.out.println(e + " , Produit archivé ");
		}
		return resp;
	}

	/**
	 * Fonction qui désarchive un produit
	 * @param idProduit
	 * @param libelle
	 * @return
	 */
	public boolean unArchiveProduct(String idProduit, String libelle) {
		int idP = 0;
		boolean resp = false;
		try {
			Statement stmt = con.createStatement();
			ResultSet nb = stmt.executeQuery("select idProduit from produit where idProduit ='" + idProduit + "' and libelleProduit='" + libelle + "'");
			while (nb.next()) {
				idP = nb.getInt("idProduit");
				System.out.println(idP);
				resp = true;
			}
			PreparedStatement prepStmt = con.prepareStatement("update client set archiveClient = false where idClient = ?");
			prepStmt.setInt(1, idP);
			prepStmt.executeUpdate();
		} catch (Exception e) {
			System.out.println(e + " , produit archivé ");
		}
		return resp;
	}

	/**
	 * Fonction qui vérifie si le produit est archivé
	 * @param libelle
	 * @param idRef
	 * @return boolean
	 */
	public boolean verifArchiveProduit(String idRef, String libelle) {
		ResultSet nb = null;
		boolean resp = false;
		try {
			Statement stmt = con.createStatement();
			nb = stmt.executeQuery("SELECT archiveProduit from Produit where idRef ='" + idRef + "'");
			while (nb.next()) {
				resp = true;
			}
		} catch (Exception e) {
			System.out.println("verifArchive " + e);
		}
		return resp;
	}


}
