package co.fullstacklabs.cuboid.challenge.dto;

import lombok.*;

import javax.validation.constraints.NotNull;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CuboidDTO {
    private Long id;

    @NotNull(message = "Cuboid width can't be null.")
    // @PositiveOrZero(message = "The width should be greater than zero.")
    private Float width;

    @NotNull(message = "Cuboid height can't be null.")
    // @PositiveOrZero(message = "The height should be greater than zero.")
    private Float height;

    @NotNull(message = "Cuboid depth can't be null.")
    // @PositiveOrZero(message = "The depth should be greater than zero.")
    private Float depth;

    @Deprecated
    private Double volume;

    @NotNull(message = "Cuboid related bag can't be null.")
    private Long bagId;

    public Double getVolume() {
        return this.depth.doubleValue() * this.width.doubleValue() * this.height.doubleValue();
    }

}
