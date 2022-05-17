/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mgodoyd.gdrive.sbt.GdriveApi.controller;

import com.fasterxml.jackson.databind.introspect.TypeResolutionContext.Empty;
import com.mgodoyd.gdrive.sbt.GdriveApi.service.HomePageService;
import com.mgodoyd.gdrive.sbt.GdriveApi.dto.FileItemDTO;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeRequestUrl;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
import com.google.api.client.googleapis.services.CommonGoogleClientRequestInitializer;
import com.google.api.client.googleapis.services.GoogleClientRequestInitializer;
import com.google.api.client.http.FileContent;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;
import com.google.api.services.drive.model.Permission;
import java.io.IOException;

import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
 import javax.swing.JOptionPane;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;



import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeRequestUrl;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Collections;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import com.google.api.services.drive.model.File;
import java.net.URLEncoder;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author godoy
 */
@Controller
public class HomeController {
    private static HttpTransport HTTP_TRANSPORT = new NetHttpTransport();
	private static JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();

	 private static final List<String> SCOPES = Collections.singletonList(DriveScopes.DRIVE);

	/*private static final List<String> SCOPES = Arrays.asList(DriveScopes.DRIVE,
			"https://www.googleapis.com/auth/drive.install");*/

        
	private static final String USER_IDENTIFIER_KEY = "CYBER_CODER";

	@Value("${google.oauth.callback.uri}")
	private String CALLBACK_URI;

	@Value("${google.secret.key.path}")
	private Resource gdSecretKeys;

	@Value("${google.credentials.folder.path}")
	private Resource credentialsFolder;
        
        
	
	/*@Value("${google.service.account.key}")
	private Resource serviceAccountKey;*/
         @Autowired
    HomePageService homePageService;
   
   
       

	private GoogleAuthorizationCodeFlow flow;

	@PostConstruct
	public void init() throws Exception {
		GoogleClientSecrets secrets = GoogleClientSecrets.load(JSON_FACTORY,
				new InputStreamReader(gdSecretKeys.getInputStream()));
		flow = new GoogleAuthorizationCodeFlow.Builder(HTTP_TRANSPORT, JSON_FACTORY, secrets, SCOPES)
				.setDataStoreFactory(new FileDataStoreFactory(credentialsFolder.getFile())).build();
	}

	@GetMapping(value = { "/" })
	public String showHomePage() throws Exception {
		boolean isUserAuthenticated = false;

		Credential credential = flow.loadCredential(USER_IDENTIFIER_KEY);
		if (credential != null) {
			boolean tokenValid = credential.refreshToken();
			if (tokenValid) {
				isUserAuthenticated = true;
			}
		}

		return isUserAuthenticated ? "dashboard.html" : "index.html";
	}

	@GetMapping(value = { "/googlesignin" })
	public void doGoogleSignIn(HttpServletResponse response) throws Exception {
		GoogleAuthorizationCodeRequestUrl url = flow.newAuthorizationUrl();
		String redirectURL = url.setRedirectUri(CALLBACK_URI).setAccessType("offline").build();
		response.sendRedirect(redirectURL);
	}

	@GetMapping(value = { "/oauth" })
	public String saveAuthorizationCode(HttpServletRequest request) throws Exception {
		String code = request.getParameter("code");
		if (code != null) {
			saveToken(code);

			return "dashboard.html";
		}

		return "index.html";
	}

	private void saveToken(String code) throws Exception {
		GoogleTokenResponse response = flow.newTokenRequest(code).setRedirectUri(CALLBACK_URI).execute();
		flow.createAndStoreCredential(response, USER_IDENTIFIER_KEY);

	}

	/*@GetMapping(value = { "/create" })
	public void createFile(HttpServletResponse response) throws Exception {
		Credential cred = flow.loadCredential(USER_IDENTIFIER_KEY);

		Drive drive = new Drive.Builder(HTTP_TRANSPORT, JSON_FACTORY, cred)
				.setApplicationName("googledrivespringbootexample").build();

		File file = new File();
		file.setName("profile.jpg");

		FileContent content = new FileContent("image/jpeg", new java.io.File("D:\\practice\\sbtgd\\sample.jpg"));
		File uploadedFile = drive.files().create(file, content).setFields("id").execute();

		String fileReference = String.format("{fileID: '%s'}", uploadedFile.getId());
		response.getWriter().write(fileReference);
	}*/
        
         @PostMapping("/uploadFile")
    public ResponseEntity<?> uploadFile(@RequestParam("file") MultipartFile file) throws IOException {
        homePageService = new HomePageService();
        Drive drive = new Drive(HTTP_TRANSPORT, JSON_FACTORY, flow.loadCredential(USER_IDENTIFIER_KEY));
        return homePageService.uploadFile(file, false,"",drive);
    }
    
        @PostMapping("/uploadFileToFolder")
    public ResponseEntity<?> uploadFileToFolder(@RequestParam("filename") MultipartFile file, @RequestParam("folderId") String folderId) throws IOException {
        homePageService = new HomePageService();
       Credential cred = flow.loadCredential(USER_IDENTIFIER_KEY);

		Drive drive = new Drive.Builder(HTTP_TRANSPORT, JSON_FACTORY, cred)
				.setApplicationName("googledrivespringbootexample").build();
        return homePageService.uploadFile(file, true, folderId,drive);
    }
   
 
     /*  @GetMapping(value = { "/uploadinfolder" })  //subir files
	public void uploadFileInFolder(HttpServletResponse response) throws Exception {
		Credential cred = flow.loadCredential(USER_IDENTIFIER_KEY);

		Drive drive = new Drive.Builder(HTTP_TRANSPORT, JSON_FACTORY, cred)
				.setApplicationName("googledrivespringbootexample").build();

                
                 File file = new File();               
               /*file.setName("googledrive.png");//+5mb
		file.setParents(Arrays.asList("1Zi_G4L2IQOsa34nT5T2zx9_ofKkf-qaM"));
                FileContent content = new FileContent("image/jpeg", new java.io.File("C:\\Users\\godoy\\Desktop\\semestre V\\progra3\\imagenes prueba\\googledrive.png"));
                int foldersize = file.setParents(Arrays.asList("1XxB7D3L_4nc0rhZq8Og7R5X5Vk3uekOD")).size();*/
                
            /*    file.setName("carro1.png");//5mb   //datos quemados
		file.setParents(Arrays.asList("1Zi_G4L2IQOsa34nT5T2zx9_ofKkf-qaM"));
                FileContent content = new FileContent("image/jpeg", new java.io.File("C:\\Users\\godoy\\Desktop\\semestre V\\progra3\\imagenes prueba\\carro1.png"));
               int foldersize = file.setParents(Arrays.asList("1Zi_G4L2IQOsa34nT5T2zx9_ofKkf-qaM")).size();
               System.out.println(file.setParents(Arrays.asList("1Zi_G4L2IQOsa34nT5T2zx9_ofKkf-qaM")).size()); */              
               /* file.setName("carro2.png");//3mb
		file.setParents(Arrays.asList("1Zi_G4L2IQOsa34nT5T2zx9_ofKkf-qaM"));
                FileContent content = new FileContent("image/jpeg", new java.io.File("C:\\Users\\godoy\\Desktop\\semestre V\\progra3\\imagenes prueba\\carro2.png"));
                int foldersize = file.setParents(Arrays.asList("1XxB7D3L_4nc0rhZq8Og7R5X5Vk3uekOD")).size();*/
                
               /*  file.setName("carro3.png");//2mb
		file.setParents(Arrays.asList("1Zi_G4L2IQOsa34nT5T2zx9_ofKkf-qaM"));
                FileContent content = new FileContent("image/jpeg", new java.io.File("C:\\Users\\godoy\\Desktop\\semestre V\\progra3\\imagenes prueba\\carro3.png"));
                int foldersize = file.setParents(Arrays.asList("1XxB7D3L_4nc0rhZq8Og7R5X5Vk3uekOD")).size();*/
                
               /*int size = (int) content.getLength();
                 Long   Limite = 5242880L; //get Size returna el valor en bytes
                    
                if(size<Limite){
                            File uploadedFile = drive.files().create(file, content).setFields("id").execute();
                            String fileReference = String.format("{fileID: '%s'}", uploadedFile.getId());
                            response.getWriter().write(fileReference);
                            // JOptionPane.showMessageDialog(null, "Archivo Supera Limite" + file.setName("googledrive.png"));
                                    }else{
                }  
                    }*/
	@GetMapping(value = { "/listfiles" }, produces = { "application/json" })
	public @ResponseBody List<FileItemDTO> listFiles() throws Exception {
		Credential cred = flow.loadCredential(USER_IDENTIFIER_KEY);

		Drive drive = new Drive.Builder(HTTP_TRANSPORT, JSON_FACTORY, cred)
				.setApplicationName("googledrivespringbootexample").build();

		List<FileItemDTO> responseList = new ArrayList<>();

		FileList fileList = drive.files().list().setFields("files(id,name,thumbnailLink)").execute();
		for (File file : fileList.getFiles()) {
			FileItemDTO item = new FileItemDTO();
			item.setId(file.getId());
			item.setName(file.getName());
			item.setThumbnailLink(file.getThumbnailLink());
			responseList.add(item);
		}

		return responseList;
	}

	@PostMapping(value = { "/makepublic/{fileId}" }, produces = { "application/json" })
	public @ResponseBody Message makePublic(@PathVariable(name = "fileId") String fileId) throws Exception {
		Credential cred = flow.loadCredential(USER_IDENTIFIER_KEY);

		Drive drive = new Drive.Builder(HTTP_TRANSPORT, JSON_FACTORY, cred)
				.setApplicationName("googledrivespringbootexample").build();

		Permission permission = new Permission();
		permission.setType("anyone");
		permission.setRole("reader");

		drive.permissions().create(fileId, permission).execute();

		Message message = new Message();
		message.setMessage("Permission has been successfully granted.");
		return message;
	}

	@DeleteMapping(value = { "/deletefile/{fileId}" }, produces = "application/json")
	public @ResponseBody Message deleteFile(@PathVariable(name = "fileId") String fileId) throws Exception {
		Credential cred = flow.loadCredential(USER_IDENTIFIER_KEY);

		Drive drive = new Drive.Builder(HTTP_TRANSPORT, JSON_FACTORY, cred)
				.setApplicationName("googledrivespringbootexample").build();

		drive.files().delete(fileId).execute();

		Message message = new Message();
		message.setMessage("File has been deleted.");
		return message;
	}

	@GetMapping(value = { "/createfolder/{folderName}" }, produces = "application/json")
	public @ResponseBody Message createFolder(@PathVariable(name = "folderName") String folder) throws Exception {
		Credential cred = flow.loadCredential(USER_IDENTIFIER_KEY);

		Drive drive = new Drive.Builder(HTTP_TRANSPORT, JSON_FACTORY, cred)
				.setApplicationName("googledrivespringbootexample").build();

		File file = new File();
		file.setName(folder);
		file.setMimeType("application/vnd.google-apps.folder");
                
		drive.files().create(file).execute();

		Message message = new Message();
		message.setMessage("Folder has been created successfully.");
		return message;
	}
        
        //crear subfolder
       /*@GetMapping(value={ "/createsubfolder/{subfolder}" }, produces="application/json")
         public @ResponseBody Message createsubFolder(@PathVariable(name = "subfolder") String subbfolder) throws Exception{
             Credential cred = flow.loadCredential(USER_IDENTIFIER_KEY);

		Drive drive = new Drive.Builder(HTTP_TRANSPORT, JSON_FACTORY, cred)
				.setApplicationName("googledrivespringbootexample").build();
               // idcarpeta="{idraiz}"
                String folderId = "1Zi_G4L2IQOsa34nT5T2zx9_ofKkf-qaM";  //con datos quedamos en este caso el id de la carpeta raiz
		File file = new File();
		file.setName(subbfolder);
                file.setParents(Collections.singletonList(folderId));
		file.setMimeType("application/vnd.google-apps.folder");
                System.out.println(folderId);
                
		drive.files().create(file).setFields("id, parent").execute();

		Message message = new Message();
		message.setMessage("Folder has been created successfully.");
		return message;
             
         
         }*/
        @PostMapping("/createsubFolder")
    public ResponseEntity<List<String>> createsubFolder(@RequestParam("folderName") String folderName ) throws IOException {
        Drive drive = new Drive(HTTP_TRANSPORT, JSON_FACTORY, flow.loadCredential(USER_IDENTIFIER_KEY));
                String folderId = "1e8swyIsCOiHm2xdjYZUxwDLjLl_u1Kpq"; 
        File fileMetadata = new File();
        fileMetadata.setName(folderName);
        fileMetadata.setParents(Collections.singletonList(folderId));
        fileMetadata.setMimeType("application/vnd.google-apps.folder");

        File file = drive.files().create(fileMetadata)
                .setFields("id")
                .execute();
        List<String> fileData = new ArrayList<>();
        if (!file.isEmpty()) {
            fileData.add(0, file.getId());
            fileData.add(1, folderName);
            return new ResponseEntity(fileData, new HttpHeaders(), HttpStatus.CREATED);
        }
        return new ResponseEntity(fileData, new HttpHeaders(), HttpStatus.BAD_REQUEST);
    } 
      

         
	/*@GetMapping(value = { "/servicelistfiles" }, produces = { "application/json" })
	public @ResponseBody List<FileItemDTO> listFilesInServiceAccount() throws Exception {
		Credential cred = GoogleCredential.fromStream(serviceAccountKey.getInputStream());
		
		GoogleClientRequestInitializer keyInitializer = new CommonGoogleClientRequestInitializer();

		Drive drive = new Drive.Builder(HTTP_TRANSPORT, JSON_FACTORY, null).setHttpRequestInitializer(cred)
				.setGoogleClientRequestInitializer(keyInitializer).build();

		List<FileItemDTO> responseList = new ArrayList<>();

		FileList fileList = drive.files().list().setFields("files(id,name,thumbnailLink)").execute();
		for (File file : fileList.getFiles()) {
			FileItemDTO item = new FileItemDTO();
			item.setId(file.getId());
			item.setName(file.getName());
			item.setThumbnailLink(file.getThumbnailLink());
			responseList.add(item);
		}

		return responseList;
	}*/

	class Message {
		private String message;

		public String getMessage() {
			return message;
		}

		public void setMessage(String message) {
			this.message = message;
		}

	}

}
