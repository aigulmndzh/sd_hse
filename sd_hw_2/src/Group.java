import java.util.ArrayList;
import java.util.Objects;
import java.util.Scanner;

public class Group {
    public ArrayList<Student> students;
    private static final Scanner in = new Scanner(System.in);

    public Group() {
        students = new ArrayList<Student>();
    }

    public Group(ArrayList<Student> students) {
        if (!students.isEmpty()) {
            this.students.addAll(students);
        }
    }

    private void AutoAddStudents() {
        String answer;
        System.out.print("Студентов нет у вас. Добавить автоматически не хотите? ([y, yes]/[n, no]): ");
        answer = in.nextLine();
        while (!Objects.equals(answer, "y") && !Objects.equals(answer, "n") &&
                !Objects.equals(answer, "yes") && !Objects.equals(answer, "no")) {
            System.out.print("Неправильный ввод, попробуйте снова ([y, yes]/[n, no]) ");
            answer = in.nextLine();
        }
        if (Objects.equals(answer, "y") || Objects.equals(answer, "yes")) {
            students.add(new Student("Наруто", "Узумаки"));
            students.add(new Student("Сакура", "Харуно"));
            students.add(new Student("Итачи", "Учиха"));
            students.add(new Student("Минато", "Намикадзе"));
            students.add(new Student("Хината", "Хьюга"));
            students.add(new Student("Рок", "Ли"));
            students.add(new Student("Конохамару", "Сарутоби"));
            students.add(new Student("Какаши", "Хатакэ"));
            System.out.println("Автоматические студенты добавлены");
        } else {
            System.out.println("Автоматические студенты не добавлены");
        }
    }

    public void AddStudent() {
        System.out.print("Имя: ");
        String name = in.nextLine();
        System.out.print("Фамилия: ");
        String lastname = in.nextLine();
        students.add(new Student(name, lastname));
        System.out.printf("Студент %s %s добавлен!\n", name, lastname);
    }

    public void ShowStudentsWithGrades() {
        if (students.isEmpty()) {
            AutoAddStudents();
        } else {
            int count = 0;
            for (var student : students) {
                if (student.isPresent && student.mark >= 0) {
                    System.out.printf("%s  %s:  %d \n", student.firstName, student.lastName, student.mark);
                    count += 1;
                }
            }
            System.out.printf("Количество студентов, которые ответили: %d \n", count);
        }
    }

    public void ShowAllStudents() {
        if (students.isEmpty()) {
            AutoAddStudents();
        } else {
            for (var student : students) {
                if (student.mark >= 0) {
                    System.out.printf("%d. %s  %s:  %d \n", students.indexOf(student),
                            student.firstName, student.lastName, student.mark);
                } else {
                    System.out.printf("%d. %s  %s:  %s \n", students.indexOf(student),
                            student.firstName, student.lastName, "нет оценки");
                }
            }
        }
    }

    private int FindStudentsNumber() {
        System.out.println("1. /r - выбрать жертву рандомно\n2. /l - выбрать жертву по списку");
        String command = in.nextLine();
        int number = 0;
        switch (command) {
            case "/r":
                number = (int) (Math.random() * students.size());
                break;
            case "/l": {
                ShowAllStudents();
                System.out.print("Введите номер студента по списку: ");
                try {
                    number = in.nextInt();
                    while (number < 0 || number > students.size()) {
                        System.out.print("Такого номера нет в списке. Введите номер снова: ");
                        number = in.nextInt();
                    }
                    break;
                } catch (Exception e) {
                    System.out.println("Неверный ввод номера студента");
                }
            }
            default: {
                System.out.println("Выбор жертв(студентов) окончен");
                break;
            }
        }
        return number;
    }

    public void PutRatings() {
        if (students.isEmpty()) {
            AutoAddStudents();
        } else {
            int number = FindStudentsNumber();
            if (students.get(number).mark <= -1) {
                String isPresent;
                System.out.printf("Отвечает %s %s.\nПрисутствует ли на паре? ([y, yes]/[n, no]) ",
                        students.get(number).firstName, students.get(number).lastName);
                isPresent = in.nextLine();
                while (!Objects.equals(isPresent, "y") && !Objects.equals(isPresent, "n") &&
                        !Objects.equals(isPresent, "yes") && !Objects.equals(isPresent, "no")) {
                    System.out.print("Неправильный ввод, попробуйте снова ([y, yes]/[n, no]) ");
                    isPresent = in.nextLine();
                }
                if (isPresent.equals("y") || isPresent.equals("yes")) {
                    students.get(number).isPresent = true;
                } else {
                    students.get(number).isPresent = false;
                }

                if (students.get(number).isPresent) {
                    System.out.print("Оценка за семинар: ");
                    int grade = in.nextInt();
                    while (grade <= -1 || grade > 10) {
                        System.out.print("Ну поставьте нормальную оценку (от 0 до 10): ");
                        grade = in.nextInt();
                    }
                    students.get(number).mark = grade;
                } else {
                    System.out.println("Сегодня студенту плохо (наверно) и он не пришел");
                }
            } else {
                System.out.printf("А студент %s %s ответил уже\n", students.get(number).firstName, students.get(number).lastName);
            }
        }
    }
}
