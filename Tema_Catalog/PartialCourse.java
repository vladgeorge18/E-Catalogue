import java.util.*;

public class PartialCourse extends Course {

    public ArrayList<Student> getGraduatedStudents() {
        ArrayList<Student> graduated=new ArrayList<Student>();
        HashMap<Student,Grade> map= getAllStudentGrades();
        for(Student student: map.keySet()){
            if(map.get(student).getTotal()>=5){
                graduated.add(student);
            }
        }
        return graduated;
    }
    public PartialCourse(PartialCourseBuilder builder){
        super(builder);
    }
    public static class PartialCourseBuilder extends Course.CourseBuilder{
        public Course build(){
            return new PartialCourse(this);
        }
    }
}
