
package emergencyservicedispatch2;
//hi clark Dalapag//

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;

public class EmergencyDispatchSystem extends JFrame {

    // DATA STRUCTURE
    private ManualPriorityQueue queue;

    // STORE ALL RECORDS, INCLUDING DISPATCHED INCIDENTS
    private Incident[] records;
    private int recordCount;

    // GUI COMPONENTS
    private DefaultTableModel tableModel;
    private JTable incidentTable;

    private JTextField idField;
    private JTextField reporterField;
    private JTextField locationField;
    private JTextField descriptionField;
    private JTextField searchField;

    private JComboBox<String> typeCombo;
    private JComboBox<String> priorityCombo;

    private JTextArea activityArea;

    public EmergencyDispatchSystem() {

        setTitle("Emergency Service Dispatch System");
        setSize(1100, 720);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        queue = new ManualPriorityQueue(10);
        records = new Incident[10];
        recordCount = 0;

        setLayout(new BorderLayout(10, 10));

        // HEADER
        JLabel title = new JLabel(
            "EMERGENCY SERVICE DISPATCH SYSTEM",
            SwingConstants.CENTER
        );

        title.setFont(new Font("Arial", Font.BOLD, 24));
        title.setForeground(Color.WHITE);
        title.setOpaque(true);
        title.setBackground(new Color(145, 25, 25));
        title.setBorder(
            BorderFactory.createEmptyBorder(20, 10, 20, 10)
        );

        add(title, BorderLayout.NORTH);

        // MAIN PANEL
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));

        mainPanel.setBorder(
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        );

        add(mainPanel, BorderLayout.CENTER);

        // LEFT PANEL
        JPanel leftPanel = new JPanel(new GridLayout(2, 1, 5, 10));

        leftPanel.add(createRegistrationPanel());
        leftPanel.add(createSearchPanel());

        leftPanel.setPreferredSize(new Dimension(350, 450));

        mainPanel.add(leftPanel, BorderLayout.WEST);

        // INCIDENT TABLE
        String[] columns = {
            "Incident ID",
            "Emergency Type",
            "Location",
            "Priority",
            "Reporter",
            "Status"
        };

        tableModel = new DefaultTableModel(columns, 0) {

            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        incidentTable = new JTable(tableModel);
        incidentTable.setRowHeight(25);

        JScrollPane tableScroll =
            new JScrollPane(incidentTable);

        JPanel tablePanel = new JPanel(new BorderLayout());

        tablePanel.setBorder(
            BorderFactory.createTitledBorder(
                "Registered Emergency Incidents"
            )
        );

        tablePanel.add(tableScroll, BorderLayout.CENTER);

        // ACTION BUTTONS
        JPanel buttonPanel = new JPanel();

        JButton viewButton =
            new JButton("View / Refresh Incidents");

        JButton dispatchButton =
            new JButton("Dispatch Highest Priority");

        viewButton.addActionListener(this::refreshTable);

        dispatchButton.addActionListener(this::dispatchIncident);

        buttonPanel.add(viewButton);
        buttonPanel.add(dispatchButton);

        tablePanel.add(buttonPanel, BorderLayout.SOUTH);

        mainPanel.add(tablePanel, BorderLayout.CENTER);

        // ACTIVITY HISTORY
        activityArea = new JTextArea(5, 30);
        activityArea.setEditable(false);
        activityArea.setLineWrap(true);

        JScrollPane activityScroll =
            new JScrollPane(activityArea);

        activityScroll.setBorder(
            BorderFactory.createTitledBorder(
                "Dispatch Activity History"
            )
        );

        add(activityScroll, BorderLayout.SOUTH);

        log("System started successfully.");

        refreshTable();
    }

    // REGISTRATION PANEL
    private JPanel createRegistrationPanel() {

        JPanel panel = new JPanel(new GridBagLayout());

        panel.setBorder(
            BorderFactory.createTitledBorder(
                "Register New Incident"
            )
        );

        GridBagConstraints c = new GridBagConstraints();

        c.insets = new Insets(5, 5, 5, 5);
        c.fill = GridBagConstraints.HORIZONTAL;

        idField = new JTextField(15);
        reporterField = new JTextField(15);
        locationField = new JTextField(15);
        descriptionField = new JTextField(15);

        typeCombo = new JComboBox<>(new String[] {
            "Medical Emergency",
            "Fire",
            "Road Accident",
            "Rescue",
            "Other"
        });

        priorityCombo = new JComboBox<>(new String[] {
            "1 - High",
            "2 - Medium",
            "3 - Low"
        });

        addField(panel, c, 0, "Incident ID:", idField);
        addField(panel, c, 1, "Reporter:", reporterField);
        addField(panel, c, 2, "Location:", locationField);
        addField(panel, c, 3, "Emergency:", typeCombo);
        addField(panel, c, 4, "Priority:", priorityCombo);
        addField(panel, c, 5, "Description:", descriptionField);

        JButton registerButton =
            new JButton("Register Incident");

        registerButton.addActionListener(this::registerIncident);

        c.gridx = 0;
        c.gridy = 6;
        c.gridwidth = 2;

        panel.add(registerButton, c);

        return panel;
    }

    // ADD FORM LABELS AND FIELDS
    private void addField(
        JPanel panel,
        GridBagConstraints c,
        int row,
        String label,
        JComponent field
    ) {

        c.gridwidth = 1;

        c.gridx = 0;
        c.gridy = row;
        c.weightx = 0;

        panel.add(new JLabel(label), c);

        c.gridx = 1;
        c.weightx = 1;

        panel.add(field, c);
    }

    // SEARCH PANEL
    private JPanel createSearchPanel() {

        JPanel panel = new JPanel(new GridBagLayout());

        panel.setBorder(
            BorderFactory.createTitledBorder(
                "Search Incident by ID"
            )
        );

        GridBagConstraints c = new GridBagConstraints();

        c.insets = new Insets(5, 5, 5, 5);
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1;

        searchField = new JTextField(15);

        c.gridx = 0;
        c.gridy = 0;
        c.gridwidth = 2;

        panel.add(new JLabel("Enter Incident ID:"), c);

        c.gridy = 1;
        panel.add(searchField, c);

        JButton searchButton = new JButton("Search");

        searchButton.addActionListener(this::searchIncident);

        c.gridy = 2;
        panel.add(searchButton, c);

        return panel;
    }

    // REGISTER INCIDENT
    private void registerIncident(ActionEvent event) {

        String id = idField.getText().trim();
        String reporter = reporterField.getText().trim();
        String location = locationField.getText().trim();
        String description = descriptionField.getText().trim();

        // CHECK REQUIRED FIELDS
        if (id.isEmpty() ||
            reporter.isEmpty() ||
            location.isEmpty() ||
            description.isEmpty()) {

            JOptionPane.showMessageDialog(
                this,
                "Please complete all fields.",
                "Missing Information",
                JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        // CHECK DUPLICATE ID
        if (searchById(id) != null) {

            JOptionPane.showMessageDialog(
                this,
                "Incident ID already exists!",
                "Duplicate ID",
                JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        String emergencyType =
            (String) typeCombo.getSelectedItem();

        int priority =
            priorityCombo.getSelectedIndex() + 1;

        // CREATE INCIDENT
        Incident incident = new Incident(
            id,
            reporter,
            location,
            emergencyType,
            description,
            priority
        );

        // SAVE TO MASTER RECORD ARRAY
        ensureRecordCapacity();

        records[recordCount] = incident;
        recordCount++;

        // ADD TO PRIORITY QUEUE
        queue.enqueue(incident);

        log("Registered incident: " + id);

        clearFields();
        refreshTable();

        JOptionPane.showMessageDialog(
            this,
            "Incident registered successfully!",
            "Success",
            JOptionPane.INFORMATION_MESSAGE
        );
    }

    // DISPATCH HIGHEST PRIORITY
    private void dispatchIncident(ActionEvent event) {

        if (queue.isEmpty()) {

            JOptionPane.showMessageDialog(
                this,
                "No incidents available for dispatch.",
                "Queue Empty",
                JOptionPane.INFORMATION_MESSAGE
            );

            return;
        }

        // REMOVE THE HIGHEST PRIORITY
        Incident incident = queue.dequeue();

        // UPDATE STATUS
        incident.setDispatched(true);

        log(
            "DISPATCHED: " +
            incident.getIncidentId() +
            " | " +
            incident.getEmergencyType() +
            " | Priority: " +
            incident.getPriorityLabel()
        );

        refreshTable();

        JOptionPane.showMessageDialog(
            this,
            "Emergency successfully dispatched!\n\n" +
            "Incident ID: " + incident.getIncidentId() +
            "\nEmergency: " + incident.getEmergencyType() +
            "\nLocation: " + incident.getLocation() +
            "\nPriority: " + incident.getPriorityLabel(),
            "Dispatch Successful",
            JOptionPane.INFORMATION_MESSAGE
        );
    }

    // MANUAL LINEAR SEARCH
    private Incident searchById(String id) {

        for (int i = 0; i < recordCount; i++) {

            if (records[i].getIncidentId()
                    .equalsIgnoreCase(id)) {

                return records[i];
            }
        }

        return null;
    }

    // SEARCH BUTTON
    private void searchIncident(ActionEvent event) {

        String id = searchField.getText().trim();

        if (id.isEmpty()) {

            JOptionPane.showMessageDialog(
                this,
                "Please enter an Incident ID.",
                "Search",
                JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        Incident incident = searchById(id);

        if (incident == null) {

            log("Incident not found: " + id);

            JOptionPane.showMessageDialog(
                this,
                "Incident ID " + id + " was not found.",
                "Not Found",
                JOptionPane.INFORMATION_MESSAGE
            );

        } else {

            log("Incident found: " + id);

            String status = incident.isDispatched()
                ? "DISPATCHED"
                : "WAITING";

            JOptionPane.showMessageDialog(
                this,
                "INCIDENT FOUND!\n\n" +
                "Incident ID: " + incident.getIncidentId() +
                "\nReporter: " + incident.getReporterName() +
                "\nLocation: " + incident.getLocation() +
                "\nEmergency: " + incident.getEmergencyType() +
                "\nDescription: " + incident.getDescription() +
                "\nPriority: " + incident.getPriorityLabel() +
                "\nStatus: " + status,
                "Search Result",
                JOptionPane.INFORMATION_MESSAGE
            );
        }
    }

    // DISPLAY ALL INCIDENTS
    private void refreshTable(ActionEvent event) {
        refreshTable();
    }

    private void refreshTable() {

        tableModel.setRowCount(0);

        for (int i = 0; i < recordCount; i++) {

            Incident incident = records[i];

            String status = incident.isDispatched()
                ? "DISPATCHED"
                : "WAITING";

            tableModel.addRow(new Object[] {

                incident.getIncidentId(),
                incident.getEmergencyType(),
                incident.getLocation(),
                incident.getPriorityLabel(),
                incident.getReporterName(),
                status
            });
        }
    }

    // INCREASE RECORD ARRAY CAPACITY
    private void ensureRecordCapacity() {

        if (recordCount < records.length) {
            return;
        }

        Incident[] newRecords =
            new Incident[records.length * 2];

        for (int i = 0; i < recordCount; i++) {
            newRecords[i] = records[i];
        }

        records = newRecords;
    }

    // CLEAR INPUT FIELDS
    private void clearFields() {

        idField.setText("");
        reporterField.setText("");
        locationField.setText("");
        descriptionField.setText("");

        typeCombo.setSelectedIndex(0);
        priorityCombo.setSelectedIndex(0);
    }

    // ACTIVITY HISTORY
    private void log(String message) {

        activityArea.append(message + "\n");

        activityArea.setCaretPosition(
            activityArea.getDocument().getLength()
        );
    }

    // MAIN METHOD
    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {

            EmergencyDispatchSystem app =
                new EmergencyDispatchSystem();

            app.setVisible(true);
        });
    }
}

