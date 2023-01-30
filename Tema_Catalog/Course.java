import java.util.*;
public abstract class Course {
    private  String name;
    private Teacher teacher;
    private HashSet<Assistant> Assistants= new HashSet<Assistant>();
    private ArrayList<Grade> grades=new ArrayList<Grade>();
    private HashMap<String,Group> groups= new HashMap<String,Group>();
    private int  credits;
    private Snapshot snapshot;


    public Course(CourseBuilder builder){
        this.name=builder.name;
        this.teacher=builder.teacher;
        this.Assistants=builder.Assistants;
        this.grades=builder.grades;
        this.groups=builder.groups;
        //this.snapshot=builder.snapshot;
        this.credits=builder.credits;
    }

    public String getName() {
        return name;
    }
    public String toString(){
        return this.getName();
    }

    public Teacher getTeacher() {
        return teacher;
    }

    public HashSet<Assistant> getAssistants() {
        return Assistants;
    }

    public HashMap<String, Group> getGroups() {
        return groups;
    }



    public ArrayList<Grade> getGrades() {
        return grades;

    }

    public int getCredits() {
        return credits;
    }

    public void addAssistnat(String ID, Assistant assistant){
        (groups.get(ID)).assistant=assistant;
        if(!(Assistants.contains(assistant))){
            Assistants.add(assistant);
        }

    }
    public void addStudent(String ID,Student student){
        (groups.get(ID)).add(student);

    }
    public void addGroup(Group group){
        groups.put(group.ID,group);
    }
    public void addGroup(String ID,Assistant assistant){
        Group group=new Group(ID,assistant);
        addGroup(group);
    }
    public void addGroup(String ID, Assistant assist, Comparator<Student> comp){
        Group group=new Group(ID, assist,comp);
        addGroup(group);
    }
    public Grade getGrade(Student student){
       for(Grade grade:grades){
           if(grade.getStudent().equals(student))
               return grade;
       }
       return null;
    }
    public void addGrade(Grade grade){
        grades.add(grade);
        Collections.sort(grades);
    }
    public ArrayList<Student> getAllStudents(){
        ArrayList<Student> list= new ArrayList<Student>();
        for(Group group: groups.values()){
            for(Student student:group){
                list.add(student);
            }
        }
        return list;
    }
    public HashMap<Student,Grade>getAllStudentGrades(){
        HashMap<Student,Grade> map=new HashMap<Student,Grade>();
        ArrayList<Student> students=getAllStudents();
        for(Student student:students){
            for(Grade grade:grades){
                if(grade.getStudent().equals(student)){
                    {
                        map.put(student,grade);
                        break;
                    }

                }
            }

        }
        return map;
    }
    public abstract static class CourseBuilder{
        private  String name;
        private Teacher teacher;
        private HashSet<Assistant> Assistants= new HashSet<Assistant>();
        private ArrayList<Grade> grades=new ArrayList<Grade>();
        private HashMap<String,Group> groups= new HashMap<String,Group>();
        private int credits;
        //private Snapshot snapshot;

        public CourseBuilder() {
        }

        public CourseBuilder name(String name){
            this.name=name;
            return this;
        }
        public CourseBuilder teacher(Teacher teacher){
            this.teacher=teacher;
            return this;
        }

        public CourseBuilder Assistants(HashSet<Assistant> Assistants){
            this.Assistants=Assistants;
            return this;
        }
        public CourseBuilder grades (ArrayList grades){
            this.grades=grades;
            return this;
        }
        public CourseBuilder groups(HashMap<String,Group> groups){
            this.groups=groups;
            return this;
        }
        public CourseBuilder credits(int credits){
            this.credits=credits;
            return this;
        }
       /* public CourseBuilder snapshot(Snapshot snapshot){
            this.snapshot=snapshot;
            return this;
        }*/
        public abstract Course build();
    }
    public Student getBestStudent(Strategy stragegy){
        return stragegy.getBestStudent(grades);
    }
    private class Snapshot{
        ArrayList<Grade> saved_grades;
        public Snapshot(){
            saved_grades=new ArrayList<Grade>();
        }

        public ArrayList<Grade> getSaved_grades() {

            return saved_grades;
        }

        public void setSaved_grades(ArrayList<Grade> saved_grades) {
            this.saved_grades = saved_grades;
        }
    }
    public abstract ArrayList<Student> getGraduatedStudents();


    public void makeBackup(){
            snapshot= new Snapshot();
            for (Grade grade:grades)
                snapshot.saved_grades.add(grade);
        }
    public void undo() {
        //snapshot= new Snapshot();
        grades = new ArrayList<Grade>();
        for (Grade grade : snapshot.saved_grades)
            grades.add(grade);

    }
}

