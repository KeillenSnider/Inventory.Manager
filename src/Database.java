//Link the Database class to the SQLite JDBC driver
import java.sql.Connection;
//Create a connection to the database
import java.sql.DriverManager;
//The error that is thrown when there is a problem with the database
import java.sql.SQLException;
//What sends the commands to the database
import java.sql.Statement;
//Impot the prepared statement to prevent SQL injection
import java.sql.PreparedStatement;
//Import the result set to get the results of a query
import java.sql.ResultSet;
//Import the ArrayList to store the results of a query
import java.util.ArrayList;
public class Database {

    //Create the main connection to the database and turn foriegn keys on
    public static Connection getConnection() throws SQLException{
    Connection connection = DriverManager.getConnection("jdbc:sqlite:library.db");
        //So it closes the statement after it is done
        try(Statement statement = connection.createStatement()){
            //Enforce foreign keys
            statement.executeUpdate("PRAGMA foreign_keys = ON");
        }
        return connection;
    }
    
    //Create the Tables
    public static void createTables(){
        //Create a connection to the Database
        //Create a statement to send commands to the database
        //Put them in the try so that they will auto close when the try is done or an error happens
        try(Connection connection = getConnection(); Statement statement = connection.createStatement()){
            //Create the Items Table
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS items(id INTEGER PRIMARY KEY, title TEXT NOT NULL, availability INTEGER NOT NULL, type TEXT NOT NULL, author TEXT, director TEXT)");
            //Create members Table
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS members(id INTEGER PRIMARY KEY, name TEXT NOT NULL)");
            //Create checkouts Table and delete the rows that are connected to the foriegn keys if they are deleted from the other tables
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS checkouts(id INTEGER PRIMARY KEY, itemId INTEGER NOT NULL, memberId INTEGER NOT NULL, checkoutDate TEXT NOT NULL, returnDate TEXT, FOREIGN KEY(itemId) REFERENCES items(id) ON DELETE CASCADE, FOREIGN KEY (memberId) REFERENCES members(id) ON DELETE CASCADE)");
            //Make a unique index on the checkouts table so that a member can only have one copy of an item checked out at a time
            statement.executeUpdate("CREATE UNIQUE INDEX IF NOT EXISTS oneOpenCheckout ON checkouts(itemId) WHERE returnDate IS NULL");
        } catch (SQLException e) {
            //Print a message and the error that happened
            System.out.println("____________________________________________________");
            System.out.println("Error creating tables: " + e.getMessage());
            System.out.println("____________________________________________________");
        }
    }

    //Add a member to the database
    public static boolean addMember(Member member){
        try(Connection connection = getConnection(); PreparedStatement ps = connection.prepareStatement("INSERT INTO members(id,name) VALUES(?,?)")){
            //Fill in the data for the statement so it is plain text and not SQL code
            ps.setInt(1, member.getId());
            ps.setString(2, member.getName());
            //Update the database with the new member
            ps.executeUpdate();
            return true;
        } catch (SQLException e){
            System.out.println("____________________________________________________");
            System.out.println("Error adding member: " + e.getMessage());
            System.out.println("____________________________________________________");
            return false;
        }
    }

    //Return a member by ID
    public static Member getMemberById(int id){
        try(Connection connection = getConnection(); PreparedStatement ps = connection.prepareStatement("SELECT * FROM members WHERE id = ?")){
            //Fill in the missing data
            ps.setInt(1, id);
            //Execute the query and get the result set
            ResultSet rs = ps.executeQuery();
            //If there is a result, create a new member and return it
            if(rs.next()){
                return new Member(id, rs.getString("name"));
            } else {
                //If there is no member with that ID, return null
                return null;
            }
        } catch (SQLException e) {
            System.out.println("____________________________________________________");
            System.out.println("Error getting member by ID: " + e.getMessage());
            System.out.println("____________________________________________________");
            return null;
        }
    }

    //Remove a member from the database
    public static boolean removeMember(Member member){
        try(Connection connection = getConnection(); PreparedStatement ps = connection.prepareStatement("DELETE FROM members WHERE id = ?")){
            //Fill in the missing data
            ps.setInt(1, member.getId());
            //Update the database to remove the member
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("____________________________________________________");
            System.out.println("Error removing member: " + e.getMessage());
            System.out.println("____________________________________________________");
            return false;
        }
    }

    //See if a member still has items that are checkedout
    public static boolean hasCheckedOutItems(int memberId){
        try(Connection connection = getConnection(); PreparedStatement ps = connection.prepareStatement("SELECT * FROM checkouts WHERE memberId = ? AND returnDate IS NULL")){
            //Fill in the missing data
            ps.setInt(1, memberId);
            //Execute the query and get the result set
            ResultSet rs = ps.executeQuery();
            //If there is a result, return true
            return rs.next();
        } catch (SQLException e) {
            System.out.println("____________________________________________________");
            System.out.println("Error checking if member has checked out items: " + e.getMessage());
            System.out.println("____________________________________________________");
            //Reson for true it to take the safe answer and say they have items just incase
            return true;
        }
    }




    //Add an item to the database
    public static boolean addItem(LibraryItem item){
        try(Connection connection = getConnection(); PreparedStatement ps = connection.prepareStatement("INSERT INTO items(id,title,availability,type,author,director) VALUES(?,?,?,?,?,?)")){
            //Fill in the data for the statement so it is plain text and not SQL code
            ps.setInt(1, item.getId());
            ps.setString(2, item.getTitle());
            //Set availability to 1 for true and 0 for false
            ps.setInt(3, item.isAvailable() ? 1 : 0);
            //Set the type to Book or DVD which ever matches the item
            ps.setString(4, item instanceof Book ? "Book" : "DVD");
            //Set the author or director depending on the type of item and null the other one so that it is not empty in the database
            if(item instanceof Book){
                ps.setString(5, ((Book)item).getAuthor());
                ps.setNull(6, java.sql.Types.VARCHAR);
            } else{
                ps.setNull(5, java.sql.Types.VARCHAR);
                ps.setString(6, ((DVD)item).getDirector());
            }
            //Update the database with the new item
            ps.executeUpdate();
            return true;
        } catch (SQLException e){
            System.out.println("____________________________________________________");
            System.out.println("Error adding item: " + e.getMessage());
            System.out.println("____________________________________________________");
            return false;
        }
    }

    //Get an Item by Id
    public static LibraryItem getItemById(int id){
        try(Connection connection = getConnection(); PreparedStatement ps = connection.prepareStatement("SELECT * FROM items WHERE id = ?")){
            //Fill in the missing data
            ps.setInt(1, id);
            //Execute the query and get the result set
            ResultSet rs = ps.executeQuery();
            //If there is a result, create a new item and return it
            if(rs.next()){
                String title = rs.getString("title");
                boolean available = rs.getInt("availability") == 1;
                String type = rs.getString("type");
                if(type.equals("Book")){
                    String author = rs.getString("author");
                    return new Book(id, title, author, available);
                } else {
                    String director = rs.getString("director");
                    return new DVD(id, title, director, available);
                }
            } else {
                //If there is no item with that ID, return null
                return null;
            }
        } catch (SQLException e) {
            System.out.println("____________________________________________________");
            System.out.println("Error getting item by ID: " + e.getMessage());
            System.out.println("____________________________________________________");
            return null;
        }
    }

    //Remove an item from the database
    public static boolean removeItem(LibraryItem item){
        try(Connection connection = getConnection(); PreparedStatement ps = connection.prepareStatement("DELETE FROM items WHERE id = ?")){
            //Fill in the missing data
            ps.setInt(1, item.getId());
            //Update the database to remove the item
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("____________________________________________________");
            System.out.println("Error removing item: " + e.getMessage());
            System.out.println("____________________________________________________");
            return false;
        }
    }




    //Search for items in the database by title
    public static ArrayList<LibraryItem> searchItemsByTitle(String title){
        ArrayList<LibraryItem> result = new ArrayList<LibraryItem>();
        try(Connection connection = getConnection(); PreparedStatement ps = connection.prepareStatement("SELECT * FROM items WHERE title LIKE ?")){
            //Fill in the missing data with wildcards for partial matches
            ps.setString(1, "%" + title + "%");
            //Execute the query and get the result set
            ResultSet rs = ps.executeQuery();
            //Loop through the results and create new items to add to the result list
            while(rs.next()){
                int id = rs.getInt("id");
                String itemTitle = rs.getString("title");
                boolean available = rs.getInt("availability") == 1;
                String type = rs.getString("type");
                if(type.equals("Book")){
                    String author = rs.getString("author");
                    result.add(new Book(id, itemTitle, author, available));
                } else {
                    String director = rs.getString("director");
                    result.add(new DVD(id, itemTitle, director, available));
                }
            }
        } catch (SQLException e) {
            System.out.println("____________________________________________________");
            System.out.println("Error searching items by title: " + e.getMessage());
            System.out.println("____________________________________________________");
        }
        return result;
    }


    //Checkout an item for a member and add it to the members borrowed items in the checkout table
    public static boolean checkoutItem(int memberId, int itemId){
        //Get a connection to the database then do both actions at once so if one fails the other will not happen
        try(Connection main = getConnection()){
            //Do this because it will not commit the changes to the database until we tell it to do so, this way if one fails the other will not happen
            main.setAutoCommit(false);
            try(PreparedStatement one = main.prepareStatement("UPDATE items SET availability = 0 WHERE id = ? AND availability = 1"); PreparedStatement two = main.prepareStatement("INSERT INTO checkouts(itemId, memberId, checkoutDate) VALUES(?, ?, ?)")){
                //Fill in the missing data
                one.setInt(1, itemId);
                two.setInt(1, itemId);
                two.setInt(2, memberId);
                //Give the day of checkout as the current date
                two.setString(3, java.time.LocalDate.now().toString());
                //Execute the updates and make sure that it changes something
                if(one.executeUpdate() == 0){
                    //If the item was not available then rollback the changes and return false
                    main.rollback();
                    System.out.println("____________________________________________________");
                    System.out.println("This item is not available for checkout.");
                    System.out.println("____________________________________________________");
                    return false;
                }
                two.executeUpdate();
                //Commit the changes to the database
                main.commit();
                return true;
            } catch(SQLException e){
                //If there is an error rollback the changes so that nothing happens
                main.rollback();
                System.out.println("____________________________________________________");
                System.out.println("Error checking out item: " + e.getMessage());
                System.out.println("____________________________________________________");
                return false;
            }
        } catch(SQLException e){
            System.out.println("____________________________________________________");
            System.out.println("Error checking out item: " + e.getMessage());
            System.out.println("____________________________________________________");
            return false;
        }
    }


    //Return an item
    public static boolean returnItem(int memberId, int itemId){
        //Get a connection to the database then do both actions at once so if one fails the other will not happen
        try(Connection main = getConnection()){
            //Do this because it will not commit the changes to the database until we tell it to do so, this way if one fails the other will not happen
            main.setAutoCommit(false);
            try(PreparedStatement one = main.prepareStatement("UPDATE items SET availability = 1 WHERE id = ? AND availability = 0"); PreparedStatement two = main.prepareStatement("UPDATE checkouts SET returnDate = ? WHERE itemId = ? AND memberId = ? AND returnDate IS NULL")){
                //Fill in the missing data
                one.setInt(1, itemId);
                two.setString(1, java.time.LocalDate.now().toString());
                two.setInt(2, itemId);
                two.setInt(3, memberId);
                //Execute the updates and make sure that it changes something
                if(one.executeUpdate() == 0 || two.executeUpdate() == 0){
                    //If the item was not checked out then rollback the changes and return false
                    main.rollback();
                    System.out.println("____________________________________________________");
                    System.out.println("This item is not currently checked out by this member.");
                    System.out.println("____________________________________________________");
                    return false;
                }
                //Commit the changes to the database
                main.commit();
                return true;
            } catch(SQLException e){
                //If there is an error rollback the changes so that nothing happens
                main.rollback();
                System.out.println("____________________________________________________");
                System.out.println("Error returning item: " + e.getMessage());
                System.out.println("____________________________________________________");
                return false;
            }
        } catch(SQLException e){
            System.out.println("____________________________________________________");
            System.out.println("Error returning item: " + e.getMessage());
            System.out.println("____________________________________________________");
            return false;
        }
    }



    //Find all overdue items in the database also prints the member that is late and by how many days they are late
    public static ArrayList<String> findOverdueItems(){
        ArrayList<String> result = new ArrayList<String>();
        try(Connection connection = getConnection(); PreparedStatement ps = connection.prepareStatement("SELECT items.id, items.title, items.type, checkouts.checkoutDate, members.name FROM checkouts JOIN items ON checkouts.itemId = items.id JOIN members ON checkouts.memberId = members.id WHERE checkouts.returnDate IS NULL")){
            //Execute the query and get the result set
            ResultSet rs = ps.executeQuery();
            //Loop through the results and create new items to add to the result list
            while(rs.next()){
                int id = rs.getInt("id");
                String title = rs.getString("title");
                String type = rs.getString("type");
                String checkoutDate = rs.getString("checkoutDate");
                String memberName = rs.getString("name");
                //Build a throwaway item just to ask it for its real loan period, so this stays the single source of truth
                LibraryItem tempItem = type.equals("Book")
                    ? new Book(id, title, null, true)
                    : new DVD(id, title, null, true);
                int loanPeriodDays = tempItem.getLoanPeriodDays();
                //Calculate how many days overdue the item is
                java.time.LocalDate dueDate = java.time.LocalDate.parse(checkoutDate).plusDays(loanPeriodDays);
                java.time.LocalDate currentDate = java.time.LocalDate.now();
                if(currentDate.isAfter(dueDate)){
                    long daysOverdue = java.time.temporal.ChronoUnit.DAYS.between(dueDate, currentDate);
                    result.add(type + " ID: " + id + ", Title: " + title + ", Checked out by: " + memberName + ", Days overdue: " + daysOverdue);
                }
            }
        } catch (SQLException e) {
            System.out.println("____________________________________________________");
            System.out.println("Error finding overdue items: " + e.getMessage());
            System.out.println("____________________________________________________");
        }
        return result;
    }
    


}
