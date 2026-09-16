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
            System.out.println("1. Add a book");
            System.out.println("2. Remove a book");
            System.out.println("3. Add a member");
            System.out.println("4. Remove a member");
            System.out.println("5. Checkout a book");
            System.out.println("6. Return a book");
            System.out.println("7. Search for a book");
            System.out.println("8. Exit");

            int choice = getValidInt(scanner);
            // Make sure to clear the scanner buffer after reading an int
            scanner.nextLine();

            if(choice == 1){
                System.out.println("Enter book ID:");
                int bookId = getValidInt(scanner);
                scanner.nextLine();
                System.out.println("Enter book title:");
                String bookTitle = scanner.nextLine();
                System.out.println("Enter book author:");
                String bookAuthor = scanner.nextLine();
                Book newBook = new Book(bookId, bookTitle, bookAuthor, true);
                if(library.addBook(newBook)){
                    System.out.println("____________________________________________________");
                    System.out.println(newBook.getTitle() + " Book added to Library.");
                    System.out.println("____________________________________________________");
                }
            }
            else if(choice == 2){
                System.out.println("Enter book ID to remove:");
                int bookId = getValidInt(scanner);
                scanner.nextLine();
                Book book = library.getBookById(bookId);
                if(book != null){
                    if(library.removeBook(book)){
                        System.out.println("____________________________________________________");
                        System.out.println(book + " Book removed from Library.");
                        System.out.println("____________________________________________________");
                    }
                } else {
                    System.out.println("____________________________________________________");
                    System.out.println("Book not found.");
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
                System.out.println("Enter book ID to checkout:");
                int bookId = getValidInt(scanner);
                scanner.nextLine();
                Member member = library.getMemberById(memberId);
                Book book = library.getBookById(bookId);
                if(member != null && book != null){
                    if(library.checkOutBook(member, book)){
                        System.out.println("____________________________________________________");
                        System.out.println(member.getName() + " has checked out " + book.getTitle());
                        System.out.println("____________________________________________________");
                    }
                } else {
                    System.out.println("____________________________________________________");
                    System.out.println("Member or book not found.");
                    System.out.println("____________________________________________________");
                }
            }
            else if(choice == 6){
                System.out.println("Enter member ID:");
                int memberId = getValidInt(scanner);
                scanner.nextLine();
                System.out.println("Enter book ID to return:");
                int bookId = getValidInt(scanner);
                scanner.nextLine();
                Member member = library.getMemberById(memberId);
                Book book = library.getBookById(bookId);
                if(member != null && book != null){
                    if(library.returnBook(member, book)){
                        System.out.println("____________________________________________________");
                        System.out.println(member.getName() + " has returned " + book.getTitle());
                        System.out.println("____________________________________________________");
                    }
                } else {
                    System.out.println("____________________________________________________");
                    System.out.println("Member or book not found.");
                    System.out.println("____________________________________________________");
                }
            }
            else if(choice == 7){
                System.out.println("Enter book title to search:");
                String bookTitle = scanner.nextLine();
                ArrayList<Book> bookList = library.searchBookByTitle(bookTitle);
                if(!bookList.isEmpty()){
                    System.out.println("____________________________________________________");
                    for(Book book : bookList){
                        System.out.println(book);
                    }
                    System.out.println("____________________________________________________");
                } else {
                    System.out.println("____________________________________________________");
                    System.out.println("Book not found.");
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