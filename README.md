# Skript Manager
A spigot plugin that lets you manage Skript files from in game.

Commands:
  - /managedevs  
    *  Executable by the console, used to manage the players who can use /devskript
  - /devskript  
    *  Manage files

/managedevs:
  - add <username>  
    *  Allows a player to use the /devskript command. Keep in mind they also need the permission "skriptmanager.manage" if they are not op
  - remove <username>  
    *  Removes a player from the list of players who can use /devskript
  - list  
    *  Lists all the developers. Can be ran by developers, but add and remove are console only

/devskript:
  - create <filename>  
    *  Creates a skript file with the file name
  - delete/remove <filename>  
    *  Deletes a skript file with the file name
  - view <filename> [page]  
    *  View a file 50 lines at a time; the first 50 lines are page 1, the second 50 lines are page 2, etc.
  - edit <filename> <url>  
    *  Edit a skript file and set its contents to the contents of the url. The url should be from https://paste.helpch.at, and make sure to click the "raw text" button after saving
  - edit <filename> -book  
    *  Players only! Edit a skript file and set its contents to the contents of a book your holding
  - rename <filename> <new filename>  
    *  Rename a skript file
  - copy-file-contents <filename>  
    *  Copy the contents of a file to your clipboard
  - list  
    *  List all the skript files
  - help  
    *  Display a help message

This plugin currently only supports file paths without spaces, but does support folders. Keep in mind the server itself can have a file path without spaces; it is just relative to the scripts folder. Thus:  
test.sk                     - Valid  
some-folder/some-file.sk    - Valid  
test file.sk                - Invalid  
a folder/file.sk            - Invalid  
a/b/c/d/e/f_to_z.sk         - Valid  

You also don't have to specifiy the .sk part, and you are not able to use non .sk extensions:  
file                        - Valid  
file.jar                    - Invalid  
file with spaces            - Invalid
