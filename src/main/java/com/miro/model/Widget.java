package com.miro.model;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.Instant;

@Getter
@Setter
@ToString
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode
public class Widget {
    private static final String SEQUENCE_NAME = "widgets_id_seq";
    @Id
    @GeneratedValue(generator = SEQUENCE_NAME, strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = SEQUENCE_NAME, sequenceName = SEQUENCE_NAME, allocationSize = 1)
    @Column(name = "id", nullable = false)
    Long id;

    @NotNull
    @Column(unique = true)
    Integer zIndex;

    @NotNull
    Integer x;

    @NotNull
    Integer y;

    @NotNull
    Integer width;

    @NotNull
    Integer height;

    @NotNull
    Instant lastModificationTs;
}