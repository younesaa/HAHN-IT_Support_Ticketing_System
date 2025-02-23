import javax.swing.*;
import net.miginfocom.swing.MigLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SupportTicketSystemUI {

    private JFrame frame;
    private CardLayout cardLayout;
    private JPanel mainPanel;

    public SupportTicketSystemUI() {
        frame = new JFrame("Support Ticket System");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        frame.setLocationRelativeTo(null);

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        mainPanel.add(createLoginPanel(), "login");
        mainPanel.add(createEmployeePanel(), "employee");
        mainPanel.add(createAdminPanel(), "admin");

        frame.add(mainPanel);
        frame.setVisible(true);
    }

    private JPanel createLoginPanel() {
        JPanel loginPanel = new JPanel(new MigLayout("", "[][grow]", "[][]20[]"));
        JLabel userLabel = new JLabel("Username:");
        JTextField userField = new JTextField(20);
        JLabel passLabel = new JLabel("Password:");
        JPasswordField passField = new JPasswordField(20);
        JButton loginButton = new JButton("Login");

        loginButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // TODO: Add JWT authentication integration
                String username = userField.getText();
                String password = new String(passField.getPassword());
                if (username.equals("admin")) {
                    cardLayout.show(mainPanel, "admin");
                } else {
                    cardLayout.show(mainPanel, "employee");
                }
            }
        });

        loginPanel.add(userLabel);
        loginPanel.add(userField, "wrap, growx");
        loginPanel.add(passLabel);
        loginPanel.add(passField, "wrap, growx");
        loginPanel.add(loginButton, "span, center");

        return loginPanel;
    }

    private JPanel createEmployeePanel() {
        JPanel employeePanel = new JPanel(new MigLayout("", "[grow]", "[]20[]"));
        JLabel titleLabel = new JLabel("Employee Dashboard");
        JButton createTicketButton = new JButton("Create Ticket");
        JButton viewTicketsButton = new JButton("View My Tickets");

        employeePanel.add(titleLabel, "wrap, center");
        employeePanel.add(createTicketButton, "wrap, growx");
        employeePanel.add(viewTicketsButton, "growx");

        return employeePanel;
    }

    private JPanel createAdminPanel() {
        JPanel adminPanel = new JPanel(new MigLayout("", "[grow]", "[]20[]20[]"));
        JLabel titleLabel = new JLabel("Admin Dashboard");
        JButton manageTicketsButton = new JButton("Manage All Tickets");
        JButton auditLogButton = new JButton("View Audit Logs");

        adminPanel.add(titleLabel, "wrap, center");
        adminPanel.add(manageTicketsButton, "wrap, growx");
        adminPanel.add(auditLogButton, "growx");

        return adminPanel;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(SupportTicketSystemUI::new);
    }
} 
