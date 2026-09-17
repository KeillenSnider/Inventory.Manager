public class DVD extends LibraryItem {
    private String director;

    //Constructor
    public DVD(int id, String title, String director, boolean availability){
        super(id, title, availability);
        this.director = director;
    }

    public String getDirector(){
        return director;
    }

    @Override
    public int getLoanPeriodDays(){
        return 7;
    }

    @Override
    public String toString(){
        return "_______________________________" + "\n" +"DVD ID: " + getId() + "\n" + "DVD Title: " + getTitle() + "\n" + "DVD Director: " + director + "\n" +
        "DVD Availability: " + isAvailability() + "\n" + "DVD Loan Period: " + getLoanPeriodDays() + " days\n" + "_______________________________";
    }
}
