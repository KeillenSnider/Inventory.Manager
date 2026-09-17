public abstract class LibraryItem {
    private int id;
    private String title;
    private boolean availability;

    //Constructor
    public LibraryItem(int id, String title, boolean availability){
        this.id = id;
        this.title = title;
        this.availability = availability;
    }

    //Getters
    public int getId(){
        return id;
    }

    public String getTitle(){
        return title;
    }

    public boolean isAvailability(){
        return availability;
    }

    //Setters
    public void setAvailability(boolean availability){
        this.availability = availability;
    }

    public int getLoanPeriodDays(){
        return 14;
    }

    
}
