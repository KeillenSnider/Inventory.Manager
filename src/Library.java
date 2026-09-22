import java.util.ArrayList;

public class Library {
    
    //Methods

    //Get an item by the ID
    public LibraryItem getItemById(int id){
        return Database.getItemById(id);
    }

    //Get a member by the ID
    public Member getMemberById(int id){
        return Database.getMemberById(id);
    }

    //Find all items that are overdue
    public ArrayList<String> findOverdueItems(){
        return Database.findOverdueItems();
    }


    //Validate the item and member
    private boolean validateMemberAndItem(Member member, LibraryItem item){
        //Make sure the member and item are not null
        if(member == null || member.getName() == null || item == null || item.getTitle() == null){
            System.out.println("____________________________________________________");
            System.out.println("No part of the member or item can be empty.");
            System.out.println("____________________________________________________");
            return false;
        }
        //Make sure the member exists in the library
        if(getMemberById(member.getId()) == null){
            System.out.println("____________________________________________________");
            System.out.println("This member does not exist in the database.");
            System.out.println("____________________________________________________");
            return false;
        }
        //Make sure the item exists in the library
        if(getItemById(item.getId()) == null){
            System.out.println("____________________________________________________");
            System.out.println("This item does not exist in the database.");
            System.out.println("____________________________________________________");
            return false;
        }
        return true;
    }

    //Add an item to the library and make sure it has a unique ID
    public boolean addItem(LibraryItem item){
        //Check for null values
        if(item == null || item.getTitle() == null){
            System.out.println("____________________________________________________");
            System.out.println("No part of the item can be empty.");
            System.out.println("____________________________________________________");
            return false;
        }
        //Add to the Database
        return Database.addItem(item);
    }


    //Add a member to the library and make sure it has a unique ID
    public boolean addMember(Member member){
        //Check for null values
        if(member == null || member.getName() == null){
            System.out.println("____________________________________________________");
            System.out.println("No part of the member can be empty.");
            System.out.println("____________________________________________________");
            return false;
        }
        
        //Add to the database
        return Database.addMember(member);
    }


    //Check out an item to a member
    public boolean checkOutItem(Member member, LibraryItem item){
        //Make sure the member and item are not null
        if(!validateMemberAndItem(member, item)){
            return false;
        }
        //Check if the item is available
        if(!item.isAvailable()){
            System.out.println("____________________________________________________");
            System.out.println("This item is not currently available for checkout.");
            System.out.println("____________________________________________________");
            return false;
        }

        //If all pass then checkout the item
        return Database.checkoutItem(member.getId(), item.getId());
    }


    //Return an item from a member
    public boolean returnItem(Member member, LibraryItem item){
        //Make sure the member and item are not null
        if(!validateMemberAndItem(member, item)){
            return false;
        }

        //If all pass then return the item
        return Database.returnItem(member.getId(), item.getId());
    }


    //Remove an item from the library
    public boolean removeItem(LibraryItem item){
        //Make sure the item is not null
        if(item == null || item.getTitle() == null){
            System.out.println("____________________________________________________");
            System.out.println("No part of the item can be empty.");
            System.out.println("____________________________________________________");
            return false;
        }
        //Check if the item exists in the library
        if(Database.getItemById(item.getId()) == null){
            System.out.println("____________________________________________________");
            System.out.println("This item does not exist in the database.");
            System.out.println("____________________________________________________");
            return false;
        }
        //Check if the item is currently checked out
        if(!item.isAvailable()){
            System.out.println("____________________________________________________");
            System.out.println("This item is currently checked out and cannot be removed.");
            System.out.println("____________________________________________________");
            return false;
        }
        //If all pass then remove the item
        return Database.removeItem(item);
    }

    //Remove a member from the library
    public boolean removeMember(Member member){
        //Make sure the member is not null
        if(member == null || member.getName() == null){
            System.out.println("____________________________________________________");
            System.out.println("No part of the member can be empty.");
            System.out.println("____________________________________________________");
            return false;
        }
        //Check if the member exists in the library
        if(Database.getMemberById(member.getId()) == null){
            System.out.println("____________________________________________________");
            System.out.println("This member does not exist in the database.");
            System.out.println("____________________________________________________");
            return false;
        }
        //Check if the member has any items checked out
        if(Database.hasCheckedOutItems(member.getId())){
            System.out.println("____________________________________________________");
            System.out.println("This member has items checked out and cannot be removed.");
            System.out.println("____________________________________________________");
            return false;
        }
        //If all pass then remove the member
        return Database.removeMember(member);
    }

    //Search for an item by title
    public ArrayList<LibraryItem> searchItemByTitle(String title){
        ArrayList<LibraryItem> result = new ArrayList<LibraryItem>();

        if(title == null || title.trim().isEmpty()){
            System.out.println("____________________________________________________");
            System.out.println("Title cannot be empty.");
            System.out.println("____________________________________________________");
            return result;
        }
        //Search the database for items with the title
        return Database.searchItemsByTitle(title);
    }
}