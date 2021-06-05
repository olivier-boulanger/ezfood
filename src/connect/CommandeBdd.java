package connect;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map.Entry;
import metier.Produit;

public class CommandeBdd {

	private int id;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	Connection con = ConnectSGBD.getConnect();
	ClientBdd cli = null;

	/* METHODE NOMBRE PRODUIT NON ARCHIVE ET EN STOCK */

	int a = 0;
	public void nbreNonArchive() {
		try {
			Statement stmt=con.createStatement(); 
			ResultSet nb=stmt.executeQuery("select count(*) from produit where archiveProduit = false and "
					+ "stockProduit > 0");

			if (nb.next()) {
				a = nb.getInt(1);
				setId(a);
			}

		} catch(Exception e){
			System.out.println("nbreNonArchive" + e);
		}
	}

	/* METHODE LISTE PRODUITS */

	public String[][] listeProduits() {
		nbreNonArchive();
		String data[][]= new String[getId()][4];
		try {
			Statement stmt=con.createStatement(); 		
			ResultSet rs=stmt.executeQuery("select * from produit where archiveProduit = false "
					+ "and stockProduit > 0");
			int i=0;
			while(rs.next()) {
				data[i][0] = String.valueOf(rs.getInt(1));
				data[i][1] = rs.getString(2);
				data[i][2] = String.valueOf(rs.getDouble(3));
				data[i][3] = String.valueOf(rs.getInt(5));
				i++;
			}

		} catch(Exception e){
			System.out.println("liste produits cmd " + e);
		}
		return data;	
	}

	/* METHODE NOUVELLE COMMANDE */

	public void newCommand(HashMap<Produit, Integer> listProduit, String mail, boolean pay) throws SQLException {
		Date aujourdhui = new Date();
		Statement stmt, stmt2;
		int b =0;
		int a = 0;

		try {
			stmt = con.createStatement();
			ResultSet nb2=stmt.executeQuery("select idClient from client where mailClient ='" + mail + "'");
			if (nb2.next()) {
				b = nb2.getInt(1);
			}
			stmt2 = con.createStatement();
			ResultSet nb=stmt2.executeQuery("SELECT max(idCommande) FROM commande");
			if (nb.next()) {
				a = nb.getInt(1);
				a++;
			}
			PreparedStatement prepStmt =con.prepareStatement("insert into commande values (?, ?, ?, ?);"); 
			prepStmt.setInt(1, a);
			prepStmt.setDate(2, new java.sql.Date(aujourdhui.getTime()));
			prepStmt.setBoolean(3, pay);
			prepStmt.setInt(4, b);
			prepStmt.executeUpdate();
			for (Entry<Produit, Integer> cd : listProduit.entrySet()) {
				int qte = cd.getValue();
				double prix = cd.getKey().getPrixHT();
				int idP = cd.getKey().getId();
				PreparedStatement prepStmt2 =con.prepareStatement("insert into concerner values (?, ?, ?, ?);"); 
				prepStmt2.setInt(1, idP);
				prepStmt2.setInt(2, a);
				prepStmt2.setInt(3, qte);
				prepStmt2.setDouble(4, prix);
				prepStmt2.executeUpdate();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} 

	}

	/* METHODE SAVOIR NOMBRE COMMANDE */

	public int nbreCommande() {
		Statement stmt2;
		try {
			stmt2 = con.createStatement();
			ResultSet nb=stmt2.executeQuery("SELECT max(idCommande) FROM commande");
			if (nb.next()) {
				a = nb.getInt(1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return a;
	}

	/* METHODE LISTE N° COMMANDE PAR MAIL */

	public ArrayList<Integer> listeCommandeParMail(String mail) {
		ArrayList<Integer> commandes = new ArrayList<>();
		ResultSet nb = null;
		try {
			Statement stmt=con.createStatement(); 
			nb=stmt.executeQuery("SELECT idCommande FROM `commande` join client on commande.idClient = client.idClient WHERE mailClient ='" + mail + "'");
			while (nb.next()) {
				int id = nb.getInt(1);
				commandes.add(id);
			}
		} catch(Exception e){
			System.out.println("listeCommandeParMail " + e);
		}

		return commandes;
	}

	/* METHODE LISTE N° COMMANDE PAR MAIL */

	public ArrayList<Integer> listeCommandeParNom(String nom) {
		ArrayList<Integer> commandes = new ArrayList<>();
		ResultSet nb = null;
		try {
			Statement stmt=con.createStatement(); 
			nb=stmt.executeQuery("SELECT idCommande FROM `commande` join client on commande.idClient = client.idClient WHERE mailClient ='" + nom + "'");
			while (nb.next()) {
				int id = nb.getInt(1);
				commandes.add(id);
			}
		} catch(Exception e){
			System.out.println("listeCommandeParMail " + e);
		}

		return commandes;
	}

	/* METHODE VERIFIER SI COMMANDE PAYE */

	public int verifPaiementCmd(int idC) {
		Statement stmt2;
		ResultSet nb;
		int resp = 0;
		try {
			stmt2 = con.createStatement();
			nb=stmt2.executeQuery("SELECT paiementCommande FROM commande where idCommande =" + idC);
			while (nb.next()) {
				resp = nb.getInt(1);
			}
			System.out.println(resp);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return resp;
	}

	/* METHODE PAIEMENT COMMANDE */

	public void payCommand(int idC) {
		try {
			PreparedStatement prepStmt =con.prepareStatement("update commande set paiementCommande= true  where idCommande = ?");
			prepStmt.setInt(1, idC);
			prepStmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/* METHODE SUPPRIMER COMMANDE */

	public boolean delCommand(int idC) {
		boolean resp = false;
		try {
			PreparedStatement prepStmt =con.prepareStatement("delete from concerner where idCommande ='" + idC + "'");
			prepStmt.executeUpdate();
			PreparedStatement prepStmt2 =con.prepareStatement("delete from commande where idCommande ='" + idC + "'");
			prepStmt2.executeUpdate();
			resp = true;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return resp;
	}

	/* METHODE RECHERCHE COMMANDE */

	public HashMap<Produit, Integer> searchCommandByNumber(int num) {
		HashMap<Produit, Integer> cmd = new HashMap<>();
		Statement stmt2;
		ResultSet nb;
		try {
			stmt2 = con.createStatement();
			nb=stmt2.executeQuery("SELECT produit.idProduit, concerner.quantiteProduit, libelleProduit, prixHtProduit, stockProduit FROM concerner join produit on concerner.idProduit = produit.idProduit WHERE idCommande =" + num);
			while (nb.next()) {
				cmd.put((new Produit(nb.getInt(1), nb.getString(3), nb.getDouble(4), nb.getInt(5))), nb.getInt(2));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return cmd;
	}

	public int nbreNom(String nom) {
		Statement stmt2;
		try {
			stmt2 = con.createStatement();
			ResultSet nb=stmt2.executeQuery("SELECT count(*) FROM client where nomClient ='" + nom + "'");
			if (nb.next()) {
				a = nb.getInt(1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return a;
	}

	public HashMap<Produit, Integer> searchCommandByName(String name) {
		HashMap<Produit, Integer> cmd = new HashMap<>();
		Statement stmt2;
		ResultSet nb;
		try {
			stmt2 = con.createStatement();
			nb=stmt2.executeQuery("SELECT produit.idProduit, concerner.quantiteProduit, libelleProduit, prixHtProduit , stockProduit"
					+ " FROM concerner join produit on concerner.idProduit = produit.idProduit"
					+ " WHERE idCommande =(select idCommande from client join commande on client.idClient = commande.idClient where nomClient = '" + name + "')");
			while (nb.next()) {
				cmd.put((new Produit(nb.getInt(1), nb.getString(3), nb.getDouble(4), nb.getInt(5))), nb.getInt(2));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return cmd;
	}

	public double sumByCommand(int num) {
		Statement stmt2;
		ResultSet nb;
		double sum = 0;
		try {
			stmt2 = con.createStatement();
			nb=stmt2.executeQuery("SELECT sum(quantiteProduit*prixVendu)  FROM concerner WHERE idCommande =" + num);
			while (nb.next()) {
				sum = nb.getDouble(1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return sum;
	}

	public String recupMailByIdCommande(int cmd) {
		Statement stmt2;
		ResultSet nb;
		String mail = null;
		try {
			stmt2 = con.createStatement();
			nb=stmt2.executeQuery("SELECT mailClient FROM client join commande on client.idClient = commande.idClient where commande.idCommande =" + cmd);
			while (nb.next()) {
				mail = nb.getString(1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return mail;
	}

	public HashMap<Produit, Integer> searchCommandByMail(String mail) {
		HashMap<Produit, Integer> cmd = new HashMap<>();
		Statement stmt2;
		ResultSet nb;
		try {
			stmt2 = con.createStatement();
			nb=stmt2.executeQuery("SELECT produit.idProduit, concerner.quantiteProduit, libelleProduit, prixHtProduit , stockProduit"
					+ " FROM concerner join produit on concerner.idProduit = produit.idProduit"
					+ " WHERE idCommande =(select idCommande from client join commande on client.idClient = commande.idClient where mailClient = '" + mail + "')");
			while (nb.next()) {
				cmd.put((new Produit(nb.getInt(1), nb.getString(3), nb.getDouble(4), nb.getInt(5))), nb.getInt(2));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return cmd;
	}

}

