/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mgodoyd.gdrive.sbt.GdriveApi.dto;
import java.io.Serializable;
/**
 *
 * @author godoy
 */
public class FileItemDTO {
   /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String name;

	private String id;

	private String thumbnailLink;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getThumbnailLink() {
		return thumbnailLink;
	}

	public void setThumbnailLink(String thumbnailLink) {
		this.thumbnailLink = thumbnailLink;
	}
 
}
