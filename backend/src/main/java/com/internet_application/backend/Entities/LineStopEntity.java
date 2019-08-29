package com.internet_application.backend.Entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.internet_application.backend.Serializers.LineStopSerializer;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.Date;

/*
    @MapsId
    Designates a ManyToOne or OneToOne relationship attribute that provides the mapping for an EmbeddedId primary key,
    an attribute within an EmbeddedId primary key, or a simple primary key of the parent entity.
    The value element specifies the attribute within a composite key to which the relationship attribute corresponds.
    If the entity's primary key is of the same Java type as the primary key of the entity referenced by the relationship,
    the value attribute is not specified.
*/

@Entity
@Table(name = "busline_stop")
@JsonSerialize(using = LineStopSerializer.class)
@NamedQuery(name = "LineStopEntity.findAllWithIdLine",
        query = "SELECT c FROM LineStopEntity c WHERE c.line.id = :id")
@NamedQuery(name="LineStopEntity.findAllWithIdLineAndDirection",
        query = "SELECT c FROM LineStopEntity c WHERE c.line.id = :id AND c.direction = :dir")
public class LineStopEntity {

    @Id
    @JsonProperty("id_busline_stop")
    @Column(name="id_busline_stop")
    @Getter
    @Setter
    private Long id;

    @ManyToOne
    @JsonProperty("id_line")
    @JoinColumn(name = "id_line")
    private BusLineEntity line;

    @ManyToOne
    @JsonProperty("id_stop")
    @JoinColumn(name = "id_stop")
    @JsonBackReference(value = "stop")
    private StopEntity stop;

    @JsonProperty("arrival_time")
    @Column(name="arrival_time")
    @Type(type = "time")
    @Getter
    @Setter
    private Date arrivalTime;

    @Column
    @Getter
    @Setter
    private Boolean direction; // 0 - Forth / 1 - Back

    public BusLineEntity getLine() {
        return line;
    }

    public void setLine(BusLineEntity line) {
        this.line = line;
    }

    public StopEntity getStop() {
        return stop;
    }

    public void setStop(StopEntity stop) {
        this.stop = stop;
    }
}