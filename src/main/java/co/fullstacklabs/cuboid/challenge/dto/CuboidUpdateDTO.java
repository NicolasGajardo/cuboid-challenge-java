package co.fullstacklabs.cuboid.challenge.dto;

import lombok.*;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CuboidUpdateDTO extends CuboidDTO {

    @NotNull(message = "Cuboid width can't be null.")
    private Long id;

}
