package library.util;

import library.model.Actor;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class ActorSet {

    public static String listActors(Set<Actor> actors) {
        List<String> actorsList = actors.stream().map(a -> a.getName()).collect(Collectors.toList());

        return String.join(", ", actorsList);
    }
}
