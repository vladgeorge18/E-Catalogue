import java.awt.*;
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.event.*;
import java.lang.reflect.Array;
import java.util.*;

public class AssistantPage extends JFrame implements ActionListener, ListSelectionListener {
    JTextField firstNameText;
    JTextField lastNameText;

    JList courses;
    JTextArea informations;

    JButton findButton;
    JButton backButton;
    JButton confirmButton;

    JPanel centerPanel;
    JPanel leftPanel;
    Vector<Course> coursesVector;
    Student student;
    JScrollPane scrollPane;
    ChoosePage choosePage;
    JLabel invalidLabel;
    static Assistant user;

    public AssistantPage(String title){
        super(title);
        setMinimumSize(new Dimension(900, 900));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(1, 2));

        firstNameText = new JTextField();
        firstNameText.setColumns(30);

        lastNameText = new JTextField();
        lastNameText.setColumns(30);

        JLabel titleLabel = new JLabel("Assistant searching page");
        titleLabel.setFont(new Font("Veranda", Font.BOLD, 30));

        JLabel firstNameLabel = new JLabel("Introduce first name of the assistant:");
        JLabel lastNameLabel = new JLabel("Introduce last name of the assistant:");

        JPanel firstNamePanel = new JPanel();
        firstNamePanel.add(firstNameLabel);
        firstNamePanel.add(firstNameText);

        JPanel lastNamePanel = new JPanel();
        lastNamePanel.add(lastNameLabel);
        lastNamePanel.add(lastNameText);

        findButton = new JButton("Search assistant");
        backButton = new JButton("Back");
        confirmButton = new JButton("Confirm");

        courses = new JList();

        JScrollPane scrollPane = new JScrollPane();
        scrollPane = new JScrollPane(courses);

        invalidLabel = new JLabel("Assistant does not exists");
        invalidLabel.setVisible(false);

        centerPanel = new JPanel(new GridLayout(3, 1));
        centerPanel.add(firstNamePanel);
        centerPanel.add(lastNamePanel);
        centerPanel.add(scrollPane);

        leftPanel = new JPanel();
        leftPanel.add(titleLabel, BorderLayout.NORTH);
        leftPanel.add(centerPanel, BorderLayout.CENTER);
        leftPanel.add(findButton, BorderLayout.SOUTH);
        leftPanel.add(backButton);
        leftPanel.add(invalidLabel);
        leftPanel.add(confirmButton);

        informations = new JTextArea("");

        JScrollPane informationsPane = new JScrollPane(informations);

        add(leftPanel);
        add(informationsPane);

        findButton.addActionListener(this);
        backButton.addActionListener(this);
        courses.addListSelectionListener(this);
        confirmButton.addActionListener(this);

        pack();
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() instanceof JButton) {
            if (e.getSource() == findButton) {
                Catalog catalog = Catalog.getInstance();
                String firstName = firstNameText.getText();
                String lastName = lastNameText.getText();
                user = (Assistant) UserFactory.getUser("Assistant", firstName, lastName);

                coursesVector = new Vector<>();
                for (Course course : catalog.getCourses()) {
                    for(Assistant assistant: course.getAssistants()) {
                        if (assistant.getFirstName().equals(firstName) && assistant.getLastName().equals(lastName)) {
                            coursesVector.add(course);
                            break;
                        }
                    }
                }

                if (coursesVector.size() != 0) {
                    courses.setListData(coursesVector);
                    invalidLabel.setVisible(false);
                } else {
                    invalidLabel.setVisible(true);
                }

                SwingUtilities.updateComponentTreeUI(this);
            } else if (e.getSource() == backButton) {
                dispose();
                choosePage = new ChoosePage("Choose");
            } else if (e.getSource() == confirmButton) {
                ScoreVisitor visitor = new ScoreVisitor();
                Catalog catalog = Catalog.getInstance();
                String firstName = firstNameText.getText();
                String lastName = lastNameText.getText();
                for (Course course : catalog.getCourses()) {
                    for(Assistant assistant: course.getAssistants()) {
                        if (assistant.getFirstName().equals(firstName) && assistant.getLastName().equals(lastName)) {
                            visitor.visit(assistant);
                            int pos=informations.getCaretPosition();
                            //String result="\nStudent with the best exam score: "+String.valueOf(course.getBestStudent(new BestExamScore()))+"\n";
                            String result="\nStudent with the best partial score: "+ String.valueOf(course.getBestStudent(new BestPartialScore()))+"\n";
                            result+="Student with the best total score: "+ String.valueOf(course.getBestStudent(new BestTotalScore()))+"\n";
                            informations.insert(result,pos);
                            informations.setForeground(Color.green);
                        }
                    }
                }
            }
        }
    }

    public void valueChanged(ListSelectionEvent e) {
        if (courses.isSelectionEmpty()) {
            return;
        } else {
            Object val = courses.getSelectedValue();
            Course course = (Course) val;
            String courseName=course.getName();
            ScoreVisitor visitor = new ScoreVisitor();
            for(Course course1:Catalog.getInstance().getCourses())
            {
                if (course1.getName().equals(courseName))
                {   String firstName = firstNameText.getText();
                    String lastName = lastNameText.getText();
                    for(Assistant assistant: course1.getAssistants()) {
                        if (assistant.getFirstName().equals(firstName) && assistant.getLastName().equals(lastName)) {
                            informations.setText(visitor.displayGradesAssistant(assistant));
                            break;
                        }
                    }
                    //informations.setText(visitor.displayGradesAssistant(course1.getTeacher()));
                    informations.setEditable(false);
                }
            }
        }
    }
}

