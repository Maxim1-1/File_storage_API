package com.Maxim.File_storage_API.controllers;

import com.Maxim.File_storage_API.dto.FileDTO;
import com.Maxim.File_storage_API.dto.HistoryDTO;
import com.Maxim.File_storage_API.entity.FileEntity;
import com.Maxim.File_storage_API.mapper.FileMapper;
import com.Maxim.File_storage_API.security.CustomPrincipal;
import com.Maxim.File_storage_API.service.FileService;
import com.Maxim.File_storage_API.service.FileUserService;
import org.springframework.http.MediaType;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/files")
public class FileRestControllerV1 {
    public FileRestControllerV1(FileMapper fileMapper, FileService fileService, FileUserService fileUserService) {
        this.fileMapper = fileMapper;
        this.fileService = fileService;
        this.fileUserService = fileUserService;
    }

    private FileMapper fileMapper;

    private FileService fileService;

    private FileUserService fileUserService;


    @GetMapping("/{fileId}")
    public Mono<FileDTO> getFileById(@PathVariable Integer fileId, Authentication authentication) {
        CustomPrincipal userDetails = (CustomPrincipal) authentication.getPrincipal();
        Integer userId = userDetails.getId();
        return fileUserService.getFileByIdForRole(authentication.getAuthorities(), userId, fileId).map(fileMapper::map);
    }

    @GetMapping("")
    public Flux<FileDTO> getAllFiles(Authentication authentication) {
        CustomPrincipal userDetails = (CustomPrincipal) authentication.getPrincipal();
        Integer userId = userDetails.getId();
        return fileUserService.getAllFilesForRole(authentication.getAuthorities(), userId).map(fileMapper::map);
    }


    @RequestMapping(value = "", method = RequestMethod.POST, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Mono<FileDTO> saveFile(@RequestPart("file") Mono<FilePart> file, Authentication authentication) {
        CustomPrincipal userDetails = (CustomPrincipal) authentication.getPrincipal();
        Integer userId = userDetails.getId();
        return fileUserService.saveFile(file, userId).map(fileMapper::map);
    }


    @PutMapping("/{id}")
    public Mono<FileDTO> updateFileById(@PathVariable Integer id, @RequestBody FileDTO fileDTO) {
        FileEntity file = fileMapper.map(fileDTO);
        return fileService.updateFileById(file, id).map(fileMapper::map);
    }

    @DeleteMapping("/{id}")
    public Mono<FileDTO> deleteFileById(@PathVariable Integer id) {
        return fileService.deleteFileById(id).map(fileMapper::map);
    }

    @GetMapping("/history")
    public Flux<HistoryDTO> getHistory(Authentication authentication) {
        CustomPrincipal userDetails = (CustomPrincipal) authentication.getPrincipal();
        Integer userId = userDetails.getId();
        return fileUserService.getHistory(authentication.getAuthorities(), userId);
    }

}
