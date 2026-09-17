public class Book extends LibraryItem{
    //Variables
    private String author;

    //Constructor
    public Book(int id, String title, String author, boolean availability){
        super(id, title, availability);
        this.author = author;
    }

    public String getAuthor(){
        return author;
    }
    @Override 
    public int getLoanPeriodDays(){
        return 21;
    }

    //Display Book information
    //Use Override toString to display info without having to call a method
    @Override
    public String toString(){
        return "_______________________________" + "\n" +"Book ID: " + getId() + "\n" + "Book Title: " + getTitle() + "\n" + "Book Author: " + author + "\n" + 
        "Book Availability: " + isAvailability() + "\n" + "Book Loan Period: " + getLoanPeriodDays() + " days\n" + "_______________________________";
    }
}