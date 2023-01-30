import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ChoosePage extends JFrame implements ActionListener {
    JButton studentButton;
    JButton parentButton;
    JButton teacherButton;
    JButton assistantButton;

    JLabel choose;

    StudentPage studentPage;
    TeacherPage teacherPage;
    AssistantPage assistantPage;

    public ChoosePage(String title) {
        super(title);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(900, 900));
        setLayout(new GridLayout(5, 1));

        choose = new JLabel("------------------- Choose your account-------------------");

        parentButton = new JButton("Parent");
        studentButton = new JButton("Student");
        teacherButton = new JButton("Teacher");
        assistantButton = new JButton("Assistant");

        add(studentButton);
        add(teacherButton);
        add(assistantButton);
        add(parentButton);

        studentButton.addActionListener(this);
        teacherButton.addActionListener(this);
        assistantButton.addActionListener(this);
        parentButton.addActionListener(this);

        pack();
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() instanceof JButton) {
            if (e.getSource() == studentButton) {
                dispose();
                studentPage = new StudentPage("Student Page");
            } else if (e.getSource() == teacherButton) {
                dispose();
                teacherPage = new TeacherPage("Teacher Page");
            } else if (e.getSource() == assistantButton) {
                dispose();
                assistantPage = new AssistantPage("Assistant Page");

            } else if (e.getSource() == parentButton) {
                dispose();
            }
        }
    }
}
