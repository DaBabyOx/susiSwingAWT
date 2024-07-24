import java.awt.*;
import javax.swing.*;
import com.github.lgooddatepicker.components.DatePicker;
import com.github.lgooddatepicker.components.DatePickerSettings;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.Random;
import java.time.LocalDate;
import java.time.Period;


class DBConnect {
    private static final String URL = "jdbc:mysql://localhost:3306/-"; //ganti nama db disini
    private static final String USER = "root"; //ganti nama disini
    private static final String PASSWORD = "-"; //ganti pw disini

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);}

    public static boolean emailcheck(String email) throws SQLException{
        String q = "SELECT email FROM users WHERE email = ?";
        try(Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(q)){
            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                return rs.getInt(1)>0;}}
        return false;}

    public static boolean vl(String nim, String password) throws SQLException{
        String q = "SELECT COUNT(*) FROM users WHERE nim = ? AND password = ?";
        try(Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(q)){
            ps.setString(1, nim);
            ps.setString(2, password);
            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                return rs.getInt(1)>0;}}
        return false;}

    public static void saveu(String fullName, String email, String birthdate, String gender, String cohort, String password, String nim) throws SQLException {
        String query = "INSERT INTO users (fullName, email, birthdate, gender, cohort, password, nim) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, fullName);
            statement.setString(2, email);
            statement.setString(3, birthdate);
            statement.setString(4, gender);
            statement.setString(5, cohort);
            statement.setString(6, password);
            statement.setString(7, nim);
            statement.executeUpdate();}}
}

class Login extends JFrame {
    final private Font mf = new Font("Serif", Font.BOLD, 14);
    final private Font confir = new Font("Serif", Font.PLAIN, 12);

    public Login() {
        logint();}

    public void logint() {
        JLabel Nim = new JLabel("Nim:");
        Nim.setFont(mf);
        JTextField tNIM = new JTextField(20);
        tNIM.setFont(mf);

        JLabel Password = new JLabel("Password:");
        Password.setFont(mf);
        JPasswordField tPassword = new JPasswordField(20);
        tPassword.setFont(mf);

        JLabel conf = new JLabel("<html><u>Don't have an account? <b>Sign Up</b></u></html>");
        conf.setFont(confir);
        conf.setForeground(Color.BLUE);
        conf.setCursor(new Cursor(Cursor.HAND_CURSOR));
        conf.addMouseListener(new java.awt.event.MouseAdapter(){
            public void mouseClicked(java.awt.event.MouseEvent evt){
                gts();}});

        JButton signInButton = new JButton("Sign In");
        signInButton.setFont(mf);
        signInButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    String nim = tNIM.getText();
                    String password = new String(tPassword.getPassword());

                    if (DBConnect.vl(nim, password)) {
                        JOptionPane.showMessageDialog(Login.this, "Login Successful!", "Success", JOptionPane.INFORMATION_MESSAGE);}
                    else{
                        JOptionPane.showMessageDialog(Login.this, "Invalid NIM or Password!", "Error", JOptionPane.ERROR_MESSAGE);}}
                catch (Exception ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(Login.this, STR."Error: \{ex.getMessage()}", "Error", JOptionPane.ERROR_MESSAGE);}}});

        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(Nim, gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        formPanel.add(tNIM, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        formPanel.add(Password, gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        formPanel.add(tPassword, gbc);

        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.EAST;
        formPanel.add(conf, gbc);

        gbc.gridx = 1;
        gbc.gridy = 3;
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.CENTER;
        formPanel.add(signInButton, gbc);

        JPanel login = new JPanel(new BorderLayout());
        login.setBackground(Color.white);

        login.add(formPanel, BorderLayout.CENTER);

        getContentPane().add(login);

        setTitle("Sign In");
        setSize(1280,720);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);}

    private void gts(){
        dispose();
        new SignUp();}}

class SignUp extends JFrame {
    final private Font mf = new Font("Serif", Font.BOLD, 14);
    final private Font confir = new Font("Serif", Font.PLAIN, 12);

    public SignUp() {
        signupnt();}

    public void signupnt() {
        JLabel fn = new JLabel("Full Name:");
        fn.setFont(mf);
        JTextField tFN = new JTextField(20);
        tFN.setFont(mf);

        JLabel Email = new JLabel("Email:");
        Email.setFont(mf);
        JTextField tEmail = new JTextField(20);
        tEmail.setFont(mf);

        JLabel birthDateLabel = new JLabel("Birthdate:");
        birthDateLabel.setFont(mf);

        DatePickerSettings ds = new DatePickerSettings();
        DatePicker bdp = new DatePicker(ds);
        bdp.setFont(mf);

        JLabel genderLabel = new JLabel("Gender:");
        genderLabel.setFont(mf);

        JRadioButton maleButton = new JRadioButton("Male");
        maleButton.setFont(mf);

        JRadioButton femaleButton = new JRadioButton("Female");
        femaleButton.setFont(mf);

        ButtonGroup genderGroup = new ButtonGroup();
        genderGroup.add(maleButton);
        genderGroup.add(femaleButton);

        JPanel genderPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        genderPanel.add(maleButton);
        genderPanel.add(femaleButton);

        JLabel cohortLabel = new JLabel("Cohort:");
        cohortLabel.setFont(mf);
        String[] cohorts = {"B23", "B24", "B25", "B26", "B27", "B28"};
        JComboBox<String> cohortBox = new JComboBox<>(cohorts);
        cohortBox.setFont(mf);

        JLabel Password = new JLabel("Password:");
        Password.setFont(mf);
        JPasswordField tPassword = new JPasswordField(20);
        tPassword.setFont(mf);

        JLabel Passwordc = new JLabel("Confirm Password:");
        Passwordc.setFont(mf);
        JPasswordField tPasswordc = new JPasswordField(20);
        tPasswordc.setFont(mf);

        JLabel conf = new JLabel("<html><u>Already have an account? <b>Sign In</b></u></html>");
        conf.setFont(confir);
        conf.setForeground(Color.BLUE);
        conf.setCursor(new Cursor(Cursor.HAND_CURSOR));
        conf.addMouseListener(new java.awt.event.MouseAdapter(){
            public void mouseClicked(java.awt.event.MouseEvent evt){
                gtl();}});

        JButton signUpButton = new JButton("Sign Up");
        signUpButton.setFont(mf);
        signUpButton.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                try{
                    String fullName = tFN.getText();
                    String email = tEmail.getText();
                    String birthdate = bdp.getDate().toString();
                    String gender = maleButton.isSelected() ? "Male" : "Female";
                    String cohort = (String) cohortBox.getSelectedItem();
                    String password = new String(tPassword.getPassword());
                    String confirmPassword = new String(tPasswordc.getPassword());

                    if (!password.equals(confirmPassword)) {
                        JOptionPane.showMessageDialog(SignUp.this, "Passwords do not match!", "Error", JOptionPane.ERROR_MESSAGE);
                        return;}

                    if (!email.contains("@")) {
                        JOptionPane.showMessageDialog(SignUp.this, "Email must have \"@\" inside it!", "Error", JOptionPane.ERROR_MESSAGE);
                        return;}

                    if (fullName.trim().split("\\s+").length < 2) {
                        JOptionPane.showMessageDialog(SignUp.this, "Full Name must contain more than one word!", "Error", JOptionPane.ERROR_MESSAGE);
                        return;}

                    if (Period.between(LocalDate.parse(birthdate), LocalDate.now()).getYears() < 16) {
                        JOptionPane.showMessageDialog(SignUp.this, "You must be at least 16 years old!", "Error", JOptionPane.ERROR_MESSAGE);
                        return;}

                    if (DBConnect.emailcheck(email)) {
                        JOptionPane.showMessageDialog(SignUp.this, "Email already exists!", "Error", JOptionPane.ERROR_MESSAGE);
                        return;}

                    String nim = genID(cohort);
                    DBConnect.saveu(fullName, email, birthdate, gender, cohort, password, nim);

                    JOptionPane.showMessageDialog(SignUp.this, STR."Your NIM: \{nim}", "Success", JOptionPane.INFORMATION_MESSAGE);
                    dispose();
                    new Login();}
                catch (Exception ex){
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(SignUp.this, STR."Error\{ex.getMessage()}", "Error", JOptionPane.ERROR_MESSAGE);}}});

        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(fn, gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        formPanel.add(tFN, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        formPanel.add(Email, gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        formPanel.add(tEmail, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        formPanel.add(birthDateLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 2;
        formPanel.add(bdp, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        formPanel.add(genderLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 3;
        formPanel.add(genderPanel, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        formPanel.add(cohortLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 4;
        formPanel.add(cohortBox, gbc);

        gbc.gridx = 0;
        gbc.gridy = 5;
        formPanel.add(Password, gbc);

        gbc.gridx = 1;
        gbc.gridy = 5;
        formPanel.add(tPassword, gbc);

        gbc.gridx = 0;
        gbc.gridy = 6;
        formPanel.add(Passwordc, gbc);

        gbc.gridx = 1;
        gbc.gridy = 6;
        formPanel.add(tPasswordc, gbc);

        gbc.gridx = 1;
        gbc.gridy = 7;
        gbc.anchor = GridBagConstraints.EAST;
        formPanel.add(conf, gbc);

        gbc.gridx = 1;
        gbc.gridy = 8;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        formPanel.add(signUpButton, gbc);

        JPanel signup = new JPanel(new BorderLayout());
        signup.setBackground(Color.white);

        signup.add(formPanel, BorderLayout.CENTER);

        getContentPane().add(signup);

        setTitle("Sign Up");
        setSize(1280, 720);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);}

    private void gtl(){
        dispose();
        new Login();}

    private String genID(String c){
        StringBuilder nim = new StringBuilder(c.substring(1));
        Random r = new Random();
        for(int i =0;i<8;i++){
            nim.append(r.nextInt(10));}
        return nim.toString();}}

public class UTSB25SWINGAWT {
    public static void main(String[] a) {
        new SignUp();}}
