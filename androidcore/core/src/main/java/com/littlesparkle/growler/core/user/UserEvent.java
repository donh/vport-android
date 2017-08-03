package com.littlesparkle.growler.core.user;

public class UserEvent {
    public final boolean signedIn;
    public final BaseUser mBaseUser;

    public UserEvent(boolean signedIn, BaseUser baseUser) {
        this.signedIn = signedIn;
        this.mBaseUser = baseUser;
    }

    @Override
    public String toString() {
        return "UserEvent{" +
                "signedIn=" + signedIn +
                ", mBaseUser=" + mBaseUser +
                '}';
    }
}
