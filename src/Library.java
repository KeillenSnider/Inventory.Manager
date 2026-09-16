import java.util.ArrayList;
public class Library {
    
    //Variables
    private ArrayList<Book> books;
    private ArrayList<Member> members;

    //Constructor
    public Library(){
        this.books = new ArrayList<Book>();
        this.members = new ArrayList<Member>();
    }


    //Methods

    //Get a book by the ID
    public Book getBookById(int id){
        for(Book b : books){
            if(b.getId() == id){
                return b;
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


    //Validate the Book and member
    private boolean validateMemberAndBook(Member member, Book book){
        //Make sure the member and book are not null
        if(member == null || member.getName() == null || book == null || book.getTitle() == null || book.getAuthor() == null){
            System.out.println("No part of the member or book can be empty.");
            return false;
        }
        //Make sure the member exists in the library
        if(!members.contains(member)){
            System.out.println("This member does not exist in the database.");
            return false;
        }
        //Make sure the book exists in the library
        if(!books.contains(book)){
            System.out.println("This book does not exist in the database.");
            return false;
        }
        return true;
    }

    //Add a book to the library and make sure it has a unique ID
    public boolean addBook(Book book){
        //Check for null values
        if(book == null || book.getTitle() == null || book.getAuthor() == null){
            System.out.println("No part of the book can be empty.");
            return false;
        }
        //Loop to see if Id is unique
        for(Book b : books){
            if(b.getId() == book.getId()){
                System.out.println("Book ID already exists. Please use a unique ID.");
                return false;
            }
        }
        //Add to the List
        books.add(book);
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


    //Check out a book to a member
    public boolean checkOutBook(Member member, Book book){
        //Make sure the member and book are not null
        if(!validateMemberAndBook(member, book)){
            return false;
        }
        //Check if the member has already checked out the book
        if(member.getBorrowedBooks().contains(book)){
            System.out.println("This member has already checked out this book.");
            return false;
        }
        //Check if the book is available
        if(!book.isAvailability()){
            System.out.println("This book is not currently available for checkout.");
            return false;
        }

        //If all pass then checkout the book
        member.addBorrowedBook(book);
        return true;
    }


    //Return book from a member
    public boolean returnBook(Member member, Book book){
        //Make sure the member and book are not null
        if(!validateMemberAndBook(member, book)){
            return false;
        }
        //Check if the member has already checkoout the book
        if(!member.getBorrowedBooks().contains(book)){
            System.out.println("This member has not checked out this book.");
            return false;
        }

        //If all pass then return the book
        member.removeBorrowedBook(book);
        return true;
    }


    //Remove a book from the library
    public boolean removeBook(Book book){
        //Make sure the book is not null
        if(book == null || book.getTitle() == null || book.getAuthor() == null){
            System.out.println("No part of the book can be empty.");
            return false;
        }
        //Check if the book exists in the library
        if(!books.contains(book)){
            System.out.println("This book does not exist in the database.");
            return false;
        }
        //Check if the book is currently checked out
        if(!book.isAvailability()){
            System.out.println("This book is currently checked out and cannot be removed.");
            return false;
        }
        //If all pass then remove the book
        books.remove(book);
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
        //Check if the member has any books checked out
        if(!member.getBorrowedBooks().isEmpty()){
            System.out.println("This member has books checked out and cannot be removed.");
            return false;
        }
        //If all pass then remove the member
        members.remove(member);
        return true;
    }

    //Search for a book by title
    public ArrayList<Book> searchBookByTitle(String title){
        ArrayList<Book> result = new ArrayList<Book>();

        if(title == null || title.trim().isEmpty()){
            System.out.println("Title cannot be empty.");
            return result;
        }
        for(Book b : books){
            if(b.getTitle().toLowerCase().contains(title.toLowerCase())){
                result.add(b);
            }
        }
        return result;
    }
}