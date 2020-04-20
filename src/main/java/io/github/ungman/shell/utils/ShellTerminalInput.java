package io.github.ungman.shell.utils;


import io.github.ungman.client.RestClient;
import io.github.ungman.pojo.Profile;
import io.github.ungman.pojo.User;
import org.jline.reader.LineReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Component
public class ShellTerminalInput {

    @Autowired
    @Lazy
    private LineReader lineReader;
    private final RestClient serverClient;

    @Autowired
    public ShellTerminalInput(RestClient serverClient) {
        this.serverClient = serverClient;
    }

    public User fillUser() {
        User user = new User();
        String userLogin = getUserLogin();
        if (userLogin == null || userLogin.isEmpty())
            return null;
        String userPassword = getUserPassword();
        if (userPassword == null || userPassword.isEmpty())
            return null;
        user.setUsername(userLogin);
        user.setPassword(userPassword);
        return user;
    }

    public Profile fillProfile() {
        Profile profile = new Profile();
        profile.setGender(getProfileGender());
        profile.setDescription(getProfileDescription());
        return profile;
    }

    private String getUserLogin() {
        String login;
        while (true) {
            login = this.lineReader.readLine("Как вас величать: ");
            System.out.println();
            if (isNotNullAndEmptyString(login)) break;
            if ("уйти".equals(login)) {
                return null;
            }
            System.out.println("Введите правильные данные");
        }
        return login;
    }

    private boolean validateInputUserLogin(String login) {
        if (isNotNullAndEmptyString(login)) {
            return true;
        }
        return false;
    }

    private String getUserPassword() {
        String password;
        while (true) {
            password = this.lineReader.readLine("Ваш секретный шифръ: ", '*');
            System.out.println();
            if (isNotNullAndEmptyString(password))
                break;
            if ("уйти".equals(password)) {
                return null;
            }
            System.out.println("Введите правильные данные");
        }
        return password;
    }

    private String getProfileGender() {
        String gender;
        while (true) {
            gender = this.lineReader.readLine("Сударь или сударыня: ");
            System.out.println();
            if (parseGender(gender))
                break;
            if ("уйти".equals(gender))
                return null;
            System.out.println("Введите правильные данные");
        }
        return gender.toLowerCase();
    }

    private String getProfileDescription() {
        String description;
        while (true) {
            description = this.lineReader.readLine("Какіе вы и что вы ищите: ");
            System.out.println();
            if (isNotNullAndEmptyString(description))
                break;
            if ("уйти".equals(description)) {
                return null;
            }
            System.out.println("Введите правильные данные");
        }
        return description;
    }

    private boolean parseGender(String data) {
        boolean result = false;
        if (data == null)
            return result;
        if ("сударь".equals(data.trim().toLowerCase()))
            result = true;
        if ("сударыня".equals(data.trim().toLowerCase()))
            result = true;
        return result;
    }

    public Integer getInputNumberMatchedProfile() {
        Integer numberAccount = null;
        String inputData = lineReader.readLine("Номер: ");
        try {
            numberAccount = Integer.parseInt(inputData);
        } catch (Exception ignored) {
        }
        return numberAccount;
    }

    private boolean isNotNullAndEmptyString(String login) {
        return login != null && !login.isEmpty();
    }

}