package co.fullstacklabs.cuboid.challenge.controller;

import co.fullstacklabs.cuboid.challenge.dto.CuboidDTO;
import co.fullstacklabs.cuboid.challenge.dto.CuboidUpdateDTO;
import co.fullstacklabs.cuboid.challenge.service.CuboidService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * @author FullStack Labs
 * @version 1.0
 * @since 2021-10-22
 */
@RestController
@RequestMapping(value = "/cuboids", produces = MediaType.APPLICATION_JSON_VALUE)
@Validated
public class CuboidController {

    private final CuboidService service;

    @Autowired
    public CuboidController(CuboidService service) {
        this.service = service;
    }

    @PostMapping()
    public ResponseEntity<CuboidDTO> create(@Valid @RequestBody final CuboidDTO cuboidDTO) {
        CuboidDTO cuboid = service.create(cuboidDTO);
        return new ResponseEntity<>(cuboid, HttpStatus.CREATED);
    }

    @GetMapping
    public List<CuboidDTO> getAll() {
        return service.getAll();
    }

    @PutMapping()
    public ResponseEntity<CuboidDTO> update(@Valid @RequestBody final CuboidUpdateDTO cuboidDTO) {
        CuboidDTO cuboid = service.update(cuboidDTO);
        return new ResponseEntity<>(cuboid, HttpStatus.OK);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") long bagId) {
        service.delete(bagId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
