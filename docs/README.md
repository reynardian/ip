# Voyager User Guide

Voyager is a **desktop task management application** optimized for users who prefer a Command Line Interface (CLI) but appreciate a clean Graphical User Interface (GUI). If you can type fast, Voyager can help you manage your daily tasks much faster than traditional "point-and-click" apps.

---

## Quick Start

1. Ensure you have **Java 17** or above installed on your computer.
2. Download the latest `voyager.jar` from our [GitHub Releases](https://github.com/reynardian/ip/releases).
3. Copy the file to the folder you want to use as the home folder for your task manager.
4. Open a command terminal, navigate to that folder, and run the app using the command:  
   `java -jar voyager.jar`
5. The GUI should appear in a few seconds.
6. Type a command in the command box and press Enter to execute it.  
   *Example:* Type `list` and press Enter to see your current tasks.

---

## Features 

### 1. Adding Tasks
Voyager supports three distinct types of tasks to help you stay organized.

* **ToDo:** A basic task without any specific date.  
    *Format:* `todo DESCRIPTION`  
    *Example:* `todo read a book`
* **Deadline:** A task that needs to be finished by a certain date.  
    *Format:* `deadline DESCRIPTION /by YYYY-MM-DD`  
    *Example:* `deadline submit report /by 2026-03-15`
* **Event:** A task with a specific start and end time.  
    *Format:* `event DESCRIPTION /from START /to END`  
    *Example:* `event project meeting /from Mon 2pm /to 4pm`

### 2. Managing the List
Keep track of what is pending and what is finished.

* **List all tasks:** View every task currently in your database.  
    *Format:* `list`
* **Mark as Done:** Change a task status to "completed."  
    *Format:* `mark INDEX`  
    *Example:* `mark 2` (Marks the 2nd task in the list as done)
* **Unmark:** Change a completed task back to "pending."  
    *Format:* `unmark INDEX`
* **Delete:** Permanently remove a task from the list.  
    *Format:* `delete INDEX`

### 3. Finding Tasks
Search for specific tasks using keywords.

* **Find:** Searches for tasks that match the keyword provided.  
    *Format:* `find KEYWORD`  
    *Example:* `find book` (Lists all tasks containing the word "book")

### 4. Advanced Sorting
Organize your tasks automatically for better visibility.

* **Sort by Name:** Rearranges all tasks alphabetically.  
    *Format:* `sort`
* **Sort by Date:** Prioritizes **Deadlines** and sorts them by their due date.  
    *Format:* `sortdate`

---

## Command Summary

| Action | Command Format |
| :--- | :--- |
| **Add Todo** | `todo <description>` |
| **Add Deadline** | `deadline <description> /by <YYYY-MM-DD>` |
| **Add Event** | `event <description> /from <start> /to <end>` |
| **List** | `list` |
| **Mark/Unmark** | `mark <index>` or `unmark <index>` |
| **Delete** | `delete <index>` |
| **Find** | `find <keyword>` |
| **Sort** | `sort` (name) or `sortdate` (date) |
| **Exit** | `bye` |

---

## FAQ

**Q: Where is my data stored?** **A:** Voyager automatically saves your data in a text file located at `./data/voyager.txt`. This file is updated every time you add, delete, or modify a task.

**Q: Can I edit the save file manually?** **A:** While possible, it is not recommended. If the file format becomes corrupted, Voyager may clear the data to prevent app crashes.