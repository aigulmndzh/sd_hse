public class Student {
    public String firstName;
    public String lastName;
    public Boolean isPresent;
    public Integer mark;

    public Student() {
        mark = -1;
        isPresent = false;
        firstName = "Студент";
        lastName = "Студентович";
    }

    public Student(String firstName, String lastName) {
        this.mark = -1;
        this.firstName = firstName;
        this.lastName = lastName;
        this.isPresent = false;
    }
}
