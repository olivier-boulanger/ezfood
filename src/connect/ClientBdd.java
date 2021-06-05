package connect;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import metier.Client;
import metier.Commande;

public class ClientBdd {

	private int id;
	Connection con = ConnectSGBD.getConnect();

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	/* METHODE QUI RETOURNE UN CLIENT PAR SON MAIL */

	public Client clientByMail(String mail) {
		ResultSet nb = null;
		String nomC = null, prenomC = null, mailC = null, dateC = null;
		Client cli = null;
		try {
			Statement stmt=con.createStatement(); 
			nb=stmt.executeQuery("select nomClient, prenomClient, mailClient, dateNaissanceClient from client where mailClient ='" + mail + "'");
			while(nb.next()){
				nomC = nb.getString("nomClient");
				prenomC = nb.getString("prenomClient");
				mailC = nb.getString("mailClient");
				dateC = nb.getString("dateNaissanceClient");
			}
			cli = new Client(nomC, prenomC, mailC, dateC);
		} catch(Exception e){
			System.out.println("verif Mail " + e);
		}
		return cli;
	}

	/* METHODE AJOUT CLIENT BDD */

	public void ajoutClient(String name, String firstname, String mail, String date) {
		try {
			Statement stmt=con.createStatement(); 
			ResultSet nb=stmt.executeQuery("SELECT max(idClient) FROM client");
			int a = 0;
			if (nb.next()) {
				a = nb.getInt(1);
				a++;
			}
			PreparedStatement prepStmt =con.prepareStatement("insert into client values (?, ?, ?, ?, ?, ?);"); 
			prepStmt.setInt(1, a);
			prepStmt.setString(2, name);
			prepStmt.setString(3, firstname);
			prepStmt.setString(4, mail);
			prepStmt.setString(5, date);
			prepStmt.setBoolean(6, false);
			prepStmt.executeUpdate();
		} catch(Exception e){
			System.out.println("ajouter client " + e);
		}
	}

	/* METHODE RECUPERER DONNEES CLIENT */

	public ArrayList<Client> dataClient(String mail) {
		ResultSet nb = null;
		ArrayList<Client> data = new ArrayList<>();
		try {
			Statement stmt=con.createStatement(); 
			nb=stmt.executeQuery("select nomClient, prenomClient, mailClient, dateNaissanceClient from client where mailClient ='" + mail + "'");
			while(nb.next()){
				String nomC = nb.getString("nomClient");
				String prenomC = nb.getString("prenomClient");
				String mailC = nb.getString("mailClient");
				String dateC = nb.getString("dateNaissanceClient");
				data.add(new Client(nomC, prenomC, mailC, dateC));
			}

		} catch(Exception e){
			System.out.println("data client " + e);
		}

		return data;

	}

	/* METHODE MODIFIER CLIENT */

	public void modifierClient(String mail, ArrayList<Client> newData) {
		ArrayList<Client> data = newData;
		try {
			PreparedStatement prepStmt =con.prepareStatement("update client set nomClient= ?, prenomClient = ?, mailClient = ?, dateNaissanceClient = ?  where mailClient ='" + mail + "'");
			prepStmt.setString(1, data.get(0).getName());
			prepStmt.setString(2, data.get(0).getPrenom());
			prepStmt.setString(3, data.get(0).getMail());
			prepStmt.setString(4, data.get(0).getDate());
			prepStmt.executeUpdate();			
		} catch(Exception e){
			System.out.println("modifier client " + e);
		}	
	}

	/* METHODE VERIFICATION MAIL EXISTE */

	public boolean verifMail(String mail) throws SQLException {
		ResultSet nb = null;
		try {
			Statement stmt=con.createStatement(); 
			nb=stmt.executeQuery("select * from client where mailClient ='" + mail + "'");
		} catch(Exception e){
			System.out.println("verif Mail " + e);
		}

		return nb.absolute(1);
	}

	/* METHODE VERIFICATION CLIENT ARCHIVE */

	public int verifArchive(String mail) {
		ResultSet nb = null;
		int resp=0;
		try {
			Statement stmt=con.createStatement(); 
			nb=stmt.executeQuery("SELECT archiveClient from client where mailClient ='" + mail + "'");
			while (nb.next()) {
				resp = nb.getInt(1);
			}
		} catch(Exception e){
			System.out.println("verifArchive " + e);
		}
		return resp;
	}

	/* METHODE ARCHIVER CLIENT */

	public boolean archiverClient(String mail) {
		int idC = 0;
		boolean resp = false;
		if (verifArchive(mail) == 0) {
			try {
				Statement stmt=con.createStatement(); 
				ResultSet nb=stmt.executeQuery("select idClient from client where mailClient ='" + mail + "'");	 
				while(nb.next()){
					idC = nb.getInt("idClient");
					resp = true;
				}
				PreparedStatement prepStmt =con.prepareStatement("update client set archiveClient = true where idClient = ?"); 
				prepStmt.setInt(1, idC);
				prepStmt.executeUpdate();
				resp = true;
			} catch(Exception e){
				System.out.println("zip Client" + e);
			}
		}

		return resp;
	}

	/* METHODE DESARCHIVER CLIENT */

	public boolean desarchiverClient(String mail) {
		int idC = 0;
		boolean resp = false; 
		if (verifArchive(mail) == 1) {
			try {
				Statement stmt=con.createStatement(); 
				ResultSet nb=stmt.executeQuery("select idClient from client where mailClient ='" + mail + "'");	 
				while(nb.next()){
					idC = nb.getInt("idClient");
					resp = true;
				}
				PreparedStatement prepStmt =con.prepareStatement("update client set archiveClient = false where idClient = ?"); 
				prepStmt.setInt(1, idC);
				prepStmt.executeUpdate();
				resp = true;
			} catch(Exception e){
				System.out.println("zip Client" + e);
			}
		}

		return resp;
	}

	/* METHODE LISTE CLIENT */

	public ArrayList<Client> listeClient() {
		ArrayList<Client> clients = new ArrayList<>(); 
		try {
			Statement stmt=con.createStatement(); 		
			ResultSet rs=stmt.executeQuery("select * from client where archiveClient = false");
			while(rs.next()) {
				String nom = rs.getString("nomClient");
				String prenom = rs.getString("prenomClient");
				String mail = rs.getString("mailClient");
				String date = rs.getString("dateNaissanceClient");
				clients.add(new Client(nom, prenom, mail, date));
			}

		} catch(Exception e){
			System.out.println("liste client" + e);
		}
		return clients;	
	}

	/* METHODE VERIFICATION NOMBRE COMMANDE CLIENT */

	public int verifNbreCmdClient(String mail) {
		ResultSet nb = null;
		int resp=0;
		try {
			Statement stmt=con.createStatement(); 
			nb=stmt.executeQuery("SELECT count(idCommande), nomClient from commande join client on commande.idClient = client.idClient where mailClient ='" + mail + "'");
			while (nb.next()) {
				resp = nb.getInt(1);
			}
		} catch(Exception e){
			System.out.println("verifNbreCmdClient " + e);
		}
		return resp;
	}

	/* METHODE SUPPRIMER CLIENT */

	public boolean supprimerClient(String mail) {
		boolean resp = false;
		if (verifNbreCmdClient(mail) == 0) {
			try {
				PreparedStatement prepStmt =con.prepareStatement("delete from client where mailClient ='" + mail + "'");
				prepStmt.executeUpdate();
				resp = true;
			} catch(Exception e){
				System.out.println("delete client " + e);
			}
		}
		return resp;

	}

	/* METHODE RECUPERER LISTE COMMANDES CLIENTS */

	public ArrayList<Commande> recupListeCmdClient(String mail) {
		ResultSet nb = null;
		ArrayList<Commande> commandes = new ArrayList<>();
		try {
			Statement stmt=con.createStatement(); 
			nb=stmt.executeQuery("SELECT * from commande join client on commande.idClient = client.idClient where client.mailClient ='" + mail + "'");
			while (nb.next()) {
				int id = nb.getInt(1);
				Date date = nb.getDate("dateCommande");
				boolean pay = nb.getBoolean("paiementCommande");
				commandes.add(new Commande(id,null, date,null, pay));
			}
		} catch(Exception e){
			System.out.println("recupNumCmdClient " + e);
		}
		return commandes;
	}

	public HashMap<String, Integer> recupQteLibelleClient(String mail, String val) {
		HashMap<String, Integer> commande = new HashMap<>();
		ResultSet nb = null;
		int resp = Integer.parseInt(val);
		try {
			Statement stmt=con.createStatement(); 
			nb=stmt.executeQuery("SELECT concerner.idProduit, quantiteProduit, prixVendu, libelleProduit"
					+ " FROM concerner join produit on concerner.idProduit = produit.idProduit"
					+ " WHERE idCommande = (SELECT idCommande FROM commande WHERE idClient = (select idClient from client where mailClient ='" + mail + "' )"
					+ " and idCommande = " + resp + ")");
			while (nb.next()) {
				commande.put( nb.getString(4), nb.getInt(2));
			}
		} catch(Exception e){
			System.out.println("verifNbreCmdClient " + e);
		}
		return commande;
	}


	int a = 0;
	public void nbreNonArchive() {
		try {
			Statement stmt=con.createStatement(); 
			ResultSet nb=stmt.executeQuery("select count(*) from client where archiveClient = false");

			if (nb.next()) {
				a = nb.getInt(1);
				setId(a);
			}

		} catch(Exception e){
			System.out.println("nbreNonArchive" + e);
		}
	}	


}
