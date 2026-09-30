package com.dapl.halaladvisor.singelton;

/**
 * TODO: Created by Tanay Mondal on 02-11-2017
 */

public class ActivityCommunication {

    private static ActivityCommunication instance = null;

    private RegisterActivityKillListener regKillListener = null;
    private LoginActivityKillListener loginKillListener = null;
    private BeforeLoginActivityKillListener beforeLoginKillListener = null;

    private ActivityCommunication() {

    }

    public static ActivityCommunication getInstance() {
        if (instance == null) {
            instance = new ActivityCommunication();
        }
        return instance;
    }

    public void setRegisterActivityKillListener(RegisterActivityKillListener listener) {
        this.regKillListener = listener;
    }

    public void setLoginActivityKillListener(LoginActivityKillListener listener) {
        this.loginKillListener = listener;
    }

    public void setBeforeLoginActivityKillListener(BeforeLoginActivityKillListener listener) {
        this.beforeLoginKillListener = listener;
    }

    public void killLoginActivityNow() {
        if (loginKillListener != null) {
            loginKillListener.killLoginActivityNow();
        }
    }

    public void killRegisterActivityNow() {
        if (regKillListener != null) {
            regKillListener.killRegisterActivityNow();
        }
    }

    public void killBeforeLoginActivityNow() {
        if (beforeLoginKillListener != null) {
            beforeLoginKillListener.killBeforeLoginActivityNow();
        }
    }

    public interface RegisterActivityKillListener {
        void killRegisterActivityNow();
    }

    public interface LoginActivityKillListener {
        void killLoginActivityNow();
    }

    public interface BeforeLoginActivityKillListener {
        void killBeforeLoginActivityNow();
    }
}
