package com.computershop.controllers;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.computershop.dao.Manufacture;
import com.computershop.dto.ManufactureDTO;
import com.computershop.exceptions.DuplicateException;
import com.computershop.exceptions.NotFoundException;
import com.computershop.helpers.ConvertObject;
import com.computershop.repositories.ManufactureRepository;


@RestController
@RequestMapping(value = "/api/manufactures")
public class ManufactureController {
	@Autowired
	private ManufactureRepository manufactureRepos;
	
	@GetMapping
	@PreAuthorize("@authorizeService.authorizeAdmin(authentication, 'ADMIN')")
	public ResponseEntity<?> getAllManufactures() {	
		List<Manufacture> manufactues = manufactureRepos.findAll();
        if (manufactues.size() == 0) 
            return ResponseEntity.noContent().build();
        return ResponseEntity.ok().body(manufactues);
		
	}
	
	
	@GetMapping("/{manufactureId}")
    @PreAuthorize("@authorizeService.authorizeGetUserById(authentication, 'ADMIN', #userId)")
    public ResponseEntity<?> getUserById(@PathVariable("manufactureId") Long manufactureId) {
        Optional<Manufacture> manufacture = manufactureRepos.findById(manufactureId);
        if (!manufacture.isPresent()) 
            throw new NotFoundException("Manufacture not found");
        return ResponseEntity.ok().body(manufacture);
    }
	
	
    @PostMapping
    @PreAuthorize("@authorizeService.authorizeAdmin(authentication, 'ADMIN')")
    public ResponseEntity<?> createNewManufacture(@RequestBody ManufactureDTO manufactureDTO) {
		Manufacture oldManufacture = manufactureRepos.findByManufactureName(manufactureDTO.getName());
		if (oldManufacture != null) 
		    throw new DuplicateException("Manufacture has already exists");
		        
		Manufacture manufacture = ConvertObject.fromManufactureDTOToDAO(manufactureDTO);
		manufactureRepos.save(manufacture);
		return ResponseEntity.status(HttpStatus.CREATED).body(manufacture);
    }
    
    @PostMapping("maufactures-collection")
    @PreAuthorize("@authorizeService.authorizeAdmin(authentication, 'ADMIN')")
    public ResponseEntity<?> createNewManufactures(@RequestBody List<ManufactureDTO> manufactureDTO) {
    	List<Manufacture> listManufactures = new LinkedList<Manufacture>();
    	
    	for(int i = 0; i < manufactureDTO.size(); i ++) {
    		
    		Manufacture oldManufacture = manufactureRepos.findByManufactureName(manufactureDTO.get(i).getName());
    		if (oldManufacture != null) 
    			throw new DuplicateException("Manufacture has already exists");
    		
    		Manufacture manufacture = ConvertObject.fromManufactureDTOToDAO(manufactureDTO.get(i));
    		listManufactures.add(manufacture);
    	}
    	
		
		
		if(listManufactures.size()==0)
			return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
		manufactureRepos.saveAll(listManufactures);
		return ResponseEntity.status(HttpStatus.CREATED).body(listManufactures);
    }
    

    @PatchMapping("/{manufactureId}")
    @PreAuthorize("@authorizeService.authorizeAdmin(authentication, 'ADMIN')")
    public ResponseEntity<?> editManufacture(@RequestBody ManufactureDTO manufactureDTO, @PathVariable("manufactureId") Long manufactureId) {
        Optional<Manufacture> optional = manufactureRepos.findByManufactureId(manufactureId);
        if (!optional.isPresent()) {
            throw new NotFoundException("Manufacture not found");
        }
        Manufacture manufacture = optional.get();

        if (manufactureDTO.getName() != null) {
        	manufacture.setManufactureName(manufactureDTO.getName());
        }
        if (manufactureDTO.getNation() != null) {
        	manufacture.setNation(manufactureDTO.getNation());
        }

        manufactureRepos.save(manufacture);

        return ResponseEntity.status(HttpStatus.OK).body(manufacture);
    }


}
