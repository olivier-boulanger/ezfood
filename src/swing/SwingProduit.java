package swing;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.ScrollPaneConstants;

import connect.ProduitBdd;
import metier.Produit;


public class SwingProduit {

	/* METHODE AJOUTER PRODUIT */

	ProduitBdd pbd = new ProduitBdd();

	JFrame frame = new JFrame();
	JPanel pan = new JPanel();
	JLabel labLib = new JLabel("Libelle :");
	JTextField libelle = new JTextField(21);
	JLabel labPrix = new JLabel("Prix :");
	JTextField prix = new JTextField(21);
	JLabel labStock = new JLabel("Stock :");
	JTextField stock = new JTextField(21);
	JButton jb = new JButton();

	public void ajouterProduit() {

		jb.setText("SUBMIT");
		jb.addActionListener(new BoutonListener());
		pan.add(labLib);
		pan.add(libelle);
		pan.add(labPrix);
		pan.add(prix);
		pan.add(labStock);
		pan.add(stock);
		pan.add(jb);
		frame.add(pan);
		frame.setSize(260, 260);
		frame.setVisible(true);
		frame.setLocationRelativeTo(null);

	}

	class BoutonListener implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			try {
				if (libelle.getText().equals("") || prix.getText().equals("") || stock.getText().equals("") ) {
					JOptionPane.showMessageDialog(null, "Les champs n'ont pas tous été remplis");
				} else {
					pbd.ajouterProduit(libelle.getText(),Double.parseDouble(prix.getText()), Integer.parseInt(stock.getText()));
					frame.setVisible(false);
				}
			} catch (Exception err) {
				JOptionPane.showMessageDialog(null, "Erreur");
			}

		}
	}

	/* METHODE LISTE PRODUITS */

	private JTable tableListeProduit;

	public void listeProduits(JPanel jpanel) {
		ArrayList<Produit> produits = pbd.listeProduits();
		ListeProduitTableModel model = new ListeProduitTableModel(produits);
		tableListeProduit = new JTable(model);
		tableListeProduit.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		tableListeProduit.setAutoCreateColumnsFromModel(true);
		tableListeProduit.setAutoCreateRowSorter(true);	
		JScrollPane scrollPane = new JScrollPane(tableListeProduit);
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		jpanel.add(scrollPane, BorderLayout.CENTER);
	}

}
