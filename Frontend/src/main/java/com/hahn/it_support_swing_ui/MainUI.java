package com.hahn.it_support_swing_ui;

import net.miginfocom.swing.MigLayout;
import javax.swing.*;
import org.json.JSONArray;
import org.json.JSONObject;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import com.hahn.it_support_swing_ui.auth.JWTUser;
import com.hahn.it_support_swing_ui.model.AuditLog;
import com.hahn.it_support_swing_ui.model.CommentResponseDTO;
import com.hahn.it_support_swing_ui.model.TicketResponseDTO;
import com.hahn.it_support_swing_ui.utils.PairReturn;

public class MainUI {

    // Constants and Variables
    private JFrame frame;
    private CardLayout cardLayout;
    private JPanel mainPanel;
    private static final String BASE_URL = "http://localhost:8080";
    private JWTUser jwtUser = new JWTUser();
    private JScrollPane scrollPane;

    // Constructor
    public MainUI() {
        initializeFrame();
        initializeMainPanel();
        cardLayout.show(mainPanel, "login"); 
        frame.setVisible(true);
    }

    // Initialization Methods
    private void initializeFrame() {
        frame = new JFrame("Support Ticket System");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1600, 1600);
        frame.setLocationRelativeTo(null);
    }

    private void initializeMainPanel() {
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        mainPanel.add(createLoginPanel(), "login");
        mainPanel.add(createAdminPanel(), "admin");
        mainPanel.add(createEmployeePanel(), "employee");
        mainPanel.add(createTicketPanel(), "createTicket");
        mainPanel.add(createEmployeeTicketsPanel(), "yourTickets");
        mainPanel.add(createItSupportPanel(), "it_support");
        mainPanel.add(createItSupportTicketsPanel(), "viewTicketsSupport");
        mainPanel.add(createItSupportAuditPanel(), "yourAudit");

        scrollPane = new JScrollPane(mainPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

        frame.add(scrollPane);
    }

    // Panel Creation Methods
    private JPanel createLoginPanel() {
        JPanel loginPanel = new JPanel(new MigLayout("", "[][grow]", "[][]"));
        JLabel userLabel = new JLabel("Username:");
        JTextField userField = new JTextField(20);
        JLabel passLabel = new JLabel("Password:");
        JPasswordField passField = new JPasswordField(20);
        JButton loginButton = new JButton("Login");

        loginButton.addActionListener(e -> authenticateUser(userField.getText(), new String(passField.getPassword())));

        loginPanel.add(userLabel);
        loginPanel.add(userField, "wrap, growx");
        loginPanel.add(passLabel);
        loginPanel.add(passField, "wrap, growx");
        loginPanel.add(loginButton, "span, center");

        return loginPanel;
    }

    private JPanel createAdminPanel() {
        JPanel adminPanel = new JPanel(new MigLayout("", "[grow]", "[]20[]20[]"));
        JLabel titleLabel = new JLabel("Admin Dashboard");
        JLabel userLabel = new JLabel("Username:");
        JTextField userField = new JTextField(20);
        JLabel passLabel = new JLabel("Password:");
        JPasswordField passField = new JPasswordField(20);
        JLabel userDeleteLabel = new JLabel("Username:");
        JTextField userDeleteField = new JTextField(20);
        JLabel roleLabel = new JLabel("Role:");
        String[] roles = {"EMPLOYEE", "IT_SUPPORT"};
        JComboBox<String> roleComboBox = new JComboBox<>(roles);
        JButton createUserButton = new JButton("Create User");
        JButton deleteUserButton = new JButton("Delete User");
        JButton logoutButton = new JButton("Logout");

        logoutButton.addActionListener(e -> cardLayout.show(mainPanel, "login"));
        createUserButton.addActionListener(e -> createUser(userField.getText(), new String(passField.getPassword()), roles[roleComboBox.getSelectedIndex()]));
        deleteUserButton.addActionListener(e -> deleteUser(userField.getText()));

        adminPanel.add(titleLabel, "wrap, center");
        adminPanel.add(userLabel);
        adminPanel.add(userField);
        adminPanel.add(passLabel);
        adminPanel.add(passField);
        adminPanel.add(roleLabel);
        adminPanel.add(roleComboBox);
        adminPanel.add(createUserButton, "wrap");
        adminPanel.add(userDeleteLabel);
        adminPanel.add(userDeleteField);
        adminPanel.add(deleteUserButton);
        adminPanel.add(logoutButton, "wrap,center");

        return adminPanel;
    }

    private JPanel createEmployeePanel() {
        JPanel employeePanel = new JPanel(new MigLayout("", "[grow]", "[]20[]"));
        JLabel titleLabel = new JLabel("Employee Dashboard");
        JButton createTicketButton = new JButton("Create Ticket");
        JButton viewTicketsButton = new JButton("View My Tickets");
        JButton logoutButton = new JButton("Logout");

        logoutButton.addActionListener(e -> cardLayout.show(mainPanel, "login"));
        createTicketButton.addActionListener(e -> cardLayout.show(mainPanel, "createTicket"));
        viewTicketsButton.addActionListener(e -> cardLayout.show(mainPanel, "yourTickets"));

        employeePanel.add(titleLabel, "wrap, center");
        employeePanel.add(createTicketButton, "wrap, growx");
        employeePanel.add(viewTicketsButton, "wrap, growx");
        employeePanel.add(logoutButton, "wrap,growx");

        return employeePanel;
    }

    private JPanel createTicketPanel() {
        JPanel TicketPanel = new JPanel(new MigLayout("", "[grow]", "[]20[]"));
        JLabel titleLabel = new JLabel("Ticket Creation Dashboard");

        JLabel ticketTitleLabel = new JLabel("Title:");
        JTextField ticketTitleField = new JTextField(20);

        JLabel DescriptionLabel = new JLabel("Description:");
        JTextField DescriptionField = new JTextField(20);

        JLabel priorityLabel = new JLabel("Priority:");
        String[] priorities = {"LOW", "MEDIUM","HIGH"};
        JComboBox<String> prioritiesComboBox = new JComboBox<>(priorities);

        JLabel categoryLabel = new JLabel("Category:");
        String[] category = {"Network", "Hardware","Software","Other"};
        JComboBox<String> categoryComboBox = new JComboBox<>(category);

        JButton createTicketButton = new JButton("Create ticket");
        JButton backButton = new JButton("Back to your Dashboard");
        JButton logoutButton = new JButton("Logout");

        logoutButton.addActionListener(e -> cardLayout.show(mainPanel, "login"));
        backButton.addActionListener(e -> cardLayout.show(mainPanel, "employee"));
        createTicketButton.addActionListener(e -> createTicket(ticketTitleField.getText(),DescriptionField.getText(),priorities[prioritiesComboBox.getSelectedIndex()],category[categoryComboBox.getSelectedIndex()]));

        TicketPanel.add(titleLabel, "wrap, center");
        TicketPanel.add(ticketTitleLabel);
        TicketPanel.add(ticketTitleField, "wrap");
        TicketPanel.add(DescriptionLabel);
        TicketPanel.add(DescriptionField, "wrap");
        TicketPanel.add(priorityLabel);
        TicketPanel.add(prioritiesComboBox, "wrap");
        TicketPanel.add(categoryLabel);
        TicketPanel.add(categoryComboBox, "wrap");
        TicketPanel.add(createTicketButton,"wrap,growx");
        TicketPanel.add(backButton,"growx");
        TicketPanel.add(logoutButton, "wrap");

        return TicketPanel;
    }

    private JPanel createEmployeeTicketsPanel() {
        PairReturn ticketsResponseReturn = getAllEmployeTickets();
        List<TicketResponseDTO> ticketResponseDTO = ticketsResponseReturn.getListTickets();
        int ticketResponseCode = ticketsResponseReturn.getResponseCode();
        
        JPanel ticketPanel = new JPanel(new MigLayout("", "[grow]", "[]20[]"));
        JLabel titleLabel = new JLabel("Employee Tickets Dashboard");
        JLabel filterLabel = new JLabel("sortBy:");
        JButton sortButton = new JButton("view Tickets");
        String[] sorts = {"ALL","NEW", "IN_PROGRESS","RESOLVED"};
        JComboBox<String> sortingComboBox = new JComboBox<>(sorts);

        ticketPanel.add(titleLabel,"growx");
        ticketPanel.add(filterLabel,"growx, left");
        ticketPanel.add(sortingComboBox,"growx");
        ticketPanel.add(sortButton,"growx, wrap");

        JButton backButton = new JButton("Back to your Dashboard");
        JButton logoutButton = new JButton("Logout");

        ticketPanel.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentShown(ComponentEvent e) {
                loadTickets(ticketPanel, sortingComboBox, sortButton, backButton, logoutButton);
            }
        });
        logoutButton.addActionListener(e -> cardLayout.show(mainPanel, "login"));
        backButton.addActionListener(e -> cardLayout.show(mainPanel, "employee"));

        ticketPanel.add(backButton,"growx");
        ticketPanel.add(logoutButton, "wrap");

        return ticketPanel;
    }

    private JPanel createItSupportPanel() {
        JPanel supportPanel = new JPanel(new MigLayout("", "[grow]", "[]20[]"));
        JLabel titleLabel = new JLabel("Support Dashboard");
        JButton createTicketButton = new JButton("view Tickets");
        JButton viewTicketsButton = new JButton("view actions History");
        JButton logoutButton = new JButton("Logout");

        logoutButton.addActionListener(e -> cardLayout.show(mainPanel, "login"));
        createTicketButton.addActionListener(e -> cardLayout.show(mainPanel, "viewTicketsSupport"));
        viewTicketsButton.addActionListener(e -> cardLayout.show(mainPanel, "yourAudit"));

        supportPanel.add(titleLabel, "wrap, center");
        supportPanel.add(createTicketButton, "wrap, growx");
        supportPanel.add(viewTicketsButton, "wrap, growx");
        supportPanel.add(logoutButton, "wrap,growx");

        return supportPanel;
    }

    private JPanel createItSupportTicketsPanel() {
        PairReturn ticketsResponseReturn = getAllEmployeTickets();
        List<TicketResponseDTO> ticketResponseDTO = ticketsResponseReturn.getListTickets();
        int ticketResponseCode = ticketsResponseReturn.getResponseCode();
        
        JPanel ticketPanel = new JPanel(new MigLayout("", "[grow]", "[]20[]"));
        JLabel titleLabel = new JLabel("support Tickets Dashboard");
        JLabel filterLabel = new JLabel("sortBy:");
        JButton sortButton = new JButton("view Tickets");
        String[] sorts = {"ALL","NEW", "IN_PROGRESS","RESOLVED"};
        JComboBox<String> sortingComboBox = new JComboBox<>(sorts);

        ticketPanel.add(titleLabel,"growx");
        ticketPanel.add(filterLabel,"growx, left");
        ticketPanel.add(sortingComboBox,"growx");
        ticketPanel.add(sortButton,"growx, wrap");

        JButton backButton = new JButton("Back to your Dashboard");
        JButton logoutButton = new JButton("Logout");

        ticketPanel.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentShown(ComponentEvent e) {
                loadTickets(ticketPanel, sortingComboBox, sortButton, backButton, logoutButton);
            }
        });
        logoutButton.addActionListener(e -> cardLayout.show(mainPanel, "login"));
        backButton.addActionListener(e -> cardLayout.show(mainPanel, "it_support"));

        ticketPanel.add(backButton,"growx");
        ticketPanel.add(logoutButton, "wrap");

        return ticketPanel;
    }

    private JPanel createItSupportAuditPanel() {
        JPanel auditPanel = new JPanel(new MigLayout("", "[grow]", "[]20[]"));
        JLabel titleLabel = new JLabel("Audit Logs Dashboard");
        JButton backButton = new JButton("Back to your Dashboard");
        JButton logoutButton = new JButton("Logout");

        auditPanel.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentShown(ComponentEvent e) {
                getLogs(auditPanel);
            }
        });
        logoutButton.addActionListener(e -> cardLayout.show(mainPanel, "login"));
        backButton.addActionListener(e -> cardLayout.show(mainPanel, "it_support"));
        auditPanel.add(titleLabel,"growx, wrap");
        auditPanel.add(backButton, "growx");
        auditPanel.add(logoutButton, "wrap");

        return auditPanel;
    }

    // Utility Methods
    private void authenticateUser(String username, String password) {
        try {
            URL url = new URL(BASE_URL + "/auth/login");
            JSONObject credentials = new JSONObject();
            credentials.put("username", username);
            credentials.put("password", password);

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setDoOutput(true);

            try (OutputStream os = connection.getOutputStream()) {
                os.write(credentials.toString().getBytes());
            }

            if (connection.getResponseCode() == 200) {
                String response;
                try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                    response = reader.lines().reduce("", (acc, line) -> acc + line);
                }
                JSONObject jsonResponse = new JSONObject(response);
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
                jwtUser.setExpiresIn(sdf.parse(jsonResponse.getString("expiresIn")));
                jwtUser.setRole(jsonResponse.getString("role"));
                jwtUser.setUsername(jsonResponse.getString("username"));
                jwtUser.setToken(jsonResponse.getString("token"));
                
                if (jwtUser.getUsername().toUpperCase().equals("ADMIN")) {
                    cardLayout.show(mainPanel, "admin");
                } else {
                    switch (jwtUser.getRole().toUpperCase()) {
                        case "IT_SUPPORT":
                            cardLayout.show(mainPanel, "it_support");
                            break;
                        default:
                            cardLayout.show(mainPanel, "employee");
                            break;
                    }
                }
            } else {
                JOptionPane.showMessageDialog(frame, "Invalid credentials.", "Login Failed", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(frame, "Error: " + ex.getMessage(), "Exception", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void createUser(String username, String password, String role) {
        try {
            URL url = new URL(BASE_URL + "/employees");
            JSONObject usr = new JSONObject();
            usr.put("username", username);
            usr.put("password", password);
            usr.put("role", role);

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("Authorization", "Bearer " + jwtUser.getToken());
            connection.setDoOutput(true);

            try (OutputStream os = connection.getOutputStream()) {
                os.write(usr.toString().getBytes());
            }

            switch (connection.getResponseCode()) {
                case 201:
                    String response;
                    try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                        response = reader.lines().reduce("", (acc, line) -> acc + line);
                    }
                    JSONObject jsonResponse = new JSONObject(response);                
                    JOptionPane.showMessageDialog(frame, jsonResponse.getString("username") + " added as " + jsonResponse.getString("role"), "user Created", JOptionPane.INFORMATION_MESSAGE);
                    break;
                case 409:
                    JOptionPane.showMessageDialog(frame, username + " already Exists", "User Created Failed", JOptionPane.ERROR_MESSAGE);
                    break;
                case 403:
                    if(jwtUser != null && jwtUser.getToken() != null ){
                        JOptionPane.showMessageDialog(frame, "refresh your login", "token refrech", JOptionPane.INFORMATION_MESSAGE);
                        cardLayout.show(mainPanel, "login");
                    }
                    break;           
                default:
                    BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    JOptionPane.showMessageDialog(frame, "Internal server error" + reader.toString() , "creation Failed", JOptionPane.ERROR_MESSAGE);
                    break;
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(frame, "Error: " + ex.getMessage(), "Exception", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void deleteUser(String username) {
        try {
            URL url = new URL(BASE_URL + "/employees/" + username);

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("DELETE");
            connection.setRequestProperty("Authorization", "Bearer " + jwtUser.getToken());

            switch (connection.getResponseCode()) {
                case 200:
                    JOptionPane.showMessageDialog(frame, username + " deleted ", "user Deleted", JOptionPane.INFORMATION_MESSAGE);
                    break;
                case 404:
                    JOptionPane.showMessageDialog(frame, username + " does not Exists", "User deletion issue", JOptionPane.ERROR_MESSAGE);
                    break;
                case 403:
                    if(jwtUser != null && jwtUser.getToken() != null){
                        JOptionPane.showMessageDialog(frame, "refresh your login", "token refrech", JOptionPane.INFORMATION_MESSAGE);
                        cardLayout.show(mainPanel, "login");
                    }
                    break;           
                default:
                    BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    JOptionPane.showMessageDialog(frame, "Internal server error" + reader.toString() , "deletetion Failed", JOptionPane.ERROR_MESSAGE);
                    break;
            }            
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(frame, "Error: " + ex.getMessage(), "Exception", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void createTicket(String title, String description, String priority, String category) {
        try {
            URL url = new URL(BASE_URL + "/tickets");
            JSONObject ticket = new JSONObject();
            ticket.put("title", title);
            ticket.put("description", description);
            ticket.put("priority", priority);
            ticket.put("category", category);

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("Authorization", "Bearer " + jwtUser.getToken());
            connection.setDoOutput(true);

            try (OutputStream os = connection.getOutputStream()) {
                os.write(ticket.toString().getBytes());
            }

            switch (connection.getResponseCode()) {
                case 201:
                    JOptionPane.showMessageDialog(frame, "ticket : " + title + " created with " + priority + " priority", "ticket created", JOptionPane.INFORMATION_MESSAGE);
                    break;
                case 404:
                    JOptionPane.showMessageDialog(frame, jwtUser.getUsername() + " does not Exists", "ticket creation issue", JOptionPane.ERROR_MESSAGE);
                    break;
                case 403:
                    if(jwtUser != null && jwtUser.getToken() != null){
                        JOptionPane.showMessageDialog(frame, "refresh your login", "token refrech", JOptionPane.INFORMATION_MESSAGE);
                        cardLayout.show(mainPanel, "login");
                    }
                    break;           
                default:
                    BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    JOptionPane.showMessageDialog(frame, "Internal server error" + reader.toString() , "ticket creation Failed", JOptionPane.ERROR_MESSAGE);
                    break;
            }            
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(frame, "Error: " + ex.getMessage(), "Exception", JOptionPane.ERROR_MESSAGE);
        }
    }

    private PairReturn getAllEmployeTickets() {
        try {
            List<TicketResponseDTO> listTicketResponseDTO = new ArrayList<>();
            URL url = new URL(BASE_URL + "/tickets");

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Authorization", "Bearer " + jwtUser.getToken());

            switch (connection.getResponseCode()) {
                case 200:
                    String response;
                    try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                        response = reader.lines().reduce("", (acc, line) -> acc + line);
                    }
                    JSONArray ticketsAJsonArray = new JSONArray(response);
                    List<JSONObject> ticketsJsonResponse = new ArrayList<>();
                    for (int i = 0; i < ticketsAJsonArray.length(); i++) {
                        ticketsJsonResponse.add(ticketsAJsonArray.getJSONObject(i));
                        
                        TicketResponseDTO ticketResponseDTO = new TicketResponseDTO();
                        ticketResponseDTO.setTicketId(ticketsJsonResponse.get(i).getLong("ticketId"));
                        ticketResponseDTO.setTitle(ticketsJsonResponse.get(i).getString("title"));
                        ticketResponseDTO.setDescription(ticketsJsonResponse.get(i).getString("description"));
                        ticketResponseDTO.setPriority(ticketsJsonResponse.get(i).getString("priority"));
                        ticketResponseDTO.setCategory(ticketsJsonResponse.get(i).getString("category"));
                        ticketResponseDTO.setStatus(ticketsJsonResponse.get(i).getString("status"));
                        ticketResponseDTO.setCreationDate(ticketsJsonResponse.get(i).getString("creationDate"));

                        JSONArray commentsAJsonArray = ticketsJsonResponse.get(i).getJSONArray("ticketComments");
                        List<JSONObject> commentsJsonResponse = new ArrayList<>();
                        List<CommentResponseDTO> listCommentResponseDTO = new ArrayList<>();
                        for (int j = 0; j < commentsAJsonArray.length(); j++) {
                            commentsJsonResponse.add(commentsAJsonArray.getJSONObject(j));

                            CommentResponseDTO commentResponseDTO = new CommentResponseDTO();
                            commentResponseDTO.setCommentId(commentsJsonResponse.get(j).getLong("commentId"));
                            commentResponseDTO.setCommentText(commentsJsonResponse.get(j).getString("commentText"));
                            commentResponseDTO.setEmployeeName(commentsJsonResponse.get(j).getString("employeeName"));
                            commentResponseDTO.setCommentDate(commentsJsonResponse.get(j).getString("commentDate"));

                            listCommentResponseDTO.add(commentResponseDTO);
                        }
                        ticketResponseDTO.setTicketComments(listCommentResponseDTO);
                        listTicketResponseDTO.add(ticketResponseDTO);
                    }
                    return new PairReturn(listTicketResponseDTO, 200);
                case 404:
                    JOptionPane.showMessageDialog(frame, jwtUser.getUsername() + " does not Exists", "tickets list issue", JOptionPane.ERROR_MESSAGE);
                    return new PairReturn(new ArrayList<>(), 404);
                case 403:
                    if(jwtUser != null && jwtUser.getToken() != null){
                        JOptionPane.showMessageDialog(frame, "refresh your login", "token refrech", JOptionPane.INFORMATION_MESSAGE);
                        cardLayout.show(mainPanel, "login");
                    }
                    return new PairReturn(new ArrayList<>(), 403);         
                default:
                    BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    JOptionPane.showMessageDialog(frame, "Internal server error" + reader.toString() , "tickets list Failed", JOptionPane.ERROR_MESSAGE);
                    return new PairReturn(new ArrayList<>(), 400);
            }            
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(frame, "Error: " + ex.getMessage(), "Exception", JOptionPane.ERROR_MESSAGE);
        }
        return new PairReturn(new ArrayList<>(), 400);
    }

    public void updateStatus(String ticketStatus, Long ticketId) {
        try {
            URL url = new URL(BASE_URL + "/tickets/status/" + ticketId + "?status=" + ticketStatus);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("PUT");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("Authorization", "Bearer " + jwtUser.getToken());

            switch (connection.getResponseCode()) {
                case 200:         
                    JOptionPane.showMessageDialog(frame, "status updated successfully to" + ticketStatus, "status updated", JOptionPane.INFORMATION_MESSAGE);
                    break;
                case 404:
                    JOptionPane.showMessageDialog(frame, "ticket not found", "status update Failed", JOptionPane.ERROR_MESSAGE);
                    break;
                case 403:
                    if(jwtUser != null && jwtUser.getToken() != null){
                        JOptionPane.showMessageDialog(frame, "refresh your login", "token refrech", JOptionPane.INFORMATION_MESSAGE);
                        cardLayout.show(mainPanel, "login");
                    }
                    break;           
                default:
                    BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    JOptionPane.showMessageDialog(frame, "Internal server error" + reader.toString() , "creation Failed", JOptionPane.ERROR_MESSAGE);
                    break;
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(frame, "Error: " + ex.getMessage(), "Exception", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void addComment(String commentTexettArea, Long ticketId) {
        try {
            URL url = new URL(BASE_URL + "/tickets/comments/" + ticketId);
            JSONObject usr = new JSONObject();
            usr.put("commentText", commentTexettArea);

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("PUT");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("Authorization", "Bearer " + jwtUser.getToken());
            connection.setDoOutput(true);

            try (OutputStream os = connection.getOutputStream()) {
                os.write(usr.toString().getBytes());
            }
            switch (connection.getResponseCode()) {
                case 201:         
                    JOptionPane.showMessageDialog(frame, "comment added successfully ", "comment added", JOptionPane.INFORMATION_MESSAGE);
                    break;
                case 404:
                    JOptionPane.showMessageDialog(frame, "ticket not found", "status update Failed", JOptionPane.ERROR_MESSAGE);
                    break;
                case 403:
                    if(jwtUser != null && jwtUser.getToken() != null){
                        JOptionPane.showMessageDialog(frame, "refresh your login", "token refrech", JOptionPane.INFORMATION_MESSAGE);
                        cardLayout.show(mainPanel, "login");
                    }
                    break;           
                default:
                    BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    JOptionPane.showMessageDialog(frame, "Internal server error" + reader.toString() , "creation Failed", JOptionPane.ERROR_MESSAGE);
                    break;
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(frame, "Error: " + ex.getMessage(), "Exception", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void getLogs(JPanel auditPanel) {
        try {
            auditPanel.removeAll();
            JLabel titleLabel = new JLabel("Audit Logs Dashboard");
            JButton backButton = new JButton("Back to your Dashboard");
            JButton logoutButton = new JButton("Logout");
            
            auditPanel.add(titleLabel,"growx, wrap");
            auditPanel.add(backButton, "growx");
            auditPanel.add(logoutButton, "wrap");

            logoutButton.addActionListener(e -> cardLayout.show(mainPanel, "login"));
            backButton.addActionListener(e -> cardLayout.show(mainPanel, "it_support"));

            List<AuditLog> listAuditResponseDTO = new ArrayList<>();
            URL url = new URL(BASE_URL + "/audit/logs");

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Authorization", "Bearer " + jwtUser.getToken());

            switch (connection.getResponseCode()) {
                case 200:
                    String response;
                    try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                        response = reader.lines().reduce("", (acc, line) -> acc + line);
                    }
                    JSONArray logsJsonArray = new JSONArray(response);
                    for (int i = 0; i < logsJsonArray.length(); i++) {
                        AuditLog auditLog = new AuditLog();
                        JSONObject audit = logsJsonArray.getJSONObject(i);
                        auditLog.setAction(audit.getString("action"));
                        auditLog.setActionDate(audit.getString("actionDate"));
                        auditLog.setEmployeeName(audit.getString("employeeName"));
                        auditLog.setTicketId(audit.getLong("ticketId"));
                        
                        listAuditResponseDTO.add(auditLog);
                    }
                    JSeparator separator10 = new JSeparator(SwingConstants.HORIZONTAL);
                    auditPanel.add(separator10, "growx");
                    JSeparator separator11 = new JSeparator(SwingConstants.HORIZONTAL);
                    auditPanel.add(separator11, "growx");
                    JSeparator separator12 = new JSeparator(SwingConstants.HORIZONTAL);
                    auditPanel.add(separator12, "growx");
                    JSeparator separator13 = new JSeparator(SwingConstants.HORIZONTAL);
                    auditPanel.add(separator13, "growx, wrap");
        
                    for (AuditLog auditLog : listAuditResponseDTO) {
                        JTextArea Auditdescription = new JTextArea("action : " + auditLog.getAction());
                        Auditdescription.setLineWrap(true);
                        Auditdescription.setBackground(new Color(255,255,255));
        
                        JTextArea AuditEmployee = new JTextArea("performedBy " + auditLog.getEmployeeName());
                        JTextArea AuditTicket = new JTextArea("on ticketId " + auditLog.getTicketId().toString());
                        JTextArea AuditDate = new JTextArea("At " + auditLog.getActionDate());
        
                        auditPanel.add(Auditdescription,"growx");
                        auditPanel.add(AuditEmployee,"growx");
                        auditPanel.add(AuditTicket,"growx");
                        auditPanel.add(AuditDate,"growx, wrap");
        
                        JSeparator separator101 = new JSeparator(SwingConstants.HORIZONTAL);
                        auditPanel.add(separator101, "growx");
                        JSeparator separator111 = new JSeparator(SwingConstants.HORIZONTAL);
                        auditPanel.add(separator111, "growx");
                        JSeparator separator121 = new JSeparator(SwingConstants.HORIZONTAL);
                        auditPanel.add(separator121, "growx");
                        JSeparator separator131 = new JSeparator(SwingConstants.HORIZONTAL);
                        auditPanel.add(separator131, "growx, wrap");
                    }
                    auditPanel.revalidate();
                    auditPanel.repaint();
                    break;
                case 403:
                    if(jwtUser != null && jwtUser.getToken() != null){
                        JOptionPane.showMessageDialog(frame, "refresh your login", "token refrech", JOptionPane.INFORMATION_MESSAGE);
                        cardLayout.show(mainPanel, "login");
                    }
                    break;
                default:
                    BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    JOptionPane.showMessageDialog(frame, "Internal server error" + reader.toString() , "tickets list Failed", JOptionPane.ERROR_MESSAGE);
                    break;
                }            
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(frame, "Error: " + ex.getMessage(), "Exception", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadTickets(JPanel ticketPanel, JComboBox<String> sortingComboBox, JButton sortButton, JButton backButton, JButton logoutButton) {
        loadTicketsLazy(ticketPanel, sortingComboBox, sortButton, backButton, logoutButton);
        sortButton.addActionListener(e -> {
            loadTicketsLazy(ticketPanel, sortingComboBox, sortButton, backButton, logoutButton);
        });
    }

    private void loadTicketsLazy(JPanel ticketPanel, JComboBox<String> sortingComboBox, JButton sortButton, JButton backButton, JButton logoutButton) {
        JLabel titleLabel = new JLabel("Employee Tickets Dashboard");
        JLabel filterLabel = new JLabel("sortBy:");
        String selectedSort = (String) sortingComboBox.getSelectedItem();
        PairReturn ticketsResponseReturn = getAllEmployeTickets();
        List<TicketResponseDTO> ticketResponseDTO = ticketsResponseReturn.getListTickets();
        if (selectedSort != null) {
            switch (selectedSort) {
                case "NEW":
                    ticketResponseDTO.sort(Comparator.comparing(TicketResponseDTO::getCreationDate).reversed());
                    break;
                case "IN_PROGRESS":
                    ticketResponseDTO.sort(Comparator.comparing(ticket -> ticket.getStatus().equals("IN_PROGRESS") ? 0 : 1));
                    break;
                case "RESOLVED":
                    ticketResponseDTO.sort(Comparator.comparing(ticket -> ticket.getStatus().equals("RESOLVED") ? 0 : 1));
                    break;
                case "ALL":
                    ticketResponseDTO.sort(Comparator.comparing(ticket -> ticket.getStatus()));
                    break;
                default:
                    break;
            }

            ticketPanel.removeAll();

            ticketPanel.add(titleLabel, "growx");
            ticketPanel.add(filterLabel, "growx");
            ticketPanel.add(sortingComboBox, "growx");
            ticketPanel.add(sortButton, "growx, wrap");

            ticketPanel.add(backButton, "growx");
            ticketPanel.add(logoutButton, "wrap");
            JSeparator separator10 = new JSeparator(SwingConstants.HORIZONTAL);
            ticketPanel.add(separator10, "growx");
            JSeparator separator11 = new JSeparator(SwingConstants.HORIZONTAL);
            ticketPanel.add(separator11, "growx");
            JSeparator separator12 = new JSeparator(SwingConstants.HORIZONTAL);
            ticketPanel.add(separator12, "growx");
            JSeparator separator13 = new JSeparator(SwingConstants.HORIZONTAL);
            ticketPanel.add(separator13, "growx, wrap");

            for (int index = 1; index <= ticketResponseDTO.size(); index++) {
                if (ticketResponseDTO.get(index - 1).getStatus().equalsIgnoreCase(selectedSort) || selectedSort == "ALL") {
                    JLabel ticketLabel = new JLabel("Ticket " + index);
                    JLabel ticketTitleLabel = new JLabel("title : " + ticketResponseDTO.get(index - 1).getTitle());
                    JLabel ticketdescriptionLabel = new JLabel("Description:");
                    JTextArea ticketdescription = new JTextArea(ticketResponseDTO.get(index - 1).getDescription());
                    ticketdescription.setBackground(new Color(255,255,255));
                    JLabel ticketpriorityLabel = new JLabel("priority : " + ticketResponseDTO.get(index - 1).getPriority());
                    JLabel ticketStatutLabel = new JLabel("status : " + ticketResponseDTO.get(index - 1).getStatus());
                    JLabel ticketCategoryLabel = new JLabel("category : " + ticketResponseDTO.get(index - 1).getCategory());
                    JLabel ticketCreationDateLabel = new JLabel("creation date : " + ticketResponseDTO.get(index - 1).getCreationDate());

                    ticketPanel.add(ticketLabel, "growx, wrap");
                    ticketPanel.add(ticketTitleLabel);
                    ticketPanel.add(ticketCategoryLabel);
                    ticketPanel.add(ticketpriorityLabel);
                    ticketPanel.add(ticketStatutLabel);
                    ticketPanel.add(ticketCreationDateLabel, "wrap");
                    ticketPanel.add(ticketdescriptionLabel);
                    ticketPanel.add(ticketdescription, "growx, wrap");

                    Long Id = ticketResponseDTO.get(index - 1).getTicketId();
                    if (jwtUser.getRole().toUpperCase().equals("IT_SUPPORT")) {
                        JButton changeStatusLabel = new JButton("change Ticket Status");
                        String[] newStatus = {"NEW", "IN_PROGRESS","RESOLVED"};
                        JComboBox<String> newStatusComboBox = new JComboBox<>(newStatus);
                        changeStatusLabel.addActionListener(event -> {
                            updateStatus(newStatusComboBox.getSelectedItem().toString(), Id);
                            loadTickets(ticketPanel, sortingComboBox, sortButton, backButton, logoutButton);
                        });
                        ticketPanel.add(newStatusComboBox);
                        ticketPanel.add(changeStatusLabel, "growx, wrap");
                    }

                    JSeparator separator = new JSeparator(SwingConstants.HORIZONTAL);
                    ticketPanel.add(separator, "growx, wrap");

                    List<CommentResponseDTO> commentList = ticketResponseDTO.get(index - 1).getTicketComments();
                    if (!commentList.isEmpty()) {
                        JLabel commentLabel = new JLabel("Comments");
                        ticketPanel.add(commentLabel, "growx, wrap");
                        for (int indexC = 1; indexC <= commentList.size(); indexC++) {
                            JLabel ticketCommentLabel = new JLabel("Comment "+ indexC);
                            JTextArea commentText = new JTextArea(commentList.get(indexC - 1).getCommentText());
                            commentText.setBackground(new Color(255,255,255));
                            JLabel commentDateLabel = new JLabel("date " + commentList.get(indexC - 1).getCommentDate());
                            JLabel commentCreatorLabel = new JLabel("agent " + commentList.get(indexC - 1).getEmployeeName());
                            ticketPanel.add(ticketCommentLabel);
                            ticketPanel.add(commentDateLabel);
                            ticketPanel.add(commentCreatorLabel,"wrap");
                            ticketPanel.add(commentText,"growx, wrap");
                        }
                    }

                    if (jwtUser.getRole().toUpperCase().equals("IT_SUPPORT")) {
                        JButton addButtonComment = new JButton("add comment");
                        TextArea commentTexettArea = new TextArea("add you comment her");

                        addButtonComment.addActionListener(event -> {
                            addComment(commentTexettArea.getText(), Id);
                            loadTickets(ticketPanel, sortingComboBox, sortButton, backButton, logoutButton);
                        });
                        ticketPanel.add(commentTexettArea,"growx");
                        ticketPanel.add(addButtonComment, "growx, wrap");
                    }

                    JSeparator separator1 = new JSeparator(SwingConstants.HORIZONTAL);
                    JSeparator separator2 = new JSeparator(SwingConstants.HORIZONTAL);
                    JSeparator separator3 = new JSeparator(SwingConstants.HORIZONTAL);
                    JSeparator separator4 = new JSeparator(SwingConstants.HORIZONTAL);
                    JSeparator separator5 = new JSeparator(SwingConstants.HORIZONTAL);
                    ticketPanel.add(separator1, "growx");
                    ticketPanel.add(separator2, "growx");
                    ticketPanel.add(separator3, "growx");
                    ticketPanel.add(separator4, "growx");
                    ticketPanel.add(separator5, "growx, wrap");
                }
            }

            ticketPanel.revalidate();
            ticketPanel.repaint();
            JViewport viewport = scrollPane.getViewport();
            viewport.setViewPosition(new Point(0, 0));
        }
    }

    // Main Method
    public static void main(String[] args) {
        SwingUtilities.invokeLater(MainUI::new);
    }
}