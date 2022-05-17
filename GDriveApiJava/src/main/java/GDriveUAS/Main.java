/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package GDriveUAS;

import com.google.api.client.http.FileContent;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.File;
import java.io.IOException;
import java.util.Collections;

/**
 *
 * @author godoy
 */
public class Main {

    /**
     * @param args the command line arguments
     */
  public static void main(String[] args) throws IOException {
       /* Drive driveService = GoogleDriveUtils.getDriveService();
//        new file in folder
        String folderId = "1_f2bRxIya6Y3i8CtArZlsoUPlP1_eGKl";
            File fileMetadata = new File();
            fileMetadata.setName("Pemrograman Komputer 21.pdf");
                        fileMetadata.setParents(Collections.singletonList(folderId));

            java.io.File filePath = new java.io.File("files/Pemrograman Komputer 2.pdf");
            FileContent mediaContent = new FileContent("text/pdf", filePath);
            File file = driveService.files().create(fileMetadata, mediaContent)
                    .setFields("id")
                    .execute();
            System.out.println("File ID: " + file.getId());
*/


//        membuat new folder
//        File fileMetadata = new File();
//        fileMetadata.setName("contoh 2");
//        fileMetadata.setMimeType("application/vnd.google-apps.folder");
//
//        File file = driveService.files().create(fileMetadata)
//                .setFields("id ")
//                .execute();
//        System.out.println("Folder ID: " + file.getId());
//        List<File> googleRootFolders = GetSubFolders.getGoogleRootFolders();
//        for (File folder : googleRootFolders) {
//
//            System.out.println("Folder ID: " + folder.getId() + " --- Name: " + folder.getName());
//
//        }


//        membuat folder di dalam folder
//        File fileMetadata = new File();
//        String folderId = "1Uza7HCnl80ouQPGA_9OHB7Qlwt4PUy3p";
//        fileMetadata.setName("Dimas shogun");
//        fileMetadata.setMimeType("application/vnd.google-apps.folder");
//        fileMetadata.setParents(Collections.singletonList(folderId));
//
//        File file = driveService.files().create(fileMetadata)
//                .setFields("id ")
//                .execute();
//        System.out.println("Folder ID: " + file.getId());
//        List<File> googleRootFolders = GetSubFolders.getGoogleRootFolders();
//        for (File folder : googleRootFolders) {
//
//            System.out.println("Folder ID: " + folder.getId() + " --- Name: " + folder.getName());
//
//        }
    }
    
    
}
