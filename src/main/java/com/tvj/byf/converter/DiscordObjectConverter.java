package com.tvj.byf.converter;

import com.tvj.byf.domain.*;
import sx.blah.discord.handle.obj.IUser;

public class DiscordObjectConverter {

    public static User discorduserToUser(IUser user){
        //todo user aanmaken in database
        return new User(user.getStringID(), user.getName(),user.getDiscriminator());
    }
}
