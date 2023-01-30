import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class ScoreVisitor implements Visitor{
    HashMap<Teacher,List<Tuple<Student,String,Double>>> examScores=new HashMap<>();
    HashMap<Assistant,List<Tuple<Student,String,Double>>> partialScores=new HashMap<>();

    public ScoreVisitor() {

    }

    public void parseGradesTeacher() {
        JSONParser parser = new JSONParser();


        try {
            FileReader fileReader = new FileReader("TestFiles/grades.json");
            JSONObject gradesObject = (JSONObject) parser.parse(fileReader);
            JSONArray gradesArray = (JSONArray) gradesObject.get("grades");
            for (int i = 0; i < gradesArray.size(); i++) {
                JSONObject gradeObject = (JSONObject) gradesArray.get(i);
                JSONObject teacher = (JSONObject) gradeObject.get("teacher");
                JSONObject student = (JSONObject) gradeObject.get("student");
                String teacherFirstName = (String) teacher.get("firstName");
                String teacherLastName = (String) teacher.get("lastName");
                String studentFirstName = (String) student.get("firstName");
                String studentLastName = (String) student.get("lastName");
                Student s = (Student) UserFactory.getUser("Student", studentFirstName, studentLastName);
                Teacher t = (Teacher) UserFactory.getUser("Teacher", teacherFirstName, teacherLastName);
                String course = (String) gradeObject.get("course");
                String grade = (String) gradeObject.get("grade");
                boolean found=false;
                for (Teacher key:examScores.keySet()) {

                    if (key.getFirstName().equals(t.getFirstName()) && key.getLastName().equals(t.getLastName())) {
                        examScores.get(key).add(new Tuple<Student, String, Double>(s, course, Double.valueOf(grade)));
                        found = true;
                    }
                }
                if(found==false)
                    {
                        ArrayList<Tuple<Student, String, Double>> tuple = new ArrayList<>();
                        tuple.add(new Tuple<Student, String, Double>(s, course, Double.valueOf(grade)));
                        examScores.put(t, tuple);
                    }

            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    public void parseGradesAssistant() {
        JSONParser parser = new JSONParser();


        try {
            FileReader fileReader = new FileReader("TestFiles/gradesAssistants.json");
            JSONObject gradesObject = (JSONObject) parser.parse(fileReader);
            JSONArray gradesArray = (JSONArray) gradesObject.get("grades");
            for (int i = 0; i < gradesArray.size(); i++) {
                JSONObject gradeObject = (JSONObject) gradesArray.get(i);
                JSONObject assistant = (JSONObject) gradeObject.get("assistant");
                JSONObject student = (JSONObject) gradeObject.get("student");
                String assistantFirstName = (String) assistant.get("firstName");
                String assistantLastName = (String) assistant.get("lastName");
                String studentFirstName = (String) student.get("firstName");
                String studentLastName = (String) student.get("lastName");
                Student s = (Student) UserFactory.getUser("Student", studentFirstName, studentLastName);
                Assistant a = (Assistant) UserFactory.getUser("Assistant", assistantFirstName, assistantLastName);
                String course = (String) gradeObject.get("course");
                String grade = (String) gradeObject.get("grade");

                boolean found=false;
                for (Assistant key : partialScores.keySet()) {

                    if (key.getFirstName().equals(a.getFirstName()) && key.getLastName().equals(a.getLastName())) {
                        partialScores.get(key).add(new Tuple<Student, String, Double>(s, course, Double.valueOf(grade)));
                        found = true;
                    }
                }
                if(found==false)
                {
                    ArrayList<Tuple<Student, String, Double>> tuple = new ArrayList<>();
                    tuple.add(new Tuple<Student, String, Double>(s, course, Double.valueOf(grade)));
                    partialScores.put(a, tuple);
                }
            }
        } catch(IOException | ParseException e) {
            throw new RuntimeException(e);
        }
    }

    public void visit(Assistant assistant) {
        parseGradesAssistant();

        for (Assistant key : partialScores.keySet()) {

            if (key.getFirstName().equals(assistant.getFirstName()) && key.getLastName().equals(assistant.getLastName())) {
                for (Tuple<Student, String, Double> tuple : partialScores.get(key)) {
                    Student student = tuple.student;
                    String course_name = tuple.course_name;
                    Double partial_grade = tuple.course_grade;
                    for (Course course : Catalog.getInstance().getCourses()) {
                        if (course.getName().equals(course_name)) {
                            boolean contains = false;
                            for (Grade gr : course.getGrades()) {
                                if (gr.getStudent().equals(student)) {
                                    contains = true;
                                    gr.setPartialScore(partial_grade);
                                }
                            }
                            if (contains == false) {
                                Grade grade = new Grade();
                                grade.setCourse(course_name);
                                grade.setStudent(student);
                                grade.setPartialScore(partial_grade);
                                course.getGrades().add(grade);
                            }
                        }
                    }
                }
            }
        }
    }
    public void visit(Teacher teacher){
        parseGradesTeacher();

        for (Teacher key:examScores.keySet()) {

            if (key.getFirstName().equals(teacher.getFirstName()) && key.getLastName().equals(teacher.getLastName())) {

                for (Tuple<Student, String, Double> tuple : examScores.get(key)) {
                    Student student = tuple.student;
                    String course_name = tuple.course_name;
                    Double exam_grade = tuple.course_grade;
                    for (Course course : Catalog.getInstance().getCourses()) {
                        if (course.getName().equals(course_name)) {

                            boolean contains = false;
                            for (Grade gr : course.getGrades()) {
                                if (gr.getStudent().equals(student)) {
                                    contains = true;
                                    gr.setExamScore(exam_grade);
                                }
                            }
                            if (contains == false) {
                                Grade grade = new Grade();
                                grade.setCourse(course_name);
                                grade.setStudent(student);
                                grade.setExamScore(exam_grade);
                                course.getGrades().add(grade);
                            }
                        }
                    }
                }
            }
        }
    }

    public HashMap<Teacher, List<Tuple<Student, String, Double>>> getExamScores() {
        return examScores;
    }

    public void setExamScores(HashMap<Teacher, List<Tuple<Student, String, Double>>> examScores) {
        this.examScores = examScores;
    }

    public HashMap<Assistant, List<Tuple<Student, String, Double>>> getPartialScores() {
        return partialScores;
    }

    public void setPartialScores(HashMap<Assistant, List<Tuple<Student, String, Double>>> partialScores) {
        this.partialScores = partialScores;
    }

    public String displayGradesTeacher(Teacher teacher) {
        String res = "";

        parseGradesTeacher();

        for (Course course : Catalog.getInstance().getCourses()) {
            for (Map.Entry<Teacher, List<Tuple<Student, String, Double>>> key : examScores.entrySet()) {
                Teacher t = key.getKey();
                List<Tuple<Student, String, Double>> tuples = key.getValue();
                for (Tuple<Student, String, Double> tuple : tuples) {
                    if (t.equals(teacher) && tuple.course_name.equals(course.getName())) {
                        res += tuple.toString() + "\n";
                    }
                }
            }
        }

        return res;
    }
    public String displayGradesAssistant(Assistant assistant) {
        String res = "";

        parseGradesAssistant();

        for (Course course : Catalog.getInstance().getCourses()) {
            for (Map.Entry<Assistant, List<Tuple<Student, String, Double>>> key : partialScores.entrySet()) {
                Assistant a = key.getKey();
                List<Tuple<Student, String, Double>> tuples = key.getValue();
                for (Tuple<Student, String, Double> tuple : tuples) {
                    if (a.equals(assistant) && tuple.course_name.equals(course.getName())) {
                        res += tuple.toString() + "\n";
                    }
                }
            }
        }

        return res;
    }

    private class Tuple<St,S,D>{
        private  St student;
        private  S course_name;
        private D course_grade;
        public Tuple(St student,S course_name,D course_grade){
            this.student=student;
            this.course_name=course_name;
            this.course_grade=course_grade;
        }

        public boolean equals(Object o) {
            Tuple<Student, String, Double> t = (Tuple<Student, String, Double>) o;
            if (this.student.equals(t.student) && this.course_name.equals(t.course_name) && this.course_grade.equals(t.course_grade)) {
                return true;
            } else {
                return false;
            }
        }

        public String toString() {
            return student.toString() + " " + course_name + " " + course_grade;
        }
    }
}
