import java.util.Scanner;
import java.util.ArrayList;
public class Main {

    //Methods

    //Checks for a valid int until it gets one
    public static int getValidInt(Scanner scanner){
        while(!scanner.hasNextInt()){
            System.out.println("Please enter a valid Number(e.g 1, 2, 3).");
            scanner.next();
        }
        return scanner.nextInt();
    }



    public static void main(String[] args){

        Scanner scanner = new Scanner(System.in);
        Library library = new Library();

        while(true){
            System.out.println("Welcome to the Library Management System");
            System.out.println("1. Add an item");
            System.out.println("2. Remove an item");
            System.out.println("3. Add a member");
            System.out.println("4. Remove a member");
            System.out.println("5. Checkout an item");
            System.out.println("6. Return an item");
            System.out.println("7. Search for an item");
            System.out.println("8. Exit");

            int choice = getValidInt(scanner);
            // Make sure to clear the scanner buffer after reading an int
            scanner.nextLine();

            if(choice == 1){
                System.out.println("What type of item? 1. Book  2. DVD");
                int typeChoice = getValidInt(scanner);
                scanner.nextLine();

                System.out.println("Enter item ID:");
                int itemId = getValidInt(scanner);
                scanner.nextLine();
                System.out.println("Enter item title:");
                String itemTitle = scanner.nextLine();

                LibraryItem newItem = null;

                if(typeChoice == 1){
                    System.out.println("Enter book author:");
                    String bookAuthor = scanner.nextLine();
                    newItem = new Book(itemId, itemTitle, bookAuthor, true);
                } else if(typeChoice == 2){
                    System.out.println("Enter DVD director:");
                    String dvdDirector = scanner.nextLine();
                    newItem = new DVD(itemId, itemTitle, dvdDirector, true);
                } else {
                    System.out.println("____________________________________________________");
                    System.out.println("Invalid item type. Item not added.");
                    System.out.println("____________________________________________________");
                    continue;
                }

                if(library.addItem(newItem)){
                    System.out.println("____________________________________________________");
                    System.out.println(newItem.getTitle() + " added to Library.");
                    System.out.println("____________________________________________________");
                }
            }
            else if(choice == 2){
                System.out.println("Enter item ID to remove:");
                int itemId = getValidInt(scanner);
                scanner.nextLine();
                LibraryItem item = library.getItemById(itemId);
                if(item != null){
                    if(library.removeItem(item)){
                        System.out.println("____________________________________________________");
                        System.out.println(item + " removed from Library.");
                        System.out.println("____________________________________________________");
                    }
                } else {
                    System.out.println("____________________________________________________");
                    System.out.println("Item not found.");
                    System.out.println("____________________________________________________");
                }
            }
            else if(choice == 3){
                System.out.println("Enter member ID:");
                int memberId = getValidInt(scanner);
                scanner.nextLine();
                System.out.println("Enter member name:");
                String memberName = scanner.nextLine();
                Member newMember = new Member(memberId, memberName);
                if(library.addMember(newMember)){
                    System.out.println("____________________________________________________");
                    System.out.println(newMember.getName() + " Member added to Library.");
                    System.out.println("____________________________________________________");
                }
            }
            else if(choice == 4){
                System.out.println("Enter member ID to remove:");
                int memberId = getValidInt(scanner);
                scanner.nextLine();
                Member member = library.getMemberById(memberId);
                if(member != null){
                    if(library.removeMember(member)){
                        System.out.println("____________________________________________________");
                        System.out.println(member.getName() + " Member removed from Library.");
                        System.out.println("____________________________________________________");
                    }
                } else {
                    System.out.println("____________________________________________________");
                    System.out.println("Member not found.");
                    System.out.println("____________________________________________________");
                }
            }
            else if(choice == 5){
                System.out.println("Enter member ID:");
                int memberId = getValidInt(scanner);
                scanner.nextLine();
                System.out.println("Enter item ID to checkout:");
                int itemId = getValidInt(scanner);
                scanner.nextLine();
                Member member = library.getMemberById(memberId);
                LibraryItem item = library.getItemById(itemId);
                if(member != null && item != null){
                    if(library.checkOutItem(member, item)){
                        System.out.println("____________________________________________________");
                        System.out.println(member.getName() + " has checked out " + item.getTitle());
                        System.out.println("____________________________________________________");
                    }
                } else {
                    System.out.println("____________________________________________________");
                    System.out.println("Member or item not found.");
                    System.out.println("____________________________________________________");
                }
            }
            else if(choice == 6){
                System.out.println("Enter member ID:");
                int memberId = getValidInt(scanner);
                scanner.nextLine();
                System.out.println("Enter item ID to return:");
                int itemId = getValidInt(scanner);
                scanner.nextLine();
                Member member = library.getMemberById(memberId);
                LibraryItem item = library.getItemById(itemId);
                if(member != null && item != null){
                    if(library.returnItem(member, item)){
                        System.out.println("____________________________________________________");
                        System.out.println(member.getName() + " has returned " + item.getTitle());
                        System.out.println("____________________________________________________");
                    }
                } else {
                    System.out.println("____________________________________________________");
                    System.out.println("Member or item not found.");
                    System.out.println("____________________________________________________");
                }
            }
            else if(choice == 7){
                System.out.println("Enter item title to search:");
                String itemTitle = scanner.nextLine();
                ArrayList<LibraryItem> itemList = library.searchItemByTitle(itemTitle);
                if(!itemList.isEmpty()){
                    System.out.println("____________________________________________________");
                    for(LibraryItem item : itemList){
                        System.out.println(item);
                    }
                    System.out.println("____________________________________________________");
                } else {
                    System.out.println("____________________________________________________");
                    System.out.println("Item not found.");
                    System.out.println("____________________________________________________");
                }
            }
            else if(choice == 8){
                System.out.println("____________________________________________________");
                System.out.println("Exiting the system. Goodbye!");
                break;
            }
            else{
                System.out.println("____________________________________________________");
                System.out.println("Invalid choice. Please try again.");
                System.out.println("____________________________________________________");
            }
        }
    }
}