package swing;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.IOException;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.plaf.nimbus.NimbusLookAndFeel;

public class VueSwing extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	Accueil vue = new Accueil();

    SwingClient sc = new SwingClient();
    SwingProduit sp = new SwingProduit();
    SwingCommande sm  =new SwingCommande();
    SwingStats st = new SwingStats();
	JPanel panel = new JPanel(new BorderLayout());
	
	public VueSwing() throws IOException {
		super("EZ Game");
		this.setSize(800, 600);
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);	
		this.setJMenuBar(createMenuBar());
		getContentPane().setLayout(new BorderLayout());		
		getContentPane().add(panel, BorderLayout.CENTER);
	}
	

	private JMenuBar createMenuBar() {
		JMenuBar menuBar = new JMenuBar();

		JMenu menuFile1 = new JMenu("Client");
		menuFile1.setMnemonic('C');
		menuBar.add(menuFile1);
		JMenu menuFile2 = new JMenu("Produit");
		menuFile2.setMnemonic('P');
		menuBar.add(menuFile2);
		JMenu menuFile3 = new JMenu("Commande");
		menuFile3.setMnemonic('m');
		menuBar.add(menuFile3);
		JMenu menuFile4 = new JMenu("Statistiques");
		menuFile4.setMnemonic('S');
		menuBar.add(menuFile4);
		JMenu menuFile5 = new JMenu("Se déconnecter");
		menuFile5.setMnemonic('X');
		menuBar.add(menuFile5);
		
		JMenuItem itemAdd = new JMenuItem("Ajout");
		itemAdd.setMnemonic('N');
		itemAdd.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A, KeyEvent.CTRL_DOWN_MASK));
		itemAdd.setIcon(new ImageIcon("src/icons/save.png"));
		itemAdd.addActionListener(this::ajoutClient);
		menuFile1.add(itemAdd);
		
		menuFile1.addSeparator();
		
		JMenuItem itemModify = new JMenuItem("Modifier");
		itemModify.setMnemonic('M');
		itemModify.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_M, KeyEvent.CTRL_DOWN_MASK));
		itemModify.setIcon(new ImageIcon("src/icons/cut.png"));
		itemModify.addActionListener(this::modifierClient);
		menuFile1.add(itemModify);

		menuFile1.addSeparator();
		
		JMenuItem itemSave = new JMenuItem("Archiver");
		itemSave.setMnemonic('S');
		itemSave.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, KeyEvent.CTRL_DOWN_MASK));
		itemSave.setIcon(new ImageIcon("src/icons/redo.png"));
		itemSave.addActionListener(this::archiverClient);
		menuFile1.add(itemSave);
		
		menuFile1.addSeparator();
		
		JMenuItem itemUnSave = new JMenuItem("Desarchiver");
		itemUnSave.setMnemonic('S');
		itemUnSave.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_H, KeyEvent.CTRL_DOWN_MASK));
		itemUnSave.setIcon(new ImageIcon("src/icons/undo.png"));
		itemUnSave.addActionListener(this::desarchiverClient);
		menuFile1.add(itemUnSave);
		
		menuFile1.addSeparator();
		
		JMenuItem itemList = new JMenuItem("Liste");
		itemList.setMnemonic('L');
		itemList.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_L, KeyEvent.CTRL_DOWN_MASK));
		itemList.setIcon(new ImageIcon("src/icons/new.png"));
		itemList.addActionListener(this::listeClients);
		menuFile1.add(itemList);
		
		menuFile1.addSeparator();
		
		JMenuItem itemLDel = new JMenuItem("Supprimer");
		itemLDel.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_D, KeyEvent.CTRL_DOWN_MASK));
		itemLDel.setIcon(new ImageIcon("src/icons/exit.png"));
		itemLDel.addActionListener(this::supprimerClient);
		menuFile1.add(itemLDel);
		
		menuFile1.addSeparator();
		
		JMenuItem itemCmd = new JMenuItem("Commande");
		itemCmd.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_E, KeyEvent.CTRL_DOWN_MASK));
		itemCmd.setIcon(new ImageIcon("src/icons/copy.png"));
		itemCmd.addActionListener(this::commandesClient);
		menuFile1.add(itemCmd);
		
		menuFile1.addSeparator();
		
		JMenuItem itemExport = new JMenuItem("Exporter");
		itemExport.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X, KeyEvent.CTRL_DOWN_MASK));
		itemExport.setIcon(new ImageIcon("src/icons/new.png"));
		itemExport.addActionListener(this::exportClient);
		menuFile1.add(itemExport);
		
		JMenuItem itemAddP = new JMenuItem("Ajout");
		itemAddP.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_J, KeyEvent.CTRL_DOWN_MASK));
		itemAddP.setIcon(new ImageIcon("src/icons/save.png"));
		itemAddP.addActionListener(this::addProduct);
		menuFile2.add(itemAddP);
		
		menuFile2.addSeparator();
		
		JMenuItem itemListProduit = new JMenuItem("Liste");
		itemListProduit.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, KeyEvent.CTRL_DOWN_MASK));
		itemListProduit.setIcon(new ImageIcon("src/icons/new.png"));
		itemListProduit.addActionListener(this::listeProduits);
		menuFile2.add(itemListProduit);
		
		JMenuItem itemAddCmd = new JMenuItem("Creer");
		itemAddCmd.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_R, KeyEvent.CTRL_DOWN_MASK));
		itemAddCmd.setIcon(new ImageIcon("src/icons/new.png"));
		itemAddCmd.addActionListener(this::newCommande);
		menuFile3.add(itemAddCmd);
		
		menuFile3.addSeparator();
		
		JMenuItem itemPay = new JMenuItem("Payer");
		itemPay.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, KeyEvent.CTRL_DOWN_MASK));
		itemPay.setIcon(new ImageIcon("src/icons/paste.png"));
		itemPay.addActionListener(this::payCommande);
		menuFile3.add(itemPay);
		
		menuFile3.addSeparator();
		
		JMenuItem itemModCmd = new JMenuItem("Modifier");
		itemModCmd.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q, KeyEvent.CTRL_DOWN_MASK));
		itemModCmd.setIcon(new ImageIcon("src/icons/cut.png"));
		itemModCmd.addActionListener(this::modifyCommande);
		menuFile3.add(itemModCmd);
		
		menuFile3.addSeparator();
		
		JMenuItem itemDelCmd = new JMenuItem("Supprimer");
		itemDelCmd.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_H, KeyEvent.CTRL_DOWN_MASK));
		itemDelCmd.setIcon(new ImageIcon("src/icons/exit.png"));
		itemDelCmd.addActionListener(this::delCommande);
		menuFile3.add(itemDelCmd);
		
		menuFile3.addSeparator();
		
		JMenuItem itemSearchCmd = new JMenuItem("Rechercher");
		itemSearchCmd.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_I, KeyEvent.CTRL_DOWN_MASK));
		itemSearchCmd.setIcon(new ImageIcon("src/icons/about.png"));
		itemSearchCmd.addActionListener(this::searchCommande);
		menuFile3.add(itemSearchCmd);
		
		menuFile3.addSeparator();
		
		JMenuItem itemFact = new JMenuItem("Editer Facture");
		itemFact.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_B, KeyEvent.CTRL_DOWN_MASK));
		itemFact.setIcon(new ImageIcon("src/icons/save.png"));
		itemFact.addActionListener(this::editCommande);
		menuFile3.add(itemFact);
		
		JMenuItem itemStats = new JMenuItem("StatsProduit");
		itemStats.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q, KeyEvent.CTRL_DOWN_MASK));
		itemStats.setIcon(new ImageIcon("src/icons/about.png"));
		itemStats.addActionListener(this::listStatsProduit);
		menuFile4.add(itemStats);
		
		menuFile4.addSeparator();
		
		JMenuItem itemStatsCA = new JMenuItem("StatsChiffreAffaire");
		itemStatsCA.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F, KeyEvent.CTRL_DOWN_MASK));
		itemStatsCA.setIcon(new ImageIcon("src/icons/about.png"));
		itemStatsCA.addActionListener(this::listStatsAffaire);
		menuFile4.add(itemStatsCA);
		
		menuFile4.addSeparator();
		
		JMenuItem itemEdit = new JMenuItem("Editer");
		itemEdit.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_D, KeyEvent.CTRL_DOWN_MASK));
		itemEdit.setIcon(new ImageIcon("src/icons/save.png"));
		itemEdit.addActionListener(this::editStats);
		menuFile4.add(itemEdit);
		
		JMenuItem itemDisconnect = new JMenuItem("Quit");
		itemDisconnect.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X, KeyEvent.CTRL_DOWN_MASK));
		itemDisconnect.setIcon(new ImageIcon("src/icons/exit.png"));
		itemDisconnect.addActionListener(e -> {
			try {
				deconnection(e);
			} catch (IOException err) {
				// TODO Auto-generated catch block
				err.printStackTrace();
			}
		});
		menuFile5.add(itemDisconnect);
		

		return menuBar;
	}
	
	/* METHODES POUR LES CLIENTS */
	
	private void ajoutClient(ActionEvent e) {
		sc.ajoutClient();   
	}
	
 	private void modifierClient(ActionEvent e) {
 		sc.modifierClient();
		}
 	
	private void archiverClient(ActionEvent e) {
		 sc.archiverClient();
		}
	
	private void desarchiverClient(ActionEvent e) {
		 sc.desarchiverClient();
		}
	
	private void listeClients(ActionEvent e) {
		sc.listeClient(this.panel);
		this.revalidate();
	}
	
	private void supprimerClient(ActionEvent e) {
		sc.supprimerClient();
	}
	
	private void commandesClient(ActionEvent e) {
		sc.NumeroCommandesClient(this.panel);
		this.revalidate();
	}
	
	private void exportClient(ActionEvent e) {
		sc.exporterClient();
	}
	
	
	/* METHODES POUR LES PRODUITS */
	
	private void addProduct(ActionEvent e) {
		sp.ajouterProduit();
	}
	
	private void listeProduits(ActionEvent e) {
		sp.listeProduits(this.panel);
		this.revalidate();
	}
	
	/* METHODES POUR LES COMMANDES */
	
	private void newCommande(ActionEvent e) {
		sm.listeProduitPourCommande();
	}
	
	private void payCommande(ActionEvent e) {
		sm.payCommand();
	}
	
	private void modifyCommande(ActionEvent e) {
		
	}
	
	private void delCommande(ActionEvent e) {
		sm.delCommande();
	}
	
	private void searchCommande(ActionEvent e) {
		sm.searchCommande();
	}
	
	private void editCommande(ActionEvent e) {
		sm.editCommande();
	}
	
	/* METHODES POUR LES STATISTIQUES */
	
	private void listStatsProduit(ActionEvent e) {
		st.listStatsProduit();
	}
	
	private void listStatsAffaire(ActionEvent e) {
		st.choix();
	}
	
	private void editStats(ActionEvent e) {
		st.editStats();
	}
	
	private void deconnection(ActionEvent e) throws IOException {
		this.setVisible(false);
		vue.auth();
	}
	
	public static void main(String[] args) throws UnsupportedLookAndFeelException, IOException {
		UIManager.setLookAndFeel(new NimbusLookAndFeel());
		VueSwing window = new VueSwing();
		window.setVisible(true);
	}
}
