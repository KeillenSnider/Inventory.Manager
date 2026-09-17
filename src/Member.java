import java.util.ArrayList;
public class Member {

    //Variables
    private int id;
    private String name;
    private ArrayList<LibraryItem> items;

    //Constructor
    public Member(int id, String name){
        this.id = id;
        this.name = name;
        this.items = new ArrayList<LibraryItem>();
    }

    //Getters
    public int getId(){
        return id;
    }

    public String getName(){
        return name;
    }

    public ArrayList<LibraryItem> getBorrowedItems(){
        //Send a copy so the main does not get wipped
        return new ArrayList<>(items);
    }


    //Setters
    public void addBorrowedItem(LibraryItem item){
        items.add(item);
        //Set the item availability to false
        item.setAvailability(false);
    }

    public void removeBorrowedItem(LibraryItem item){
        items.remove(item);
        //Set the item availability to true
        item.setAvailability(true);
    }

    //Display member information
    //Use Override toString to display info without having to call a method
    @Override
    public String toString(){
        String result = "_______________________________" + "\n" + "Member ID: " + id + "\n" + "Member Name: " + name + "\n" 
        + "Items checked out by Member: " + items.size() + "\n" + "Items Titles: " + "\n";

        for (LibraryItem i : items){
            result += i.getTitle() + "\n";
        }

        result += "_______________________________";

        return result;
    }
}
