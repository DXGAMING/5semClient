package com.client.information_window;

import com.client.connection.AttributeKey;
import com.client.connection.RequestParameter;
import javafx.scene.control.Alert;

import java.util.Map;

public class InformationWindow {
    public static void showAuthorizationError() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Ошибка авторизации");
        alert.setHeaderText(null);
        StringBuilder builder = new StringBuilder();

        builder.append("Не удалось войти в систему.\n");
        builder.append("1.Пожалуйста, убедитесь, что вы ввели свой логин и пароль правильно.\n");
        builder.append("2.Проверьте, не нажата ли клавиша Caps Lock.\n");
        builder.append("3.Убедитесь, что выбран правильный язык.\n");
        alert.setContentText(builder.toString());
        alert.showAndWait();
    }

    public static void showAccountIsBlocked() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Аккаунт заблокирован");
        alert.setHeaderText(null);
        alert.setContentText("Администратор заблокировал ваш аккаунт");
        alert.showAndWait();
    }

    public static void showLineIsEmpty() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("");
        alert.setHeaderText(null);
        alert.setContentText("\nВы не заполнили все поля!!!");
        alert.showAndWait();
    }

    public static void changePasswordError() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("");
        alert.setHeaderText(null);
        alert.setContentText("\nОшибка ввода, повторите попытку.");
        alert.showAndWait();
    }

    public static void changePasswordSuccessful() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("");
        alert.setHeaderText(null);
        alert.setContentText("\nПароль успешно изменен.");
        alert.showAndWait();
    }

    public static void showSuccessfulSaving() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("");
        alert.setHeaderText(null);
        alert.setContentText("\nДанные успешно сохранены");
        alert.showAndWait();
    }

    public static void showRegistrationError(Map<String, Boolean> incorrectParameters) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("");
        alert.setHeaderText(null);
        StringBuilder builder = new StringBuilder();
        Boolean isLoginExist = incorrectParameters.get(AttributeKey.LOGIN_EXIST);
        Boolean isEmailIncorrect = incorrectParameters.get(RequestParameter.EMAIL);
        Boolean isNameIncorrect = incorrectParameters.get(RequestParameter.NAME);
        Boolean isSurnameIncorrect = incorrectParameters.get(RequestParameter.SURNAME);
        Boolean isPasswordIncorrect = incorrectParameters.get(RequestParameter.PASSWORD);

        if (isLoginExist != null && isLoginExist) {
            builder.append("Такой логин уже существует.\n");
        }
        if (isEmailIncorrect != null && isEmailIncorrect) {
            builder.append("Неправильный формат эмейла.\n");
        }
        if (isNameIncorrect != null && isNameIncorrect) {
            builder.append("Неправильный формат имени.\n");
        }
        if (isSurnameIncorrect != null && isSurnameIncorrect) {
            builder.append("Неправильный формат фамилии.\n");
        }
        if (isPasswordIncorrect != null && isPasswordIncorrect) {
            builder.append("формат пароля должен быть в виде : S1s&.");
        }
        alert.setContentText(builder.toString());
        alert.showAndWait();
    }

    public static void showNoDataMessage() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("");
        alert.setHeaderText(null);
        alert.setContentText("\nДанных не найдено");
        alert.showAndWait();
    }

    public static void loginRepeatError() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("");
        alert.setHeaderText(null);
        alert.setContentText("\nтакой логин уже существует, выберете другой");
        alert.showAndWait();
    }
}
