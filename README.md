# Inventory.Manager

A console-based Library Management System built in Java. It tracks books, DVDs, and members, handles checkouts and returns, and stores everything in a SQLite database so nothing is lost between runs.

I built this mainly to practice and actually understand OOP, things like polymorphism and encapsulation, not just be able to define them. Then partway through I extended it to also practice moving a project from in-memory Java objects (ArrayLists) to a real persistent database, and to get more comfortable with JDBC, SQL, and designing a schema instead of just using Flask/SQLite from the Python side like I'm used to.

## Built With

- **Java** (JDK)
- **SQLite**, via the `sqlite-jdbc` driver (jar in `/lib`)
- **VS Code** with the Java extension pack

## How It's Structured

- `LibraryItem` — abstract base class for anything the library owns. Holds `id`, `title`, `availability`, and a default 14-day loan period.
- `Book` and `DVD` — extend `LibraryItem`. Books get a 21-day loan period and an author, DVDs get a 7-day loan period and a director.
- `Member` — just `id` and `name`. It used to carry an ArrayList of borrowed items, but once checkouts moved into the database, that list was always empty and misleading, so I deleted it. Now `Member.toString()` asks the database directly whether the member has anything checked out.
- `Library` — the public-facing class `Main` talks to. Every method it exposes just validates input and passes the real work to `Database`.
- `Database` — every SQL statement in the project lives here. Nothing outside this class ever touches SQL directly.
- `Main` — the console menu.

## Why I Built the OOP Side the Way I Did

**Polymorphism with `LibraryItem`.**
`Book` and `DVD` both extend the abstract `LibraryItem` class and override `getLoanPeriodDays()` to return their own values (21 for a Book, 7 for a DVD). Anywhere in the code that has a `LibraryItem`, like in `Library` or when calculating a due date, it just calls `.getLoanPeriodDays()` and doesn't need to know or care whether it's actually holding a Book or a DVD. The right version runs automatically. This is also why due dates could stay calculated instead of stored, the loan period logic already lives in one place per type, and I could reuse it wherever I needed it.

**Encapsulation.**
Every field in `LibraryItem`, `Book`, `DVD`, and `Member` is private, with getters (and only the setters that are actually needed) controlling access. `setAvailability()` on `LibraryItem` is a good example, it's only ever called from one place in the whole project, when a checkout or return actually happens, instead of letting any code reach in and flip an item's availability directly. Keeping that to a single entry point is what made it possible to trust that `availability` in the database always matches what actually happened.

## Why I Built the Database the Way I Did

**One `items` table instead of separate `books` and `dvds` tables.**
Books and DVDs share most of their fields (id, title, availability), so keeping them in one table means I don't have to write two versions of every query, like search or getById. The table has a `type` column (`"Book"` or `"DVD"`) plus `author` and `director` columns, and whichever one doesn't apply gets stored as `NULL`.

**`checkouts` as its own table instead of a column on `items`.**
An item can be checked out, returned, and checked out again many times over its life, and I wanted the full history, not just the current status. So `checkouts` has its own auto-incrementing id, a foreign key to the item, a foreign key to the member, a `checkoutDate`, and a `returnDate` that stays `NULL` until the item comes back. A `NULL` `returnDate` is what "still checked out" means in this project.

**Due dates are calculated, not stored.**
I don't save a `dueDate` column anywhere. Instead it's always `checkoutDate` + that item's `getLoanPeriodDays()`. I did this on purpose, it's the same "single source of truth" rule I used for `availability`: only one place in the code decides what a Book's loan period is, and everything else asks that place instead of hardcoding the number again somewhere else.

**`PreparedStatement` everywhere, never building SQL strings by hand.**
Every value that comes from user input goes in through a `?` placeholder and `setInt`/`setString`, never concatenated into the SQL text. This is the same thing Flask's `cursor.execute(sql, params)` does under the hood, Java just makes you write both steps yourself. I actually tested this against a fake SQL injection string as one of my member names, and it just got stored as plain text instead of breaking the table.

**Checkout and return use real transactions.**
Both of those actions have to change two tables at once, the item's availability and a row in `checkouts`. If only one of those succeeded, the database would be in a state that doesn't make sense: an item marked unavailable with no record of who has it, or a checkout logged for an item that still shows as available. So both of those methods turn `autoCommit` off, run both updates, and either `commit()` if both worked or `rollback()` if anything failed, so it's always all-or-nothing.

**Foreign keys with `ON DELETE CASCADE`.**
SQLite doesn't enforce foreign keys unless you turn them on per connection, so every connection in this project runs `PRAGMA foreign_keys = ON`. I set both foreign keys in `checkouts` to cascade, so removing a member or an item also removes their checkout history. I thought about blocking deletion instead so history never disappears, but decided that if you delete a member or an item, their checkout records aren't useful to keep around either.

**A partial unique index so an item can't be checked out twice at once.**
My checkout logic already prevents double-checkouts (`UPDATE ... WHERE availability = 1`), but I added a unique index on `checkouts(itemId) WHERE returnDate IS NULL` so the database itself guarantees it too, not just my application code. Old, returned checkouts for the same item don't count against this, only ever the current one.

## Features

- Add / remove books and DVDs
- Add / remove members (can't remove a member who still has something checked out)
- Check out and return items, tracked with real dates
- Search items by title (partial match)
- See every item that's currently overdue, who has it, and by how many days

## How to Run It

1. Clone the repo.
2. Open the project folder in VS Code with the Java extension pack installed.
3. Make sure `lib/sqlite-jdbc-3.53.4.0.jar` is on the classpath (it already is in the VS Code config in this repo).
4. Run `Main.java`.
5. `library.db` will be created automatically in the project folder the first time you run it. (It's gitignored, so everyone who clones this starts with a clean database.)
6. Follow the menu.

## What's Not in the Repo

`library.db` and compiled `.class` files are gitignored on purpose, so the database always starts empty for anyone running the project fresh.
