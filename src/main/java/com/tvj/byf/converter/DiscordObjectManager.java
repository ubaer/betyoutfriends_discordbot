package com.tvj.byf.converter;

import com.tvj.byf.dao.UserJPA;
import com.tvj.byf.domain.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sx.blah.discord.handle.obj.IUser;

import javax.transaction.Transactional;

@Service
@Transactional
public class DiscordObjectManager {

    private final UserJPA userJPA;

    @Autowired
    public DiscordObjectManager(UserJPA userJPA) {
        this.userJPA = userJPA;
    }

    public User discorduserToUser(IUser user) {
        User dbUser = getDatabaseUser(user);
        if (dbUser == null) {
            dbUser = createDatabaseUser(user);
        }
        return dbUser;
    }


    private User getDatabaseUser(IUser user) {
        return userJPA.findOne(user.getStringID());
    }

    private User createDatabaseUser(IUser discordUser) {
        User user = new User(discordUser.getStringID(), discordUser.getName(), discordUser.getDiscriminator());
        user = userJPA.save(user);
        return user;
    }
}
