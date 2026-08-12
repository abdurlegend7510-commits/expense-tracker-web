# Ledger — Expense & Inventory Tracker (Web Version)

A Spring Boot + vanilla HTML/JS rebuild of the Swing desktop app. The
model layer (`Expense`, `FixedExpense`, `VariableExpense`, `ExpenseManager`,
`InventoryItem`, `InventoryManager`) is unchanged in logic from the
Swing version — only the GUI layer (`MainGUI.java`) has been replaced
with a REST API + browser frontend.

## Requirements
- JDK 17 or newer
- Maven (or use the Maven wrapper if you add one via `mvn -N wrapper:wrapper`)
- Internet access the first time you build (Maven needs to download
  Spring Boot dependencies from Maven Central)

## Run it

```
cd webapp
mvn spring-boot:run
```

Then open **http://localhost:8080** in a browser.

## Project layout

```
webapp/
  pom.xml
  src/main/java/com/expensetracker/
    ExpenseTrackerApplication.java   entry point
    model/                           Expense, FixedExpense, VariableExpense,
                                      InventoryItem, exceptions
    service/                         ExpenseManager, InventoryManager
                                      (@Service singletons, same CRUD/search/
                                      summary/file logic as the Swing version)
    web/                             REST controllers + DTOs + global
                                      exception handler
  src/main/resources/
    application.properties           port config (default 8080)
    static/                          index.html, style.css, app.js
                                      (served automatically by Spring Boot)
    data/                            expenses.txt / inventory.txt get
                                      created here at runtime
```

## API endpoints

| Method | Path                  | Purpose                          |
|--------|-----------------------|-----------------------------------|
| GET    | /api/expenses          | list all, or `?keyword=` to search |
| POST   | /api/expenses          | add a Fixed or Variable expense  |
| PUT    | /api/expenses/{id}     | update amount/description        |
| DELETE | /api/expenses/{id}     | delete                           |
| GET    | /api/inventory          | list all, or `?keyword=` to search |
| POST   | /api/inventory          | add an item                      |
| PUT    | /api/inventory/{id}     | update quantity/price            |
| DELETE | /api/inventory/{id}     | delete                           |
| GET    | /api/summary             | totals for the Summary tab       |

## Notes / next steps

- **Not compiled in this environment** — my sandbox can't reach Maven
  Central, so this was written and reviewed carefully (brace/paren
  balance checked, method signatures cross-checked by hand) but not
  actually built. Run `mvn compile` first thing and fix anything that
  comes up — flag it back to me and I'll patch it.
- **Storage is still flat files** (`data/expenses.txt`, `data/inventory.txt`),
  same as the desktop version. Fine for a single-user demo; swap for a
  real database (e.g. SQLite via Spring Data JPA) before treating this
  as multi-user or production.
- **No auth** — anyone who can reach the URL can read/write everything.
  Fine for local/personal use, not fine for a public deployment as-is.
- To deploy publicly: push to GitHub, then connect the repo on
  Render.com or Railway.app — both auto-detect a Spring Boot project.
