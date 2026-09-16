import java.util.ArrayList;
public class Member {

    //Variables
    private int id;
    private String name;
    private ArrayList<Book> borrowedBooks;

    //Constructor
    public Member(int id, String name){
        this.id = id;
        this.name = name;
        this.borrowedBooks = new ArrayList<Book>();
    }

    //Getters
    public int getId(){
        return id;
    }

    public String getName(){
        return name;
    }

    public ArrayList<Book> getBorrowedBooks(){
        //Send a copy so the main does not get wipped
        return new ArrayList<>(borrowedBooks);
    }


    //Setters
    public void addBorrowedBook(Book book){
        borrowedBooks.add(book);
        //Set the book availability to false
        book.setAvailability(false);
    }

    public void removeBorrowedBook(Book book){
        borrowedBooks.remove(book);
        //Set the book availability to true
        book.setAvailability(true);
    }

    //Display member information
    //Use Override toString to display info without having to call a method
    @Override
    public String toString(){
        String result = "_______________________________" + "\n" + "Member ID: " + id + "\n" + "Member Name: " + name + "\n" 
        + "Books checked out by Member: " + borrowedBooks.size() + "\n" + "Book Titles: " + "\n";

        for (Book b : borrowedBooks){
            result += b.getTitle() + "\n";
        }

        result += "_______________________________";

        return result;
    }
}
