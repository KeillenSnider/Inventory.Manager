class Book{
    //Variables
    private int id;
    private String title;
    private String author;
    private boolean availability;

    //Constructor
    public Book(int id, String title, String author, boolean availability){
        this.id = id;
        this.title = title;
        this.author = author;
        this.availability = availability;
    }

    //Getters
    public int getId(){
        return id;
    }

    public String getTitle(){
        return title;
    }

    public String getAuthor(){
        return author;
    }

    public boolean isAvailability(){
        return availability;
    }

    //Setters
    public void setAvailability(boolean availability){
        this.availability = availability;
    }

    //Display Book information
    //Use Override toString to display info without having to call a method
    @Override
    public String toString(){
        return "_______________________________" + "\n" +"Book ID: " + id + "\n" + "Book Title: " + title + "\n" + "Book Author: " + author + "\n" + 
        "Book Availability: " + availability + "\n" + "_______________________________";
    }
}