import java.util.Objects;
import java.util.Scanner;

public class Main {
    public static void help() {
        System.out.println(
                "1. /l - Показать список студентов (всех)\n" +
                "2. /a - Показать список студентов (только тех, кто ответил)\n" +
                "3. /d - Добавить студента в группу\n" +
                "4. /h - Допросить жертву(студента)\n" +
                "5. /help - Справка (этот список функций)\n" +
                "6. /exit - Я устал быть семинаристом (выход из программы)");
    }
    public static void main(String[] args) {
        Group students = new Group();
        Scanner in = new Scanner(System.in);
        System.out.print("Привет! Сегодня ты будешь оценивать студентов, твои функции: \n");
        help();

        System.out.print("Введите команду: ");
        String command = in.nextLine();
        while (!Objects.equals(command, "/exit")) {
            switch (command) {
                case  "/l":
                    students.ShowAllStudents();
                    break;
                case "/a" :
                    students.ShowStudentsWithGrades();
                    break;
                case "/d":
                    students.AddStudent();
                    break;
                case "/h":
                    students.PutRatings();
                    break;
                case "/help":
                    help();
                    break;
            }
            System.out.print("Введите команду: ");
            command = in.nextLine();
        }
    }
}