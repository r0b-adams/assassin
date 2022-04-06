
import java.util.*;

// The Assassin Manager keeps track of the participants of a game of Assassin,
// maintaining a list of active participants and who they're targeting, and
// another of people that have been killed and their killer. The last person
// left alive is declared the winner.
public class AssassinManager {
   private AssassinNode killRing;   // reference to the first name in the kill ring
   private AssassinNode graveyard;  // reference to the first name in the graveyard list

   // Param: names: The list of names of people in the Assassin game.
   //   Pre: The passed list of names is not null and its size is not zero;
   //        otherwise, throws IllegalArgumentException.
   //  Post: Constructs a new Assassin Manager, with names of the people given
   //        in the kill ring list, and the graveyard list initially empty
   public AssassinManager(List<String> names) {
      if (names == null || names.size() == 0) {
         throw new IllegalArgumentException();
      }
      this.killRing = addNames(names);
      this.graveyard = null;
   }

   // Param: names: The list of names of people in the Assassin game.
   //  Post: Builds a list of names of people in the game, and returns
   //        a reference to the first person in the list.
   private AssassinNode addNames(List<String> names) {
      AssassinNode first = new AssassinNode(names.get(0));
      AssassinNode buildList = first;
      for (int i = 1; i < names.size(); i++) {
         buildList.next = new AssassinNode(names.get(i));
         buildList = buildList.next;
      }
      return first;
   }

   // Post: Prints the names of people still active in the game, and their targets,
   //       with each person (and who they're targeting) on their own line
   public void printKillRing() {
      AssassinNode living = this.killRing;
      while (living.next != null) {
         System.out.println("  " + living.name + " is stalking " + living.next.name);
         living = living.next;
      }

      // last person in the list is stalking the first
      System.out.println("  " + living.name + " is stalking " + this.killRing.name);
   }

   // Post: Prints a list of the people that have been killed and their killer,
   //       each on their own line from most recently killed to first killed.
   public void printGraveyard() {
      AssassinNode dead = this.graveyard;
      while (dead != null) {
         System.out.println("  " + dead.name + " was killed by " + dead.killer);
         dead = dead.next;
      }
   }

   // Param: name: person given by user to be killed
   //  Post: returns true if name appears in kill ring list, else false
   public boolean killRingContains(String name) {
      if (containsName(this.killRing, name)) {
         return true;
      }
      return false;
   }

   // Param: name: person given by user to be killed
   //  Post: returns true if name appears in graveyard list, else false
   public boolean graveyardContains(String name) {
      if (containsName(this.graveyard, name)) {
         return true;
      }
      return false;
   }

   // Param: nameCheck: reference to the front of the list to check (killRing or graveyard)
   //             name: person given by user to be killed
   //  Post: returns true if the name appears in the passed list, else false
   private boolean containsName(AssassinNode nameCheck, String name) {
      AssassinNode current = nameCheck;
      while (current != null) {
         if (current.name.equalsIgnoreCase(name)) {
            return true;
         }
         current = current.next;
      }
      return false;
   }

   //  Post: returns true if there is one person left in kill ring, else false
   public boolean isGameOver() {
      if (this.killRing.next == null) {
         return true;
      }
      return false;
   }

   //  Post: returns the name of the last person in kill ring
   public String winner() {
      return this.killRing.name;
   }

   // Param: name: person given by user to be killed
   //   Pre: the game is not over, else throws IllegalStateException
   //        the name given is part of the kill ring, else throws IllegalArguementException
   //  Post: removes the given name from the kill ring and adds it to front of graveyard list
   public void kill(String name) {
      if (isGameOver()) {
         throw new IllegalStateException();
      }
      if (!killRingContains(name)) {
         throw new IllegalArgumentException();
      }
      AssassinNode current = this.killRing;
      AssassinNode killed;

      // if the person to be killed is at the front of the kill ring
      if (this.killRing.name.equalsIgnoreCase(name)) {
         killed = this.killRing;
         while (current.next != null) {   // go to the end of the list to get killer's name
            current = current.next;
         }
         killed.killer = current.name;
         this.killRing = this.killRing.next;

      // otherwise, keep looking for person to be killed
      } else {
         while (!current.next.name.equalsIgnoreCase(name)) {
            current = current.next;
         }
         killed = current.next;
         killed.killer = current.name;

         // remove killed from kill ring
         if (current.next.next == null) {     // if the killed person is last in the list,
            current.next = null;              // the second to last person then stalks the first
         } else {
            current.next = current.next.next; // else, the kill ring skips the killed person
         }
      }

      // strips the killed person of their target and adds them to the front of the graveyard list
      killed.next = null;
      killed.next = this.graveyard;
      this.graveyard = killed;
   }
}