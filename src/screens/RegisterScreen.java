package screens;

import exceptions.DuplicateLoginException;
import exceptions.EmptyPasswordException;
import model.Client;

public class RegisterScreen extends Screen {
    private String login;
    private String password;

    public RegisterScreen() {
        login = "";
        password = "";
    }

    @Override
    public void render() {
        System.out.println("Ekran rejestracji klienta\n");
        System.out.print("Utwórz swój login. ");
        System.out.println("Pamiętaj, że musi być unikalny - nie może być 2 użytkowników z tym samym loginem!");
        System.out.println("Podaj swój login:");
        login = INPUT.nextLine();
        System.out.println("Podaj swoje hasło:");
        password = INPUT.nextLine();

        try {
            hotel.add(new Client(login, password));
            System.out.println("\nPomyślnie utworzono konto. Możesz się teraz do niego zalogować.\n");
        } catch (DuplicateLoginException duplicateException) {
            System.out.println("\nBłąd: Twój login jest już zajęty!");
            System.out.println("Jeśli to Twoje konto, to się do niego zaloguj.");
            System.out.println("W przeciwnym wypadku utwórz inne konto.\n");
        } catch (EmptyPasswordException e) {
            System.out.println("\nBłąd: Twoje hasło jest puste!");
            System.out.println("Spróbuj stworzyć konto jeszcze raz, ale tym razem podaj hasło.\n");
        }
        changeScreen(new WelcomeScreen());
    }
}
