package io.github.ungman.shell;

import io.github.ungman.shell.utils.ShellTerminalService;
import org.jline.reader.LineReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.shell.Availability;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellMethodAvailability;

@ShellComponent
public class ShellTerminal {

    @Autowired
    @Lazy
    private LineReader lineReader;
    private final ShellTerminalService shellTerminalService;

    @Autowired
    public ShellTerminal(ShellTerminalService shellTerminalService) {
        this.shellTerminalService = shellTerminalService;
        if(!init()){
            shellTerminalService.showClosingAppMessage();
            System.exit(-1);
        }
    }

    @ShellMethod("Влево \n" +
            "Если пользователь не авторизован - показ предыдущей анкеты \n" +
            "Если пользователь авторизован - пропуск анкеты")
    public void влево() {
        shellTerminalService.showProfile();
    }

    @ShellMethodAvailability("accountAvailability")
    @ShellMethod("Вправо \n" +
            "Если пользователь не авторизован - показ следующей анкеты \n" +
            "Если пользователь авторизован - отмечаем интерес")
    public void вправо() {
        shellTerminalService.setMatch();
        if(shellTerminalService.getMatch())
            shellTerminalService.showMessageMatch();
        shellTerminalService.showProfile();
    }

    @ShellMethod("Авторизоваться в системе для возможности ставить лайки интересующим анкетам.")
    public void войти() {
        System.out.println();
        shellTerminalService.authUser();
    }

    @ShellMethodAvailability("createAccountAvailability")
    @ShellMethod("Создание анкеты для возможности ставить лайки интересующим анкетам.")
    public void создать() {
        System.out.println();
        shellTerminalService.createUser();
    }

    @ShellMethodAvailability("accountAvailability")
    @ShellMethod("Редактирование информации об себе")
    public void изменить() {
        System.out.println();
        shellTerminalService.changeUserProfile();
    }

    @ShellMethodAvailability("accountAvailability")
    @ShellMethod("Удалить аккаунт")
    public void удалить() {
        System.out.println();
        shellTerminalService.deleteAccount();
    }
//
    @ShellMethodAvailability("accountAvailability")
    @ShellMethod("Выход из аккаунта")
    public void выйти() {
        System.out.println();
        shellTerminalService.exitFromAccount();
    }

    @ShellMethodAvailability("accountAvailability")
    @ShellMethod("Показать обоюдные симпатии")
    public void любимцы() {
        shellTerminalService.showMatchProfile();
    }

    public Availability accountAvailability() {
        return shellTerminalService.authCheck() ? Availability.available() :
                Availability.unavailable("Пожалуйста зайдите в аккаунт");
    }

    public Availability createAccountAvailability() {
        return !shellTerminalService.authCheck() ? Availability.available() :
                Availability.unavailable("Пожалуйста зайдите в аккаунт");
    }

    private boolean init() {
        boolean isServerAvailable = shellTerminalService.checkAvailableConnection();
        if (isServerAvailable)
            shellTerminalService.showProfile();
        return isServerAvailable;
    }
}
