import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.event.*;
import java.awt.*;
import java.util.*;

public class StudentPage extends JFrame implements ActionListener, ListSelectionListener {
    JTextField firstNameText;
    JTextField lastNameText;

    JList courses;
    JTextArea informations;

    JButton findButton;
    JButton backButton;

    JPanel centerPanel;
    JPanel leftPanel;
    Vector<Course> coursesVector;
    Student student;
    JScrollPane scrollPane;
    ChoosePage choosePage;
    JLabel invalidLabel;

    public StudentPage(String title){
        super(title);
        setMinimumSize(new Dimension(900, 900));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(1, 2));

        firstNameText = new JTextField();
        firstNameText.setColumns(30);

        lastNameText = new JTextField();
        lastNameText.setColumns(30);

        JLabel titleLabel = new JLabel("Student searching page");
        titleLabel.setFont(new Font("Calibri", Font.ITALIC, 30));

        JLabel firstNameLabel = new JLabel("Introduce first name of the student:");
        JLabel lastNameLabel = new JLabel("Introduce last name of the student:");

        JPanel firstNamePanel = new JPanel();
        firstNamePanel.add(firstNameLabel);
        firstNamePanel.add(firstNameText);

        JPanel lastNamePanel = new JPanel();
        lastNamePanel.add(lastNameLabel);
        lastNamePanel.add(lastNameText);

        findButton = new JButton("Search student");
        backButton = new JButton("Back");

        courses = new JList();

        JScrollPane scrollPane = new JScrollPane();
        scrollPane = new JScrollPane(courses);

        invalidLabel = new JLabel("Student does not exists");
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

        informations = new JTextArea("");

        JScrollPane informationsPane = new JScrollPane(informations);

        add(leftPanel);
        add(informationsPane);

        findButton.addActionListener(this);
        backButton.addActionListener(this);
        courses.addListSelectionListener(this);

        pack();
        setVisible(true);
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() instanceof JButton) {
            if (e.getSource() == findButton) {
                Catalog catalog = Catalog.getInstance();

                //catalog.parse();

                String firstName = firstNameText.getText();
                String lastName = lastNameText.getText();
                student = (Student) UserFactory.getUser("Student", firstName, lastName);

                coursesVector = new Vector<>();
                for (Course course : catalog.getCourses()) {
                    ArrayList<Student> students = course.getAllStudents();
                    for (Student s : students) {
                        if (s.equals(student)) {
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
            }
        }
    }

    @Override
    public void valueChanged(ListSelectionEvent e) {
        if (courses.isSelectionEmpty()) {
            return;
        } else {
            Object value = courses.getSelectedValue();
            Course course = (Course) value;

            String credits = "Credits: " + course.getCredits() + "\n";
            String teacher = "Teacher: " + course.getTeacher().getFirstName() + " " + course.getTeacher().getLastName() + "\n";
            String assistants = "Assistants:\n";

            Iterator<Assistant> it = course.getAssistants().iterator();

            while (it.hasNext()) {
                assistants += "\t- " + it.next() + "\n";
            }

            String firstName = firstNameText.getText();
            String lastName = lastNameText.getText();

            String info="";
            for(Student student:course.getAllStudents()) {
                if (firstName.equals(student.getFirstName()) && lastName.equals(student.getLastName())) {
                    String myGroup = "My group: ";
                    String myAssistant = "My assistant: ";

                    for (Map.Entry<String, Group> group : course.getGroups().entrySet()) {
                        String id = group.getKey();
                        if (group.getValue().contains(student)) {
                            myGroup += id + "\n";
                            myAssistant += group.getValue().assistant.getFirstName() + " " + group.getValue().assistant.getLastName() + "\n";
                            break;
                        }
                    }

                    String myGrade = "My grade:\n";
                    for (Grade grade : course.getGrades()) {
                        if (grade.getStudent().equals(student)) {
                            if(grade.getPartialScore()!=0) {
                                myGrade += "\t- Partial score: " + grade.getPartialScore() + "\n";
                            }else{
                                myGrade += "\t- Partial score: Not Graded\n";
                            }
                            if(grade.getExamScore()!=0) {
                                myGrade += "\t- Exam score: " + grade.getExamScore() + "\n";
                            }else{
                                myGrade += "\t- Exam score: Not Graded\n";
                            }
                            myGrade += "\t- Total score:   " + grade.getTotal() + "\n";
                            break;
                        }
                    }

                    String state = "State: ";

                    if (course.getGraduatedStudents().contains(student)) {
                        state += "promoted";
                    } else {
                        state += "loser";
                    }

                    info = credits + teacher + assistants + myGroup + myAssistant + myGrade + state;

                    break;
                }
            }
            informations.setText(info);
            informations.setEditable(false);
        }
    }
}
