package GUI;


import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.*;

import chat.Chat;
import user.User;



public class GUI {
	JFrame loginFrame;
	JFrame mainFrame;
	//Login login;
	boolean isLogged = false;
	User user;
	JScrollPane msgScrollPane; 
	JTextArea textArea;
	JPanel panel1;
	
	public GUI(User user)
	 {
		 buildGUI();
		 this.user = user;
	 }
	
	 public void buildGUI() {
		 
		 createLoginFrame();
		 
	 }
	 
	 public void createLoginFrame() {
		 loginFrame = new JFrame();
		 
		 //set frame size
		 loginFrame.setSize(450, 700);
		 loginFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		 
		 createUserNPassFrame();
		 
		 //make frame visible
		 loginFrame.setVisible(true);
		 
	 }
	 public void createUserNPassFrame() {
		 
		 JLabel userLabel = new JLabel("Username");
		 JTextField usernameField = new JTextField(16);
		 
		 JLabel passLabel = new JLabel("passsword");
		 JPasswordField passwordField = new JPasswordField(16);
		 
		 JButton submitB = new JButton("Login");
		 
		 JLabel welcomeLabel = new JLabel("Welcome", SwingConstants.CENTER);
		 welcomeLabel.setFont(new Font("Arial", Font.BOLD, 28));
		 
		 
		 submitB.addActionListener(e -> {
			    String username = usernameField.getText();
			    String password = new String(passwordField.getPassword());

			    //System.out.println(username + "\n"+ password);
			    verifyLogin(username, password);
			});
		 
		 //layout
		 JPanel formPanel = new JPanel();
	     formPanel.setLayout(new GridLayout(5, 1, 0, 1));
	     formPanel.add(welcomeLabel);
	     formPanel.add(userLabel);
	     formPanel.add(usernameField);
	     formPanel.add(passLabel);
	     formPanel.add(passwordField);
	     
	     //combine login
	     JPanel mainPanel = new JPanel(new BorderLayout(0, 10));
	     mainPanel.add(formPanel, BorderLayout.CENTER);
	     mainPanel.add(submitB, BorderLayout.SOUTH);

	     JPanel centerPanel = new JPanel(new GridBagLayout());
	     centerPanel.add(mainPanel);
	     
	     //loginFrame.add(topPanel, BorderLayout.NORTH);
	     loginFrame.add(centerPanel, BorderLayout.CENTER);
		 
	 }
	 public void verifyLogin(String username, String password) {
		 System.out.println("verifying");
		 
		 /*
		 User user = new User(username, password);
		 
		 isLogged = client.login(user, .....);
		 isLogged = user.authenticateLogin(username, password);
		 System.out.println(isLogged);
		 System.out.println(username + "\n"+ password);
		 
		 if(!isLogged) {
			 JOptionPane.showMessageDialog(loginFrame, "Login failed");
			 return;
		 }
		 */
		 loginFrame.dispose();
		 createMainFrame();
		 
	 }
	 
	 public void createMainFrame() {
		 mainFrame = new JFrame();
		 
		 mainFrame.setSize(750, 700);
		 mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		 //mainFrame.setLocationRelativeTo(null);
		 
		 createLeftMainPanel();
		 createRightMainPanel();
		 mainFrame.setVisible(true);
		 
	 }
	 
	 private void createLeftMainPanel() {
		 JPanel leftPanel = new JPanel();
		 leftPanel.setPreferredSize(new Dimension(250, 0));
		 leftPanel.setBorder(BorderFactory.createTitledBorder("Left Panel"));	//for testin remove after 
		
		 //top
		 JPanel addChatPanel = new JPanel();
		 addChatPanel.setPreferredSize(new Dimension(240, 50));
		 addChatPanel.setBorder(BorderFactory.createTitledBorder("addchatPanel"));
		 
		 //middle
		 JScrollPane optionScrollPane = new JScrollPane();		//hold all the people user had message
		 optionScrollPane.setBorder(BorderFactory.createTitledBorder("scroll Panel"));		//for testing
		 optionScrollPane.setPreferredSize(new Dimension(240, 530));
		 
		 //display chatList
		 
		 JPanel chatListPanel = new JPanel();
		 chatListPanel.setLayout(new BoxLayout(chatListPanel, BoxLayout.Y_AXIS));
		 chatListPanel.setBorder(BorderFactory.createTitledBorder("right Panel"));
		 
		 for (int i = 0; i < user.getChatList().getNumChat(); i++) {
			    Chat chat = user.getChatList().getChat(i);
			    //final Chat currentChat = chat;

			    JButton chatButton = new JButton("Chat " + i);
			    chatButton.setMaximumSize(new Dimension(220, 40));

			    chatButton.addActionListener(e -> {
			    displayChat(chat);
			    
			    });
			    chatListPanel.add(chatButton);
			}
		 
		 optionScrollPane.setViewportView(chatListPanel);
		 

		 //bottom
		 JPanel leftBottomPane = new JPanel();
		 leftBottomPane.setPreferredSize(new Dimension(240, 50));
		 leftBottomPane.setBackground(new Color(163, 177, 138));
		 
		 JPanel textPanel = new JPanel();
	     textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
	     textPanel.setPreferredSize(new Dimension(130, 40));
	     textPanel.setOpaque(false);
	     
	     
		 JLabel pic = new JLabel();
	     pic.setPreferredSize(new Dimension(40, 30));
	     pic.setBackground(new Color(255, 192, 203));
	     pic.setOpaque(true);
	     
	     JLabel nameLabel = new JLabel(user.getUsername());	//pass in users name
	     nameLabel.setForeground(Color.WHITE);
	     nameLabel.setFont(nameLabel.getFont().deriveFont(Font.BOLD, 14f));

	     JLabel subLabel = new JLabel("view pfp");
	     subLabel.setForeground(Color.GRAY);
	     subLabel.setFont(subLabel.getFont().deriveFont(12f));
	     
	     textPanel.add(nameLabel);
	     textPanel.add(subLabel);
	     
	     leftBottomPane.add(pic, BorderLayout.WEST);
	     leftBottomPane.add(textPanel, BorderLayout.CENTER);

	     leftBottomPane.addMouseListener(new MouseAdapter() {
	    	 public void mouseClicked(MouseEvent e) {
	    		 createProfileMenu().show(leftBottomPane, e.getX(), e.getY());
	    	 }

			 
	     });
	     
	     
	     
	     
	     
	     leftPanel.add(addChatPanel);
		 leftPanel.add(optionScrollPane);
		 leftPanel.add(leftBottomPane);
		 
		 
		 
		 //adding to main frame
		 mainFrame.add(leftPanel, BorderLayout.WEST);
	     
		 
	 }
	 
	 private void createRightMainPanel() {
		 JPanel rightPanel = new JPanel();
		 rightPanel.setBorder(BorderFactory.createTitledBorder("right Panel"));	//for testing remove after
		 rightPanel.setLayout(new BoxLayout(rightPanel,BoxLayout.Y_AXIS ));
		 
		 JScrollPane msgScrollPane = new JScrollPane();
		 msgScrollPane.setBorder(BorderFactory.createTitledBorder("scroll Panel"));		//for testing
		 msgScrollPane.setPreferredSize(new Dimension(10, 700));
		 
		 panel1 = new JPanel();
		 panel1.setLayout(new BoxLayout(panel1, BoxLayout.Y_AXIS));
		 panel1.setBorder(BorderFactory.createTitledBorder("Panel1"));
         
		 
		 
		 msgScrollPane.setViewportView(panel1);
		 
		 
		 
		 JTextField inputField = new JTextField();
		 JButton sendButton = new JButton("Send");

		 
		 JPanel rightBottomPanel = new JPanel(new BorderLayout());
		 rightBottomPanel.add(inputField, BorderLayout.CENTER);
		 rightBottomPanel.add(sendButton, BorderLayout.EAST);
		 
		 rightPanel.add(msgScrollPane);
		 rightPanel.add(rightBottomPanel);
		 
		 mainFrame.add(rightPanel, BorderLayout.CENTER);
	 }
	 
	 
	 
	 
	 
	 public void displayChat(Chat chat) {
		 panel1.removeAll();
		 
		 for(int i = 0; i< chat.getNumMessages(); i++) {
			 
			 JPanel msgPanel = new JPanel(new BorderLayout());
		 
			 JTextArea textArea = new JTextArea(chat.getMessage(i).getText());
			 textArea.setBorder(BorderFactory.createTitledBorder("box"));	
			 textArea.setLineWrap(true);
			 textArea.setWrapStyleWord(true);
			 textArea.setEditable(false);
			 
			 textArea.setColumns(15);  
			 //textArea.setRows(5);
			 textArea.setSize(textArea.getPreferredSize());
			 msgPanel.setMaximumSize(msgPanel.getPreferredSize());
			 
			 if(chat.getMessage(i).getUsername().equals(user.getUsername())) {
				 msgPanel.add(textArea, BorderLayout.EAST);
				 
			 }else {
				 msgPanel.add(textArea, BorderLayout.WEST);
				 
				 
			 }
			 
			 msgPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, msgPanel.getPreferredSize().height));
			 //msgPanel.setMaximumSize(msgPanel.getPreferredSize());
			 panel1.add(msgPanel);
		 }
		 panel1.revalidate();
		 panel1.repaint();
		 
	}
	
	 private JPopupMenu createProfileMenu() {
		JPopupMenu profile = new JPopupMenu();
		//JMenuItem myProfile = new JMenuItem("My Profile"); //add if have time
	    JMenuItem audit = new JMenuItem("Audit Mode");
	    JMenuItem logout = new JMenuItem("Logout");
	    
		//myProfile.addActionListener(e -> openProfilePage());
		//audit.addActionListener(e -> openSettingsPage());
		//logout.addActionListener(e -> logoutUser());
		
		//profile.add(myProfile);
	    //if(user.isI));
	    profile.add(audit);
	    profile.addSeparator();
	    profile.add(logout);
		
		return profile;
	}
}