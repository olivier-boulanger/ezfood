package swing;

import java.awt.BorderLayout;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import connect.ClientBdd;
import connect.CommandeBdd;
import metier.Client;
import metier.Produit;


public class SwingCommande {

	CommandeBdd cbd = new CommandeBdd();
	ClientBdd cbd2 = new ClientBdd();

	/* METHODE VERIFICATION VALIDITE EMAIL */

	public boolean isEmailAdress(String email){	
		Pattern p = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,4}$");
		Matcher m = p.matcher(email.toUpperCase());
		return m.matches();
	}

	/* METHODE NOUVELLE COMMANDE */


	JFrame f = new JFrame("Liste des produits en stock");
	HashMap<Produit, Integer> listProduit = new HashMap<>();
	public void listeProduitPourCommande() {
		JOptionPane.showConfirmDialog(null, "Cliquez sur le produit pour l'ajouter à la commande", "Instructions", JOptionPane.DEFAULT_OPTION);
		String data[][] = cbd.listeProduits();
		String columns[] = {"Ref.", "LIbelle", "Prix HT", "Stock"};
		DefaultTableModel model = new DefaultTableModel(data, columns);
		JTable table = new JTable(model);
		table.setShowGrid(true);
		table.setShowVerticalLines(true);
		JScrollPane pane = new JScrollPane(table);
		JFrame f = new JFrame("Liste des produits en stock");
		JPanel panel = new JPanel();
		panel.add(pane, BorderLayout.CENTER);
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
				String ref = table.getModel().getValueAt(ligne,0).toString();
				String value = table.getModel().getValueAt(ligne,1).toString();
				String value2 = table.getModel().getValueAt(ligne,2).toString();
				String value3 = table.getModel().getValueAt(ligne,3).toString();
				String rep = JOptionPane.showInputDialog(null, "Quantité de " + value, "Qté : ", JOptionPane.QUESTION_MESSAGE);
				try {
					int refP = Integer.parseInt(ref);
					int qte = Integer.parseInt(rep);
					int stock = Integer.parseInt(value3);
					double prix = Double.parseDouble(value2);
					if (qte > stock) {
						JOptionPane.showMessageDialog(null, "Il n'y pas assez de stock");
					} else if (qte < 1) {
						JOptionPane.showMessageDialog(null, "Veuillez entrer un nombre au moins égal à 1");
					} else {
						listProduit.put(new Produit(refP, value, prix, stock), qte);
						int dialogResult = JOptionPane.showConfirmDialog (null, "Commande terminée ?","",JOptionPane.YES_NO_OPTION);
						if (dialogResult == 0) {
							String client = null;
							boolean pay = false;
							do {
								try {
									client = JOptionPane.showInputDialog(null, "Mail du client : ", "Client : ", JOptionPane.QUESTION_MESSAGE);
									if (!cbd2.verifMail(client)) {
										JOptionPane.showMessageDialog(null, "Ce mail n'existe pas");
									} else if (!isEmailAdress(client)) {
										JOptionPane.showMessageDialog(null, "Ce mail n'est pas valide");
									} else {
										int payCmd = JOptionPane.showConfirmDialog (null, "Voulez-vous régler votre commande ?","",JOptionPane.YES_NO_OPTION);
										if (payCmd == 0) {
											pay = true;
										}
										cbd.newCommand(listProduit, client, pay);
										f.dispose();
									}
								} catch (HeadlessException e1) {
									e1.printStackTrace();
								} catch (SQLException e1) {
									e1.printStackTrace();
								}
							} while (!cbd2.verifMail(client) || !isEmailAdress(client));

						}

					}
				} catch (NumberFormatException | SQLException err) {
					JOptionPane.showMessageDialog(null, "Ce n'est pas un nombre");
				}
			}
		});
		f.getContentPane().add(panel);
		f.setSize(600, 400);
		f.setLocationRelativeTo(null);
		f.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		f.pack();
		f.setVisible(true);
	}

	/* METHODE PAIEMENT COMMANDE */

	public void payCommand() {
		try {
			int num, nbr, resp;
			do {
				String numCmd = JOptionPane.showInputDialog(null, "N° commande ", "", JOptionPane.QUESTION_MESSAGE);
				num = Integer.parseInt(numCmd);
				nbr = cbd.nbreCommande();
				resp = cbd.verifPaiementCmd(num);
				if (num > nbr) {
					JOptionPane.showMessageDialog(null, "Il n'y a pas de commande à ce numéro");
				} else if (resp == 1) {
					JOptionPane.showMessageDialog(null, "Cette commande est déjà payée");
				} else {
					cbd.payCommand(num);
				}
			} while( num > nbr || resp == 1);

		} catch (NumberFormatException  err) {
			JOptionPane.showMessageDialog(null, "Ce n'est pas un nombre");
		}

	}

	/* METHODE SUPPRIMER COMMANDE */

	public void delCommande() {
		try {
			int num, nbr, resp;
			boolean ret;
			do {
				String numCmd = JOptionPane.showInputDialog(null, "N° commande ", "", JOptionPane.QUESTION_MESSAGE);
				num = Integer.parseInt(numCmd);
				nbr = cbd.nbreCommande();
				resp = cbd.verifPaiementCmd(num);
				if (num > nbr) {
					JOptionPane.showMessageDialog(null, "Il n'y a pas de commande à ce numéro");
				} else if (resp == 1) {
					JOptionPane.showMessageDialog(null, "Vous ne pouvez pas supprimer cette commande. Elle a été payée");
				} else {
					ret = cbd.delCommand(num);
					if (ret) {
						JOptionPane.showMessageDialog(null, "Commande numéro " + num + " supprimée");
					}
				}
			} while( num > nbr || resp == 1);

		} catch (NumberFormatException  err) {
			JOptionPane.showMessageDialog(null, "Ce n'est pas un nombre");
		}
	}

	/* METHODE RECHERCHE COMMANDE */

	JFrame frame = new JFrame();
	JPanel pan = new JPanel();
	JLabel labNom = new JLabel("Nom :");
	JTextField nom = new JTextField(21);
	JLabel labNum = new JLabel("N° Commande :");
	JTextField num = new JTextField(21);
	JButton jb = new JButton();
	public void searchCommande() {
		JOptionPane.showConfirmDialog(null, "Remplir un seul des 2 champs", "Instructions", JOptionPane.DEFAULT_OPTION);
		pan.add(labNom);
		pan.add(nom);
		pan.add(labNum);
		pan.add(num);
		jb.setText("SEARCH");
		jb.addActionListener(new BoutonListenerSearch());
		pan.add(jb);
		frame.add(pan);
		frame.setSize(260, 300);
		frame.setVisible(true);
		frame.setLocationRelativeTo(null);

	}

	class BoutonListenerSearch implements ActionListener{   
		public void actionPerformed(ActionEvent e) {
			boolean verif = false;
			HashMap<Produit, Integer> cmd = null;
			try {
				if ((nom.getText().equals("") && num.getText().equals(""))){
					JOptionPane.showMessageDialog(null,"Les 2 champs sont vides"); 
				} else if (!nom.getText().equals("") && !num.getText().equals("")) {
					JOptionPane.showMessageDialog(null,"Un seul champ doit être rempli"); 
				} else {
					verif = true;
				}
			} catch (HeadlessException e1) {
				JOptionPane.showMessageDialog(null, "Ce n'est pas un nombre");
			}
			if (verif) {
				frame.dispose();
				if (nom.getText().equals("")){  
					try {
						cmd = cbd.searchCommandByNumber(Integer.parseInt(num.getText()));
						String data[][] = new String[cmd.size()][3];
						int i = 0;
						for (Entry<Produit, Integer> cd : cmd.entrySet()) {
							data[i][0] = cd.getKey().getLibelle();
							data[i][1] = Double.toString(cd.getKey().getPrixHT());
							data[i][2] = String.valueOf(cd.getValue());
							i++;
						}
						String columns[] = { "Libelle", "Prix HT", "Quantité"};

						DefaultTableModel model = new DefaultTableModel(data, columns);
						JTable table = new JTable(model);
						table.setShowGrid(true);
						table.setShowVerticalLines(true);
						JScrollPane pane = new JScrollPane(table);
						JFrame f = new JFrame("Commande");
						JPanel panel = new JPanel();
						panel.add(pane, BorderLayout.CENTER);
						f.getContentPane().add(panel);
						f.setSize(300, 400);
						f.setLocationRelativeTo(null);
						f.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
						f.pack();
						f.setVisible(true);	
					} catch (NumberFormatException e1) {
						JOptionPane.showMessageDialog(null, "Ce n'est pas un nombre");
					}

				} else {
					int name = cbd.nbreNom(nom.getText()); 
					if (name > 1) { 
						JOptionPane.showMessageDialog(null,"Il y a plusieurs fois le même nom, précisez le mail");
						String client = JOptionPane.showInputDialog(null, "Mail : ", "", JOptionPane.QUESTION_MESSAGE);
						try {
							if (!cbd2.verifMail(client)) {
								JOptionPane.showMessageDialog(null, "Ce mail n'existe pas");
							} else if (!isEmailAdress(client)) {
								JOptionPane.showMessageDialog(null, "Ce mail n'est pas valide");
							} else {
								ArrayList<Integer> numCmd = cbd.listeCommandeParMail(client);
								if (numCmd.size() == 0) {
									JOptionPane.showMessageDialog(null, "Pas de commande");
								} else {
									Integer data2[][] = new Integer[numCmd.size()][1];
									String columns2[] = { "N° Cmd"};
									for (int j=0; j<numCmd.size();j++) {
										data2[j][0] = numCmd.get(j);
									}
									DefaultTableModel model = new DefaultTableModel(data2, columns2);
									JTable table = new JTable(model);
									table.setShowGrid(true);
									table.setShowVerticalLines(true);
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
											try {
												HashMap<Produit, Integer> cmd = cbd.searchCommandByNumber(Integer.valueOf(value));
												String data[][] = new String[cmd.size()][3];
												int i = 0;
												for (Entry<Produit, Integer> cd : cmd.entrySet()) {
													data[i][0] = cd.getKey().getLibelle();
													data[i][1] = Double.toString(cd.getKey().getPrixHT());
													data[i][2] = String.valueOf(cd.getValue());
													i++;
												}
												String columns[] = { "Libelle", "Prix HT", "Quantité"};

												DefaultTableModel model = new DefaultTableModel(data, columns);
												JTable table = new JTable(model);
												table.setShowGrid(true);
												table.setShowVerticalLines(true);
												JScrollPane pane = new JScrollPane(table);
												JFrame f = new JFrame("Commande");
												JPanel panel = new JPanel();
												panel.add(pane, BorderLayout.CENTER);
												f.getContentPane().add(panel);
												f.setSize(300, 400);
												f.setLocationRelativeTo(null);
												f.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
												f.pack();
												f.setVisible(true);	
											} catch (NumberFormatException e1) {
												JOptionPane.showMessageDialog(null, "Ce n'est pas un nombre");
											}
										}
									});
									JScrollPane pane = new JScrollPane(table);
									JFrame f = new JFrame("Commande");
									JPanel panel = new JPanel();
									panel.add(pane, BorderLayout.CENTER);
									f.getContentPane().add(panel);
									f.setSize(300, 400);
									f.setLocationRelativeTo(null);
									f.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
									f.pack();
									f.setVisible(true);
								}
							}
						} catch (HeadlessException e1) {
							e1.printStackTrace();
						} catch (SQLException e1) {
							e1.printStackTrace();
						}
					} else { 
						try {
							cmd = cbd.searchCommandByName(nom.getText());
							if (cmd.size() == 0) {
								JOptionPane.showMessageDialog(null, "Pas de commande");
							} else {
								String data[][] = new String[cmd.size()][3];
								int i = 0;
								for (Entry<Produit, Integer> cd : cmd.entrySet()) {
									data[i][0] = cd.getKey().getLibelle();
									data[i][1] = Double.toString(cd.getKey().getPrixHT());
									data[i][2] = String.valueOf(cd.getValue());
									i++;
								}
								String columns[] = { "Libelle", "Prix HT", "Quantité"};

								DefaultTableModel model = new DefaultTableModel(data, columns);
								JTable table = new JTable(model);
								table.setShowGrid(true);
								table.setShowVerticalLines(true);
								JScrollPane pane = new JScrollPane(table);
								JFrame f = new JFrame("Commande");
								JPanel panel = new JPanel();
								panel.add(pane, BorderLayout.CENTER);
								f.getContentPane().add(panel);
								f.setSize(300, 400);
								f.setLocationRelativeTo(null);
								f.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
								f.pack();
								f.setVisible(true);	
							}
						} catch (Exception err) {
							System.out.println(err);
						}
					}
				}
			}
		}
	}

	/* METHODE EDIT COMMANDE */

	JFrame frame3 = new JFrame();
	String rep3;
	public void editCommande() {
		try {
			do {
				rep3 = JOptionPane.showInputDialog(null, "N° Commande : ", "Commande d'un client", JOptionPane.QUESTION_MESSAGE);
			} while (rep3.equals(""));

			JPanel pan = new JPanel();
			JButton jb = new JButton();
			jb.setText("PDF");
			JButton jb2 = new JButton();
			jb2.setText("TXT");
			jb.addActionListener(new BtnPDF());
			jb2.addActionListener(new BtnTXT());
			pan.add(jb);
			pan.add(jb2);
			frame3.add(pan); 
			frame3.setSize(200, 120);
			frame3.setVisible(true);
			frame3.setLocationRelativeTo(null);

		} catch (NumberFormatException e2) {
			JOptionPane.showMessageDialog(null, "Ce n'est pas un nombre");
		}
	}

	class BtnPDF implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			HashMap<Produit, Integer> dataClient = cbd.searchCommandByNumber(Integer.parseInt(rep3));
			String rep4 = cbd.recupMailByIdCommande(Integer.parseInt(rep3)); 
			ArrayList<Client> client = cbd2.dataClient(rep4); 
			frame3.dispose();
			savePdfFile(dataClient, client,  rep3);
		}

	}


	public void savePdfFile(HashMap<Produit, Integer> dataClient, ArrayList<Client> client, String rep2) {
		try {
			String file = "facture__" + rep2 + ".pdf";

			Double sum = cbd.sumByCommand(Integer.parseInt(rep2));

			Document document = new Document();
			PdfWriter.getInstance(document, new FileOutputStream(file));
			document.open();

			Paragraph para = new Paragraph("NOM : " + client.get(0).getName());
			Paragraph para2 = new Paragraph("PRENOM : " + client.get(0).getPrenom());
			Paragraph para3 = new Paragraph("MAIL : " + client.get(0).getMail());
			Paragraph para4 = new Paragraph("DATE : " + client.get(0).getDate());
			document.add(para);
			document.add(para2);
			document.add(para3);
			document.add(para4);
			document.add(new Paragraph(" "));

			PdfPTable pdfTab = new PdfPTable(3);
			PdfPCell pdfCell;

			if (dataClient.size() == 0) {
				Paragraph para5 = new Paragraph("PAS DE COMMANDES");
				document.add(para5);
			} else {
				pdfCell = new PdfPCell(new Phrase("COMMANDE N° " + rep2));
				pdfCell.setBackgroundColor(BaseColor.LIGHT_GRAY);
				pdfTab.addCell(pdfCell);
				pdfTab.addCell("");
				pdfTab.addCell("");
				pdfCell = new PdfPCell(new Phrase("PRODUIT"));
				pdfCell.setBackgroundColor(BaseColor.CYAN);
				pdfTab.addCell(pdfCell);
				pdfCell = new PdfPCell(new Phrase("QUANTITE"));
				pdfCell.setBackgroundColor(BaseColor.CYAN);
				pdfTab.addCell(pdfCell);
				pdfCell = new PdfPCell(new Phrase("PRIX HT"));
				pdfCell.setBackgroundColor(BaseColor.CYAN);
				pdfTab.addCell(pdfCell);
				for (Entry<Produit, Integer> cd : dataClient.entrySet()) {
					pdfTab.addCell(cd.getKey().getLibelle());
					pdfTab.addCell(String.valueOf(cd.getValue()));
					pdfTab.addCell(String.valueOf(cd.getKey().getPrixHT()));
				}
				document.add(pdfTab);
				document.add(new Paragraph(" "));
				Paragraph para5 = new Paragraph("TOTAL HT : " + sum);
				Paragraph para6 = new Paragraph("TOTAL TTC : " + (sum*1.206));
				document.add(para5);
				document.add(para6);
			}

			document.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (DocumentException e) {
			e.printStackTrace();
		}
	}

	class BtnTXT implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			HashMap<Produit, Integer> dataClient = cbd.searchCommandByNumber(Integer.parseInt(rep3));
			String rep4 = cbd.recupMailByIdCommande(Integer.parseInt(rep3));
			ArrayList<Client> client = cbd2.dataClient(rep4);
			frame3.dispose();
			saveTextFile(dataClient, client, rep3);
		}

	}

	public void saveTextFile(HashMap<Produit, Integer> dataClient, ArrayList<Client> client, String rep2) {
		Double sum = cbd.sumByCommand(Integer.parseInt(rep2));
		try {
			FileWriter fw = new FileWriter("facture" + client.get(0).getName() + "__" + client.get(0).getMail() + ".txt");
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write("NOM : " + client.get(0).getName());
			bw.newLine();
			bw.write("PRENOM : " + client.get(0).getPrenom());
			bw.newLine();
			bw.write("MAIL : " + client.get(0).getMail());
			bw.newLine();
			bw.write("DATE : " + client.get(0).getDate());
			bw.newLine();
			bw.newLine();
			if (dataClient.size() == 0) {
				bw.write("");
				bw.write("Pas de Commandes");
			} else {
				bw.write("N° Commande : " + rep2 + "\n");
				bw.write("");
				bw.write("PRODUIT   |  QUANTITE  |  PRIX HT\n");
				bw.write("---------------------------\n");
				for (Entry<Produit, Integer> fc : dataClient.entrySet()) {
					bw.write(fc.getKey().getLibelle() + " | " + fc.getValue() + " | " + fc.getKey().getPrixHT() + "\n" );
				}
				bw.newLine();
				bw.write("TOTAL HT : " + sum);
				bw.newLine();
				bw.write("TOTAL TTC : " + (sum*1.206));
			}

			bw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}



