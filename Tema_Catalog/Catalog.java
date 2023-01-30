import static java.util.Objects.isNull;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class Catalog implements Subject {
    private List<Observer> observers = new ArrayList<>();
    private List<Course> Courses= new ArrayList<Course>();
    private static Catalog instance=null;
    public static Catalog getInstance(){
        if(isNull(instance)){
            instance= new Catalog();
        }
        return instance;
    }
    private Catalog(){

    }

    public List<Course> getCourses() {
        return Courses;
    }

    public void setCourses(List<Course> courses) {
        Courses = courses;
    }

    public void addCourse(Course course){
       if(Courses.contains(course)==false) {
            Courses.add(course);
        }
    }
    public void removeCourse(Course course){
        if(Courses.contains(course)==true)
        Courses.remove(course);
    }
    public void addObserver(Observer observer){
        observers.add(observer);
    }
    public void removeObserver(Observer observer){
        observers.remove(observer);
    }
    public void notifyObservers(Grade grade){
        for (Observer observer:observers){
                if (grade.getStudent().getMother().equals(observer)){
                    Notification notification=new Notification(grade);
                    observer.update(notification);
                }
                if (grade.getStudent().getFather().equals(observer)){
                    Notification notification=new Notification(grade);
                    observer.update(notification);
                }
        }
    }

    public void parse() {
        Courses.clear();
        JSONParser parser = new JSONParser();

        try {
            FileReader fileReader = new FileReader("TestFiles/test.json");
            JSONObject coursesObject = (JSONObject) parser.parse(fileReader);
            JSONArray courses = (JSONArray) coursesObject.get("courses");

            for (int i = 0; i < courses.size(); i++) {
                JSONObject course = (JSONObject) courses.get(i);
                String name = (String) course.get("name");
                String credits = (String) course.get("credits");
                JSONObject teacherObject = (JSONObject) course.get("teacher");
                String firstName = (String) teacherObject.get("firstName");
                String lastName = (String) teacherObject.get("lastName");
                Teacher teacher = (Teacher) UserFactory.getUser("Teacher", firstName, lastName);

                HashSet<Assistant> assistants = new HashSet<>();
                JSONArray assistantArray = (JSONArray) course.get("assistants");
                for (int j = 0; j < assistantArray.size(); j++) {
                    JSONObject assistant = (JSONObject) assistantArray.get(j);
                    String assistantFirstName = (String) assistant.get("firstName");
                    String assistantLastName = (String) assistant.get("lastName");
                    assistants.add((Assistant) UserFactory.getUser("Assistant", assistantFirstName, assistantLastName));
                }

                ArrayList<Grade> grades = new ArrayList<>();
                JSONArray courseGrades = (JSONArray) course.get("grades");
                for (int j = 0; j < courseGrades.size(); j++) {
                    JSONObject grade = (JSONObject) courseGrades.get(j);
                    String partial = (String) grade.get("partial");
                    String exam = (String) grade.get("exam");
                    JSONObject gradeStudent = (JSONObject) grade.get("student");
                    String studentFirstName = (String) gradeStudent.get("firstName");
                    String studentLastName = (String) gradeStudent.get("lastName");
                    Student student = (Student) UserFactory.getUser("Student", studentFirstName, studentLastName);
                    String courseName = (String) grade.get("course");
                    grades.add(new Grade(Double.valueOf(partial), Double.valueOf(exam), student, courseName));
                }

                HashMap<String, Group> groups = new HashMap<>();
                JSONArray groupArray = (JSONArray) course.get("groups");
                for (int j = 0; j < groupArray.size(); j++) {
                    JSONObject groupObject = (JSONObject) groupArray.get(j);
                    String groupID = (String) groupObject.get("id");
                    JSONObject assistant = (JSONObject) groupObject.get("assistant");
                    String assistantFirstName = (String) assistant.get("firstName");
                    String assistantLastName = (String) assistant.get("lastName");
                    Group group = new Group(groupID, (Assistant) UserFactory.getUser("Assistant", assistantFirstName, assistantLastName));

                    JSONArray students = (JSONArray) groupObject.get("students");
                    for (int m = 0; m < students.size(); m++) {
                        JSONObject student = (JSONObject) students.get(m);
                        String studentFirstName = (String) student.get("firstName");
                        String studentLastName = (String) student.get("lastName");
                        group.add((Student) UserFactory.getUser("Student", studentFirstName, studentLastName));
                    }

                    groups.put(groupID, group);
                }

                String type = (String) course.get("type");

                if (type.equals("partial")) {
                    PartialCourse coursePartial =(PartialCourse) new PartialCourse.PartialCourseBuilder().name(name)
                            .teacher(teacher)
                            .Assistants(assistants)
                            .groups(groups)
                            .grades(grades)
                            .credits(Integer.parseInt(credits))
                            .build();
                    Courses.add(coursePartial);
                } else if (type.equals("full")) {
                    FullCourse courseFull = (FullCourse) new FullCourse.FullCourseBuilder().name(name)
                            .teacher(teacher)
                            .Assistants(assistants)
                            .groups(groups)
                            .grades(grades)
                            .credits(Integer.parseInt(credits))
                            .build();
                    Courses.add(courseFull);
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
}
