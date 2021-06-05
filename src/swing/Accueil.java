package swing;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.UnsupportedLookAndFeelException;

import connect.AccueilBdd;

public class Accueil extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	AccueilBdd abd = new AccueilBdd();

	ImageIcon image = new ImageIcon("./image/ezfood.jpg");
	JLabel icone = new JLabel(image);
	JFrame frame = new JFrame("Authentification");
	JPanel pan = new JPanel();
	JLabel labId = new JLabel("Identifiant     ");
	JTextField id = new JTextField(21);
	JLabel labPwd = new JLabel("Mot de passe");
	JPasswordField pass = new JPasswordField(21);

	public void auth() {
		pan.add(icone);
		pan.add(labId);
		pan.add(id);
		pan.add(labPwd);
		pan.add(pass);
		JButton jb = new JButton();
		jb.setText("SUBMIT");
		jb.addActionListener(new BoutonListener());
		pan.add(jb);
		frame.add(pan);
		frame.setSize(280,520);
		frame.setVisible(true);
		frame.setLocationRelativeTo(null);
	}

	class BoutonListener implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			String passx = String.valueOf(pass.getPassword());
			String pattern = "(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^/&+=])(?=\\S+$).{8,}";
			if (passx.matches(pattern)) {
		        MessageDigest md;
		        boolean verifPass = false;
				try {
					md = MessageDigest.getInstance("SHA-256");
			        md.update(passx.getBytes());
			        byte byteData[] = md.digest();
			        
			        StringBuffer sb = new StringBuffer();
			        for (int i = 0; i < byteData.length; i++) {
			         sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
			        }
			        verifPass = abd.verifPasswd(sb.toString());
				} catch (NoSuchAlgorithmException e2) {
					e2.printStackTrace();
				}
		        
				if (verifPass) {
					try {
						try {
							VueSwing.main(null);
							frame.dispose();
						} catch (IOException e1) {
							e1.printStackTrace();
						}
					} catch (UnsupportedLookAndFeelException e1) {
						e1.printStackTrace();
					}
				} else {
					JOptionPane.showMessageDialog(null,"Identifiant et/ou mot de passe incorrect(s)");
				}
			} else {
				JOptionPane.showMessageDialog(null,"Mot de passe trop faible");
			}

		}
	}


	public static void main(String[] args) throws UnsupportedLookAndFeelException {
		Accueil vue = new Accueil();
		vue.auth();
	}

}
