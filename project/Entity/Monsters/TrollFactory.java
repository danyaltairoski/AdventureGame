package Entity.Monsters;

import AdventureModel.Room;

/*
* Factory design pattern. Takes an input and is responsible to creating that troll
 */
public class TrollFactory {

    public Troll createTroll(Room.TrollType type) {
        switch (type) {
            case KING:
                return new KingTroll();
            case SUPER:
                return new SuperTroll();
            case MINI:
                return new MiniTroll();
            default:
                throw new IllegalArgumentException("Unsupported troll type: " + type);
        }
    }
}

