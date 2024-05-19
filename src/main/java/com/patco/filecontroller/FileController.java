package com.patco.filecontroller;

// import org.springframework.boot.SpringApplication;
// import org.springframework.boot.autoconfigure.SpringBootApplication;
// import org.springframework.web.bind.annotation.GetMapping;
// import org.springframework.web.bind.annotation.RequestParam;
// import org.springframework.web.bind.annotation.RestController;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.FileExistsException;
import org.apache.commons.io.FileUtils;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;

//import org.springframework.boot.autoconfigure.couchbase.CouchbaseProperties.Io;

import com.patco.hashmap.HashMap;

public class FileController {
    String bin;
    String unorganized_bin;

    public String printCWD() {
        return System.getProperty("user.dir");
    }

    public boolean setBin(String path) {
        File folder = new File(path);
        if (folder.exists()) {
            this.bin = path;
            System.out.println("Bin set!");
            return true;
        }
        return false;
    }

    public boolean setUnorganizedBin(String path) {
        File folder = new File(path);
        if (folder.exists()) {
            this.unorganized_bin = path;
            System.out.println("Unsorted Bin set!");
            return true;
        }
        return false;
    }

    public ArrayList<String> gatherFileTypes() {
        ArrayList<String> file_types = new ArrayList<>();
        return file_types;
    }

    public void iterateOverFiles(String path, String destination_path, HashMap<String, String> map) throws IOException {
        File folder = new File(path);
        File[] files = folder.listFiles();

        if (files.length > 0) {

            for (int i = 0; i < files.length; i++) {
                String selected_year;
                if (files[i].isDirectory()) {
                    System.out.println("Directory: " + files[i].getName());
                    moveDirectory(files[i], destination_path);
                }
                if (files[i].isFile()) {
                    Path p = files[i].toPath();
                    System.out.println("File: " + files[i].getName());
                    String type = FilenameUtils.getExtension(files[i].getName()).toLowerCase();
                    String parent_group = map.get(type);

                    System.out.println("(key): " + type);
                    System.out.println("(parent_group): " + parent_group);

                    BasicFileAttributes attr = Files.readAttributes(p, BasicFileAttributes.class);

                    FileTime creation_year = attr.creationTime();
                    FileTime modified_year = attr.lastModifiedTime();
                    int older = creation_year.compareTo(modified_year);
                    System.out.println("Older: " + older);

                    if (older > 0) {
                        selected_year = modified_year.toString().split("-")[0];
                    } else {
                        selected_year = creation_year.toString().split("-")[0];
                    }

                    movefile(files[i], destination_path, parent_group, type, selected_year);
                }
            }
        } else {
            System.out.println("No files found!");
        }

    }

    public boolean generateEnviroment(String unorganized_bin_path, String bin_path) {
        createDirectory(unorganized_bin_path);
        createDirectory(bin_path);
        createDirectory(bin_path + "\\" + "unhandled_directories");
        createDirectory(bin_path + "\\" + "backups");

        return true;
    }

    public boolean createFile(String path) {
        try {
            File file = new File(path);
            if (file.createNewFile()) {
                System.out.println("File created: " + file.getName());
                return true;
            } else {
                System.out.println("File already exists.");
            }
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
        return false;
    }

    public boolean deleteFile(File f) {
        return false;
    }

    // TODO: Check to see if file name is taken. If so append a unique id to the
    // file being moved. The file will not be moved if it already exist.
    public boolean movefile(File src_file, String destination_path, String parent_group, String type, String date) {

        createDirectory(destination_path + "\\" + parent_group);
        createDirectory(destination_path + "\\" + parent_group + "\\" + type);
        createDirectory(destination_path + "\\" + parent_group + "\\" + type + "\\" + date);

        try {
            File destination_file = new File(
                    destination_path + "\\" + parent_group + "\\" + type + "\\" + date + "\\" + src_file.getName());
            FileUtils.moveFile(src_file, destination_file);
            System.out.println("File: " + src_file.getName() + " moved.");
            return true;
        } catch (NullPointerException n) {
        } catch (FileExistsException fe) {
        } catch (IOException e) {
        }

        return false;
    }

    public boolean copyFile() {
        return false;
    }

    // TODO: Look into using mkdirs instead.
    public boolean createDirectory(String path) {
        File directory = new File(path);
        if (directory.mkdir()) {
            System.out.println("Directory created: " + directory.getName());
            return true;
        } else {
            System.out.println("Directory already exists.");
        }
        return false;
    }

    public boolean deleteDirectory() {
        return false;
    }

    // TODO: What happens if a directory already exist at the destination.
    public boolean moveDirectory(File src_directory, String destination_path) {
        try {
            File destination_directory = new File(
                    destination_path + "\\" + "unhandled_directories" + "\\");
            FileUtils.moveDirectoryToDirectory(src_directory, destination_directory, true);
            System.out.println("File: " + src_directory.getName() + " moved.");
            return true;
        } catch (NullPointerException n) {
        } catch (FileExistsException fe) {
        } catch (IOException e) {
        }
        return false;
    }

    public boolean copyDirectory() {
        return false;
    }

}
