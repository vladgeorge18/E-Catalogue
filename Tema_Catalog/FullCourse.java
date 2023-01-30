import java.util.ArrayList;
import java.util.HashMap;

public class FullCourse extends Course{
    public ArrayList<Student> getGraduatedStudents() {
        ArrayList<Student> graduated=new ArrayList<Student>();
        HashMap<Student,Grade> map= getAllStudentGrades();
        for(Student student: map.keySet()){
            if(map.get(student).getPartialScore()>=3 && map.get(student).getExamScore()>=2){
                graduated.add(student);
            }
        }
        return graduated;
    }
    public FullCourse(FullCourseBuilder builder){
        super(builder);
    }
    public static class FullCourseBuilder extends Course.CourseBuilder{
        public Course build(){
            return new FullCourse(this);
        }
    }
}
