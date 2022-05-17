
/* global await */

$(document).ready(function(){
	
	$("#simpleUpload").click(function(){
		$.ajax({
			url:'/create',
			success: function() {
				alert("File upload complete.");
			}     
		});
	});
	
	$("#refreshFileButton").click(function(){
		$.ajax({
			url: '/listfiles',
		}).done(function(data){
			console.dir(data);
			var fileHTML = "";
			for(file of data) {
                             if(file.thumbnailLink!= null){
                                fileHTML += '<br><div  class="list-group-item"><img class="fotos" src="' + file.thumbnailLink + '">' 
				+'</br>'+ file.name 
				+ '<button class="eliminar" onclick="makePublic(\'' + file.id + '\')"><img src="css/compartir (1).png"/></button>' 
				+ '<button  class="eliminar" onclick="deleteFile(\'' + file.id + '\')"><img src="css/eliminar.png"/></button></div>'; 
                             }else{
                                 fileHTML += '<br><div  class="list-group-item"><img class="imgcarpetas" src="https://icones.pro/wp-content/uploads/2021/04/icone-de-dossier-symbole-png-gris.png">' 
				+'</br>'+ file.name 
				+ '<button class="eliminar" onclick="makePublic(\'' + file.id + '\')"><img src="css/compartir (1).png"/></button>' 
				+ '<button  class="eliminar" onclick="deleteFile(\'' + file.id + '\')"><img src="css/eliminar.png"/></button></div>';
                                //+ '<button  class="eliminar" id="createsubFolder"><img src="css/folder.png"/>create</button></div>';
                             }
                                
			 }
			
			$("#fileListContainer").html(fileHTML);
		});
	});
	
        $("#createFolderButton").click(function(){
		var folderName = prompt('Ingrese Nombre Carpeta');
		$.ajax({
			url: '/createfolder/' + folderName
		}).done(function(data){
			console.dir(data);
                       // alert("Creado Con Exito");
                         Swal.fire(
                        'Creado con Exito',
                        ' ',
                        'success'
                )
		})
	});
        
         /* $("#createsubFolderButton").click(function(){
		var subfolder = prompt('Ingrese Nombre de SubCarpeta');
		$.ajax({
			url: '/createsubfolder/' + subfolder 
		}).done(function(data){
			console.dir(data);
                       // alert("Creado Con Exito");
                         Swal.fire(
                        'Creado con Exito',
                        ' ',
                        'success'
                )
		})
	});*/
     
                    $("#uploadFile").click(function (event) {
                    //stop submit the form, we will post it manually.
                    event.preventDefault();

                    // Get form
                    var form = $('#fileUploadtoFolder')[0];

                    // Create an FormData object 
                    var data = new FormData(form);
                    data.append("folderId", $("#folderId").val());
                    // disabled the submit button
                    $("#uploadFile").prop("disabled", true);

                    $.ajax({
                        type: "POST",
                        enctype: 'multipart/form-data',
                        url: "/uploadFileToFolder",
                        data: data,
                        processData: false, //prevent jQuery from automatically transforming the data into a query string
                        contentType: false,
                        cache: false,
                        timeout: 600000,
                        success: function (data) {

                           
                            console.log("SUCCESS : ", data);
                            $("#uploadFile").prop("disabled", false);
                             Swal.fire(
                        'Subido con Exito',
                        ' ',
                        'success'
                        )
                        },
                        error: function (e) {
                        
                            console.log("ERROR : ", e);
                            $("#uploadFile").prop("disabled", false);
                            Swal.fire(
                        'Archivo Supera Limite',
                        ' ',
                        'error'
                        )
                        }
                    });

                });
          

    $("#btnSubmit").click(function (event) {

                    //stop submit the form, we will post it manually.
                    event.preventDefault();

                    // Get form
                    var form = $('#fileUploadForm')[0];

                    // Create an FormData object 
                    var data = new FormData(form);

                    // disabled the submit button
                    $("#btnSubmit").prop("disabled", true);

                    $.ajax({
                        type: "POST",
                        enctype: 'multipart/form-data',
                        url: "/uploadFile",
                        data: data,
                        processData: false, //prevent jQuery from automatically transforming the data into a query string
                        contentType: false,
                        cache: false,
                        timeout: 100000,
                        success: function (data) {

                            $("#result").text(data);
                                Swal.fire(
                        'Subido con Exito',
                        ' ',
                        'success'
                )
                            console.log("SUCCESS : ", data);
                            $("#btnSubmit").prop("disabled", false);
                        

                        },
                        error: function (e) {

                            $("#result").text(e.responseText);
                                 Swal.fire(
                        'Archivo supera Limite ',
                        ' ',
                        'error'
                )
                            console.log("ERROR : ", e);
                            $("#btnSubmit").prop("disabled", false);
                           
                        }
                    });

                });
          
     $("#createsubFolder").click(function() {

        
                    //stop submit the form, we will post it manually.
                    event.preventDefault();

                    // disabled the submit button
                    $("#createFolder").prop("disabled", true);
                    var folderName = prompt('Ingrese Nombre subCarpeta'); 
                    $("#folderName").val();
//                    alert(folderName);
                    $.ajax({
                        type: "POST",
                        url: "/createsubFolder" ,
                        data: {folderName: folderName},
                        cache: false,
                        timeout: 600000,
                        success: function (data) {

                            $("#foldercraeted").text("Folder Created With Name " + data[1]);
                            console.log("SUCCESS : ", data);
                            console.log("SUCCESS : ", data[0]);
//                            console.log("SUCCESS : ", data[1]);
                            $("#folderId").val(data[0]);
                            $("#folderName").val(data[1]);
                            $("#chooseFile").text("Choose File to Upload to folder : " + data[1]);
                           Swal.fire(
                        'Creado con Exito',
                        ' ',
                        'success'
                )

                        },
                        error: function (e) {

//                            $("#folderresult").text(e.responseText);
                            console.log("ERROR : ", e);
                            $("#createFolder").prop("disabled", false);
                          Swal.fire(
                        'Error al Crear',
                        ' ',
                        'error'
                )
                        }
                    });

                });
    
});

function deleteFile(id) {
	$.ajax({
		url: '/deletefile/' + id,
		method: 'DELETE'
	}).done(function(){
		//alert('File has been deleted. Please refresh the list.');
                  Swal.fire(
                        'Eliminado con Exito',
                        ' ',
                        'error'
                )
	});
}

function makePublic(id) {
	$.ajax({
		url: '/makepublic/' + id,
		method: 'POST'
	}).done(function(){
		//alert('File can be viewed by anyone on internet.');
                  Swal.fire(
                        'Compartido con Exito',
                        ' ',
                        'success'
                )
	});
 }   
 
/*function subfolder(id) {
     
        var subfolder = prompt('Ingrese Nombre de SubCarpeta');
		$.ajax({
			url: '/createsubfolder/' + subfolder 
		}).done(function(data){
			console.dir(data);
                       // alert("Creado Con Exito");
                         Swal.fire(
                        'Creado con Exito',
                        ' ',
                        'success'
                )
		});
          } */       
  
  
        
          



    

