package screens;

import exceptions.InvalidPasswordException;
import exceptions.LoginNotFoundException;
import managers.UserManager;
import model.Admin;

public class LoginScreen extends Screen {
    private final UserManager users;
    private String login;
    private String password;

    public LoginScreen() {
        this.users = hotel.getUsers();
        login = "";
        password = "";
    }

    @Override
    public void render() {
        System.out.println("Ekran logowania\n");
        System.out.println("Podaj swój login:");
        login = INPUT.nextLine();
        System.out.println("Podaj swoje hasło:");
        password = INPUT.nextLine();

        try {
            users.login(login, password);
            if (users.getCurrentUser() instanceof Admin) {
                System.out.println("\nZalogowano pomyślnie jako administrator. Przenoszenie do panelu administratora...\n");
                changeScreen(new AdminScreen());
            } else {
                System.out.println("\nZalogowano pomyślnie jako klient. Przenoszenie do panelu klienta...\n");
                changeScreen(new ClientScreen());
            }
        } catch (LoginNotFoundException loginException) {
            System.out.println("Błąd: użytkownik o podanym loginie nie istnieje!");
            System.out.println("To może być Twój błąd, lub takie konto wogule nie istnieje.");
            System.out.println("Jeśli jesteś klientem, może spróbuj się zarejestrować?");
            System.out.println("Jeśli jesteś administratorem skontaktuj się z innym z nich. Być może źle wprowadził Twój login?");
            changeScreen(new WelcomeScreen());
        } catch (InvalidPasswordException passwordException) {
            System.out.println("Błąd: niepoprawne hasło dla użytkownika " + login + "!");
            changeScreen(new WelcomeScreen());
        }
    }
}
