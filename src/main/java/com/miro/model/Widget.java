package com.miro.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.Instant;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Widget {
    private static final String SEQUENCE_NAME = "widgets_id_seq";

    @Id
    @GeneratedValue(generator = SEQUENCE_NAME, strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = SEQUENCE_NAME, sequenceName = SEQUENCE_NAME, allocationSize = 1, initialValue = 0)
    @Column(name = "id", nullable = false)
    Long id;

    Integer zIndex;

    Integer x;

    Integer y;

    Integer width;

    Integer height;

    Instant lastModificationTs;
}
