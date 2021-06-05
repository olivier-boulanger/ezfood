package swing;

import java.awt.BorderLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map.Entry;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import connect.StatBdd;
import metier.Client;

public class SwingStats {

	StatBdd stb = new StatBdd();

	public void listStatsProduit() {
		HashMap<String, Integer> stats = stb.stat();
		String data[][] = new String[stats.size()][3];
		int i = 0;
		String varia = "plus";
		for (Entry<String, Integer> cd : stats.entrySet()) {
			if (i == 1) {
				varia = "moins";
			}
			data[i][0] = "Produit le " + varia + " vendu";
			data[i][1] = cd.getKey();
			data[i][2] = String.valueOf(cd.getValue());
			i++;
		}
		String columns[] = { "", "Libelle", "Vente"};

		DefaultTableModel model = new DefaultTableModel(data, columns);
		JTable table = new JTable(model);
		table.setShowGrid(true);
		table.setShowVerticalLines(true);
		JScrollPane pane = new JScrollPane(table);
		JFrame f = new JFrame("STATISTIQUES");
		JPanel panel = new JPanel();
		panel.add(pane, BorderLayout.CENTER);
		f.getContentPane().add(panel);
		f.setSize(300, 400);
		f.setLocationRelativeTo(null);
		f.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		f.pack();
		f.setVisible(true);	
	}

	public void choix() {
		String columns[] = { "CHOIX"};
		String data[][] = new String[4][1];
		data[0][0] = "mois";
		data[1][0] = "année";
		data[2][0] = "total";
		data[3][0] = "client";
		DefaultTableModel model = new DefaultTableModel(data, columns);
		JTable table = new JTable(model);
		table.setShowGrid(true);
		table.setShowVerticalLines(true);
		JScrollPane pane = new JScrollPane(table);
		table.addMouseListener(new MouseListener() {

			@Override
			public void mouseReleased(MouseEvent e) {
			}

			@Override
			public void mousePressed(MouseEvent e) {
			}

			@Override
			public void mouseExited(MouseEvent e) {
			}

			@Override
			public void mouseEntered(MouseEvent e) {
			}

			@Override
			public void mouseClicked(MouseEvent e) {
				int ligne = table.getSelectedRow();
				String value = table.getModel().getValueAt(ligne,0).toString();
				if (value.equals("mois")) {
					listStatsMois();
				} else if (value.equals("année")) {
					listStatsAnnee();
				} else if (value.equals("total")) {
					double tot = stb.statTotal();
					JOptionPane.showMessageDialog(null, "CA total : " + tot);
				} else if (value.equals("client")) {
					listStatsClient();
				}
			}
		});
		JFrame f = new JFrame("STATISTIQUES");
		JPanel panel = new JPanel();
		panel.add(pane, BorderLayout.CENTER);
		f.getContentPane().add(panel);
		f.setSize(300, 400);
		f.setLocationRelativeTo(null);
		f.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		f.pack();
		f.setVisible(true);	
	}

	public void listStatsMois() {
		HashMap<String, Double> stats = stb.statMois();
		String data[][] = new String[stats.size()][2];
		int i = 0;
		for (Entry<String, Double> cd : stats.entrySet()) {
			data[i][0] = cd.getKey();
			data[i][1] = String.valueOf(cd.getValue());
			i++;
		}
		String columns[] = {"Mois", "Chiffre"};

		DefaultTableModel model = new DefaultTableModel(data, columns);
		JTable table = new JTable(model);
		table.setShowGrid(true);
		table.setShowVerticalLines(true);
		JScrollPane pane = new JScrollPane(table);
		JFrame f = new JFrame("STATISTIQUES");
		JPanel panel = new JPanel();
		panel.add(pane, BorderLayout.CENTER);
		f.getContentPane().add(panel);
		f.setSize(300, 400);
		f.setLocationRelativeTo(null);
		f.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		f.pack();
		f.setVisible(true);	
	}

	public void listStatsAnnee() {
		HashMap<String, Double> stats = stb.statAnnee();
		String data[][] = new String[stats.size()][2];
		int i = 0;
		for (Entry<String, Double> cd : stats.entrySet()) {
			data[i][0] = cd.getKey();
			data[i][1] = String.valueOf(cd.getValue());
			i++;
		}
		String columns[] = {"Année", "Chiffre"};

		DefaultTableModel model = new DefaultTableModel(data, columns);
		JTable table = new JTable(model);
		table.setShowGrid(true);
		table.setShowVerticalLines(true);
		JScrollPane pane = new JScrollPane(table);
		JFrame f = new JFrame("STATISTIQUES");
		JPanel panel = new JPanel();
		panel.add(pane, BorderLayout.CENTER);
		f.getContentPane().add(panel);
		f.setSize(300, 400);
		f.setLocationRelativeTo(null);
		f.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		f.pack();
		f.setVisible(true);	
	}

	public void listStatsClient() {
		HashMap<Client, Double> stats = stb.statClient();
		String data[][] = new String[stats.size()][3];
		int i = 0;
		for (Entry<Client, Double> cd : stats.entrySet()) {
			data[i][0] = cd.getKey().getName();
			data[i][1] = cd.getKey().getMail();
			data[i][2] = String.valueOf(cd.getValue());
			i++;
		}
		String columns[] = {"Nom", "Mail", "Chiffre"};

		DefaultTableModel model = new DefaultTableModel(data, columns);
		JTable table = new JTable(model);
		table.setShowGrid(true);
		table.setShowVerticalLines(true);
		JScrollPane pane = new JScrollPane(table);
		JFrame f = new JFrame("STATISTIQUES");
		JPanel panel = new JPanel();
		panel.add(pane, BorderLayout.CENTER);
		f.getContentPane().add(panel);
		f.setSize(300, 400);
		f.setLocationRelativeTo(null);
		f.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		f.pack();
		f.setVisible(true);	
	}



	public void editStats() {
		HashMap<String, Integer> stats = stb.stat();
		HashMap<String, Double> statsMois = stb.statMois();
		HashMap<String, Double> statsAn = stb.statAnnee();
		HashMap<Client, Double> statsClient = stb.statClient();
		try {
			FileWriter fw = new FileWriter("stats.txt");
			BufferedWriter bw = new BufferedWriter(fw);
			String varia = "PLUS";
			int i = 0;
			for (Entry<String, Integer> cd : stats.entrySet()) {
				if (i==1) {
					varia = "MOINS";
					bw.newLine();
				}
				bw.write("PRODUIT LE " + varia + " VENDU : " + cd.getKey() + " | QTE : " + cd.getValue());
				i++;
			}
			bw.newLine();
			bw.write("CHIFFRE PAR MOIS");
			bw.newLine();
			for (Entry<String, Double> cd : statsMois.entrySet()) {
				bw.newLine();
				bw.write("MOIS : " + cd.getKey() + " | SOMME : " + cd.getValue());
			}
			bw.newLine();
			bw.write("CHIFFRE PAR AN");
			bw.newLine();
			for (Entry<String, Double> cd : statsAn.entrySet()) {
				bw.newLine();
				bw.write("ANNEE : " + cd.getKey() + " | SOMME : " + cd.getValue());
			}
			bw.newLine();
			bw.newLine();
			bw.write("CHIFFRE TOTAL : " + stb.statTotal());
			bw.newLine();
			bw.newLine();
			bw.write("CHIFFRE PAR CLIENT");
			bw.newLine();
			for (Entry<Client, Double> cd : statsClient.entrySet()) {
				bw.newLine();
				bw.write("NOM : " + cd.getKey().getName() + " | MAIL : " + cd.getKey().getMail() + " | SOMME : " + cd.getValue());
			}
			bw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
