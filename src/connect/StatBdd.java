package connect;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;

import metier.Client;

public class StatBdd {

	Connection con = ConnectSGBD.getConnect();

	public HashMap<String, Integer> stat(){
		HashMap<String, Integer> stats = new HashMap<>();
		ResultSet nb = null;
		Statement stmt;
		try {
			stmt = con.createStatement();
			nb=stmt.executeQuery("SELECT libelleProduit, sum(quantiteProduit) FROM concerner join produit"
					+ " on concerner.idProduit = produit.idProduit GROUP BY libelleProduit ORDER by sum(quantiteProduit) DESC LIMIT 1");
			while (nb.next()) {
				stats.put(nb.getString(1), nb.getInt(2));
			}
			nb=stmt.executeQuery("SELECT libelleProduit, sum(quantiteProduit) FROM concerner join produit"
					+ " on concerner.idProduit = produit.idProduit GROUP BY libelleProduit ORDER by sum(quantiteProduit) ASC LIMIT 1");
			while (nb.next()) {
				stats.put(nb.getString(1), nb.getInt(2));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}	
		return stats;
	}

	public HashMap<String, Double> statMois(){
		HashMap<String, Double> stats = new HashMap<>();
		ResultSet nb = null;
		Statement stmt;
		try {
			stmt = con.createStatement();
			nb=stmt.executeQuery("SELECT month(dateCommande), sum(quantiteProduit * prixVendu) FROM commande join concerner"
					+ " on  commande.idCommande = concerner.idCommande GROUP by month(dateCommande) order by sum(quantiteProduit) desc");
			while (nb.next()) {
				stats.put(nb.getString(1), nb.getDouble(2));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}	
		return stats;
	}

	public HashMap<String, Double> statAnnee(){
		HashMap<String, Double> stats = new HashMap<>();
		ResultSet nb = null;
		Statement stmt;
		try {
			stmt = con.createStatement();
			nb=stmt.executeQuery("SELECT year(dateCommande), sum(quantiteProduit * prixVendu) FROM commande"
					+ " join concerner on  commande.idCommande = concerner.idCommande GROUP by year(dateCommande) order by sum(quantiteProduit) desc");
			while (nb.next()) {
				stats.put(nb.getString(1), nb.getDouble(2));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}	
		return stats;
	}

	public HashMap<Client, Double> statClient(){
		HashMap<Client, Double> stats = new HashMap<>();
		ResultSet nb = null;
		Statement stmt;
		try {
			stmt = con.createStatement();
			nb=stmt.executeQuery("SELECT nomClient, mailClient, sum(quantiteProduit * prixVendu) FROM commande , client, concerner"
					+ " where  commande.idCommande = concerner.idCommande and commande.idClient = client.idClient GROUP by mailClient order by sum(quantiteProduit) desc");
			while (nb.next()) {
				stats.put(new Client(nb.getString(1), null, nb.getString(2), null), nb.getDouble(3));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}	
		return stats;
	}

	public double statTotal(){
		ResultSet nb = null;
		Statement stmt;
		double tot = 0;
		try {
			stmt = con.createStatement();
			nb=stmt.executeQuery("SELECT  sum(quantiteProduit * prixVendu) FROM commande join concerner on  commande.idCommande = concerner.idCommande  order by sum(quantiteProduit) desc");
			while (nb.next()) {
				tot = nb.getDouble(1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}	
		return tot;
	}

}
