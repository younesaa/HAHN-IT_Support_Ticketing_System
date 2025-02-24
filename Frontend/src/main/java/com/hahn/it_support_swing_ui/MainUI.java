package com.hahn.it_support_swing_ui;

import net.miginfocom.swing.MigLayout;
import javax.swing.*;

import org.json.JSONArray;
import org.json.JSONObject;

import java.awt.CardLayout;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import com.hahn.it_support_swing_ui.auth.JWTUser;
import com.hahn.it_support_swing_ui.model.CommentResponseDTO;
import com.hahn.it_support_swing_ui.model.TicketResponseDTO;
import com.hahn.it_support_swing_ui.utils.PairReturn;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

public class MainUI {

    private JFrame frame;
    private CardLayout cardLayout;
    private JPanel mainPanel;
    final static String BASE_URL = "http://localhost:8080";
    JWTUser jwtUser = new JWTUser();

    public MainUI() {
        
        frame = new JFrame("Support Ticket System");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        frame.setLocationRelativeTo(null);

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);


        mainPanel.add(createLoginPanel(), "login");

        mainPanel.add(createAdminPanel(), "admin");

        mainPanel.add(createEmployeePanel(), "employee");
        mainPanel.add(createTicketPanel(), "createTicket");
        mainPanel.add(createEmployeeTicketsPanel(), "yourTickets");


        mainPanel.add(createItSupportPanel(), "it_support");
        frame.add(mainPanel);
        frame.setVisible(true);
    }

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
    private void authenticateUser(String username, String password) {
        try{
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
                
                if(jwtUser.getUsername().toUpperCase().equals("ADMIN")){
                    cardLayout.show(mainPanel, "admin");
                }else{
                    switch (jwtUser.getRole().toUpperCase()) {
                        case "IT_SUPPORT":
                            cardLayout.show(mainPanel, "it_support");
                            break;
                        default:
                            cardLayout.show(mainPanel, "employee");
                            break;
                    }
                }
            }else{
                JOptionPane.showMessageDialog(frame, "Invalid credentials.", "Login Failed", JOptionPane.ERROR_MESSAGE);
            }

        }
        catch (Exception ex) {
            JOptionPane.showMessageDialog(frame, "Error: " + ex.getMessage(), "Exception", JOptionPane.ERROR_MESSAGE);
        }
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
        createUserButton.addActionListener(e -> createUser(userField.getText(),new String(passField.getPassword()),roles[roleComboBox.getSelectedIndex()]));
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
    public void createUser(String username, String password, String role){
        try{
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
                    JOptionPane.showMessageDialog(frame, "refresh your login", "token refrech", JOptionPane.INFORMATION_MESSAGE);
                    cardLayout.show(mainPanel, "login");
                    break;           
                default:
                    BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    JOptionPane.showMessageDialog(frame, "Internal server error" + reader.toString() , "creation Failed", JOptionPane.ERROR_MESSAGE);
                    break;
            }
        }
        catch (Exception ex) {
            JOptionPane.showMessageDialog(frame, "Error: " + ex.getMessage(), "Exception", JOptionPane.ERROR_MESSAGE);
        }
    }
    public void deleteUser(String username){
        try{
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
                    JOptionPane.showMessageDialog(frame, "refresh your login", "token refrech", JOptionPane.INFORMATION_MESSAGE);
                    cardLayout.show(mainPanel, "login");
                    break;           
                default:
                    BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    JOptionPane.showMessageDialog(frame, "Internal server error" + reader.toString() , "deletetion Failed", JOptionPane.ERROR_MESSAGE);
                    break;
            }            
        }
        catch (Exception ex) {
            JOptionPane.showMessageDialog(frame, "Error: " + ex.getMessage(), "Exception", JOptionPane.ERROR_MESSAGE);
        }
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
    public void createTicket(String title, String description, String priority, String category){
        try{
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
                    JOptionPane.showMessageDialog(frame, "refresh your login", "token refrech", JOptionPane.INFORMATION_MESSAGE);
                    cardLayout.show(mainPanel, "login");
                    break;           
                default:
                    BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    JOptionPane.showMessageDialog(frame, "Internal server error" + reader.toString() , "ticket creation Failed", JOptionPane.ERROR_MESSAGE);
                    break;
            }            
        }
        catch (Exception ex) {
            JOptionPane.showMessageDialog(frame, "Error: " + ex.getMessage(), "Exception", JOptionPane.ERROR_MESSAGE);
        }
    }
    private JPanel createEmployeeTicketsPanel() {

        PairReturn ticketsResponseReturn = getAllEmployeTickets();
        List<TicketResponseDTO> ticketResponseDTO = ticketsResponseReturn.getListTickets();
        int ticketResponseCode = ticketsResponseReturn.getResponseCode();
        
        JPanel ticketPanel = new JPanel(new MigLayout("", "[grow]", "[]20[]"));
        JLabel titleLabel = new JLabel("Employee Tickets Dashboard");
        JLabel filterLabel = new JLabel("sortBy:");
        JButton sortButton = new JButton("sort Tickets");
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
    private void loadTickets(JPanel ticketPanel, JComboBox<String> sortingComboBox, JButton sortButton, JButton backButton, JButton logoutButton) {
        JLabel titleLabel = new JLabel("Employee Tickets Dashboard");
        JLabel filterLabel = new JLabel("sortBy:");
        PairReturn ticketsResponseReturn = getAllEmployeTickets();
        List<TicketResponseDTO> ticketResponseDTO = ticketsResponseReturn.getListTickets();
        int ticketResponseCode = ticketsResponseReturn.getResponseCode();

        System.out.println(ticketResponseCode);
        System.out.println(ticketResponseDTO.isEmpty());

        if (ticketResponseCode == 200 && !ticketResponseDTO.isEmpty()) {
            for (int index = 1; index <= ticketResponseDTO.size(); index++) {
                JLabel ticketLabel = new JLabel("Ticket " + index);
                JLabel ticketTitleLabel = new JLabel("title : " + ticketResponseDTO.get(index - 1).getTitle());
                JLabel ticketdescriptionLabel = new JLabel("description : " + ticketResponseDTO.get(index - 1).getDescription());
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
                ticketPanel.add(ticketdescriptionLabel, "wrap");

                List<CommentResponseDTO> commentList = ticketResponseDTO.get(index - 1).getTicketComments();
                if (!commentList.isEmpty()) {
                    JLabel commentLabel = new JLabel("Comments");
                    ticketPanel.add(commentLabel, "growx, wrap");
                    for (int indexC = 1; indexC <= commentList.size(); indexC++) {
                        JLabel commentTixtLabel = new JLabel("Comment " + indexC + " : " + commentList.get(indexC - 1).getCommentText());
                        JLabel commentDateLabel = new JLabel("date " + commentList.get(indexC - 1).getCommentDate());
                        JLabel commentCreatorLabel = new JLabel("agent " + commentList.get(indexC - 1).getEmployeeName());
                        ticketPanel.add(commentTixtLabel);
                        ticketPanel.add(commentDateLabel);
                        ticketPanel.add(commentCreatorLabel);
                    }
                }
            }
        }

        // Add sorting functionality
        sortButton.addActionListener(e -> {
            String selectedSort = (String) sortingComboBox.getSelectedItem();
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
                        ticketResponseDTO.sort(Comparator.comparing(ticket -> ticket.getStatus().equals("NEW") ? 0 : 1));
                        break;
                    default:
                        break;
                }

                // Remove all components from the ticketPanel
                ticketPanel.removeAll();

                // Re-add the title, filter, and sort components
                ticketPanel.add(titleLabel, "growx");
                ticketPanel.add(filterLabel, "growx");
                ticketPanel.add(sortingComboBox, "growx");
                ticketPanel.add(sortButton, "growx, wrap");

                // Re-add the sorted tickets to the ticketPanel
                for (int index = 1; index <= ticketResponseDTO.size(); index++) {
                    if(ticketResponseDTO.get(index -1).getStatus().equalsIgnoreCase(selectedSort) || selectedSort == "ALL"){
                        JLabel ticketLabel = new JLabel("Ticket " + index);
                        JLabel ticketTitleLabel = new JLabel("title : " + ticketResponseDTO.get(index - 1).getTitle());
                        JLabel ticketdescriptionLabel = new JLabel("description : " + ticketResponseDTO.get(index - 1).getDescription());
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
                        ticketPanel.add(ticketdescriptionLabel, "wrap");

                        List<CommentResponseDTO> commentList = ticketResponseDTO.get(index - 1).getTicketComments();
                        if (!commentList.isEmpty()) {
                            JLabel commentLabel = new JLabel("Comments");
                            ticketPanel.add(commentLabel, "growx, wrap");
                            for (int indexC = 1; indexC <= commentList.size(); indexC++) {
                                JLabel commentTixtLabel = new JLabel("Comment " + indexC + " : " + commentList.get(indexC - 1).getCommentText());
                                JLabel commentDateLabel = new JLabel("date " + commentList.get(indexC - 1).getCommentDate());
                                JLabel commentCreatorLabel = new JLabel("agent " + commentList.get(indexC - 1).getEmployeeName());
                                ticketPanel.add(commentTixtLabel);
                                ticketPanel.add(commentDateLabel);
                                ticketPanel.add(commentCreatorLabel);
                            }
                        }
                    }
                }

                // Re-add the back and logout buttons
                ticketPanel.add(backButton, "growx");
                ticketPanel.add(logoutButton, "wrap");

                // Revalidate and repaint the panel to reflect the changes
                ticketPanel.revalidate();
                ticketPanel.repaint();
            }
        });
    }
    private  PairReturn getAllEmployeTickets(){
        try{
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
                        ticketResponseDTO.setTitle(ticketsJsonResponse.get(i).getString("title"));
                        ticketResponseDTO.setDescription(ticketsJsonResponse.get(i).getString("description"));
                        ticketResponseDTO.setPriority(ticketsJsonResponse.get(i).getString("priority"));
                        ticketResponseDTO.setCategory(ticketsJsonResponse.get(i).getString("category"));
                        ticketResponseDTO.setStatus(ticketsJsonResponse.get(i).getString("status"));
                        ticketResponseDTO.setCreationDate(ticketsJsonResponse.get(i).getString("creationDate"));

                        JSONArray commentsAJsonArray = ticketsJsonResponse.get(i).getJSONArray("ticketComments");
                        List<JSONObject> commentsJsonResponse = new ArrayList<>();
                        List<CommentResponseDTO> listCommentResponseDTO = new ArrayList<>();
                        for(int j = 0; j < commentsAJsonArray.length(); j++) {
                            commentsJsonResponse.add(commentsAJsonArray.getJSONObject(j));

                            CommentResponseDTO commentResponseDTO = new CommentResponseDTO();
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
                    JOptionPane.showMessageDialog(frame, "refresh your login", "token refrech", JOptionPane.INFORMATION_MESSAGE);
                    cardLayout.show(mainPanel, "login");
                    return new PairReturn(new ArrayList<>(), 403);         
                default:
                    BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    JOptionPane.showMessageDialog(frame, "Internal server error" + reader.toString() , "tickets list Failed", JOptionPane.ERROR_MESSAGE);
                    return new PairReturn(new ArrayList<>(), 400);
            }            
        }
        catch (Exception ex) {
            JOptionPane.showMessageDialog(frame, "Error: " + ex.getMessage(), "Exception", JOptionPane.ERROR_MESSAGE);
        }
        return new PairReturn(new ArrayList<>(), 400);
    }

    private JPanel createItSupportPanel() {
        
        JPanel adminPanel = new JPanel(new MigLayout("", "[grow]", "[]20[]20[]"));
        JLabel titleLabel = new JLabel("support IT Dashboard");
        JButton manageTicketsButton = new JButton("Manage All Tickets");

        adminPanel.add(titleLabel, "wrap, center");
        adminPanel.add(manageTicketsButton, "wrap, growx");

        return adminPanel;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(MainUI::new);
    }
}
