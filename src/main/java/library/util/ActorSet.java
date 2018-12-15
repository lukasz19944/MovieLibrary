package library.util;

import library.model.Actor;

import java.util.Set;

public class ActorSet {

    public static String listActors(Set<Actor> actors) {
        String result = "";

        for (Actor actor : actors) {
            result += actor.getFirstName() + " " + actor.getLastName() + " | ";
        }

        return result;
    }
}
