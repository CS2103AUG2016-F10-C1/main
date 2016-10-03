# User Guide

* [Quick Start](#quick-start)
* [Features](#features)
* [FAQ](#faq)
* [Command Summary](#command-summary)

## Quick Start

0. Ensure you have Java version `1.8.0_60` or later installed in your Computer.<br>
   > Having any Java 8 version is not enough. <br>
   This app will not work with earlier versions of Java 8.
   
   Click [here](http://www.oracle.com/technetwork/java/javase/downloads/jre8-downloads-2133155.html) to download the latest Java version.
   
1. Download the latest `tars.jar` from the 'releases' tab. [*No releases available yet*]
2. Copy the file to the folder you want to use as the home folder for your TARS App.
3. Double-click the file to start the app. The GUI should appear in a few seconds. 
   > *Image not available yet*

4. Type the command in the command box and press <kbd>Enter</kbd> to execute it. <br>
   e.g. typing **`help`** and pressing <kbd>Enter</kbd> will open the help window. 
5. Some example commands you can try:
   * **`ls`** : lists all tasks
   * **`add`**` add -n Complete CS2103 Quiz 3 -dt 23/09/2016 -p H, -t Quiz, CS2103` : 
     adds a task `Complete CS2103 Quiz 3` to the TARS list.
   * **`del`**` 3` : deletes the 3rd task shown in the TARS list
   * **`exit`** : exits the app
6. Refer to the [Features](#features) section below for details of each command.<br>


## Features

#### Displaying a list of available commands : `help`
Displays a list of available commands with their individual usage information <br>
Format: `help`

> Help is also shown if you enter an incorrect command e.g. `abcd`
 
#### Adding a task : `add`
Adds a task to the To-Do List<br>
Format: `add -n <TASK> -dt <START_DATE/TIME> to <END_DATE/TIME> -p <PRIORITY> -t <TAG>[, <TAG>, <TAG>,...] -r <NUM_TIMES> <FREQUENCY>` 
 
> Words in `UPPER_CASE` are the parameters. Other than `<TASK>`, all parameters are optional. 
> Order of parameters are fixed.
>
> Time is in a 24 hour format (e.g. `1330`) 
> 
> Priority options are: `High` or `H`, `Medium` or `M`, `Low` or `L`

Examples: 
* `add -n Meet John Doe -dt 26/09/2016 0900 to 1030 -t CATCH UP`
* `add -n Complete CS2103 Quiz -dt 23/09/2016 -p H, -t Quiz, CS2103, -r 13 EVERY WEEK`

#### Reserving timeslots for a task : `rsv`
Reserves one or more timeslot for a task<br>
Format: `-n <TASK> -dt <START_DATE/TIME> to <END_DATE/TIME> [, <START_DATE/TIME> to <END_DATE/TIME>, …]`

> Words in <UPPER_CASE> are the parameters. 
>
> Time is in a 24-hr format. More than one date/time can be added.

Examples:
* `rsv -n Meet John Doe -dt 26/09/2016 0900 to 1030, 28/09/2016 1000 to 1130`

#### Editing a reserved timeslot : `rsv -e`
Renames a task with reserved time slots or Adds/Deletes a reserved timeslot for a task <br>
Format: `rsv -e <INDEX> -n <TASK> -dta <START_DATE/TIME> to <END_DATE/TIME> -dtr <START_DATE/TIME> to <END_DATE/TIME>`

> Words in <UPPER_CASE> are the parameters. 
> 
> Time is in a 24-hr format. More than one date/time can be edited.
>
> Use -n to rename the task <br>
> Use -dta to add a timeslot <br>
> Use -dtr to remove a timeslot <br>

Examples:
* `rsv -e 2 -n Meet John Tan -dta 08/10/2016 1000 to 1200`
* `rsv -e 1 -n -dta 09/10/2016 1100 to 1230 -dtr 05/10/2016 1000 to 1200`

#### Deleting a task with reserved timeslots : `rsv -d`
Deletes a task with all its reserved time slots <br>
Format: `rsv -d <INDEX>`

> Deletes the task at the specific `<INDEX>`. 
> The index refers to the index number shown in the task list.
> The index **must be a positive integer** 1, 2, 3, ...

Examples:
* `rsv -d 5`

#### Confirming a reserved timeslot : `confirm`
Confirms a reserved timeslot for a particular tasks and removed all the other reserved time slots. <br>
Format: `confirm <INDEX_TASK> <INDEX_TIMESLOT>`

> Confirm the task of a specific `<INDEX_TASK>` at a timeslot of a specific `<INDEX_TIMESLOT>` <br>
> The `<INDEX_TASK>` refers to the index number shown in the task list. <br>
> The `<INDEX_TIMESLOT>` refers to the index number of the timeslot shown under each task. <br>
> Both indexes **must be a positive integer** 1, 2, 3, ...

Examples:
* `confirm 3 2`

#### Editing a task : `edit`
Edits any component of a particular task <br>
Format: `edit <INDEX> -n <TASK> -dt <START_DATE/TIME> to <END_DATE/TIME> -p <PRIORITY> -t <TAG(s)`

> Edits the task at the specific `<INDEX>`. 
> The index refers to the index number shown in the task list.
> The index **must be a positive integer** 1, 2, 3, ... 
>
> Words in `UPPER_CASE` are the parameters. Other than `<INDEX>`, all parameters are optional. <br>
> Order of parameters are **not** fixed.
>
> Time is in a 24-hr format (e.g. 1330)

Examples:
* `edit 3 -n Meet John Tan -dt 08/10/2016 1000 to 1200 -p H -t friend`

#### Editing a task by appending details to a task : `edit -ap`
Edits a task by appending details to a particular task name <br>
Format: `edit <INDEX> -ap <DETAILS_TO_APPEND_TO_TASK>`

> Appends details to the task at the specific `<INDEX>`. 
> The index refers to the index number shown in the task list.
> The index **must be a positive integer** 1, 2, 3, ... 
>
> `<DETAILS_TO_APPEND_TO_TASK>` are words that the user wants to append to the back of the task.

Examples:
* `edit 3 -ap At UTown` edits `3. Meet John Doe` to `3. Meet John Doe At UTown`

#### Editing a tag's name : `tag -e`
Edits a tag’s name <br>
Format: `tag -e <INDEX> <TAG>`

> Edits the name of the tag at the specific `<INDEX>`. 
> The index refers to the index number shown in the tag list.
> The index **must be a positive integer** 1, 2, 3, ... 

Examples:
* `tag -e 5 Assignment`

#### Deleting a tag : `tag -d`
Deletes a particular tag <br>
Format: `tag -d <INDEX>`

> Deletes the tag at the specific `<INDEX>`
> The index refers to the index number shown in the tag list.
> The index **must be a positive integer** 1, 2, 3, ...

Examples:
* `tag -d 4` deletes the tag at Index 4

#### Listing all tags : `tag -ls`
Lists all tags in the To-Do List <br>
Format: `tag -ls`

#### Finding tags : `tag -f`
Finds all tags by keywords (i.e. AND search) <br>
Format: `tag -f <KEYWORD>[ , <KEYWORD>, <KEYWORD>, ...]`

> `<KEYWORD>` are **case-insensitive**. The order of the `<KEYWORD>` does not matter.

Examples:
* `tag -f assignment` returns all tags with the word “assignment” (e.g. CS2103 assignment, CS2010 assignment)
* `tag -f cs2103 assignment` returns all tags with both the words “cs2103” and “assignment" (e.g. CS2103 assignment)

#### Marking tasks : `mark`
Marks a particular task(s) with the status `done` or `undone` <br>
Format: `mark -do <INDEX>[ , <INDEX>, <INDEX>, ...] -ud <INDEX>[, <INDEX>, <INDEX>, …]`

> Marks the task at the specific `<INDEX>`
> The index refers to the index number shown in the tag list.
> The index **must be a positive integer** 1, 2, 3, ...
> 
> Use -do to mark a task(s) as `done` <br>
> Use -ud to mark a task(s) as `undone`

Examples:
* `mark -do 3, 5, 7`
* `mark -ud 2, 3`

#### Deleting a task : `del`
Deletes a particular task, or a list of task based on a specific criteria (i.e. INDEX, done/undone status, date, tags, priority) <br>
Formats: 
* `del <INDEX> [, <INDEX>, <INDEX>, …]`
* `del -all <INDEX> [, <INDEX>, <INDEX>, …]`
* `del -dt [<START_DATE> to <END_DATE>] <INDEX>[, <INDEX>, <INDEX>,...]`
* `del -t <TAG>[ , <TAG>, <TAG>] <INDEX>[, <INDEX>, <INDEX>,...]`
* `del -p [PRIORITY] <INDEX> [, <INDEX>, <INDEX>, …]`

> use -all to delete selected undone and done tasks <br>
> use -dt to delete selected undone tasks in that date range <br>
> use -t to delete selected undone tasks with a searched tag(s) <br>
> use -p to delete selected undone tasks with that particular priority level

Examples:
* `del 3, 6`
* `del -all 3, 23`
* `del -dt 23/09/16 to 24/09/16 9, 10`
* `del -t friend, family 9, 15`
* `del -p high 3, 5, 6`

#### Listing tasks : `ls`
Lists all tasks in the To-Do List with available list filters.<br>
Format: 
* `ls`
* `ls -all`
* `ls -dt [START_DATE] to [START_TIME]`
* `ls -t <TAG>[ , <TAG>, <TAG>]`
* `ls -p [PRIORITY]`

> default is to list all undone task <br>
> use -all to list all done and undone tasks <br>
> use -dt to list all undone tasks in that date range <br>
> use -t to list all undone tasks with a searched tag(s) <br>
> use -p to list all undone tasks with that particular priority level

Examples:
* `ls`
* `ls -all`
* `ls -dt 23/09/16 to 24/09/16`
* `ls -t assignments, projects`
* `ls -p high`


#### Finding tasks : `find`
Finds all tasks containing a list of keywords (i.e. AND search).<br>
Format: `find <KEYWORD>[, KEYWORD, KEYWORD]`

> `<KEYWORD>` are **case-insensitive**. The order of the `<KEYWORD>` does not matter.

Examples: 
* `find meet, John` returns all tasks containing the keywords “meet” and “John” (e.g. meet John Doe)

#### Undoing a command : `undo`
Undo a command executed by the user. <br>
Format: `undo` 

> Able to undo up to 10 past commands

#### Clearing the data storage file : `clear`
Clears the whole To-Do List storage file.<br>
Format: `clear`  

#### Exiting the program : `exit`
Exits the program.<br>
Format: `exit` 

#### Changing data storage location : `cd`
Changes the directory of which the data storage file is saved in. <br>
Format: `cd <FILE_PATH>`

> Returns an error if the there is an error in the directory chosen

Examples:
* `cd C:\Users\John_Doe\Documents`  

#### Saving the data 
To-Do List data are saved in the hard disk automatically after any command that changes the data.<br>
There is no need to save manually.

## FAQ

**Q**: How do I transfer my data to another Computer?<br>
**A**: Install the app in the other computer and overwrite the empty data file it creates with 
       the file that contains the data of your previous To-Do List.
       
## Command Summary

Command | Format  
-------- | :-------- 
[Add](#adding-a-task-add)| `add -n <TASK> -dt <START_DATE/TIME> to <END_DATE/TIME> -p <PRIORITY> -t <TAG(s)> -r <NUM_TIMES> <FREQUENCY>`
[Change Storage Location](#changing-data-storage-location-cd) | `cd <FILE_PATH>`
[Clear](#clearing-the-data-storage-file-clear) | `clear`
[Confirm](#confirming-a-reserved-timeslot-confirm) | `confirm <INDEX_TASK> <INDEX_TIMESLOT>`
[Delete](#deleting-a-task-del) | `del <INDEX> [, <INDEX>, <INDEX>, …]`
[Delete [by Date]](#deleting-a-task-del) | `del -dt [<START_DATE> to <END_DATE>] <INDEX>[, <INDEX>, <INDEX>,...]`
[Delete [by Priority]](#deleting-a-task-del) | `del -p [PRIORITY] <INDEX> [, <INDEX>, <INDEX>, …]`
[Delete [by Tags]](#deleting-a-task-del) | `del -t <TAG>[ , <TAG>, <TAG>] <INDEX>[, <INDEX>, <INDEX>,...]`
[Edit](#editing-a-task-edit) | `edit <INDEX> -n <TASK> -dt <START_DATE/TIME> to <END_DATE/TIME> -p <PRIORITY> -t <TAG(s)>`
[Edit [Append]](#editing-a-task-by-appending-details-to-a-task-edit-ap) | `edit <INDEX> -ap <TO APPEND>`
[Exit](#exiting-the-program-exit) | `exit`
[Find](#finding-tasks-find) | `find KEYWORD [MORE_KEYWORDS]`
[Help](#displaying-a-list-of-available-commands-help) | `help`
[List](#listing-tasks-ls) | `ls`
[List All](#listing-tasks-ls) | `ls -all`
[List [Date]](#listing-tasks-ls) | `ls -dt [<START_DATE> to <END_DATE>]`
[List [Priority]](#listing-tasks-ls) | `ls -p [PRIORITY]`
[List [Tags]](#listing-tasks-ls) | `ls -t <TAG>[ , <TAG>, <TAG>]`
[Mark Done](#marking-tasks-mark) | `mark -do <INDEX>[ , <INDEX>, <INDEX>, ...]`
[Mark Undone](#marking-tasks-mark) | `mark -ud <INDEX>[ , <INDEX>, <INDEX>, ...]`
[Reserve](#reserving-timeslots-for-a-task-rsv) | `rsv -n <TASK> -dt <START_DATE/TIME> to <END_DATE/TIME> [, <START_DATE/TIME> to <END_DATE/TIME>, …]`
[Reserve [Delete]](#reserving-timeslots-for-a-task-rsv) | `rsv -d <INDEX>`
[Reserve [Edit]](#reserving-timeslots-for-a-task-rsv) | `rsv -e <INDEX> -n <TASK> -dta <START_DATE/TIME> to <END_DATE/TIME> -dtr <START_DATE/TIME> to <END_DATE/TIME`
[Tag (Delete)](#deleting-a-tag-tag-d) | `tag -d <INDEX>`
[Tag [Edit]](#editing-a-tags-name-tag-e) | `tag -e <INDEX> <TAG>`
[Tag [Find]](#finding-tags-tag-f) | `tag -f <KEYWORD>[ , <KEYWORD>, <KEYWORD>]`
[Tag [List]](#listing-all-tags-tag-ls) | `tag -ls`
[Undo](#undoing-a-command-undo) | `undo`

