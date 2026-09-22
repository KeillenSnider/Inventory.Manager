public class Member {

    //Variables
    private int id;
    private String name;

    //Constructor
    public Member(int id, String name){
        this.id = id;
        this.name = name;
    }

    //Getters
    public int getId(){
        return id;
    }

    public String getName(){
        return name;
    }

    //Display member information
    //Use Override toString to display info without having to call a method
    @Override
    public String toString(){
        String result = "_______________________________" + "\n" + "Member ID: " + id + "\n" + "Member Name: " + name + "\n" 
        + "\n";

        result += "_______________________________";

        return result;
    }
}
