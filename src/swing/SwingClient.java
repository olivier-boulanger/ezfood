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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.text.ParseException;
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
import metier.Client;
import metier.Commande;

public class SwingClient {

	ClientBdd cbd = new ClientBdd();

	/* METHODE VERIFICATION FORMAT DATE */

	public static boolean isValid(String strdate, String format) {
		SimpleDateFormat df = new SimpleDateFormat(format);
		df.setLenient(false);
		try {
			df.parse(strdate);
			return true;
		} catch (ParseException ex) {
			return false;
		}
	}

	/* METHODE AJOUT CLIENT */	

	JFrame frame = new JFrame();
	JPanel pan = new JPanel();
	JLabel labNom = new JLabel("Nom :");
	JTextField nom = new JTextField(21);
	JLabel labPrenom = new JLabel("Prenom :");
	JTextField prenom = new JTextField(21);
	JLabel labMail = new JLabel("Mail :");
	JTextField mail = new JTextField(21);
	JLabel labDate = new JLabel("Date Naissance (format YYYY/MM/DD) :");
	JTextField date = new JTextField(21);
	JButton jb = new JButton();

	public void ajoutClient() {
		jb.setText("SUBMIT");
		jb.addActionListener(new BoutonListenerAjout());
		pan.add(labNom);
		pan.add(nom);
		pan.add(labPrenom);
		pan.add(prenom);
		pan.add(labMail);
		pan.add(mail);
		pan.add(labDate);
		pan.add(date);
		pan.add(jb);
		frame.add(pan);
		frame.setSize(260, 400);
		frame.setVisible(true);
		frame.setLocationRelativeTo(null);
	}

	class BoutonListenerAjout implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			boolean verif = false;
			try {
				if (cbd.verifMail(mail.getText())) {
					JOptionPane.showMessageDialog(null,"Ce mail existe deja");
				} else {
					verif = true;
				}
			} catch (HeadlessException | SQLException e1) {
				e1.printStackTrace();
			}
			if (verif) {
				System.out.println("YES");
				if (nom.getText().equals("") || prenom.getText().equals("") || mail.getText().equals("") || date.getText().equals("")) {
					JOptionPane.showMessageDialog(null, "Les champs n'ont pas tous été remplis");
				} else if (!isEmailAdress(mail.getText())) {
					JOptionPane.showMessageDialog(null, "Ce mail n'est pas valide");
				} else if (!SwingClient.isValid(date.getText(), "yyyy/mm/dd"))  {
					JOptionPane.showMessageDialog(null, "Cette date n'est pas valide");
				}
				else { 
					cbd.ajoutClient(nom.getText(), prenom.getText(), mail.getText(), date.getText());
					frame.dispose();
				}
			}
		}
	}

	/* METHODE VERIFICATION VALIDITE EMAIL */

	public boolean isEmailAdress(String email){	
		Pattern p = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,4}$");
		Matcher m = p.matcher(email.toUpperCase());
		return m.matches();
	}


	/* METHODE MODIFIER CLIENT */

	JTextField tname;
	JTextField tsurname;
	JTextField tmail;
	JTextField tdate;
	ArrayList<Client> recupData = new ArrayList<>();
	ArrayList<Client> newData = new ArrayList<>();
	JFrame modif = new JFrame();
	private String rep="";
	public void modifierClient() {

		try {
			do {
				rep = JOptionPane.showInputDialog(null, "Mail : ", "Modification d'un client", JOptionPane.QUESTION_MESSAGE);
				if (!isEmailAdress(rep)) {
					JOptionPane.showMessageDialog(null, "Ce mail n'est pas valide");
				} 	else if (!cbd.verifMail(rep)) {
					JOptionPane.showMessageDialog(null, "Ce client n'existe pas");
				} else {
					recupData = cbd.dataClient(rep);
					JPanel pan = new JPanel();
					JLabel lname = new JLabel("Nom     :");
					tname = new JTextField(21);
					tname.setText(recupData.get(0).getName());
					JLabel lsurname = new JLabel("Prenom     :");
					tsurname = new JTextField(21);
					tsurname.setText(recupData.get(0).getPrenom());
					JLabel lmail = new JLabel("Mail     :");
					tmail = new JTextField(21);
					tmail.setText(recupData.get(0).getMail());
					JLabel ldate = new JLabel("Date     :");
					tdate = new JTextField(21);
					tdate.setText(recupData.get(0).getDate());
					pan.add(lname);
					pan.add(tname);
					pan.add(lsurname);
					pan.add(tsurname);
					pan.add(lmail);
					pan.add(tmail);
					pan.add(ldate);
					pan.add(tdate);
					JButton jb = new JButton();
					jb.setText("SUBMIT");
					jb.addActionListener(new BoutonListener());
					pan.add(jb);
					modif.add(pan); 
					modif.setSize(260, 400);
					modif.setVisible(true);
					modif.setLocationRelativeTo(null);

				}
			} while (!isEmailAdress(rep) || !cbd.verifMail(rep));
		} catch (Exception e2) {
			System.out.println(e2);
		}
	}

	class BoutonListener implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			if (tname.getText().equals(recupData.get(0).getName()) && tsurname.getText().equals(recupData.get(0).getPrenom()) 
					&& tmail.getText().equals(recupData.get(0).getMail()) && tdate.getText().equals(recupData.get(0).getDate())) {
				JOptionPane.showMessageDialog(null, "Il n'y a pas de modification");
			} else if (!isEmailAdress(tmail.getText())) {
				JOptionPane.showMessageDialog(null, "Ce mail n'est pas valide");
			} 
			else if (tname.getText().equals("") || tsurname.getText().equals("") || tmail.getText().equals("") || tdate.getText().equals("")) {
				JOptionPane.showMessageDialog(null, "Les champs n'ont pas tous été remplis");
			} else {
				newData.add(new Client(tname.getText(),tsurname.getText(), tmail.getText(),tdate.getText()));
				cbd.modifierClient(rep, newData);
				recupData.clear();
				newData.clear();
				modif.dispose();
			}

		}

	}

	/* METHODE ARCHIVER CLIENT */

	public void archiverClient() {
		String rep2="";
		try {
			do {
				rep2 = JOptionPane.showInputDialog(null, "Mail : ", "Archivage d'un client", JOptionPane.QUESTION_MESSAGE);
				if (!isEmailAdress(rep2)) {
					JOptionPane.showMessageDialog(null, "Ce mail n'est pas valide");
				}
				else if (!cbd.verifMail(rep2)) {
					JOptionPane.showMessageDialog(null, "Ce mail n'existe pas");
				} 
				else if (rep2.equals("")) {
					JOptionPane.showMessageDialog(null, "Vous devez saisir un mail");
				} else {
					if (cbd.archiverClient(rep2)) {
						JOptionPane.showMessageDialog(null, "Client archivé");
					} else {
						JOptionPane.showMessageDialog(null, "Ce client est deja archivé");
					}
				}
			} while (rep2.equals("") || !cbd.verifMail(rep2) || !isEmailAdress(rep2));
		} catch (Exception e2) {
			System.out.println(e2);
		}
	}

	/* METHODE DESARCHIVER CLIENT */

	public void desarchiverClient() {
		String rep2="";

		try {
			do {
				rep2 = JOptionPane.showInputDialog(null, "Mail : ", "Desarchivage d'un client", JOptionPane.QUESTION_MESSAGE);
				if (!isEmailAdress(rep2)) {
					JOptionPane.showMessageDialog(null, "Ce mail n'est pas valide");
				}
				else if (!cbd.verifMail(rep2)) {
					JOptionPane.showMessageDialog(null, "Ce mail n'existe pas");
				} 
				else if (rep2.equals("")) {
					JOptionPane.showMessageDialog(null, "Vous devez saisir un mail");
				} else {
					if (cbd.desarchiverClient(rep2)) {
						JOptionPane.showMessageDialog(null, "Client desarchivé");
					} else {
						JOptionPane.showMessageDialog(null, "Ce client est deja desarchivé");
					}
				}
			} while (rep2.equals("") || !cbd.verifMail(rep2) || !isEmailAdress(rep2));
		} catch (Exception e2) {
			System.out.println(e2);
		}
	}

	/* METHODE LISTE CLIENTS */

	private JTable tableListeClient;

	public void listeClient(JPanel panel) {
		ArrayList<Client> clients = cbd.listeClient();
		ListeClientTableModel model = new ListeClientTableModel(clients);
		tableListeClient = new JTable(model);
		tableListeClient.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		tableListeClient.setAutoCreateColumnsFromModel(true);
		tableListeClient.setAutoCreateRowSorter(true);	
		JScrollPane scrollPane = new JScrollPane(tableListeClient);
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		panel.add(scrollPane, BorderLayout.CENTER);
	}

	/* METHODE SUPPIMER CLIENT */


	public void supprimerClient() {
		String rep2="";
		boolean resp = false;
		try {
			do {
				rep2 = JOptionPane.showInputDialog(null, "Mail : ", "Suppression d'un client", JOptionPane.QUESTION_MESSAGE);
				System.out.println("IS EMAIL ADDRESS" + isEmailAdress(rep2));
				if (!isEmailAdress(rep2)) {
					JOptionPane.showMessageDialog(null, "Ce mail n'est pas valide");
				}
				else if (!cbd.verifMail(rep2)) {
					JOptionPane.showMessageDialog(null, "Ce mail n'existe pas");
				}  else {
					resp = cbd.supprimerClient(rep2);
					if (!resp) {
						JOptionPane.showMessageDialog(null, "Vous ne pouvez pas le supprimer. Le client a deja commandé");
					} else {
						JOptionPane.showMessageDialog(null, "Le client a bien été supprimé");
					}
				}
			} while (!cbd.verifMail(rep2) && !isEmailAdress(rep2));
		} catch (Exception e2) {
			System.out.println(e2);
		}
	}

	/* METHODE COMMANDES CLIENTS */

	List<String> recup = new ArrayList<String>();
	private JTable table;
	String rep2="";

	public void NumeroCommandesClient(JPanel panel){
		try {
			do {
				rep2 = JOptionPane.showInputDialog(null, "Mail : ", "Commande d'un client", JOptionPane.QUESTION_MESSAGE);
				System.out.println("IS EMAIL ADDRESS" + isEmailAdress(rep2));
				if (!isEmailAdress(rep2)) {
					JOptionPane.showMessageDialog(null, "Ce mail n'est pas valide");
				}
				else if (!cbd.verifMail(rep2)) {
					JOptionPane.showMessageDialog(null, "Ce mail n'existe pas");
				}  else if (cbd.verifNbreCmdClient(rep2) == 0) {
					JOptionPane.showMessageDialog(null, "Ce client n'a pas de commandes");
				} else {

					ArrayList<Commande> numeroCmd = cbd.recupListeCmdClient(rep2);
					NumCommandeTableModel model = new NumCommandeTableModel(numeroCmd);
					table = new JTable(model);
					table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
					table.setAutoCreateColumnsFromModel(true);
					table.setAutoCreateRowSorter(true);	

					JScrollPane scrollPane = new JScrollPane(table);
					scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
					panel.add(scrollPane, BorderLayout.CENTER);
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
							System.out.println(value);
							listeCommandeClient(value, rep2);
						}
					});
				}
			} while (!cbd.verifMail(rep2) || !isEmailAdress(rep2));
		} catch (Exception e2) {
			System.out.println(e2);
		}
	}



	public void listeCommandeClient(String val, String rep2) {
		HashMap<String, Integer> cmd = cbd.recupQteLibelleClient(rep2, val);
		String data[][]= new String[cmd.size()][2];
		int i = 0;
		for (Entry<String, Integer> cd : cmd.entrySet()) {
			data[i][0] = cd.getKey();
			data[i][1] = String.valueOf(cd.getValue());
			i++;
		}
		String columns[] = { "Libelle", "Qte" };

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

	/* METHODE EXPORTER PDF TXT CLIENT */

	JFrame frame2 = new JFrame();
	public void exporterClient() {
		try {
			do {
				rep2 = JOptionPane.showInputDialog(null, "Mail : ", "Commande d'un client", JOptionPane.QUESTION_MESSAGE);
				if (!isEmailAdress(rep2)) {
					JOptionPane.showMessageDialog(null, "Ce mail n'est pas valide");
				}
				else if (!cbd.verifMail(rep2)) {
					JOptionPane.showMessageDialog(null, "Ce mail n'existe pas");
				} else {
					System.out.println("OK");
					JPanel pan = new JPanel();
					JButton jb = new JButton();
					jb.setText("PDF");
					JButton jb2 = new JButton();
					jb2.setText("TXT");
					jb.addActionListener(new BtnPDF());
					jb2.addActionListener(new BtnTXT());
					pan.add(jb);
					pan.add(jb2);
					frame2.add(pan); 
					frame2.setSize(200, 120);
					frame2.setVisible(true);
					frame2.setLocationRelativeTo(null);
				}
			} while (!cbd.verifMail(rep2) || !isEmailAdress(rep2));
		} catch (Exception e2) {
			System.out.println("exporter client " + e2);
		}
	}

	class BtnPDF implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			ArrayList<Client> dataClient = cbd.dataClient(rep2);
			ArrayList<Commande> commandes = cbd.recupListeCmdClient(rep2);
			frame2.dispose();
			savePdfFile(dataClient, commandes, rep2);
		}

	}
	class BtnTXT implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			ArrayList<Client> dataClient = cbd.dataClient(rep2);
			ArrayList<Commande> commandes = cbd.recupListeCmdClient(rep2);
			frame2.dispose();
			saveTextFile(dataClient, commandes, rep2);
		}

	}

	public void saveTextFile(ArrayList<Client> dataClient, ArrayList<Commande> commandes, String rep2) {
		try {
			FileWriter fw = new FileWriter("sauvegarde_" + dataClient.get(0).getName() + "__" + dataClient.get(0).getMail() + ".txt");
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write("NOM : " + dataClient.get(0).getName());
			bw.newLine();
			bw.write("PRENOM : " + dataClient.get(0).getPrenom());
			bw.newLine();
			bw.write("MAIL : " + dataClient.get(0).getMail());
			bw.newLine();
			bw.write("DATE : " + dataClient.get(0).getDate());
			bw.newLine();
			bw.newLine();
			for (int i=0; i< commandes.size(); i++) {
				bw.write("N° Commande : " + commandes.get(i).getId() + "\n");
				bw.write("---------------------------\n");
				bw.write("Produit   |  Qte       \n");
				HashMap<String, Integer> fileClient = cbd.recupQteLibelleClient(rep2, String.valueOf(commandes.get(i).getId()));
				for (Entry<String, Integer> fc : fileClient.entrySet()) {
					bw.write(fc.getKey() + " | " + fc.getValue() + "\n" );
				}
				bw.newLine();
			}
			bw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public void savePdfFile(ArrayList<Client> dataClient, ArrayList<Commande> commandes, String rep2) {
		try {
			String file = "sauvegarde_" + dataClient.get(0).getName() + "__" + dataClient.get(0).getMail() + ".pdf";
			Document document = new Document();
			PdfWriter.getInstance(document, new FileOutputStream(file));
			document.open();

			Paragraph para = new Paragraph("NOM : " + dataClient.get(0).getName());
			Paragraph para2 = new Paragraph("PRENOM : " + dataClient.get(0).getPrenom());
			Paragraph para3 = new Paragraph("MAIL : " + dataClient.get(0).getMail());
			Paragraph para4 = new Paragraph("DATE : " + dataClient.get(0).getDate());
			document.add(para);
			document.add(para2);
			document.add(para3);
			document.add(para4);
			document.add(new Paragraph(" "));

			PdfPTable pdfTab = new PdfPTable(2);
			PdfPCell pdfCell;

			if (commandes.size() == 0) {
				Paragraph para5 = new Paragraph("PAS DE COMMANDES");
				document.add(para5);
			} else {
				for (int i=0; i< commandes.size(); i++) {
					pdfCell = new PdfPCell(new Phrase("COMMANDE N° " + commandes.get(i).getId()));
					pdfCell.setBackgroundColor(BaseColor.LIGHT_GRAY);
					pdfTab.addCell(pdfCell);
					pdfTab.addCell("");
					pdfCell = new PdfPCell(new Phrase("PRODUIT"));
					pdfCell.setBackgroundColor(BaseColor.CYAN);
					pdfTab.addCell(pdfCell);
					pdfCell = new PdfPCell(new Phrase("QUANTITE"));
					pdfCell.setBackgroundColor(BaseColor.CYAN);
					pdfTab.addCell(pdfCell);
					HashMap<String, Integer> fileClient = cbd.recupQteLibelleClient(rep2, String.valueOf(commandes.get(i).getId()));
					for (Entry<String, Integer> fc : fileClient.entrySet()) {
						pdfTab.addCell(fc.getKey());
						pdfTab.addCell(String.valueOf(fc.getValue()));
					}
				}
				document.add(pdfTab);
			}

			document.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (DocumentException e) {
			e.printStackTrace();
		}
	}


}



