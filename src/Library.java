import java.util.ArrayList;
public class Library {
    
    //Variables
    private ArrayList<LibraryItem> items;
    private ArrayList<Member> members;

    //Constructor
    public Library(){
        this.items = new ArrayList<LibraryItem>();
        this.members = new ArrayList<Member>();
    }


    //Methods

    //Get an item by the ID
    public LibraryItem getItemById(int id){
        for(LibraryItem i : items){
            if(i.getId() == id){
                return i;
            }
        }
        return null;
    }

    //Get a member by the ID
    public Member getMemberById(int id){
        for(Member m : members){
            if(m.getId() == id){
                return m;
            }
        }
        return null;
    }


    //Validate the item and member
    private boolean validateMemberAndItem(Member member, LibraryItem item){
        //Make sure the member and item are not null
        if(member == null || member.getName() == null || item == null || item.getTitle() == null){
            System.out.println("No part of the member or item can be empty.");
            return false;
        }
        //Make sure the member exists in the library
        if(!members.contains(member)){
            System.out.println("This member does not exist in the database.");
            return false;
        }
        //Make sure the item exists in the library
        if(!items.contains(item)){
            System.out.println("This item does not exist in the database.");
            return false;
        }
        return true;
    }

    //Add an item to the library and make sure it has a unique ID
    public boolean addItem(LibraryItem item){
        //Check for null values
        if(item == null || item.getTitle() == null){
            System.out.println("No part of the item can be empty.");
            return false;
        }
        //Loop to see if Id is unique
        for(LibraryItem i : items){
            if(i.getId() == item.getId()){
                System.out.println("Item ID already exists. Please use a unique ID.");
                return false;
            }
        }
        //Add to the List
        items.add(item);
        return true;
    }


    //Add a member to the library and make sure it has a unique ID
    public boolean addMember(Member member){
        //Check for null values
        if(member == null || member.getName() == null){
            System.out.println("No part of the member can be empty.");
            return false;
        }
        //Loop to see if Id is unique
        for(Member m : members){
            if(m.getId() == member.getId()){
                System.out.println("Member ID already exists. Please use a unique ID.");
                return false;
            }
        }
        //Add to the List
        members.add(member);
        return true;
    }


    //Check out an item to a member
    public boolean checkOutItem(Member member, LibraryItem item){
        //Make sure the member and item are not null
        if(!validateMemberAndItem(member, item)){
            return false;
        }
        //Check if the member has already checked out the item
        if(member.getBorrowedItems().contains(item)){
            System.out.println("This member has already checked out this item.");
            return false;
        }
        //Check if the item is available
        if(!item.isAvailability()){
            System.out.println("This item is not currently available for checkout.");
            return false;
        }

        //If all pass then checkout the item
        member.addBorrowedItem(item);
        return true;
    }


    //Return an item from a member
    public boolean returnItem(Member member, LibraryItem item){
        //Make sure the member and item are not null
        if(!validateMemberAndItem(member, item)){
            return false;
        }
        //Check if the member has already checked out the item
        if(!member.getBorrowedItems().contains(item)){
            System.out.println("This member has not checked out this item.");
            return false;
        }

        //If all pass then return the item
        member.removeBorrowedItem(item);
        return true;
    }


    //Remove an item from the library
    public boolean removeItem(LibraryItem item){
        //Make sure the item is not null
        if(item == null || item.getTitle() == null){
            System.out.println("No part of the item can be empty.");
            return false;
        }
        //Check if the item exists in the library
        if(!items.contains(item)){
            System.out.println("This item does not exist in the database.");
            return false;
        }
        //Check if the item is currently checked out
        if(!item.isAvailability()){
            System.out.println("This item is currently checked out and cannot be removed.");
            return false;
        }
        //If all pass then remove the item
        items.remove(item);
        return true;
    }

    //Remove a member from the library
    public boolean removeMember(Member member){
        //Make sure the member is not null
        if(member == null || member.getName() == null){
            System.out.println("No part of the member can be empty.");
            return false;
        }
        //Check if the member exists in the library
        if(!members.contains(member)){
            System.out.println("This member does not exist in the database.");
            return false;
        }
        //Check if the member has any items checked out
        if(!member.getBorrowedItems().isEmpty()){
            System.out.println("This member has items checked out and cannot be removed.");
            return false;
        }
        //If all pass then remove the member
        members.remove(member);
        return true;
    }

    //Search for an item by title
    public ArrayList<LibraryItem> searchItemByTitle(String title){
        ArrayList<LibraryItem> result = new ArrayList<LibraryItem>();

        if(title == null || title.trim().isEmpty()){
            System.out.println("Title cannot be empty.");
            return result;
        }
        for(LibraryItem i : items){
            if(i.getTitle().toLowerCase().contains(title.toLowerCase())){
                result.add(i);
            }
        }
        return result;
    }
}