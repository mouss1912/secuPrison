package fr.stazi.epsi.spring.boot.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import fr.stazi.epsi.spring.boot.entity.Cell;
import fr.stazi.epsi.spring.boot.exception.AlreadyExistsException;
import fr.stazi.epsi.spring.boot.exception.NotFoundException;
import fr.stazi.epsi.spring.boot.repository.CellRepository;

@RestController
@RequestMapping("/api/cells")
public class CellController {
	@Autowired
	private CellRepository cellRepository;
	
	
	@GetMapping
	public List<Cell> getCells() {
		return cellRepository.findAll();
	}
	
	@GetMapping("/{id}")
	public Cell getCell(@PathVariable Long id) {
		if(cellRepository.findById(id).isPresent())
			return cellRepository.findById(id).get();
		else return null;
	}
	
	@PreAuthorize("hasAuthority('CELL_CREATE')")
	@PostMapping
	public Cell createCell(@RequestBody Cell cell) throws AlreadyExistsException {
		if (cell.getId() == null) {
			return cellRepository.save(cell);
		} 	
		throw new AlreadyExistsException();
	}
	
	
	@PreAuthorize("@securityMethodsService.canManage(#id, principal)")
	@PutMapping("/{id}")
	public Cell updateCell(@PathVariable Long id, @RequestBody Cell cell) throws NotFoundException {
		Optional<Cell> existingCell = cellRepository.findById(id);
		if (existingCell.isPresent()) {
			cell.setId(id);
			return cellRepository.save(cell);
		}
		
		throw new NotFoundException();
	}
	
	@PreAuthorize("hasAuthority('CELL_DELETE')")
	@DeleteMapping("/{id}")
	public void deleteCell(@PathVariable Long id) throws NotFoundException {
		Cell existingCell = cellRepository.findById(id).orElseThrow(() -> new NotFoundException());
		cellRepository.delete(existingCell);
	}
}
