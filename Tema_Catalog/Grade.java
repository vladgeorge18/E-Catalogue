public class Grade implements Comparable<Grade>,Cloneable{
    private Double partialScore, examScore;
    private Student student;
    private String course; // Numele cursului
    public Notification notification;

    public Grade() {

    }

    public Grade(Double partialScore, Double examScore, Student student, String course) {
        this.partialScore = partialScore;
        this.examScore = examScore;
        this.student = student;
        this.course = course;
    }

    public void setPartialScore(Double partialScore){
        this.partialScore=partialScore;
    }
    public Double getPartialScore(){
        return partialScore;
    }

    public void setExamScore(Double examScore) {
        this.examScore = examScore;
    }
    public Double getExamScore() {
        return examScore;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    public String getCourse() {
        return course;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public Student getStudent() {
        return student;
    }
    public Double getTotal(){
        return partialScore+examScore;
    }
    public String toString(){
        return this.partialScore + " " + this.getExamScore();
    }

    public int compareTo(Grade grade) {
        if (this.getTotal() < grade.getTotal())
            return 1;
        else if (this.getTotal() > grade.getTotal())
            return -1;
        else
            return 0;
    }
    public Object clone()
            throws CloneNotSupportedException
    {
        return super.clone();
    }

}
