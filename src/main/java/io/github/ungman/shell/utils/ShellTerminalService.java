package io.github.ungman.shell.utils;

import io.github.ungman.client.RestClient;
import io.github.ungman.pojo.Profile;
import io.github.ungman.pojo.User;
import io.github.ungman.utils.FormatData;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@AllArgsConstructor
@Component
public class ShellTerminalService {

    private  final RestClient restClient;
    private final ShellTerminalInput shellTerminalInput;
    private  Long idProfile;

    @Autowired
    public ShellTerminalService(RestClient restClient, ShellTerminalInput shellTerminalInput) {
        idProfile=-1L;
        this.restClient = restClient;
        this.shellTerminalInput = shellTerminalInput;
    }

    public void showClosingAppMessage() {
        String message = "Сервер недоступен. Повторите попытку позднее";
        printFormatMessage(message);
    }

    public void showProfile() {
        Profile profile=restClient.getProfile(idProfile);
        idProfile=profile.getIdUser();
        String profileFormatted = FormatData.getProfileFormatted(profile);
        System.out.println(profileFormatted);

    }

    public boolean checkAvailableConnection() {
        return restClient.checkAvailableConnection();
    }

    public void authUser() {
        User user=shellTerminalInput.fillUser();
        if(user==null)
            return;
        boolean isAuth=restClient.authUser(user);
        if(isAuth) {
            printFormatMessage("Добро пожаловать");
            showProfile();
        }
    }

    public void createUser() {
        User user=shellTerminalInput.fillUser();
        if(user==null) {
            printFormatMessage("Вы прервали ввод");
            return;
        }
        Profile profile=shellTerminalInput.fillProfile();
        if(profile==null){
            printFormatMessage("Вы прервали ввод");
            return;
        }
        profile.setName(user.getUsername());
        user=restClient.createUser(user);
        if(user==null){
            printFormatMessage("Произошла ошибка. Попробуйте попытку снова");
            return;
        }
        profile.setIdUser(user.getIdUser());
        profile=restClient.createProfile(profile);
        if(profile==null){
            printFormatMessage("Произошла ошибка. Попробуйте попытку снова");
            return;
        }
    }

    private void printFormatMessage(String message) {
        String dataFormatted = FormatData.showLinesFormatted(message, "|", "_");
        System.out.println(dataFormatted);
    }

    public boolean authCheck() {
       return restClient.checkAuth();
    }

    public boolean setMatch() {
        return  restClient.setMatch();
    }

    public boolean getMatch() {
        return  restClient.getMatch();
    }

    public void showMessageMatch() {
        String message="Вы любимы";
        System.out.println();
        printFormatMessage(message);
    }

    public void showMatchProfile() {
        List<Profile> listProfile = restClient.getMatchProfiles();
        if (listProfile.size() > 0) {

            int i = 0;
            for (Profile profile : listProfile) {
                String formattedString = i++ + ". " + profile.getName();
                String data = FormatData.showLinesFormatted(formattedString, "||", "-");
                System.out.println(data);
            }
            Integer numberAccount = -1;
            do {
                numberAccount = shellTerminalInput.getInputNumberMatchedProfile();
                if (numberAccount == null)
                    return;
            } while (numberAccount < 0 || numberAccount >= listProfile.size());

            String profileFormatted = FormatData.getProfileFormatted(listProfile.get(numberAccount));
            System.out.println(profileFormatted);
        }
    }

    public void exitFromAccount() {
        boolean result=restClient.exitFromAccount();
        printFormatMessage("Вы вышли из аакунта");
    }

    public void changeUserProfile() {
        Profile profile=shellTerminalInput.fillProfile();
        profile=restClient.editProfile(profile);
    }

    public void deleteAccount() {
        restClient.deleteAccount();
        printFormatMessage("Ваш аккаунт был удален");
    }
}
