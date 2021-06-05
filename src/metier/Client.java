package metier;

public class Client {
	
	private String name;
	private String prenom;
	private String mail;
	private String date;
		
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPrenom() {
		return prenom;
	}

	public void setPrenom(String prenom) {
		this.prenom = prenom;
	}

	public String getMail() {
		return mail;
	}

	public void setMail(String mail) {
		this.mail = mail;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}
	
	
	public Client(String name, String prenom, String mail, String date) {
		super();
		this.name = name;
		this.prenom = prenom;
		this.mail = mail;
		this.date = date;
	}
	

}

