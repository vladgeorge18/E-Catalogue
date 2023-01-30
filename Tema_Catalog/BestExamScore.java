import java.util.ArrayList;
import java.util.SortedSet;

public class BestExamScore implements Strategy {
        public Student getBestStudent(ArrayList<Grade> grades){
            Grade curr_grade=grades.get(0);
            Double result=0.0;
            for(Grade grade:grades){
                if(grade.getExamScore()>result){
                    curr_grade=grade;
                    result=grade.getExamScore();
                }
            }
            return curr_grade.getStudent();
        }
    }
