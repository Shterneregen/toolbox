# BackUper

This small program allows you to make backup copies of the files we need.

### Usage

- Create a file to store settings, e.g. props.txt.  
- Add `folderForBkps` property, it describes target folder to back up files  
- Add `files` property, it's comma separated list of files we want to back up  

props.txt
```properties
folderForBkps=C:/BkpFolder
files=D:/folder/file1.txt,\
D:/folder/file1.txt,\
D:/folder/file1.txt
```

- Build JAR file, after building it will be located in the `build` folder
```shell
gradlew clean build
```

- Run the command, it copies files, overwrites if files have been changed
```shell
java -jar bkps.jar -p props.txt
```
With `-n` flag backups will be created in a folder with the current date, e.g. `C:/BkpFolder/16-01-2022`
```shell
java -jar bkps.jar -p props.txt -n
```
