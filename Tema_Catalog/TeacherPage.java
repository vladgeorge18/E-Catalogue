import java.awt.*;
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.event.*;
import java.lang.reflect.Array;
import java.util.*;

public class TeacherPage extends JFrame implements ActionListener, ListSelectionListener {
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
    static Teacher user;

    public TeacherPage(String title){
        super(title);
        setMinimumSize(new Dimension(900, 900));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(1, 2));

        firstNameText = new JTextField();
        firstNameText.setColumns(30);

        lastNameText = new JTextField();
        lastNameText.setColumns(30);

        JLabel titleLabel = new JLabel("Teacher searching page");
        titleLabel.setFont(new Font("Verdana", Font.ITALIC, 30));

        JLabel firstNameLabel = new JLabel("Introduce first name of the teacher:");
        JLabel lastNameLabel = new JLabel("Introduce last name of the teacher:");

        JPanel firstNamePanel = new JPanel();
        firstNamePanel.add(firstNameLabel);
        firstNamePanel.add(firstNameText);

        JPanel lastNamePanel = new JPanel();
        lastNamePanel.add(lastNameLabel);
        lastNamePanel.add(lastNameText);

        findButton = new JButton("Search teacher");
        backButton = new JButton("Back");
        confirmButton = new JButton("Confirm");

        courses = new JList();

        JScrollPane scrollPane = new JScrollPane();
        scrollPane = new JScrollPane(courses);

        invalidLabel = new JLabel("Teacher does not exists");
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

                //catalog.parse();

                String firstName = firstNameText.getText();
                String lastName = lastNameText.getText();
                //student = (Student) UserFactory.getUser("Student", firstName, lastName);
                user = (Teacher) UserFactory.getUser("Teacher", firstName, lastName);

                coursesVector = new Vector<>();
                System.out.println(catalog.getCourses().size());

                for (Course course : catalog.getCourses()) {
                    if (course.getTeacher().getFirstName().equals(firstName) && course.getTeacher().getLastName().equals(lastName)) {
                        coursesVector.add(course);
                        break;
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
                    if (course.getTeacher().getFirstName().equals(firstName) && course.getTeacher().getLastName().equals(lastName)) {
                        visitor.visit(course.getTeacher());
                        int pos=informations.getCaretPosition();
                        String result="\nStudent with the best exam score: "+String.valueOf(course.getBestStudent(new BestExamScore()))+"\n";
                        result+="Student with the best total score: "+ String.valueOf(course.getBestStudent(new BestTotalScore()))+"\n";
                        informations.insert(result,pos);
                        informations.setForeground(Color.green);
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
            String result="";
            for(Course course1:Catalog.getInstance().getCourses())
            {
                if (course1.getName().equals(courseName))
                {
                    result=visitor.displayGradesTeacher(course1.getTeacher());
                    //result=result+String.valueOf(course.getBestStudent(new BestExamScore()));
                    //informations.setText(visitor.displayGradesTeacher(course1.getTeacher()));
                    //informations.setEditable(false);
                }
            }
            informations.setText(result);
            informations.setEditable(false);
        }
    }
}
