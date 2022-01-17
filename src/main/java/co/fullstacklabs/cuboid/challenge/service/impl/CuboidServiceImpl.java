package co.fullstacklabs.cuboid.challenge.service.impl;

import co.fullstacklabs.cuboid.challenge.dto.BagDTO;
import co.fullstacklabs.cuboid.challenge.dto.CuboidDTO;
import co.fullstacklabs.cuboid.challenge.exception.ResourceNotFoundException;
import co.fullstacklabs.cuboid.challenge.exception.UnprocessableEntityException;
import co.fullstacklabs.cuboid.challenge.model.Bag;
import co.fullstacklabs.cuboid.challenge.model.Cuboid;
import co.fullstacklabs.cuboid.challenge.repository.BagRepository;
import co.fullstacklabs.cuboid.challenge.repository.CuboidRepository;
import co.fullstacklabs.cuboid.challenge.service.BagService;
import co.fullstacklabs.cuboid.challenge.service.CuboidService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Implementation class for BagService
 *
 * @author FullStack Labs
 * @version 1.0
 * @since 2021-10-22
 */
@Service
public class CuboidServiceImpl implements CuboidService {
    @Autowired
    private CuboidRepository repository;

    @Autowired
    private BagRepository bagRepository;

    @Autowired
    private BagService bagService;

    @Autowired
    private ModelMapper mapper;

    /**
     * Create a new cuboid and add it to its bag checking the bag available
     * capacity.
     *
     * @param cuboidDTO DTO with cuboid properties to be persisted
     * @return CuboidDTO with the data created
     */
    @Override
    @Transactional
    public CuboidDTO create(CuboidDTO cuboidDTO) {
        Bag bag = getBagById(cuboidDTO.getBagId());
        Cuboid cuboid = mapper.map(cuboidDTO, Cuboid.class);
        cuboid.setBag(bag);
        cuboid = repository.save(cuboid);
        return mapper.map(cuboid, CuboidDTO.class);
    }

    /**
     * List all cuboids
     * 
     * @return List<CuboidDTO>
     */
    @Override
    @Transactional(readOnly = true)
    public List<CuboidDTO> getAll() {
        List<Cuboid> cuboids = repository.findAll();
        return cuboids.stream().map(bag -> mapper.map(bag, CuboidDTO.class))
                .collect(Collectors.toList());
    }

    private Bag getBagById(long bagId) {
        return bagRepository.findById(bagId).orElseThrow(() -> new ResourceNotFoundException("Bag not found"));
    }

    @Override
    public CuboidDTO update(CuboidDTO cuboid) {
        if (repository.existsById(cuboid.getId())
                && this.bagRepository.existsById(cuboid.getBagId())) {

            BagDTO bag = this.bagService.findById(cuboid.getBagId().longValue());

            Double oldCuboidVolume = 0d;

            Optional<Double> oldCuboidVolumeOpt = bag.getCuboids().stream()
                    .filter(a -> 0 == Long.compare(cuboid.getId(), a.getId()))
                    .map(CuboidDTO::getVolume).findFirst();

            if (oldCuboidVolumeOpt.isPresent()) {
                oldCuboidVolume = oldCuboidVolumeOpt.get();
            }

            Double newPayloadVolume = bag.getPayloadVolume() - oldCuboidVolume;
            Double newAvailableVolume = bag.getVolume() - newPayloadVolume;

            if (newAvailableVolume >= cuboid.getVolume()) {
                Cuboid cuboidToPersist = mapper.map(cuboid, Cuboid.class);
                cuboidToPersist.setBag(mapper.map(bag, Bag.class));

                return mapper.map(this.repository.save(cuboidToPersist), CuboidDTO.class);
            } else {
                throw new UnprocessableEntityException("The cuboid doesn't fit");
            }

        } else {
            throw new ResourceNotFoundException("Cuboid not found");
        }
    }

    @Override
    public void delete(Long cuboidId) {
        if (repository.existsById(cuboidId)) {
            repository.deleteById(cuboidId);
        } else {
            throw new ResourceNotFoundException("Cuboid not found");
        }
    }

}
