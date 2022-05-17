/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GDriveUAS;

/**
 *
 * @author KMalif
 */
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;
 
public class FindFilesByName {
 
    // com.google.api.services.drive.model.File
    public static final List<File> getGoogleFilesByName(String fileNameLike) throws IOException {
 
        Drive driveService = GoogleDriveUtils.getDriveService();

        String pageToken = null;
        List<File> list = new ArrayList<File>();

        String query = null;
 if (fileNameLike == null) {
        
            query = " mimeType = 'application/vnd.google-apps.folder' " //
                    + " and '" + fileNameLike + "' in parents";
        } else {
              query =" name contains '" + fileNameLike + "' " //
                + " and mimeType != 'application/vnd.google-apps.folder' ";
        }
        do {
            FileList result = driveService.files().list().setQ(query).setSpaces("drive") //
                    // Fields will be assigned values: id, name, createdTime, mimeType
                   .setQ("mimeType='image/png'")
                   // .setQ("mimeType='text/plain'").setQ("mimeType='image/png'")
                    .setFields("nextPageToken, files(id, name, createdTime, mimeType)")//
                    .setPageToken(pageToken).execute();
            for (File file : result.getFiles()) {
                list.add(file);
            }
            pageToken = result.getNextPageToken();
        } while (pageToken != null);
        //
        return list;
        }
        
     
    // com.google.api.services.drive.model.File
    public static final List<File> getGoogleRootFoldersByName() throws IOException {
        return getGoogleFilesByName(null);
    }
              
    public static void main(String[] args) throws IOException {
 
        List<File> rootGoogleFolders = getGoogleFilesByName("prueba.txt");
        for (File folder : rootGoogleFolders) {
 
            System.out.println("Type: " + folder.getMimeType() + " --- Name: " + folder.getName() +" ID = "+ folder.getId());
        }
       //  System.out.println("error");
    }
    
       // com.google.api.services.drive.model.File
   /* public static final List<File> getGoogleFilesByName(String fileNameLike) throws IOException {
 
        Drive driveService = GoogleDriveUtils.getDriveService();
 
        String pageToken = null;
        List<File> list = new ArrayList<File>();
        String query = " name contains '" + fileNameLike + "' " //
                + " and mimeType != 'application/vnd.google-apps.folder' ";
      
       /*String query = null;
        if (fileNameLike == null) {
            query = " mimeType = 'application/vnd.google-apps.folder' " //
                    + " and 'root' in parents";
        } else {
            query =  " name contains '" + fileNameLike + "' " //
                + " and mimeType != 'application/vnd.google-apps.folder' ";
        }*/
 
/*     
do {
   FileList result = driveService.files().list().setQ(query)
      .setQ("mimeType='image/png'")
      .setSpaces("drive")
      .setFields("nextPageToken, files(id, name)")
      .setPageToken(pageToken)
      .execute();
  for (File file : result.getFiles()) {
    /*System.out.printf("Found file: %s (%s)\n",
        file.getName(), file.getId());*/
/*    System.out.println("File ID: " + file.getId() + " --- Name: " + file.getName());
  }
  pageToken = result.getNextPageToken();
} while (pageToken != null);
        //
        
        return list;
    }
 
    // com.google.api.services.drive.model.File
   public static final List<File> getGoogleRootFolders() throws IOException {
        return getGoogleFilesByName(null);
    }
 
    public static void main(String[] args) throws IOException {
 
        List<File> rootGoogleFolders = getGoogleFilesByName("guide.txt");
        for (File file : rootGoogleFolders) {
 
            System.out.println("File ID: " + file.getMimeType() + " --- Name: " + file.getName());
        }
      //  System.out.println("Error");
    }*/
 
}